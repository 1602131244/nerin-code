package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2016/7/11.
 */
public class ContractApHeaderDTO {
    private Long kheaderId;
    private Long headerId;  //合同头ID
    private String contractNumber;//合同编号
    private Double total;//合同总额
    private String currencyCode;//币种
    private Long version;//版本
    private String status;//状态
    private String amount_flag;//是否确认金额
    private Double getAmount;//累计收入确认金额
    private Double getPercent;//累计收入确认百分比

    public Double getGetAmount() {
        return getAmount;
    }

    public void setGetAmount(Double getAmount) {
        this.getAmount = getAmount;
    }

    public Double getGetPercent() {
        return getPercent;
    }

    public void setGetPercent(Double getPercent) {
        this.getPercent = getPercent;
    }

    public Long getKheaderId() {
        return kheaderId;
    }

    public void setKheaderId(Long kheaderId) {
        this.kheaderId = kheaderId;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount_flag() {
        return amount_flag;
    }

    public void setAmount_flag(String amount_flag) {
        this.amount_flag = amount_flag;
    }
}
