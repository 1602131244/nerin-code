package com.nerin.nims.opt.glcs.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Administrator on 2017/10/30.
 */
public class ReportHeaderEntity extends OracleBaseEntity implements ORAData {

    private Long headerId;
    private String periodName;
    private String description;
    private Long rangeId;
    private Long rangeVerson;
    private Long unitId;
    private Long ledgerId;
    private String companyCode;
    private Long reportId;
    private String type;
    private String reportStatus;
    private String submitStatus;
    private Long createBy;
    private Date createDate;
    private Long lastBy;
    private Date lastDate;
    private Long approvalBy;
    private Date approvalDate;
    private Long submitBy;
    private Date submitDate;
    private String postingFlag;

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(String reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getSubmitStatus() {
        return submitStatus;
    }

    public void setSubmitStatus(String submitStatus) {
        this.submitStatus = submitStatus;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getLastBy() {
        return lastBy;
    }

    public void setLastBy(Long lastBy) {
        this.lastBy = lastBy;
    }

    public Date getLastDate() {
        return lastDate;
    }

    public void setLastDate(Date lastDate) {
        this.lastDate = lastDate;
    }

    public Long getApprovalBy() {
        return approvalBy;
    }

    public void setApprovalBy(Long approvalBy) {
        this.approvalBy = approvalBy;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public Long getSubmitBy() {
        return submitBy;
    }

    public void setSubmitBy(Long submitBy) {
        this.submitBy = submitBy;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public String getPostingFlag() {
        return postingFlag;
    }

    public void setPostingFlag(String postingFlag) {
        this.postingFlag = postingFlag;
    }

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_GLCS_REPORT_HEADER_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.NUMBER
            , OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.DATE
            , OracleTypes.VARCHAR
    };
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public ReportHeaderEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getHeaderId());
        _struct.setAttribute(1, this.getPeriodName());
        _struct.setAttribute(2, this.getDescription());
        _struct.setAttribute(3, this.getRangeId());
        _struct.setAttribute(4, this.getRangeVerson());
        _struct.setAttribute(5, this.getUnitId());
        _struct.setAttribute(6, this.getLedgerId());
        _struct.setAttribute(7, this.getCompanyCode());
        _struct.setAttribute(8, this.getReportId());
        _struct.setAttribute(9, this.getType());
        _struct.setAttribute(10, this.getReportStatus());
        _struct.setAttribute(11, this.getSubmitStatus());
        _struct.setAttribute(12, this.getCreateBy());
        _struct.setAttribute(13, this.getCreateDate());
        _struct.setAttribute(14, this.getLastBy());
        _struct.setAttribute(15, this.getLastDate());
        _struct.setAttribute(16, this.getApprovalBy());
        _struct.setAttribute(17, this.getApprovalDate());
        _struct.setAttribute(18, this.getSubmitBy());
        _struct.setAttribute(19, this.getSubmitDate());
        _struct.setAttribute(20, this.getPostingFlag());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
