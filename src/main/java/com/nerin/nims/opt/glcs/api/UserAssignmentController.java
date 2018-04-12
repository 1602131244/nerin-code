package com.nerin.nims.opt.glcs.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.aria.common.EventParm;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.BusinessException;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.cqs.dto.DataTablesDTO;
import com.nerin.nims.opt.glcs.dto.UserAssignmentDto;
import com.nerin.nims.opt.glcs.service.UserAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/19.
 */
@RestController
@RequestMapping("/api/glcsUserAssignment")
public class UserAssignmentController {

    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    private UserAssignmentService userAssignmentService;


    /**
     * 获取用户列表
     * @param userId
     * @param userNumber
     * @param userName
     * @param usersearch
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "ebsUserList", method = RequestMethod.GET)
    public DataTablesDTO queryUserList(@RequestParam(value = "userId", required = false, defaultValue = "") Long userId,
                                       @RequestParam(value = "userNumber", required = false, defaultValue = "") String userNumber,
                                       @RequestParam(value = "userName", required = false, defaultValue = "") String userName,
                                       @RequestParam(value = "usersearch", required = false, defaultValue = "") String usersearch,
                                       @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                       HttpServletRequest request
    ) {
        return userAssignmentService.getUserList( userId, userNumber, userName, usersearch,  pageNo, pageSize);
    }

    /**
     * 获取报表列表
     * @param reportId
     * @param reportName
     * @param checkSign  判定是否检查查询权限  1 检查  0 不检查
     * @param checkCreate  判定是否检查创建报表权限  1 检查  0 不检查
     * @param userId
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param ledgerId
     * @param companyCode
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "ebsReportList", method = RequestMethod.GET)
    public DataTablesDTO queryEbsReportList(@RequestParam(value = "reportId", required = false, defaultValue = "") Long reportId,
                                             @RequestParam(value = "reportName", required = false, defaultValue = "") String reportName,
                                            @RequestParam(value = "checkSign", required = false, defaultValue = "0") String checkSign,
                                            @RequestParam(value = "checkCreate", required = false, defaultValue = "0") String checkCreate,
                                            @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                            @RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                                            @RequestParam(value = "rangeVerson", required = false, defaultValue = "") Long rangeVerson,
                                            @RequestParam(value = "unitId", required = false, defaultValue = "") Long unitId,
                                            @RequestParam(value = "ledgerId", required = false, defaultValue = "") Long ledgerId,
                                            @RequestParam(value = "companyCode", required = false, defaultValue = "") String companyCode,
                                             @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                             HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();
        return userAssignmentService.getEbsReportList( reportId, reportName, checkSign, checkCreate, userId, rangeId, rangeVerson, unitId,
                ledgerId, companyCode, pageNo, pageSize);
    }

    /**
     * 用户权限分配
     * @param assignmentId
     * @param userId
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param ledgerId
     * @param companyCode
     * @param reportId
     * @param search
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "userAssignmentList", method = RequestMethod.GET)
    public DataTablesDTO queryUserAssignmentList(@RequestParam(value = "assignmentId", required = false, defaultValue = "") Long assignmentId,
                                                @RequestParam(value = "userId", required = false, defaultValue = "") Long userId,
                                                @RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                                                @RequestParam(value = "rangeVerson", required = false, defaultValue = "") Long rangeVerson,
                                                 @RequestParam(value = "unitId", required = false, defaultValue = "") Long unitId,
                                                 @RequestParam(value = "ledgerId", required = false, defaultValue = "") Long ledgerId,
                                                 @RequestParam(value = "companyCode", required = false, defaultValue = "") String companyCode,
                                                 @RequestParam(value = "reportId", required = false, defaultValue = "") Long reportId,
                                                 @RequestParam(value = "search", required = false, defaultValue = "") String search,
                                                @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                                HttpServletRequest request
    ) {
        return userAssignmentService.getUserAssignmentList(assignmentId, userId, rangeId, rangeVerson, unitId, ledgerId, companyCode, reportId, search, pageNo, pageSize);
    }

    /**
     * 保存用户权限分配
     * @param userAssignmentDto
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveUserAssignment", method = RequestMethod.POST)
    public Map saveBillEvent(@RequestBody UserAssignmentDto  userAssignmentDto, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = userAssignmentService.saveUserAssignment (userAssignmentDto, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 删除用户权限分配
     * @param assignmentId
     * @return
     */
    @RequestMapping(value = "delUserAssignment", method = RequestMethod.POST)
    public Map delUserAssignment(@RequestParam(value = "assignmentId", required = false, defaultValue = "") Long assignmentId
                                 ) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = userAssignmentService.delUserAssignment(assignmentId);
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

}
