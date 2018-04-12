package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class ProjectPersonInfoDTO {
    private String fullName;//姓名
    private String assignment_name;//项目角色
    private String specaility_name;//专业
    private String m_phone;//移动电话
    private String work_phone;//固定电话
    private String email_address;//电子邮箱

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAssignment_name() {
        return assignment_name;
    }

    public void setAssignment_name(String assignment_name) {
        this.assignment_name = assignment_name;
    }

    public String getSpecaility_name() {
        return specaility_name;
    }

    public void setSpecaility_name(String specaility_name) {
        this.specaility_name = specaility_name;
    }

    public String getM_phone() {
        return m_phone;
    }

    public void setM_phone(String m_phone) {
        this.m_phone = m_phone;
    }

    public String getWork_phone() {
        return work_phone;
    }

    public void setWork_phone(String work_phone) {
        this.work_phone = work_phone;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }
}
