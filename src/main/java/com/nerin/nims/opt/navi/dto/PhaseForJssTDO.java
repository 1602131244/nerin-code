package com.nerin.nims.opt.navi.dto;

/**
 * Created by yinglgu on 8/9/2016.
 */
public class PhaseForJssTDO {
    private String jssbh;//varchar2:计算书编号
    private String jssm;//varchar2：计算书名
    private String requestName;//varchar2：流程名称
    private String status;//varchar2：状态
    private String createDate;//varchar2：创建时间
    private Long requestId;//number：流程id
    private String creater;//varchar2：创建人
    private Long calcuId;//varchar2：计算书ID
    private String calcuNumber;//varchar2：计算书编号
    private String currentPerson;//varchar2：当前审批人
    public Long getCalcuId() {
        return calcuId;
    }

    public void setCalcuId(Long calcuId) {
        this.calcuId = calcuId;
    }

    public String getCalcuNumber() {
        return calcuNumber;
    }

    public void setCalcuNumber(String calcuNumber) {
        this.calcuNumber = calcuNumber;
    }

    public String getJssbh() {
        return jssbh;
    }

    public void setJssbh(String jssbh) {
        this.jssbh = jssbh;
    }

    public String getJssm() {
        return jssm;
    }

    public void setJssm(String jssm) {
        this.jssm = jssm;
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
