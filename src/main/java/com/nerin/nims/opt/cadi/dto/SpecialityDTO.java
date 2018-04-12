package com.nerin.nims.opt.cadi.dto;

/**
 * Created by user on 16/7/15.
 */
public class SpecialityDTO {
    public String getSpecialityCode() {
        return specialityCode;
    }

    public void setSpecialityCode(String specialityCode) {
        this.specialityCode = specialityCode;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    private String specialityCode; //专业代码
    private String specialityName; //专业名称

}
