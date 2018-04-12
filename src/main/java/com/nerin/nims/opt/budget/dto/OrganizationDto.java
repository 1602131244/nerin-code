package com.nerin.nims.opt.budget.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2016/8/30.
 */
public class OrganizationDto {

    private Long orgId;
    private String orgName;

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
