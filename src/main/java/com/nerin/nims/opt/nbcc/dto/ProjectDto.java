package com.nerin.nims.opt.nbcc.dto;

/**
 * Created by Administrator on 2016/7/5.
 */
public class ProjectDto {

    private Long projectId;
    private String projectNumber;
    private String projectName;
    private String projectManager;
    private Long projOrgId;
    private String projOrgName;

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

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public Long getProjOrgId() {
        return projOrgId;
    }

    public void setProjOrgId(Long projOrgId) {
        this.projOrgId = projOrgId;
    }

    public String getProjOrgName() {
        return projOrgName;
    }

    public void setProjOrgName(String projOrgName) {
        this.projOrgName = projOrgName;
    }
}
