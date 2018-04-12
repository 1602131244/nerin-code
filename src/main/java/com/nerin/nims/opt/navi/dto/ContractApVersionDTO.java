package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2016/7/11.
 */
public class ContractApVersionDTO {
    private Long version;
    private String statusVersion;

    public String getStatusVersion() {
        return statusVersion;
    }

    public void setStatusVersion(String statusVersion) {
        this.statusVersion = statusVersion;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
