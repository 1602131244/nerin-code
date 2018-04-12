package com.nerin.nims.opt.wbsp.api;

import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.wbsp.dto.*;
import com.nerin.nims.opt.wbsp.service.PaLevTwoPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/26.
 */

@RestController
@RequestMapping("/api/lev2")
public class PaLevTwoPlanController {
    @Autowired
    private PaLevTwoPlanService paLevTwoPlanService;

    /**
     * @param projectId
     * @param phaseCode
     * @param request
     * @return
     */
    @RequestMapping(value = "enterProject", method = RequestMethod.GET)
    public PaInfo2RepDTO getProjectInfo(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                     @RequestParam(value = "phaseID", required = false, defaultValue = "") String phaseCode,
                                     @RequestParam(value = "userID", required = true, defaultValue = "") long userId,
                                     HttpServletRequest request) {
        //List<PaInfo2DTO> paInfoDTOs = new ArrayList<PaInfo2DTO>();
        //paInfoDTOs.add(paLevTwoPlanService.getProjectInfo(projectId, phaseCode, userId));
        request.getSession();
        return paLevTwoPlanService.getProjectInfo(projectId, phaseCode, userId);
    }

    /**
     * 二级策划界面项目切换模糊查找项目列表及信息
     *
     * @param params
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "queryProjectList", method = RequestMethod.GET)
    public DataTablesDTO queryProjectList(@RequestParam(value = "keywords", required = false, defaultValue = "") String params,
                                          @RequestParam(value = "page", required = false, defaultValue = "1") long pageNo,
                                          @RequestParam(value = "rows", required = false, defaultValue = "300") long pageSize,
                                          @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        return paLevTwoPlanService.getSearchProjectList(params, userId, pageNo, pageSize);
    }

    /**
     * 二级策划界面项目切换当前项目阶段列表
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "queryPhaseList", method = RequestMethod.GET)
    public List<PaPhaseListDTO> queryPhaseList(@RequestParam(value = "projID", required = true, defaultValue = "") Long projectId) {
        return paLevTwoPlanService.getProjectPhaseList(projectId);
    }

    /**
     * 取当前项目发布版
     *
     * @param projectId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "queryPublishStructureList", method = RequestMethod.GET)
    public DataTablesDTO queryPaPubilshStructureList(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                                     @RequestParam(value = "phaseID", required = true, defaultValue = "") String phaseCode,
                                                     @RequestParam(value = "page", required = false, defaultValue = "1") long pageNo,
                                                     @RequestParam(value = "rows", required = false, defaultValue = "300") long pageSize) {
        return paLevTwoPlanService.getPaPubilshStructureList(projectId, phaseCode, pageNo, pageSize);
    }

    /**
     * 取发布版的系统子项
     *
     * @param phaseCode
     * @param structureVerId
     * @return
     */
    @RequestMapping(value = "queryPublishStructureDetails", method = RequestMethod.GET)
    public ThePubilshDataDTO queryPublishStructureDetails(@RequestParam(value = "phaseCode", required = false, defaultValue = "") String phaseCode,
                                                                               @RequestParam(value = "pubwbsID", required = true, defaultValue = "") long structureVerId) {

        ThePubilshDataDTO thePubilshDataDTO = new ThePubilshDataDTO();
        StuctureExtInfoDTO tmp = new StuctureExtInfoDTO();
        tmp = paLevTwoPlanService.structureExtInfo(structureVerId, phaseCode,"N");
        String level = tmp.getFlag();
        if(tmp.getDivsion() != null){
            thePubilshDataDTO.setDivison(getBool(tmp.getDivsion()));
            thePubilshDataDTO.setExists(true);
        }else
        { thePubilshDataDTO.setExists(false);}
        thePubilshDataDTO.setPaDetailsDTO(paLevTwoPlanService.treePublishList(paLevTwoPlanService.getPublishStructureDetails(phaseCode, structureVerId), level));
        return thePubilshDataDTO;
        }

