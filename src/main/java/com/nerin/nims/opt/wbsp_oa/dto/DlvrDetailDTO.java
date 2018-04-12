package com.nerin.nims.opt.wbsp_oa.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class DlvrDetailDTO {
    private String zxhmx;//子项号明细,
    private String divEquip; //子项设备名称,
    private String drawNumAbbr; //图号,
    private Long drawCount;//自然张数,
    private Long dlvrId;//工作包号,
    private String dlvrName; //工作包名称,
    private String descr; //说明

    public String getZxhmx() {
        return zxhmx;
    }

    public void setZxhmx(String zxhmx) {
        this.zxhmx = zxhmx;
    }

    public String getDivEquip() {
        return divEquip;
    }

    public void setDivEquip(String divEquip) {
        this.divEquip = divEquip;
    }

    public String getDrawNumAbbr() {
        return drawNumAbbr;
    }

    public void setDrawNumAbbr(String drawNumAbbr) {
        this.drawNumAbbr = drawNumAbbr;
    }

    public Long getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(Long drawCount) {
        this.drawCount = drawCount;
    }

    public Long getDlvrId() {
        return dlvrId;
    }

    public void setDlvrId(Long dlvrId) {
        this.dlvrId = dlvrId;
    }

    public String getDlvrName() {
        return dlvrName;
    }

    public void setDlvrName(String dlvrName) {
        this.dlvrName = dlvrName;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
