package com.nerin.nims.opt.navi.dto;

/**
 * Created by yinglgu on 7/30/2016.
 */
public class PhaseForZykgbgDTO {
    private Long taskHeaderId;//number:文本ID
    private String specialityName;//varchar2:专业
    private String specialityCode;//varchar2:专业code
    private String bt;//varchar2：流程名称
    private String status;//varchar2：状态
    private String createId;//varchar2：创建人id
    private String createdName;//varchar2：创建人
    private String createDate;//varchar2:创建时间
    private String lastOperateDate;//办结时间
    private long requestId;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getSpecialityCode() {
        return specialityCode;
    }

    public void setSpecialityCode(String specialityCode) {
        this.specialityCode = specialityCode;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public Long getTaskHeaderId() {
        return taskHeaderId;
    }

    public void setTaskHeaderId(Long taskHeaderId) {
        this.taskHeaderId = taskHeaderId;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreatedName() {
        return createdName;
    }

    public void setCreatedName(String createdName) {
        this.createdName = createdName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getLastOperateDate() {
        return lastOperateDate;
    }

    public void setLastOperateDate(String lastOperateDate) {
        this.lastOperateDate = lastOperateDate;
    }
}
