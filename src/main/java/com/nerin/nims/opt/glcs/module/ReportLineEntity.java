package com.nerin.nims.opt.glcs.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/12/1.
 */
public class ReportLineEntity extends OracleBaseEntity implements ORAData {
    private Long headerId;
    private Long lineId;
    private String rowNumber;
    private String columnNumber;
    private double amount;

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

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_GLCS_REPORT_LINE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.DATE
    };
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public ReportLineEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getHeaderId());
        _struct.setAttribute(1, this.getLineId());
        _struct.setAttribute(2, this.getRowNumber());
        _struct.setAttribute(3, this.getColumnNumber());
        _struct.setAttribute(4, this.getAmount());
        _struct.setAttribute(5, this.getCreatedBy());
        _struct.setAttribute(6, this.getCreationDate());
        _struct.setAttribute(7, this.getLastUpdatedBy());
        _struct.setAttribute(8, this.getLastUpdateDate());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
