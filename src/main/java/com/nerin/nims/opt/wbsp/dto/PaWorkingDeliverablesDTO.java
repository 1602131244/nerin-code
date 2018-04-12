package com.nerin.nims.opt.wbsp.dto;

import oracle.jdbc.driver.DatabaseError;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class PaWorkingDeliverablesDTO {
    private long specId;  //专业ELEMENTID
    private long parentid;//专业ELEMENTID的版本ID
    private long dlvrId; //工作包ID
    private String dlvrName;//工作包名称
    private long taskTypeId;//element type 工作包 系统 子项 专业
    private Date endDate;//计划完成日期
    private Date startDate;//计划起始日期
    private String designId;//设计人员ID
    private String designName;//设计人员
    private String checkId;//校核人员
    private String checkName;//校核人员
    private String reviewId;//审核人员
    private String reviewName;//审核人员
    private String approveId;//审定人员
    private String approveName;//审定人员
    private String regEngineerId;//注册
    private String regEngineerName;//注册
    private double workHour;//计划工时
    private String majorCode;//专业代字
    private String grandNum;//专业孙项号
    private String workgrandNum;//工作包孙项号
    private String worktype;//工作包类型
    private long authorNum;//创建人ID
    private String authorName;//创建人姓名
    private String description;//说明
    private String statusCode;//工作包状态
    private String statusName;
    //private String otherslist;//其他参与人信息
    private String grandName;//专业孙项名称
    private String matCode;//关联物料编码
    private String matName;//关联物料名称
    private String srcID;//引用版本ID
    private String srcRow;//引用行ID或verID
    private String srcClass;//引用类型 PBS还是WBS
    private String srcName;//引用版本名称
    private String authorClass;//创建人角色
    private long dlvrExtId;//扩展表ID
    private List<PaOthers> otherslist;
    public long getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(long taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public long getSpecId() {
        return specId;
    }

    public void setSpecId(long specId) {
        this.specId = specId;
    }

    public long getParentid() {
        return parentid;
    }

    public void setParentid(long parentid) {
        this.parentid = parentid;
    }

    public long getDlvrId() {
        return dlvrId;
    }

    public void setDlvrId(long dlvrId) {
        this.dlvrId = dlvrId;
    }

    public String getDlvrName() {
        return dlvrName;
    }

    public void setDlvrName(String dlvrName) {
        this.dlvrName = dlvrName;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getDesignId() {
        return designId;
    }

    public void setDesignId(String designId) {
        this.designId = designId;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public String getApproveId() {
        return approveId;
    }

    public void setApproveId(String approveId) {
        this.approveId = approveId;
    }

    public String getApproveName() {
        return approveName;
    }

    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }

    public String getRegEngineerId() {
        return regEngineerId;
    }

    public void setRegEngineerId(String regEngineerId) {
        this.regEngineerId = regEngineerId;
    }

    public String getRegEngineerName() {
        return regEngineerName;
    }

    public void setRegEngineerName(String regEngineerName) {
        this.regEngineerName = regEngineerName;
    }

    public double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(double workHour) {
        this.workHour = workHour;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getGrandNum() {
        return grandNum;
    }

    public void setGrandNum(String grandNum) {
        this.grandNum = grandNum;
    }

    public String getWorkgrandNum() {
        return workgrandNum;
    }

    public void setWorkgrandNum(String workgrandNum) {
        this.workgrandNum = workgrandNum;
    }

    public String getWorktype() {
        return worktype;
    }

    public void setWorktype(String worktype) {
        this.worktype = worktype;
    }

    public long getAuthorNum() {
        return authorNum;
    }

    public void setAuthorNum(long authorNum) {
        this.authorNum = authorNum;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }



    public String getGrandName() {
        return grandName;
    }

    public void setGrandName(String grandName) {
        this.grandName = grandName;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getSrcID() {
        return srcID;
    }

    public void setSrcID(String srcID) {
        this.srcID = srcID;
    }

    public String getSrcRow() {
        return srcRow;
    }

    public void setSrcRow(String srcRow) {
        this.srcRow = srcRow;
    }

    public String getSrcClass() {
        return srcClass;
    }

    public void setSrcClass(String srcClass) {
        this.srcClass = srcClass;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getAuthorClass() {
        return authorClass;
    }

    public void setAuthorClass(String authorClass) {
        this.authorClass = authorClass;
    }

    public long getDlvrExtId() {
        return dlvrExtId;
    }

    public void setDlvrExtId(long dlvrExtId) {
        this.dlvrExtId = dlvrExtId;
    }

    public List<PaOthers> getOtherslist() {
        return otherslist;
    }

    public void setOtherslist(List<PaOthers> otherslist) {
        this.otherslist = otherslist;
    }
}
