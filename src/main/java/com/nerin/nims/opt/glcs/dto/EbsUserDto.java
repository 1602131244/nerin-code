package com.nerin.nims.opt.glcs.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

import java.util.Date;

/**
 * Created by Administrator on 2017/10/23 0023.
 */
public class EbsUserDto extends OracleBaseDTO {
    private Long userId;
    private String userNumber;
    private String userName;
    private String description;
    private Date startdate;
    private Date endDate;
    private Long employeeId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}
