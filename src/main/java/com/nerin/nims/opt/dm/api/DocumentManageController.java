package com.nerin.nims.opt.dm.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.dm.dto.DocumentManageDto;
import com.nerin.nims.opt.dm.service.DocumentManageService;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/13.
 */
@RestController
@RequestMapping("/api/documentManage")
public class DocumentManageController {

    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    public DocumentManageService documentManageService;


    @RequestMapping(value = "saveDocumentManage", method = RequestMethod.POST)
    public Map saveDocumentManage(@RequestBody List<DocumentManageDto> documentManageDtos, HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        //long userId = 1336;
        Map reMap = documentManageService.addDocumentManage(documentManageDtos, //userId
                    SessionUtil.getLdapUserInfo(request).getUserId()
                    );
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }

        return restFulData;
    }
}
