package com.nerin.nims.opt.budget.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2016/9/26.
 */
public class BudgetLineEntity extends OracleBaseEntity implements ORAData {
    private Long lineId;
    private Long headerId;
    private String deptid;
    private String status;
    private Long oldProjectId;
    private String projectNumber;
    private String projectName;
    private String type;
    private String expenditureType;
    private Long outlineNumber;
    private Long rbsVersionId;
    private String alias;
    private double oldAmount;
    private double oldCost;
    private String sign;
    private Long totalPersons;
    private double perCapitaAmount;
    private double amount;
    private double auditAmount1;
    private Long personIdAudit1;
    private double auditAmount2;
    private Long personIdAudit2;
    private double auditAmount3;
    private Long personIdAudit3;
    private double auditAmount4;
    private Long personIdAudit4;
    private double auditAmount5;
    private Long personIdAudit5;
    private double auditAmount6;
    private Long personIdAudit6;
    private String description;
    private String attributeCategory;   //备用分类
    private String attribute1;  //二级组织ID
    private String attribute2; //是否导入数据标识
    private String attribute3; //备用属性3
    private String attribute4; //备用属性4
    private String attribute5; //备用属性5
    private String attribute6; //备用属性6
    private String attribute7; //备用属性7
    private String attribute8; //备用属性8
    private String attribute9; //备用属性9
    private String attribute10;  //备用属性10
    private String attribute11; //备用属性11
    private String attribute12; //备用属性12
    private String attribute13; //备用属性13
    private String attribute14; //备用属性14
    private String attribute15; //备用属性15

    public double getOldCost() {
        return oldCost;
    }

