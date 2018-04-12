package com.nerin.nims.opt.glcs.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2017/10/19.
 */
public class EbsReportDto extends OracleBaseDTO {

    private Long reportId;
    private String reportName;
    private String reportDescription;
    private Long rowSetId;
    private String rowSetName;
    private Long columnSetId;
    private String columnSetName;

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    public void setReportDescription(String reportDescription) {
        this.reportDescription = reportDescription;
    }

    public Long getRowSetId() {
        return rowSetId;
    }

    public void setRowSetId(Long rowSetId) {
        this.rowSetId = rowSetId;
    }

    public String getRowSetName() {
        return rowSetName;
    }

    public void setRowSetName(String rowSetName) {
        this.rowSetName = rowSetName;
    }

    public Long getColumnSetId() {
        return columnSetId;
    }

    public void setColumnSetId(Long columnSetId) {
        this.columnSetId = columnSetId;
    }

    public String getColumnSetName() {
        return columnSetName;
    }

    public void setColumnSetName(String columnSetName) {
        this.columnSetName = columnSetName;
    }
}
