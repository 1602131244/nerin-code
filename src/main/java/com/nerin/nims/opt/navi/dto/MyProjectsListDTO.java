package com.nerin.nims.opt.navi.dto;
import java.util.Date;
/**
 * Created by user on 16/7/14.
 */
public class MyProjectsListDTO {
    private Long projectId;
    private String projectNumber; //项目编号
    private String projectName; //项目名称
    private String projectLongName; //项目全称
    private String customer; //客户
    private String projectStatusName; //项目状态
    private String projectType; //项目类型
    private String projectClass; //行业类型
    private String projectManager; //项目经理
    private String projectOrgName; //所属组织
    private String projectExeName; //执行部门
    private Date projStartDate; //项目开始时间
    private Date projEndDate; //项目结束时间
    private Date startDate; //事务处理开始时间

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    private Date completionDate; //事务处理完成时间
    private String overdue;    //是否财务过期

    public String getOverdue() {
        return overdue;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
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

    public String getProjectStatusName() {
        return projectStatusName;
    }

    public void setProjectStatusName(String projectStatusName) {
        this.projectStatusName = projectStatusName;
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

    public String getProjectExeName() {
        return projectExeName;
    }

    public void setProjectExeName(String projExeName) {
        this.projectExeName = projExeName;
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


}
