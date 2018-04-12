package com.nerin.nims.opt.wbsp_oa.dto;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class CreateDlvrDTO {


    private long projectId;
    private long requestId;
    private String major;
    private String specId;
    private String startDate;
    private String endDate;
    private long designId;
    private long checkId;
    private long reviewId;
    private long approveId;
    private long schemeDesignerId;
    private long regEngineerId;
    private long userId;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getCheckId() {
        return checkId;
    }

    public void setCheckId(long checkId) {
        this.checkId = checkId;
    }

    public long getReviewId() {
        return reviewId;
    }

    public void setReviewId(long reviewId) {
        this.reviewId = reviewId;
    }

    public long getApproveId() {
        return approveId;
    }

    public void setApproveId(long approveId) {
        this.approveId = approveId;
    }

    public long getSchemeDesignerId() {
        return schemeDesignerId;
    }

    public void setSchemeDesignerId(long schemeDesignerId) {
        this.schemeDesignerId = schemeDesignerId;
    }

    public long getRegEngineerId() {
        return regEngineerId;
    }

    public void setRegEngineerId(long regEngineerId) {
        this.regEngineerId = regEngineerId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDesignId() {
        return designId;
    }

    public void setDesignId(long designId) {
        this.designId = designId;
    }
}
