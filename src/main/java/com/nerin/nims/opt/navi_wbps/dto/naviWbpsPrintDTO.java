package com.nerin.nims.opt.navi_wbps.dto;

/**
 * Created by Administrator on 2017/8/2.
 */
public class naviWbpsPrintDTO {
    private Long requestId;
    private String requestName;
    private String major;
    private String createBy;
    private String createDate;
    private String status;
    private String currentPerson;
    private String wydbh;//文印单编号
    private String wynr;//文印内容
    private String xmmc;
    private String cyly;
    private String xgnr;
    private String sjjd;
    private String rwlx;
    private String pltStatus;
    private String zgfzg;
    private long pltorder;

    public long getPltorder() {
        return pltorder;
    }

    public void setPltorder(long pltorder) {
        this.pltorder = pltorder;
    }

    public String getZgfzg() {
        return zgfzg;
    }

    public void setZgfzg(String zgfzg) {
        this.zgfzg = zgfzg;
    }

    public String getSjjd() {
        return sjjd;
    }

    public void setSjjd(String sjjd) {
        this.sjjd = sjjd;
    }

    public String getRwlx() {
        return rwlx;
    }

    public void setRwlx(String rwlx) {
        this.rwlx = rwlx;
    }


    public String getPltStatus() {
        return pltStatus;
    }

    public void setPltStatus(String pltStatus) {
        this.pltStatus = pltStatus;
    }

    public String getCyly() {
        return cyly;
    }

    public void setCyly(String cyly) {
        this.cyly = cyly;
    }

    public String getXgnr() {
        return xgnr;
    }

    public void setXgnr(String xgnr) {
        this.xgnr = xgnr;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(String currentPerson) {
        this.currentPerson = currentPerson;
    }

    public String getWydbh() {
        return wydbh;
    }

    public void setWydbh(String wydbh) {
        this.wydbh = wydbh;
    }

    public String getWynr() {
        return wynr;
    }

    public void setWynr(String wynr) {
        this.wynr = wynr;
    }
}
