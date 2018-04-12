package com.nerin.nims.opt.wbsp.dto;

import java.util.List;

/**
 * Created by Administrator on 2016/7/24.
 */
public class PaIndustriesResponDTO {

    private Long projID; //ID  projectIndustryId
    private List<PaIndustriesDTO> specialtyRatio;

    public Long getProjID() {
        return projID;
    }

    public void setProjID(Long projID) {
        this.projID = projID;
    }

    public List<PaIndustriesDTO> getSpecialtyRatio() {
        return specialtyRatio;
    }

    public void setSpecialtyRatio(List<PaIndustriesDTO> specialtyRatio) {
        this.specialtyRatio = specialtyRatio;
    }
}
