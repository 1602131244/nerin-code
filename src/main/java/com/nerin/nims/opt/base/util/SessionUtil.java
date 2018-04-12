package com.nerin.nims.opt.base.util;

import com.nerin.nims.opt.base.rest.LdapUser;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yinglgu on 6/24/2016.
 */
public class SessionUtil {

    public static LdapUser getLdapUserInfo(HttpServletRequest request) {
        Object o = request.getSession().getAttribute("ldapUser");
        if (null != o) {
            return (LdapUser) o;
        } else {
            LdapUser tmp = new LdapUser();
            tmp.setUserId(0l);
            return tmp;
        }

    }
}
