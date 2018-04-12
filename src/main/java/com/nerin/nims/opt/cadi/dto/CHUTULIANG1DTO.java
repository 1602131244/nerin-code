package com.nerin.nims.opt.cadi.dto;

/**
 * Created by user on 16/7/15.
 */
public class CHUTULIANG1DTO {

    private String project_num; //项目编号
    private String project_name; //项目名称
    private String designed_name; //设计人名
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

    public String getDesigned_name() {
        return designed_name;
    }

    public void setDesigned_name(String designed_name) {
        this.designed_name = designed_name;
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
