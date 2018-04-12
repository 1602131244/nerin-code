package com.nerin.nims.opt.nbcc.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/2/13.
 */
public class TaskTypeElementsEntity extends OracleBaseEntity implements ORAData {
    private Long assignmentId;
    private String taskTypeCode;//文本类型编码
    private Long elementTypeId;//工作包类型ID
    private String elementTypeName;//工作包类型名称
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

    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getTaskTypeCode() {
        return taskTypeCode;
    }

    public void setTaskTypeCode(String taskTypeCode) {
        this.taskTypeCode = taskTypeCode;
    }

    public Long getElementTypeId() {
        return elementTypeId;
    }

    public void setElementTypeId(Long elementTypeId) {
        this.elementTypeId = elementTypeId;
    }

    public String getElementTypeName() {
        return elementTypeName;
    }

    public void setElementTypeName(String elementTypeName) {
        this.elementTypeName = elementTypeName;
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

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_TASK_TYPE_ELEMENTS_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER,OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.DATE};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public TaskTypeElementsEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getAssignmentId());
        _struct.setAttribute(1, this.getTaskTypeCode());
        _struct.setAttribute(2, this.getElementTypeId());
        _struct.setAttribute(3, this.getAttributeCategory());
        _struct.setAttribute(4, this.getAttribute1());
        _struct.setAttribute(5, this.getAttribute2());
        _struct.setAttribute(6, this.getAttribute3());
        _struct.setAttribute(7, this.getAttribute4());
        _struct.setAttribute(8, this.getAttribute5());
        _struct.setAttribute(9, this.getAttribute6());
        _struct.setAttribute(10, this.getAttribute7());
        _struct.setAttribute(11, this.getAttribute8());
        _struct.setAttribute(12, this.getAttribute9());
        _struct.setAttribute(13, this.getAttribute10());
        _struct.setAttribute(14, this.getAttribute11());
        _struct.setAttribute(15, this.getAttribute12());
        _struct.setAttribute(16, this.getAttribute13());
        _struct.setAttribute(17, this.getAttribute14());
        _struct.setAttribute(18, this.getAttribute15());
        _struct.setAttribute(19, this.getLastUpdateDate());
        _struct.setAttribute(20, this.getLastUpdatedBy());
        _struct.setAttribute(21, this.getLastUpdateLogin());
        _struct.setAttribute(22, this.getCreatedBy());
        _struct.setAttribute(23, this.getCreationDate());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
