package com.nerin.nims.opt.navi.dto;

import oracle.jdbc.driver.DatabaseError;
import oracle.sql.DATE;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/3.
 */
public class ProjectInfoDTO {
    private Long projectId;//项目id
    private String projectNumber;//项目编号
    private String projectName;//项目名称
    private String manager;//项目经理
    private String projectLongName;//项目长名称
    private String projectClass;//项目类型
    private Long orgId;//所属组织Id
    private String orgName;//所属组织
    private Long exeId;//执行部门Id
    private String exeName;//执行部门
    private Date projStartDate;//项目开始时间
    private Date projEndDate;//项目结束时间
    private Date startDate;//事务处理开始时间
    private Date completionDate;//事务处理结束时间
    private String customer;//客户
    private String projectStatus;//项目状态
    private String projectStatusCode;//项目状态
    private String overdue;//是否财务超期

    private String sysDate;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getExeId() {
        return exeId;
    }

    public void setExeId(Long exeId) {
        this.exeId = exeId;
    }

    public String getSysDate() {
        return sysDate;
    }

    public void setSysDate(String sysDate) {
        this.sysDate = sysDate;
    }

    public String getProjectStatusCode() {
        return projectStatusCode;
    }

    public void setProjectStatusCode(String projectStatusCode) {
        this.projectStatusCode = projectStatusCode;
    }

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

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getProjectLongName() {
        return projectLongName;
    }

    public void setProjectLongName(String projectLongName) {
        this.projectLongName = projectLongName;
    }

    public String getProjectClass() {
        return projectClass;
    }

    public void setProjectClass(String projectClass) {
        this.projectClass = projectClass;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getExeName() {
        return exeName;
    }

    public void setExeName(String exeName) {
        this.exeName = exeName;
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

    public String getOverdue() {
        return overdue;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
    }
}
