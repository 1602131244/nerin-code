package com.nerin.nims.opt.aria.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2017/6/21.
 */
public class RateTypeDto extends OracleBaseDTO {
    private String rateType;
    private String rateTypeName;
    private String rateTypeDesc;

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getRateTypeName() {
        return rateTypeName;
    }

    public void setRateTypeName(String rateTypeName) {
        this.rateTypeName = rateTypeName;
    }

    public String getRateTypeDesc() {
        return rateTypeDesc;
    }

    public void setRateTypeDesc(String rateTypeDesc) {
        this.rateTypeDesc = rateTypeDesc;
    }
}
