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
public class CommentEntity implements ORAData {
    private String description;
    private long id;
    private long vId;
    private long createdBy;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_V_COMMENT_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.NUMBER,OracleTypes.NUMBER, OracleTypes.VARCHAR,OracleTypes.NUMBER};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public CommentEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getId());
        _struct.setAttribute(1, this.getvId());
        _struct.setAttribute(2, this.getDescription());
        _struct.setAttribute(3, this.getCreatedBy());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
