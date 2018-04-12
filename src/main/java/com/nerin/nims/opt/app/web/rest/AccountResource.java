package com.nerin.nims.opt.app.web.rest;

import com.nerin.nims.opt.app.security.SecurityUtils;
import com.nerin.nims.opt.app.service.UserService;
import com.nerin.nims.opt.app.service.util.LdapService;
import com.nerin.nims.opt.app.web.rest.dto.ManagedUserDTO;
import com.nerin.nims.opt.app.web.rest.dto.UserDTO;
import com.nerin.nims.opt.app.web.rest.util.HeaderUtil;
import com.nerin.nims.opt.app.domain.User;
import com.nerin.nims.opt.app.repository.UserRepository;
import com.nerin.nims.opt.base.rest.LdapUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Autowired
    private LdapService ldapService;

    @RequestMapping(value = "/ldapUser", method = RequestMethod.GET)
    public LdapUser queryLadpUserByNo(@RequestParam(value = "ldapNo", required = false, defaultValue = "") String ldapNo) {
        return ldapService.getLdapUser(ldapNo);
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request
     * @return the login if the user is authenticated
     */
    @CrossOrigin(origins = "http://localhost:9000")
    @RequestMapping(value = "/authenticate",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public Map isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated:"+request.getRemoteUser());
        Map<String,String> authentication = new HashMap<>();
        authentication.put("currentUser",request.getRemoteUser());
        return authentication;
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the current user in body, or status 500 (Internal Server Error) if the user couldn't be returned
     */
    @RequestMapping(value = "/account",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getAccount() {
        return Optional.ofNullable(userService.getUserWithAuthorities())
            .map(user -> new ResponseEntity<>(new UserDTO(user), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO the current user information
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) or 500 (Internal Server Error) if the user couldn't be updated
     */
    @RequestMapping(value = "/account",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveAccount(@Valid @RequestBody UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findOneByEmail(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userDTO.getLogin()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("user-management", "emailexists", "Email already in use")).body(null);
        }
        return userRepository
            .findOneByLogin(SecurityUtils.getCurrentUserLogin())
            .map(u -> {
                userService.updateUserInformation(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
                    userDTO.getLangKey());
                return new ResponseEntity<String>(HttpStatus.OK);
            })
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    /**
     * POST  /account/change_password : changes the current user's password
     *
     * @param password the new password
     * @return the ResponseEntity with status 200 (OK), or status 400 (Bad Request) if the new password is not strong enough
     */
    @RequestMapping(value = "/account/change_password",
        method = RequestMethod.POST,
        produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody String password) {
        if (!checkPasswordLength(password)) {
            return new ResponseEntity<>("Incorrect password", HttpStatus.BAD_REQUEST);
        }
        userService.changePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private boolean checkPasswordLength(String password) {
        return (!StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserDTO.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserDTO.PASSWORD_MAX_LENGTH);
    }
}
