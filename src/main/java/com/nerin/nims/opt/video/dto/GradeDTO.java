package com.nerin.nims.opt.video.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class GradeDTO {
    private long vId;
    private long createdBy;
    private long grade;

    public long getvId() {
        return vId;
    }

    public void setvId(long vId) {
        this.vId = vId;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getGrade() {
        return grade;
    }

    public void setGrade(long grade) {
        this.grade = grade;
    }
}
