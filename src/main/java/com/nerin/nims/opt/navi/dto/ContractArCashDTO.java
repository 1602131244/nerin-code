package com.nerin.nims.opt.navi.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/11.
 */
public class ContractArCashDTO {
    private String receiptNumber;
    private Date glDate;
    private String recCurr ;//币种
    private Double recAmount;//原币
    private Double recAmountCNY;//人民币
    private String projNumber;
    private String custName;
    private String  description;

    public String getRecCurr() {
        return recCurr;
    }

    public void setRecCurr(String recCurr) {
        this.recCurr = recCurr;
    }

    public Double getRecAmountCNY() {
        return recAmountCNY;
    }

    public void setRecAmountCNY(Double recAmountCNY) {
        this.recAmountCNY = recAmountCNY;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjNumber() {
        return projNumber;
    }

    public void setProjNumber(String projNumber) {
        this.projNumber = projNumber;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public Date getGlDate() {
        return glDate;
    }

    public void setGlDate(Date glDate) {
        this.glDate = glDate;
    }

    public Double getRecAmount() {
        return recAmount;
    }

    public void setRecAmount(Double recAmount) {
        this.recAmount = recAmount;
    }
}
