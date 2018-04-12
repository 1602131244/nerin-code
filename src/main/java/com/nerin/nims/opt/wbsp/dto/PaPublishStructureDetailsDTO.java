package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/25.
 */
public class PaPublishStructureDetailsDTO extends TreeDataDTO {
    private Long taskElementId;
    //private Long taskElementVerId;
    private Long taskWbsLevel;
    private Long taskStructureVerId;
    private String taskStructureName;
    private String level;
    private String taskWbsNumber;
    private String taskName;
    private Long taskTypeId;
    private String taskType;
    private Date taskStartDate;
    private Date taskEndDate;
    private String taskBilingual;
    private String taskWorkload;
    private String taskStatusCode;
    private String defaultFlag;
    //private Long taskParentsId;

    public Long getTaskWbsLevel() {
        return taskWbsLevel;
    }

    public void setTaskWbsLevel(Long taskWbsLevel) {
        this.taskWbsLevel = taskWbsLevel;
    }

    public Long getTaskElementId() {
        return taskElementId;
    }

    public void setTaskElementId(Long taskElementId) {
        this.taskElementId = taskElementId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTaskWbsNumber() {
        return taskWbsNumber;
    }

    public void setTaskWbsNumber(String taskWbsNumber) {
        this.taskWbsNumber = taskWbsNumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(Long taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Date getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Date getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(Date taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public String getTaskBilingual() {
        return taskBilingual;
    }

    public void setTaskBilingual(String taskBilingual) {
        this.taskBilingual = taskBilingual;
    }

    public String getTaskWorkload() {
        return taskWorkload;
    }

    public void setTaskWorkload(String taskWorkload) {
        this.taskWorkload = taskWorkload;
    }

    public String getTaskStatusCode() {
        return taskStatusCode;
    }

    public void setTaskStatusCode(String taskStatusCode) {
        String statusCode = new String();
        if (taskStatusCode == null) {
            statusCode = "生效";
        } else {
            switch (taskStatusCode) {
                case "N":
                    statusCode = "新增";
                    break;
                case "E":
                    statusCode = "生效";
                    break;
                case "L":
                    statusCode = "失效";
                    break;
                default:
                    statusCode = "生效";
                    break;
            }
        }

        this.taskStatusCode = statusCode;
    }

    public Long getTaskStructureVerId() {
        return taskStructureVerId;
    }

    public void setTaskStructureVerId(Long taskStructureVerId) {
        this.taskStructureVerId = taskStructureVerId;
    }

    public String getTaskStructureName() {
        return taskStructureName;
    }

    public void setTaskStructureName(String taskStructureName) {
        this.taskStructureName = taskStructureName;
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
}
