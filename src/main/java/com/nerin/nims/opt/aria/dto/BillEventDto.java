package com.nerin.nims.opt.aria.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/21.
 */
public class BillEventDto extends OracleBaseDTO {
    private Long billEventId;    //开票申请ID
    private Long contHeaderId;  //合同头ID
    private String contLineId;  //合同行ID
    private Long deliverableId; //合同市场活动物品ID
    private Date eventDate;    //开票申请日期
    private String eventType;  //开票申请类型
    private Long paEventId;     //开票申请对应项目开票事物处理ID
    private Long itemId;     //
    private String lineId;   //行ID
    private Long chgReqId;
    private Long projectId;   //项目ID
    private Long taskId;     //项目任务ID
    private Long orgId;     //组织ID
    private String fundRef1;
    private String fundRef2;
    private String fundRef3;
    private String billOfLading;
    private String serialNum;
    private String curr;       //币别
    private String rateType;   //汇率类型
    private Date rateDate;   //汇率日期
    private double exchangeRate;   //汇率
    private String description;   //开票说明
    private double  quantity;    //数量
    private double unitPrice;    //单间
    private double revenueAmount;  //金额
    private String attributeCategory;   //备用分类
    private String attribute1;  //预计收款时间
    private String attribute2; //实物发票类型
    private String attribute3;  // 开票申请标识
    private String attribute4;
    private String attribute5;
    private String attribute6;
    private String attribute7;
    private String attribute8;  //开票客户
    private String attribute9;  //发票签收日期
    private String attribute10; //发票收入日期
    private String attribute11;
    private String attribute12;
    private String attribute13;
    private String attribute14;
    private String attribute15;
    private String initiatedFlag;

    private String billEventNumber;
    private String lineNumber;
    private String lineDescription;
    private String lineStyle;
    private String projectName;
    private String projectNumber;
    private String taskName;
    private String processedFlag;
    private String amount;
    private String deliverablePrice;
    private String state;
    private String custName;

    public Long getBillEventId() {
        return billEventId;
    }

    public void setBillEventId(Long billEventId) {
        this.billEventId = billEventId;
    }

    public Long getContHeaderId() {
        return contHeaderId;
    }

    public void setContHeaderId(Long contHeaderId) {
        this.contHeaderId = contHeaderId;
    }

    public String getContLineId() {
        return contLineId;
    }

    public void setContLineId(String contLineId) {
        this.contLineId = contLineId;
    }

    public Long getDeliverableId() {
        return deliverableId;
    }

    public void setDeliverableId(Long deliverableId) {
        this.deliverableId = deliverableId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getPaEventId() {
        return paEventId;
    }

    public void setPaEventId(Long paEventId) {
        this.paEventId = paEventId;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public Long getChgReqId() {
        return chgReqId;
    }

    public void setChgReqId(Long chgReqId) {
        this.chgReqId = chgReqId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getFundRef1() {
        return fundRef1;
    }

    public void setFundRef1(String fundRef1) {
        this.fundRef1 = fundRef1;
    }

    public String getFundRef2() {
        return fundRef2;
    }

    public void setFundRef2(String fundRef2) {
        this.fundRef2 = fundRef2;
    }

    public String getFundRef3() {
        return fundRef3;
    }

    public void setFundRef3(String fundRef3) {
        this.fundRef3 = fundRef3;
    }

    public String getBillOfLading() {
        return billOfLading;
    }

    public void setBillOfLading(String billOfLading) {
        this.billOfLading = billOfLading;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum;
    }

    public String getCurr() {
        return curr;
    }

    public void setCurr(String curr) {
        this.curr = curr;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public Date getRateDate() {
        return rateDate;
    }

    public void setRateDate(Date rateDate) {
        this.rateDate = rateDate;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    public String getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }

    public String getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }

    public String getAttribute11() {
        return attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }

    public String getAttribute12() {
        return attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }

    public String getAttribute13() {
        return attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }

    public String getAttribute14() {
        return attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }

    public String getAttribute15() {
        return attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
    }

    public String getInitiatedFlag() {
        return initiatedFlag;
    }

    public void setInitiatedFlag(String initiatedFlag) {
        this.initiatedFlag = initiatedFlag;
    }

    public String getBillEventNumber() {
        return billEventNumber;
    }

    public void setBillEventNumber(String billEventNumber) {
        this.billEventNumber = billEventNumber;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProcessedFlag() {
        return processedFlag;
    }

    public void setProcessedFlag(String processedFlag) {
        this.processedFlag = processedFlag;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public double getRevenueAmount() {
        return revenueAmount;
    }

    public void setRevenueAmount(double revenueAmount) {
        this.revenueAmount = revenueAmount;
    }

    public String getDeliverablePrice() {
        return deliverablePrice;
    }

    public void setDeliverablePrice(String deliverablePrice) {
        this.deliverablePrice = deliverablePrice;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLineDescription() {
        return lineDescription;
    }

    public void setLineDescription(String lineDescription) {
        this.lineDescription = lineDescription;
    }

    public String getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(String lineStyle) {
        this.lineStyle = lineStyle;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
}
