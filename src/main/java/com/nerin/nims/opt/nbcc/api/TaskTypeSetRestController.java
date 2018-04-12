package com.nerin.nims.opt.nbcc.api;

import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import com.nerin.nims.opt.nbcc.dto.TaskTypeSetDTO;
import com.nerin.nims.opt.nbcc.service.TaskTypeSetService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by yinglgu on 6/26/2016.
 */
@RestController
@RequestMapping("/api/taskTypeSetRest")
public class TaskTypeSetRestController {

    @Autowired
    private TaskTypeSetService taskTypeSetService;

    @RequestMapping(value = "saveTaskType", method = RequestMethod.POST)
    public Map saveTaskType(@RequestBody TaskTypeSetDTO taskTypeSetDTO, HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        int i = 0;
        // 如果有删除id，先进行删除
        if (StringUtils.isNotEmpty(taskTypeSetDTO.getDelRoles())) {
            Map dMap = taskTypeSetService.delTaskTypeAssignments(taskTypeSetDTO.getDelRoles());
            if (1 != (Long) dMap.get(NbccParm.DB_STATE)) {
                restFulData.put(RestFulData.RETURN_CODE, dMap.get(NbccParm.DB_STATE) + "");
                restFulData.put(RestFulData.RETURN_MSG, dMap.get(NbccParm.DB_MSG) + "");
                i = 1;
            } else {
                restFulData.putAll(dMap);
            }
        }
        if (0 == i) {
            if (StringUtils.isNotEmpty(taskTypeSetDTO.getDelElements())) {
                Map dMap = taskTypeSetService.delTaskTypeElements(taskTypeSetDTO.getDelRoles());
                if (1 != (Long) dMap.get(NbccParm.DB_STATE)) {
                    restFulData.put(RestFulData.RETURN_CODE, dMap.get(NbccParm.DB_STATE) + "");
                    restFulData.put(RestFulData.RETURN_MSG, dMap.get(NbccParm.DB_MSG) + "");
                    i = 1;
                } else {
                    restFulData.putAll(dMap);
                }
            }
        }
        if (0 == i) {
            Map reMap = taskTypeSetService.addOrUpTaskType(taskTypeSetDTO, SessionUtil.getLdapUserInfo(request).getUserId());
            if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
                restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
                restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
            } else {
                restFulData.putAll(reMap);
            }
        }
        return restFulData;
    }

    /**
     * 查询文本类型列表
     * @param taskTypeDesc
     * @param taskTypeCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "taskTypeList", method = RequestMethod.GET)
    public DataTablesDTO queryTaskTypeList(@RequestParam(value = "taskTypeDesc", required = false, defaultValue = "") String taskTypeDesc,
                                           @RequestParam(value = "taskTypeCode", required = false, defaultValue = "") String taskTypeCode,
                                                 @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize) {
        return taskTypeSetService.getTaskTypeList(taskTypeDesc, taskTypeCode, null, pageNo, pageSize);
    }

    /**
     * 获取用户有权限（有权限的所有文本任务使用过的文本类型）查看的任务类型
     * @param taskTypeDesc
     * @param taskTypeCode
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "userTaskTypeList", method = RequestMethod.GET)
    public DataTablesDTO queryUserTaskTypeList(@RequestParam(value = "taskTypeDesc", required = false, defaultValue = "") String taskTypeDesc,
                                           @RequestParam(value = "taskTypeCode", required = false, defaultValue = "") String taskTypeCode,
                                           @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                           HttpServletRequest request) {
        return taskTypeSetService.getTaskTypeList(taskTypeDesc, taskTypeCode, SessionUtil.getLdapUserInfo(request).getUserId(), pageNo, pageSize);
    }


    /**
     * 查询项目创建角色和审批提交角色
     * @param taskTypeCode
     * @param type
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "taskTypeAssignments", method = RequestMethod.GET)
    public DataTablesDTO queryTaskTypeAssignments(@RequestParam(value = "taskTypeCode", required = false, defaultValue = "") String taskTypeCode,
                                                  @RequestParam(value = "type", required = false, defaultValue = "") String type,
                                           @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize) {
        return taskTypeSetService.getTaskTypeAssignments(taskTypeCode, type, pageNo, pageSize);
    }


    /**
     * 查询文本类型分配的工作包类型列表
     * @param taskTypeCode
     * @param elementTypeId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "taskTypeElements", method = RequestMethod.GET)
    public DataTablesDTO queryTaskTypeElements(@RequestParam(value = "taskTypeCode", required = false, defaultValue = "") String taskTypeCode,
                                                  @RequestParam(value = "elementTypeId", required = false, defaultValue = "") Long elementTypeId,
                                                  @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                  @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize) {
        return taskTypeSetService.getTaskTypeElements(taskTypeCode, elementTypeId, pageNo, pageSize);
    }

    /**
     * 项目阶段列表
     * @param taskTypeCode
     * @param projectId         文本创建时要输入参数项目编号，否则传空
     * @return
     */
    @RequestMapping(value = "projectPhase", method = RequestMethod.GET)
    public DataTablesDTO queryProjectPhaseList(@RequestParam(value = "taskTypeCode", required = false, defaultValue = "") String taskTypeCode,
                                               @RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId
    ) {
        return taskTypeSetService.getProjectPhaseList(taskTypeCode,projectId);
    }

    /**
     * 获取文本类型大类
     * @param phaseCategorCode
     * @return
     */
    @RequestMapping(value = "projectPhaseCategor", method = RequestMethod.GET)
    public DataTablesDTO queryProjectPhaseCategorList(@RequestParam(value = "phaseCategorCode", required = false, defaultValue = "") String phaseCategorCode) {
        return taskTypeSetService.getProjectPhaseCategorList(phaseCategorCode);
    }
    /**
     * 删除文本类型
     * @param taskTypeCodes
     * @return
     */
    @RequestMapping(value = "delTaskType", method = RequestMethod.POST)
    public Map delTaskType(@RequestParam(value = "taskTypeCodes", required = false, defaultValue = "-1") String taskTypeCodes) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskTypeSetService.delTaskType(taskTypeCodes);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 文本类型，code、name创建前的唯一校验
     * @param taskTypeCode
     * @param taskTypeName
     * @param work
     * @return
     */
    @RequestMapping(value = "valTask", method = RequestMethod.GET)
    public String valTask(@RequestParam(value = "taskTypeCode", required = false, defaultValue = "") String taskTypeCode,
                                  @RequestParam(value = "taskTypeName", required = false, defaultValue = "") String taskTypeName,
                                  @RequestParam(value = "work", required = false, defaultValue = "") int work) {
        int v = taskTypeSetService.checkTaskTypeCodeAndName(taskTypeCode, taskTypeName, work);
        if (1 == v)
            return "true";
        else
            return "false";
    }

    /**
     * 删除分配的项目角色和审批提交角色
     * @param taskTypeAssignments
     * @return
     */
    @RequestMapping(value = "delTaskTypeAssignments", method = RequestMethod.POST)
    public Map delTaskTypeAssignments(@RequestParam(value = "taskTypeAssignments", required = false, defaultValue = "-1") String taskTypeAssignments) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskTypeSetService.delTaskTypeAssignments(taskTypeAssignments);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 更新文本类型状态
     * @param taskTypeCode
     * @param taskTypeStatus
     * @return
     */
    @RequestMapping(value = "upDateTaskTypeStatus", method = RequestMethod.POST)
    public Map updateTaskTypeStatus(@RequestParam(value = "taskTypeCode", required = false, defaultValue = "") String taskTypeCode,
                                    @RequestParam(value = "taskTypeStatus", required = false, defaultValue = "") int taskTypeStatus) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskTypeSetService.updateTaskTypeStatus(taskTypeCode, taskTypeStatus);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

}
