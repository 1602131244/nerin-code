package com.nerin.nims.opt.glcs.service;

import com.nerin.nims.opt.glcs.dto.ReportAmountType;
import com.nerin.nims.opt.glcs.dto.ReportItemDto;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
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
 * Created by Administrator on 2018/2/28.
 */
@Component
@Transactional
public class EntryService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * 查报表行项目
     * @param reportID
     * @param reportName
     * @param reportRowId
     * @param reportItemNo
     * @param reportItemName
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getReportItemList(Long reportID, String reportName, Long reportRowId, String reportItemNo, String reportItemName,
                                            long pageNo, long pageSize
    ) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<ReportItemDto> resultList = (List<ReportItemDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_ENTRY_PKG.GET_REPORT_ITEM(?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == reportID)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, reportID);
                        cs.setString(2, reportName);// 设置输入参数的值
                        if (null == reportRowId)
                            cs.setBigDecimal(3, null);
                        else
                            cs.setLong(3, reportRowId);

                        cs.setString(4, reportItemNo);// 设置输入参数的值
                        cs.setString(5, reportItemName);// 设置输入参数的值
                        cs.setLong(6, pageNo);// 设置输入参数的值
                        cs.setLong(7, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(8, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(9, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ReportItemDto> results = new ArrayList<ReportItemDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(8);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(9));
                        ReportItemDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ReportItemDto();
                            tmp.setApplcationId(rs.getLong("APPLICATION_ID"));
                            tmp.setReportRowId(rs.getLong("AXIS_SET_ID"));
                            tmp.setItemNo(rs.getString("SEQUENCE"));
                            tmp.setItemName(rs.getString("DESCRIPTION"));
                            tmp.setChangeFlag(rs.getString("CHANGE_SIGN_FLAG"));
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
     * 查报表金额类型
     * @param amountTypeName
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getReportAmountTypeList(String amountTypeName, long pageNo, long pageSize
    ) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<ReportAmountType> resultList = (List<ReportAmountType>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_ENTRY_PKG.GET_REPORT_AMOUNT_TYPE(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, amountTypeName);// 设置输入参数的值
                        cs.setLong(2, pageNo);// 设置输入参数的值
                        cs.setLong(3, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ReportAmountType> results = new ArrayList<ReportAmountType>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(5));
                        ReportAmountType tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ReportAmountType();
                            tmp.setAxisName(rs.getString("DESCRIPTION"));
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
