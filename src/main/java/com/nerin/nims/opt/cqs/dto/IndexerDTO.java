package com.nerin.nims.opt.cqs.dto;

import java.util.Date;

/**
 * Created by user on 16/6/28.
 */
public class IndexerDTO {
    private Long indexId; //'PK'
    private Long indexOrder;//'等级索引排序号'
    private String catagory; //'等级类别'
    private String services; //'适用介质'
    private String designTempSource; //'设计温度（℃）'
    private Double designTempMin; //'设计温度-下限'
    private Double designTempMax; //'设计温度-上限'
    private String designTempSpec;//'设计温度（℃）-特殊项'
    private String designPresSource;//'设计压力（MPa）'
    private Double designPresMin;//'设计压力-下限'
    private Double designPresMax;//'设计压力-上限'
    private String designPresSpec;//'设计压力（MPa）-特殊项'
    private String pipingMatlClass;//'管道压力等级'
    private String basicMaterial;//'基本材料'
    private String rating;//'压力等级'
    private String flangeFacing;//'法兰密封面'
    private Double ca; //'腐蚀裕量（mm）'
    private String note;//'备注'
    private Long batchId;//数据批ID
    //SET WHO 5字段
    private Long createdBy;
    private Date creationDate;
    private Long lastUpdatedBy;
    private Date lastUpdateDate;
    private Long lastUpdateLogin;
    //备用16字段
    private String attributeCcategory;
    private String attribute1;
    private String attribute2;
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


    public Long getIndexId() {
        return indexId;
    }

    public void setIndexId(Long indexId) {
        this.indexId = indexId;
    }

    public Long getIndexOrder() {
        return indexOrder;
    }

    public void setIndexOrder(Long indexOrder) {
        this.indexOrder = indexOrder;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getDesignTempSource() {
        return designTempSource;
    }

    public void setDesignTempSource(String designTempSource) {
        this.designTempSource = designTempSource;
    }

    public Double getDesignTempMin() {
        return designTempMin;
    }

    public void setDesignTempMin(Double designTempMin) {
        this.designTempMin = designTempMin;
    }

    public Double getDesignTempMax() {
        return designTempMax;
    }

    public void setDesignTempMax(Double designTempMax) {
        this.designTempMax = designTempMax;
    }

    public String getDesignTempSpec() {
        return designTempSpec;
    }

    public void setDesignTempSpec(String designTempSpec) {
        this.designTempSpec = designTempSpec;
    }

    public String getDesignPresSource() {
        return designPresSource;
    }

    public void setDesignPresSource(String designPresSource) {
        this.designPresSource = designPresSource;
    }

    public Double getDesignPresMin() {
        return designPresMin;
    }

    public void setDesignPresMin(Double designPresMin) {
        this.designPresMin = designPresMin;
    }

    public Double getDesignPresMax() {
        return designPresMax;
    }

    public void setDesignPresMax(Double designPresMax) {
        this.designPresMax = designPresMax;
    }

    public String getDesignPresSpec() {
        return designPresSpec;
    }

    public void setDesignPresSpec(String designPresSpec) {
        this.designPresSpec = designPresSpec;
    }

    public String getPipingMatlClass() {
        return pipingMatlClass;
    }

    public void setPipingMatlClass(String pipingMatlClass) {
        this.pipingMatlClass = pipingMatlClass;
    }

    public String getBasicMaterial() {
        return basicMaterial;
    }

    public void setBasicMaterial(String basicMaterial) {
        this.basicMaterial = basicMaterial;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getFlangeFacing() {
        return flangeFacing;
    }

    public void setFlangeFacing(String flangeFacing) {
        this.flangeFacing = flangeFacing;
    }

    public Double getCa() {
        return ca;
    }

    public void setCa(Double ca) {
        this.ca = ca;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    public String getAttributeCcategory() {
        return attributeCcategory;
    }

    public void setAttributeCcategory(String attributeCcategory) {
        this.attributeCcategory = attributeCcategory;
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

}
