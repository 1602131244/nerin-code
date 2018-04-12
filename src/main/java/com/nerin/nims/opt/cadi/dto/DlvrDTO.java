package com.nerin.nims.opt.cadi.dto;

/**
 * Created by user on 16/7/15.
 */
public class DlvrDTO {

    private Long projectId;
    private String SpecialityCode;
    private String SpecialityName;
    private String taskType;
    private Long dlvrId;
    private String dlvrCode;
    private String dlvrName;

    public String getDlvrName() {
        return dlvrName;
    }

    public void setDlvrName(String dlvrName) {
        this.dlvrName = dlvrName;
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSpecialityCode() {
        return SpecialityCode;
    }

    public void setSpecialityCode(String specialityCode) {
        SpecialityCode = specialityCode;
    }

    public String getSpecialityName() {
        return SpecialityName;
    }

    public void setSpecialityName(String specialityName) {
        SpecialityName = specialityName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Long getDlvrId() {
        return dlvrId;
    }

    public void setDlvrId(Long dlvrId) {
        this.dlvrId = dlvrId;
    }

    public String getDlvrCode() {
        return dlvrCode;
    }

    public void setDlvrCode(String dlvrCode) {
        this.dlvrCode = dlvrCode;
    }
}
