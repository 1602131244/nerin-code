package com.nerin.nims.opt.nbcc.dto;

/**
 * Created by Administrator on 2016/7/18.
 */
public class TaskResponsibleDto {
    private long projectId; //项目ID
    private long roleId; // 项目角色ID
    private String roleName; // 项目角色名称
    private String specialty;  //专业
    private String specialtyName; //专业名称
    private long personIdResponsible;  //节点负责人的ID
    private String personNameResponsible; //节点负责人的名称

    public long getPersonIdResponsible() {
        return personIdResponsible;
    }

    public void setPersonIdResponsible(long personIdResponsible) {
        this.personIdResponsible = personIdResponsible;
    }

    public String getPersonNameResponsible() {
        return personNameResponsible;
    }

    public void setPersonNameResponsible(String personNameResponsible) {
        this.personNameResponsible = personNameResponsible;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

}
