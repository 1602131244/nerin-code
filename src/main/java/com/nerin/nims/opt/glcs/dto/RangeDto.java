package com.nerin.nims.opt.glcs.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

import java.util.Date;

/**
 * Created by Administrator on 2017/9/12.
 */
public class RangeDto extends OracleBaseDTO {
    private Long rangeId;
    private String rangeNumber;
    private String rangeName;
    private String description;
    private Long verson;
    private Date startDate;
    private Date endDate;
    private String createByName;
    private String lastByName;

    public Long getRangeId() {
        return rangeId;
    }

    public void setRangeId(Long rangeId) {
        this.rangeId = rangeId;
    }

    public String getRangeNumber() {
        return rangeNumber;
    }

    public void setRangeNumber(String rangeNumber) {
        this.rangeNumber = rangeNumber;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVerson() {
        return verson;
    }

    public void setVerson(Long verson) {
        this.verson = verson;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getLastByName() {
        return lastByName;
    }

    public void setLastByName(String lastByName) {
        this.lastByName = lastByName;
    }
}
