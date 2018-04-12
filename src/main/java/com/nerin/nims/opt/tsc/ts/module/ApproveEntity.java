package com.nerin.nims.opt.tsc.ts.module;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by yinglgu on 2016/11/30.
 */
public class ApproveEntity implements ORAData {
    private String approvedUser;

    public String getApprovedUser() {
        return approvedUser;
    }

    public void setApprovedUser(String approvedUser) {
        this.approvedUser = approvedUser;
    }

    public static final String _ORACLE_TYPE_NAME = "APPS.APPROVE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public ApproveEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getApprovedUser());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
