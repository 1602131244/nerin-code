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
public class LockDlvrEntity extends OracleBaseEntity implements ORAData {
    private Long dlvrId;

    public Long getDlvrId() {
        return dlvrId;
    }

    public void setDlvrId(Long dlvrId) {
        this.dlvrId = dlvrId;
    }

    public static final String _ORACLE_TYPE_NAME = "NUMBER";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.NUMBER};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public LockDlvrEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getDlvrId());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
