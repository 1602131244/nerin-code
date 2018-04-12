package com.nerin.nims.opt.aria.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.aria.common.EventParm;
import com.nerin.nims.opt.aria.dto.BillEventDto;
import com.nerin.nims.opt.aria.service.InvoiceApplyService;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.BusinessException;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.cqs.dto.DataTablesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/20.
 */
@RestController
@RequestMapping("/api/invoiceApply")
public class InvoiceApplyController {

    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    private InvoiceApplyService invoiceApplyService;

    /**
     * 获取合同与项目交际的客户信息
     * @param contHeaderId
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping(value = "contCustList", method = RequestMethod.GET)
    public DataTablesDTO getContCustList(@RequestParam(value = "contHeaderId", required = false, defaultValue = "") Long contHeaderId,
                                        @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                        HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();

        return invoiceApplyService.getContCustList(contHeaderId);
    }

    /**
     * 项目列表
     * @param contLineId
     * @param projectSearch
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping(value = "projectList", method = RequestMethod.GET)
    public DataTablesDTO getProjectList(@RequestParam(value = "contLineId", required = false, defaultValue = "") String contLineId,
                                                 @RequestParam(value = "projectSearch", required = false, defaultValue = "") String projectSearch,
                                        @RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                        @RequestParam(value = "projectNumber", required = false, defaultValue = "") String projectNumber,
                                        @RequestParam(value = "checkProjectMember", required = false, defaultValue = "Y") String checkProjectMember,
                                                 @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                                 HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();

        return invoiceApplyService.getProjectList(projectSearch, projectId, projectNumber, contLineId, checkProjectMember, userId);
    }


    /**
     * 获取合同列表
     * @param contractId
     * @param contractNumber
     * @param contractSearch
     * @param projectId
     * @param projectNumber
     * @param projectSearch
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "contHeaderList", method = RequestMethod.GET)
    public DataTablesDTO queryContractHeaderList(@RequestParam(value = "contractId", required = false, defaultValue = "") Long contractId,
                                             @RequestParam(value = "contractNumber", required = false, defaultValue = "") String contractNumber,
                                             @RequestParam(value = "contractSearch", required = false, defaultValue = "") String contractSearch,
                                             @RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                             @RequestParam(value = "projectNumber", required = false, defaultValue = "") String projectNumber,
                                             @RequestParam(value = "projectSearch", required = false, defaultValue = "") String projectSearch,
                                             @RequestParam(value = "projContSearch", required = false, defaultValue = "") String projContSearch,
                                             @RequestParam(value = "checkManager", required = false, defaultValue = "Y") String checkManager,
                                             @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                             @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                             HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();

        return invoiceApplyService.getContractHeaderList(contractId, contractNumber, contractSearch, projectId, projectNumber, projectSearch, projContSearch, checkManager, userId, pageNo, pageSize);
    }

    /**
     * 获取市场活动物品列表
     * @param contractId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "deliverablesList", method = RequestMethod.GET)
    public DataTablesDTO queryDeliverablesList(@RequestParam(value = "contractId", required = false, defaultValue = "") Long contractId,
                                               @RequestParam(value = "deliverablesId", required = false, defaultValue = "") Long deliverablesId,
                                                 @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                                 HttpServletRequest request
    ) {
        return invoiceApplyService.getDeliverablesList(contractId, deliverablesId, pageNo, pageSize);
    }

    /**
     * 获取开票申请列表
     * @param deliverablesId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "billEventList", method = RequestMethod.GET)
    public DataTablesDTO queryBillEventList(@RequestParam(value = "contId", required = false, defaultValue = "") Long contId,
                                            @RequestParam(value = "deliverablesId", required = false, defaultValue = "") Long deliverablesId,
                                            @RequestParam(value = "billEventId", required = false, defaultValue = "") Long billEventId,
                                            @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                            HttpServletRequest request
    ) {
        return invoiceApplyService.getBillEventList(contId, deliverablesId, billEventId, pageNo, pageSize);
    }

    /**
     * 获取开票类型
     * @param projectId
     * @param request
     * @return
     */
    @RequestMapping(value = "eventTypeList", method = RequestMethod.GET)
    public DataTablesDTO queryEventTypeList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                            HttpServletRequest request
    ) {
        return invoiceApplyService.getEventTypeList(projectId);
    }

    /**
     * 获取发票类型
     * @return
     */
    @RequestMapping(value = "invoiceTypeList", method = RequestMethod.GET)
    public DataTablesDTO queryInvoiceTypeList(
    ) {
        return invoiceApplyService.getInvoiceTypeList();
    }

    /**
     *获取任务列表
     * @param projectId
     * @param request
     * @return
     */
    @RequestMapping(value = "taskList", method = RequestMethod.GET)
    public DataTablesDTO queryTaskList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                            HttpServletRequest request
    ) {
        return invoiceApplyService.getTaskList(projectId);
    }

    /**
     * 获取币别列表
     * @return
     */
    @RequestMapping(value = "currList", method = RequestMethod.GET)
    public DataTablesDTO queryCurrList(
    ) {
        return invoiceApplyService.getCurrList();
    }

    /**
     * 获取汇率类型
     * @return
     */
    @RequestMapping(value = "rateTypeList", method = RequestMethod.GET)
    public DataTablesDTO queryRateTypeList(
    ) {
        return invoiceApplyService.getRateTypeList();
    }

    /**
     * 获取组织列表
     * @param orgId
     * @param request
     * @return
     */
    @RequestMapping(value = "orgList", method = RequestMethod.GET)
    public DataTablesDTO queryOrgList(@RequestParam(value = "orgId", required = false, defaultValue = "") Long orgId,
                                       HttpServletRequest request
    ) {
        return invoiceApplyService.getOrgList(orgId);
    }

    /**
     * 获取汇率
     * @param fromCurr
     * @param toCurr
     * @param rateType
     * @param rateDate
     * @param request
     * @return
     */
    @RequestMapping(value = "getRate", method = RequestMethod.GET)
    public String getRate(@RequestParam(value = "fromCurr", required = false, defaultValue = "") String fromCurr,
                        @RequestParam(value = "toCurr", required = false, defaultValue = "") String toCurr,
                        @RequestParam(value = "rateType", required = false, defaultValue = "") String rateType,
                        @RequestParam(value = "rateDate", required = false, defaultValue = "") String rateDate,
                                      HttpServletRequest request
    ) {
        return invoiceApplyService.getRate(fromCurr, toCurr, rateType, rateDate);
    }

    /**
     * 获取合同开票金额信息
     * @param contId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "getContInvoices", method = RequestMethod.GET)
    public DataTablesDTO getContInvoices(@RequestParam(value = "contId", required = false, defaultValue = "") Long contId,
                                            @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                            HttpServletRequest request
    ) {
        return invoiceApplyService.getContInvoices(contId,pageNo, pageSize);
    }

    /**
     * 删除开票申请
     * @param billEventId
     * @return
     */
    @RequestMapping(value = "delBillEvent", method = RequestMethod.POST)
    public Map delBillEvent(@RequestParam(value = "billEventId", required = false, defaultValue = "-1") Long billEventId) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = invoiceApplyService.delBillEvent(billEventId);
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 保存开票申请
     * @param billEventDto
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveBillEvent", method = RequestMethod.POST)
    public Map saveBillEvent(@RequestBody BillEventDto billEventDto, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = invoiceApplyService.addOrUpBillEvent (billEventDto,"SAVE",SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 保存并提交开票申请
     * @param billEventDto
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "fillBillEvent", method = RequestMethod.POST)
    public Map fillBillEvent(@RequestBody BillEventDto billEventDto, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = invoiceApplyService.addOrUpBillEvent (billEventDto,"FILL",SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

}
