package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2017/8/25.
 */
public class ContractApLineNumDTO {
    private String lineNumber;
    private String lineStyle;
    private Long lseId;
    private Long lineValue;
    private String key;

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(String lineStyle) {
        this.lineStyle = lineStyle;
    }

    public Long getLseId() {
        return lseId;
    }

    public void setLseId(Long lseId) {
        this.lseId = lseId;
    }

    public Long getLineValue() {
        return lineValue;
    }

    public void setLineValue(Long lineValue) {
        this.lineValue = lineValue;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
