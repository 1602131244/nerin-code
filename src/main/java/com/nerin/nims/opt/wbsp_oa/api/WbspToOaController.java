package com.nerin.nims.opt.wbsp_oa.api;


import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.video.dto.*;
import com.nerin.nims.opt.video.service.VideoService;
import com.nerin.nims.opt.wbsp_oa.dto.*;
import com.nerin.nims.opt.wbsp_oa.service.WbspToOaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wbspToOa")
public class WbspToOaController {
    @Autowired
    private WbspToOaService wbspToOaService;

    /**
     * 查询工作包相关信息
     */
    @RequestMapping(value = "getDlvr", method = RequestMethod.GET)
    public DlvrDTO getDlvr(@RequestParam(value = "dlvrId", required = false, defaultValue = "") Long dlvrId) {
        return wbspToOaService.getDlvr(dlvrId);
    }

    /**
     * 查询OA校审流程表单数据
     */
    @RequestMapping(value = "getApproveForm", method = RequestMethod.GET)
    public ApproveFromDTO getApproveForm(@RequestParam(value = "requestId", required = false, defaultValue = "") Long requestId) {
        return wbspToOaService.getApproveForm(requestId);
    }

    /**
     * 更新校审流程图号中转表/会签流程图号
     */
    @RequestMapping(value = "saveDrawNum", method = RequestMethod.POST)
    public Map saveComment(@RequestBody OaDrawNumDTO oaDrawNumDTO) {
        return wbspToOaService.saveDrawNum(oaDrawNumDTO);
    }

    /**
     * 发起本部设计成果校审流程
     */
    @RequestMapping(value = "saveHead", method = RequestMethod.POST)
    public Map saveHead(@RequestBody HeadAchievementOaDTO headAchievementOaDTO,
                        HttpServletRequest request) {
        return wbspToOaService.saveHead(headAchievementOaDTO, SessionUtil.getLdapUserInfo(request).getEmployeeNo(), "A");
    }

    /**
     * 发起本部图纸会签流程
     */
    @RequestMapping(value = "saveSign", method = RequestMethod.POST)
    public Map saveSign(@RequestBody HeadAchievementOaDTO headAchievementOaDTO,
                        HttpServletRequest request) {
        return wbspToOaService.saveHead(headAchievementOaDTO, SessionUtil.getLdapUserInfo(request).getEmployeeNo(), "B");
    }

    /**
     * 更新审批流程
     */
    @RequestMapping(value = "updateApprove", method = RequestMethod.POST)
    public Map updateApprove(@RequestBody ApproveDTO approveDTO) {
        return wbspToOaService.updateApprove(approveDTO);
    }

    /**
     * 工作包校核流程 OA列表
     */
    @RequestMapping(value = "getDlvrOa", method = RequestMethod.POST)
    public List<DlvrOAProcessDTO> getDlvrOa(@RequestParam(value = "dlvrId", required = false, defaultValue = "") Long dlvrId) {
        return wbspToOaService.getDlvrOa(dlvrId);
    }

    /**
     * 创建工作包（建筑）
     */
    @RequestMapping(value = "createDlvr", method = RequestMethod.POST)
    public Map createDlvr(@RequestBody CreateDlvrDTO createDlvrDTO) {
        return wbspToOaService.createDlvr(createDlvrDTO);
    }
}


