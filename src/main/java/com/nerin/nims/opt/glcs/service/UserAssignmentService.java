package com.nerin.nims.opt.glcs.service;

import com.nerin.nims.opt.aria.common.EventParm;
import com.nerin.nims.opt.cqs.dto.DataTablesDTO;
import com.nerin.nims.opt.glcs.dto.EbsReportDto;
import com.nerin.nims.opt.glcs.dto.UserAssignmentDto;
import com.nerin.nims.opt.glcs.dto.EbsUserDto;
import com.nerin.nims.opt.glcs.module.UserAssignmentEntity;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/19.
 */
@Component
@Transactional
public class UserAssignmentService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    private UserAssignmentEntity userAssignmentDtoToEntity  (UserAssignmentDto userAssignmentDto) {
        UserAssignmentEntity entity = new UserAssignmentEntity();
        try {
            PropertyUtils.copyProperties(entity, userAssignmentDto);
            entity.setAssignmentId(userAssignmentDto.getAssignmentId());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    /**
     * 获取用户列表
     * @param userId
     * @param userNumber
     * @param userName
     * @param usersearch
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getUserList(Long userId, String userNumber, String userName, String usersearch, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<EbsUserDto> resultList = (List<EbsUserDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_USER_ASSIGNMENT_PKG.GET_USER(?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == userId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, userId);
                        cs.setString(2, userNumber);// 设置输入参数的值
                        cs.setString(3, userName);// 设置输入参数的值
                        cs.setString(4, usersearch);// 设置输入参数的值
                        cs.setLong(5, pageNo);// 设置输入参数的值
                        cs.setLong(6, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(7, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(8, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<EbsUserDto> results = new ArrayList<EbsUserDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(7);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(8));
                        EbsUserDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new EbsUserDto();
                            tmp.setUserId(rs.getLong("USER_ID"));
                            tmp.setUserNumber(rs.getString("USER_NUMBER"));
                            tmp.setDescription(rs.getString("DESCRIPTION"));
                            tmp.setUserName(rs.getString("USER_NAME"));
                            tmp.setStartdate(rs.getDate("START_DATE"));
                            tmp.setEndDate(rs.getDate("END_DATE"));
                            tmp.setEmployeeId(rs.getLong("EMPLOYEE_ID"));

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
     * 查询报表
     * @param reportId
     * @param reportName
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getEbsReportList(Long reportId, String reportName, String checkSign, String checkCreate, Long userId, Long rangeId,
                                          Long rangeVerson, Long  unitId, Long ledgerId, String companyCode, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<EbsReportDto> resultList = (List<EbsReportDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_USER_ASSIGNMENT_PKG.GET_REPORT(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == reportId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, reportId);
                        cs.setString(2, reportName);// 设置输入参数的值
                        cs.setString(3, checkSign);// 设置输入参数的值
                        cs.setString(4, checkCreate);// 设置输入参数的值
                        cs.setLong(5, userId);
                        if (null == rangeId)
                            cs.setBigDecimal(6, null);
                        else
                            cs.setLong(6, rangeId);
                        if (null == rangeVerson)
                            cs.setBigDecimal(7, null);
                        else
                            cs.setLong(7, rangeVerson);
                        if (null == unitId)
                            cs.setBigDecimal(8, null);
                        else
                            cs.setLong(8, unitId);
                        if (null == ledgerId)
                            cs.setBigDecimal(9, null);
                        else
                            cs.setLong(9, ledgerId);
                        cs.setString(10, companyCode);// 设置输入参数的值
                        cs.setLong(11, pageNo);// 设置输入参数的值
                        cs.setLong(12, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(13, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(14, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<EbsReportDto> results = new ArrayList<EbsReportDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(13);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(14));
                        EbsReportDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new EbsReportDto();
                            tmp.setReportId(rs.getLong("REPORT_ID"));
                            tmp.setReportName(rs.getString("NAME"));
                            tmp.setReportDescription(rs.getString("DESCRIPTION"));
                            tmp.setRowSetId(rs.getLong("ROW_SET_ID"));
                            tmp.setRowSetName(rs.getString("ROW_SET"));
                            tmp.setColumnSetId(rs.getLong("COLUMN_SET_ID"));
                            tmp.setColumnSetName(rs.getString("COLUMN_SET"));

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
     * 查询用户权限分配列表
     * @param assignmentId
     * @param userId
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param ledgerId
     * @param companyCode
     * @param reportId
     * @param search
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getUserAssignmentList(Long assignmentId, Long userId, Long rangeId, Long rangeVerson, Long unitId, Long ledgerId,
                                      String companyCode, Long reportId, String search, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<UserAssignmentDto> resultList = (List<UserAssignmentDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_USER_ASSIGNMENT_PKG.GET_USER_ASSIGNMENT(?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == assignmentId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, assignmentId);
                        if (null == userId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, userId);
                        if (null == rangeId)
                            cs.setBigDecimal(3, null);
                        else
                            cs.setLong(3, rangeId);
                        if (null == rangeVerson)
                            cs.setBigDecimal(4, null);
                        else
                            cs.setLong(4, rangeVerson);
                        if (null == unitId)
                            cs.setBigDecimal(5, null);
                        else
                            cs.setLong(5, unitId);
                        if (null == ledgerId)
                            cs.setBigDecimal(6, null);
                        else
                            cs.setLong(6, ledgerId);
                        cs.setString(7, companyCode);// 设置输入参数的值
                        if (null == reportId)
                            cs.setBigDecimal(8, null);
                        else
                            cs.setLong(8, reportId);
                        cs.setString(9, search);// 设置输入参数的值
                        cs.setLong(10, pageNo);// 设置输入参数的值
                        cs.setLong(11, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(12, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(13, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<UserAssignmentDto> results = new ArrayList<UserAssignmentDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(12);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(13));
                        UserAssignmentDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new UserAssignmentDto();
                            tmp.setAssignmentId(rs.getLong("assignment_ID"));
                            tmp.setUserId(rs.getLong("USER_ID"));
                            tmp.setRangeId(rs.getLong("RANGE_ID"));
                            tmp.setRangeVerson(rs.getLong("RANGE_VERSON"));
                            tmp.setUnitId(rs.getLong("UNIT_ID"));
                            tmp.setLedgerId(rs.getLong("LEDGER_ID"));
                            tmp.setCompanyCode(rs.getString("COMPANY_CODE"));
                            tmp.setReportId(rs.getLong("REPORT_ID"));
                            tmp.setSelectFlag(rs.getString("SELECT_FLAG"));
                            tmp.setInsertFlag(rs.getString("INSERT_FLAG"));
                            tmp.setUpdateFlag(rs.getString("UPDATE_FLAG"));
                            tmp.setDeleteFlag(rs.getString("DELETE_FLAG"));
                            tmp.setReportFlag(rs.getString("REPORT_FLAG"));
                            tmp.setApprovalFlag(rs.getString("APPROVAL_FLAG"));
                            tmp.setReceiveFlag(rs.getString("RECEIVE_FLAG"));

                            tmp.setRangeName(rs.getString("RANGE_NAME"));
                            tmp.setUnitName(rs.getString("UNIT_NAME"));
                            tmp.setLedgerName(rs.getString("ledger_name"));
                            tmp.setCompanyName(rs.getString("company_name"));
                            tmp.setReportName(rs.getString("report_name"));
                            tmp.setUserNumber(rs.getString("User_Number"));
                            tmp.setUserName(rs.getString("USER_NAME"));

                            tmp.setCreatedBy(rs.getLong("CREATE_BY"));
                            tmp.setCreateByName(rs.getString("CREATE_BY_NAME"));
                            tmp.setCreationDate(rs.getDate("CREATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_BY"));
                            tmp.setLastByName(rs.getString("LAST_BY_NAME"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_DATE"));

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
     * 保存用户权限分配
     * @param userAssignmentDto
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map saveUserAssignment(UserAssignmentDto userAssignmentDto , Long userId) {
        userAssignmentDto.setDateAndUser(userId);
        List<UserAssignmentEntity> dataList = new ArrayList<UserAssignmentEntity>();
        dataList.add(this.userAssignmentDtoToEntity(userAssignmentDto));
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_GLCS_USER_ASSIGNMENT_PKG.INSERT_USER_ASSIGNMENT(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_GLCS_USER_ASSIGNMENT_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(EventParm.DB_SID, cs.getLong(3));
                        tmp.put(EventParm.DB_STATE, cs.getLong(4));
                        tmp.put(EventParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 删除用户权限分配记录
     * @param assignmentId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map delUserAssignment(Long  assignmentId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_USER_ASSIGNMENT_PKG.DELETE_USER_ASSIGNMENT(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, assignmentId);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_STATE, cs.getLong(2));
                        tmp.put(EventParm.DB_MSG, cs.getString(3));
                        return tmp;
                    }
                });
        return map;
    }





}
