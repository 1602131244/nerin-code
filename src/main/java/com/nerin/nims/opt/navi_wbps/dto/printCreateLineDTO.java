package com.nerin.nims.opt.navi_wbps.dto;

/**
 * Created by Administrator on 2017/8/28.
 */
public class printCreateLineDTO {
    //图纸文印流程发起，行DTO
    private String DivEquip;
    private String drawnums;
    private String drawcount;
    private String xSize;
    private String plancnDrawingNum;
    private String plandrawingNum;
    private String specialityName;

    public String getDivEquip() {
        return DivEquip;
    }

    public void setDivEquip(String divEquip) {
        DivEquip = divEquip;
    }

    public String getDrawnums() {
        return drawnums;
    }

    public void setDrawnums(String drawnums) {
        this.drawnums = drawnums;
    }

    public String getDrawcount() {
        return drawcount;
    }

    public void setDrawcount(String drawcount) {
        this.drawcount = drawcount;
    }

    public String getxSize() {
        return xSize;
    }

    public void setxSize(String xSize) {
        this.xSize = xSize;
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

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }
}
