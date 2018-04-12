package com.nerin.nims.opt.cqs.dto;

/**
 * Created by user on 16/7/13.
 */
public class PtRatingTblDTO {

    private Long ptOrderNumber;
    private Long ptId;
    private String pipingMatlClass;
    private Double temperature;
    private Double pressure;

    public Long getPtOrderNumber() {
        return ptOrderNumber;
    }

    public void setPtOrderNumber(Long ptOrderNumber) {
        this.ptOrderNumber = ptOrderNumber;
    }

    public Long getPtId() {
        return ptId;
    }

    public void setPtId(Long ptId) {
        this.ptId = ptId;
    }

    public String getPipingMatlClass() {
        return pipingMatlClass;
    }

    public void setPipingMatlClass(String pipingMatlClass) {
        this.pipingMatlClass = pipingMatlClass;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }
}
