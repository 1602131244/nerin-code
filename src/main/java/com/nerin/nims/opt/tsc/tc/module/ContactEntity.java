package com.nerin.nims.opt.tsc.tc.module;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by yinglgu on 11/8/2016.
 */
public class ContactEntity implements ORAData {
    private Long customerId;//           NUMBER, --vendor_id
    private Long contactId;//          NUMBER, --contact_id
    private Long lineNum;//            NUMBER, --line_num
    private String personName;//         VARCHAR2(100), --姓名
    private String personTitle;//        VARCHAR2(100), --职务
    private String department;//          VARCHAR2(100), --部门
    private String mobile;//              VARCHAR2(30), --手机
    private String phone;//               VARCHAR2(30), --电话
    private String emailAddress;//       VARCHAR2(250), --邮箱
    private String contactPersonName;// VARCHAR2(250), --内部联系人
    private String comments;//            VARCHAR2(250), --备注
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getLineNum() {
        return lineNum;
    }

    public void setLineNum(Long lineNum) {
        this.lineNum = lineNum;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonTitle() {
        return personTitle;
    }

    public void setPersonTitle(String personTitle) {
        this.personTitle = personTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
    public static final String _ORACLE_TYPE_NAME = "APPS.CUSTOMER_CONTACT_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public ContactEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };


    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getCustomerId());
        _struct.setAttribute(1, this.getContactId());
        _struct.setAttribute(2, this.getLineNum());
        _struct.setAttribute(3, this.getPersonName());
        _struct.setAttribute(4, this.getPersonTitle());
        _struct.setAttribute(5, this.getDepartment());
        _struct.setAttribute(6, this.getMobile());
        _struct.setAttribute(7, this.getPhone());
        _struct.setAttribute(8, this.getEmailAddress());
        _struct.setAttribute(9, this.getContactPersonName());
        _struct.setAttribute(10, this.getComments());
        _struct.setAttribute(11, this.getAttribute1());
        _struct.setAttribute(12, this.getAttribute2());
        _struct.setAttribute(13, this.getAttribute3());
        _struct.setAttribute(14, this.getAttribute4());
        _struct.setAttribute(15, this.getAttribute5());
        _struct.setAttribute(16, this.getAttribute6());
        _struct.setAttribute(17, this.getAttribute7());
        _struct.setAttribute(18, this.getAttribute8());
        _struct.setAttribute(19, this.getAttribute9());
        _struct.setAttribute(20, this.getAttribute10());
        _struct.setAttribute(21, this.getAttribute11());
        _struct.setAttribute(22, this.getAttribute12());
        _struct.setAttribute(23, this.getAttribute13());
        _struct.setAttribute(24, this.getAttribute14());
        _struct.setAttribute(25, this.getAttribute15());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
