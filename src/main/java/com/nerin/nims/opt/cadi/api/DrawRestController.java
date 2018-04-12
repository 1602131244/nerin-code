package com.nerin.nims.opt.cadi.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.ecm.ridc.RidcUtil;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.cadi.dto.*;
import com.nerin.nims.opt.cadi.service.*;
import com.nerin.nims.opt.navi.dto.MyProjectsListDTO;
import com.nerin.nims.opt.navi.dto.ProjectPhasesDTO;
import com.nerin.nims.opt.navi.service.ProjectService;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.TemplateChaptersDTO;
import oracle.stellent.ridc.IdcClientException;
import oracle.stellent.ridc.model.DataBinder;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 16/7/15.
 */
@RestController
@RequestMapping("/api/draw")
public class DrawRestController {
    @Autowired
    private NerinProperties nerinProperties;
    @Autowired
    private DrawPrintService drawPrintService;
    @Autowired
    private DrawProjectService drawProjectService;
    @Autowired
    private ProjectService projectService;

    /**
     * 打印包下载服务
     */
    @RequestMapping(value = "getZipFileByEcm", method = RequestMethod.POST)
    public Map getZipFileByEcm(@RequestParam(value = "plt_order_header_id", required = false, defaultValue = "") String plt_order_header_id,
                                  @RequestParam(value = "plt_num", required = false, defaultValue = "") String plt_num,
                                  @RequestParam(value = "plt_category", required = false, defaultValue = "") String plt_category) {
        Map restFulData = RestFulData.getRestInitData();
        List<CatLineDTO> lines = drawPrintService.searchDrawprintLines(Long.parseLong(plt_order_header_id));
        String lineString = "";
        for(CatLineDTO c : lines) {
            lineString += c.getDid() + ",";
        }
        String filePath = "";
        try {
            DataBinder mainBinder = RidcUtil.getDataBinder();
            //打印包下载代码
            mainBinder.putLocal("IdcService","GET_DOWNLOAD_PATH_PRINT");
            mainBinder.putLocal("PLT_ORDER_HEADER_ID", plt_order_header_id);
            mainBinder.putLocal("PLT_NUM", plt_num);
            mainBinder.putLocal("STA_FLAG","PRICING");
            mainBinder.putLocal("Is_Text","1");
            mainBinder.putLocal("PLT_CATEGORY", plt_category);
            mainBinder.putLocal("DOWNLOAD_TYPES","1");
            mainBinder.putLocal("TYPE_DOC","P");
            mainBinder.putLocal("DOWNLOAD_HEADER_ID", plt_order_header_id);
            mainBinder.putLocal("Is_Link","");
            mainBinder.putLocal("idString", lineString);

            // 执行服务
            DataBinder serverBinder = RidcUtil.executeService(mainBinder);
            // 将下载URL输出
            filePath = serverBinder.getLocal("cux_filepath");
            restFulData.put("filePath", filePath);
        } catch (Exception e) {
            restFulData.put(RestFulData.RETURN_CODE, "0001");
            restFulData.put(RestFulData.RETURN_MSG, "下载打印包错误");
            return restFulData;
        }
        return restFulData;
    }


    /**
     * 获取子项列表
     *
     * @param projectId
     * @param phaseId
     * @return
     */
    @RequestMapping(value = "unitTask", method = RequestMethod.GET)
    public List<UnitTaskDTO> getUnitTaskList(
            @RequestParam(value = "projectId", required = true, defaultValue = "") Long projectId,
            @RequestParam(value = "phaseId", required = false, defaultValue = "") String phaseId
    ) {
        return drawProjectService.getUnitTaskList(projectId, phaseId);
    }

    /**
     * 获取专业列表
     *
     * @param projectId
     * @param phaseId
     * @return
     */
    @RequestMapping(value = "speciality", method = RequestMethod.GET)
    public List<SpecialityDTO> getSpecialityList(
            @RequestParam(value = "projectId", required = true, defaultValue = "") Long projectId,
            @RequestParam(value = "phaseId", required = false, defaultValue = "") String phaseId
    ) {
        return drawProjectService.getSpecialityList(projectId, phaseId);
    }

    /**
     * 获得工作包列表
     *
     * @param projectId
     * @param specialityCode
     * @return
     */
    @RequestMapping(value = "dlvr", method = RequestMethod.GET)
    public List<DlvrDTO> getDlvrList(
            @RequestParam(value = "projectId", required = true, defaultValue = "") Long projectId,
            @RequestParam(value = "specialityCode", required = false, defaultValue = "") String specialityCode
    ) {
        return drawProjectService.getDlvrList(projectId, specialityCode);
    }

    /**
     * 获取图纸状态列表
     *
     * @return
     */
    @RequestMapping(value = "drawStatus", method = RequestMethod.GET)
    public List<DrawStatusDTO> getDrawStatusList(
    ) {
        return drawProjectService.getDrawStatusList();
    }

    /**
     * 获取我的项目列表
     *
     * @param paramText
     * @param inPage
     * @param inSize
     * @return
     */
    @RequestMapping(value = "myProjectsList", method = RequestMethod.GET)
    public List<MyProjectsListDTO> getMyProjectsList(
            @RequestParam(value = "paramText", required = false, defaultValue = "") String paramText,
            @RequestParam(value = "inPage", required = false, defaultValue = "1") Long inPage,
            @RequestParam(value = "inSize", required = false, defaultValue = "3000") Long inSize,
            HttpServletRequest request
    ) {
        return projectService.getMyProjectsListDTO(SessionUtil.getLdapUserInfo(request).getUserId(), paramText, inPage, inSize);
    }

