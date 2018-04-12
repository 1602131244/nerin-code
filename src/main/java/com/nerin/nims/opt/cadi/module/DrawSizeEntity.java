package com.nerin.nims.opt.cadi.module;

import com.nerin.nims.opt.cadi.dto.*;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2016/7/11.
 */
public class DrawSizeEntity extends DrawSizeDTO implements ORAData {


    public static final String _ORACLE_TYPE_NAME = "WCC_OCS.CAT_DRAWLIST_SIZE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.NUMBER, OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public DrawSizeEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getIndex());
        _struct.setAttribute(1, this.getPdrawnum());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
