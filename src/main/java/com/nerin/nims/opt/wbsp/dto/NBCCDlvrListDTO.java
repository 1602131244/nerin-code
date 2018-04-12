package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/3.
 */
public class NBCCDlvrListDTO {
    private long id;
    private String name;
    private Date dueDate;
    private Date startDate;
    private long designID;
    private String designName;
    private long checkID;
    private String checkName;
    private long approveID;
    private String approveName;
    private long reviewID;
    private String reviewName;
    private String workHour;
    private String workCode;
    private long specID;

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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public long getDesignID() {
        return designID;
    }

    public void setDesignID(long designID) {
        this.designID = designID;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public long getCheckID() {
        return checkID;
    }

    public void setCheckID(long checkID) {
        this.checkID = checkID;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public long getReviewID() {
        return reviewID;
    }

    public void setReviewID(long reviewID) {
        this.reviewID = reviewID;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public String getWorkHour() {
        return workHour;
    }

    public void setWorkHour(String workHour) {
        this.workHour = workHour;
    }

    public long getSpecID() {
        return specID;
    }

    public void setSpecID(long specID) {
        this.specID = specID;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public long getApproveID() {
        return approveID;
    }

    public void setApproveID(long approveID) {
        this.approveID = approveID;
    }

    public String getApproveName() {
        return approveName;
    }

    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }
}
