package com.nerin.nims.opt.wbsp_oa.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class DlvrDTO {
    private String design; //设计人六位员工号,
    private String check; // 校核人六位员工号,
    private String review; // 审核人六位员工号,
    private String approve; //审定人六位员工号,
    private String certified; // 注册工程师六位员工号,
    private String scheme; // 方案设计人六位员工号,
    private String majorCode; //专业id,
    private String worktypeID; //工作包类型

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

    public String getWorktypeID() {
        return worktypeID;
    }

    public void setWorktypeID(String worktypeID) {
        this.worktypeID = worktypeID;
    }
}
