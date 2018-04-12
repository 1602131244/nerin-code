package com.nerin.nims.opt.navi.module;

import com.nerin.nims.opt.navi.dto.MilestoneDto;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.ORAData;
import oracle.sql.Datum;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2016/7/8.
 */
public class MilestoneEntity extends MilestoneDto implements ORAData{

    public static final String _ORACLE_TYPE_NAME = "APPS.NAVI_MILESTONE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.DATE
            , OracleTypes.DATE , OracleTypes.DATE, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.DATE
            , OracleTypes.NUMBER, OracleTypes.DATE, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER,OracleTypes.NUMBER};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public MilestoneEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getMilestoneId());
        _struct.setAttribute(1, this.getProjectId());
        _struct.setAttribute(2, this.getName());
        _struct.setAttribute(3, this.getPhaseWeight());
        _struct.setAttribute(4, this.getPlanStartDate());
        _struct.setAttribute(5, this.getPlanEndDate());
        _struct.setAttribute(6, this.getActualCompleteDate());
        _struct.setAttribute(7, this.getMilestoneStatus());
        _struct.setAttribute(8, this.getApproveStatus());
        _struct.setAttribute(9, this.getVersion());
        _struct.setAttribute(10, this.getParentId());
        _struct.setAttribute(11, this.getSeqNum());
        _struct.setAttribute(12, this.getCreatedBy());
        _struct.setAttribute(13, this.getCreationDate());
        _struct.setAttribute(14, this.getLastUpdatedBy());
        _struct.setAttribute(15, this.getLastUpdateDate());
        _struct.setAttribute(16, this.getAttribute1());
        _struct.setAttribute(17, this.getAttribute2());
        _struct.setAttribute(18, this.getAttribute3());
        _struct.setAttribute(19, this.getAttribute4());
        _struct.setAttribute(20, this.getAttribute5());
        _struct.setAttribute(21, this.getAttribute6());
        _struct.setAttribute(22, this.getAttribute7());
        _struct.setAttribute(23, this.getAttribute8());
        _struct.setAttribute(24, this.getAttribute9());
        _struct.setAttribute(25, this.getAttribute10());
        _struct.setAttribute(26, this.getPhaseCode());
        _struct.setAttribute(27, this.getLineId());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