    /**
     * 获取项目阶段列表
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "projectPhase", method = RequestMethod.GET)
    public List<ProjectPhasesDTO> getProjectPhases(
            @RequestParam(value = "projectId", required = true, defaultValue = "") Long projectId,
            @RequestParam(value = "docType", required = false, defaultValue = "") String docType
    ) {
        return projectService.getProjectPhasesDTO(projectId, docType);
    }

    /**
     * 获取图纸列表
     *
     * @param projectId
     * @param phaseCode
     * @param unitTaskCode
     * @param specialityCode
     * @param dlvrId
     * @param drawStatusCode
     * @param docType 图纸文印传：PJT-DRAW 文本文印不传或为空,PJT-DRAW为图纸 PJT-DESIGN-DOC为文本
     * @return
     */
    @RequestMapping(value = "draw", method = RequestMethod.GET)
    public List<DrawsDTO> getDrawsList(
            @RequestParam(value = "ProjectNumber", required = true, defaultValue = "0") Long projectId,
            @RequestParam(value = "PhaseCode", required = false, defaultValue = "") String phaseCode,
            @RequestParam(value = "UnitTaskNumber", required = false, defaultValue = "") String unitTaskCode,
            @RequestParam(value = "SpecialityCode", required = false, defaultValue = "") String specialityCode,
            @RequestParam(value = "DlvrId", required = false, defaultValue = "") Long dlvrId,
            @RequestParam(value = "DrawStatus", required = false, defaultValue = "") String drawStatusCode,
            @RequestParam(value = "docType", required = false, defaultValue = "") String docType,
            HttpServletRequest request
    ) {
        return drawPrintService.getDrawList(SessionUtil.getLdapUserInfo(request).getEmployeeNo(), projectId, phaseCode, unitTaskCode, specialityCode, dlvrId, drawStatusCode, docType);
    }

    /**
     * create tmp item;
     *
     * @param tmpPltDTO
     * @param request
     * @return
     */
    @RequestMapping(value = "refreshTmpPlt", method = RequestMethod.POST)
    public List<TmpPltDTO> RefreshTmpPlt(
            @RequestBody List<TmpPltDTO> tmpPltDTO,
            HttpServletRequest request) {
        return drawPrintService.RefreshTmpPlt(SessionUtil.getLdapUserInfo(request).getUserId(), tmpPltDTO);
    }

    @RequestMapping(value = "createDrawsCatalog", method = RequestMethod.POST)
    public Long createDrawsCatalog(@RequestBody DrawsCatalogDTO drawsCatalogDTO,
                                   HttpServletRequest request) {
        return drawPrintService.generateDrawsCatalog(drawsCatalogDTO, SessionUtil.getLdapUserInfo(request).getEmployeeNo());
    }

    /**
     * 得到当前的临时文印列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "getTmpPlt", method = RequestMethod.GET)
    public List<TmpPltDTO> getTmpPlt(HttpServletRequest request) {
        return drawPrintService.getTmpPlt(SessionUtil.getLdapUserInfo(request).getUserId());
    }

    /**
     * 获取当前图号的图纸版本
     *
     * @param drawNum
     * @return
     */
    @RequestMapping(value = "drawRevision", method = RequestMethod.GET)
    public String getDrawsList(
            @RequestParam(value = "drawNum", required = true, defaultValue = "0") String drawNum
    ) {
        return drawPrintService.getDrawRevision(drawNum);
    }

    /**
     * 获取当前项目的图纸目录模板
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "drawsCatalogTem", method = RequestMethod.GET)
    public List<DrawsCatalogTemDTO> getDrawsCatalogTem(
            @RequestParam(value = "projectId", required = true, defaultValue = "0") Long projectId
    ) {
        return drawProjectService.getDrawsCatalogTem(projectId);
    }

    /**
     * 获取当前的图纸目录对象，用来加入到待文印列表中
     *
     * @param did
     * @return
     */
    @RequestMapping(value = "drawsCatalogObj", method = RequestMethod.GET)
    public List<TmpPltDTO> getDrawsCatalogObj(@RequestParam(value = "did", required = true, defaultValue = "1576935") Long did) {
        return drawPrintService.getDrawCatalogObj(did);
    }

    /**
     * 生成图纸目录，依据待文印对象
     * @param didStr
     * @param pltCategory
     * @param request
     * @return
     */
    @RequestMapping(value = "generatePltOrder", method = RequestMethod.GET)
    public Long generatePltOrder(@RequestParam(value = "didStr", required = true, defaultValue = "") String didStr,
                                 @RequestParam(value = "pltCategory", required = true, defaultValue = "") String pltCategory,
                                 @RequestParam(value = "userName",required = true, defaultValue = "") String userName,
                                 HttpServletRequest request) {
        return drawPrintService.generatePrintOrder(didStr, pltCategory,userName);
    }

