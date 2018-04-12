package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2016/7/11.
 */
public class ContractNumberDTO {
    private Long kHeaderId;

    public String getCognomen() {
        return cognomen;
    }

    public void setCognomen(String cognomen) {
        this.cognomen = cognomen;
    }

    public Long getkHeaderId() {
        return kHeaderId;
    }

    public void setkHeaderId(Long kHeaderId) {
        this.kHeaderId = kHeaderId;
    }

    private String cognomen;//合同简称
}
