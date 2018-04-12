package com.nerin.nims.opt.bbsLogin.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.rest.LdapUser;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.bbsLogin.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by 104528 on 2017/9/26.
 * discuz login demo
 */
@RestController
@RequestMapping("/api/bbsLogin")
public class UcRestController {

    /**
     * @param request 请求
     * @return string
     * 从session中提取email，向bbs UCenter请求email登录，返回登录地址
     * <script type="text/javascript" src="http://localhost/api/uc.php?time=1408327309&code=bc6bFLa6WH343ni" reload="1"></script>
     * 将该dom元素插入页面自动访问获取bbs cookie，再跳转至任意bbs url即可
     */
    @RequestMapping(value = "getLoginScript", method = RequestMethod.GET)
    public Map getBBSLoginScript(HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        LdapUser user = SessionUtil.getLdapUserInfo(request);
        Client uc = new Client();

        String ucSynLogin = uc.uc_user_synlogin_by_email(user.getEmail());

        System.out.println(ucSynLogin);
        restFulData.put("script", ucSynLogin);
        return restFulData;
    }
}
