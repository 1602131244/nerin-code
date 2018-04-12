package com.nerin.nims.opt.navi.dto;

import oracle.sql.DATE;

import java.util.Date;

/**
 * Created by yinglgu on 8/9/2016.
 */
public class PhaseForTjdDTO {
    private Long conditionId;//number：条件单id
    private String conditionNumber;//varchar2:条件单编号
    private String conditionName;//varchar2：条件单名称
    private String status;//varchar2：状态
    private String submitDate;//date：提交时间
    private String receiveDate;//date：接收时间
    private String receivePerson;//varchar2：接收人
    private String checkWrokout;//varchar2：是否可以提交编制
    private String scheduleDate;//varchar2：计划编制时间

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getCheckWrokout() {
        return checkWrokout;
    }

    public void setCheckWrokout(String checkWrokout) {
        this.checkWrokout = checkWrokout;
    }

    public Long getConditionId() {
        return conditionId;
    }

    public void setConditionId(Long conditionId) {
        this.conditionId = conditionId;
    }

    public String getConditionNumber() {
        return conditionNumber;
    }

    public void setConditionNumber(String conditionNumber) {
        this.conditionNumber = conditionNumber;
    }

    public String getConditionName() {
        return conditionName;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getReceivePerson() {
        return receivePerson;
    }

    public void setReceivePerson(String receivePerson) {
        this.receivePerson = receivePerson;
    }
}
