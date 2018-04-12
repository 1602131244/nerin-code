package com.nerin.nims.opt.wbsp.api;

import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.wbsp.dto.*;
import com.nerin.nims.opt.wbsp.service.PaLevThreePlanService;
import com.nerin.nims.opt.wbsp.service.ToNBCCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2016/8/4.
 */
@RestController
@RequestMapping("/api/lev3")
public class ToNBCCController {
    @Autowired
    private PaLevThreePlanService paLevThreePlanService;
    @Autowired
    private ToNBCCService toNBCCService;

    /**
     * 创建工作包
     * @param projectId 项目
     * @param phaseCode 阶段code
     * @param major 专业
     * @param dlvrName 工作包名称
     * @param startDate 起始时间
     * @param endDate 结束时间
     * @param design 设计人
     * @param check 校核人
     * @param review 审核人
     * @param approve 审定人
     * @param workHour 工时
     * @param workCode 工作包类型 WTJ
     * @param workID 工作包类型ID 14029
     * @param request
     * @return
     */
    @RequestMapping(value = "createDlvr", method = RequestMethod.GET)
    public Map createDlvr(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                          @RequestParam(value = "phaseCode", required = false, defaultValue = "") String phaseCode,
                          @RequestParam(value = "major", required = false, defaultValue = "") String major,
                          @RequestParam(value = "dlvrName", required = false, defaultValue = "") String dlvrName,
                          @RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
                          @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
                          @RequestParam(value = "design", required = false, defaultValue = "0") long design,
                          @RequestParam(value = "check", required = false, defaultValue = "0") long check,
                          @RequestParam(value = "review", required = false, defaultValue = "0") long review,
                          @RequestParam(value = "approve", required = false, defaultValue = "0") long approve,
                          @RequestParam(value = "workHour", required = false, defaultValue = "") long workHour,
                          @RequestParam(value = "workCode", required = false, defaultValue = "") String workCode,
                          @RequestParam(value = "workID", required = false, defaultValue = "") long workID,
                          HttpServletRequest request) {
        return toNBCCService.createDlvr(projectId, phaseCode, major, dlvrName, startDate, endDate, design, check, review, approve, workHour, workCode, workID, SessionUtil.getLdapUserInfo(request).getUserId());
    }

    @RequestMapping(value = "updateDlvr", method = RequestMethod.GET)
    public Map updateDlvr(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                          @RequestParam(value = "major", required = false, defaultValue = "") String major,
                          @RequestParam(value = "dlvrID", required = false, defaultValue = "") long dlvrID,
                          @RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
                          @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
                          @RequestParam(value = "design", required = false, defaultValue = "0") long design,
                          @RequestParam(value = "check", required = false, defaultValue = "0") long check,
                          @RequestParam(value = "review", required = false, defaultValue = "0") long review,
                          @RequestParam(value = "approve", required = false, defaultValue = "0") long approve,
                          @RequestParam(value = "workHour", required = false, defaultValue = "") long workHour,
                          @RequestParam(value = "specID", required = true, defaultValue = "") long specID) {

        return toNBCCService.updateDlvr(projectId, major, dlvrID, startDate, endDate, design, check, review, approve, workHour, specID);
    }

