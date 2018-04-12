package com.nerin.nims.opt.navi.api;

import com.nerin.nims.opt.app.web.rest.dto.DataTableDTO;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.navi.dto.CDataTableDTO;
import com.nerin.nims.opt.navi.service.PinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2016/8/7.
 */
@RestController
@RequestMapping("/api/Pinfo")
public class PinfoRestController {
    @Autowired
    public PinfoService pinfoService;


    /**
     * 获取项目信息
     * @param projectId  项目ID
     * @return
     */
    @RequestMapping(value = "info", method = RequestMethod.GET)
    public DataTableDTO queryProjInfo(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId
    ) {

        return pinfoService.getProjectInfo(projectId);
    }

    /**
     * 获取项目人员信息
     * @param projectId  项目ID
     * @param  inpage 页数
     * @param insize  每页行数
     * @return
     */
    @RequestMapping(value = "personInfo", method = RequestMethod.GET)
    public DataTableDTO queryProjPersonInfo(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                            @RequestParam(value = "inpage", required = false, defaultValue = "1") Long inpage,
                                            @RequestParam(value = "insize", required = false, defaultValue = "300") Long insize
    ) {

        return pinfoService.getProjectpersonInfo(projectId,inpage,insize);
    }

    /**
     * 获取项目管理工时
     * @param projectId  项目ID
     * @param userId  用户id
     * @param ismanager  是否是项目经理
     * @return
     */
    @RequestMapping(value = "Mhours", method = RequestMethod.GET)
    public CDataTableDTO queryProjMHours(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                         @RequestParam(value = "userId", required = false, defaultValue = "") Long userId,
                                         @RequestParam(value = "ismanager", required = false, defaultValue = "") Long ismanager
    ) {

        return pinfoService.getManageHours(projectId,userId,ismanager);
    }

    /**
     * 获取项目施工服务工时
     * @param projectId  项目ID
     * @param userId  用户id
     * @param ismanager  是否是项目经理
     * @return
     */
    @RequestMapping(value = "Shours", method = RequestMethod.GET)
    public CDataTableDTO queryProjSHours(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                         @RequestParam(value = "userId", required = false, defaultValue = "") Long userId,
                                         @RequestParam(value = "ismanager", required = false, defaultValue = "") Long ismanager
    ) {

        return pinfoService.getServerHours(projectId,userId,ismanager);
    }

    /**
     * 获取项目工时列表
     * @param projectId  项目ID
     * @param userId  用户id
     * @param ismanager  是否是项目经理
     * @return
     */
    @RequestMapping(value = "Whours", method = RequestMethod.GET)
    public CDataTableDTO queryProjWHours(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                         @RequestParam(value = "userId", required = false, defaultValue = "") Long userId,
                                         @RequestParam(value = "ismanager", required = false, defaultValue = "") Long ismanager,
                                         HttpServletRequest request
    ) {

        return pinfoService.getWorkHours(projectId, SessionUtil.getLdapUserInfo(request).getUserId(),ismanager);
    }
}
