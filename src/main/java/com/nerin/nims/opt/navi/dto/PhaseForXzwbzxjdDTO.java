package com.nerin.nims.opt.navi.dto;

import java.util.Date;

/**
 * Created by user on 16/11/9.
 */
public class PhaseForXzwbzxjdDTO {
    private Long chapterId;
    private String chapterNo;
    private String chapterName;
    private String attribute7;
    private String chapterStatus;
    private String statusCode;
    private String progress;
    private Date dueDate;
    private Date zyjsCompletionDate;
    private String currentPerson;
    private String owner;
    private String designed;
    private String checked;
    private String reviewed;
    private String approved;
    private String sqr;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(String chapterNo) {
        this.chapterNo = chapterNo;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    public String getChapterStatus() {
        return chapterStatus;
    }

    public void setChapterStatus(String chapterStatus) {
        this.chapterStatus = chapterStatus;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getZyjsCompletionDate() {
        return zyjsCompletionDate;
    }

    public void setZyjsCompletionDate(Date zyjsCompletionDate) {
        this.zyjsCompletionDate = zyjsCompletionDate;
    }

    public String getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(String currentPerson) {
        this.currentPerson = currentPerson;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDesigned() {
        return designed;
    }

    public void setDesigned(String designed) {
        this.designed = designed;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getReviewed() {
        return reviewed;
    }

    public void setReviewed(String reviewed) {
        this.reviewed = reviewed;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getSqr() {
        return sqr;
    }

    public void setSqr(String sqr) {
        this.sqr = sqr;
    }
}
/**
 ctc.chapter_id,
 ctc.chapter_name,
 ctc.attribute7, --排序
 fl.meaning chapter_status, --状态
 fl.description progress, --进度
 v.due_date, --计划完成时间,计划专业校审完成时间
 get_zyjs_approved_date(ctc.chapter_id) zyjs_completion_date, --实际专业校审完成时间
 get_current_person_wb(ctc.chapter_id) current_person,
 get_person_name(ctc.person_id_responsible) responsible, --节点负责人
 get_person_name(ctc.person_id_design) design, --设计人
 get_person_name(ctc.person_id_proofread) proofread, --校核人
 get_person_name(ctc.person_id_audit) auditer, --审核人
 get_person_name(ctc.person_id_approve) approve, --审定人
 get_person_name(ctc.attribute11) sqr
 */
