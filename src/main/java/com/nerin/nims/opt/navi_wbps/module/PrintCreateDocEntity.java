package com.nerin.nims.opt.navi_wbps.module;

import com.nerin.nims.opt.navi_wbps.dto.printCreateDocDTO;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/8/29.
 */
public class PrintCreateDocEntity extends printCreateDocDTO implements ORAData {
    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_PRINT_DOC_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public PrintCreateDocEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getRequestId());
        _struct.setAttribute(1, this.getPlancnDrawingNum());
        _struct.setAttribute(2, this.getPlandrawingNum());


        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
