package com.nerin.nims.opt.navi_wbps.dto;

/**
 * Created by Administrator on 2017/8/29.
 */
public class printCreateDocDTO {
    private long requestId;
    private String plancnDrawingNum;
    private String plandrawingNum;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getPlancnDrawingNum() {
        return plancnDrawingNum;
    }

    public void setPlancnDrawingNum(String plancnDrawingNum) {
        this.plancnDrawingNum = plancnDrawingNum;
    }

    public String getPlandrawingNum() {
        return plandrawingNum;
    }

    public void setPlandrawingNum(String plandrawingNum) {
        this.plandrawingNum = plandrawingNum;
    }
}
