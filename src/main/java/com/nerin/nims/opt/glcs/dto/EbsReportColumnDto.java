package com.nerin.nims.opt.glcs.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2017/11/16.
 */
public class EbsReportColumnDto extends OracleBaseDTO {
    private String columnCode;
    private String columnName;
    private String columnData;

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnData() {
        return columnData;
    }

    public void setColumnData(String columnData) {
        this.columnData = columnData;
    }
}
