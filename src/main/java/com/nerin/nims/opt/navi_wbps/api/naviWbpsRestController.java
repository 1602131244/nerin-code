package com.nerin.nims.opt.navi_wbps.api;

import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.navi.dto.OaWorkFlowDTO;
import com.nerin.nims.opt.navi.dto.PhaseForJssTDO;
import com.nerin.nims.opt.navi.dto.PhaseForOalcDTO;
import com.nerin.nims.opt.navi.dto.PhaseForTjdDTO;
import com.nerin.nims.opt.navi.service.PhaseService;
import com.nerin.nims.opt.navi_wbps.dto.*;
import com.nerin.nims.opt.navi_wbps.service.naviWpbsService;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/28.
 */
@RestController
@RequestMapping("/api/naviWbpsRest")
public class naviWbpsRestController {
    @Autowired
    private naviWpbsService naviWpbsService;

    /**
     * wb
     * @param pElementId
     * @return
     */
    @RequestMapping(value = "wb", method = RequestMethod.GET)
    public List<naviWbpsWorkpkgwbDTO> querywbheader(@RequestParam(value = "pElementId", required = false, defaultValue = "") Long pElementId) {
        return naviWpbsService.getwbHeader(pElementId);
    }

    /**
     * wbtask
     * @param taskHeaderId
     * @return
     */
    @RequestMapping(value = "wbTask", method = RequestMethod.GET)
    public List<PhaseForOalcDTO> querywbtask(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId) {
        return naviWpbsService.getwbTask(taskHeaderId);
    }
    /**
     * wbchapter
     * @param pElementId
     * @param taskHeaderId
     * @return
     */
    @RequestMapping(value = "wbChapter", method = RequestMethod.GET)
    public List<PhaseForOalcDTO> querywbchapter(@RequestParam(value = "pElementId", required = false, defaultValue = "-1") Long pElementId,
                                                @RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                                @RequestParam(value = "zhuanye", required = false, defaultValue = "") String zhuanye) {
        return naviWpbsService.getwbChapter(pElementId,taskHeaderId,zhuanye);
    }

    /**
     * 工作包进度-计算书OA
     * @param projElementId
     * @return
     */
    @RequestMapping(value = "jss_oa", method = RequestMethod.GET)
    public List<PhaseForJssTDO> queryJssOa(@RequestParam(value = "projElementId", required = false, defaultValue = "") Long projElementId) {
        return naviWpbsService.getJssOa(projElementId);
    }

    /**
     * 工作包进度-条件单
     * @param projElementId
     * @return
     */
    @RequestMapping(value = "tjd", method = RequestMethod.GET)
    public List<PhaseForTjdDTO> queryTjd(@RequestParam(value = "projElementId", required = false, defaultValue = "") Long projElementId
                                                              ) {
        return naviWpbsService.getTjd(projElementId);
    }

    /**
     * OA-条件单
     * @param conditionId
     * @return
     */
    @RequestMapping(value = "tjdOa", method = RequestMethod.GET)
    public List<PhaseForOalcDTO> queryTjdOa(@RequestParam(value = "conditionId", required = false, defaultValue = "") Long conditionId) {
        return naviWpbsService.getTjdOa(conditionId);
    }

    /**
     * 检查工作包信息
     * @param projectId
     *  @param elementId
     *  @param submitCode
     * @param receiveCode
     * @param schleDate
     * @return
     */
    @RequestMapping(value = "checkGzb", method = RequestMethod.GET)
    public String queryCheckGzb(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                @RequestParam(value = "elementId", required = false, defaultValue = "") Long elementId,
                                @RequestParam(value = "submitCode", required = false, defaultValue = "") String submitCode,
                                @RequestParam(value = "receiveCode", required = false, defaultValue = "") String receiveCode,
                                @RequestParam(value = "schleDate", required = false, defaultValue = "") String schleDate) {
        return naviWpbsService.checkGzbData(projectId,elementId,submitCode,receiveCode,schleDate);
    }

