package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class PaHistoryProjectDTO {
    private long id;//行ID
    private long projID;//行ID
    private String name;
    private String state;
    private Date time;
    private String operator;
    private long parentId;

    public long getProjID() {
        return projID;
    }

    public void setProjID(long projID) {
        this.projID = projID;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
            this.state = state;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }
}