   /* *//**
     * 取历史项目
     *
     * @param queryTerm
     * @param pageNo
     * @param pageSize
     * @return
     *//*
    @RequestMapping(value = "queryHistoryProjectList", method = RequestMethod.GET)
    public List<PaHistoryProjectDTO> queryHistoryProjectList(@RequestParam(value = "keywords", required = false, defaultValue = "") String queryTerm,
                                                             @RequestParam(value = "class", required = false, defaultValue = "") String pClass,
                                                             @RequestParam(value = "tgid", required = false, defaultValue = "1")long tgId,
                                                             @RequestParam(value = "page", required = false, defaultValue = "1") long pageNo,
                                                             @RequestParam(value = "rows", required = false, defaultValue = "10") long pageSize,
                                                             @RequestParam(value = "userID", required = true, defaultValue = "0" ) long userId) {
        if(pClass.equals("全部行业")){pClass=null;}
        return paLevTwoPlanService.queryHistoryProjectList(queryTerm, pClass,tgId, userId, pageNo, pageSize);

    }

    *//**
     * 取历史项目各版
     * @param parentsId
     * @param projectId
     * @param tgId
     * @return
     *//*
    @RequestMapping(value = "queryHistoryStructureList", method = RequestMethod.GET)
    public List<TreePaHistoryStructureDTO> queryHistoryStructureList(@RequestParam(value = "id", required = true, defaultValue = "") long parentsId,
                                                                     @RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                                               @RequestParam(value = "tgid", required = true, defaultValue = "") long tgId) {
        return paLevTwoPlanService.getPaHistoryStructureList(parentsId,projectId,tgId);
    }*/

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
        return paLevTwoPlanService.queryHistoryProjectList(queryTerm, pClass, tgId, userId, pageNo, pageSize);

    }

    /**
     * 取历史项目WBS的系统子项
     *
     * @param structureId
     * @return
     */
    @RequestMapping(value = "queryHistoryWbsDetails", method = RequestMethod.GET)
    public TheHistoryDataDTO getHistoryWbsDetails(@RequestParam(value = "wbsID", required = true, defaultValue = "") long structureId) {
        TheHistoryDataDTO theHistoryDataDTO = new TheHistoryDataDTO();
        StuctureExtInfoDTO tmp = new StuctureExtInfoDTO();
        tmp = paLevTwoPlanService.structureExtInfo1(structureId);
        String level = tmp.getFlag();
        if(tmp.getDivsion() != null){
            theHistoryDataDTO.setDivison(getBool(tmp.getDivsion()));
            theHistoryDataDTO.setExists(true);
        }else
        { theHistoryDataDTO.setExists(false);}

        theHistoryDataDTO.setPaDetailsDTO( paLevTwoPlanService.treeHisWbsDetailsList(paLevTwoPlanService.getHistoryWbsStructureDetails(structureId),level));
        return theHistoryDataDTO;
    }


    /**
     * 取历史项目PBS的系统子项
     *
     * @param structureId
     * @return
     */
    @RequestMapping(value = "queryHistoryPbsDetails", method = RequestMethod.GET)
    public List<TreePaHistoryPbsDetailsDTO> getHistoryPbsDetails(@RequestParam(value = "pbsID", required = true, defaultValue = "") long structureId) {
        return paLevTwoPlanService.treeHisPbsDetailsList(paLevTwoPlanService.getHistoryPbsStructureDetails(structureId));


    }

    /**
     * 取正在编辑版的结构
     *
     * @param strucVerId
     * @param phaseCode
     * @return
     */
    @RequestMapping(value = "queryWorkingStructure", method = RequestMethod.GET)
    public List<PaWorkingStructureDTO> getWorkingStructure(@RequestParam(value = "draftwbsID", required = true, defaultValue = "") long strucVerId,
                                                           @RequestParam(value = "prjPhaseID", required = false, defaultValue = "") String phaseCode) {
        List<PaWorkingStructureDTO> paWorkingStructureDTOs = new ArrayList<PaWorkingStructureDTO>();
        paWorkingStructureDTOs.add(paLevTwoPlanService.getWorkingStructure(strucVerId, phaseCode));
        return paWorkingStructureDTOs;
    }

    /**
     * 取正在编辑版的系统子项
     *
     * @param phaseCode
     * @param structureVerId
     * @return
     */
    @RequestMapping(value = "queryWorkingStructureDetails", method = RequestMethod.GET)
    public TheWokingDataDTO getWorkingStructureDetails(@RequestParam(value = "prjPhaseID", required = false, defaultValue = "") String phaseCode,
                                                                             @RequestParam(value = "draftwbsID", required = true, defaultValue = "") long structureVerId
    ) { TheWokingDataDTO theWokingDataDTO = new TheWokingDataDTO();
        StuctureExtInfoDTO tmp = new StuctureExtInfoDTO();
        tmp = paLevTwoPlanService.structureExtInfo(structureVerId, phaseCode,"Y");
        String level = tmp.getFlag();
        if(tmp.getDivsion() != null){
            theWokingDataDTO.setDivison(getBool(tmp.getDivsion()));
            theWokingDataDTO.setExists(true);
        }else
        { theWokingDataDTO.setExists(false);}
        theWokingDataDTO.setPaDetailsDTO(paLevTwoPlanService.treePaWorkingList(paLevTwoPlanService.getWorkingStructureDetails(phaseCode, structureVerId), level));
        return theWokingDataDTO;
    }

    /**
     * 取历史系统
     *
     * @param sysName
     * @param pClass
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "queryHistorySystemList", method = RequestMethod.GET)
    public DataTablesDTO queryHistorySystemList(@RequestParam(value = "keywords", required = false, defaultValue = "") String sysName,
                                                @RequestParam(value = "class", required = false, defaultValue = "") String pClass,
                                                @RequestParam(value = "page", required = false, defaultValue = "1") long pageNo,
                                                @RequestParam(value = "rows", required = false, defaultValue = "10") long pageSize) {
        if (pClass.equals("全部行业")) {
            pClass = null;
        }
        return paLevTwoPlanService.getHistorySystemList(sysName, pClass, pageNo, pageSize);
    }

    /**
     * 取历史子项
     *
     * @param subName
     * @param pClass
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "queryHistorySubList", method = RequestMethod.GET)
    public DataTablesDTO queryHistorySubList(@RequestParam(value = "keywords", required = false, defaultValue = "") String subName,
                                             @RequestParam(value = "class", required = false, defaultValue = "") String pClass,
                                             @RequestParam(value = "page", required = false, defaultValue = "1") long pageNo,
                                             @RequestParam(value = "rows", required = false, defaultValue = "10") long pageSize) {
        if (pClass.equals("全部行业")) {
            pClass = null;
        }
        return paLevTwoPlanService.getHistorySubList(subName, pClass, pageNo, pageSize);
    }

    /**
     * 取所有的专业信息
     *
     * @return
     */
    @RequestMapping(value = "querySpecListAll", method = RequestMethod.GET)
    public List<PaIndustriesAllDTO> querySpecListAll(@RequestParam(value = "projID", required = true, defaultValue = "") String projectId) {
        return paLevTwoPlanService.getSpecListAll(projectId);
    }

    /**
     * 取项目行业
     *
     * @return
     */
    @RequestMapping(value = "queryIndustryList", method = RequestMethod.GET)
    public List<PaIndustryDTO> queryIndustryList(@RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        return paLevTwoPlanService.getIndustryList(userId);
    }

    /**
     * 取项目全部行业
     *
     * @return
     */
    @RequestMapping(value = "queryAllIndustryList", method = RequestMethod.GET)
    public List<PaIndustryDTO> queryAllIndustryList() {
        return paLevTwoPlanService.getAllIndustryList();
    }

    /**
     * 当前阶段的专业信息
     *
     * @param projectId
     * @param phaseCode
     * @return
     */
    @RequestMapping(value = "querySpecList", method = RequestMethod.GET)
    public List<PaIndustriesDTO> querySpecList(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                               @RequestParam(value = "phaseID", required = true, defaultValue = "") String phaseCode
            , @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        return paLevTwoPlanService.getSpecList(projectId, phaseCode, userId);
    }

    /**
     * 签出/锁定  初始化阶段
     *
     * @param projectId
     * @param phaseCode
     * @return
     */
    @RequestMapping(value = "lockWorkingStructure", method = RequestMethod.POST)
    public Map lockWorkingStructure(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                    @RequestParam(value = "phaseID", required = true, defaultValue = "") String phaseCode
            , @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = paLevTwoPlanService.lockWorkingStructure(projectId, phaseCode, userId);
        restFulData.putAll(reMap);
        return restFulData;
    }

    /**
     * 签入/解锁
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "unlockWorkingStructure", method = RequestMethod.POST)
    public Map unlockWorkingStructure(@RequestParam(value = "projectId", required = true, defaultValue = "") long projectId
            , @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = paLevTwoPlanService.unlockWorkingStructure(projectId, userId);
        restFulData.putAll(reMap);
        return restFulData;
    }

    /**
     * 保存版本信息及扩展内容
     *
     * @param projectId
     * @param phaseCode
     * @param structureClass
     * @param division
     * @param request
     * @return
     *//*
    @RequestMapping(value = "saveWorkingStructure", method = RequestMethod.POST)
    public Map saveWorkingStructure(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                    @RequestParam(value = "phaseID", required = true, defaultValue = "") String phaseCode,
                                    @RequestParam(value = "headOrbranch", required = true, defaultValue = "") String structureClass,
                                    @RequestParam(value = "division", required = true, defaultValue = "") boolean division
            , @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        String onlySpec = new String();
        if (division) {
            onlySpec = "Y";
        } else {
            onlySpec = "N";
        }
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = paLevTwoPlanService.saveWorkingStructure(projectId, phaseCode, userId, structureClass, onlySpec);
        restFulData.putAll(reMap);
        return restFulData;
    }*/

    /**
     * 发布版本，更新扩展内容
     *
     * @param projectId
     * @param phaseCode
     * @param structureClass
     * @param division
     * @return
     */
    @RequestMapping(value = "publishStructure", method = RequestMethod.POST)
    public Map publishStructure(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                @RequestParam(value = "phaseID", required = true, defaultValue = "") String phaseCode,
                                @RequestParam(value = "headOrbranch", required = true, defaultValue = "") String structureClass,
                                @RequestParam(value = "division", required = true, defaultValue = "") Boolean division
            , @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        String onlySpec = new String();
        if (division) {
            onlySpec = "Y";
        } else {
            onlySpec = "N";
        }
        //Map restFulData = RestFulData.getRestInitData();
        Map reMap = paLevTwoPlanService.publishStructure(projectId, phaseCode, userId, structureClass, onlySpec);
        //restFulData.putAll(reMap);
        return reMap;
    }

    /**
     * 创建专业比例
     *
     * @param paIndustriesResponDTO
     * @return
     */
    @RequestMapping(value = "creatIndustriesRate", method = RequestMethod.POST)
    public Map creatIndustriesRate(@RequestBody PaIndustriesResponDTO paIndustriesResponDTO, @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = paLevTwoPlanService.creatIndustriesRate(paIndustriesResponDTO, userId);
        restFulData.putAll(reMap);
        return restFulData;
    }

    /**
     * 获取图纸状态变化列表
     *
     * @param projectId
     * @param phaseCode
     * @return
     */
    @RequestMapping(value = "getDwgChangeList", method = RequestMethod.GET)
    public DwgStatusChangeResponseDTO getDwgChangeList(@RequestParam(value = "projID", required = true, defaultValue = "") long projectId,
                                                       @RequestParam(value = "phaseID", required = true, defaultValue = "") String phaseCode,
                                                       @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        return paLevTwoPlanService.getDwgChangeList(projectId, phaseCode, userId);
    }

    /**
     * 图纸变更状态历史列表
     *
     * @param projectId
     * @param phaseCode
     * @return
     */
    @RequestMapping(value = "getHistoryChange", method = RequestMethod.POST)
    public List<DwgHisStatusChangeDTO> getHistoryChange(@RequestParam(value = "projectId", required = true, defaultValue = "") long projectId,
                                                        @RequestParam(value = "phaseCode", required = true, defaultValue = "") String phaseCode) {
        return paLevTwoPlanService.getHistoryChange(projectId, phaseCode);
    }

    /**
     * 保存结构
     *
     * @param elementSaveDTO
     * @return
     */
    @RequestMapping(value = "addTask", method = RequestMethod.POST)
    public Map addTask(@RequestBody ElementSaveDTO elementSaveDTO,
                       @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        return paLevTwoPlanService.addTask(elementSaveDTO, userId);
    }

    /**
     * 保存
     *
     * @param phaseCode
     * @param structureVerId
     * @return
     */
    @RequestMapping(value = "updateDivision", method = RequestMethod.GET)
    public Map updateDivision(@RequestParam(value = "draftwbsID", required = true, defaultValue = "") long structureVerId,
                                           @RequestParam(value = "phaseID", required = true, defaultValue = "") String phaseCode,
                                           @RequestParam(value = "division", required = true, defaultValue = "") Boolean division,
                                           @RequestParam(value = "userID", required = true, defaultValue = "") long userId) {
        String onlySpec = new String();
        if (division) {
            onlySpec = "Y";
        } else {
            onlySpec = "N";
        }
        return paLevTwoPlanService.updateWorkingStructure(structureVerId, phaseCode, userId,onlySpec);
    }

    /**
     * taskId taskverid返前端
     *
     * @return
     */
    @RequestMapping(value = "getTaskId", method = RequestMethod.GET)
    public ResponseTaskIdDTO getTaskId() {
        return paLevTwoPlanService.getTaskId();
    }

    /**
     * taskId 批量返前端
     *
     * @return
     */
    @RequestMapping(value = "getTaskIds", method = RequestMethod.GET)
    public List<ResponseNewIdsDTO> getTaskIds(@RequestParam(value = "num", required = true, defaultValue = "1") long num) {
        return paLevTwoPlanService.getTaskIds(num);
    }

    public String getLevel(String headOrbranch, boolean division) {
        String result;
        if (division) {
            if (headOrbranch.equals("head")) {
                result = "ALL";
            } else {
                result = "SUB";
            }
        } else {
            result = "SPEC";
        }
        return result;

    }
    private Boolean getBool(String type) {
        Boolean result = false;
        switch (type) {
            case "Y":
                result = true;
                break;
            case "N":
                result = false;
                break;
        }
        return result;
    }
}
