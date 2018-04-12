package com.nerin.nims.opt.wbsp_oa.dto;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class ApproveFromDTO {


    private String status;  //OA流程当前状态,
    private String currentPersonNum;  //流程当前操作人六位员工号,多人逗号隔开,
    private String design;  //设计人六位员工号,
    private String check;  // 校核人六位员工号,
    private String review;  // 审核人六位员工号,
    private String approve;  //审定人六位员工号,
    private String certified;  // 注册工程师六位员工号,
    private String scheme;  // 方案设计人六位员工号,
    private String  majorCode;  //专业id,
    private String dlvrs;  //工作包id串,
    private String nbccTaskName;  //协作文本名称,
    private String lcsm;  //流程说明,
    private String startDate; //起始日期
    private String endDate; //结束日期
    private long  formid;  //数据库表id
    private String  erpid;
    private String zyjds;  //专业节点串,

    public String getErpid() {
        return erpid;
    }

    public void setErpid(String erpid) {
        this.erpid = erpid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentPersonNum() {
        return currentPersonNum;
    }

    public void setCurrentPersonNum(String currentPersonNum) {
        this.currentPersonNum = currentPersonNum;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getCertified() {
        return certified;
    }

    public void setCertified(String certified) {
        this.certified = certified;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getDlvrs() {
        return dlvrs;
    }

    public void setDlvrs(String dlvrs) {
        this.dlvrs = dlvrs;
    }

    public String getNbccTaskName() {
        return nbccTaskName;
    }

    public void setNbccTaskName(String nbccTaskName) {
        this.nbccTaskName = nbccTaskName;
    }

    public long getFormid() {
        return formid;
    }

    public void setFormid(long formid) {
        this.formid = formid;
    }

    public String getLcsm() {
        return lcsm;
    }

    public void setLcsm(String lcsm) {
        this.lcsm = lcsm;
    }

    public String getZyjds() {
        return zyjds;
    }

    public void setZyjds(String zyjds) {
        this.zyjds = zyjds;
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
}
