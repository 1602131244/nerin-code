package com.nerin.nims.opt.dsin.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class SpecTypeDocListDTO {
    private long id;
    private long hid;
    private String name;
    private String describe;
    private String speciality;
    private String receiveDate;
    private String createBy;
    private String required;
    private String approveStauts;
    private String enabled;
    private long   createById;
    private long   userId;
    private long   personId;
    private String psyj;

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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getApproveStauts() {
        return approveStauts;
    }

    public void setApproveStauts(String approveStauts) {
        this.approveStauts = approveStauts;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public long getCreateById() {
        return createById;
    }

    public void setCreateById(long createById) {
        this.createById = createById;
    }

    public long getHid() {
        return hid;
    }

    public void setHid(long hid) {
        this.hid = hid;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPsyj() {
        return psyj;
    }

    public void setPsyj(String psyj) {
        this.psyj = psyj;
    }
}
