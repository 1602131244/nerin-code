package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/2.
 */
public class PaPublishDeliverablesDTO {
    private long specId;  //专业ELEMENTID
    private long parentId;//专业ELEMENTID的版本ID
    private long id; //工作包ID
    private String name;//工作包名称
    private long taskTypeId;//element type 工作包 系统 子项 专业
    private Date endDate;//计划完成日期
    private Date startDate;//计划起始日期
    private String designNum;//设计人员ID
    private String designName;//设计人员
    private String checkNum;//校核人员
    private String checkName;//校核人员
    private String reviewNum;//审核人员
    private String reviewName;//审核人员
    private String approveNum;//审定人员
    private String approveName;//审定人员
    private String certifiedNum;//注册
    private String certifiedName;//注册
    private double workHour;//计划工时
    private String majorCode;//专业代字
    private String grandNum;//专业孙项号
    private String workgrandNum;//工作包孙项号
    private String workCode;//工作包代字ID  对应后台 WDDA 的ID
    private String worktypeID;//工作包类型ID 图纸 文本
    private String authorNum;//创建人工号
    private String authorName;//创建人姓名
    private String authorId;//创建人工号
    private String description;//说明
    private String statusCode;//工作包状态
    private String status;
    //private String otherslist;//其他参与人信息
    private String grandName;//专业孙项名称
    private String matCode;//关联物料编码
    private String matName;//关联物料名称
    private String srcID;//引用版本ID
    private String srcRow;//引用行ID或verID
    private String srcClass;//引用类型 PBS还是WBS
    private String src;//引用版本名称
    private String authorClass;//创建人角色
    private long dlvrExtId;//扩展表ID
    private String amountTrial;//送审额
    private String amountAppr; //核定额 Check_Amt
    private String schemeNum; //方案设计人ID Scheme_Designer scheme_id
    private String schemeName; //方案设计人ID Scheme_Designer scheme_id
    private String dwgNum;//是否有效
    private String budget;//是否有效
    private String code; //工作包编号
    private String level;//层级
    private String iconCls;//图标
    private PaOthersList otherslist;
    private String designId;
    private String checkId;
    private String reviewId;
    private String approveId;
    private String certifiedId;
    private String schemeId;
    private String recvSpec;//接受专业
    private String defaultFlag;//接受专业
    private String schedule; //进度

    public String getRecvSpec() {
        return recvSpec;
    }

    public void setRecvSpec(String recvSpec) {
        this.recvSpec = recvSpec;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getSchemeId() {
        return schemeId;
    }

    public void setSchemeId(String schemeId) {
        this.schemeId = schemeId;
    }

    public String getDesignId() {
        return designId;
    }

    public void setDesignId(String designId) {
        this.designId = designId;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getApproveId() {
        return approveId;
    }

    public void setApproveId(String approveId) {
        this.approveId = approveId;
    }

    public String getCertifiedId() {
        return certifiedId;
    }

    public void setCertifiedId(String certifiedId) {
        this.certifiedId = certifiedId;
    }

    public String getWorktypeID() {
        return worktypeID;
    }

    public void setWorktypeID(String worktypeID) {
        this.worktypeID = worktypeID;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDwgNum() {
        return dwgNum;
    }

    public void setDwgNum(String dwgNum) {
        this.dwgNum = dwgNum;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getAmountTrial() {
        return amountTrial;
    }

    public void setAmountTrial(String amountTrial) {
        this.amountTrial = amountTrial;
    }

    public String getAmountAppr() {
        return amountAppr;
    }

    public void setAmountAppr(String amountAppr) {
        this.amountAppr = amountAppr;
    }

    public String getSchemeNum() {
        return schemeNum;
    }

    public void setSchemeNum(String schemeNum) {
        this.schemeNum = schemeNum;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

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

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
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

    public String getDesignNum() {
        return designNum;
    }

    public void setDesignNum(String designNum) {
        this.designNum = designNum;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public String getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(String reviewNum) {
        this.reviewNum = reviewNum;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public String getApproveNum() {
        return approveNum;
    }

    public void setApproveNum(String approveNum) {
        this.approveNum = approveNum;
    }

    public String getApproveName() {
        return approveName;
    }

    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }

    public String getCertifiedNum() {
        return certifiedNum;
    }

    public void setCertifiedNum(String certifiedNum) {
        this.certifiedNum = certifiedNum;
    }

    public String getCertifiedName() {
        return certifiedName;
    }

    public void setCertifiedName(String certifiedName) {
        this.certifiedName = certifiedName;
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

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getAuthorNum() {
        return authorNum;
    }

    public void setAuthorNum(String authorNum) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
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

    public PaOthersList getOtherslist() {
        return otherslist;
    }

    public void setOtherslist(PaOthersList otherslist) {
        this.otherslist = otherslist;
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
