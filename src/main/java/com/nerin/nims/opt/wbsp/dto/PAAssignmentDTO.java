package com.nerin.nims.opt.wbsp.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class PAAssignmentDTO {
    private String perName;
    private Long perId;
    private String perNum;
    private String specialty;
    private String duty;

    public String getPerName() {
        return perName;
    }

    public void setPerName(String perName) {
        this.perName = perName;
    }

    public Long getPerId() {
        return perId;
    }

    public void setPerId(Long perId) {
        this.perId = perId;
    }

    public String getPerNum() {
        return perNum;
    }

    public void setPerNum(String perNum) {
        this.perNum = perNum;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }
}
