package com.nerin.nims.opt.navi_wbps.module;

import com.nerin.nims.opt.navi_wbps.dto.printCreateLineDTO;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/8/28.
 */
public class PrintCreateLineEntity  extends printCreateLineDTO implements ORAData {
    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_PRINT_LINE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR,OracleTypes.VARCHAR,
            OracleTypes.VARCHAR,OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public PrintCreateLineEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getDivEquip());
        _struct.setAttribute(1, this.getDrawnums());
        _struct.setAttribute(2, this.getDrawcount());
        _struct.setAttribute(3, this.getxSize());
        _struct.setAttribute(4, this.getPlancnDrawingNum());
        _struct.setAttribute(5, this.getPlandrawingNum());
        _struct.setAttribute(6, this.getSpecialityName());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