    public void setOldCost(double oldCost) {
        this.oldCost = oldCost;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOldProjectId() {
        return oldProjectId;
    }

    public void setOldProjectId(Long oldProjectId) {
        this.oldProjectId = oldProjectId;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExpenditureType() {
        return expenditureType;
    }

    public void setExpenditureType(String expenditureType) {
        this.expenditureType = expenditureType;
    }

    public Long getOutlineNumber() {
        return outlineNumber;
    }

    public void setOutlineNumber(Long outlineNumber) {
        this.outlineNumber = outlineNumber;
    }

    public Long getRbsVersionId() {
        return rbsVersionId;
    }

    public void setRbsVersionId(Long rbsVersionId) {
        this.rbsVersionId = rbsVersionId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public double getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(double oldAmount) {
        this.oldAmount = oldAmount;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Long getTotalPersons() {
        return totalPersons;
    }

    public void setTotalPersons(Long totalPersons) {
        this.totalPersons = totalPersons;
    }

    public double getPerCapitaAmount() {
        return perCapitaAmount;
    }

    public void setPerCapitaAmount(double perCapitaAmount) {
        this.perCapitaAmount = perCapitaAmount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAuditAmount1() {
        return auditAmount1;
    }

    public void setAuditAmount1(double auditAmount1) {
        this.auditAmount1 = auditAmount1;
    }

    public Long getPersonIdAudit1() {
        return personIdAudit1;
    }

    public void setPersonIdAudit1(Long personIdAudit1) {
        this.personIdAudit1 = personIdAudit1;
    }

    public double getAuditAmount2() {
        return auditAmount2;
    }

    public void setAuditAmount2(double auditAmount2) {
        this.auditAmount2 = auditAmount2;
    }

    public Long getPersonIdAudit2() {
        return personIdAudit2;
    }

    public void setPersonIdAudit2(Long personIdAudit2) {
        this.personIdAudit2 = personIdAudit2;
    }

    public double getAuditAmount3() {
        return auditAmount3;
    }

    public void setAuditAmount3(double auditAmount3) {
        this.auditAmount3 = auditAmount3;
    }

    public Long getPersonIdAudit3() {
        return personIdAudit3;
    }

    public void setPersonIdAudit3(Long personIdAudit3) {
        this.personIdAudit3 = personIdAudit3;
    }

    public double getAuditAmount4() {
        return auditAmount4;
    }

    public void setAuditAmount4(double auditAmount4) {
        this.auditAmount4 = auditAmount4;
    }

    public Long getPersonIdAudit4() {
        return personIdAudit4;
    }

    public void setPersonIdAudit4(Long personIdAudit4) {
        this.personIdAudit4 = personIdAudit4;
    }

    public double getAuditAmount5() {
        return auditAmount5;
    }

    public void setAuditAmount5(double auditAmount5) {
        this.auditAmount5 = auditAmount5;
    }

    public Long getPersonIdAudit5() {
        return personIdAudit5;
    }

    public void setPersonIdAudit5(Long personIdAudit5) {
        this.personIdAudit5 = personIdAudit5;
    }

    public double getAuditAmount6() {
        return auditAmount6;
    }

    public void setAuditAmount6(double auditAmount6) {
        this.auditAmount6 = auditAmount6;
    }

    public Long getPersonIdAudit6() {
        return personIdAudit6;
    }

    public void setPersonIdAudit6(Long personIdAudit6) {
        this.personIdAudit6 = personIdAudit6;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_DEPT_BUDGET_LINES_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER
            , OracleTypes.VARCHAR , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR
            , OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR,OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.DATE};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public BudgetLineEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getLineId());
        _struct.setAttribute(1, this.getHeaderId());
        _struct.setAttribute(2, this.getDeptid());
        _struct.setAttribute(3, this.getStatus());
        _struct.setAttribute(4, this.getOldProjectId());
        _struct.setAttribute(5, this.getProjectNumber());
        _struct.setAttribute(6, this.getProjectName());
        _struct.setAttribute(7, this.getType());
        _struct.setAttribute(8, this.getExpenditureType());
        _struct.setAttribute(9, this.getOutlineNumber());
        _struct.setAttribute(10, this.getRbsVersionId());
        _struct.setAttribute(11, this.getAlias());
        _struct.setAttribute(12, this.getOldAmount());
        _struct.setAttribute(13, this.getOldCost());
        _struct.setAttribute(14, this.getSign());
        _struct.setAttribute(15, this.getTotalPersons());
        _struct.setAttribute(16, this.getPerCapitaAmount());
        _struct.setAttribute(17, this.getAmount());
        _struct.setAttribute(18, this.getAuditAmount1());
        _struct.setAttribute(19, this.getPersonIdAudit1());
        _struct.setAttribute(20, this.getAuditAmount2());
        _struct.setAttribute(21, this.getPersonIdAudit2());
        _struct.setAttribute(22, this.getAuditAmount3());
        _struct.setAttribute(23, this.getPersonIdAudit3());
        _struct.setAttribute(24, this.getAuditAmount4());
        _struct.setAttribute(25, this.getPersonIdAudit4());
        _struct.setAttribute(26, this.getAuditAmount5());
        _struct.setAttribute(27, this.getPersonIdAudit5());
        _struct.setAttribute(28, this.getAuditAmount6());
        _struct.setAttribute(29, this.getPersonIdAudit6());
        _struct.setAttribute(30, this.getDescription());
        _struct.setAttribute(31, this.getAttributeCategory());
        _struct.setAttribute(32, this.getAttribute1());
        _struct.setAttribute(33, this.getAttribute2());
        _struct.setAttribute(34, this.getAttribute3());
        _struct.setAttribute(35, this.getAttribute4());
        _struct.setAttribute(36, this.getAttribute5());
        _struct.setAttribute(37, this.getAttribute6());
        _struct.setAttribute(38, this.getAttribute7());
        _struct.setAttribute(39, this.getAttribute8());
        _struct.setAttribute(40, this.getAttribute9());
        _struct.setAttribute(41, this.getAttribute10());
        _struct.setAttribute(42, this.getAttribute11());
        _struct.setAttribute(43, this.getAttribute12());
        _struct.setAttribute(44, this.getAttribute13());
        _struct.setAttribute(45, this.getAttribute14());
        _struct.setAttribute(46, this.getAttribute15());
        _struct.setAttribute(47, this.getLastUpdateDate());
        _struct.setAttribute(48, this.getLastUpdatedBy());
        _struct.setAttribute(49, this.getLastUpdateLogin());
        _struct.setAttribute(50, this.getCreatedBy());
        _struct.setAttribute(51, this.getCreationDate());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }

}