    /**
     * 生成条件单
     * @param projectId
     *  @param elementId
     *  @param submitCode
     * @param receiveCode
     * @param schleDate
     * @param jszyfzr
     * @return
     */
    @RequestMapping(value = "createcondition", method = RequestMethod.GET)
    public naviWbpsDTO queryCreateCondition(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                            @RequestParam(value = "elementId", required = false, defaultValue = "") Long elementId,
                                            @RequestParam(value = "deliverableId", required = false, defaultValue = "") Long deliverableId,
                                            @RequestParam(value = "submitCode", required = false, defaultValue = "") String submitCode,
                                            @RequestParam(value = "receiveCode", required = false, defaultValue = "") String receiveCode,
                                            @RequestParam(value = "schleDate", required = false, defaultValue = "") String schleDate,
                                            HttpServletRequest request,
                                            @RequestParam(value = "jszyfzr", required = false, defaultValue = "") String jszyfzr
    ) {
        return naviWpbsService.createCondition(projectId,elementId,deliverableId,submitCode,receiveCode,schleDate,SessionUtil.getLdapUserInfo(request).getUserId(),jszyfzr);
    }

    /**
     * OA-专业方案评审流程查询
     * @param projectId
     *  @param phaseId
     * @return
     */
    @RequestMapping(value = "zyfapsOa", method = RequestMethod.GET)
    public List<naviWbpsOaDTO> queryZyfapsOa(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                             @RequestParam(value = "phaseId", required = false, defaultValue = "0") Long phaseId ) {
        return naviWpbsService.getZyfapsOa(projectId,phaseId);
    }
    /**
     * OA-公司级方案评审流程查询
     * @param projectId
     *  @param phaseId
     * @return
     */
    @RequestMapping(value = "gsjfapsOa", method = RequestMethod.GET)
    public List<naviWbpsOaDTO> queryGsjfapsOa(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                              @RequestParam(value = "phaseId", required = false, defaultValue = "0") Long phaseId ) {
        return naviWpbsService.getGsjfapsOa(projectId,phaseId);
    }
    /**
     * OA-设计输入评审流程查询
     * @param projectId
     *  @param phaseId
     * @return
     */
    @RequestMapping(value = "sjsrpsOa", method = RequestMethod.GET)
    public List<naviWbpsOaDTO> querySjsrpsOa(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                              @RequestParam(value = "phaseId", required = false, defaultValue = "0") Long phaseId ) {
        return naviWpbsService.getSjsrpsOa(projectId,phaseId);
    }

    /**
     * OA-设计变更流程查询
     * @param projectId
     *  @param phaseId
     * @return
     */
    @RequestMapping(value = "sjbgOa", method = RequestMethod.GET)
    public naviDataDTO querySjbgOa(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                   @RequestParam(value = "phaseId", required = false, defaultValue = "") Long phaseId,
                                   @RequestParam(value = "keyWords", required = false, defaultValue = "") String keyWords,
                                   @RequestParam(value = "works", required = false, defaultValue = "") String works,
                                   @RequestParam(value = "draws", required = false, defaultValue = "") String draws,
                                   @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
                                   @RequestParam(value = "rows", required = false, defaultValue = "20") Long rows) {
        return naviWpbsService.getSjbgOa(projectId,phaseId,keyWords,works,draws,page,rows);
    }

    /**
     * OA-设计变更流程明细查询
     * @param requestId
     * @return
     */
    @RequestMapping(value = "sjbgdtOa", method = RequestMethod.GET)
    public naviWbpsSjbgdtDTO querySjbgdtOa(@RequestParam(value = "requestId", required = false, defaultValue = "") float requestId
                                  ) {
        return naviWpbsService.getSjbgDtOa(requestId);
    }


