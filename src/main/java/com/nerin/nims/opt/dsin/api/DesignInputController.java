package com.nerin.nims.opt.dsin.api;


import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.dsin.dto.*;
import com.nerin.nims.opt.dsin.service.DesignInputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/26.
 */

@RestController
@RequestMapping("/api/dsin")
public class DesignInputController {
    @Autowired
    private DesignInputService designInputService;

    /**
     * 设计输入权限
     */
    @RequestMapping(value = "diAuthority", method = RequestMethod.GET)
    public Map sjsrAuthority(@RequestParam(value = "prjid", required = false, defaultValue = "") Long projectId,
                             @RequestParam(value = "personID", required = false, defaultValue = "") Long personId) {
        return designInputService.diAuthority(projectId, personId);
    }

    /**
     * 项目输入表
     */
    @RequestMapping(value = "projGrid", method = RequestMethod.GET)
    public List<ProjDocListDTO> projGrid(@RequestParam(value = "prjid", required = false, defaultValue = "") Long projectId,
                                         @RequestParam(value = "prjPhaseID", required = false, defaultValue = "") Long phaseCode) {
        return designInputService.projInputDoc(projectId, phaseCode);
    }

    /**
     * 专业输入树
     */
    @RequestMapping(value = "specTree", method = RequestMethod.GET,produces="text/html;charset=UTF-8")
    public String specTree(@RequestParam(value = "prjid", required = false, defaultValue = "") Long projectId,
                           @RequestParam(value = "personID", required = false, defaultValue = "") Long personId,
                           @RequestParam(value = "role", required = false, defaultValue = "") String role,
                           @RequestParam(value = "prjPhaseID", required = false, defaultValue = "") Long phaseCode) {
        return designInputService.specInputType(role, projectId, phaseCode, personId);
    }

    /**
     * 专业输入表
     */
    @RequestMapping(value = "specGrid", method = RequestMethod.GET)
    public List<SpecTypeDocListDTO> specGrid(@RequestParam(value = "prjid", required = false, defaultValue = "") Long projectId,
                                             @RequestParam(value = "personID", required = false, defaultValue = "") Long personId,
                                             @RequestParam(value = "role", required = false, defaultValue = "") String role,
                                             @RequestParam(value = "prjPhaseID", required = false, defaultValue = "") Long phaseCode) {
        return designInputService.specInputDoc(role, projectId, phaseCode, personId);
    }

    /**
     * 设备输入
     */
    @RequestMapping(value = "equiData", method = RequestMethod.GET)
    public Map equiGrid(@RequestParam(value = "prjid", required = false, defaultValue = "") Long projectId,
                        @RequestParam(value = "personID", required = false, defaultValue = "") Long personId,
                        @RequestParam(value = "role", required = false, defaultValue = "") String role,
                        @RequestParam(value = "prjPhaseID", required = false, defaultValue = "") Long phaseCode) {
        return designInputService.equiInput(role, projectId, phaseCode, personId);
    }

    /**
     * 代管输入
     */
    @RequestMapping(value = "tempGrid", method = RequestMethod.GET)
    public List<TmpDocListDTO> tempGrid(@RequestParam(value = "prjid", required = false, defaultValue = "") Long projectId) {
        return designInputService.tempInputDoc(projectId);
    }

    /**
     * 视图
     */
    @RequestMapping(value = "getErpId", method = RequestMethod.GET)
    public long getErpId(@RequestParam(value = "prjid", required = false, defaultValue = "") Long projectId,
                         @RequestParam(value = "personID", required = false, defaultValue = "") Long personId,
                         @RequestParam(value = "docID", required = false, defaultValue = "0") Long docId,
                         @RequestParam(value = "phaseName", required = false, defaultValue = "") String phaseName,
                         @RequestParam(value = "specName", required = false, defaultValue = "") String specName) {
        return designInputService.getErpId(projectId, personId, docId, phaseName, specName);
    }

    /**
     * 获取专业
     */
    @RequestMapping(value = "getSpecList", method = RequestMethod.GET)
    public List<SpecListDTO>  getSpecList(@RequestParam(value = "prjid", required = false, defaultValue = "") Long projectId,
                                          @RequestParam(value = "personID", required = false, defaultValue = "") Long personId,
                                          @RequestParam(value = "role", required = false, defaultValue = "") String role,
                                          @RequestParam(value = "prjPhaseID", required = false, defaultValue = "") Long phaseCode) {
        return designInputService.getSpecList(role, projectId, phaseCode, personId);
    }
    /**
     * 创建评审视图
     */
    @RequestMapping(value = "approve", method = RequestMethod.POST)
    public Map createApproveView(@RequestBody ApproveInfoDTO approveInfoDTO) {
        return designInputService.createApproveView(approveInfoDTO);
    }
    /**
     * 编辑修改状态
     */
    @RequestMapping(value = "changeStauts", method = RequestMethod.POST)
    public Map changeStauts(@RequestParam(value = "tabindex", required = false, defaultValue = "") Long tabIndex,
                            @RequestParam(value = "id", required = false, defaultValue = "") Long id) {
        return designInputService.changeStauts(tabIndex,id);
    }
    /**
     * 删除数据
     */
    @RequestMapping(value = "deleteDoc", method = RequestMethod.POST)
    public Map deleteDoc(@RequestBody ApproveInfoDTO approveInfoDTO) {
        return designInputService.deleteDoc(approveInfoDTO);
    }
    /**
     * 获取专业
     */
    @RequestMapping(value = "getApproveRequest", method = RequestMethod.GET)
    public List<ApproveRequestDTO>  getApproveRequest(@RequestParam(value = "tabIndex", required = false, defaultValue = "") Long tabIndex,
                                          @RequestParam(value = "id", required = false, defaultValue = "") Long id) {
        return designInputService.getApproveRequest(tabIndex,id);
    }
}
