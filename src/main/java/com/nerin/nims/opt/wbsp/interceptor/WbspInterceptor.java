package com.nerin.nims.opt.wbsp.interceptor;

import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.wbsp.service.PaLevThreePlanService;
import com.nerin.nims.opt.wbsp.service.PaLevTwoPlanService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by 100096 on 2016/12/18.
 */
public class WbspInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    PaLevTwoPlanService paLevTwoPlanService;
    @Autowired
    PaLevThreePlanService paLevThreePlanService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        // String pathName =(String)request.getPathInfo();
        //  System.out.println("pathName:"+pathName);
        //  String contextPathName =(String)request.getContextPath();
        //  System.out.println("contextPathName:"+contextPathName);
        String requestURI = (String) request.getRequestURI();
        System.out.println("requestURI:" + requestURI);
        if (requestURI.equals("/api/lev2/queryWorkingStructure")) {
            HttpSession session = request.getSession();
            long projId = Long.parseLong(request.getParameter("projID"));
            long userId = SessionUtil.getLdapUserInfo(request).getUserId();
            System.out.println("userID:" + userId);
            String sessionId = request.getSession().getId();
            System.out.println("sessionId:" + sessionId);
            if (paLevTwoPlanService == null) {
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                paLevTwoPlanService = (PaLevTwoPlanService) factory.getBean("paLevTwoPlanService");
            }
            String isLockedByUser = paLevTwoPlanService.isLockedByUser(projId, userId);
            System.out.println("isLockedByUser:" + isLockedByUser);
            if (isLockedByUser.equals("Y")) {
                paLevTwoPlanService.lockStru(sessionId, userId, projId);//生成锁定的记录
            }

        }
        if (requestURI.equals("/api/lev3/enterProject")) {
            HttpSession session = request.getSession();
            long projId = Long.parseLong(request.getParameter("projID"));
            String phaseCode = request.getParameter("phaseID");
            long userId = SessionUtil.getLdapUserInfo(request).getUserId();
            long personId = SessionUtil.getLdapUserInfo(request).getPersonId();
            System.out.println("userID:" + userId);
            System.out.println("personID:" + personId);
            String sessionId = request.getSession().getId();
            System.out.println("sessionId:" + sessionId);
            System.out.println("phaseCode:" + phaseCode);
            if (paLevThreePlanService == null) {
                BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
                paLevThreePlanService = (PaLevThreePlanService) factory.getBean("paLevThreePlanService");
            }
            //Map result = paLevThreePlanService.saveLockSpec(userId,sessionId,projId,phaseCode);
            Map result = paLevThreePlanService.saveLockSpec(personId,sessionId,projId,phaseCode);
            System.out.println("result:" + result.get("result"));

        }
    }


}
