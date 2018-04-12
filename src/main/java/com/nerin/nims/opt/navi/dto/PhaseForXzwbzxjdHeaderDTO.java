package com.nerin.nims.opt.navi.dto;

import java.util.Date;

/**
 * Created by user on 16/11/9.
 */
public class PhaseForXzwbzxjdHeaderDTO {
    private Long taskHeaderId;
    private String taskName;
    private String progress;
    private String creater;
    private String taskProgress;
    private String taskProgress2;
    private String status;
    private String tag;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Long getTaskHeaderId() {
        return taskHeaderId;
    }

    public void setTaskHeaderId(Long taskHeaderId) {
        this.taskHeaderId = taskHeaderId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
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

    public String getTaskProgress2() {
        return taskProgress2;
    }

    public void setTaskProgress2(String taskProgress2) {
        this.taskProgress2 = taskProgress2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}