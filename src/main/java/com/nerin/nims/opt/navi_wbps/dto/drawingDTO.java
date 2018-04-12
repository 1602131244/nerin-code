package com.nerin.nims.opt.navi_wbps.dto;

/**
 * Created by Administrator on 2017/8/16.
 */
public class drawingDTO {
    private String tzmc;//图纸名称
    private String th;
    private String sjr;
    private String jhr;
    private String shr;
    private String sdr;
    private String zcgcs;
    private Long gzbh;
    private Long tzid;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTzmc() {
        return tzmc;
    }

    public void setTzmc(String tzmc) {
        this.tzmc = tzmc;
    }

    public String getTh() {
        return th;
    }

    public void setTh(String th) {
        this.th = th;
    }

    public String getSjr() {
        return sjr;
    }

    public void setSjr(String sjr) {
        this.sjr = sjr;
    }

    public String getJhr() {
        return jhr;
    }

    public void setJhr(String jhr) {
        this.jhr = jhr;
    }

    public String getShr() {
        return shr;
    }

    public void setShr(String shr) {
        this.shr = shr;
    }

    public String getSdr() {
        return sdr;
    }

    public void setSdr(String sdr) {
        this.sdr = sdr;
    }

    public String getZcgcs() {
        return zcgcs;
    }

    public void setZcgcs(String zcgcs) {
        this.zcgcs = zcgcs;
    }

    public Long getGzbh() {
        return gzbh;
    }

    public void setGzbh(Long gzbh) {
        this.gzbh = gzbh;
    }

    public Long getTzid() {
        return tzid;
    }

    public void setTzid(Long tzid) {
        this.tzid = tzid;
    }
}
