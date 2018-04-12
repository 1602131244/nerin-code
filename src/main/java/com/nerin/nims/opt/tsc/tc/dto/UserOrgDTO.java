package com.nerin.nims.opt.tsc.tc.dto;

/**
 * Created by yinglgu on 11/14/2016.
 */
public class UserOrgDTO {

    private Long organizationId;//   OUT NUMBER,
    private String organizationName;// OUT VARCHAR2,
    private Long orgId;//            OUT NUMBER,
    private String orgName;//          OUT VARCHAR2

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
