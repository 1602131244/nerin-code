package com.nerin.nims.opt.navi.service;

import com.nerin.nims.opt.navi.dto.*;
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
 * Created by user on 16/7/14.
 */
@Component
@Transactional
public class ProjectService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<MyProjectsListDTO> getMyProjectsListDTO(Long userId,
                                             String paramText,
                                             Long inPage,
                                             Long inSize
                                             )
    {
        List<MyProjectsListDTO> resultList = (List<MyProjectsListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_proj_pkg.my_project_list(?,?,?,?,?,?)}";   //6
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1,userId);
                        cs.setString(2,paramText);
                        cs.setLong(3,inPage);
                        cs.setLong(4,inSize);
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<MyProjectsListDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);
                        MyProjectsListDTO tmp = null;
                        while (rs.next()) {
                            tmp = new MyProjectsListDTO();
                            tmp.setProjectId(rs.getLong("project_id"));
                            tmp.setProjectNumber(rs.getString("project_number"));
                            tmp.setProjectName(rs.getString("project_name"));
                            tmp.setProjectLongName(rs.getString("project_long_name"));
                            tmp.setCustomer(rs.getString("customer"));
                            tmp.setProjectStatusName(rs.getString("project_status"));
                            tmp.setProjectType(rs.getString("project_type"));
                            tmp.setProjectClass(rs.getString("project_class"));
                            tmp.setProjectManager(rs.getString("project_manager"));
                            tmp.setProjectOrgName(rs.getString("project_org_name"));
                            tmp.setProjectExeName(rs.getString("project_exe_name"));
                            tmp.setProjStartDate(rs.getDate("proj_start_date"));
                            tmp.setProjEndDate(rs.getDate("proj_end_date"));
                            tmp.setStartDate(rs.getDate("start_date"));
                            tmp.setCompletionDate(rs.getDate("completion_date"));
                            tmp.setOverdue(rs.getString("overdue"));
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
    public List<ProjectPhasesDTO> getProjectPhasesDTO(Long projectId, String docType)
    {
        List<ProjectPhasesDTO> resultList = (List<ProjectPhasesDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cadi_utils1_pkg.project_phase(?,?,?)}";   //2
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1,projectId);
                        cs.setString(2, docType);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectPhasesDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);
                        ProjectPhasesDTO tmp = null;
                        while (rs.next()) {
                            tmp = new ProjectPhasesDTO();
                            tmp.setProjectId(rs.getLong("project_id"));
                            tmp.setProjElementId(rs.getLong("proj_element_id"));
                            tmp.setPhaseCode(rs.getString("phase_code"));
                            tmp.setPhaseName(rs.getString("phase_name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }
}
