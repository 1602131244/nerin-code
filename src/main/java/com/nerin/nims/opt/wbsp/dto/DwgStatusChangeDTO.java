package com.nerin.nims.opt.wbsp.dto;

/**
 * Created by wancong on 2016/7/7.
 */
public class DwgStatusChangeDTO {


    private Long workId;//工作包号
    private String divisionNum;//工作包所属子项号变更描述
    private String divisionName;//工作包所属子项名称变更描述
    private String specialty;//工作包所属专业
    private String workName;//工作包名称变更描述
    private String status;//图纸状态变更描述
    private String design;//设计人变更描述
    private String check;//校核人变更描述
    private String review;//审核人变更描述
    private String approve;//审定人变更描述
    private String certified;//注册工程师变更描述
    private String scheme;//方案设计人变更描述
    private String grandNum;//专业孙项号变更描述
    private String grandName;//专业孙项名称变更描述
    private String Workgrandnum;//工作包孙项号
    private String dual;//子项双语名称变更描述
    private String matCode;//关联物料编码变更描述
    private String matName;//关联物料名称变更描述

    public String getWorkgrandnum() {
        return Workgrandnum;
    }

    public void setWorkgrandnum(String workgrandnum) {
        Workgrandnum = workgrandnum;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public String getDivisionNum() {
        return divisionNum;
    }

    public void setDivisionNum(String divisionNum) {
        this.divisionNum = divisionNum;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getGrandNum() {
        return grandNum;
    }

    public void setGrandNum(String grandNum) {
        this.grandNum = grandNum;
    }

    public String getGrandName() {
        return grandName;
    }

    public void setGrandName(String grandName) {
        this.grandName = grandName;
    }

    public String getDual() {
        return dual;
    }

    public void setDual(String dual) {
        this.dual = dual;
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
}
