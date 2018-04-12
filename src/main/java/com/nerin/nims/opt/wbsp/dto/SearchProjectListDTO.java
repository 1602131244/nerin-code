package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class SearchProjectListDTO {
    private Long projID;
    private String projNumber;
    private String projName;
    private String projLongName;
    private String type;
    //private String projectStatusCode;
    private String status;
    private Date startDate;
    private Date endDate;
    //private Long projectCarryingOutOrgId;
    private String orgName;
    private String mgr;
    //private Long projectManagerId;
    private String customer;
    //private Long projectCustomerId;
    private List<PaPhaseListDTO> phaseList;


    public Long getProjID() {
        return projID;
    }

    public void setProjID(Long projID) {
        this.projID = projID;
    }

    public String getProjNumber() {
        return projNumber;
    }

    public void setProjNumber(String projNumber) {
        this.projNumber = projNumber;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjLongName() {
        return projLongName;
    }

    public void setProjLongName(String projLongName) {
        this.projLongName = projLongName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getMgr() {
        return mgr;
    }

    public void setMgr(String mgr) {
        this.mgr = mgr;
    }


    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }


    public List<PaPhaseListDTO> getPhaseList() {
        return phaseList;
    }

    public void setPhaseList(List<PaPhaseListDTO> phaseList) {
        this.phaseList = phaseList;
    }
}
