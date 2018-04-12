package com.nerin.nims.opt.wbsp_oa.module;

import com.nerin.nims.opt.wbsp_oa.dto.DlvrDetailDTO;
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
public class DlvrDetailEntity extends DlvrDetailDTO implements ORAData {
    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_WBSPOA_DT_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.VARCHAR,OracleTypes.VARCHAR,OracleTypes.VARCHAR,OracleTypes.NUMBER,OracleTypes.NUMBER,OracleTypes.VARCHAR,OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public DlvrDetailEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getZxhmx());
        _struct.setAttribute(1, this.getDivEquip());
        _struct.setAttribute(2, this.getDrawNumAbbr());
        _struct.setAttribute(3, this.getDrawCount());
        _struct.setAttribute(4, this.getDlvrId());
        _struct.setAttribute(5, this.getDlvrName());
        _struct.setAttribute(6, this.getDescr());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
