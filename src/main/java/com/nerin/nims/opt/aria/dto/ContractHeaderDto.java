package com.nerin.nims.opt.aria.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2017/6/20.
 */
public class ContractHeaderDto extends OracleBaseDTO {
    private Long contHeaderId;
    private String contNumber;
    private String contName;
    private String contLongName;
    private String contTypeCode;
    private String contType;
    private String contStatusCode;
    private String contStatus;
    private Long orgId;
    private String orgName;
    private String curr;
    private double amount;
    private String amountDis;
    private Long headerProjectID;
    private String headerPorjectName;
    private String headerProjectNumber;
    private String lineProjects;
    private String contCust;

    public Long getContHeaderId() {
        return contHeaderId;
    }

    public void setContHeaderId(Long contHeaderId) {
        this.contHeaderId = contHeaderId;
    }

    public String getContNumber() {
        return contNumber;
    }

    public void setContNumber(String contNumber) {
        this.contNumber = contNumber;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public String getContLongName() {
        return contLongName;
    }

    public void setContLongName(String contLongName) {
        this.contLongName = contLongName;
    }

    public String getContTypeCode() {
        return contTypeCode;
    }

    public void setContTypeCode(String contTypeCode) {
        this.contTypeCode = contTypeCode;
    }

    public String getContType() {
        return contType;
    }

    public void setContType(String contType) {
        this.contType = contType;
    }

    public String getContStatusCode() {
        return contStatusCode;
    }

    public void setContStatusCode(String contStatusCode) {
        this.contStatusCode = contStatusCode;
    }

    public String getContStatus() {
        return contStatus;
    }

    public void setContStatus(String contStatus) {
        this.contStatus = contStatus;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getCurr() {
        return curr;
    }

    public void setCurr(String curr) {
        this.curr = curr;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmountDis() {
        return amountDis;
    }

    public void setAmountDis(String amountDis) {
        this.amountDis = amountDis;
    }

    public Long getHeaderProjectID() {
        return headerProjectID;
    }

    public void setHeaderProjectID(Long headerProjectID) {
        this.headerProjectID = headerProjectID;
    }

    public String getHeaderPorjectName() {
        return headerPorjectName;
    }

    public void setHeaderPorjectName(String headerPorjectName) {
        this.headerPorjectName = headerPorjectName;
    }

    public String getHeaderProjectNumber() {
        return headerProjectNumber;
    }

    public void setHeaderProjectNumber(String headerProjectNumber) {
        this.headerProjectNumber = headerProjectNumber;
    }

    public String getLineProjects() {
        return lineProjects;
    }

    public void setLineProjects(String lineProjects) {
        this.lineProjects = lineProjects;
    }

    public String getContCust() {
        return contCust;
    }

    public void setContCust(String contCust) {
        this.contCust = contCust;
    }
}
