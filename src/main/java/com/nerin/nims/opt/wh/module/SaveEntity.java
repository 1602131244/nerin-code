package com.nerin.nims.opt.wh.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

/**
 * Created by Administrator on 2016/7/14.
 */
public class SaveEntity extends OracleBaseEntity implements ORAData {
    private Long projId;
    private Long taskId;
    private Long dlvrId;
    private float updateWorkHour;
    private String commit;

    public Long getProjId() {
        return projId;
    }

    public void setProjId(Long projId) {
        this.projId = projId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getDlvrId() {
        return dlvrId;
    }

    public void setDlvrId(Long dlvrId) {
        this.dlvrId = dlvrId;
    }

    public float getUpdateWorkHour() {
        return updateWorkHour;
    }

    public void setUpdateWorkHour(float updateWorkHour) {
        this.updateWorkHour = updateWorkHour;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_SAVE_WH_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.VARCHAR };
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public SaveEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getUpdateWorkHour());
        _struct.setAttribute(1, this.getProjId());
        _struct.setAttribute(2, this.getTaskId());
        _struct.setAttribute(3, this.getDlvrId());
        _struct.setAttribute(4, this.getCommit());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
