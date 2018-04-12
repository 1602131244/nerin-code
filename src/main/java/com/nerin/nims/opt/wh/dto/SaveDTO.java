package com.nerin.nims.opt.wh.dto;

import java.util.ArrayList;
import java.util.List;


public class SaveDTO {
    private  Long projId;
    private  Long taskId;
    private  Long dlvrId;
    private  float updateWorkHour;
    private  String commit;


    public Long getProjId() {
        return projId;
    }

    public void setProjId(Long projId) {
        this.projId = projId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getDlvrId() {
        return dlvrId;
    }

    public void setDlvrId(Long dlvrId) {
        this.dlvrId = dlvrId;
    }

    public float getUpdateWorkHour() {
        return updateWorkHour;
    }

    public void setUpdateWorkHour(float updateWorkHour) {
        this.updateWorkHour = updateWorkHour;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }
}
