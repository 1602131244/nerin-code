package com.nerin.nims.opt.cqs.dto;

/**
 * Created by user on 16/7/13.
 */
public class PipeThicknessTblDTO {

    private Long pipeOrderNumber;
    private Long pipeId;
    private String pipingMatlClass;
    private Double pipeDn;
    private Double pipeOuter;
    private Double pipeThickness;

    public Long getPipeOrderNumber() {
        return pipeOrderNumber;
    }

    public void setPipeOrderNumber(Long pipeOrderNumber) {
        this.pipeOrderNumber = pipeOrderNumber;
    }

    public Long getPipeId() {
        return pipeId;
    }

    public void setPipeId(Long pipeId) {
        this.pipeId = pipeId;
    }

    public String getPipingMatlClass() {
        return pipingMatlClass;
    }

    public void setPipingMatlClass(String pipingMatlClass) {
        this.pipingMatlClass = pipingMatlClass;
    }

    public Double getPipeDn() {
        return pipeDn;
    }

    public void setPipeDn(Double pipeDn) {
        this.pipeDn = pipeDn;
    }

    public Double getPipeOuter() {
        return pipeOuter;
    }

    public void setPipeOuter(Double pipeOuter) {
        this.pipeOuter = pipeOuter;
    }

    public Double getPipeThickness() {
        return pipeThickness;
    }

    public void setPipeThickness(Double pipeThickness) {
        this.pipeThickness = pipeThickness;
    }
}
