package com.nerin.nims.opt.wbsp_oa.dto;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class HeadAchievementOaDTO {

    private String xmbh; //项目编号,
    private String xmmc; //项目名称,
    private String content; //校审内容,
    private String zy; //专业名称,
    private String wb; //文本url,
    private String tz; //图纸url,
    private ArrayList<DlvrDetailDTO> dlvrDetail; //图纸工作包列表数组
    private String design; //设计人六位员工号,
    private String check; // 校核人六位员工号,
    private String review; // 审核人六位员工号,
    private String approve; //审定人六位员工号,
    private String certified; // 注册工程师六位员工号,
    private String scheme; // 方案设计人六位员工号,
    private String majorCode; //专业id,
    private String dlvrs; //工作包id串,
    private String nbccTaskName; //协作文本名称,
    private Long nbccDlvr; //文本工作包id,
    private Long taskHeaderId; //协作文本id

    public String getXmbh() {
        return xmbh;
    }

    public void setXmbh(String xmbh) {
        this.xmbh = xmbh;
    }

    public String getXmmc() {
        return xmmc;
    }

    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getZy() {
        return zy;
    }

    public void setZy(String zy) {
        this.zy = zy;
    }

    public String getWb() {
        return wb;
    }

    public void setWb(String wb) {
        this.wb = wb;
    }

    public String getTz() {
        return tz;
    }

    public void setTz(String tz) {
        this.tz = tz;
    }

    public ArrayList<DlvrDetailDTO> getDlvrDetail() {
        return dlvrDetail;
    }

    public void setDlvrDetail(ArrayList<DlvrDetailDTO> dlvrDetail) {
        this.dlvrDetail = dlvrDetail;
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

    public Long getNbccDlvr() {
        return nbccDlvr;
    }

    public void setNbccDlvr(Long nbccDlvr) {
        this.nbccDlvr = nbccDlvr;
    }

    public Long getTaskHeaderId() {
        return taskHeaderId;
    }

    public void setTaskHeaderId(Long taskHeaderId) {
        this.taskHeaderId = taskHeaderId;
    }
}
