package com.nerin.nims.opt.navi.service;

import com.nerin.nims.opt.app.web.rest.dto.DataTableDTO;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/7.
 */
@Component
@Transactional
public class PinfoService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取项目信息
     * @param projectId 项目ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getProjectInfo(long projectId) {
        DataTableDTO dt = new DataTableDTO();
        List<ProjectInfoDTO> resultList = (List<ProjectInfoDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_project_info_pkg.get_project_info(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectInfoDTO> results = new ArrayList<ProjectInfoDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ProjectInfoDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectInfoDTO();
                            tmp.setProjectId(rs.getLong("project_id"));
                            tmp.setProjectNumber(rs.getString("project_number"));
                            tmp.setProjectName(rs.getString("project_name"));
                            tmp.setManager(rs.getString("manager"));
                            tmp.setProjectLongName(rs.getString("project_long_name"));
                            tmp.setProjectClass(rs.getString("project_class"));
                            tmp.setOrgName(rs.getString("org_name"));
                            tmp.setOrgId(rs.getLong("org_id"));
                            tmp.setExeName(rs.getString("exe_name"));
                            tmp.setExeId(rs.getLong("exe_id"));
                            tmp.setProjStartDate(rs.getDate("proj_start_date"));
                            tmp.setProjEndDate(rs.getDate("proj_end_date"));
                            tmp.setStartDate(rs.getDate("start_date"));
                            tmp.setCompletionDate(rs.getDate("completion_date"));
                            tmp.setCustomer(rs.getString("customer"));
                            tmp.setProjectStatus(rs.getString("project_status"));
                            tmp.setProjectStatusCode(rs.getString("project_status_code"));
                            tmp.setOverdue(rs.getString("overdue"));
                            tmp.setSysDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }

    /**
     * 获取项目人员信息
     * @param projectId  合同ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getProjectpersonInfo(Long projectId,Long inpage,Long insize) {
        DataTableDTO dt = new DataTableDTO();
        List<ProjectPersonInfoDTO> resultList = (List<ProjectPersonInfoDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_project_info_pkg.get_project_person(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setLong(2, inpage);// 设置输入参数的值
                        cs.setLong(3, insize);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectPersonInfoDTO> results = new ArrayList<ProjectPersonInfoDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        ProjectPersonInfoDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectPersonInfoDTO();
                            tmp.setFullName(rs.getString("full_name"));
                            tmp.setAssignment_name(rs.getString("assignment_name"));
                            tmp.setSpecaility_name(rs.getString("specaility_name"));
                            tmp.setEmail_address(rs.getString("email_address"));
                            tmp.setM_phone(rs.getString("m_phone"));
                            tmp.setWork_phone(rs.getString("work_phone"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }

    /**
     * 获取管理工时
     * @param projectId  合同ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public CDataTableDTO getManageHours(Long projectId,Long userId,Long ismanager) {
        CDataTableDTO dt = new CDataTableDTO();
        List<ProjectManageHoursDTO> resultList = (List<ProjectManageHoursDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_project_info_pkg.get_project_manage_hours(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setLong(2, userId);// 设置输入参数的值
                        cs.setLong(3, ismanager);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.CURSOR );// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectManageHoursDTO> results = new ArrayList<ProjectManageHoursDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setTotal(cs.getLong(4));
                        ProjectManageHoursDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectManageHoursDTO();
                            tmp.setFullName(rs.getString("full_name"));
                            tmp.setMeasure(rs.getFloat("measure"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }

    /**
     * 获取施工服务工时
     * @param projectId  合同ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public CDataTableDTO getServerHours(Long projectId,Long userId,Long ismanager) {
        CDataTableDTO dt = new CDataTableDTO();
        List<ProjectManageHoursDTO> resultList = (List<ProjectManageHoursDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_project_info_pkg.get_project_server_hours(?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setLong(2, userId);// 设置输入参数的值
                        cs.setLong(3, ismanager);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR );// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectManageHoursDTO> results = new ArrayList<ProjectManageHoursDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        ProjectManageHoursDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectManageHoursDTO();
                            tmp.setFullName(rs.getString("full_name"));
                            tmp.setMeasure(rs.getFloat("measure"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }

    /**
     * 获取人员工时，包括管理，服务，设计工时
     * @param projectId  合同ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public CDataTableDTO getWorkHours(Long projectId,Long userId,Long ismanager) {
        CDataTableDTO dt = new CDataTableDTO();
        List<ProjectWorkHoursDTO> resultList = (List<ProjectWorkHoursDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_project_info_pkg.get_project_work_hours(?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setLong(2, userId);// 设置输入参数的值
                        cs.setLong(3, ismanager);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR );// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectWorkHoursDTO> results = new ArrayList<ProjectWorkHoursDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        //dt.setTotal(cs.getLong(4));
                        ProjectWorkHoursDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectWorkHoursDTO();
                            tmp.setFullName(rs.getString("full_name"));
                            tmp.setManageHours(rs.getFloat("manage_hours"));
                            tmp.setDesignHours(rs.getFloat("design_hours"));
                            tmp.setServerHours(rs.getFloat("server_hours"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }
}
