package com.nerin.nims.opt.glcs.service;

import com.nerin.nims.opt.aria.common.EventParm;
import com.nerin.nims.opt.glcs.dto.EbsReportColumnDto;
import com.nerin.nims.opt.glcs.dto.PeriodNameDto;
import com.nerin.nims.opt.glcs.dto.ReportHeaderDto;
import com.nerin.nims.opt.glcs.dto.ReportLineDto;
import com.nerin.nims.opt.glcs.module.ReportHeaderEntity;
import com.nerin.nims.opt.glcs.module.ReportLineEntity;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
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
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2017/10/30.
 */
@Component
@Transactional
public class ReportService {


    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    private ReportHeaderEntity reportHeaderDtoToEntity (ReportHeaderDto reportHeaderDto) {
        ReportHeaderEntity entity = new ReportHeaderEntity();
        try {
            PropertyUtils.copyProperties(entity, reportHeaderDto);
            entity.setHeaderId(reportHeaderDto.getHeaderId());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private ReportLineEntity reportLineDtoToEntity (ReportLineDto reportLineDto) {
        ReportLineEntity entity = new ReportLineEntity();
        try {
            PropertyUtils.copyProperties(entity, reportLineDto);
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
     * 获取期间
     * @param periodName
     * @param ledgerId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getPeriodNameList(String periodName, Long ledgerId, long pageNo, long pageSize
    ) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<PeriodNameDto> resultList = (List<PeriodNameDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_REPORT_PKG.GET_PERIOD_NAME(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, periodName);// 设置输入参数的值
                        if (null == ledgerId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, ledgerId);
                        cs.setLong(3, pageNo);// 设置输入参数的值
                        cs.setLong(4, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PeriodNameDto> results = new ArrayList<PeriodNameDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(6));
                        PeriodNameDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PeriodNameDto();
                            tmp.setPeriodName(rs.getString("PERIOD_NAME"));
                            tmp.setYear(rs.getString("PERIOD_YEAR"));
                            tmp.setMonth(rs.getString("PERIOD_MONTH"));
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
     * 查询报表列表
     * @param headerId
     * @param reportId
     * @param reportName
     * @param periodName
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param ledgerId
     * @param companyCode
     * @param type
     * @param reportStatus
     * @param submitStatus
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getReportHeaderList(Long headerId, Long reportId, String reportName, String periodName, Long rangeId,
                                       Long rangeVerson, Long unitId, Long ledgerId, String companyCode, String type,
                                      String reportStatus, String submitStatus, String checkSign, Long userId, long pageNo, long pageSize
                                      ) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<ReportHeaderDto> resultList = (List<ReportHeaderDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_REPORT_PKG.GET_REPORT_HEADER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == headerId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, headerId);
                        if (null == reportId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, reportId);
                        cs.setString(3, reportName);// 设置输入参数的值
                        cs.setString(4, periodName);// 设置输入参数的值
                        if (null == rangeId)
                            cs.setBigDecimal(5, null);
                        else
                            cs.setLong(5, rangeId);
                        if (null == rangeVerson)
                            cs.setBigDecimal(6, null);
                        else
                            cs.setLong(6, rangeVerson);
                        if (null == unitId)
                            cs.setBigDecimal(7, null);
                        else
                            cs.setLong(7, unitId);
                        if (null == ledgerId)
                            cs.setBigDecimal(8, null);
                        else
                            cs.setLong(8, ledgerId);
                        cs.setString(9, companyCode);// 设置输入参数的值
                        cs.setString(10, type);// 设置输入参数的值
                        cs.setString(11, reportStatus);// 设置输入参数的值
                        cs.setString(12, submitStatus);// 设置输入参数的值
                        cs.setString(13, checkSign);// 设置输入参数的值
                        cs.setLong(14, userId);// 设置输入参数的值
                        cs.setLong(15, pageNo);// 设置输入参数的值
                        cs.setLong(16, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(17, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(18, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ReportHeaderDto> results = new ArrayList<ReportHeaderDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(17);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(18));
                        ReportHeaderDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ReportHeaderDto();
                            tmp.setHeaderId(rs.getLong("HEADER_ID"));
                            tmp.setPeriodName(rs.getString("PERIOD_NAME"));
                            tmp.setReportName(rs.getString("REPORT_NAME"));
                            tmp.setRangeId(rs.getLong("RANGE_ID"));
                            tmp.setRangeVerson(rs.getLong("RANGE_VERSON"));
                            tmp.setUnitId(rs.getLong("UNIT_ID"));
                            tmp.setLedgerId(rs.getLong("LEDGER_ID"));
                            tmp.setCompanyCode(rs.getString("COMPANY_CODE"));
                            tmp.setReportId(rs.getLong("REPORT_ID"));
                            tmp.setType(rs.getString("TYPE"));
                            tmp.setReportStatus(rs.getString("REPORT_STATUS"));
                            tmp.setSubmitStatus(rs.getString("SUBMIT_STATUS"));
                            tmp.setCreatedBy(rs.getLong("CREATE_BY"));
                            tmp.setCreateByName(rs.getString("CREATE_BY_NAME"));
                            tmp.setCreateDate(rs.getDate("CREATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_BY"));
                            tmp.setLastByName(rs.getString("LAST_BY_NAME"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_DATE"));
                            tmp.setApprovalBy(rs.getLong("APPROVAL_BY"));
                            tmp.setApprovalByName(rs.getString("APPROVAL_BY_NAME"));
                            tmp.setApprovalDate(rs.getDate("APPROVAL_DATE"));
                            tmp.setSubmitBy(rs.getLong("SUBMIT_BY"));
                            tmp.setSubmitByName(rs.getString("SUBMIT_BY_NAME"));
                            tmp.setSubmitDate(rs.getDate("SUBMIT_DATE"));
                            tmp.setRangeName(rs.getString("RANGE_NAME"));
                            tmp.setUnitName(rs.getString("UNIT_NAME"));
                            tmp.setLedgerName(rs.getString("LEDGER_NAME"));
                            tmp.setCompanyName(rs.getString("COMPANY_NAME"));
                            tmp.setDescription(rs.getString("DESCRIPTION"));
                            tmp.setPostingFlag(rs.getString("POSTING_FLAG"));

                            tmp.setUpdateFlag(rs.getString("UPDATE_FLAG"));
                            tmp.setDeleteFlag(rs.getString("DELETE_FLAG"));
                            tmp.setReportFlag(rs.getString("REPORT_FLAG"));
                            tmp.setApprovalFlag(rs.getString("APPROVAL_FLAG"));

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
     * 保存报表表头
     * @param reportHeaderDto
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map saveReportHeader(ReportHeaderDto reportHeaderDto, Long userId) {
        reportHeaderDto.setDateAndUser(userId);
        List<ReportHeaderEntity> dataList = new ArrayList<ReportHeaderEntity>();
        dataList.add(this.reportHeaderDtoToEntity(reportHeaderDto));
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_GLCS_REPORT_PKG.INSERT_REPORT_HEADER(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_GLCS_REPORT_HEADER_TBL", or);
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
     * 删除报表头
     * @param headerId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map delReportHeader(Long  headerId, Long  userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_REPORT_PKG.DELETE_REPORT_HEADER(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, headerId);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_STATE, cs.getLong(3));
                        tmp.put(EventParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 审批报表
     * @param headerId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map upReportHeaderApproved(Long  headerId, Long  userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_REPORT_PKG.UPDATE_REPORT_HEADER_APPROVED(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, headerId);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_STATE, cs.getLong(3));
                        tmp.put(EventParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 反审批
     * @param headerId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map upReportHeaderReject(Long  headerId, Long  userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_REPORT_PKG.UPDATE_REPORT_HEADER_REJECT(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, headerId);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_STATE, cs.getLong(3));
                        tmp.put(EventParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 上报报表
     * @param headerId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map upReportHeaderSubmit(Long  headerId, Long  userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_REPORT_PKG.UPDATE_REPORT_HEADER_SUBMIT(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, headerId);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_STATE, cs.getLong(3));
                        tmp.put(EventParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }


    /**
     * 获取报表列集合
     * @param reportId
     * @param colnumNumber
     * @param pageNo
     * @param pageSize
     * @return
     */
    public DataTablesDTO getReportColnumList(Long reportId, String colnumNumber, long pageNo, long pageSize
    ) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<EbsReportColumnDto> resultList = (List<EbsReportColumnDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_REPORT_PKG.GET_REPORT_COLUMN(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == reportId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, reportId);
                        cs.setString(2, colnumNumber);// 设置输入参数的值
                        cs.setLong(3, pageNo);// 设置输入参数的值
                        cs.setLong(4, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<EbsReportColumnDto> results = new ArrayList<EbsReportColumnDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(6));
                        EbsReportColumnDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new EbsReportColumnDto();
                            tmp.setColumnCode(rs.getString("SEQUENCE"));
                            tmp.setColumnName(rs.getString("NAME"));
                            tmp.setColumnData(rs.getString("DATA"));
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
     * 获取报表行记录
     * @param headerId
     * @return
     */
    public DataTablesDTO getReportLinesList(Long headerId
    ) {
        DataTablesDTO dt = new DataTablesDTO();
//        dt.setPageNo(pageNo);
//        dt.setPageSize(pageSize);
        List<ReportLineDto> resultList = (List<ReportLineDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_REPORT_PKG.GET_REPORT_LINES(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == headerId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, headerId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ReportLineDto> results = new ArrayList<ReportLineDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(3));
                        ReportLineDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ReportLineDto();
                            tmp.setHeaderId(rs.getLong("HEADER_ID"));
                            tmp.setRowNumber(rs.getString("ROW_NUMBER"));
                            tmp.setRowName(rs.getString("ROW_NAME"));
                            tmp.setAmount01(rs.getString("AMOUNT_1"));
                            tmp.setAmount02(rs.getString("AMOUNT_2"));
                            tmp.setAmount03(rs.getString("AMOUNT_3"));
                            tmp.setAmount04(rs.getString("AMOUNT_4"));
                            tmp.setAmount05(rs.getString("AMOUNT_5"));
                            tmp.setAmount06(rs.getString("AMOUNT_6"));
                            tmp.setAmount07(rs.getString("AMOUNT_7"));
                            tmp.setAmount08(rs.getString("AMOUNT_8"));
                            tmp.setAmount09(rs.getString("AMOUNT_9"));
                            tmp.setAmount10(rs.getString("AMOUNT_10"));

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
     * 计算报表
     * @param headerId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map createReportLines(Long  headerId, Long  userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_REPORT_PKG.CREATE_REPORT_LINES_COMPANY(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, headerId);
                        cs.setLong(2, headerId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_STATE, cs.getLong(3));
                        tmp.put(EventParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     *保存报表数据
     * @param reportLineDtos
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map addReportLine(List<ReportLineDto> reportLineDtos, Long userId) {
        List<ReportLineEntity> inData = reportLineDtos.stream().map(chapter -> {
            chapter.setDateAndUser(userId);
            ReportLineEntity tmp = this.reportLineDtoToEntity(chapter);
            return tmp;
        }).collect(Collectors.toList());
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_GLCS_REPORT_PKG.INSERT_REPORT_LINES(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_GLCS_REPORT_LINE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
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
                        tmp.put(NbccParm.DB_SID, cs.getLong(3));
                        tmp.put(NbccParm.DB_STATE, cs.getLong(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }


}
