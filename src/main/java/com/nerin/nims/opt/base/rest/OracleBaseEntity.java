package com.nerin.nims.opt.base.rest;

import java.util.Date;

/**
 * Created by yinglgu on 6/20/2016.
 */
public class OracleBaseEntity {
    public Date lastUpdateDate;//最后更新日期
    public Long lastUpdatedBy; //最后更新用户ID
    public Long lastUpdateLogin;//最后更新登录ID
    public Long createdBy;//创建人ID
    public Date creationDate;//创建日期

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Long getLastUpdateLogin() {
        return lastUpdateLogin;
    }

    public void setLastUpdateLogin(Long lastUpdateLogin) {
        this.lastUpdateLogin = lastUpdateLogin;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
