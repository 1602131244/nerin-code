package com.nerin.nims.opt.wh.api;


import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.wh.dto.*;
import com.nerin.nims.opt.wh.service.WHService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/wh")
public class WHController {
    @Autowired
    private WHService whService;

    /**
     * 查询工作包相关信息
     */
    @RequestMapping(value = "getWhInfo", method = RequestMethod.GET)
    public GetWHL1DTO getWhInfo(@RequestParam(value = "currDate", required = true, defaultValue = "") String currDate,
                                @RequestParam(value = "perId", required = false, defaultValue = "0") long perId,
                                HttpServletRequest request) {
        if (0 == perId)
            perId = SessionUtil.getLdapUserInfo(request).getPersonId();
        return whService.getWHInfo(currDate, perId);
    }


    /**
     * 按项目获取工作包
     */
    @RequestMapping(value = "getProjInfo", method = RequestMethod.GET)
    public DataTablesDTO getProjInfo(@RequestParam(value = "currDate", required = true, defaultValue = "") String currDate,
                                     @RequestParam(value = "perId", required = false, defaultValue = "0") long perId,
                                     @RequestParam(value = "page", required = true, defaultValue = "1") long page,
                                     @RequestParam(value = "rows", required = true, defaultValue = "5") long rows,
                                     @RequestParam(value = "keywords", required = false, defaultValue = "") String keywords,
                                     @RequestParam(value = "projType", required = false, defaultValue = "") String projType,
                                     @RequestParam(value = "flag", required = false, defaultValue = "Y") String flag,
                                     HttpServletRequest request) {
        if (0 == perId)
            perId = SessionUtil.getLdapUserInfo(request).getPersonId();
        return whService.getProject(currDate, perId, keywords, projType, page, rows, flag);
    }

    /**
     * 执行保存
     *
     * @param dataSaveDTOS
     * @param perId
     * @param request
     * @return
     */
    @RequestMapping(value = "saveDlvr", method = RequestMethod.POST)
    public Map saveDlvr(@RequestBody ArrayList<DataSaveDTO> dataSaveDTOS,
                        @RequestParam(value = "perId", required = false, defaultValue = "0") long perId,
                        HttpServletRequest request) {
        if (0 == perId)
            perId = SessionUtil.getLdapUserInfo(request).getPersonId();
        Map tmp = new HashMap();
        Map result = new HashMap();
        String status = "Success";
        String errMsg = "";
        for (int i = 0; i < dataSaveDTOS.size(); i++) {
            tmp = whService.saveWH(dataSaveDTOS.get(i), perId);
            if (!("Success".equals(String.valueOf(tmp.get("Status"))))) {
                status = "Error";
                errMsg = errMsg+";"+ dataSaveDTOS.get(i).getDate()+String.valueOf(tmp.get("ErrMsg"));
            }
            result.put("Status",status);
            result.put("ErrMsg",errMsg);
        }
        return result;
    }
}


