package com.nerin.nims.opt.cqs.dto;

/**
 * Created by user on 16/7/13.
 */
public class BranchConnectTblDTO {

    private Long connOrderNumber;
    private Long connectionId;
    private String pipingMatlClass;
    private Double branchDn;
    private Double headerDn;
    private String connectionType;

    public Long getConnOrderNumber() {
        return connOrderNumber;
    }

    public void setConnOrderNumber(Long connOrderNumber) {
        this.connOrderNumber = connOrderNumber;
    }

    public Long getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }

    public String getPipingMatlClass() {
        return pipingMatlClass;
    }

    public void setPipingMatlClass(String pipingMatlClass) {
        this.pipingMatlClass = pipingMatlClass;
    }

    public Double getBranchDn() {
        return branchDn;
    }

    public void setBranchDn(Double branchDn) {
        this.branchDn = branchDn;
    }

    public Double getHeaderDn() {
        return headerDn;
    }

    public void setHeaderDn(Double headerDn) {
        this.headerDn = headerDn;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }
}
