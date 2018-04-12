package com.nerin.nims.opt.navi.api;

import com.nerin.nims.opt.app.web.rest.dto.DataTableDTO;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.navi.dto.ProjectTextOaDTO;
import com.nerin.nims.opt.navi.dto.ProjectYBlineDTO;
import com.nerin.nims.opt.navi.dto.YBLDataTableDTO;
import com.nerin.nims.opt.navi.service.PtextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
@RestController
@RequestMapping("/api/Ptext")
public class PtextRestController {
    @Autowired
    public PtextService ptextService;

    /**
     * 获取项目文本信息
     * @param projectId  项目ID
     * @return
     */
    @RequestMapping(value = "text", method = RequestMethod.GET)
    public DataTableDTO queryPtext(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId
    ) {

        return ptextService.getptext(projectId);
    }
     //文本OA
    @RequestMapping(value = "textoa", method = RequestMethod.GET)
    public List<ProjectTextOaDTO> queryPtextOa(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                               @RequestParam(value = "taskType", required = false, defaultValue = "") String taskType
    ) {

        return ptextService.getPtextOA(taskHeaderId,taskType);
    }
    //月报头表
    @RequestMapping(value = "ybhead", method = RequestMethod.GET)
    public DataTableDTO queryPybhead(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId

    ) {

        return ptextService.getybhead(projectId);
    }
    //月报行表
    @RequestMapping(value = "ybline", method = RequestMethod.GET)
    public YBLDataTableDTO queryPybline(@RequestParam(value = "pHeadId", required = false, defaultValue = "") Long pHeadId,
                                        @RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId

    ) {

        return ptextService.getybline(pHeadId,projectId);
    }

    //创建月报时取阶段，拼接标题，如果message返回‘F’,没有批准的里程碑
    @RequestMapping(value = "ybphase", method = RequestMethod.GET)
    public YBLDataTableDTO queryPybphase(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId

    ) {

        return ptextService.getybphase(projectId);
    }

    @RequestMapping(value = "savehead", method = RequestMethod.POST)
    public Long saveybhead(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                           @RequestParam(value = "pHeadId", required = false, defaultValue = "") Long pHeadId,
                           @RequestParam(value = "pBt", required = false, defaultValue = "") String pBt,
                           @RequestParam(value = "pYxqk", required = false, defaultValue = "") String pYxqk,
                           @RequestParam(value = "pSfqk", required = false, defaultValue = "") String pSfqk,
                           HttpServletRequest request) {
        return ptextService.saveybhead(projectId, pHeadId,pBt,pYxqk,pSfqk,SessionUtil.getLdapUserInfo(request).getUserId());

    }
//保存行
    @RequestMapping(value = "saveline", method = RequestMethod.POST)
    public String saveybline(@RequestBody List<ProjectYBlineDTO> projectYBlineDTOs, HttpServletRequest request
    ) {
        long pHeadId = projectYBlineDTOs.get(0).getHeadID();
        ptextService.saveybline(pHeadId,projectYBlineDTOs,SessionUtil.getLdapUserInfo(request).getUserId());
        return "true";
    }

    @RequestMapping(value = "delYb", method = RequestMethod.POST)
    public String delYb(@RequestParam(value = "pHeadId", required = false, defaultValue = "") Long pHeadId
    ) {
        ptextService.delYb(pHeadId);
        return "true";
    }
}
