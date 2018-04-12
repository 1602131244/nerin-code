package com.nerin.nims.opt.app.security;

import com.nerin.nims.opt.app.config.Constants;
import com.nerin.nims.opt.app.service.util.LdapService;
import com.nerin.nims.opt.base.rest.LdapUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private LdapService ldapService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
        throws IOException, ServletException {
        String ldapName = request.getParameter("j_username");

        LdapUser ldapUser = ldapService.getLdapUser(ldapName);

        // 等接口提供了，就调用接口去Ladp数据，现在先new
//        LdapUser ldapUser = new LdapUser();
//        ldapUser.setUserId(1l);
        request.getSession().setAttribute("ldapUser", ldapUser);

        //if form login , redirect to home page
        if (Constants.LOGIN_TYPE_FORM.equals(request.getParameter("loginType"))){
          response.sendRedirect("/");
        }else {
          response.setStatus(HttpServletResponse.SC_OK);
        }

    }
}
