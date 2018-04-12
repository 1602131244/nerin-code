package com.nerin.nims.opt.budget.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.BusinessException;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.budget.dto.BudgetHeaderDto;
import com.nerin.nims.opt.budget.dto.BudgetLineDto;
import com.nerin.nims.opt.budget.service.BudgetService;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/29.
 */
@RestController
@RequestMapping("/api/budgetRest")
public class BudgetRestController {

    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    public BudgetService budgetService;

    /**
     * 获取组织列表
     * @param orgName
     * @param orgId
     * @param sign      Y 标示限定为财务组织，N 标示所有组织
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getOrgList", method = RequestMethod.GET)
    public DataTablesDTO queryOrgList(@RequestParam(value = "orgName", required = false, defaultValue = "") String orgName,
                                      @RequestParam(value = "orgId", required = false, defaultValue = "") Long orgId,
                                      @RequestParam(value = "sign", required = false, defaultValue = "N") String sign,
                                      @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "3000") long pageSize
    ) {
        return budgetService.getOrgList(orgName, orgId, sign, pageNo, pageSize);
    }

    /**
     * 获取年份列表
     * @param year
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getYearList", method = RequestMethod.GET)
    public DataTablesDTO queryYearList(@RequestParam(value = "year", required = false, defaultValue = "") Long year,
                                       @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "3000") long pageSize
    ) {
        return budgetService.getYearList(year, pageNo, pageSize);
    }

    /**
     * 获取预算项目类型列表
     * @param subType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getSubTypeList", method = RequestMethod.GET)
    public DataTablesDTO querySubTypeList(@RequestParam(value = "subType", required = false, defaultValue = "") String subType,
                                       @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "3000") long pageSize
    ) {
        return budgetService.getSubTypeList(subType, pageNo, pageSize);
    }

    /**
     * 获取支出类型
     * @param subType
     * @param costType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getCostTypeList", method = RequestMethod.GET)
    public DataTablesDTO queryCostTypeList(@RequestParam(value = "subType", required = false, defaultValue = "") String subType,
                                          @RequestParam(value = "costType", required = false, defaultValue = "") String costType,
                                          @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "3000") long pageSize
    ) {
        return budgetService.getCostTypeList(subType, costType, pageNo, pageSize);
    }

    /**
     * 获取年度预算任务头状态列表
     * @param statusType  CUX_DEPT_BUDGET_STATUS
     * @param statusCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getHeaderStatusList", method = RequestMethod.GET)
    public DataTablesDTO queryHeaderStatusList(@RequestParam(value = "statusType", required = false, defaultValue = "CUX_DEPT_BUDGET_STATUS") String statusType,
                                                @RequestParam(value = "statusCode", required = false, defaultValue = "") String statusCode,
                                                @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "3000") long pageSize
    ) {
        return budgetService.getStatusList(statusType, statusCode, pageNo, pageSize);
    }

    /**
     * 获取年度预算任务行状态列表
     * @param statusType  CUX_DEPT_BUDGET_LINE_STATUS
     * @param statusCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getLineStatusList", method = RequestMethod.GET)
    public DataTablesDTO queryLineStatusList(@RequestParam(value = "statusType", required = false, defaultValue = "CUX_DEPT_BUDGET_LINE_STATUS") String statusType,
                                              @RequestParam(value = "statusCode", required = false, defaultValue = "") String statusCode,
                                              @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                              @RequestParam(value = "pageSize", required = false, defaultValue = "3000") long pageSize
    ) {
        return budgetService.getStatusList(statusType, statusCode, pageNo, pageSize);
    }

    /**
     * 获取标识
     * @param signCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getSignList", method = RequestMethod.GET)
    public DataTablesDTO querySignList(@RequestParam(value = "signCode", required = false, defaultValue = "") String signCode,
                                           @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "3000") long pageSize
    ) {
        return budgetService.getSignList(signCode, pageNo, pageSize);
    }

    /**
     * 获取部门预算取头列表
     * @param orgName
     * @param year
     * @param budgetName
     * @param createName
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "budgetHeaderList", method = RequestMethod.POST)
    public DataTablesDTO queryTaskHeaderList(@RequestParam(value = "orgName", required = false, defaultValue = "") String orgName,
                                             @RequestParam(value = "year", required = false, defaultValue = "") String year,
                                             @RequestParam(value = "budgetName", required = false, defaultValue = "") String budgetName,
                                             @RequestParam(value = "createName", required = false, defaultValue = "") String createName,
                                             @RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                             HttpServletRequest request,
                                             @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize) {
        return budgetService.getBudgetHeaderList(orgName, year, budgetName, createName, headerId, SessionUtil.getLdapUserInfo(request).getUserId(), pageNo, pageSize);
    }

    /**
     * 删除部门年度预算任务
     * @param headerIds
     * @return
     */
    @RequestMapping(value = "delBudget", method = RequestMethod.POST)
    public Map delBudget(@RequestParam(value = "headerIds", required = false, defaultValue = "-1") String headerIds) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = budgetService.delBudget(headerIds);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 保存预算任务头表
     * @param budgetHeaderDto
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveBudgetHeader", method = RequestMethod.POST)
    public Map saveBudgetHeader(@RequestBody BudgetHeaderDto budgetHeaderDto, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = budgetService.addOrUpBudget (budgetHeaderDto, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 导入预算任务明细表数据
     * @param headerId
     * @param request
     * @return
     */
    @RequestMapping(value = "importBudgetLines", method = RequestMethod.POST)
    public Map importBudgetLines(@RequestParam(value = "headerId", required = false, defaultValue = "-1") long headerId
                                 ,HttpServletRequest request
                         ) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = budgetService.importBudgetLines(headerId, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 获取预算任务明细表列表
     * @param headerId
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "budgetLineList", method = RequestMethod.POST)
    public DataTablesDTO queryTasklINEList(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                             HttpServletRequest request,
                                             @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "9000") long pageSize) {
        return budgetService.getBudgetLineList(headerId, SessionUtil.getLdapUserInfo(request).getUserId(), pageNo, pageSize);
    }

    /**
     * 保存预算任务明细表
     * @param budgetLineDtos
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveBudgetLine", method = RequestMethod.POST)
    public Map saveBudgetLine(@RequestBody List<BudgetLineDto> budgetLineDtos, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = budgetService.addOrUpBudgetLine (budgetLineDtos, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 删除预算任务明细数据
     * @param lineIds
     * @return
     */
    @RequestMapping(value = "delBudgetLines", method = RequestMethod.POST)
    public Map delBudgetLines(@RequestParam(value = "lineIds", required = false, defaultValue = "-1") String lineIds) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = budgetService.delBudgetLines(lineIds);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 发起填报流程时获取OA的链接地址
     * @param headerId
     * @param request
     * @return
     */
    @RequestMapping(value = "getFillProcessList", method = RequestMethod.GET)
    public DataTablesDTO queryFillProcessList(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                                HttpServletRequest request
    ) {
        return budgetService.getFillProcessList(headerId, SessionUtil.getLdapUserInfo(request).getUserId());
    }

    /**
     * 更新OA流程的签收人、签收日期
     * @param requestId
     * @param request
     * @return
     */
//    @RequestMapping(value = "updateOaRequestOie", method = RequestMethod.POST)
//    public Map updateOaRequestOie(@RequestParam(value = "requestId", required = false, defaultValue = "") Long requestId,
//                                  HttpServletRequest request) {
//        Map restFulData = RestFulData.getRestInitData();
//        Map reMap = null;
//
//        reMap = budgetService.updateOaRequestOie (requestId, SessionUtil.getLdapUserInfo(request).getUserId());
//        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
//            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
//            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
//        } else {
//            restFulData.putAll(reMap);
//        }
//        return restFulData;
//    }


    /**
     * 获取项目预算列表
     * @param projectId
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping(value = "projectBudgetList", method = RequestMethod.GET)
    public DataTablesDTO queryprojectBudgetList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                               @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                               HttpServletRequest request) {
        if (0 == userId) {
                userId = SessionUtil.getLdapUserInfo(request).getUserId();
        }

        return budgetService.getProjBudgetList(projectId, userId);
    }

    /**
     * 获取项目成本明细列表
     * @param projectId
     * @param rbsVersionId
     * @param expenditureType
     * @param taskId
     * @param request
     * @return
     */
    @RequestMapping(value = "projectCostList", method = RequestMethod.GET)
    public DataTablesDTO queryProjectCostList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                @RequestParam(value = "rbsVersionId", required = false, defaultValue = "0") Long rbsVersionId,
                                              @RequestParam(value = "expenditureType", required = false, defaultValue = "") String expenditureType,
                                              @RequestParam(value = "taskId", required = false, defaultValue = "") Long taskId,
                                                HttpServletRequest request) {

        return budgetService.getProjCostList(projectId, rbsVersionId, expenditureType, taskId );
    }


    /**
     * 查询项目工时信息
     * @param projectId   项目ID
     * @param projectSearch   项目检索信息
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "projectInfoList", method = RequestMethod.GET)
    public DataTablesDTO queryProjectInfoList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                             @RequestParam(value = "projectSearch", required = false, defaultValue = "") String projectSearch,
                                              @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                             HttpServletRequest request,
                                             @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize) {
        if (0 == userId) {
            userId = SessionUtil.getLdapUserInfo(request).getUserId();
        }
        return budgetService.getprojectInfo(projectId, projectSearch, userId, pageNo, pageSize);
    }

    /**
     * 更新项目总工时
     * @param projectId
     * @param cExtAttr4
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping(value = "upProjInfo", method = RequestMethod.POST)
    public Map upProjInfo(@RequestParam(value = "projectId", required = false, defaultValue = "-1") Long projectId,
                              @RequestParam(value = "cExtAttr4", required = false, defaultValue = "") String cExtAttr4,
                              @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                              HttpServletRequest request) {
        if (0 == userId) {
            userId = SessionUtil.getLdapUserInfo(request).getUserId();
        }
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = budgetService.upProjInfo(projectId,cExtAttr4,userId);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }
}