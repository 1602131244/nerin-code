package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class LockSpecReqDTO {
    private Long projID;
    private String phaseID;
    private List<LockSpecDTO> lockSpecs = new ArrayList<LockSpecDTO>();

    public Long getProjID() {
        return projID;
    }

    public void setProjID(Long projID) {
        this.projID = projID;
    }

    public String getPhaseID() {
        return phaseID;
    }

    public void setPhaseID(String phaseID) {
        this.phaseID = phaseID;
    }

    public List<LockSpecDTO> getLockSpecs() {
        return lockSpecs;
    }

    public void setLockSpecs(List<LockSpecDTO> lockSpecs) {
        this.lockSpecs = lockSpecs;
    }
}
