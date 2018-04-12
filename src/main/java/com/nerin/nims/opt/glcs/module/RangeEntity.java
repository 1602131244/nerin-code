package com.nerin.nims.opt.glcs.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/13.
 */
public class RangeEntity extends OracleBaseEntity implements ORAData {
    private Long rangeId;
    private String rangeNumber;
    private String rangeName;
    private String description;
    private Long verson;
    private Date startDate;
    private Date endDate;


    public Long getRangeId() {
        return rangeId;
    }

    public void setRangeId(Long rangeId) {
        this.rangeId = rangeId;
    }

    public String getRangeNumber() {
        return rangeNumber;
    }

    public void setRangeNumber(String rangeNumber) {
        this.rangeNumber = rangeNumber;
    }

    public String getRangeName() {
        return rangeName;
    }

    public void setRangeName(String rangeName) {
        this.rangeName = rangeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getVerson() {
        return verson;
    }

    public void setVerson(Long verson) {
        this.verson = verson;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_GLCS_RANGE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER
            , OracleTypes.DATE, OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.NUMBER
            , OracleTypes.DATE
    };
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public RangeEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getRangeId());
        _struct.setAttribute(1, this.getRangeNumber());
        _struct.setAttribute(2, this.getRangeName());
        _struct.setAttribute(3, this.getDescription());
        _struct.setAttribute(4, this.getVerson());
        _struct.setAttribute(5, this.getStartDate());
        _struct.setAttribute(6, this.getEndDate());
        _struct.setAttribute(7, this.getCreatedBy());
        _struct.setAttribute(8, this.getCreationDate());
        _struct.setAttribute(9, this.getLastUpdatedBy());
        _struct.setAttribute(10, this.getLastUpdateDate());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }

}
