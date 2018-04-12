package com.nerin.nims.opt.nbcc.module;

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
 * Created by yinglgu on 6/27/2016.
 */
public class TaskTypeEntity extends OracleBaseEntity implements ORAData {
    private String lookupType;
    private String lookupCode;//任务类型编码
    private String meaning;//任务类型名称
    private String description;//OA流程编号
    private String enabledFlag;//是否启用
    private Date startDateActive;//开始日期
    private Date endDateActive;//结束日期
    private String tag; // A、B、C类
    private String attributeCategory;//上下文值
    private String attribute1;//对应项目阶段
    private String attribute2;//对应项目阶段
    private String attribute3;//对应项目阶段
    private String attribute4;//对应项目阶段
    private String attribute5;//对应项目阶段
    private String attribute6;//对应项目阶段
    private String attribute7;//对应项目阶段
    private String attribute8;//协作模板是否必备专业和工作包信息
    private Long attribute9;//任务类型模板
    private String attribute10;
    private String attribute11;
    private String attribute12;
    private String attribute13;
    private String attribute14;
    private String attribute15;

    public String getLookupType() {
        return lookupType;
    }

    public void setLookupType(String lookupType) {
        this.lookupType = lookupType;
    }

    public String getLookupCode() {
        return lookupCode;
    }

    public void setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    public Date getStartDateActive() {
        return startDateActive;
    }

    public void setStartDateActive(Date startDateActive) {
        this.startDateActive = startDateActive;
    }

    public Date getEndDateActive() {
        return endDateActive;
    }

    public void setEndDateActive(Date endDateActive) {
        this.endDateActive = endDateActive;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public Long getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(Long attribute9) {
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

    public static final String _ORACLE_TYPE_NAME = "APPS.NBCC_TASK_TYPE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.DATE, OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR,
            OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR,
            OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR,
            OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public TaskTypeEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getLookupType());
        _struct.setAttribute(1, this.getLookupCode());
        _struct.setAttribute(2, this.getMeaning());
        _struct.setAttribute(3, this.getDescription());
        _struct.setAttribute(4, this.getEnabledFlag());
        _struct.setAttribute(5, this.getStartDateActive());
        _struct.setAttribute(6, this.getEndDateActive());
        _struct.setAttribute(7, this.getCreatedBy());
        _struct.setAttribute(8, this.getCreationDate());
        _struct.setAttribute(9, this.getLastUpdatedBy());
        _struct.setAttribute(10, this.getLastUpdateLogin());
        _struct.setAttribute(11, this.getLastUpdateDate());
        _struct.setAttribute(12, this.getAttributeCategory());
        _struct.setAttribute(13, this.getAttribute1());
        _struct.setAttribute(14, this.getAttribute2());
        _struct.setAttribute(15, this.getAttribute3());
        _struct.setAttribute(16, this.getAttribute4());
        _struct.setAttribute(17, this.getAttribute5());
        _struct.setAttribute(18, this.getAttribute6());
        _struct.setAttribute(19, this.getAttribute7());
        _struct.setAttribute(20, this.getAttribute8());
        _struct.setAttribute(21, this.getAttribute9());
        _struct.setAttribute(22, this.getAttribute10());
        _struct.setAttribute(23, this.getAttribute11());
        _struct.setAttribute(24, this.getAttribute12());
        _struct.setAttribute(25, this.getAttribute13());
        _struct.setAttribute(26, this.getAttribute14());
        _struct.setAttribute(27, this.getAttribute15());
        _struct.setAttribute(28, this.getTag());
        return  _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
