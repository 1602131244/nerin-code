package com.nerin.nims.opt.navi.module;

import com.nerin.nims.opt.navi.dto.ContractApLineDTO;
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
public class ContractApEntity extends ContractApLineDTO implements ORAData {


    public static final String _ORACLE_TYPE_NAME = "APPS.NAVI_CONTRACT_AP_LINE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER
            , OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.DATE, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR,OracleTypes.VARCHAR,OracleTypes.VARCHAR,OracleTypes.VARCHAR,OracleTypes.VARCHAR
            ,OracleTypes.VARCHAR,OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public ContractApEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getHeaderId());
        _struct.setAttribute(1, this.getLineId());
        _struct.setAttribute(2, this.getLineNumber());
        _struct.setAttribute(3, this.getLineStyle());
        _struct.setAttribute(4, this.getKheaderId());
        _struct.setAttribute(5, this.getStyleDesc());
        _struct.setAttribute(6, this.getLseId());
        _struct.setAttribute(7, this.getRcvType());
        _struct.setAttribute(8, this.getPlanapDate());
        _struct.setAttribute(9, this.getApPlan());
        _struct.setAttribute(10, this.getPlanapAmount());
        _struct.setAttribute(11, this.getCreationDate());
        _struct.setAttribute(12, this.getCreatedBy());
        _struct.setAttribute(13, this.getLastUpdateDate());
        _struct.setAttribute(14, this.getLastUpdatedBy());
        _struct.setAttribute(15, this.getLastUpdateLogin());
        _struct.setAttribute(16, this.getAttribute1());
        _struct.setAttribute(18, this.getAttribute3());
        _struct.setAttribute(19, this.getAttribute4());
        _struct.setAttribute(20, this.getAttribute5());
        _struct.setAttribute(21, this.getAttribute6());
        _struct.setAttribute(22, this.getAttribute7());
        _struct.setAttribute(23, this.getAttribute8());
        _struct.setAttribute(24, this.getAttribute9());
        _struct.setAttribute(25, this.getAttribute10());
        _struct.setAttribute(26, this.getAttribute11());
        _struct.setAttribute(27, this.getAttribute12());
        _struct.setAttribute(28, this.getAttribute13());
        _struct.setAttribute(17, this.getAttribute2());
        _struct.setAttribute(29, this.getAttribute14());
        _struct.setAttribute(30, this.getAttribute15());
        _struct.setAttribute(31, this.getMilestoneFlag());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
