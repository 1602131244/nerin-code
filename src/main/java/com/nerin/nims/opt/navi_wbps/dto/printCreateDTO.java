package com.nerin.nims.opt.navi_wbps.dto;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/28.
 */
public class printCreateDTO {
    private String xmbh;
    private String xmmc;
    private String xmjl;
    private String xmfzr;
    private String zyfzr;
    private String xmmsx;
    private String wynr;
    private String zipurl;
    private String zy;
    private String fgld;
    private String zgfzg;
    private long pltOrder;


    private ArrayList<printCreateLineDTO> detail;
    private ArrayList<printCreateDocDTO> docList;

    public long getPltOrder() {
        return pltOrder;
    }

    public void setPltOrder(long pltOrder) {
        this.pltOrder = pltOrder;
    }

    public String getZgfzg() {
        return zgfzg;
    }

    public void setZgfzg(String zgfzg) {
        this.zgfzg = zgfzg;
    }

    public ArrayList<printCreateDocDTO> getDocList() {
        return docList;
    }

    public void setDocList(ArrayList<printCreateDocDTO> docList) {
        this.docList = docList;
    }

    public String getXmbh() {
        return xmbh;
    }

    public void setXmbh(String xmbh) {
        this.xmbh = xmbh;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getXmjl() {
        return xmjl;
    }

    public void setXmjl(String xmjl) {
        this.xmjl = xmjl;
    }

    public String getXmfzr() {
        return xmfzr;
    }

    public void setXmfzr(String xmfzr) {
        this.xmfzr = xmfzr;
    }

    public String getZyfzr() {
        return zyfzr;
    }

    public void setZyfzr(String zyfzr) {
        this.zyfzr = zyfzr;
    }

    public String getXmmsx() {
        return xmmsx;
    }

    public void setXmmsx(String xmmsx) {
        this.xmmsx = xmmsx;
    }

    public String getWynr() {
        return wynr;
    }

    public void setWynr(String wynr) {
        this.wynr = wynr;
    }

    public String getZipurl() {
        return zipurl;
    }

    public void setZipurl(String zipurl) {
        this.zipurl = zipurl;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getFgld() {
        return fgld;
    }

    public void setFgld(String fgld) {
        this.fgld = fgld;
    }

    public ArrayList<printCreateLineDTO> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<printCreateLineDTO> detail) {
        this.detail = detail;
    }
}
