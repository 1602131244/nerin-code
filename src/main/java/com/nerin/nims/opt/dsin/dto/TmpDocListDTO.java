package com.nerin.nims.opt.dsin.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/3.
 */
public class TmpDocListDTO {
    private long id;
    private String name;
    private String describe;
    private String backDate;
    private String planBackDate;
    private String receiveDate;
    private String createBy;
    private String contact;
    private long contactId;
    private String  stauts;
    private long   createById;
    private long   userId;
    private long   personId;

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getBackDate() {
        return backDate;
    }

    public void setBackDate(String backDate) {
        this.backDate = backDate;
    }

    public String getPlanBackDate() {
        return planBackDate;
    }

    public void setPlanBackDate(String planBackDate) {
        this.planBackDate = planBackDate;
    }

    public String getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(String receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getStauts() {
        return stauts;
    }

    public void setStauts(String stauts) {
        this.stauts = stauts;
    }



    public long getCreateById() {
        return createById;
    }

    public void setCreateById(long createById) {
        this.createById = createById;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
