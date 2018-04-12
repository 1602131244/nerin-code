package com.nerin.nims.opt.wbsp.api;

import com.nerin.nims.opt.wbsp.dto.*;
import com.nerin.nims.opt.wbsp.service.PaLevThreePlanService;
import com.nerin.nims.opt.wbsp.service.PaLevTwoPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by 100096 on 2016/12/27.
 */
@RestController
@RequestMapping("/api/lev3")
public class PaLevThreePlanController {
    @Autowired
    private PaLevThreePlanService paLevThreePlanService;
    @Autowired
    private PaLevTwoPlanService paLevTwoPlanService;

    /**
     * 进入项目，初始化项目所需信息
     *
     * @param projectId
     * @param phaseCode
     * @return
     */
    @RequestMapping(value = "enterProject", method = RequestMethod.GET)
    public PaInfo3RepDTO getProjectInfo(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                        @RequestParam(value = "phaseID", required = false, defaultValue = "") String phaseCode,
                                        @RequestParam(value = "userID", required = true, defaultValue = "") long userId,
                                        HttpServletRequest request) {
        request.getSession();
        return paLevThreePlanService.getProjectInfo(projectId, phaseCode, userId);
    }

    /**
     * 工作包类型列表
     *
     * @return
     */
    @RequestMapping(value = "getDlvrClass", method = RequestMethod.GET)
    public List<DeliverableClassDTO> getDeliverableClass() {
        return paLevThreePlanService.getDeliverableClass();
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
    @RequestMapping(value = "getDeliverableTypeList", method = RequestMethod.GET)
    public DataTablesDTO getDeliverableTypeList(@RequestParam(value = "keywords", required = false, defaultValue = "") String queryParams,
                                                @RequestParam(value = "class", required = false, defaultValue = "") String drlvClass,
                                                @RequestParam(value = "orgID", required = false, defaultValue = "101") long orgId,
                                                @RequestParam(value = "page", required = false, defaultValue = "1") long pageNo,
                                                @RequestParam(value = "rows", required = false, defaultValue = "10") long pageSize,
                                                @RequestParam(value = "spec", required = false, defaultValue = "") String spec,
                                                @RequestParam(value = "phaseID", required = false, defaultValue = "") String phaseID) {
        return paLevThreePlanService.getDeliverableTypeList(queryParams, drlvClass, orgId, pageNo, pageSize, spec, phaseID);
    }

    /**
     * 条件单使用 --取所有专业信息
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "getDlvrSpecList", method = RequestMethod.GET)
    public List<PaIndustriesAllDTO> getDeliverableSpecList(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId) {
        return paLevThreePlanService.getDeliverableSpecList(projectId);
    }

    /**
     * 取历史项目各版
     *
     * @param queryTerm
     * @param pClass
     * @param tgId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "queryHistoryProjectList", method = RequestMethod.GET)
    public DataTablesDTO queryHistoryProjectList(@RequestParam(value = "keywords", required = false, defaultValue = "") String queryTerm,
                                                 @RequestParam(value = "class", required = false, defaultValue = "") String pClass,
                                                 @RequestParam(value = "tgid", required = false, defaultValue = "1") long tgId,
                                                 @RequestParam(value = "page", required = false, defaultValue = "1") long pageNo,
                                                 @RequestParam(value = "rows", required = false, defaultValue = "10") long pageSize,
                                                 @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        if (pClass.equals("全部行业")) {
            pClass = null;
        }
        return paLevThreePlanService.queryHistoryProjectList(queryTerm, pClass, tgId, userId, pageNo, pageSize);

    }

    /**
     * 历史项目的WBS结构和工作包
     *
     * @param structureId
     * @return
     */
    @RequestMapping(value = "getHisWbsDlvrList", method = RequestMethod.GET)
    public List<TreePaHistoryWbsDeliverablesDTO> getHisWbsDlvrList(@RequestParam(value = "wbsID", required = true, defaultValue = "") long structureId,
                                                                   @RequestParam(value = "spec", required = false, defaultValue = "") String spec) {
        StuctureExtInfoDTO tmp = new StuctureExtInfoDTO();
        tmp = paLevTwoPlanService.structureExtInfo1(structureId);
        String level = tmp.getFlag();
        return paLevThreePlanService.treeHistoryWbsDeliverableList(paLevThreePlanService.getHisWbsDeliverables(structureId, spec), level);
    }

    /**
     * 历史项目的PBS结构和工作包
     *
     * @param structureId
     * @return
     */
    @RequestMapping(value = "getHisPbsDlvrList", method = RequestMethod.GET)
    public List<TreePaHistoryPbsDeliverablesDTO> getHisPbsDlvrList(@RequestParam(value = "wbsID", required = true, defaultValue = "") long structureId,
                                                                   @RequestParam(value = "spec", required = false, defaultValue = "") String spec,
                                                                   @RequestParam(value = "orgID", required = true, defaultValue = "101") long orgId) {
        return paLevThreePlanService.treeHistoryPbsDeliverableList(paLevThreePlanService.getHisPbsDeliverables(structureId, spec, orgId));
    }

    /**
     * 最新发布版结构和工作包
     *
     * @param projectId
     * @param phaseCode
     * @param structureVerId
     * @param spec
     * @return
     */
    @RequestMapping(value = "getPubDlvrList", method = RequestMethod.GET)
    public List<TreePaPubDeliverableDTO> getPubDlvrList(@RequestParam(value = "projectId", required = true, defaultValue = "") long projectId,
                                                        @RequestParam(value = "phaseCode", required = false, defaultValue = "") String phaseCode,
                                                        @RequestParam(value = "structureVerId", required = true, defaultValue = "") long structureVerId,
                                                        @RequestParam(value = "spec", required = false, defaultValue = "") String spec,
                                                        @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        StuctureExtInfoDTO tmp = new StuctureExtInfoDTO();
        tmp = paLevTwoPlanService.structureExtInfo(structureVerId, phaseCode, "Y");
        String level = tmp.getFlag();
        return paLevThreePlanService.treePublishDeliverableList(paLevThreePlanService.getPublishDeliverables(projectId, phaseCode, structureVerId, spec, userId), level);
    }


    /**
     * 取所有设校审人员
     *
     * @param projectId
     * @param spec
     * @return
     */
    @RequestMapping(value = "getAssignmentListAll", method = RequestMethod.GET)
    public List<PAAssignmentListDTO> getAssignmentListAll(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                                          @RequestParam(value = "specialty", required = false, defaultValue = "") String spec,
                                                          HttpServletRequest request) {
        request.getSession();
        List<PAAssignmentDTO> results = new ArrayList<PAAssignmentDTO>();
        results = paLevThreePlanService.getAssignmentListAll(projectId, spec);
        List<PAAssignmentListDTO> pAAssignmentListDTO = new ArrayList<PAAssignmentListDTO>();
        List<PAMemberSpecDTO> paMemberSpecs = new ArrayList<PAMemberSpecDTO>();
        List<PAMemberDTO> paMember = null;
        PAMemberSpecDTO tmp = null;
        PAAssignmentListDTO tmp1 = null;
        PAAssignmentListDTO tmp3 = null;
        PAMemberDTO tmp2 = null;
        //取出专业和职责组合
        for (int i = 0; i < results.size(); i++) {
            tmp = new PAMemberSpecDTO();
            tmp.setSpecialty(results.get(i).getSpecialty());
            tmp.setDuty(results.get(i).getDuty());
            paMemberSpecs.add(tmp);
        }
        //去掉重复的专业和职责组合
       /* for (int i = 0; i < paMemberSpecs.size(); i++) {
            HashSet<PAMemberSpecDTO> h = new HashSet<PAMemberSpecDTO>(paMemberSpecs);
            paMemberSpecs.addAll(h);
        }*/

        for (int i = 0; i < paMemberSpecs.size() - 1; i++) {
            for (int j = paMemberSpecs.size() - 1; j > i; j--) {
                if (paMemberSpecs.get(j).getDuty().equals(paMemberSpecs.get(i).getDuty()) && paMemberSpecs.get(j).getSpecialty().equals(paMemberSpecs.get(i).getSpecialty())) {
                    paMemberSpecs.remove(j);
                }
            }

        }

        //循环比较 把同一专业 职责的人员归类
        for (int i = 0; i < paMemberSpecs.size(); i++) {
            paMember = new ArrayList<PAMemberDTO>();
            tmp3 = new PAAssignmentListDTO();
            tmp3.setPaMemberSpecDTO(paMemberSpecs.get(i));
            for (int j = 0; j < results.size(); j++) {
                if (paMemberSpecs.get(i).getDuty().equals(results.get(j).getDuty()) &&
                        paMemberSpecs.get(i).getSpecialty().equals(results.get(j).getSpecialty())) {
                    tmp2 = new PAMemberDTO();
                    tmp2.setPerNum(results.get(j).getPerNum());
                    tmp2.setPerId(results.get(j).getPerId());
                    tmp2.setPerName(results.get(j).getPerName());
                    paMember.add(tmp2);
                }
            }
            tmp3.setMember(paMember);
            pAAssignmentListDTO.add(tmp3);
        }
        return pAAssignmentListDTO;
    }

    /**
     * 取专业负责人
     *
     * @param projectId
     * @param spec
     * @return
     */
    @RequestMapping(value = "getProfessionListAll", method = RequestMethod.GET)
    public List<PAAssignmentListDTO> getProfessionListAll(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                                          @RequestParam(value = "specialty", required = false, defaultValue = "") String spec,
                                                          HttpServletRequest request) {
        request.getSession();
        List<PAAssignmentDTO> results = new ArrayList<PAAssignmentDTO>();
        results = paLevThreePlanService.getProfessionListAll(projectId, spec);
        List<PAAssignmentListDTO> pAAssignmentListDTO = new ArrayList<PAAssignmentListDTO>();
        List<PAMemberSpecDTO> paMemberSpecs = new ArrayList<PAMemberSpecDTO>();
        List<PAMemberDTO> paMember = null;
        PAMemberSpecDTO tmp = null;
        PAAssignmentListDTO tmp1 = null;
        PAAssignmentListDTO tmp3 = null;
        PAMemberDTO tmp2 = null;
        //取出专业和职责组合
        for (int i = 0; i < results.size(); i++) {
            tmp = new PAMemberSpecDTO();
            tmp.setSpecialty(results.get(i).getSpecialty());
            tmp.setDuty(results.get(i).getDuty());
            paMemberSpecs.add(tmp);
        }
        //去掉重复的专业和职责组合
       /* for (int i = 0; i < paMemberSpecs.size(); i++) {
            HashSet<PAMemberSpecDTO> h = new HashSet<PAMemberSpecDTO>(paMemberSpecs);
            paMemberSpecs.addAll(h);
        }*/

        for (int i = 0; i < paMemberSpecs.size() - 1; i++) {
            for (int j = paMemberSpecs.size() - 1; j > i; j--) {
                if (paMemberSpecs.get(j).getDuty().equals(paMemberSpecs.get(i).getDuty()) && paMemberSpecs.get(j).getSpecialty().equals(paMemberSpecs.get(i).getSpecialty())) {
                    paMemberSpecs.remove(j);
                }
            }

        }

        //循环比较 把同一专业 职责的人员归类
        for (int i = 0; i < paMemberSpecs.size(); i++) {
            paMember = new ArrayList<PAMemberDTO>();
            tmp3 = new PAAssignmentListDTO();
            tmp3.setPaMemberSpecDTO(paMemberSpecs.get(i));
            for (int j = 0; j < results.size(); j++) {
                if (paMemberSpecs.get(i).getDuty().equals(results.get(j).getDuty()) &&
                        paMemberSpecs.get(i).getSpecialty().equals(results.get(j).getSpecialty())) {
                    tmp2 = new PAMemberDTO();
                    tmp2.setPerNum(results.get(j).getPerNum());
                    tmp2.setPerId(results.get(j).getPerId());
                    tmp2.setPerName(results.get(j).getPerName());
                    paMember.add(tmp2);
                }
            }
            tmp3.setMember(paMember);
            pAAssignmentListDTO.add(tmp3);
        }
        return pAAssignmentListDTO;
    }

    /**
     * 取项目经理公司分管项目秘书
     *
     * @param projectId
     * @param spec
     * @return
     */
    @RequestMapping(value = "getMemberList", method = RequestMethod.GET)
    public Map getMemberList(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                             HttpServletRequest request) {
        request.getSession();
        Map result = new HashMap();
        List<PAAssignmentDTO> results = new ArrayList<PAAssignmentDTO>();
        results = paLevThreePlanService.getMemberList(projectId);
        List<PAMemberListDTO> paMemberListDTO = new ArrayList<PAMemberListDTO>();
        List<PAMemberSpecDTO> paMemberSpecs = new ArrayList<PAMemberSpecDTO>();
        List<PAMemberDTO> paMember = null;
        PAMemberSpecDTO tmp = null;
        PAMemberListDTO tmp3 = null;
        PAMemberDTO tmp2 = null;
        //取出专业和职责组合
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i).getDuty().equals("项目经理")) {
                tmp2 = new PAMemberDTO();
                tmp2.setPerNum(results.get(i).getPerNum());
                tmp2.setPerId(results.get(i).getPerId());
                tmp2.setPerName(results.get(i).getPerName());
                result.put("mgr", tmp2);
            } else {
                tmp = new PAMemberSpecDTO();
                tmp.setDuty(results.get(i).getDuty());
                paMemberSpecs.add(tmp);
            }


        }
        //去掉重复的专业和职责组合
       /* for (int i = 0; i < paMemberSpecs.size(); i++) {
            HashSet<PAMemberSpecDTO> h = new HashSet<PAMemberSpecDTO>(paMemberSpecs);
            paMemberSpecs.addAll(h);
        }*/

        for (int i = 0; i < paMemberSpecs.size() - 1; i++) {
            for (int j = paMemberSpecs.size() - 1; j > i; j--) {
                if (paMemberSpecs.get(j).getDuty().equals(paMemberSpecs.get(i).getDuty())) {
                    paMemberSpecs.remove(j);
                }
            }
        }

        //循环比较 把同一专业 职责的人员归类
        for (int i = 0; i < paMemberSpecs.size(); i++) {
            paMember = new ArrayList<PAMemberDTO>();
            tmp3 = new PAMemberListDTO();
            tmp3.setDuty(paMemberSpecs.get(i).getDuty());
            for (int j = 0; j < results.size(); j++) {
                if (paMemberSpecs.get(i).getDuty().equals(results.get(j).getDuty())) {
                    tmp2 = new PAMemberDTO();
                    tmp2.setPerNum(results.get(j).getPerNum());
                    tmp2.setPerId(results.get(j).getPerId());
                    tmp2.setPerName(results.get(j).getPerName());
                    paMember.add(tmp2);
                }
            }
            tmp3.setMember(paMember);
            paMemberListDTO.add(tmp3);
            result.put("oth", paMemberListDTO);
        }
        return result;
    }

    /**
     * 执行保存
     *
     * @param dlvrSaveDTO
     * @return
     */
    @RequestMapping(value = "saveDlvr", method = RequestMethod.POST)
    public Map saveDlvr(@RequestBody DlvrSaveDTO dlvrSaveDTO,
                        @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {

        return paLevThreePlanService.saveDlvr(dlvrSaveDTO, userId);
    }

    /**
     * 跳图纸状态列表
     *
     * @param dlvrSaveDTO
     * @return
     */
    @RequestMapping(value = "saveChange", method = RequestMethod.POST)
    public DwgStatusChangeResponseDTO saveChange(@RequestBody DlvrSaveDTO dlvrSaveDTO,
                                                 @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        return paLevThreePlanService.saveChange(userId, dlvrSaveDTO);
    }


    /**
     * 取消图纸状态列表
     *
     * @param projectId
     * @param phaseCode
     * @return
     */
    @RequestMapping(value = "cancelChange", method = RequestMethod.GET)
    public Map cancelChange(@RequestParam(value = "projectId", required = true, defaultValue = "") long projectId,
                            @RequestParam(value = "projectId", required = true, defaultValue = "") String phaseCode) {
        return paLevThreePlanService.cancelChange(projectId, phaseCode);
    }

    /**
     * 锁定
     */
    @RequestMapping(value = "checkOutSpec", method = RequestMethod.POST)
    public Map checkOutSpec(@RequestBody LockSpecReqDTO lockSpecReqDTO, @RequestParam(value = "personID", required = true, defaultValue = "") long userId,
                            HttpServletRequest request) {
        return paLevThreePlanService.lockSpec(userId, request.getSession().getId(), lockSpecReqDTO);
    }

    /**
     * 解锁
     */
    @RequestMapping(value = "checkInSpec", method = RequestMethod.POST)
    public Map checkInSpec(@RequestBody LockSpecReqDTO lockSpecReqDTO,
                           HttpServletRequest request) {
        return paLevThreePlanService.unlockSpec(/*SessionUtil.getLdapUserInfo(request).getUserId()*/lockSpecReqDTO);
    }
}
