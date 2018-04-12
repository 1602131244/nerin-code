package com.nerin.nims.opt.navi.api;

import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.navi.dto.*;
import com.nerin.nims.opt.navi.service.PhaseService;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yinglgu on 7/30/2016.
 */
@RestController
@RequestMapping("/api/phaseRest")
public class PhaseRestController {

    @Autowired
    private PhaseService phaseService;

    /**
     * 项目开工报告
     * @param projectId
     * @param phaseCode
     * @return
     */
    @RequestMapping(value = "phaseForXmkgbg", method = RequestMethod.GET)
    public List<PhaseForXmkgbgDTO> queryPhaseForXmkgbgDTOList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                            @RequestParam(value = "phaseCode", required = false, defaultValue = "") String phaseCode) {
        return phaseService.getPhaseForXmkgbgDTOList(projectId, phaseCode);
    }

    /**
     * 专业开工报告
     * @param projectId
     * @param phaseCode
     * @param isManager
     * @param request
     * @return
     */
    @RequestMapping(value = "phaseForZykgbg", method = RequestMethod.GET)
    public List<PhaseForZykgbgDTO> queryPhaseForZykgbgDTOList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                              @RequestParam(value = "phaseCode", required = false, defaultValue = "") Long phaseCode,
                                                              @RequestParam(value = "isManager", required = false, defaultValue = "") Long isManager,
                                                              HttpServletRequest request) {
        return phaseService.getPhaseForZykgbgDTOList(projectId, SessionUtil.getLdapUserInfo(request).getUserId(), phaseCode, isManager);
    }

    /**
     * OA流程
     * @param taskHeaderId
     * @return
     */
    @RequestMapping(value = "phaseForOalc", method = RequestMethod.GET)
    public List<PhaseForOalcDTO> queryPhaseForOalcDTOList(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId) {
        return phaseService.getPhaseForOalcDTOList(taskHeaderId);
    }

    /**
     * 工作包执行进度-专业
     * @param projectId
     * @param phaseCode
     * @param isManager
     * @param request
     * @return
     */
    @RequestMapping(value = "phaseForGzbzxjd_zy", method = RequestMethod.GET)
    public List<PhaseForGzbzxjd_zy_DTO> queryPhaseForGzbzxjd_zy_DTOList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                                        @RequestParam(value = "phaseCode", required = false, defaultValue = "") Long phaseCode,
                                                                        @RequestParam(value = "isManager", required = false, defaultValue = "") Long isManager,
                                                                        HttpServletRequest request) {
        return phaseService.getPhaseForGzbzxjd_zy_DTOList(projectId, SessionUtil.getLdapUserInfo(request).getUserId(), phaseCode, isManager);
    }

    /**
     * 工作包执行进度-树
     * @param projectId
     * @param phaseCode
     * @param isManager
     * @param speciality
     * @param request
     * @return
     */
    @RequestMapping(value = "phaseForGzbzxjd_gzbTree", method = RequestMethod.GET)
    public List<PhaseForGzbzxjd_zy_DTO> queryPhaseForGzbzxjd_gzbTree_DTOList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                                        @RequestParam(value = "phaseCode", required = false, defaultValue = "") Long phaseCode,
                                                                        @RequestParam(value = "isManager", required = false, defaultValue = "") Long isManager,
                                                                        @RequestParam(value = "speciality", required = false, defaultValue = "") String speciality,
                                                                        HttpServletRequest request) {
        return phaseService.getPhaseForGzbzxjd_gzbTree_DTOList(projectId, SessionUtil.getLdapUserInfo(request).getUserId(), phaseCode, speciality, isManager);
    }

    /**
     * 工作包类型
     * @param projectId
     * @param phaseCode
     * @param isManager
     * @param speciality
     * @param request
     * @return
     */
    @RequestMapping(value = "phaseForGzblx", method = RequestMethod.GET)
    public List<PhaseForGzblxDTO> queryPhaseForGzblxDTOList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                            @RequestParam(value = "phaseCode", required = false, defaultValue = "") Long phaseCode,
                                                            @RequestParam(value = "isManager", required = false, defaultValue = "") Long isManager,
                                                            @RequestParam(value = "speciality", required = false, defaultValue = "") String speciality,
                                                            HttpServletRequest request) {
        return phaseService.getPhaseForGzblxDTOList(projectId, SessionUtil.getLdapUserInfo(request).getUserId(), phaseCode, speciality, isManager);
    }

    /**
     * 工作包执行进度
     * @param projectId
     * @param phaseCode
     * @param isManager
     * @param specialitys
     * @param workpkgTypeCode
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "phaseForGzbzxjd", method = RequestMethod.POST)
    public DataTablesDTO queryPhaseForPhaseForGzbzxjdDTOList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                             @RequestParam(value = "phaseCode", required = false, defaultValue = "") Long phaseCode,
                                                             @RequestParam(value = "isManager", required = false, defaultValue = "") Long isManager,
                                                             @RequestParam(value = "specialitys", required = false, defaultValue = "") String specialitys,
                                                             @RequestParam(value = "workpkgTypeCode", required = false, defaultValue = "") String workpkgTypeCode,
                                                             @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                             @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                                             HttpServletRequest request) {
        return phaseService.getPhaseForPhaseForGzbzxjdDTOList(projectId, SessionUtil.getLdapUserInfo(request).getUserId(), phaseCode, specialitys, isManager, workpkgTypeCode, pageNo, pageSize);
    }

    /**
     * 工作包执行进度-工作包明细
     * @param projElementId
     * @return
     */
    @RequestMapping(value = "phaseForGzbzxjdDetail", method = RequestMethod.GET)
    public List<PhaseForGzbzxjdDTO> queryPhaseForPhaseForGzbzxjdDetailDTOList(@RequestParam(value = "projElementId", required = false, defaultValue = "") Long projElementId) {
        return phaseService.getPhaseForPhaseForGzbzxjdDetailDTOList(projElementId);
    }

    /**
     * 接口条件收件箱
     * @param projectId
     * @param phaseCode
     * @param isManager
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "phaseForJktjsjx", method = RequestMethod.GET)
    public DataTablesDTO queryPhaseForJktjsjxDTOList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                             @RequestParam(value = "phaseCode", required = false, defaultValue = "") Long phaseCode,
                                                             @RequestParam(value = "isManager", required = false, defaultValue = "") Long isManager,
                                                             @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                             @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                                             HttpServletRequest request) {
        return phaseService.getPhaseForJktjsjxDTOList(projectId, SessionUtil.getLdapUserInfo(request).getUserId(), phaseCode, isManager, pageNo, pageSize);
    }

    /**
     * 工作包进度-文本列表
     * @param projElementId
     * @return
     */
    @RequestMapping(value = "phaseForGzbzxjd_wb", method = RequestMethod.GET)
    public List<PhaseForXmkgbgDTO> queryPhaseForGzbzxjd_wb_List(@RequestParam(value = "projElementId", required = false, defaultValue = "") Long projElementId) {
        return phaseService.getPhaseForGzbzxjd_wb_List(projElementId);
    }

    /**
     * OA-文本
     * @param taskHeaderId
     * @return
     */
    @RequestMapping(value = "phaseForOalc_wb", method = RequestMethod.GET)
    public List<PhaseForOalcDTO> queryPhaseForOalc_wb_DTOList(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId) {
        return phaseService.getPhaseForOalc_wb_DTOList(taskHeaderId);
    }

    /**
     * 工作包进度-条件单
     * @param projElementId
     * @return
     */
    @RequestMapping(value = "phaseForGzbzxjd_tjd", method = RequestMethod.GET)
    public List<PhaseForTjdDTO> queryPhaseForGzbzxjd_tjd_List(@RequestParam(value = "projElementId", required = false, defaultValue = "") Long projElementId,
                                                              HttpServletRequest request) {
        return phaseService.getPhaseForGzbzxjd_tjd_List(projElementId,SessionUtil.getLdapUserInfo(request).getUserId());
    }

    /**
     * 条件单--生成编码
     * @param pConditionId
     * @return
     */
    @RequestMapping(value = "phaseForGzbzxjd_tjd_num", method = RequestMethod.GET)
    public String queryPhaseForGzbzxjd_tjd_List(@RequestParam(value = "pConditionId", required = false, defaultValue = "") Long pConditionId
                                                             ) {
        return phaseService.setConditionNum(pConditionId);
    }

    /**
     * OA-条件单
     * @param conditionId
     * @return
     */
    @RequestMapping(value = "phaseForOalc_tjd", method = RequestMethod.GET)
    public List<OaWorkFlowDTO> queryPhaseForOalc_tjd_DTOList(@RequestParam(value = "conditionId", required = false, defaultValue = "") Long conditionId) {
        return phaseService.getPhaseForOalc_tjd_DTOList(conditionId);
    }

    /**
     * 工作包进度-计算书
     * @param projElementId
     * @return
     */
    @RequestMapping(value = "phaseForGzbzxjd_jss", method = RequestMethod.GET)
    public List<PhaseForJssTDO> queryPhaseForGzbzxjd_jss_List(@RequestParam(value = "projElementId", required = false, defaultValue = "") Long projElementId) {
        return phaseService.getPhaseForGzbzxjd_jss_List(projElementId);
    }

    /**
     * 协作文本执行进度头
     * @param projectId
     * @param phaseCode
     * @return
     */
    @RequestMapping(value = "phaseForXzwbzxjdHeader", method = RequestMethod.GET)
    public List<PhaseForXzwbzxjdHeaderDTO> queryPhaseForXzwbzxjd(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                           @RequestParam(value = "phaseCode", required = false, defaultValue = "") String phaseCode) {
        return phaseService.getPhaseForXzwbzxjdHeader(projectId, phaseCode);
    }

    /**
     * 协作文本执行进度
     * @param taskHeaderId
     * @return
     */
    @RequestMapping(value = "phaseForXzwbzxjd", method = RequestMethod.GET)
    public List<PhaseForXzwbzxjdDTO> queryPhaseForXzwbzxjd(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId) {
        return phaseService.getPhaseForXzwbzxjd(taskHeaderId);
    }

    /**
     * 建筑院接口条件收件箱-专业
     * @param projectId
     * @param phaseCode
     * @param isManager
     * @param request
     * @return
     */
    @RequestMapping(value = "phaseForJzyJktjsjx", method = RequestMethod.GET)
    public List<PhaseForGzbzxjd_zy_DTO> queryPhaseForJzyJktjzyDTOList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                     @RequestParam(value = "phaseCode", required = false, defaultValue = "") Long phaseCode,
                                                     @RequestParam(value = "isManager", required = false, defaultValue = "") Long isManager,
                                                     HttpServletRequest request) {
        return phaseService.getPhaseForJktjzyDTOList(projectId, phaseCode,SessionUtil.getLdapUserInfo(request).getUserId(),  isManager);
    }

    /**
     * 建筑院接口条件-树
     * @param projectId
     * @param phaseCode
     * @param isManager
     * @param speciality
     * @param request
     * @return
     */
    @RequestMapping(value = "phaseForJzy_Tree", method = RequestMethod.GET)
    public List<PhaseForGzbzxjd_zy_DTO> queryPhaseForjzy_Tree_DTOList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                                             @RequestParam(value = "phaseCode", required = false, defaultValue = "") Long phaseCode,
                                                                             @RequestParam(value = "isManager", required = false, defaultValue = "") Long isManager,
                                                                             @RequestParam(value = "speciality", required = false, defaultValue = "") String speciality,
                                                                             HttpServletRequest request) {
        return phaseService.getJzy_Tree_DTOList(projectId, phaseCode,SessionUtil.getLdapUserInfo(request).getUserId(),  speciality, isManager);
    }
}
