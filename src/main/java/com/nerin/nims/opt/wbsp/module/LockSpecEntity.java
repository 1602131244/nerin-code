package com.nerin.nims.opt.wbsp.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2016/7/14.
 */
public class LockSpecEntity extends OracleBaseEntity implements ORAData {
    private String specId;
    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public static final String _ORACLE_TYPE_NAME = "WBSP_LOCK_SPEC_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public LockSpecEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getSpecId());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
