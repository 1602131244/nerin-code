package com.nerin.nims.opt.navi.dto;

/**
 * Created by yinglgu on 7/30/2016.
 */
public class PhaseForXmkgbgDTO {

    private String taskHeaderId;
    private String taskName;//varchar2：文本名称
    private String taskStatusName;//varchar2：状态
    private String taskProgress;//varchar2：章节完成情况
    private String createdName;//varchar2：创建人
    private String templateHeaderId;//

    public String getTemplateHeaderId() {
        return templateHeaderId;
    }

    public void setTemplateHeaderId(String templateHeaderId) {
        this.templateHeaderId = templateHeaderId;
    }

    private String taskProgress2;//varchar2：专业检审完成情况

    public String getTaskProgress2() {
        return taskProgress2;
    }

    public void setTaskProgress2(String taskProgress2) {
        this.taskProgress2 = taskProgress2;
    }

    public String getTaskHeaderId() {
        return taskHeaderId;
    }

    public void setTaskHeaderId(String taskHeaderId) {
        this.taskHeaderId = taskHeaderId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }

    public String getTaskProgress() {
        return taskProgress;
    }

    public void setTaskProgress(String taskProgress) {
        this.taskProgress = taskProgress;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }
}
