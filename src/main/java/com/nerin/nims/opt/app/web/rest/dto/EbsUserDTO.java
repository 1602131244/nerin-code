package com.nerin.nims.opt.app.web.rest.dto;

/**
 * Created by yinglgu on 10/17/2016.
 */
public class EbsUserDTO {
    private String empNum;
    private String lastName;
    private String sysAccount;
    private String orgName;

    public String getEmpNum() {
        return empNum;
    }

    public void setEmpNum(String empNum) {
        this.empNum = empNum;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSysAccount() {
        return sysAccount;
    }

    public void setSysAccount(String sysAccount) {
        this.sysAccount = sysAccount;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
