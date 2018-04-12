package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class ProjectTextDTO {
    private String taskName;
    private Long taskheaderId;
    private String creater;
    private String taskProgress;
    private String status;
    private String taskType;

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Long getTaskheaderId() {
        return taskheaderId;
    }

    public void setTaskheaderId(Long taskheaderId) {
        this.taskheaderId = taskheaderId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getTaskProgress() {
        return taskProgress;
    }

    public void setTaskProgress(String taskProgress) {
        this.taskProgress = taskProgress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
