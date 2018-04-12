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
public class FavoriteEntity implements ORAData {
    private long vId;
    private long createdBy;

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public long getvId() {
        return vId;
    }

    public void setvId(long vId) {
        this.vId = vId;
    }

    public static final String _ORACLE_TYPE_NAME = "CUX_V_FAVOR_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.NUMBER, OracleTypes.NUMBER};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public FavoriteEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getvId());
        _struct.setAttribute(1, this.getCreatedBy());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
