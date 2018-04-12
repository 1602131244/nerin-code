package com.nerin.nims.opt.glcs.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2018/2/27.
 */
public class EntryLineDto extends OracleBaseDTO {
    private Long headerId;
    private Long lineId;
    private Long lineNo;
    private String description;
    private Long ledgerId;
    private String companyCode;
    private String referenceType;
    private String reportRowId;
    private String reportItemNo;
    private double referenceDr;
    private double referenceCr;
    private double accountedDr;
    private double accountedCr;

    private String ledgerName;
    private String companyName;
    private String referenceTypeName;
    private String ledgerCompanyCode;
    private String reportRowItem;

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Long getLineNo() {
        return lineNo;
    }

    public void setLineNo(Long lineNo) {
        this.lineNo = lineNo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getReportRowId() {
        return reportRowId;
    }

    public void setReportRowId(String reportRowId) {
        this.reportRowId = reportRowId;
    }

    public String getReportItemNo() {
        return reportItemNo;
    }

    public void setReportItemNo(String reportItemNo) {
        this.reportItemNo = reportItemNo;
    }

    public double getReferenceDr() {
        return referenceDr;
    }

    public void setReferenceDr(double referenceDr) {
        this.referenceDr = referenceDr;
    }

    public double getReferenceCr() {
        return referenceCr;
    }

    public void setReferenceCr(double referenceCr) {
        this.referenceCr = referenceCr;
    }

    public double getAccountedDr() {
        return accountedDr;
    }

    public void setAccountedDr(double accountedDr) {
        this.accountedDr = accountedDr;
    }

    public double getAccountedCr() {
        return accountedCr;
    }

    public void setAccountedCr(double accountedCr) {
        this.accountedCr = accountedCr;
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

    public String getReferenceTypeName() {
        return referenceTypeName;
    }

    public void setReferenceTypeName(String referenceTypeName) {
        this.referenceTypeName = referenceTypeName;
    }

    public String getLedgerCompanyCode() {
        return ledgerCompanyCode;
    }

    public void setLedgerCompanyCode(String ledgerCompanyCode) {
        this.ledgerCompanyCode = ledgerCompanyCode;
    }

    public String getReportRowItem() {
        return reportRowItem;
    }

    public void setReportRowItem(String reportRowItem) {
        this.reportRowItem = reportRowItem;
    }
}
