package com.nerin.nims.opt.cadi.dto;

/**
 * Created by user on 16/7/15.
 */
public class CHUTULIANG3DTO {

    private String project_num; //项目编号
    private String project_name; //项目名称
    private String organization_name; //部门名
    private Long zrzs;        //自然张数
    private Long a1; //折A1张数

    public String getProject_num() {
        return project_num;
    }

    public void setProject_num(String project_num) {
        this.project_num = project_num;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }

    public Long getZrzs() {
        return zrzs;
    }

    public void setZrzs(Long zrzs) {
        this.zrzs = zrzs;
    }

    public Long getA1() {
        return a1;
    }

    public void setA1(Long a1) {
        this.a1 = a1;
    }
}
