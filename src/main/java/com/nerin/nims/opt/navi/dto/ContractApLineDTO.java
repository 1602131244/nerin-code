package com.nerin.nims.opt.navi.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;
import oracle.sql.DATE;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/11.
 */
public class ContractApLineDTO extends OracleBaseDTO {
    private Long headerId;
    private Long lineId; //行ID
    private Long lineNumber;//行序号
    private String lineStyle;//费用项
    private String lineAmount;//行金额
    private Long kheaderId;//合同ID
    private Long klineId;//合同行ID
    private String styleDesc;//费用项说明
    private Long lseId;//费用项ID
    private String rcvTypeMeaning;//款项
    private String rcvType;//款项ID
    private Date planapDate;//计划收款时间
    private Float apPlan;//应收百分比
    private Double planapAmount;//应收金额
    private Long milestoneId;//里程ID
    private String name;//里程碑名称
    private String milestoneStatus;//里程碑状态
    private String milestoneFlag;//标记列
    private String attribute1;
    private String attribute2;
    private String attribute3;//合同行号
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
    private String lineInfo;

    public String getLineAmount() {
        return lineAmount;
    }

    public void setLineAmount(String lineAmount) {
        this.lineAmount = lineAmount;
    }

    public String getLineInfo() {
        return lineInfo;
    }

    public void setLineInfo(String lineInfo) {
        this.lineInfo = lineInfo;
    }

    public Long getKlineId() {
        return klineId;
    }

    public void setKlineId(Long klineId) {
        this.klineId = klineId;
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

    public String getAttribute15() {
        return attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
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

    public String getMilestoneFlag() {
        return milestoneFlag;
    }

    public void setMilestoneFlag(String milestoneFlag) {
        this.milestoneFlag = milestoneFlag;
    }

    public Long getKheaderId() {
        return kheaderId;
    }

    public void setKheaderId(Long kheaderId) {
        this.kheaderId = kheaderId;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Long lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(String lineStyle) {
        this.lineStyle = lineStyle;
    }

    public String getStyleDesc() {
        return styleDesc;
    }

    public void setStyleDesc(String styleDesc) {
        this.styleDesc = styleDesc;
    }

    public Long getLseId() {
        return lseId;
    }

    public void setLseId(Long lseId) {
        this.lseId = lseId;
    }

    public String getRcvTypeMeaning() {
        return rcvTypeMeaning;
    }

    public void setRcvTypeMeaning(String rcvTypeMeaning) {
        this.rcvTypeMeaning = rcvTypeMeaning;
    }

    public String getRcvType() {
        return rcvType;
    }

    public void setRcvType(String rcvType) {
        this.rcvType = rcvType;
    }

    public Date getPlanapDate() {
        return planapDate;
    }

    public void setPlanapDate(Date planapDate) {
        this.planapDate = planapDate;
    }

    public Float getApPlan() {
        return apPlan;
    }

    public void setApPlan(Float apPlan) {
        this.apPlan = apPlan;
    }

    public Double getPlanapAmount() {
        return planapAmount;
    }

    public void setPlanapAmount(Double planapAmount) {
        this.planapAmount = planapAmount;
    }

    public Long getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMilestoneStatus() {
        return milestoneStatus;
    }

    public void setMilestoneStatus(String milestoneStatus) {
        this.milestoneStatus = milestoneStatus;
    }
}
