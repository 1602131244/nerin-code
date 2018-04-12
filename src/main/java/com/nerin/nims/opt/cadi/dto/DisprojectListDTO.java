package com.nerin.nims.opt.cadi.dto;

/**
 * Created by user on 16/7/15.
 */
public class DisprojectListDTO {

    private String segment1; //项目编号
    private String name; //项目名称
    private String disname; //显示名称：项目编号+项目名称
    private String project_id; //ID

    public String getSegment1() {
        return segment1;
    }

    public void setSegment1(String segment1) {
        this.segment1 = segment1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisname() {
        return disname;
    }

    public void setDisname(String disname) {
        this.disname = disname;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }
}
