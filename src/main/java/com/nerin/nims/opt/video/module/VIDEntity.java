package com.nerin.nims.opt.video.module;

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
public class VIDEntity implements ORAData {
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public static final String _ORACLE_TYPE_NAME = "CUX_V_IDS_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.NUMBER};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public VIDEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getId());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
