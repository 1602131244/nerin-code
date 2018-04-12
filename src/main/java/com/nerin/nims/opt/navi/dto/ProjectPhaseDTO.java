package com.nerin.nims.opt.navi.dto;

/**
 * Created by yinglgu on 7/15/2016.
 */
public class ProjectPhaseDTO {

    private Long projectId;//number:项目id；
    private String phaseCode;//varchar2:阶段代码；
    private String phaseName;//varchar2:阶段名称；

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPhaseCode() {
        return phaseCode;
    }

    public void setPhaseCode(String phaseCode) {
        this.phaseCode = phaseCode;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }
}
