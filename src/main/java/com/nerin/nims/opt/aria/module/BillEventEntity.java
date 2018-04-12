package com.nerin.nims.opt.aria.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/29.
 */
public class BillEventEntity extends OracleBaseEntity implements ORAData {
    private Long billEventId;
    private Long contHeaderId;
    private String contLineId;
    private Long deliverableId;
    private Date eventDate;
    private String eventType;
    private Long paEventId;
    private Long itemId;
    private String lineId;
    private Long chgReqId;
    private Long projectId;
    private Long taskId;
    private Long orgId;
    private String fundRef1;
    private String fundRef2;
    private String fundRef3;
    private String billOfLading;
    private String serialNum;
    private String curr;
    private String rateType;
    private Date rateDate;
    private double exchangeRate;
    private String description;
    private double  quantity;
    private double unitPrice;
    private double revenueAmount;
    private String attributeCategory;   //备用分类
    private String attribute1;  //预计收款时间
    private String attribute2; //实物发票类型
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private String attribute6;
    private String attribute7;
    private String attribute8;
    private String attribute9;
    private String attribute10;
    private String attribute11;
    private String attribute12;
    private String attribute13;
    private String attribute14;
    private String attribute15;
    private String initiatedFlag;

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

    public double getRevenueAmount() {
        return revenueAmount;
    }

    public void setRevenueAmount(double revenueAmount) {
        this.revenueAmount = revenueAmount;
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

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_OKE_K_BILLING_EVENTS_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER,  OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.DATE
            , OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR,OracleTypes.DATE, OracleTypes.NUMBER
            , OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.NUMBER
    };
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public BillEventEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getBillEventId());
        _struct.setAttribute(1, this.getContHeaderId());
        _struct.setAttribute(2, this.getContLineId());
        _struct.setAttribute(3, this.getDeliverableId());
        _struct.setAttribute(4, this.getEventDate());
        _struct.setAttribute(5, this.getEventType());
        _struct.setAttribute(6, this.getPaEventId());
        _struct.setAttribute(7, this.getItemId());
        _struct.setAttribute(8, this.getLineId());
        _struct.setAttribute(9, this.getChgReqId());
        _struct.setAttribute(10, this.getProjectId());
        _struct.setAttribute(11, this.getTaskId());
        _struct.setAttribute(12, this.getOrgId());
        _struct.setAttribute(13, this.getFundRef1());
        _struct.setAttribute(14, this.getFundRef2());
        _struct.setAttribute(15, this.getFundRef3());
        _struct.setAttribute(16, this.getBillOfLading());
        _struct.setAttribute(17, this.getSerialNum());
        _struct.setAttribute(18, this.getCurr());
        _struct.setAttribute(19, this.getRateType());
        _struct.setAttribute(20, this.getRateDate());
        _struct.setAttribute(21, this.getExchangeRate());
        _struct.setAttribute(22, this.getDescription());
        _struct.setAttribute(23, this.getQuantity());
        _struct.setAttribute(24, this.getUnitPrice());
        _struct.setAttribute(25, this.getRevenueAmount());
        _struct.setAttribute(26, this.getAttributeCategory());
        _struct.setAttribute(27, this.getAttribute1());
        _struct.setAttribute(28, this.getAttribute2());
        _struct.setAttribute(29, this.getAttribute3());
        _struct.setAttribute(30, this.getAttribute4());
        _struct.setAttribute(31, this.getAttribute5());
        _struct.setAttribute(32, this.getAttribute6());
        _struct.setAttribute(33, this.getAttribute7());
        _struct.setAttribute(34, this.getAttribute8());
        _struct.setAttribute(35, this.getAttribute9());
        _struct.setAttribute(36, this.getAttribute10());
        _struct.setAttribute(37, this.getAttribute11());
        _struct.setAttribute(38, this.getAttribute12());
        _struct.setAttribute(39, this.getAttribute13());
        _struct.setAttribute(40, this.getAttribute14());
        _struct.setAttribute(41, this.getAttribute15());
        _struct.setAttribute(42, this.getInitiatedFlag());
        _struct.setAttribute(43, this.getCreationDate());
        _struct.setAttribute(44, this.getCreatedBy());
        _struct.setAttribute(45, this.getLastUpdateDate());
        _struct.setAttribute(46, this.getLastUpdatedBy());
        _struct.setAttribute(47, this.getLastUpdateLogin());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
