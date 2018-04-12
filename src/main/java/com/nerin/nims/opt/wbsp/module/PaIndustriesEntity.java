package com.nerin.nims.opt.wbsp.module;

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
public class PaIndustriesEntity extends OracleBaseEntity implements ORAData {
    private String code; //代字
    private String name;//专业
    private Long id; //ID
    private Long projectId;//项目编号
    private String industryCode;    //代字
    private double ratio;    //比例
    private String phaseCode;    //阶段
    private String enableFlag;   //启用标志位

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(String enableFlag) {
        this.enableFlag = enableFlag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public String getPhaseCode() {
        return phaseCode;
    }

    public void setPhaseCode(String phaseCode) {
        this.phaseCode = phaseCode;
    }

    public static final String _ORACLE_TYPE_NAME = "APPS.WBSP_PA_INDUSTRIES_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR
            , OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.DATE, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.DATE};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public PaIndustriesEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getCode());
        _struct.setAttribute(1, this.getName());
        _struct.setAttribute(2, this.getId());
        _struct.setAttribute(3, this.getProjectId());
        _struct.setAttribute(4, this.getIndustryCode());
        _struct.setAttribute(5, this.getRatio());
        _struct.setAttribute(6, this.getPhaseCode());
        _struct.setAttribute(7, this.getEnableFlag());
        _struct.setAttribute(8, this.getLastUpdateDate());
        _struct.setAttribute(9, this.getLastUpdatedBy());
        _struct.setAttribute(10, this.getLastUpdateLogin());
        _struct.setAttribute(11, this.getCreatedBy());
        _struct.setAttribute(12, this.getCreationDate());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
