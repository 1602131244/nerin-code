package com.nerin.nims.opt.video.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class SaveRecordDTO {
    private float lastPosition;
    private long vId;
    private long createdBy;

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

    public float getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(float lastPosition) {
        this.lastPosition = lastPosition;
    }
}
