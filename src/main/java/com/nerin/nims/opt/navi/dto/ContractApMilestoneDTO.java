package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2016/7/21.
 */
public class ContractApMilestoneDTO {
    private Long lineId;
    private String name;
    private String milestoneStatus;
    private String conlineNum;
    private Long projectId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getConlineNum() {
        return conlineNum;
    }

    public void setConlineNum(String conlineNum) {
        this.conlineNum = conlineNum;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMilestoneStatus() {
        return milestoneStatus;
    }

    public void setMilestoneStatus(String milestoneStatus) {
        this.milestoneStatus = milestoneStatus;
    }
}
