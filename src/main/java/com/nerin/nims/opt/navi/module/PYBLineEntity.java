package com.nerin.nims.opt.navi.module;

import com.nerin.nims.opt.navi.dto.ProjectYBlineDTO;
import oracle.jdbc.OracleTypes;
import oracle.sql.Datum;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;
import java.sql.Connection;
import java.sql.SQLException;
/**
 * Created by Administrator on 2017/2/24.
 */
public class PYBLineEntity extends ProjectYBlineDTO implements ORAData  {
    public static final String _ORACLE_TYPE_NAME = "APPS.NAVI_PROJECT_YB_LINE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER,OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.NUMBER,
            OracleTypes.NUMBER, OracleTypes.VARCHAR ,OracleTypes.NUMBER,OracleTypes.NUMBER,OracleTypes.NUMBER,OracleTypes.NUMBER,OracleTypes.NUMBER,
            OracleTypes.NUMBER,OracleTypes.NUMBER,OracleTypes.NUMBER,OracleTypes.NUMBER,OracleTypes.NUMBER};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];
    public PYBLineEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }
    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getHeadID());
        _struct.setAttribute(1, this.getLineId());
        _struct.setAttribute(2, this.getPhase());
        _struct.setAttribute(3, this.getPhaseWeight());
        _struct.setAttribute(4, this.getPhaseProgress());
        _struct.setAttribute(5, this.getSeq());
        _struct.setAttribute(6, this.getComments());
        _struct.setAttribute(7, this.getAttribute1());
        _struct.setAttribute(8, this.getAttribute2());
        _struct.setAttribute(9, this.getAttribute3());
        _struct.setAttribute(10, this.getAttribute4());
        _struct.setAttribute(11, this.getAttribute5());
        _struct.setAttribute(12, this.getAttribute6());
        _struct.setAttribute(13, this.getAttribute7());
        _struct.setAttribute(14, this.getAttribute8());
        _struct.setAttribute(15, this.getAttribute9());
        _struct.setAttribute(16, this.getAttribute10());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
