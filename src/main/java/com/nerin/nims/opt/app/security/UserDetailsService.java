package com.nerin.nims.opt.app.security;

import com.nerin.nims.opt.app.domain.Authority;
import com.nerin.nims.opt.app.domain.LadpUserAuthority;
import com.nerin.nims.opt.app.domain.User;
import com.nerin.nims.opt.app.repository.UserRepository;
import com.nerin.nims.opt.app.service.UserService;
import com.nerin.nims.opt.app.service.util.DirectoryAutherntication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.naming.NamingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(UserDetailsService.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserService userService;

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<User> userFromDatabase = userRepository.findOneByLogin(lowercaseLogin);
        // 如果系统用户表没有查询到用户，则去Ad域查询
//        if (!userFromDatabase.isPresent()) {
//            try {
//                User ladpUser = new User();
//                Map map = DirectoryAutherntication.findLdapUserByUserName("n" + login);
//                if (null != map) {
//                    ladpUser.setLogin(login);
//                    ladpUser.setPassword(passwordEncoder.encode(login));
//                    List<LadpUserAuthority> roles = userService.getLadpUserRoles(ladpUser.getLogin());
//                    roles.stream().forEach(ladpUserAuthority -> {
//                        Authority tmp = new Authority();
//                        tmp.setName(ladpUserAuthority.getAuthorityName());
//                        ladpUser.getAuthorities().add(tmp);
//                    });
//                    userFromDatabase = Optional.of(ladpUser);
//                }
//            } catch (NamingException e) {
//                e.printStackTrace();
//            }
//        }
        if (this.isInteger(login.substring(0, 1))) {
            User ladpUser = new User();
            ladpUser.setLogin(login);
            ladpUser.setPassword(passwordEncoder.encode(login));
            List<LadpUserAuthority> roles = userService.getLadpUserRoles(ladpUser.getLogin());
            roles.stream().forEach(ladpUserAuthority -> {
                Authority tmp = new Authority();
                tmp.setName(ladpUserAuthority.getAuthorityName());
                ladpUser.getAuthorities().add(tmp);
            });
            userFromDatabase = Optional.of(ladpUser);
        }

//        this.log.error(userFromDatabase.get().getCreatedDate().getTime()+"");
        return userFromDatabase.map(user -> {
//            if (!user.getActivated()) {
//                throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
//            }
            List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
            return new org.springframework.security.core.userdetails.User(lowercaseLogin,
                user.getPassword(),
                grantedAuthorities);
        }).orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the " +
        "database"));
    }
}
