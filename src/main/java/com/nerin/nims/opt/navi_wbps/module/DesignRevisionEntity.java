package com.nerin.nims.opt.navi_wbps.module;

import com.nerin.nims.opt.navi_wbps.dto.drawingDTO;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/8/16.
 */
public class DesignRevisionEntity extends drawingDTO implements ORAData {
    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_DRAWING_REVISION_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR,OracleTypes.VARCHAR,
            OracleTypes.VARCHAR,OracleTypes.VARCHAR,OracleTypes.NUMBER,OracleTypes.NUMBER,OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public DesignRevisionEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getTzmc());
        _struct.setAttribute(1, this.getTh());
        _struct.setAttribute(2, this.getSjr());
        _struct.setAttribute(3, this.getJhr());
        _struct.setAttribute(4, this.getShr());
        _struct.setAttribute(5, this.getSdr());
        _struct.setAttribute(6, this.getZcgcs());
        _struct.setAttribute(7, this.getGzbh());
        _struct.setAttribute(8, this.getTzid());
        _struct.setAttribute(9, this.getUrl());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
