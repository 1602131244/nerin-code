package com.nerin.nims.opt.navi.api;

import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.navi.dto.OaWorkFlowDTO;
import com.nerin.nims.opt.navi.dto.ProjectPhaseDTO;
import com.nerin.nims.opt.navi.service.NaviIndexService;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by yinglgu on 7/15/2016.
 */
@RestController
@RequestMapping("/api/naviIndexRest")
public class NaviIndexRestController {

    @Autowired
    private NaviIndexService naviIndexService;

    @RequestMapping(value = "getOAurl", method = RequestMethod.GET)
    public Map getOAurl() {
        Map restFulData = RestFulData.getRestInitData();
        String url = naviIndexService.getOAurl();
        restFulData.put("db_url", url);
        restFulData.put("db_indexUrl", url.split("login")[0]);
        return restFulData;
    }

    @RequestMapping(value = "proJoinLogList", method = RequestMethod.GET)
    public DataTablesDTO queryProJoinLogList(HttpServletRequest request) {
        return naviIndexService.getProJoinLogList(SessionUtil.getLdapUserInfo(request).getUserId());
    }

    @RequestMapping(value = "overdueList", method = RequestMethod.GET)
    public DataTablesDTO queryOverdueList(@RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                             HttpServletRequest request) {
        return naviIndexService.getProOverdueList(SessionUtil.getLdapUserInfo(request).getUserId(), pageNo, pageSize);
    }
   //任务超期列表，新需求
    @RequestMapping(value = "overdueList1", method = RequestMethod.GET)
    public DataTablesDTO queryOverdueList1(@RequestParam(value = "userid", required = false, defaultValue = "1") long userid,
                                           @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                          HttpServletRequest request) {
        return naviIndexService.getProOverdueList1(SessionUtil.getLdapUserInfo(request).getUserId(), pageNo, pageSize);
    }


    @RequestMapping(value = "proMyAllList", method = RequestMethod.GET)
    public DataTablesDTO queryProMyAllList(@RequestParam(value = "taskTypeDesc", required = false, defaultValue = "") String taskTypeDesc,
                                             @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                             HttpServletRequest request) {
        return naviIndexService.getProMyAllList(SessionUtil.getLdapUserInfo(request).getUserId(), taskTypeDesc, pageNo, pageSize);
    }

    @RequestMapping(value = "proPhaseList", method = RequestMethod.GET)
    public List<ProjectPhaseDTO> queryProPhaseList(@RequestParam(value = "proId", required = false, defaultValue = "") Long proId) {
        return naviIndexService.getProPhaseList(proId);
    }

    @RequestMapping(value = "visitPro", method = RequestMethod.POST)
    public String doVisitPro(@RequestParam(value = "proId", required = false, defaultValue = "") Long proId,
                                            @RequestParam(value = "proNum", required = false, defaultValue = "") String proNum,
                                            @RequestParam(value = "proManager", required = false, defaultValue = "") String proManager,
                                            @RequestParam(value = "proName", required = false, defaultValue = "") String proName,
                                            HttpServletRequest request) {
        naviIndexService.insertVisitPro(SessionUtil.getLdapUserInfo(request).getUserId(), proId, proNum, proManager, proName);
        return "1";
    }

    @RequestMapping(value = "getOaOverAll", method = RequestMethod.GET)
    public List<OaWorkFlowDTO> getOaOverAll(@RequestParam(value = "xmbh", required = false, defaultValue = "") String xmbh,
                                            @RequestParam(value = "id", required = false, defaultValue = "") Long id,
                                            @RequestParam(value = "formId", required = false, defaultValue = "") Long formId,
                                            @RequestParam(value = "status", required = false, defaultValue = "") String status) {
        return naviIndexService.getOaOverAll(xmbh, id, formId, status);
    }

    @RequestMapping(value = "getBuildingInstitutePro", method = RequestMethod.GET)
    public List<OaWorkFlowDTO> getBuildingInstitutePro(@RequestParam(value = "xmbh", required = false, defaultValue = "") String xmbh,
                                            @RequestParam(value = "id", required = false, defaultValue = "") Long id,
                                            @RequestParam(value = "formId", required = false, defaultValue = "") Long formId,
                                            @RequestParam(value = "status", required = false, defaultValue = "") String status,
                                            @RequestParam(value = "zy", required = false, defaultValue = "") String zy) {
        return naviIndexService.getBuildingInstitutePro(xmbh, id, formId, zy, status);
    }

    @RequestMapping(value = "getBuildingInstitutePro23", method = RequestMethod.GET)
    public List<OaWorkFlowDTO> getBuildingInstitutePro23(@RequestParam(value = "xmbh", required = false, defaultValue = "") String xmbh,
                                                         @RequestParam(value = "zx", required = false, defaultValue = "") String zx,
                                                         @RequestParam(value = "types", required = false, defaultValue = "") String types,
                                                       @RequestParam(value = "status", required = false, defaultValue = "") String status,
                                                       @RequestParam(value = "zy", required = false, defaultValue = "") String zy) {
        return naviIndexService.getBuildingInstitutePro23(xmbh, zx, zy, status, types);
    }

    @RequestMapping(value = "getDepartmentOA", method = RequestMethod.GET)
    public List<OaWorkFlowDTO> getDepartmentOA(@RequestParam(value = "id", required = false, defaultValue = "") String id,
                                               @RequestParam(value = "formId", required = false, defaultValue = "") String formId,
                                               @RequestParam(value = "erpId", required = false, defaultValue = "") Long erpId,
                                               @RequestParam(value = "erpId2", required = false, defaultValue = "") Long erpId2) {
        return naviIndexService.getDepartmentOA(id, formId, erpId, erpId2);
    }
}
