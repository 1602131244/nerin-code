package com.nerin.nims.opt.oie.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.oie.module.CheckAdUser;
import com.nerin.nims.opt.oie.service.OieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/27.
 */
@RestController
@RequestMapping("/api/oieRest")
public class OieRestController {

    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    public OieService oieService;

    /**
     * 更新OA流程的签收人、签收日期
     * @param requestId
     * @param request
     * @return
     */
    @RequestMapping(value = "updateOaRequestOie", method = RequestMethod.POST)
    public Map updateOaRequestOie(@RequestParam(value = "requestType", required = false, defaultValue = "") String requestType,
                                  @RequestParam(value = "requestId", required = false, defaultValue = "") Long requestId,
                                  @RequestParam(value = "personNumber", required = false, defaultValue = "") String personNumber,
                                  @RequestParam(value = "type", required = false, defaultValue = "RECEIVE") String type,
                                  HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = oieService.updateOaRequestOie (requestType,requestId, SessionUtil.getLdapUserInfo(request).getUserId(),personNumber,type);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /*
    验证AD域用户、密码
     */
    @RequestMapping(value = "checkAdUser", method = RequestMethod.POST)
    public CheckAdUser checkAdUser(@RequestParam(value = "username", required = false, defaultValue = "") String username,
                                   @RequestParam(value = "password", required = false, defaultValue = "") String password
                                   ) {
        CheckAdUser cau = new CheckAdUser();
        cau.connect(username,password);
//        cau.connect("101319@nims.com","123!@#qaz");
        return cau;
    }
}
