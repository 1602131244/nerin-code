package com.nerin.nims.opt.base.rest;

/**
 * Created by yinglgu on 6/24/2016.
 */
public class LdapUser {

    private Long userId;

    private Long personId;//number:员工id;
    private String userName;//varchar2:用户名；
    private String fullName;//varchar2:姓名；
    private String deptOrgId;//varchar2:部门id
    private String deptOrgName;//varchar2:部门名称；
    private String mobile;//varchar2:电话；
    private String email;//varchar2:电子邮箱；
    private String photo;//varchar2:照片头像路径；
    private String employeeNo;//varchar2:员工编号

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDeptOrgId() {
        return deptOrgId;
    }

    public void setDeptOrgId(String deptOrgId) {
        this.deptOrgId = deptOrgId;
    }

    public String getDeptOrgName() {
        return deptOrgName;
    }

    public void setDeptOrgName(String deptOrgName) {
        this.deptOrgName = deptOrgName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
