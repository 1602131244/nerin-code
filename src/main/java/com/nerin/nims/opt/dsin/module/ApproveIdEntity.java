package com.nerin.nims.opt.dsin.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
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
public class ApproveIdEntity implements ORAData {
    private Long id;
    private Long tabId;
    private String url;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTabId() {
        return tabId;
    }

    public void setTabId(Long tabId) {
        this.tabId = tabId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public static final String _ORACLE_TYPE_NAME = "APPS.CUX_DESIGNINPUT_APPROVE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.NUMBER, OracleTypes.NUMBER,OracleTypes.VARCHAR };
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public ApproveIdEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getId());
        _struct.setAttribute(1, this.getTabId());
        _struct.setAttribute(2, this.getUrl());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
