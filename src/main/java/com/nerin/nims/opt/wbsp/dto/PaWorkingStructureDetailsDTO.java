package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/25.
 */
public class PaWorkingStructureDetailsDTO {
    private Long taskElementId;
    private Long id;
    private Long taskWbsLevel;
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
    private String taskSourceClass;
    private String taskSourceStructureVerId;
    private String taskSourceStructurename;
    private String taskSourceEleVerId;
    private String flag;//如果不存在值，那么就是未处理过的项目
    private String defaultFlag;//默认系统子项标记
    private Long parentId;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (taskWbsNumber == null) {this.taskWbsNumber = "";}
        else{
        this.taskWbsNumber = taskWbsNumber;}
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
        switch (taskStatusCode)
        {
            case "N" :
                statusCode = "新增";
                break;
            case "E" :
                statusCode = "生效";
                break;
            case "L" :
                statusCode = "失效";
                break;
        }
        this.taskStatusCode = statusCode;
    }

    public String getTaskSourceStructureVerId() {
        return taskSourceStructureVerId;
    }

    public void setTaskSourceStructureVerId(String taskSourceStructureVerId) {
        this.taskSourceStructureVerId = taskSourceStructureVerId;
    }

    public String getTaskSourceClass() {
        return taskSourceClass;
    }

    public void setTaskSourceClass(String taskSourceClass) {
        this.taskSourceClass = taskSourceClass;
    }

    public String getTaskSourceStructurename() {
        return taskSourceStructurename;
    }

    public void setTaskSourceStructurename(String taskSourceStructurename) {
        this.taskSourceStructurename = taskSourceStructurename;
    }

    public String getTaskSourceEleVerId() {
        return taskSourceEleVerId;
    }

    public void setTaskSourceEleVerId(String taskSourceEleVerId) {
        this.taskSourceEleVerId = taskSourceEleVerId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
}
