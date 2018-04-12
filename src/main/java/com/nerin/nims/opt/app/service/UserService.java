package com.nerin.nims.opt.app.service;

import com.nerin.nims.opt.app.config.Constants;
import com.nerin.nims.opt.app.domain.Authority;
import com.nerin.nims.opt.app.domain.LadpUserAuthority;
import com.nerin.nims.opt.app.domain.User;
import com.nerin.nims.opt.app.repository.AuthorityRepository;
import com.nerin.nims.opt.app.repository.UserRepository;
import com.nerin.nims.opt.app.security.SecurityUtils;
import com.nerin.nims.opt.app.web.rest.dto.ManagedUserDTO;
import com.nerin.nims.opt.app.service.util.RandomUtil;
import com.nerin.nims.opt.app.web.rest.dto.UserNewDTO;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Inject
    private AuthorityRepository authorityRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(new Date());
                userRepository.save(user);
                return user;
            });
    }

    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
        String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne("ROLE_USER");
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(ManagedUserDTO managedUserDTO) {
        User user = new User();
        user.setLogin(managedUserDTO.getLogin());
        user.setFirstName(managedUserDTO.getFirstName());
        user.setLastName(managedUserDTO.getLastName());
        user.setEmail(managedUserDTO.getEmail());
        if (managedUserDTO.getLangKey() == null) {
            user.setLangKey("zh"); // default language
        } else {
            user.setLangKey(managedUserDTO.getLangKey());
        }
        if (managedUserDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            managedUserDTO.getAuthorities().stream().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(new Date());
        user.setActivated(true);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    public void updateUserInformation(String firstName, String lastName, String email, String langKey) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);
            userRepository.save(u);
            log.debug("Changed Information for User: {}", u);
        });
    }

    public void deleteUserInformation(String login) {
        userRepository.findOneByLogin(login).ifPresent(u -> {
            userRepository.delete(u);
            log.debug("Deleted User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(u -> {
            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);
            userRepository.save(u);
            log.debug("Changed password for User: {}", u);
        });
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByLogin(login).map(u -> {
            u.getAuthorities().size();
            return u;
        });
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        User user = userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }


    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
//        ZonedDateTime now = ZonedDateTime.now();
//        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
//        for (User user : users) {
//            log.debug("Deleting not activated user {}", user.getLogin());
//            userRepository.delete(user);
//        }
    }

    public Page<User> getUserForAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User getUserInfoById(long id) {
         return userRepository.findOne(id);
    }

    public User createNewUser(User user) {
        if (null  == user.getId()) {
            String encryptedPassword = passwordEncoder.encode("111111");
            user.setPassword(encryptedPassword);
            user.setResetKey(RandomUtil.generateResetKey());
            user.setResetDate(new Date());
            user.setActivated(true);
        } else {

        }
        userRepository.save(user);
        return user;
    }

    public void deleteUser(long id) {
        userRepository.delete(id);
    }

    public List<Authority> getAuthorityAll() {
        return authorityRepository.findAll();
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public List<LadpUserAuthority> getLadpUserRoles(String name) {
        String sql = "select * from " + Constants.DB_PREFIX + ".NERIN_LADP_USER_AUTHORITY t where lower(t.LADP_USER) = ?";
        List<LadpUserAuthority> data = jdbcTemplate.query(sql, new Object[]{name}, new RowMapper<LadpUserAuthority>() {
            @Override
            public LadpUserAuthority mapRow(ResultSet resultSet, int i) throws SQLException {
                LadpUserAuthority user = new LadpUserAuthority();
                user.setLadpUser(resultSet.getString("LADP_USER"));
                user.setAuthorityName(resultSet.getString("AUTHORITY_NAME"));
                return user;
            }
        });
        return data;
    }
}
