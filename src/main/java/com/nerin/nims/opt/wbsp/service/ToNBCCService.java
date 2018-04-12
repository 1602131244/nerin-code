package com.nerin.nims.opt.wbsp.service;

import com.nerin.nims.opt.wbsp.common.WbspParm;
import com.nerin.nims.opt.wbsp.dto.*;
import com.nerin.nims.opt.wbsp.module.LockSpecEntity;
import com.nerin.nims.opt.wbsp.module.PaDeliverableEntity;
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
import java.util.*;

/**
 * Created by Administrator on 2016/8/3.
 */
@Component
@Transactional
public class ToNBCCService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PaLevTwoPlanService paLevTwoPlanService;


    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map createDlvr(long projectId, String phaseCode, String major, String dlvrName, String startDate, String endDate, long design, long check, long review, long approve, long workHour, String workCode, long workID, long userId) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Nbcc_Create_Dlvr(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10,:11,:12,:13,:14,:15,:16)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, phaseCode);// 设置输入参数的值
                        cs.setString(3, major);// 设置输入参数的值
                        cs.setString(4, dlvrName);// 设置输入参数的值
                        cs.setString(5, startDate);// 设置输入项目ID
                        cs.setString(6, endDate);// 设置输入参数的值
                        cs.setLong(7, design);// 设置输入项目ID
                        cs.setLong(8, check);// 设置输入项目ID
                        cs.setLong(9, review);// 设置输入项目ID
                        cs.setLong(10, approve);// 设置输入项目ID
                        cs.setLong(11, workHour);// 设置输入项目ID
                        cs.setString(12, workCode);// 设置输入项目ID
                        cs.setLong(13, workID);// 设置输入项目ID
                        cs.setLong(14, userId);// 设置输入参数的值
                        cs.registerOutParameter(15, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(16, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        //tmp.put(WbspParm.DB_STATE, cs.getString(4));
                        //tmp.put(WbspParm.DB_MSG, cs.getString(5));
                        if (cs.getString(15).equals("S")) {
                            tmp.put("create", "done");
                        } else {
                            tmp.put("create", "fail");
                            tmp.put("MSG", cs.getString(16));
                        }
                        return tmp;
                    }
                });
        return map;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map updateDlvr(long projectId, String major, long dlvrID, String startDate, String endDate, long design, long check, long review, long approve, long workHour, long specID) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Nbcc_Update_Dlvr(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10,:11,:12,:13)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, major);// 设置输入参数的值
                        cs.setLong(3, dlvrID);// 设置输入项目ID
                        cs.setString(4, startDate);// 设置输入项目ID
                        cs.setString(5, endDate);// 设置输入参数的值
                        cs.setLong(6, design);// 设置输入项目ID
                        cs.setLong(7, check);// 设置输入项目ID
                        cs.setLong(8, review);// 设置输入项目ID
                        cs.setLong(9, approve);// 设置输入项目ID
                        cs.setLong(10, workHour);// 设置输入项目ID
                        cs.setLong(11, specID);// 设置输入参数的值
                        cs.registerOutParameter(12, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(13, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        //tmp.put(WbspParm.DB_STATE, cs.getString(4));
                        //tmp.put(WbspParm.DB_MSG, cs.getString(5));
                        if (cs.getString(12).equals("S")) {
                            tmp.put("update", "done");
                        } else {
                            tmp.put("update", "fail");
                            tmp.put("MSG", cs.getString(13));
                        }
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 取工作包
     *
     * @param projectId
     * @param phaseCode
     * @param major
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<NBCCDlvrListDTO> getDlvr(long projectId, String phaseCode, String major) {
        List<NBCCDlvrListDTO> result = (List<NBCCDlvrListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Nbcc_Get_Dlvr(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, phaseCode);// 设置输入参数的值
                        cs.setString(3, major);//
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<NBCCDlvrListDTO> nbccDlvrListDTOs = new ArrayList<NBCCDlvrListDTO>();
                        NBCCDlvrListDTO nbccDlvrListDTO;
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        if (cs.getString(5).equals("S")) {
                            while (rs.next()) {// 转换每行的返回值到Map中
                                nbccDlvrListDTO = new NBCCDlvrListDTO();
                                nbccDlvrListDTO.setId(rs.getLong("Id")); //获取对应输出结果
                                nbccDlvrListDTO.setName(rs.getString("Name")); //获取对应输出结果
                                nbccDlvrListDTO.setDueDate(rs.getDate("Due_Date")); //获取对应输出结果
                                nbccDlvrListDTO.setStartDate(rs.getDate("Start_Date")); //获取对应输出结果
                                nbccDlvrListDTO.setDesignID(rs.getLong("Design_Id")); //获取对应输出结果
                                nbccDlvrListDTO.setDesignName(rs.getString("Design_Name")); //获取对应输出结果
                                nbccDlvrListDTO.setCheckID(rs.getLong("Check_Id")); //获取对应输出结果
                                nbccDlvrListDTO.setCheckName(rs.getString("Check_Name")); //获取对应输出结果
                                nbccDlvrListDTO.setReviewID(rs.getLong("Verify_Id")); //获取对应输出结果
                                nbccDlvrListDTO.setReviewName(rs.getString("Verify_Name")); //获取对应输出结果
                                nbccDlvrListDTO.setApproveID(rs.getLong("Approve_Id")); //获取对应输出结果
                                nbccDlvrListDTO.setApproveName(rs.getString("Approve_Name")); //获取对应输出结果
                                nbccDlvrListDTO.setWorkHour(rs.getString("Workhour")); //获取对应输出结果
                                nbccDlvrListDTO.setWorkCode(rs.getString("Workcode")); //获取对应输出结果
                                nbccDlvrListDTO.setSpecID(rs.getLong("Spec_Id")); //获取对应输出结果
                                nbccDlvrListDTOs.add(nbccDlvrListDTO);
                            }
                            rs.close();

                        }
                        return nbccDlvrListDTOs;

                    }
                });
        return result;
    }

}
