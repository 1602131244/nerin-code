package com.nerin.nims.opt.glcs.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/10/19.
 */
public class UserAssignmentEntity extends OracleBaseEntity implements ORAData {

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

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_GLCS_USER_ASSIGNMENT_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.DATE
    };
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public UserAssignmentEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getAssignmentId());
        _struct.setAttribute(1, this.getUserId());
        _struct.setAttribute(2, this.getRangeId());
        _struct.setAttribute(3, this.getRangeVerson());
        _struct.setAttribute(4, this.getUnitId());
        _struct.setAttribute(5, this.getLedgerId());
        _struct.setAttribute(6, this.getCompanyCode());
        _struct.setAttribute(7, this.getReportId());
        _struct.setAttribute(8, this.getSelectFlag());
        _struct.setAttribute(9, this.getInsertFlag());
        _struct.setAttribute(10, this.getUpdateFlag());
        _struct.setAttribute(11, this.getDeleteFlag());
        _struct.setAttribute(12, this.getReportFlag());
        _struct.setAttribute(13, this.getApprovalFlag());
        _struct.setAttribute(14, this.getReceiveFlag());
        _struct.setAttribute(15, this.getCreatedBy());
        _struct.setAttribute(16, this.getCreationDate());
        _struct.setAttribute(17, this.getLastUpdatedBy());
        _struct.setAttribute(18, this.getLastUpdateDate());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
