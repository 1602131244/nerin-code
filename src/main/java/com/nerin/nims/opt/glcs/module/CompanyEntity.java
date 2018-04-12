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
 * Created by Administrator on 2017/9/19.
 */
public class CompanyEntity extends OracleBaseEntity implements ORAData {

    private Long rangeId;
    private Long rangeVerson;
    private Long unitId;
    private String companyName;
    private String description;
    private Long ledgerId;
    private String companyCode;

    public Long getRangeId() {
        return rangeId;
    }

    public void setRangeId(Long rangeId) {
        this.rangeId = rangeId;
    }

    public Long getRangeVerson() {
        return rangeVerson;
    }

    public void setRangeVerson(Long rangeVerson) {
        this.rangeVerson = rangeVerson;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_GLCS_COMPANY_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.NUMBER
            , OracleTypes.DATE
    };
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public CompanyEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getRangeId());
        _struct.setAttribute(1, this.getRangeVerson());
        _struct.setAttribute(2, this.getUnitId());
        _struct.setAttribute(3, this.getCompanyName());
        _struct.setAttribute(4, this.getDescription());
        _struct.setAttribute(5, this.getLedgerId());
        _struct.setAttribute(6, this.getCompanyCode());
        _struct.setAttribute(7, this.getCreatedBy());
        _struct.setAttribute(8, this.getCreationDate());
        _struct.setAttribute(9, this.getLastUpdatedBy());
        _struct.setAttribute(10, this.getLastUpdateDate());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
