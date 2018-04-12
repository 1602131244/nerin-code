package com.nerin.nims.opt.video.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class CommentDTO {
    private long vId;
    private long createdBy;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
