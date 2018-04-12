package com.nerin.nims.opt.tsc.tc.dto;

/**
 * Created by Administrator on 2017/5/19.
 */
public class LxrDTO {
    private String personName;//          姓名,
    private String customerName;//    VARCHAR2(360), --客户单位
    private String attribute1;//      VARCHAR2(360), --任职履历
    private String attribute2;//      VARCHAR2(360), --职位
    private String email;//      VARCHAR2(360), --电子邮箱
    private String phone;//      VARCHAR2(360), --联系电话
    private String address;//      VARCHAR2(360), --地址
    private String custI;//      VARCHAR2(360), --行业

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustI() {
        return custI;
    }

    public void setCustI(String custI) {
        this.custI = custI;
    }
}