    /**
     * 生成图纸目录
     * @param dids
     * @param isallem
     * @param request
     * @return
     */
    @RequestMapping(value = "generateCatContentsMain", method = RequestMethod.POST)
    public Long generateCatContentsMain(@RequestParam(value = "dids", required = false, defaultValue = "") String dids,
                                        @RequestParam(value = "isallem", required = false, defaultValue = "") String isallem,
                                        HttpServletRequest request) {
        return drawPrintService.generateCatContentsMain(dids, isallem, SessionUtil.getLdapUserInfo(request).getEmployeeNo());
    }

    /**
     * 按generateCatContentsMain给的id查询图纸目录头
     * @param id
     * @return
     */
    @RequestMapping(value = "searchCatHeaders", method = RequestMethod.GET)
    public CatHeaderDTO searchCatHeaders(@RequestParam(value = "id", required = false, defaultValue = "") Long id) {
        return drawPrintService.searchCatHeaders(id);
    }

    /**
     * 按generateCatContentsMain给的id查询图纸目录明细列表
     * @param id
     * @return
     */
    @RequestMapping(value = "searchCatLines", method = RequestMethod.GET)
    public List<CatLineDTO> searchCatLines(@RequestParam(value = "id", required = false, defaultValue = "") Long id) {
        return drawPrintService.searchCatLines(id);
    }

    /**
     * 预览目录，获得 xmlid,tempid，用于
     * -- argsvalue=3940 生成图纸目录的cat_header_id
     * http://192.168.15.252:16200/rep/?xmlid=984566&tempid=839204&argsname=P_HEADER_ID&argsvalue=3940
     * @param templateCode
     * @return
     */
    @RequestMapping(value = "getSourceFileId", method = RequestMethod.GET)
    public Map getSourceFileId(@RequestParam(value = "templateCode", required = false, defaultValue = "") String templateCode,
                               @RequestParam(value = "proId", required = false, defaultValue = "") String proId) {
        return drawPrintService.getSourceFileId(templateCode, proId);
    }

