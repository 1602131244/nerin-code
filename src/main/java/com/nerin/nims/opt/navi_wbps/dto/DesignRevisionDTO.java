package com.nerin.nims.opt.navi_wbps.dto;


import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/16.
 */
public class DesignRevisionDTO {
    private String xmjl;
    private String zyfzr;
    private String xmbh;
    private String xmmc;
    private String sjjd;
    private String zxmc;
    private String zxh;
    private String zy;
    private String bgyyOA;
    private String zysx;
    private String bgyyJsp;
    private String subName;
    private String drawsName;
    private String drawsNum;
    private String editDoc;
    private String viewDoc;
    private String getPdf;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBgyyOA() {
        return bgyyOA;
    }

    public void setBgyyOA(String bgyyOA) {
        this.bgyyOA = bgyyOA;
    }

    public String getZysx() {
        return zysx;
    }

    public void setZysx(String zysx) {
        this.zysx = zysx;
    }

    public String getBgyyJsp() {
        return bgyyJsp;
    }

    public void setBgyyJsp(String bgyyJsp) {
        this.bgyyJsp = bgyyJsp;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getDrawsName() {
        return drawsName;
    }

    public void setDrawsName(String drawsName) {
        this.drawsName = drawsName;
    }

    public String getDrawsNum() {
        return drawsNum;
    }

    public void setDrawsNum(String drawsNum) {
        this.drawsNum = drawsNum;
    }

    public String getEditDoc() {
        return editDoc;
    }

    public void setEditDoc(String editDoc) {
        this.editDoc = editDoc;
    }

    public String getViewDoc() {
        return viewDoc;
    }

    public void setViewDoc(String viewDoc) {
        this.viewDoc = viewDoc;
    }

    public String getGetPdf() {
        return getPdf;
    }

    public void setGetPdf(String getPdf) {
        this.getPdf = getPdf;
    }

    private ArrayList<drawingDTO> detail;

    public String getXmjl() {
        return xmjl;
    }

    public void setXmjl(String xmjl) {
        this.xmjl = xmjl;
    }

    public String getZyfzr() {
        return zyfzr;
    }

    public void setZyfzr(String zyfzr) {
        this.zyfzr = zyfzr;
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

    public String getSjjd() {
        return sjjd;
    }

    public void setSjjd(String sjjd) {
        this.sjjd = sjjd;
    }

    public String getZxmc() {
        return zxmc;
    }

    public void setZxmc(String zxmc) {
        this.zxmc = zxmc;
    }

    public String getZxh() {
        return zxh;
    }

    public void setZxh(String zxh) {
        this.zxh = zxh;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public ArrayList<drawingDTO> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<drawingDTO> detail) {
        this.detail = detail;
    }
}
