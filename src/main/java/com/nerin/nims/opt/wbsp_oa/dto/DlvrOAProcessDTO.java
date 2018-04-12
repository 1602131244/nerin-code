package com.nerin.nims.opt.wbsp_oa.dto;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class DlvrOAProcessDTO {
    private String jsnr;//varchar2：校审内容
    private String requestName;//varchar2：流程名称
    private String status;//varchar2：状态
    private String createDate;//varchar2：创建时间
    private Long requestId;//number：流程id
    private String creater;//varchar2：创建人
    private String currentPerson;//varchar2：当前审批人

    public String getJsnr() {
        return jsnr;
    }

    public void setJsnr(String jsnr) {
        this.jsnr = jsnr;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(String currentPerson) {
        this.currentPerson = currentPerson;
    }
}
