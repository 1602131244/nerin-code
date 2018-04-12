package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2016/8/11.
 */
public class ProjectTextOaDTO {
    private Long requestId;//number:OA请求ID
    private String requestName;//varchar2：OA名称
    private String status;//varchar2：状态
    private String createDate;//varchar2：创建时间
    private String createBy;//varchar2：创建人
    private String currentPerson;//varchar2：当前处理人

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(String currentPerson) {
        this.currentPerson = currentPerson;
    }
}
