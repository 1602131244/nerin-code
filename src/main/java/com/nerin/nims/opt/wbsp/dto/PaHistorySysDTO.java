package com.nerin.nims.opt.wbsp.dto;

/**
 * Created by Administrator on 2016/7/25.
 */
public class PaHistorySysDTO {

    private String name;
    private String industry;
    private String dual;
    //   private String structureName;
    //   private long structureVerId;
    //  private long elementVersionId;

    public String getDual() {
        return dual;
    }

    public void setDual(String dual) {
        this.dual = dual;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }


}
