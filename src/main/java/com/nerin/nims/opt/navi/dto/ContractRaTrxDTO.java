package com.nerin.nims.opt.navi.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/11.
 */
public class ContractRaTrxDTO {

    private String trxNumber;
    private Date trxDate;
    private String invCURR;
    private Double amount;
    private Double amountCNY;
    private String projNumber;
    private String custName;
    private String  description;


    public String getInvCURR() {
        return invCURR;
    }

    public void setInvCURR(String invCURR) {
        this.invCURR = invCURR;
    }

    public Double getAmountCNY() {
        return amountCNY;
    }

    public void setAmountCNY(Double amountCNY) {
        this.amountCNY = amountCNY;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrxNumber() {
        return trxNumber;
    }

    public void setTrxNumber(String trxNumber) {
        this.trxNumber = trxNumber;
    }

    public Date getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(Date trxDate) {
        this.trxDate = trxDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