    /**
     * OA-图纸文印流程查询
     * @param projectId
     *  @param formId
     * @return
     */
    @RequestMapping(value = "printAllOa", method = RequestMethod.GET)
    public naviDataDTO queryPrintAllOa(@RequestParam(value = "projectId", required = false, defaultValue = "-1") Long projectId,
                                   @RequestParam(value = "formId", required = false, defaultValue = "") Long formId,
                                      // @RequestParam(value = "userNo", required = false, defaultValue = "") String userNo,
                                   @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
                                   @RequestParam(value = "rows", required = false, defaultValue = "20") Long rows,
                                       HttpServletRequest request) {
        return naviWpbsService.getPrintAllOa(projectId,formId,SessionUtil.getLdapUserInfo(request).getEmployeeNo(),page,rows);
    }
    /**
     * 获取项目阶段图纸份数
     * @param projectId
     * @return
     */
    @RequestMapping(value = "drawingNum", method = RequestMethod.GET)
    public List<naviWbpsDrawingNumDTO> queryDrawingNum(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId) {
        return naviWpbsService.getDrawingNum(projectId);
    }
    /**
     * 提交设计变更
     * @param designRevisionDTO
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    public Map drawingCreate(@RequestBody DesignRevisionDTO designRevisionDTO,HttpServletRequest request) {
        return naviWpbsService.drawingCreate(designRevisionDTO,SessionUtil.getLdapUserInfo(request).getUserId());
    }
    /**
     * 提交图纸文印流程
     * @param printCreateDTO
     * @return
     */
    @RequestMapping(value = "printSubmit" , method = RequestMethod.POST)
    public Map printCreate(@RequestBody printCreateDTO printCreateDTO,HttpServletRequest request,
                           @RequestParam(value = "workflowId", required = false, defaultValue = "") Long workflowId
                           ) {
        return naviWpbsService.printCreate(printCreateDTO,workflowId,SessionUtil.getLdapUserInfo(request).getUserId());
    }
    /**
     * 获取项目角色
     * @param projectId
     * @return
     */
    @RequestMapping(value = "getProjRoles" , method = RequestMethod.GET)
    public List<getProjectRoleDTO> printCreate(
                           @RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId
    ) {
        return naviWpbsService.getProjectRoles(projectId);
    }
    /**
     * 建筑院图纸校审流程提交
     * @param jzyPrintCheckDTO
     * @return
     */
    @RequestMapping(value = "jzyPrintCheck" , method = RequestMethod.POST)
    public Map jzyPrintCheck(@RequestBody  jzyPrintCheckDTO jzyPrintCheckDTO,HttpServletRequest request,
                              @RequestParam(value = "workflowId", required = false, defaultValue = "") Long workflowId
    ) {
        return naviWpbsService.jzyPrintCheck(jzyPrintCheckDTO,workflowId,SessionUtil.getLdapUserInfo(request).getUserId());
    }
    /**
     * 建筑院图纸校审流程更新图纸发布
     * @param requestId
     * @return
     */
    @RequestMapping(value = "updateOatzfb" , method = RequestMethod.POST)
    public Map updateOatzfb(
                             @RequestParam(value = "requestId", required = false, defaultValue = "") Long requestId
    ) {
        return naviWpbsService.updateOatzfb(requestId);
    }
    /**
     * OA-流程查询
     * @param formId
     *  @param xmId
     * @param jdcode
     *  @param zyjds
     * @return
     */
    @RequestMapping(value = "jzyPrintCheckOa", method = RequestMethod.GET)
    public naviDataDTO getJzyPrintCheckOa(@RequestParam(value = "formId", required = false, defaultValue = "") Long formId,
                                   @RequestParam(value = "xmId", required = false, defaultValue = "-1") Long xmId,
                                   @RequestParam(value = "jdcode", required = false, defaultValue = "") String jdcode,
                                   @RequestParam(value = "zyjds", required = false, defaultValue = "") String zyjds,
                                   @RequestParam(value = "page", required = false, defaultValue = "1") Long page,
                                   @RequestParam(value = "rows", required = false, defaultValue = "20") Long rows,
                                          HttpServletRequest request) {
        return naviWpbsService.getJzyPrintCheckOa(formId,xmId,jdcode,zyjds,page,rows,SessionUtil.getLdapUserInfo(request).getEmployeeNo());
    }




}
