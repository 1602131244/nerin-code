package com.nerin.nims.opt.app.security;

import com.nerin.nims.opt.app.config.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring Security logout handler, specialized for Ajax requests.
 */
@Component
public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
    implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication)
        throws IOException, ServletException {
      if (Constants.LOGIN_TYPE_FORM.equals(request.getParameter("loginType"))){
        response.sendRedirect("/");
      }else {
        response.setStatus(HttpServletResponse.SC_OK);
      }

    }
}
