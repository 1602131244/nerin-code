package com.nerin.nims.opt.aria.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2017/11/14.
 */
public class ContCustDto extends OracleBaseDTO {
    private Long custId;
    private String custName;
    private String custNumber;
    private String custStatus;

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustNumber() {
        return custNumber;
    }

    public void setCustNumber(String custNumber) {
        this.custNumber = custNumber;
    }

    public String getCustStatus() {
        return custStatus;
    }

    public void setCustStatus(String custStatus) {
        this.custStatus = custStatus;
    }
}
