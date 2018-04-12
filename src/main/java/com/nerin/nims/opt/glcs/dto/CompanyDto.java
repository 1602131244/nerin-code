package com.nerin.nims.opt.glcs.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2017/9/19.
 */
public class CompanyDto extends OracleBaseDTO {
    private Long rangeId;
    private Long rangeVerson;
    private Long unitId;
    private String companyName;
    private String description;
    private Long ledgerId;
    private String companyCode;
    private String createByName;
    private String lastByName;
    private String ledgerNameList;
    private String companyNameList;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getLedgerNameList() {
        return ledgerNameList;
    }

    public void setLedgerNameList(String ledgerNameList) {
        this.ledgerNameList = ledgerNameList;
    }

    public String getCompanyNameList() {
        return companyNameList;
    }

    public void setCompanyNameList(String companyNameList) {
        this.companyNameList = companyNameList;
    }
}
