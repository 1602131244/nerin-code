package com.nerin.nims.opt.dsin.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class EQDocListDTO {
    private long id;
    private String name;
    private String describe;
    private String speciality;
    private String item;
    private String equipName;
    private String orderNO;
    private String orderName;
    private String requiredDate;
    private String receiveDate;
    private String createBy;
    private String approveStauts;
    private String enabled;
    private String psyj;
    private long   createById;
    private long   userId;
    private long   personId;

    public String getPsyj() {
        return psyj;
    }

    public void setPsyj(String psyj) {
        this.psyj = psyj;
    }

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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getEquipName() {
        return equipName;
    }

    public void setEquipName(String equipName) {
        this.equipName = equipName;
    }

    public String getOrderNO() {
        return orderNO;
    }

    public void setOrderNO(String orderNO) {
        this.orderNO = orderNO;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
