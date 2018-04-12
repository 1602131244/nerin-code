package com.nerin.nims.opt.cqs.module;

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
 * Created by user on 16/7/4.
 */
public class IndexerEntity extends OracleBaseEntity implements ORAData {

    private Long indexId; //'PK'
    private Long indexOrder;//'等级索引排序号'
    private String catagory; //'等级类别'
    private String services; //'适用介质'
    private String desginTempSource; //'设计温度（℃）'
    private Long designTempMin; //'设计温度-下限'
    private Long designTempMax; //'设计温度-上限'
    private String designTempSpec;//'设计温度（℃）-特殊项'
    private String designPresSource;//'设计压力（MPa）'
    private Long designPresMin;//'设计压力-下限'
    private Long designPresMax;//'设计压力-上限'
    private String designPresSpec;//'设计压力（MPa）-特殊项'
    private String pipingMatlClass;//'管道压力等级'
    private String basicMaterial;//'基本材料'
    private String rating;//'压力等级'
    private String flangeFacing;//'法兰密封面'
    private Long ca; //'腐蚀裕量（mm）'
    private String note;//'备注'
    private Long batchId;//数据批ID
    //备用16字段
    private String attributeCategory;
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

    public String getDesginTempSource() {
        return desginTempSource;
    }

    public void setDesginTempSource(String desginTempSource) {
        this.desginTempSource = desginTempSource;
    }

    public Long getDesignTempMin() {
        return designTempMin;
    }

    public void setDesignTempMin(Long designTempMin) {
        this.designTempMin = designTempMin;
    }

    public Long getDesignTempMax() {
        return designTempMax;
    }

    public void setDesignTempMax(Long designTempMax) {
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

    public Long getDesignPresMin() {
        return designPresMin;
    }

    public void setDesignPresMin(Long designPresMin) {
        this.designPresMin = designPresMin;
    }

    public Long getDesignPresMax() {
        return designPresMax;
    }

    public void setDesignPresMax(Long designPresMax) {
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

    public Long getCa() {
        return ca;
    }

    public void setCa(Long ca) {
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


    public static final String _ORACLE_TYPE_NAME = "APPS.CQS_INDEX_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.NUMBER , OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.VARCHAR //20
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.DATE //set who 5s
    };
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public IndexerEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getIndexId());
        _struct.setAttribute(1, this.getIndexOrder());
        _struct.setAttribute(2, this.getCatagory());
        _struct.setAttribute(3, this.getServices());
        _struct.setAttribute(4, this.getDesginTempSource());
        _struct.setAttribute(5, this.getDesignPresMin());
        _struct.setAttribute(6, this.getDesignPresMax());
        _struct.setAttribute(7, this.getDesignTempSpec());
        _struct.setAttribute(8, this.getDesignPresSource());
        _struct.setAttribute(9, this.getDesignPresMin());
        _struct.setAttribute(10, this.getDesignPresMax());
        _struct.setAttribute(11, this.getDesignPresSpec());
        _struct.setAttribute(12, this.getPipingMatlClass());
        _struct.setAttribute(13, this.getBasicMaterial());
        _struct.setAttribute(14, this.getRating());
        _struct.setAttribute(15, this.getFlangeFacing());
        _struct.setAttribute(16, this.getCa());
        _struct.setAttribute(17, this.getNote());
        _struct.setAttribute(18, this.getBatchId());

        _struct.setAttribute(19, this.getAttributeCategory());
        _struct.setAttribute(20, this.getAttribute1());
        _struct.setAttribute(21, this.getAttribute2());
        _struct.setAttribute(22, this.getAttribute3());
        _struct.setAttribute(23, this.getAttribute4());
        _struct.setAttribute(24, this.getAttribute5());
        _struct.setAttribute(25, this.getAttribute6());
        _struct.setAttribute(26, this.getAttribute7());
        _struct.setAttribute(27, this.getAttribute8());
        _struct.setAttribute(28, this.getAttribute9());
        _struct.setAttribute(29, this.getAttribute10());
        _struct.setAttribute(30, this.getAttribute11());
        _struct.setAttribute(31, this.getAttribute12());
        _struct.setAttribute(32, this.getAttribute13());
        _struct.setAttribute(33, this.getAttribute14());
        _struct.setAttribute(34, this.getAttribute15());
        _struct.setAttribute(35, this.getLastUpdateDate());
        _struct.setAttribute(36, this.getLastUpdatedBy());
        _struct.setAttribute(37, this.getLastUpdateLogin());
        _struct.setAttribute(38, this.getCreatedBy());
        _struct.setAttribute(39, this.getCreationDate());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }

}
