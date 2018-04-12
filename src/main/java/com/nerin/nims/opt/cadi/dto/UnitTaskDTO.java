package com.nerin.nims.opt.cadi.dto;

/**
 * Created by user on 16/7/15.
 */
public class UnitTaskDTO {

    private Long projectId;  //项目ID
    private Long phaseId; //阶段ID
    private String phaseCode; //阶段代码
    private String phaseName; //阶段名称
    private String unitTaskId;  //子项ID
    private String unitTaskName; //子项名称
    private String unitTaskCode; //子项代码

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
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

    public String getUnitTaskId() {
        return unitTaskId;
    }

    public void setUnitTaskId(String unitTaskId) {
        this.unitTaskId = unitTaskId;
    }

    public String getUnitTaskName() {
        return unitTaskName;
    }

    public void setUnitTaskName(String unitTaskName) {
        this.unitTaskName = unitTaskName;
    }

    public String getUnitTaskCode() {
        return unitTaskCode;
    }

    public void setUnitTaskCode(String unitTaskCode) {
        this.unitTaskCode = unitTaskCode;
    }
}
