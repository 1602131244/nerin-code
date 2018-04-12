package com.nerin.nims.opt.cqs.dto;

/**
 * Created by user on 16/7/13.
 */
public class PipingMatlClassHdrDTO {



    private String pipingMatlClass;
    private String flangeFacing;
    private String basicMaterial;
    private String service;
    private double ca;
    private String designTempSource;
    private String designPresSource;



    public String getPipingMatlClass() {
        return pipingMatlClass;
    }

    public void setPipingMatlClass(String pipingMatlClass) {
        this.pipingMatlClass = pipingMatlClass;
    }

    public String getFlangeFacing() {
        return flangeFacing;
    }

    public void setFlangeFacing(String flangeFacing) {
        this.flangeFacing = flangeFacing;
    }

    public String getBasicMaterial() {
        return basicMaterial;
    }

    public void setBasicMaterial(String basicMaterial) {
        this.basicMaterial = basicMaterial;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public double getCa() {
        return ca;
    }

    public void setCa(double ca) {
        this.ca = ca;
    }

    public String getDesignTempSource() {
        return designTempSource;
    }

    public void setDesignTempSource(String designTempSource) {
        this.designTempSource = designTempSource;
    }

    public String getDesignPresSource() {
        return designPresSource;
    }

    public void setDesignPresSource(String designPresSource) {
        this.designPresSource = designPresSource;
    }

}
