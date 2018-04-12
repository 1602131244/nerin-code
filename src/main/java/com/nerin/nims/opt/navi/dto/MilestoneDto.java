package com.nerin.nims.opt.navi.dto;


import com.nerin.nims.opt.base.rest.OracleBaseDTO;
import com.nerin.nims.opt.nbcc.dto.TemplateChaptersDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class MilestoneDto extends OracleBaseDTO {
    private String title;
    private boolean folder = false;
    private String tooltip;

    private Long milestoneId;
    private Long lineId;
    private Long projectId;
    private String name;
    private Long phaseCode;  //阶段ID
    private Long phasePercent; //估算进度
    private String  percent;  //阶段百分比 0/0
    private Float phaseWeight; //阶段权重
    private Date planStartDate;
    private Date planEndDate;
    private Date actualCompleteDate;
    private String milestoneStatus;
    private String approveStatus;
    private Long version;
    private Long parentId;
    private Long seqNum;
    private String ismark;//是否被收款计划标记
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private String attribute6;
    private String attribute7;
    private String attribute8;
    private String attribute9;
    private String attribute10;
   private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private List<MilestoneDto> children;

    public String getIsmark() {
        return ismark;
    }

    public void setIsmark(String ismark) {
        this.ismark = ismark;
    }

    public String getTitle() {
        return title;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    public String getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }

    public String getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }

    public Long getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhaseCode() {
        return phaseCode;
    }

    public void setPhaseCode(Long phaseCode) {
        this.phaseCode = phaseCode;
    }

    public Long getPhasePercent() {
        return phasePercent;
    }

    public void setPhasePercent(Long phasePercent) {
        this.phasePercent = phasePercent;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    public Float getPhaseWeight() {
        return phaseWeight;
    }

    public void setPhaseWeight(Float phaseWeight) {
        this.phaseWeight = phaseWeight;
    }

    public Date getPlanStartDate() {
        return planStartDate;
    }

    public void setPlanStartDate(Date planStartDate) {
        this.planStartDate = planStartDate;
    }

    public Date getPlanEndDate() {
        return planEndDate;
    }

    public void setPlanEndDate(Date planEndDate) {
        this.planEndDate = planEndDate;
    }

    public Date getActualCompleteDate() {
        return actualCompleteDate;
    }

    public void setActualCompleteDate(Date actualCompleteDate) {
        this.actualCompleteDate = actualCompleteDate;
    }

    public String getMilestoneStatus() {
        return milestoneStatus;
    }

    public void setMilestoneStatus(String milestoneStatus) {
        this.milestoneStatus = milestoneStatus;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Long seqNum) {
        this.seqNum = seqNum;
    }

    public List<MilestoneDto> getChildren() {
        return children;
    }

    public void setChildren(List<MilestoneDto> children) {
        this.children = children;
    }
}
