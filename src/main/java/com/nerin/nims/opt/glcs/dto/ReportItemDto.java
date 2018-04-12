package com.nerin.nims.opt.glcs.dto;

/**
 * Created by Administrator on 2018/2/28.
 */
public class ReportItemDto {
    private Long applcationId;
    private Long reportRowId;
    private String itemNo;
    private String itemName;
    private String changeFlag;

    public Long getApplcationId() {
        return applcationId;
    }

    public void setApplcationId(Long applcationId) {
        this.applcationId = applcationId;
    }

    public Long getReportRowId() {
        return reportRowId;
    }

    public void setReportRowId(Long reportRowId) {
        this.reportRowId = reportRowId;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(String changeFlag) {
        this.changeFlag = changeFlag;
    }
}
