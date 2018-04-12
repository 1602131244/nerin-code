package com.nerin.nims.opt.glcs.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2017/10/19.
 */
public class UserAssignmentDto extends OracleBaseDTO {
    private Long assignmentId;
    private Long userId;
    private Long rangeId;
    private Long rangeVerson;
    private Long unitId;
    private Long ledgerId;
    private String companyCode;
    private Long reportId;
    private String selectFlag;       //查询权限  1 表示有权限 0表示无权限
    private String insertFlag;      //新增权限  1 表示有权限 0表示无权限
    private String updateFlag;      //修改权限  1 表示有权限 0表示无权限
    private String deleteFlag;      //删除权限  1 表示有权限 0表示无权限
    private String reportFlag;      //上报权限  1 表示有权限 0表示无权限
    private String approvalFlag;       //审批权限 1 表示有权限 0表示无权限
    private String receiveFlag;       //接收权限 1 表示有权限 0表示无权限
    private String rangeName;       //合并范围名称
    private String unitName;       //合并单元名称
    private String ledgerName;       //分类账名称
    private String companyName;       //公司名称
    private String reportName;       //报表名称
    private String userNumber;
    private String userName;
    private String createByName;
    private String lastByName;

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRangeId() {
        return rangeId;
    }

    public void setRangeId(Long rangeId) {
        this.rangeId = rangeId;
    }

    public Long getRangeVerson() {
        return rangeVerson;
    }

    public void setRangeVerson(Long rangeVerson) {
        this.rangeVerson = rangeVerson;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getSelectFlag() {
        return selectFlag;
    }

    public void setSelectFlag(String selectFlag) {
        this.selectFlag = selectFlag;
    }

    public String getInsertFlag() {
        return insertFlag;
    }

    public void setInsertFlag(String insertFlag) {
        this.insertFlag = insertFlag;
    }

    public String getUpdateFlag() {
        return updateFlag;
    }

    public void setUpdateFlag(String updateFlag) {
        this.updateFlag = updateFlag;
    }

    public String getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(String deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getReportFlag() {
        return reportFlag;
    }

    public void setReportFlag(String reportFlag) {
        this.reportFlag = reportFlag;
    }

    public String getApprovalFlag() {
        return approvalFlag;
    }

    public void setApprovalFlag(String approvalFlag) {
        this.approvalFlag = approvalFlag;
    }

    public String getReceiveFlag() {
        return receiveFlag;
    }

    public void setReceiveFlag(String receiveFlag) {
        this.receiveFlag = receiveFlag;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
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

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getLastByName() {
        return lastByName;
    }

    public void setLastByName(String lastByName) {
        this.lastByName = lastByName;
    }
}
