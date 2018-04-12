package com.nerin.nims.opt.navi.dto;

import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;

/**
 * Created by Administrator on 2017/2/22.
 */
public class YBLDataTableDTO extends DataTablesDTO {
    private Long headId;
    private String bt;
    private String yxqk;
    private String sfqk;
    private String isrequired;//是否必填
    private String message;

    public String getIsrequired() {
        return isrequired;
    }

    public void setIsrequired(String isrequired) {
        this.isrequired = isrequired;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getHeadId() {
        return headId;
    }

    public void setHeadId(Long headId) {
        this.headId = headId;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getYxqk() {
        return yxqk;
    }

    public void setYxqk(String yxqk) {
        this.yxqk = yxqk;
    }

    public String getSfqk() {
        return sfqk;
    }

    public void setSfqk(String sfqk) {
        this.sfqk = sfqk;
    }
}
