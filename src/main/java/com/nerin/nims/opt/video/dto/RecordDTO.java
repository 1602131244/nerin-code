package com.nerin.nims.opt.video.dto;

import oracle.sql.DATE;

import java.sql.Date;

/**
 * Created by Administrator on 2016/8/3.
 */
public class RecordDTO {
    private float lastPosition;
    private Date lastUpdateTime;

    public float getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(float lastPosition) {
        this.lastPosition = lastPosition;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
