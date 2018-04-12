package com.nerin.nims.opt.navi.api;

import com.nerin.nims.opt.app.web.rest.dto.DataTableDTO;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.navi.dto.CDataTableDTO;
import com.nerin.nims.opt.navi.dto.ContractApLineDTO;
import com.nerin.nims.opt.navi.service.ContractService;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/11.
 */
@RestController
@RequestMapping("/api/contract")
public class ContractApRestController {
    @Autowired
    public ContractService contractService;

    /**
     * 检查项目是否有合同
     * @param projectId  项目ID
     * @return
     */
    @RequestMapping(value = "checkContract", method = RequestMethod.GET)
    public Long checkContract(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId
    ) {

        return contractService.checkcontract(projectId);
    }

    /**
     * 获取合同名称列表
     * @param projectId  项目ID
     * @return
     */
    @RequestMapping(value = "number", method = RequestMethod.GET)
    public DataTableDTO queryContract(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId
    ) {

        return contractService.getContractNumber(projectId);
    }

    /**
     * 获取合同版本列表
     * @param kheaderId  合同ID
     * @return
     */
    @RequestMapping(value = "version", method = RequestMethod.GET)
    public CDataTableDTO queryContractApVersion(@RequestParam(value = "kheaderId", required = false, defaultValue = "") Long kheaderId,
            @RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                HttpServletRequest request
    ) {

        return contractService.getContractApVersion(kheaderId,projectId,SessionUtil.getLdapUserInfo(request).getUserId());
    }

    /**
     * 获取合同头信息
     * @param kheaderId  合同ID
     * @param version  合同ID
     * @return
     */
    @RequestMapping(value = "head", method = RequestMethod.GET)
    public DataTableDTO queryContractApHead(@RequestParam(value = "kheaderId", required = false, defaultValue = "") Long kheaderId,
                                               @RequestParam(value = "version", required = false, defaultValue = "") Long version,
                                            @RequestParam(value = "statusVersion", required = false, defaultValue = "nowVersion") String statusVersion
    ) {

        return contractService.getContractApHead(kheaderId,version, statusVersion);
    }

    /**
     * 获取合同行信息
     * @param headerId  合同ID
     * @return
     */
    @RequestMapping(value = "line", method = RequestMethod.GET)
    public DataTableDTO queryContractApLine(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                            @RequestParam(value = "statusVersion", required = false, defaultValue = "nowVersion") String statusVersion
    ) {
        return contractService.getContractApline(headerId, statusVersion);
    }


    /**
     * 获取费用项列表
     * @param kheaderId  合同ID
     * @return
     */
    @RequestMapping(value = "linestyle", method = RequestMethod.GET)
    public DataTableDTO queryContractApLineStyle(@RequestParam(value = "kheaderId", required = false, defaultValue = "") Long kheaderId
    ) {

        return contractService.getContractLineStyle(kheaderId);
    }
    /**
     * 获取合同行
     * @param kheaderId  合同ID
     * @return
     */
    @RequestMapping(value = "lineNum", method = RequestMethod.GET)
    public DataTableDTO queryContractApLineNum(@RequestParam(value = "kheaderId", required = false, defaultValue = "") Long kheaderId
    ) {

        return contractService.getContractLineNum(kheaderId);
    }


    /**
     * 获取款项列表
     * @return
     */
    @RequestMapping(value = "rcvtype", method = RequestMethod.GET)
    public DataTableDTO queryContractApRcvType() {

        return contractService.getContractApRcvType();
    }
    /**
     * 获取里程碑列表
     * @param kHeaderId  项目ID
     * @return
     */
    @RequestMapping(value = "milestone", method = RequestMethod.GET)
    public DataTableDTO queryContractApMilestone(@RequestParam(value = "kHeaderId", required = false, defaultValue = "") Long kHeaderId,
                                                 @RequestParam(value = "lineNumber", required = false, defaultValue = "") String lineNumber
    ) {

        return contractService.getApMilestone(kHeaderId,lineNumber);
    }
    /**
     * 调整计划
     * @param kheaderId
     * @param userId
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Long updatePlan(@RequestParam(value = "kheaderId", required = false, defaultValue = "") Long kheaderId,
                           @RequestParam(value = "userId", required = false, defaultValue = "") Long userId,
                           @RequestParam(value = "version", required = false, defaultValue = "") Long version,
                           HttpServletRequest request) {
        return contractService.updatePlan(kheaderId, SessionUtil.getLdapUserInfo(request).getUserId(), version);

    }

    /**
     * 保存
     * @param contractApLineDTOs
     * //@param userId
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public CDataTableDTO saveContractAp(@RequestBody List<ContractApLineDTO> contractApLineDTOs,
                               HttpServletRequest request) {
            return contractService.saveContractAp(contractApLineDTOs, SessionUtil.getLdapUserInfo(request).getUserId());

    }

    /**
     * 获取收款列表
     * @param kheaderId  合同ID
     * @return
     */
    @RequestMapping(value = "cash", method = RequestMethod.GET)
    public CDataTableDTO queryContractArCash(@RequestParam(value = "kheaderId", required = false, defaultValue = "") Long kheaderId) {
        return contractService.getContractArCash(kheaderId);

    }

    /**
     * 获取发票列表
     * @param kheaderId  合同ID
     * @return
     */
    @RequestMapping(value = "ratrx", method = RequestMethod.GET)
    public CDataTableDTO queryContractRaTrx(@RequestParam(value = "kheaderId", required = false, defaultValue = "") Long kheaderId) {
        return contractService.getContractRaTrx(kheaderId);

    }

    /**
     *  收款计划前台提交OA审批程序
     * @param headerId
     * @param request
     * @return
     */
    @RequestMapping(value = "submitOaApproved", method = RequestMethod.POST)
    public Map submitOaApproved(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = contractService.submitOaApproved(headerId, SessionUtil.getLdapUserInfo(request).getEmployeeNo());
        if (!"S".equals(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 删除收款计划版本
     *
     * @param headerId 项目ID
     * @param kHeaderId   里程碑版本
     * @return
     */
    @RequestMapping(value = "deleteapv", method = RequestMethod.GET)
    public Map deleteMilestone(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                               @RequestParam(value = "kHeaderId", required = false, defaultValue = "") Long kHeaderId
    ) {
        return contractService.deleteVersion(headerId, kHeaderId);
    }
    /**
     * 获取里程碑状态
     * @param kHeaderId  项目ID
     * @param lineNum  项目ID
     * @param    lineId
     * @return
     */
    @RequestMapping(value = "milestoneStatus", method = RequestMethod.GET)
    public Map getMilestoneStatus(@RequestParam(value = "kHeaderId", required = false, defaultValue = "") Long kHeaderId,
                                  @RequestParam(value = "lineNum", required = false, defaultValue = "") String lineNum,
                                  @RequestParam(value = "lineId", required = false, defaultValue = "") Long lineId
    ) {

        return contractService.getMilestoneStatus(kHeaderId,lineNum,lineId);
    }
    /**
     * 获取合同行信息
     * @param kheaderId  项目ID
     *      @param    lineNum
     * @return
     */
    @RequestMapping(value = "contractInfo", method = RequestMethod.GET)
    public Map gecontractInfo(@RequestParam(value = "kheaderId", required = false, defaultValue = "") Long kheaderId,
                                  @RequestParam(value = "lineNum", required = false, defaultValue = "") String lineNum
    ) {

        return contractService.getContractInfo(kheaderId,lineNum);
    }
}
