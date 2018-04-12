package com.nerin.nims.opt.navi.api;

import com.nerin.nims.opt.app.web.rest.dto.DataTableDTO;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.navi.dto.MDataTableDTO;
import com.nerin.nims.opt.navi.dto.MilestoneDto;
import com.nerin.nims.opt.navi.dto.MilestoneProjectDTO;
import com.nerin.nims.opt.navi.service.MilestoneService;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/7.
 */
@RestController
@RequestMapping("/api/milestone")
public class MilestoneRestController {
    @Autowired
    public MilestoneService milestoneService;


    /**
     * 检查工作计划版本
     *
     * @param projectId 项目ID
     * @return
     */
    @RequestMapping(value = "workplan", method = RequestMethod.GET)
    public Long querycheckworkplan(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId
    ) {

        return milestoneService.checkworkplanStatus(projectId);
    }

    /**
     * 获取项目信息，树形结构第一行
     *
     * @param projectId 项目ID
     * @param version   版本ID
     * @return
     */
    @RequestMapping(value = "project", method = RequestMethod.GET)
    public MilestoneProjectDTO queryMilestoneProject(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                     @RequestParam(value = "version", required = false, defaultValue = "") Long version
    ) {

        return milestoneService.getMilestoneProject(projectId, version);
    }

    /**
     * 获取里程碑状态列表
     *
     * @param projectId 项目ID
     * @param version   版本ID
     * @return
     */
    @RequestMapping(value = "status", method = RequestMethod.GET)
    public MDataTableDTO queryApproveStatus(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                            @RequestParam(value = "version", required = false, defaultValue = "") Long version
    ) {

        return milestoneService.getMilestoneStatus(projectId, version);
    }

    /**
     * 获取里程碑列表
     *
     * @param projectId
     * @param version
     * @return
     */

    @RequestMapping(value = "milestonelist1", method = RequestMethod.GET)
    public List<MilestoneDto> queryTemplateChaptersList1(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                         @RequestParam(value = "version", required = false, defaultValue = "") Long version
    ) {
        return milestoneService.getMilestoneList1(projectId, version);
    }

    /**
     * 获取项目角色列表
     *
     * @param projectId 项目ID
     * @return
     */
    @RequestMapping(value = "rolelist", method = RequestMethod.GET)
    public DataTableDTO queryRoleList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                      HttpServletRequest request) {
        return milestoneService.getProjectRole(SessionUtil.getLdapUserInfo(request).getPersonId(), projectId);
    }

    /**
     * 获取项目角色组
     *
     * @param projectId 项目ID
     * @return
     */
    @RequestMapping(value = "rolegroup", method = RequestMethod.GET)
    public DataTableDTO queryRoleGroup(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                       //@RequestParam(value = "personId", required = false, defaultValue = "") Long personId
                                      HttpServletRequest request
    ) {
        return milestoneService.getRoleGroup(SessionUtil.getLdapUserInfo(request).getPersonId(),projectId);
    }

    /**
     * 获取里程碑版本列表
     *
     * @param projectId 项目ID
     * @return
     */
    @RequestMapping(value = "version", method = RequestMethod.GET)
    public DataTableDTO queryMilestoneVersion(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId
    ) {

        return milestoneService.getMilestoneVersion(projectId);
    }

    /**
     * 调整计划
     *
     * @param projectId
     * @param userId
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.GET)
    public Long updatePlan(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                           @RequestParam(value = "userId", required = false, defaultValue = "") Long userId,
                           HttpServletRequest request) {
        return milestoneService.updatePlan(projectId, SessionUtil.getLdapUserInfo(request).getUserId());

    }

    /**
     * 保存
     *
     * @param milestoneDtos //  * @param userId
     * @return
     */
    @RequestMapping(value = "save", method = RequestMethod.POST)
    public Long saveContractAp(@RequestBody List<MilestoneDto> milestoneDtos
                               //        @RequestParam(value = "userId", required = false, defaultValue = "") Long userId
    ) {
        return milestoneService.saveMilestone(milestoneDtos);

    }

    /**
     * 更新里程碑状态
     *
     * @param milestoneIds 序号列合集
     * @param status
     * @return
     */
    @RequestMapping(value = "updateMilestoneStatus", method = RequestMethod.GET)
    public String updateMilestoneStatus(@RequestParam(value = "milestoneIds", required = false, defaultValue = "") String milestoneIds,
                                        @RequestParam(value = "status", required = false, defaultValue = "") String status
    ) {

        return milestoneService.updateMilestoneStatus(milestoneIds, status);
    }

    /**
     * 提交审批
     *
     * @param projectId 项目ID
     * @param version   里程碑版本
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.GET)
    public String submit(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                         @RequestParam(value = "version", required = false, defaultValue = "") Long version
    ) {

        return milestoneService.submit(projectId, version);
    }

    /**
     * 里程碑提交审批
     * @param milestoneId
     * @param request
     * @return
     */
    @RequestMapping(value = "submitOaApproved", method = RequestMethod.POST)
    public Map submitOaApproved(@RequestParam(value = "milestoneId", required = false, defaultValue = "") Long milestoneId,
                                @RequestParam(value = "status", required = false, defaultValue = "") String status,
                                HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = milestoneService.submitOaApproved(milestoneId, SessionUtil.getLdapUserInfo(request).getEmployeeNo(), status);
        if (!"S".equals(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

//    /**
//     * 根据里程碑节点ID判断是否为收入节点
//     * @param milestoneId
//     * @return
//     */
//    @RequestMapping(value = "isMilestoneIncomeNode", method = RequestMethod.GET)
//    public String isMilestoneIncomeNode(@RequestParam(value = "milestoneId", required = false, defaultValue = "") Long milestoneId) {
//        return milestoneService.isMilestoneIncomeNode(milestoneId);
//    }

    /**
     * 项目导航前台提交自审批
     * @param proId
     * @param no
     * @param request
     * @return
     */
    @RequestMapping(value = "selfApproval", method = RequestMethod.POST)
    public Map selfApproval(@RequestParam(value = "proId", required = false, defaultValue = "") Long proId,
                            @RequestParam(value = "no", required = false, defaultValue = "") Long no,
                                HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = milestoneService.selfApproval(proId, no, SessionUtil.getLdapUserInfo(request).getUserId());
        if (!"S".equals(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 验证阶段计划时间
     * @param proId
     * @param phaseCode
     * @param date
     * @param request
     * @return
     */
    @RequestMapping(value = "validataPlanDate", method = RequestMethod.POST)
    public Map validataPlanDate(@RequestParam(value = "proId", required = false, defaultValue = "") Long proId,
                            @RequestParam(value = "phaseCode", required = false, defaultValue = "") String phaseCode,
                            @RequestParam(value = "date", required = false, defaultValue = "") String date,
                            HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = milestoneService.validataPlanDate(proId, phaseCode, date);
        if (!"S".equals(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }
    /**
     * 删除里程碑
     *
     * @param projectId 项目ID
     * @param version   里程碑版本
     * @return
     */
    @RequestMapping(value = "deletev", method = RequestMethod.GET)
    public Map deleteMilestone(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                   @RequestParam(value = "version", required = false, defaultValue = "") Long version
    ) {
        return milestoneService.deleteMilestone(projectId, version);
    }
}
