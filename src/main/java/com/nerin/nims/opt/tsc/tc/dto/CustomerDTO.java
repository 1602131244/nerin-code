package com.nerin.nims.opt.tsc.tc.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yinglgu on 11/14/2016.
 */
public class CustomerDTO{

    private Long customerId;//          NUMBER,
    private String partyName;//           VARCHAR2(360), --名称
    private String organizationName;//    VARCHAR2(360), --简称
    private String customerSource;//      VARCHAR2(360), --来源
    private String customerCategory;//    VARCHAR2(360), --属性
    private String customerLevel;//      VARCHAR2(250), --等级
    private String customerIndustry ;//   VARCHAR2(250), --行业
    private String address;//   ;           VARCHAR2(360), --地址
    private String vatRegistrationNum;// VARCHAR2(250), --纳税登记编号
    private String knownAs;//             VARCHAR2(250), --曾用名
    private String customerUrl;//         VARCHAR2(250), --网址
    private String commets;//             VARCHAR2(1000), --简介
    private String status;//               VARCHAR2(100),
    private String oaUrl;//               VARCHAR2(250),
    private Long requestUserId;//      NUMBER --提交人
    private Date requestDate;//         DATE --提交日期
    private Long approvedUserId;//     NUMBER --审批人
    private Date approvedDate;//        DATE --审批日期
    private Long orgId;//              NUMBER --
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

    private String statusCode;
    private String customerCategoryCode;
    private String customerSourceCode;
    private String customerLevelCode;
    private String customerIndustryCode;
    private String attachmentExists;//  是否有附件
    private String partyNumber;
    private String personName;// 提交人

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPartyNumber() {
        return partyNumber;
    }

    public void setPartyNumber(String partyNumber) {
        this.partyNumber = partyNumber;
    }

    public String getAttachmentExists() {
        return attachmentExists;
    }

    public void setAttachmentExists(String attachmentExists) {
        this.attachmentExists = attachmentExists;
    }

    private List<ContactDTO> contacts = new ArrayList<ContactDTO>();
    private List<AttachmentDTO> attachments = new ArrayList<AttachmentDTO>();

    public List<ContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactDTO> contacts) {
        this.contacts = contacts;
    }

    public List<AttachmentDTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentDTO> attachments) {
        this.attachments = attachments;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getCustomerSource() {
        return customerSource;
    }

    public void setCustomerSource(String customerSource) {
        this.customerSource = customerSource;
    }

    public String getCustomerCategory() {
        return customerCategory;
    }

    public void setCustomerCategory(String customerCategory) {
        this.customerCategory = customerCategory;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getCustomerIndustry() {
        return customerIndustry;
    }

    public void setCustomerIndustry(String customerIndustry) {
        this.customerIndustry = customerIndustry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getVatRegistrationNum() {
        return vatRegistrationNum;
    }

    public void setVatRegistrationNum(String vatRegistrationNum) {
        this.vatRegistrationNum = vatRegistrationNum;
    }

    public String getKnownAs() {
        return knownAs;
    }

    public void setKnownAs(String knownAs) {
        this.knownAs = knownAs;
    }

    public String getCustomerUrl() {
        return customerUrl;
    }

    public void setCustomerUrl(String customerUrl) {
        this.customerUrl = customerUrl;
    }

    public String getCommets() {
        return commets;
    }

    public void setCommets(String commets) {
        this.commets = commets;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOaUrl() {
        return oaUrl;
    }

    public void setOaUrl(String oaUrl) {
        this.oaUrl = oaUrl;
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

    public Long getApprovedUserId() {
        return approvedUserId;
    }

    public void setApprovedUserId(Long approvedUserId) {
        this.approvedUserId = approvedUserId;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getCustomerCategoryCode() {
        return customerCategoryCode;
    }

    public void setCustomerCategoryCode(String customerCategoryCode) {
        this.customerCategoryCode = customerCategoryCode;
    }

    public String getCustomerSourceCode() {
        return customerSourceCode;
    }

    public void setCustomerSourceCode(String customerSourceCode) {
        this.customerSourceCode = customerSourceCode;
    }

    public String getCustomerLevelCode() {
        return customerLevelCode;
    }

    public void setCustomerLevelCode(String customerLevelCode) {
        this.customerLevelCode = customerLevelCode;
    }

    public String getCustomerIndustryCode() {
        return customerIndustryCode;
    }

    public void setCustomerIndustryCode(String customerIndustryCode) {
        this.customerIndustryCode = customerIndustryCode;
    }
}
