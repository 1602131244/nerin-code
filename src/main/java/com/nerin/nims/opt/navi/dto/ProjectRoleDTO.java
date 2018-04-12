package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2016/7/7.
 */
public class ProjectRoleDTO {
    private Long projectRoleId;

    public String getProjectRole() {
        return projectRole;
    }

    public void setProjectRole(String projectRole) {
        this.projectRole = projectRole;
    }

    public Long getProjectRoleId() {
        return projectRoleId;
    }

    public void setProjectRoleId(Long projectRoleId) {
        this.projectRoleId = projectRoleId;
    }

    private String projectRole;
}
