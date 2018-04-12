package com.nerin.nims.opt.glcs.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2017/10/16.
 */
public class LedgerDto extends OracleBaseDTO {
    private Long ledgerId;
    private String ledgerName;
    private String ledgerShortName;
    private String ledgerdescrition;
    private String ledgerType;

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getLedgerName() {
        return ledgerName;
    }

    public void setLedgerName(String ledgerName) {
        this.ledgerName = ledgerName;
    }

    public String getLedgerShortName() {
        return ledgerShortName;
    }

    public void setLedgerShortName(String ledgerShortName) {
        this.ledgerShortName = ledgerShortName;
    }

    public String getLedgerdescrition() {
        return ledgerdescrition;
    }

    public void setLedgerdescrition(String ledgerdescrition) {
        this.ledgerdescrition = ledgerdescrition;
    }

    public String getLedgerType() {
        return ledgerType;
    }

    public void setLedgerType(String ledgerType) {
        this.ledgerType = ledgerType;
    }
}
