package com.nerin.nims.opt.glcs.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2017/11/17.
 */
public class ReportLineDto extends OracleBaseDTO {
    private Long headerId;
    private Long lineId;
    private String rowNumber;
    private String columnNumber;
    private double amount;

    private String amount01;
    private String amount02;
    private String amount03;
    private String amount04;
    private String amount05;
    private String amount06;
    private String amount07;
    private String amount08;
    private String amount09;
    private String amount10;
    private String rowName;

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public String getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(String rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getColumnNumber() {
        return columnNumber;
    }

    public void setColumnNumber(String columnNumber) {
        this.columnNumber = columnNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getAmount01() {
        return amount01;
    }

    public void setAmount01(String amount01) {
        this.amount01 = amount01;
    }

    public String getAmount02() {
        return amount02;
    }

    public void setAmount02(String amount02) {
        this.amount02 = amount02;
    }

    public String getAmount03() {
        return amount03;
    }

    public void setAmount03(String amount03) {
        this.amount03 = amount03;
    }

    public String getAmount04() {
        return amount04;
    }

    public void setAmount04(String amount04) {
        this.amount04 = amount04;
    }

    public String getAmount05() {
        return amount05;
    }

    public void setAmount05(String amount05) {
        this.amount05 = amount05;
    }

    public String getAmount06() {
        return amount06;
    }

    public void setAmount06(String amount06) {
        this.amount06 = amount06;
    }

    public String getAmount07() {
        return amount07;
    }

    public void setAmount07(String amount07) {
        this.amount07 = amount07;
    }

    public String getAmount08() {
        return amount08;
    }

    public void setAmount08(String amount08) {
        this.amount08 = amount08;
    }

    public String getAmount09() {
        return amount09;
    }

    public void setAmount09(String amount09) {
        this.amount09 = amount09;
    }

    public String getAmount10() {
        return amount10;
    }

    public void setAmount10(String amount10) {
        this.amount10 = amount10;
    }

    public String getRowName() {
        return rowName;
    }

    public void setRowName(String rowName) {
        this.rowName = rowName;
    }
}
