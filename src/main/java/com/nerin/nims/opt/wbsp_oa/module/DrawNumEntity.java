package com.nerin.nims.opt.wbsp_oa.module;

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
public class DrawNumEntity implements ORAData {
    private String drawNum;

    public String getDrawNum() {
        return drawNum;
    }

    public void setDrawNum(String drawNum) {
        this.drawNum = drawNum;
    }

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_DRAWNUMS_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public DrawNumEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getDrawNum());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
