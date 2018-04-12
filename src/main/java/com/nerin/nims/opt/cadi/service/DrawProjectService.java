package com.nerin.nims.opt.cadi.service;

import com.nerin.nims.opt.cadi.dto.*;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 16/7/15.
 */
@Component
@Transactional
public class DrawProjectService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<UnitTaskDTO> getUnitTaskList(Long projectId,
                                             String phaseId
    ) {
        List<UnitTaskDTO> resultList = (List<UnitTaskDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cadi_utils1_pkg.get_unit_tasks_list(?,?,?)}";   //3
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (projectId != null)
                            cs.setLong(1, projectId);
                        else
                            cs.setBigDecimal(1, null);
                        cs.setString(2, phaseId);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<UnitTaskDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);
                        UnitTaskDTO tmp = null;
                        while (rs.next()) {
                            tmp = new UnitTaskDTO();
                            tmp.setProjectId(rs.getLong("project_id"));
                            tmp.setPhaseId(rs.getLong("phase_id"));
                            tmp.setPhaseCode(rs.getString("phase_code"));
                            tmp.setPhaseName(rs.getString("phase_name"));
                            tmp.setUnitTaskId(rs.getString("unit_task_id"));
                            tmp.setUnitTaskCode(rs.getString("unit_task_code"));
                            tmp.setUnitTaskName(rs.getString("unit_task_name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }


    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<SpecialityDTO> getSpecialityList(Long projectId,
                                                 String phaseId
    ) {
        List<SpecialityDTO> resultList = (List<SpecialityDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cadi_utils1_pkg.get_speciality_list(?,?,?)}";   //3
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (projectId != null)
                            cs.setLong(1, projectId);
                        else
                            cs.setBigDecimal(1, null);
                        cs.setString(2, phaseId);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<SpecialityDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);
                        SpecialityDTO tmp = null;
                        while (rs.next()) {
                            tmp = new SpecialityDTO();
                            tmp.setSpecialityCode(rs.getString("speciality_code"));
                            tmp.setSpecialityName(rs.getString("speciality_name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }


    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<DlvrDTO> getDlvrList(Long projectId,
                                     String specialityCode
    ) {
        List<DlvrDTO> resultList = (List<DlvrDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cadi_utils1_pkg.get_dlvrs_list(?,?,?)}";   //3
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (projectId != null)
                            cs.setLong(1, projectId);
                        else
                            cs.setBigDecimal(1, null);

                        cs.setString(2, specialityCode);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DlvrDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);
                        DlvrDTO tmp = null;
                        while (rs.next()) {
                            tmp = new DlvrDTO();
                            tmp.setProjectId(rs.getLong("project_id"));
                            tmp.setSpecialityCode(rs.getString("Speciality_Class"));
                            tmp.setSpecialityName(rs.getString("Speciality_Class_Number"));
                            tmp.setTaskType(rs.getString("task_type"));
                            tmp.setDlvrId(rs.getLong("dlvr_id"));
                            tmp.setDlvrCode(rs.getString("dlvr_code"));
                            tmp.setDlvrName(rs.getString("dlvr_name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }


    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<DrawStatusDTO> getDrawStatusList(

    ) {
        List<DrawStatusDTO> resultList = (List<DrawStatusDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cadi_utils1_pkg.get_draw_status_list(?)}";   //3
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DrawStatusDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);
                        DrawStatusDTO tmp = null;
                        while (rs.next()) {
                            tmp = new DrawStatusDTO();
                            tmp.setDrawStatusCode(rs.getString("draw_status_code"));
                            tmp.setDrawStatusName(rs.getString("draw_status_name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<DrawsCatalogTemDTO> getDrawsCatalogTem(Long projectId) {
        List<DrawsCatalogTemDTO> resultList = (List<DrawsCatalogTemDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cadi_utils1_pkg.get_draw_catalog_tem(?,?)}";   //2
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DrawsCatalogTemDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        DrawsCatalogTemDTO tmp = null;
                        while (rs.next()) {
                            tmp = new DrawsCatalogTemDTO();
                            tmp.setTemplateCode(rs.getString("template_code"));
                            tmp.setTemplateName(rs.getString("template_name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }


}