    private String sendEcm(long id, String xmlid, String tempid, String docType, String userNo) throws IdcClientException {
        CatHeaderDTO dto = drawPrintService.searchCatHeaders(id);
        List<CatLineDTO> lines = drawPrintService.searchCatLines(id);
        String lineString = "";
        for(CatLineDTO c : lines) {
            lineString += c.getDid() + ",";
        }

        DataBinder mainBinder = RidcUtil.getDataBinder();
        mainBinder.putLocal("IdcService","CUX_SubmmitCatalogue_S");
        mainBinder.putLocal("source", "nims2.0");
        mainBinder.putLocal("tempid", tempid);
        mainBinder.putLocal("rtfid", tempid);
        mainBinder.putLocal("xmlid", xmlid);
        mainBinder.putLocal("dInDate", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
        mainBinder.putLocal("dDocAuthor", userNo);
        mainBinder.putLocal("dDocName", dto.getDrawingNum());
        mainBinder.putLocal("dDocTitle", "图纸目录");
        mainBinder.putLocal("dDocType", docType);
        mainBinder.putLocal("dSecurityGroup","PROJECT");
        mainBinder.putLocal("xDETAIL_TYPE","图纸目录");
        mainBinder.putLocal("xPJT_REVISE_NUM", dto.getVersionNum());
        mainBinder.putLocal("PAGE_COUNT","1");
        mainBinder.putLocal("Is_DLVR","");
        mainBinder.putLocal("Is_BD_DL","");
        mainBinder.putLocal("Is_Devision","");
        mainBinder.putLocal("Is_Devision_SEQ","");
        mainBinder.putLocal("Is_CAD","");
        mainBinder.putLocal("IsCreate","1");
        mainBinder.putLocal("IS_CREATE","1");
        mainBinder.putLocal("CAT_HEADER_ID", id + "");
        mainBinder.putLocal("DLVR_ID","");
        mainBinder.putLocal("DLVR_VERSION","");
        mainBinder.putLocal("isAllEM","0");
        mainBinder.putLocal("idString", lineString);
        mainBinder.putLocal("cpdBasketID","");
        mainBinder.putLocal("role_type","1");
        mainBinder.putLocal("xPROJECT_PNUM", dto.getParentPrjNum());
        mainBinder.putLocal("xPROJECT_PNAME", dto.getParentPrjName());
        mainBinder.putLocal("xCLIENT", dto.getCustomerName());
        mainBinder.putLocal("PROJECT_NUM", dto.getProjectNum());
        mainBinder.putLocal("UNIT_TASK_CODE", dto.getUnitTaskCode());
        mainBinder.putLocal("xDIVISION", dto.getUnitTaskName());
        mainBinder.putLocal("xREF_DWG_NO", dto.getDrawingLnum());
        mainBinder.putLocal("SPECIALITY_CLASS_NUMBER", dto.getSpecialityClassNum());
        mainBinder.putLocal("UNIT_SEQ", dto.getUnitSeq() + "");
        mainBinder.putLocal("PHASE_CODE", dto.getPhaseCode());
        mainBinder.putLocal("CAT_STATUS_FLAG", dto.getCatStatusFlag());
//        mainBinder.putLocal("fParentGUID", dto.getFguId() + "");
        mainBinder.putLocal("dDocAccount","ZY-2" + dto.getSpecialityClassNum());
        mainBinder.putLocal("PROJECT_DNAME",dto.getProjectDname());
        mainBinder.putLocal("xPROJECT_LNAME", dto.getProjectLname());
        mainBinder.putLocal("xPROJECT_SNAME", dto.getProjectSname());
        mainBinder.putLocal("xDLVR_TYPE", dto.getDlvrType());
        mainBinder.putLocal("xSIZE","A4");
        mainBinder.putLocal("xDESIGN_PHASE", dto.getPhaseCode());
        mainBinder.putLocal("xSPECIALTY", dto.getSpecialityClassNum());
        mainBinder.putLocal("preview_flag","");
        mainBinder.putLocal("TEMP","");
        mainBinder.putLocal("xPROJECT_MANAGER_NAME", dto.getManagerName());
        mainBinder.putLocal("xREF_DWG_NO", dto.getDrawingLnum());
        mainBinder.putLocal("XEQUIPMENT", dto.getEquipmentC());
        mainBinder.putLocal("XEX_DWG_TITLE", "DWG LIST");
        // 执行服务
        DataBinder serverBinder = RidcUtil.executeService(mainBinder);
        return serverBinder.getLocal("dID");
    }

    /**
     * 提交目录
     * @param id
     * @param templateCode
     * @return
     */
    @RequestMapping(value = "submitCatHeaders", method = RequestMethod.POST)
    public Map submitCatHeaders(@RequestParam(value = "id", required = false, defaultValue = "") Long id,
                                @RequestParam(value = "templateCode", required = false, defaultValue = "") String templateCode,
                                @RequestParam(value = "tw", required = false, defaultValue = "") String tw,
                                @RequestParam(value = "xmlid", required = false, defaultValue = "") String xmlid,
                                @RequestParam(value = "tempid", required = false, defaultValue = "") String tempid,
                                @RequestParam(value = "docType", required = false, defaultValue = "") String docType,
                                HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        String did = "";
        try {
            did = this.sendEcm(id, xmlid, tempid, docType, SessionUtil.getLdapUserInfo(request).getEmployeeNo());
        } catch(IdcClientException e) {
            e.printStackTrace();
            restFulData.put(RestFulData.RETURN_CODE, "0001");
            restFulData.put(RestFulData.RETURN_MSG, e.getMessage());
            return restFulData;
        } catch (Exception e) {
            e.printStackTrace();
            restFulData.put(RestFulData.RETURN_CODE, "0001");
            restFulData.put(RestFulData.RETURN_MSG, e.getMessage());
            return restFulData;
        }
        Map reMap = null;
        reMap = drawPrintService.submitCatHeaders(id, Long.parseLong(did), templateCode, tw);
        if (!"S".equals(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     *  查询待出图文印列表
     * @param projectId
     * @param phaseCode
     * @param unitTaskCode
     * @param specialityCode
     * @param dlvrId
     * @param drawStatusCode
     * @param request
     * @return
     */
    @RequestMapping(value = "searchTmpPlts", method = RequestMethod.GET)
    public List<TmpPltDTO> searchTmpPlts(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                         @RequestParam(value = "phaseCode", required = false, defaultValue = "") String phaseCode,
                                         @RequestParam(value = "unitTaskCode", required = false, defaultValue = "") String unitTaskCode,
                                         @RequestParam(value = "specialityCode", required = false, defaultValue = "") String specialityCode,
                                         @RequestParam(value = "dlvrId", required = false, defaultValue = "") Long dlvrId,
                                         @RequestParam(value = "drawStatusCode", required = false, defaultValue = "") String drawStatusCode,
                                         @RequestParam(value = "tw", required = false, defaultValue = "") String tw,
                                         HttpServletRequest request) {
        return drawPrintService.searchTmpPlts(SessionUtil.getLdapUserInfo(request).getEmployeeNo(), projectId, phaseCode, unitTaskCode, specialityCode, dlvrId, drawStatusCode, tw);
    }

    /**
     * 删除记录
     * @param dids
     * @return
     */
    @RequestMapping(value = "deleteTmpPlts", method = RequestMethod.POST)
    public String deleteTmpPlts(@RequestParam(value = "dids", required = false, defaultValue = "") String dids) {
        drawPrintService.deleteTmpPlts(dids);
        return "true";
    }

    /**
     * 生成出图单，获得id
     * @param dids
     * @param pltCategory
     * @param request
     * @return
     */
    @RequestMapping(value = "generatePltOrderMain", method = RequestMethod.POST)
    public Long generatePltOrderMain(@RequestParam(value = "dids", required = false, defaultValue = "") String dids,
                                        @RequestParam(value = "pltCategory", required = false, defaultValue = "PLT_NEW_PRINT") String pltCategory,
                                        HttpServletRequest request) {
        return drawPrintService.generatePltOrderMain(dids, pltCategory, SessionUtil.getLdapUserInfo(request).getEmployeeNo());
    }

    /**
     * 文印选项头
     * @param id
     * @return
     */
    @RequestMapping(value = "searchDrawPrintInfo", method = RequestMethod.GET)
    public CatConfigHeaderDTO searchDrawPrintInfo(@RequestParam(value = "id", required = false, defaultValue = "") Long id) {
        return drawPrintService.searchDrawPrintInfo(id);
    }

    /**
     * 文印选项明细
     * @param id
     * @return
     */
    @RequestMapping(value = "searchDrawprintLines", method = RequestMethod.GET)
    public List<CatLineDTO> searchDrawprintLines(@RequestParam(value = "id", required = false, defaultValue = "") Long id) {
        return drawPrintService.searchDrawprintLines(id);
    }

    /**
     * 文印途径
     * @return
     */
    @RequestMapping(value = "getPrintWayList", method = RequestMethod.GET)
    public List<String> getPrintWayList() {
        return drawPrintService.getPrintWayList();
    }

    /**
     * 项目负责人
     * @param proId
     * @return
     */
    @RequestMapping(value = "getProjectManagerList", method = RequestMethod.GET)
    public List<String> getProjectManagerList(@RequestParam(value = "proId", required = false, defaultValue = "") Long proId) {
        return drawPrintService.getProjectManagerList(proId);
    }

    /**
     * 提交保存文印选项
     * @param id
     * @param pltContent
     * @param projectResBy
     * @param printChannel
     * @param sendOwner
     * @param sgService
     * @param archiveFlag
     * @param paperType
     * @param operationType 操作类型 SAVE保存/SUBMIT提交
     * @return
     */
    @RequestMapping(value = "submitDrawprints", method = RequestMethod.POST)
    public Map submitDrawprints(@RequestParam(value = "id", required = false, defaultValue = "") Long id,
                                @RequestParam(value = "pltContent", required = false, defaultValue = "") String pltContent,
                                @RequestParam(value = "projectResBy", required = false, defaultValue = "") String projectResBy,
                                @RequestParam(value = "printChannel", required = false, defaultValue = "") String printChannel,
                                @RequestParam(value = "sendOwner", required = false, defaultValue = "") int sendOwner,
                                @RequestParam(value = "sgService", required = false, defaultValue = "") int sgService,
                                @RequestParam(value = "archiveFlag", required = false, defaultValue = "") String archiveFlag,
                                @RequestParam(value = "paperType", required = false, defaultValue = "") String paperType,
                                @RequestParam(value = "operationType", required = false, defaultValue = "") String operationType,
                                HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = drawPrintService.submitDrawprints(id, pltContent, projectResBy, printChannel, sendOwner, sgService, archiveFlag, paperType, operationType, SessionUtil.getLdapUserInfo(request).getEmployeeNo());
        if (!"S".equals(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    @RequestMapping(value = "submitTextprints", method = RequestMethod.POST)
    public Map submitTextprints(@RequestParam(value = "id", required = false, defaultValue = "") Long id,
                                @RequestParam(value = "pltContent", required = false, defaultValue = "") String pltContent,
                                @RequestParam(value = "projectResBy", required = false, defaultValue = "") String projectResBy,
                                @RequestParam(value = "printChannel", required = false, defaultValue = "") String printChannel,
                                @RequestParam(value = "sendOwner", required = false, defaultValue = "") int sendOwner,
                                @RequestParam(value = "sgService", required = false, defaultValue = "") int sgService,
                                @RequestParam(value = "archiveFlag", required = false, defaultValue = "") String archiveFlag,
                                @RequestParam(value = "print_size", required = false, defaultValue = "") String print_size,
                                @RequestParam(value = "his_typeset", required = false, defaultValue = "") String his_typeset,
                                @RequestParam(value = "bindiing_type", required = false, defaultValue = "") String bindiing_type,
                                @RequestParam(value = "is_inparts", required = false, defaultValue = "") String is_inparts,
                                @RequestParam(value = "plt_comment", required = false, defaultValue = "") String plt_comment,
                                @RequestParam(value = "operationType", required = false, defaultValue = "") String operationType,
                                HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = drawPrintService.submitTextprints(id, pltContent, projectResBy, printChannel, sendOwner, sgService, archiveFlag, print_size, his_typeset,
                bindiing_type, is_inparts, plt_comment, operationType, SessionUtil.getLdapUserInfo(request).getEmployeeNo());
        if (!"S".equals(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 查询文印出图单
     * @param projectNum
     * @param xequipmentName
     * @param xdivisionNum
     * @param xdivisionName
     * @param specialty
     * @param pltNum
     * @param pltCategory
     * @param designPhase
     * @param pltContent
     * @param archiveFlag
     * @param pltStatus
     * @param startDate
     * @param endDate
     * @param pltRequestorName
     * @param pltDepartment
     * @param request
     * @return
     */
    @RequestMapping(value = "searchPltOrders", method = RequestMethod.GET)
    public List<MyDrawsDTO> searchPltOrders(@RequestParam(value = "projectNum", required = false, defaultValue = "") String projectNum,
                                         @RequestParam(value = "xequipmentName", required = false, defaultValue = "") String xequipmentName,
                                         @RequestParam(value = "xdivisionNum", required = false, defaultValue = "") String xdivisionNum,
                                         @RequestParam(value = "xdivisionName", required = false, defaultValue = "") String xdivisionName,
                                         @RequestParam(value = "specialty", required = false, defaultValue = "") String specialty,
                                         @RequestParam(value = "pltNum", required = false, defaultValue = "") String pltNum,
                                         @RequestParam(value = "pltCategory", required = false, defaultValue = "") String pltCategory,
                                         @RequestParam(value = "designPhase", required = false, defaultValue = "") String designPhase,
                                         @RequestParam(value = "pltContent", required = false, defaultValue = "") String pltContent,
                                         @RequestParam(value = "archiveFlag", required = false, defaultValue = "") String archiveFlag,
                                         @RequestParam(value = "pltStatus", required = false, defaultValue = "") String pltStatus,
                                         @RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
                                         @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
                                         @RequestParam(value = "pltRequestorName", required = false, defaultValue = "") String pltRequestorName,
                                         @RequestParam(value = "pltDepartment", required = false, defaultValue = "") String pltDepartment,
                                         HttpServletRequest request) {
        return drawPrintService.searchPltOrders(projectNum, xequipmentName, xdivisionNum, xdivisionName, specialty, pltNum, pltCategory,
                designPhase, pltContent, archiveFlag, pltStatus, startDate, endDate, pltRequestorName, pltDepartment, SessionUtil.getLdapUserInfo(request).getEmployeeNo());
    }


    /**
     * 添嗮、加印
     * @param request
     * @return
     */
    @RequestMapping(value = "generatePltOrderAppend", method = RequestMethod.POST)
    public Long generatePltOrderAppend(@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
                                     HttpServletRequest request) {
        return drawPrintService.generatePltOrderAppend(ids, SessionUtil.getLdapUserInfo(request).getEmployeeNo());
    }

    /**
     * 我的出图单删除
     * @param id
     * @return
     */
    @RequestMapping(value = "deletePltOrders", method = RequestMethod.POST)
    public Map deletePltOrders(@RequestParam(value = "id", required = false, defaultValue = "") Long id) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = drawPrintService.deletePltOrders(id);
        if (!"S".equals(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 文印状态
     * @return
     */
    @RequestMapping(value = "getPrintStatusList", method = RequestMethod.GET)
    public List<String> getPrintStatusList() {
        return drawPrintService.getPrintStatusList();
    }

    /**
     * 文印类型
     * @return
     */
    @RequestMapping(value = "getDrawingPrintTypeList", method = RequestMethod.GET)
    public List<String> getDrawingPrintTypeList() {
        return drawPrintService.getDrawingPrintTypeList();
    }

    /**
     * 添加文本文印至待出图文列表
     * @param dids
     * @param request
     * @return
     */
    @RequestMapping(value = "genPjtDesignDocPltTmp", method = RequestMethod.POST)
    public Map genPjtDesignDocPltTmp(@RequestParam(value = "dids", required = false, defaultValue = "") String dids,
                                HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = drawPrintService.genPjtDesignDocPltTmp(dids, SessionUtil.getLdapUserInfo(request).getEmployeeNo());
        if (!"S".equals(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    @RequestMapping(value = "addPltRint", method = RequestMethod.POST)
    public Map addPltRint(@RequestParam(value = "plt_print_set_id", required = false, defaultValue = "") Long plt_print_set_id,
            @RequestParam(value = "plt_order_header_id", required = false, defaultValue = "") Long plt_order_header_id,
            @RequestParam(value = "charge_item", required = false, defaultValue = "") String charge_item,
            @RequestParam(value = "print_set_size", required = false, defaultValue = "") String print_set_size,
            @RequestParam(value = "amount", required = false, defaultValue = "0") int amount,
            @RequestParam(value = "unit", required = false, defaultValue = "0") String unit,
            @RequestParam(value = "unit_price", required = false, defaultValue = "0") double unit_price,
            @RequestParam(value = "total_price", required = false, defaultValue = "0") double total_price) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = drawPrintService.addPltRint(plt_print_set_id, plt_order_header_id, charge_item, print_set_size, amount, unit, unit_price, total_price);
        if (!"0".equals(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    @RequestMapping(value = "getPltRint", method = RequestMethod.GET)
    public List<PltPrintDTO> getPltRint(@RequestParam(value = "plt_order_header_id", required = false, defaultValue = "") Long plt_order_header_id) {
        return drawPrintService.getPltRint(plt_order_header_id);
    }

    @RequestMapping(value = "getSfList", method = RequestMethod.GET)
    public List<PltPrintDTO> getSfList() {
        return drawPrintService.getSfList();
    }

    @RequestMapping(value = "delPltRint", method = RequestMethod.POST)
    public Map delPltRint(@RequestParam(value = "plt_print_set_id", required = false, defaultValue = "") Long plt_print_set_id) {
        return drawPrintService.delPltRint(plt_print_set_id);
    }

    @RequestMapping(value = "uploadFile", method = RequestMethod.POST)
    public PltAttDTO uploadZzFile(@RequestParam(value = "templateFile", required = false) MultipartFile[] files,
                            @RequestParam(value = "plt_order_header_id", required = false, defaultValue = "") Long plt_order_header_id,
                                            HttpServletRequest request) throws UnsupportedEncodingException {
        PltAttDTO dto = new PltAttDTO();
        String dir = nerinProperties.getNbcc().getCadiDocFileUrl() + plt_order_header_id + "/";
        File dirPath = new File(dir);
        if(!dirPath.exists())
            dirPath.mkdirs();
        File filePath = null;
        String fileName = "";
        for (MultipartFile m : files) {
            fileName = m.getOriginalFilename();
            if (0 <= fileName.lastIndexOf("\\")) {
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
            }
            filePath = new File(new String((dir + fileName).getBytes("UTF-8")));
            if (filePath.exists())
                filePath.delete();
            try {
                m.transferTo(new File(dir, new String(fileName.getBytes("UTF-8"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            dto.setTitle(fileName);
            dto.setComments(fileName);
            dto.setDoc_info(dir + fileName);
            dto.setCreation_date(new Date());
            dto.setAttribute1("项目-设计文本");
            dto.setAuthor(SessionUtil.getLdapUserInfo(request).getFullName());
        }
        return dto;
    }

    @RequestMapping(value = "addPltAtt", method = RequestMethod.POST)
    public Map addPltAtt(@RequestParam(value = "plt_order_header_id", required = false, defaultValue = "") Long plt_order_header_id,
                          @RequestParam(value = "p_file_id", required = false, defaultValue = "") Long p_file_id,
                          @RequestParam(value = "p_title", required = false, defaultValue = "") String p_title,
                          @RequestParam(value = "p_comments", required = false, defaultValue = "") String p_comments,
                          @RequestParam(value = "p_doc_info", required = false, defaultValue = "") String p_doc_info,
                          @RequestParam(value = "creation_date", required = false, defaultValue = "") Timestamp creation_date,
                         HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = drawPrintService.addPltAtt(plt_order_header_id, p_file_id, p_title, p_comments, SessionUtil.getLdapUserInfo(request).getFullName(), p_doc_info, creation_date);
        if (!"0".equals(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    @RequestMapping(value = "getPltAtt", method = RequestMethod.GET)
    public List<PltAttDTO> getPltAtt(@RequestParam(value = "plt_order_header_id", required = false, defaultValue = "") Long plt_order_header_id) {
        return drawPrintService.getPltAtt(plt_order_header_id);
    }

    @RequestMapping(value = "delPltAtt", method = RequestMethod.POST)
    public Map delPltRint(@RequestParam(value = "p_plt_order_header_id", required = false, defaultValue = "") Long p_plt_order_header_id,
                          @RequestParam(value = "p_file_id", required = false, defaultValue = "") Long p_file_id) {
        return drawPrintService.delPltAtt(p_plt_order_header_id, p_file_id);
    }

    /**
     * 附件下载
     * @param
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "downloadFile", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadWordFile(@RequestParam(value = "path", required = false, defaultValue = "") String path) throws IOException {
        File file=new File(path);
        HttpHeaders headers = new HttpHeaders();
        String downFileName= path.substring(path.lastIndexOf("/") + 1);
        headers.setContentDispositionFormData("attachment", java.net.URLEncoder.encode(downFileName, "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }
    /**
     * 获取设计变更图纸列表   by lwl
     * @param dlvrIds
     * @param ddocname  图号或者图名
     * @param page 第几页
     * @param rows 每页几行
     * @return
     */
    @RequestMapping(value = "DesignChange", method = RequestMethod.GET)
    public DataTables2DTO queryDesignChange(
            @RequestParam(value = "DlvrIds", required = false, defaultValue = "") String dlvrIds,
            @RequestParam(value = "DdocName", required = false, defaultValue = "") String ddocname,
            @RequestParam(value = "page", required = false, defaultValue = "") Long page,
            @RequestParam(value = "rows", required = false, defaultValue = "") Long rows

    ) {
        return drawPrintService.getDesignChangeList( dlvrIds, ddocname, page, rows);
    }

    /**
     * 获取出图文印图纸列表，通过前端的工作包ID串取值，不区分图纸状态，不显示文本，不显示图纸目录  by lwl
     * @param dlvrIds
     * @param ddocname  图号或者图名
     * @param page 第几页
     * @param rows 每页几行
     * @return
     */
    @RequestMapping(value = "DrawList", method = RequestMethod.POST)
    public DataTables2DTO queryDrawList(
            @RequestParam(value = "DlvrIds", required = false, defaultValue = "") String dlvrIds,
            @RequestParam(value = "DdocName", required = false, defaultValue = "") String ddocname,
            @RequestParam(value = "page", required = false, defaultValue = "") Long page,
            @RequestParam(value = "rows", required = false, defaultValue = "") Long rows,
            @RequestParam(value = "isPM", required = false, defaultValue = "") Long ispm,
            HttpServletRequest request
    ) {
        return drawPrintService.getDesignChangeList2(SessionUtil.getLdapUserInfo(request).getEmployeeNo(), dlvrIds, ddocname, page, rows, ispm);
    }

    /**
     * 生成图纸目录  by lwl
     * 增加是否项目经理入参，项目经理可以出施工图，图纸目录号规则 2A17A09DDG0405PM1DL1-1
     * @param dids
     * @param isallem
     * @param request
     * @param ispm
     * @return
     */
    @RequestMapping(value = "generateCatContentsMain1", method = RequestMethod.POST)
    public Long generateCatContentsMain1(@RequestParam(value = "dids", required = false, defaultValue = "") String dids,
                                        @RequestParam(value = "isallem", required = false, defaultValue = "") String isallem,
                                         @RequestParam(value = "isPM", required = false, defaultValue = "") Long ispm,
                                         HttpServletRequest request) {
        return drawPrintService.generateCatContentsMain1(dids, isallem, ispm, SessionUtil.getLdapUserInfo(request).getEmployeeNo());
    }

    /**
     * 通过所属图号获取图纸目录自然张数
     * @param DrawSizeDTOs
     * //@param userId
     * @return
     */
    @RequestMapping(value = "DrawSize", method = RequestMethod.POST)
    public List<DrawSizeReturnDTO> DrawSize(@RequestBody List<DrawSizeDTO> DrawSizeDTOs,
                               HttpServletRequest request) {
        return drawPrintService.DrawSize(DrawSizeDTOs);
    }

    /**
     * 出图量统计报表   by lwl
     * @param ProjectNum  项目编号
     * @param SpecClassNum  专业代字
     * @param request
     * @param DesignedID  设计人工号
     * @param FromDate  开始时间
     * @param ToDate  截止时间
     * @param Code  1本部门员工在每个项目的出图量情况；2本部门不同专业在某项目的出图量；3本部门全年的不同项目的累计出图量；
     * @return
     */
    @RequestMapping(value = "chutuliang1", method = RequestMethod.GET)
    public List<CHUTULIANG1DTO> chutuliang1(
            @RequestParam(value = "ProjectNum", required = false, defaultValue = "") String ProjectNum,
            @RequestParam(value = "ProjectNum", required = false, defaultValue = "") String SpecClassNum,
            @RequestParam(value = "Designed", required = false, defaultValue = "") String DesignedID,
            @RequestParam(value = "Code", required = false, defaultValue = "") Long Code,
            @RequestParam(value = "FromDate", required = false, defaultValue = "") String FromDate,
            @RequestParam(value = "ToDate", required = false, defaultValue = "") String ToDate,
            HttpServletRequest request

    ) {
        return drawPrintService.getChutuliangList1( ProjectNum, SpecClassNum, SessionUtil.getLdapUserInfo(request).getEmployeeNo(), DesignedID, FromDate, ToDate, Code);
    }

    /**
     * 出图量统计报表   by lwl  专业出图量
     * @param ProjectNum  项目编号
     * @param SpecClassNum  专业代字
     * @param request
     * @param Code  1本部门员工在每个项目的出图量情况；2本部门不同专业在某项目的出图量；3本部门全年的不同项目的累计出图量；
     * @return
     */
    @RequestMapping(value = "chutuliang2", method = RequestMethod.GET)
    public List<CHUTULIANG2DTO> chutuliang2(
            @RequestParam(value = "ProjectNum", required = false, defaultValue = "") String ProjectNum,
            @RequestParam(value = "SpecClassNum", required = false, defaultValue = "") String SpecClassNum,
            @RequestParam(value = "Code", required = false, defaultValue = "") Long Code,
            HttpServletRequest request

    ) {
        return drawPrintService.getChutuliangList2( ProjectNum, SpecClassNum, SessionUtil.getLdapUserInfo(request).getEmployeeNo(), Code);
    }

    /**
     * 出图量统计报表   by lwl  部门出图量
     * @param ProjectNum  项目编号
     * @param request
     * @param Code  1本部门员工在每个项目的出图量情况；2本部门不同专业在某项目的出图量；3本部门全年的不同项目的累计出图量；
     * @return
     */
    @RequestMapping(value = "chutuliang3", method = RequestMethod.GET)
    public List<CHUTULIANG3DTO> chutuliang3(
            @RequestParam(value = "ProjectNum", required = false, defaultValue = "") String ProjectNum,
            @RequestParam(value = "Organization", required = false, defaultValue = "") String Organization,
            @RequestParam(value = "Code", required = false, defaultValue = "") Long Code,
            HttpServletRequest request

    ) {
        return drawPrintService.getChutuliangList3( ProjectNum, SessionUtil.getLdapUserInfo(request).getEmployeeNo(), Code);
    }

    /**
     * 获取所有项目列表
     *
     * @param projectId
     * @return
     */
    @RequestMapping(value = "DISProject", method = RequestMethod.GET)
    public List<DisprojectListDTO> getDISProjectList(
            @RequestParam(value = "projectId", required = true, defaultValue = "") String projectId
    ) {
        return drawPrintService.getDISProjectList(projectId);
    }

    /**
     * 获取人员列表
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "DISName", method = RequestMethod.GET)
    public List<DisNameListDTO> getDISNameList(
            HttpServletRequest request
    ) {
        return drawPrintService.getDISNameList(SessionUtil.getLdapUserInfo(request).getEmployeeNo());
    }

    /**
     * 图纸编号串反查ECM图纸列表 。 OA流程REQUESTID
     先到中转表获取相关图号列表再查询ECM数据  by lwl
     * @param requestids  OA表单号
     * @param page 第几页
     * @param rows 每页几行
     * @return
     */
    @RequestMapping(value = "OADrawList", method = RequestMethod.POST)
    public DataTables2DTO queryDrawList(
            @RequestParam(value = "requestids", required = false, defaultValue = "") String requestids,
            @RequestParam(value = "page", required = false, defaultValue = "") Long page,
            @RequestParam(value = "rows", required = false, defaultValue = "") Long rows,
            HttpServletRequest request
    ) {
        return drawPrintService.getDesignChangeList4(requestids, page, rows);
    }

    /**
     * 插入新开档ECM表，Insert_Cux_Ebs_Queues
     * @param dlvrs
     * @return
     */
    @RequestMapping(value = "InsertCuxQueuesDlvr", method = RequestMethod.POST)
    public String InsertCuxQueuesDlvr(@RequestParam(value = "dlvrs", required = false, defaultValue = "") String dlvrs) {
        drawPrintService.InsertCuxQueuesDlvr(dlvrs);
        return "true";
    }


}
