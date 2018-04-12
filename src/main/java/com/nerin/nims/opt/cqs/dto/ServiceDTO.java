package com.nerin.nims.opt.cqs.dto;

/**
 * Created by user on 16/6/28.
 */
public class ServiceDTO {

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
    //适用介质list
    private String service;

    private String category;
    private String pyszm;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPyszm() {
        return pyszm;
    }

    public void setPyszm(String pyszm) {
        this.pyszm = pyszm;
    }
}
