package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2017/2/16.
 */
public class ProjectWorkHoursDTO {
    private Long personId;
    private String fullName;
    private Float manageHours;
    private Float serverHours;
    private Float designHours;

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Float getManageHours() {
        return manageHours;
    }

    public void setManageHours(Float manageHours) {
        this.manageHours = manageHours;
    }

    public Float getServerHours() {
        return serverHours;
    }

    public void setServerHours(Float serverHours) {
        this.serverHours = serverHours;
    }

    public Float getDesignHours() {
        return designHours;
    }

    public void setDesignHours(Float designHours) {
        this.designHours = designHours;
    }
}
