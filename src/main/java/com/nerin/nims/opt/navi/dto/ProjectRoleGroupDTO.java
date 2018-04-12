package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2017/3/6.
 */
public class ProjectRoleGroupDTO {
    private Long seq;
    private String roleGroup;
    private String role_name;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getRoleGroup() {
        return roleGroup;
    }

    public void setRoleGroup(String roleGroup) {
        this.roleGroup = roleGroup;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }
}
