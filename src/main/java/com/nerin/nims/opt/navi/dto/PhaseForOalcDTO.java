package com.nerin.nims.opt.navi.dto;

/**
 * Created by yinglgu on 7/30/2016.
 */
public class PhaseForOalcDTO {
    private Long requestId;//number:OA请求ID
    private String requestName;//varchar2：OA名称
    private String status;//varchar2：状态
    private String createDate;//varchar2：创建时间
    private String createBy;//varchar2：创建人
    private String currentPerson;//varchar2：当前处理人
    private String workflowname;
   private String lastname;
    private String taskName;//varchar2:文本名称
    private String zy;//varchar2 专业
    private String conditionNumber;//varchar2:条件单编号
    private String createtime;
    private String approver;

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getZy() {
        return zy;
    }
    public void setZy(String zy) {
        this.zy = zy;
    }
    public String getConditionNumber() {
        return conditionNumber;
    }

    public String getWorkflowname() {
        return workflowname;
    }

    public void setWorkflowname(String workflowname) {
        this.workflowname = workflowname;
    }

    public void setConditionNumber(String conditionNumber) {
        this.conditionNumber = conditionNumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

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
