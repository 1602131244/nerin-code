package com.nerin.nims.opt.navi.dto;

import java.util.Date;

/**
 * Created by yinglgu on 7/15/2016.
 */
public class ProjectListDTO {

    private Long projectId;//number://项目id；
    private String projectNumber;//varchar2;//项目编号；
    private String projectName;//varchar2://项目名称
    private String projectLongName;//varchar2;//项目全称
    private String customer;//varchar2：客户
    private String projectStatus;//varchar2：项目状态
    private String projectType;//varchar2:项目类型
    private String projectClass;//varchar2:行业类型
    private String projectManager;//varchar2:项目经理
    private String projectOrgName;//varchar2：所属组织
    private Date projStartDate;//date:项目开始时间
    private Date projEndDate;//date:项目结束时间
    private Date startDate;//date:事务处理开始时间
    private Date completionDate;//date:事务处理结束时间
    private String overdue;//varchar2: 是否财务过期

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectLongName() {
        return projectLongName;
    }

    public void setProjectLongName(String projectLongName) {
        this.projectLongName = projectLongName;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectClass() {
        return projectClass;
    }

    public void setProjectClass(String projectClass) {
        this.projectClass = projectClass;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getProjectOrgName() {
        return projectOrgName;
    }

    public void setProjectOrgName(String projectOrgName) {
        this.projectOrgName = projectOrgName;
    }

    public Date getProjStartDate() {
        return projStartDate;
    }

    public void setProjStartDate(Date projStartDate) {
        this.projStartDate = projStartDate;
    }

    public Date getProjEndDate() {
        return projEndDate;
    }

    public void setProjEndDate(Date projEndDate) {
        this.projEndDate = projEndDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getOverdue() {
        return overdue;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
    }
}
