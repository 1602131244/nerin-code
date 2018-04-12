package com.nerin.nims.opt.wbsp.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class DeliverableTypeDTO {
    private String dlvrCode;
    private String dlvrName;
    private String className;
    private Long classID;
    private Long dlvrID;//工作包类型ID存交付物

    public Long getClassID() {
        return classID;
    }

    public void setClassID(Long classID) {
        this.classID = classID;
    }

    public Long getDlvrID() {
        return dlvrID;
    }

    public void setDlvrID(Long dlvrID) {
        this.dlvrID = dlvrID;
    }

    public String getDlvrCode() {
        return dlvrCode;
    }

    public void setDlvrCode(String dlvrCode) {
        this.dlvrCode = dlvrCode;
    }

    public String getDlvrName() {
        return dlvrName;
    }

    public void setDlvrName(String dlvrName) {
        this.dlvrName = dlvrName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
