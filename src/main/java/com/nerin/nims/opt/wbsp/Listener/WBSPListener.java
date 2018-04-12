package com.nerin.nims.opt.wbsp.Listener;

import com.nerin.nims.opt.wbsp.service.PaLevTwoPlanService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Created by Administrator on 2016/9/7.
 */
@WebListener
public class WBSPListener implements HttpSessionListener {
    @Autowired
    PaLevTwoPlanService paLevTwoPlanService;
    @Override
    public void sessionCreated(HttpSessionEvent se) {
       System.out.println("Session 被创建");
       System.out.println("SessionID:"+se.getSession().getId());
        paLevTwoPlanService.saveSession(se.getSession().getId());
      //  System.out.println("Data:"+se.getSession().getCreationTime());
      //  System.out.println("Data:"+se.getSession().getLastAccessedTime());
      //  System.out.println("UserID:"+se.getSession().getAttribute("ldapUser"));
        }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("Session 被关闭");
        System.out.println("SessionID:"+se.getSession().getId());
        paLevTwoPlanService.deleteSession(se.getSession().getId());
    }
}
