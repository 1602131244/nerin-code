package com.nerin.nims.opt.nbcc.module;

import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Administrator on 2016/7/18.
 */
public class TaskResponsibleEntity implements ORAData {

    private long projectId; //项目ID
    private long roleId; // 项目角色ID
    private String roleName; // 项目角色名称
    private String specialty; //专业
    private String specialtyName; //专业名称
    private long personIdResponsible; //节点负责人的ID
    private String personNameResponsible; //节点负责人的名称

    public long getPersonIdResponsible() {
        return personIdResponsible;
    }

    public void setPersonIdResponsible(long personIdResponsible) {
        this.personIdResponsible = personIdResponsible;
    }

    public String getPersonNameResponsible() {
        return personNameResponsible;
    }

    public void setPersonNameResponsible(String personNameResponsible) {
        this.personNameResponsible = personNameResponsible;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }


    public static final String _ORACLE_TYPE_NAME = "APPS.NBCC_TASK_RESPONSIBLE_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.NUMBER , OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public TaskResponsibleEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getProjectId());
        _struct.setAttribute(1, this.getRoleId());
        _struct.setAttribute(2, this.getRoleName());
        _struct.setAttribute(3, this.getSpecialty());
        _struct.setAttribute(4, this.getSpecialtyName());
        _struct.setAttribute(5, this.getPersonIdResponsible());
        _struct.setAttribute(6, this.getPersonNameResponsible());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }

}
