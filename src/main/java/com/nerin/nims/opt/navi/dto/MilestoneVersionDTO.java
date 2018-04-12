package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2016/7/7.
 */
public class MilestoneVersionDTO {
    private Long version;
    private String approveStatus;

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
