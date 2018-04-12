package com.nerin.nims.opt.app.web.rest;


import com.nerin.nims.opt.app.config.Constants;
import com.nerin.nims.opt.app.domain.Authority;
import com.nerin.nims.opt.app.security.AuthoritiesConstants;
import com.nerin.nims.opt.app.security.SecurityUtils;
import com.nerin.nims.opt.app.service.UserService;
import com.nerin.nims.opt.app.web.rest.dto.DataTableDTO;
import com.nerin.nims.opt.app.web.rest.dto.FancyTreeDTO;
import com.nerin.nims.opt.app.web.rest.dto.UserNewDTO;
import com.nerin.nims.opt.app.web.rest.util.HeaderUtil;
import com.nerin.nims.opt.app.web.rest.util.PaginationUtil;
import com.nerin.nims.opt.app.domain.User;
import com.nerin.nims.opt.app.repository.AuthorityRepository;
import com.nerin.nims.opt.app.repository.UserRepository;
import com.nerin.nims.opt.app.web.rest.dto.ManagedUserDTO;
import com.nerin.nims.opt.base.rest.LdapUser;
import com.nerin.nims.opt.base.util.BeanCopy;
import com.nerin.nims.opt.base.util.SessionUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing users.
 *
 * <p>This class accesses the User entity, and needs to fetch its collection of authorities.</p>
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * </p>
 * <p>
 * We use a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>Another option would be to have a specific JPA entity graph to handle this case.</p>
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

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

    @RequestMapping(value = "/isLogon", method = RequestMethod.GET)
    public String isLogon(HttpServletRequest request) {
        LdapUser user = SessionUtil.getLdapUserInfo(request);
        String loginName = SecurityUtils.getCurrentUserLogin();
        if (null != loginName && !"anonymousUser".equals(loginName)) {
            if (this.isInteger(loginName.substring(0, 1))) {
                if (0 == user.getUserId())
                    return "false";
                else
                    return "true";
            } else {
                 return "true";
            }
        } else {
            if (0 == user.getUserId())
                return "false";
            else
                return "true";
        }
    }

    @RequestMapping(value = "/logonUser", method = RequestMethod.GET)
    public LdapUser ldapUser(HttpServletRequest request) {
        LdapUser user = SessionUtil.getLdapUserInfo(request);
        return user;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     * </p>
     *
     * @param managedUserDTO the user to create
     * @param request the HTTP request
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntaxt is incorrect
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<?> createUser(@RequestBody ManagedUserDTO managedUserDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save User : {}", managedUserDTO);

        //Lowercase the user login before comparing with database
        if (userRepository.findOneByLogin(managedUserDTO.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(managedUserDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "Email already in use"))
                .body(null);
        } else {
            User newUser = userService.createUser(managedUserDTO);
            String baseUrl = request.getScheme() + // "http"
            "://" +                                // "://"
            request.getServerName() +              // "myhost"
            ":" +                                  // ":"
            request.getServerPort() +              // "80"
            request.getContextPath();              // "/myContextPath" or "" if deployed in root context

            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert( "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * PUT  /users : Updates an existing User.
     *
     * @param managedUserDTO the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldnt be updated
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<ManagedUserDTO> updateUser(@RequestBody ManagedUserDTO managedUserDTO) {
        log.debug("REST request to update User : {}", managedUserDTO);
        Optional<User> existingUser = userRepository.findOneByEmail(managedUserDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserDTO.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "emailexists", "E-mail already in use")).body(null);
        }
        existingUser = userRepository.findOneByLogin(managedUserDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserDTO.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userManagement", "userexists", "Login already in use")).body(null);
        }
        return userRepository
            .findOneById(managedUserDTO.getId())
            .map(user -> {
                user.setLogin(managedUserDTO.getLogin());
                user.setFirstName(managedUserDTO.getFirstName());
                user.setLastName(managedUserDTO.getLastName());
                user.setEmail(managedUserDTO.getEmail());
                user.setActivated(managedUserDTO.isActivated());
                user.setLangKey(managedUserDTO.getLangKey());
                Set<Authority> authorities = user.getAuthorities();
                authorities.clear();
                managedUserDTO.getAuthorities().stream().forEach(
                    authority -> authorities.add(authorityRepository.findOne(authority))
                );
                return ResponseEntity.ok()
                    .headers(HeaderUtil.createAlert("userManagement.updated", managedUserDTO.getLogin()))
                    .body(new ManagedUserDTO(userRepository
                        .findOne(managedUserDTO.getId())));
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));

    }

    /**
     * GET  /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     * @throws URISyntaxException if the pagination headers couldnt be generated
     */
    @RequestMapping(value = "/users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional(readOnly = true)
    public ResponseEntity<List<ManagedUserDTO>> getAllUsers(Pageable pageable)
        throws URISyntaxException {
        Page<User> page = userRepository.findAll(pageable);
        List<ManagedUserDTO> managedUserDTOs = page.getContent().stream()
            .map(ManagedUserDTO::new)
            .collect(Collectors.toList());
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(managedUserDTOs, headers, HttpStatus.OK);
    }

    /**
     * GET  /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/users/{login:" + Constants.LOGIN_REGEX + "}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedUserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return userService.getUserWithAuthoritiesByLogin(login)
                .map(ManagedUserDTO::new)
                .map(managedUserDTO -> new ResponseEntity<>(managedUserDTO, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    /**
     * DELETE  USER :login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/users/{login:" + Constants.LOGIN_REGEX + "}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUserInformation(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
    }

    @RequestMapping(value = "/management/queryUserList", method = RequestMethod.GET)
    public DataTableDTO queryUserList(@RequestParam(value="pageSize", required=false, defaultValue="1000") int pageSize) {
        Pageable pageRequest = buildPageRequest(1, pageSize);
        DataTableDTO table = new DataTableDTO();
        Page<User> data = userService.getUserForAll(pageRequest);
        List<UserNewDTO> newData = data.getContent().stream().map(user -> {
            UserNewDTO dto = new UserNewDTO();
            try {
                BeanUtils.copyProperties(dto, user);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return dto;
        }).collect(Collectors.toList());
        table.setDraw(1);
        table.setRecordsTotal(data.getTotalElements());
        table.setRecordsFiltered(data.getTotalElements());
        table.setDataSource(newData);
        return table;
    }

    @RequestMapping(value = "/management/createOrUpdateUser", method = RequestMethod.POST)
    public String createOrUpdateUserInfo(@Valid @RequestBody UserNewDTO userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream().map(fieldError -> {
                return fieldError.getField() + "," + fieldError.getDefaultMessage();
            }).collect(Collectors.toList());
            return "false";
        }

        User user = null;
        if (null == userDto.getId()) {
            userDto.setId(null);
            user = new User();
        } else {
            user = userService.getUserInfoById(userDto.getId());
        }
        try {
            BeanCopy.copyProperties(userDto, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userService.createNewUser(user);
        return "true";
    }

    @RequestMapping(value = "/management/editUserRoles", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public String editUserRoles(@RequestParam(value = "rid", required = false) Long id, @RequestParam(value = "role", required = false) String[] roles) {
        User tmp = userService.getUserInfoById(id);
        Set<Authority> authorities = new HashSet<>();
        Authority authority = null;
        for (String r : roles) {
            authority = new Authority();
            authority.setName(r);
            authorities.add(authority);
        }
        tmp.setAuthorities(authorities);
        userService.updateUser(tmp);
        return "true";
    }

    @RequestMapping(value = "/management/queryUserInfo/{id}", method = RequestMethod.GET, produces={"application/xml", "application/json"})
    @ResponseStatus(HttpStatus.OK)
    public UserNewDTO queryUserInfo(@PathVariable(value = "id") Long id) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        User tmp = userService.getUserInfoById(id);
        UserNewDTO dto = new UserNewDTO();
        BeanUtils.copyProperties(dto, tmp);
        return dto;
    }

    @RequestMapping(value = "/management/deleteUserInfo/{ids}", method = RequestMethod.POST)
    public String deleteUser(@PathVariable Long[] ids) {
        for (Long l : ids)
            userService.deleteUser(l);
        return "true";
    }

    @RequestMapping(value = "/management/queryAuthorityAll", method = RequestMethod.GET)
    public List<Authority> queryAuthorityAll() {
        return userService.getAuthorityAll();
    }

    @RequestMapping(value = "/management/queryFancyTree", method = RequestMethod.GET)
    public List<FancyTreeDTO> queryFancyTree(@RequestParam(value = "key", required = false, defaultValue = "-1") Long parentId) {
        List<FancyTreeDTO> allTreeData = new ArrayList<FancyTreeDTO>();
        if (1 == parentId) {
            FancyTreeDTO fancyTreeDTO = new FancyTreeDTO();
            fancyTreeDTO.setTitle("titleSide1");
            fancyTreeDTO.setKey("10");
            allTreeData.add(fancyTreeDTO);
        } else {
            FancyTreeDTO fancyTreeDTO = new FancyTreeDTO();
            fancyTreeDTO.setTitle("otherSide1");
            fancyTreeDTO.setKey("20");
            allTreeData.add(fancyTreeDTO);
            fancyTreeDTO = new FancyTreeDTO();
            fancyTreeDTO.setTitle("otherSide2");
            fancyTreeDTO.setKey("21");
            allTreeData.add(fancyTreeDTO);
        }
        return allTreeData;
    }

    /**
     * 创建分页请求.
     */
    private PageRequest buildPageRequest(int pageNumber, int pagzSize) {
        Sort sort = null;
        return new PageRequest(pageNumber - 1, pagzSize, sort);
    }
}