    /**
     * 模糊查找 工作包列表
     *
     * @param queryParams
     * @param drlvClass
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "getDlvrTypeList", method = RequestMethod.GET)
    public DataTablesDTO getDlvrTypeList(@RequestParam(value = "keywords", required = false, defaultValue = "") String queryParams,
                                         @RequestParam(value = "class", required = false, defaultValue = "02") String drlvClass,
                                         @RequestParam(value = "orgID", required = false, defaultValue = "101") long orgId,
                                         @RequestParam(value = "page", required = false, defaultValue = "1") long pageNo,
                                         @RequestParam(value = "rows", required = false, defaultValue = "1000") long pageSize, @RequestParam(value = "userID", required = true, defaultValue = "1336") long userId,
                                         @RequestParam(value = "spec", required = false, defaultValue = "") String spec,
                                         @RequestParam(value = "phaseID", required = false, defaultValue = "") String phaseID) {
        return paLevThreePlanService.getDeliverableTypeList(queryParams, drlvClass, orgId, pageNo, pageSize,spec,phaseID);
    }

    /**
     * 文本类型设置中关联的工作包类型
     * @param queryParams
     * @param drlvClass
     * @param orgId
     * @param pageNo
     * @param pageSize
     * @param userId
     * @param spec
     * @param phaseID
     * @param taskTypeCode
     * @return
     */
    @RequestMapping(value = "getDlvrTypeListForTaskType", method = RequestMethod.GET)
    public DataTablesDTO getDlvrTypeList(@RequestParam(value = "keywords", required = false, defaultValue = "") String queryParams,
                                         @RequestParam(value = "class", required = false, defaultValue = "02") String drlvClass,
                                         @RequestParam(value = "orgID", required = false, defaultValue = "101") long orgId,
                                         @RequestParam(value = "page", required = false, defaultValue = "1") long pageNo,
                                         @RequestParam(value = "rows", required = false, defaultValue = "1000") long pageSize, @RequestParam(value = "userID", required = true, defaultValue = "1336") long userId,
                                         @RequestParam(value = "spec", required = false, defaultValue = "") String spec,
                                         @RequestParam(value = "phaseID", required = false, defaultValue = "") String phaseID,
                                         @RequestParam(value = "taskTypeCode", required = false, defaultValue = "") String taskTypeCode) {
        return paLevThreePlanService.getDeliverableTypeListForTaskType(queryParams, drlvClass, orgId, pageNo, pageSize,spec,phaseID, taskTypeCode);
    }

    /**
     * 查找工作包
     *
     * @param projectId
     * @param phaseCode
     * @param major
     * @return
     */
    @RequestMapping(value = "getDlvr", method = RequestMethod.GET)
    public List<NBCCDlvrListDTO> getDlvr(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                         @RequestParam(value = "phaseCode", required = false, defaultValue = "") String phaseCode,
                                         @RequestParam(value = "major", required = false, defaultValue = "") String major) {
        return toNBCCService.getDlvr(projectId, phaseCode, major);
    }

    /**
     * 取所有设校审人员
     *
     * @param projectId
     * @param spec
     * @return
     */
    @RequestMapping(value = "getAssignmentList", method = RequestMethod.GET)
    public NBCCAssignmentListDTO getAssignmentList(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                                       @RequestParam(value = "specialty", required = false, defaultValue = "") String spec) {
        List<PAAssignmentDTO> results = new ArrayList<PAAssignmentDTO>();
        results = paLevThreePlanService.getAssignmentListAll(projectId, spec);
        NBCCAssignmentListDTO nbccAssignmentListDTO = new NBCCAssignmentListDTO();
        List<PAMemberDTO> designs = new ArrayList<PAMemberDTO>();
        List<PAMemberDTO> checks = new ArrayList<PAMemberDTO>();
        List<PAMemberDTO> reviews = new ArrayList<PAMemberDTO>();
        List<PAMemberDTO> approves = new ArrayList<PAMemberDTO>();
        PAMemberDTO design;
        PAMemberDTO check;
        PAMemberDTO review;
        PAMemberDTO approve;
        //取出专业和职责组合
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getDuty().equals("设计人")) {
                design = new PAMemberDTO();
                design.setPerId(results.get(i).getPerId());
                design.setPerName(results.get(i).getPerName());
                design.setPerNum(results.get(i).getPerNum());
                designs.add(design);
            }
            if (results.get(i).getDuty().equals("校核人")) {
                check = new PAMemberDTO();
                check.setPerId(results.get(i).getPerId());
                check.setPerName(results.get(i).getPerName());
                check.setPerNum(results.get(i).getPerNum());
                checks.add(check);
            }
            if (results.get(i).getDuty().equals("审核人")) {
                review = new PAMemberDTO();
                review.setPerId(results.get(i).getPerId());
                review.setPerName(results.get(i).getPerName());
                review.setPerNum(results.get(i).getPerNum());
                reviews.add(review);
            }
            if (results.get(i).getDuty().equals("审定人")) {
                approve = new PAMemberDTO();
                approve.setPerId(results.get(i).getPerId());
                approve.setPerName(results.get(i).getPerName());
                approve.setPerNum(results.get(i).getPerNum());
                approves.add(approve);
            }
        }
        nbccAssignmentListDTO.setDesign(designs);
        nbccAssignmentListDTO.setCheck(checks);
        nbccAssignmentListDTO.setReview(reviews);
        nbccAssignmentListDTO.setApprove(approves);

        return nbccAssignmentListDTO;
    }
}
