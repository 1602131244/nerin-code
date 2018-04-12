package com.nerin.nims.opt.tsc.ts.module;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by yinglgu on 11/8/2016.
 */
public class SupplierEntity implements ORAData {
    private Long vendorId;//            NUMBER, --vendor_id
    private String vendorName;//          VARCHAR2(240), --供应商名称
    private String bankName;//            VARCHAR2(250), --帐户名
    private String bankAccountNum;//     VARCHAR2(250), --银行帐号
    private String address;//              VARCHAR2(1000), --地址
    private String vendorType;//          VARCHAR2(100), --供应商属性
    private String supplierNature;//      VARCHAR2(250), --供应商等级
    private String vendorLevel;//         VARCHAR2(250), --供应商性质
    private String vatRegistrationNum;// VARCHAR2(250), --纳税登记编号
    private String num_1099;//             VARCHAR2(250), --纳税人标识
    private String status;//               VARCHAR2(250), --状态 NEW
    private Long requestUserId;//      NUMBER, --提交人
    private Date requestDate;//         DATE, --提交日期
    private Long orgId;//               NUMBER,
    private String attribute1;  //备用属性1
    private String attribute2; //备用属性2
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

    public Long getVendorId() {
        return vendorId;
    }

    public void setVendorId(Long vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNum() {
        return bankAccountNum;
    }

    public void setBankAccountNum(String bankAccountNum) {
        this.bankAccountNum = bankAccountNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVendorType() {
        return vendorType;
    }

    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
    }

    public String getSupplierNature() {
        return supplierNature;
    }

    public void setSupplierNature(String supplierNature) {
        this.supplierNature = supplierNature;
    }

    public String getVendorLevel() {
        return vendorLevel;
    }

    public void setVendorLevel(String vendorLevel) {
        this.vendorLevel = vendorLevel;
    }

    public String getVatRegistrationNum() {
        return vatRegistrationNum;
    }

    public void setVatRegistrationNum(String vatRegistrationNum) {
        this.vatRegistrationNum = vatRegistrationNum;
    }

    public String getNum_1099() {
        return num_1099;
    }

    public void setNum_1099(String num_1099) {
        this.num_1099 = num_1099;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(Long requestUserId) {
        this.requestUserId = requestUserId;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
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

    public static final String _ORACLE_TYPE_NAME = "APPS.SUPPLIER_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public SupplierEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getVendorId());
        _struct.setAttribute(1, this.getVendorName());
        _struct.setAttribute(2, this.getBankName());
        _struct.setAttribute(3, this.getBankAccountNum());
        _struct.setAttribute(4, this.getAddress());
        _struct.setAttribute(5, this.getVendorType());
        _struct.setAttribute(6, this.getSupplierNature());
        _struct.setAttribute(7, this.getVendorLevel());
        _struct.setAttribute(8, this.getVatRegistrationNum());
        _struct.setAttribute(9, this.getNum_1099());
        _struct.setAttribute(10, this.getStatus());
        _struct.setAttribute(11, this.getRequestUserId());
        _struct.setAttribute(12, this.getRequestDate());
        _struct.setAttribute(13, this.getOrgId());
        _struct.setAttribute(14, this.getAttribute1());
        _struct.setAttribute(15, this.getAttribute2());
        _struct.setAttribute(16, this.getAttribute3());
        _struct.setAttribute(17, this.getAttribute4());
        _struct.setAttribute(18, this.getAttribute5());
        _struct.setAttribute(19, this.getAttribute6());
        _struct.setAttribute(20, this.getAttribute7());
        _struct.setAttribute(21, this.getAttribute8());
        _struct.setAttribute(22, this.getAttribute9());
        _struct.setAttribute(23, this.getAttribute10());
        _struct.setAttribute(24, this.getAttribute11());
        _struct.setAttribute(25, this.getAttribute12());
        _struct.setAttribute(26, this.getAttribute13());
        _struct.setAttribute(27, this.getAttribute14());
        _struct.setAttribute(28, this.getAttribute15());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }

}
