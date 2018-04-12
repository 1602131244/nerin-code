package com.nerin.nims.opt.navi.service;

import com.nerin.nims.opt.app.web.rest.dto.DataTableDTO;
import com.nerin.nims.opt.navi.dto.*;
import com.nerin.nims.opt.navi.module.ContractApEntity;
import com.nerin.nims.opt.navi.module.MilestoneEntity;
import com.nerin.nims.opt.nbcc.common.NbccParm;
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
 * Created by Administrator on 2016/7/11.
 */
@Component
@Transactional
public class ContractService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private ContractApEntity cdtoToEntity(ContractApLineDTO contractApLineDTO) {
        ContractApEntity entity = new ContractApEntity();
        try {
            PropertyUtils.copyProperties(entity, contractApLineDTO);
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
     * 检查项目是否有对应合同
     *
     * @param projectId
     * @return 返回版本号
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Long checkcontract(Long projectId) {
        Long i = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.check_contract(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);//设置参数
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getLong(2);
                    }
                });
        return i;
    }


    /**
     * 获取合同名称
     * @param projectId  项目ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getContractNumber(long projectId ) {
        DataTableDTO dt = new DataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<ContractNumberDTO> resultList = (List<ContractNumberDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_contract_number(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ContractNumberDTO> results = new ArrayList<ContractNumberDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ContractNumberDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContractNumberDTO();
                            tmp.setkHeaderId(rs.getLong("k_header_id"));
                            tmp.setCognomen(rs.getString("cognomen"));
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
     * 获取合同版本列表
     * @param kheaderId  合同ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public CDataTableDTO getContractApVersion(long kheaderId,long projectId,long userId) {
        CDataTableDTO dt = new CDataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<ContractApVersionDTO> resultList = (List<ContractApVersionDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_contract_ap_version(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, kheaderId);// 设置输入参数的值
                        cs.setLong(2, projectId);// 设置输入参数的值
                        cs.setLong(3, userId);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ContractApVersionDTO> results = new ArrayList<ContractApVersionDTO>();
                        cs.execute();
                        dt.setMessage(cs.getString(5));//是否可以操作 S,F1,F2
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        ContractApVersionDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContractApVersionDTO();
                            tmp.setVersion(rs.getLong("major_version"));
                            tmp.setStatusVersion(rs.getString("status"));
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
     * 获取合同头信息
     * @param kheaderId  合同ID
     * @param version  版本

     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getContractApHead(long kheaderId,long version, String statusVersion) {
        DataTableDTO dt = new DataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<ContractApHeaderDTO> resultList = (List<ContractApHeaderDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_contract_ap_header(?,?,?)}";// 调用的sql
                        if ("lastVersion".equals(statusVersion))
                            storedProc = "{call APPS.cux_navi_skjh_pkg.get_contract_ap_header_temp(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, kheaderId);// 设置输入参数的值
                        cs.setLong(2, version);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ContractApHeaderDTO> results = new ArrayList<ContractApHeaderDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        ContractApHeaderDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContractApHeaderDTO();
                            tmp.setKheaderId(rs.getLong("k_header_id"));
                            tmp.setHeaderId(rs.getLong("header_id"));
                            tmp.setContractNumber(rs.getString("contract_number"));
                            tmp.setTotal(rs.getDouble("total"));
                            tmp.setCurrencyCode(rs.getString("currency_code"));
                            tmp.setVersion(rs.getLong("version"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setAmount_flag(rs.getString("amount_flag"));
                            tmp.setGetAmount(rs.getDouble("get_amount"));
                            tmp.setGetPercent(rs.getDouble("get_percent"));
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
     * 获取合同行信息
     * @param headerId  合同头ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getContractApline(long headerId, String statusVersion) {
        DataTableDTO dt = new DataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<ContractApLineDTO> resultList = (List<ContractApLineDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_contract_ap_line(?,?)}";// 调用的sql
                        if ("lastVersion".equals(statusVersion))
                            storedProc = "{call APPS.cux_navi_skjh_pkg.get_contract_ap_line_temp(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, headerId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ContractApLineDTO> results = new ArrayList<ContractApLineDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ContractApLineDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContractApLineDTO();
                            tmp.setHeaderId(rs.getLong("header_id"));
                            tmp.setLineId(rs.getLong("line_id"));
                            tmp.setLineNumber(rs.getLong("line_number"));
                            tmp.setLineStyle(rs.getString("line_style"));
                            tmp.setLineAmount(rs.getString("line_value"));
                            tmp.setKheaderId(rs.getLong("k_header_id"));
                            tmp.setStyleDesc(rs.getString("style_desc"));
                            tmp.setLseId(rs.getLong("lse_id"));
                            tmp.setRcvTypeMeaning(rs.getString("rcv_type_meaning"));
                            tmp.setRcvType(rs.getString("rcv_type"));
                            tmp.setPlanapDate(rs.getDate("planap_date"));
                            tmp.setApPlan(rs.getFloat("ap_plan"));
                            tmp.setPlanapAmount(rs.getDouble("planap_amount"));
                            tmp.setMilestoneId(rs.getLong("milestone_id"));
                            tmp.setName(rs.getString("name"));
                            tmp.setMilestoneStatus(rs.getString("milestone_status"));
                            tmp.setMilestoneFlag(rs.getString("milestone_flag"));
                            tmp.setAttribute2(rs.getString("ATTRIBUTE2"));
                            tmp.setAttribute3(rs.getString("ATTRIBUTE3"));//合同行号
                            tmp.setLineInfo(rs.getString("line_info"));//合同行金额
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
     * 获取费用项列表
     *
     * @param kheaderId 合同ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getContractLineStyle(long kheaderId) {
        DataTableDTO dt = new DataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<ContractApLineStyleDTO> resultList = (List<ContractApLineStyleDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_line_style(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, kheaderId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ContractApLineStyleDTO> results = new ArrayList<ContractApLineStyleDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ContractApLineStyleDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContractApLineStyleDTO();
                            tmp.setName(rs.getString("name"));
                            tmp.setLseId(rs.getLong("lse_id"));
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
     * 获取合同行号
     *
     * @param kheaderId 合同ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getContractLineNum(long kheaderId) {
        DataTableDTO dt = new DataTableDTO();

        List<ContractApLineNumDTO> resultList = (List<ContractApLineNumDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_line_number(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, kheaderId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ContractApLineNumDTO> results = new ArrayList<ContractApLineNumDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ContractApLineNumDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContractApLineNumDTO();
                            tmp.setLineNumber(rs.getString("line_number"));
                            tmp.setKey(rs.getString("key"));
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
     * 获取款项性质列表
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getContractApRcvType() {
        DataTableDTO dt = new DataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<ContractApRcvTypeDTO> resultList = (List<ContractApRcvTypeDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_rcv_type(?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);

                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ContractApRcvTypeDTO> results = new ArrayList<ContractApRcvTypeDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        ContractApRcvTypeDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContractApRcvTypeDTO();
                            tmp.setTypeName(rs.getString("type_name"));
                            tmp.setTypeCode(rs.getString("type_code"));
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
     * 获取里程碑列表
     *@param kHeaderId  项目ID
     * @param lineNumber  项目ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getApMilestone(Long kHeaderId,String lineNumber) {
        DataTableDTO dt = new DataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<ContractApMilestoneDTO> resultList = (List<ContractApMilestoneDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_ap_milestone(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, kHeaderId);// 设置输入参数的值
                        cs.setString(2, lineNumber);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ContractApMilestoneDTO> results = new ArrayList<ContractApMilestoneDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        ContractApMilestoneDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContractApMilestoneDTO();
                            tmp.setConlineNum(rs.getString("line_number"));
                            tmp.setProjectId(rs.getLong("project_id"));
                            tmp.setName(rs.getString("name"));
                            tmp.setLineId(rs.getLong("line_id"));
                            tmp.setMilestoneStatus(rs.getString("milestone_status"));
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
     * 获取收款列表
     * @param kheaderId  合同ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public CDataTableDTO getContractArCash(Long kheaderId ) {
        CDataTableDTO dt = new CDataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<ContractArCashDTO> resultList = (List<ContractArCashDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_ar_cash(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, kheaderId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ContractArCashDTO> results = new ArrayList<ContractArCashDTO>();
                        cs.execute();
                        dt.setTotal(cs.getDouble(3));
                        dt.setPercent(cs.getLong(4));
                        dt.setTotalCNY(cs.getDouble(5));
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ContractArCashDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContractArCashDTO();
                            tmp.setReceiptNumber(rs.getString("receipt_number"));
                            tmp.setGlDate(rs.getDate("gl_date"));
                            tmp.setRecCurr(rs.getString("rec_curr"));
                            tmp.setRecAmount(rs.getDouble("rec_amount"));
                            tmp.setRecAmountCNY(rs.getDouble("rec_amount_cny"));
                            tmp.setProjNumber(rs.getString("proj_number"));
                            tmp.setCustName(rs.getString("cust_name"));
                            tmp.setDescription(rs.getString("description"));
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
     * 获取发票列表
     * @param kheaderId  合同ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public CDataTableDTO getContractRaTrx(long kheaderId ) {
        CDataTableDTO dt = new CDataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<ContractRaTrxDTO> resultList = (List<ContractRaTrxDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_ra_trx(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, kheaderId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ContractRaTrxDTO> results = new ArrayList<ContractRaTrxDTO>();
                        cs.execute();
                        dt.setTotal(cs.getDouble(3));
                        dt.setPercent(cs.getLong(4));
                        dt.setTotalCNY(cs.getDouble(5));
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ContractRaTrxDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContractRaTrxDTO();
                            tmp.setTrxNumber(rs.getString("trx_number"));
                            tmp.setTrxDate(rs.getDate("trx_date"));
                            tmp.setInvCURR(rs.getString("INV_CURR"));
                            tmp.setAmount(rs.getDouble("amount"));
                            tmp.setAmountCNY(rs.getDouble("amount_cny"));
                            tmp.setProjNumber(rs.getString("proj_number"));
                            tmp.setCustName(rs.getString("cust_name"));
                            tmp.setDescription(rs.getString("description"));
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
     *调整计划按钮，返回头ID
     * @param kheaderId  合同ID
     * @param userId
     * @return
     * 返回版本号
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Long updatePlan(Long kheaderId, Long userId, long version) {
        Long i = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.update_contract_ap_plan(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, kheaderId);//设置参数
                        cs.setLong(2, userId);//设置参数
                        cs.setLong(3, version);//设置参数
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return cs.getLong(4);
                    }
                });
        return i;
    }

    /**
     *保存按钮，返回头ID号
     * @param contractApLineDTOs
     * @param userId
     * @return
     * 返回版本号
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public CDataTableDTO saveContractAp(List<ContractApLineDTO> contractApLineDTOs,Long userId) {

        List<ContractApEntity> inData = contractApLineDTOs.stream().map(mile -> {
            ContractApEntity tmp = this.cdtoToEntity(mile);
            return tmp;
        }).collect(Collectors.toList());
        CDataTableDTO i = (CDataTableDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.save_contract_ap_line(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.NAVI_CONTRACT_AP_LINE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
                        cs.setArray(1, vArray);
                        cs.setLong(2,userId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        CDataTableDTO res=new CDataTableDTO();
                        res.setHeaderId(cs.getLong(3));
                        res.setMessage(cs.getString(4));
                        return res;
                    }
                });
        return i;
    }

    @SuppressWarnings("unchecked")
    public Map submitOaApproved(long id, String userNo) {
        Map result = (Map)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.Submit_Oa_Approved(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.setString(2, userNo);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {

                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(3));
                        tmp.put(NbccParm.DB_STATE, cs.getString(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return result;
    }

    //删除版本
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map deleteVersion( long headerId, long kHeaderId) {
        Map message = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.delete_version(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, headerId);//设置参数
                        cs.setLong(2, kHeaderId);//设置参数
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(3));
                        return tmp;
                    }
                });
        return message;
    }
    /*里程碑状态获取
    * @param kheaderId  合同ID
    * @param userId
    * @return
            * 返回版本号
    */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map getMilestoneStatus(long kHeaderId, String lineNum,long lineId) {
        Map status = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_milestone_status(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, kHeaderId);//设置参数
                        cs.setString(2, lineNum);//设置参数
                        cs.setLong(3, lineId);//设置参数
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put("status", cs.getString(4));
                        return  tmp;
                    }
                });
        return status;
    }
    /*合同行信息
    * @param kheaderId  合同ID
    * @param userId
    * @return
            * 返回版本号
    */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map getContractInfo(long kheaderId, String lineNum) {
        Map status = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_skjh_pkg.get_line_info(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, kheaderId);//设置参数
                        cs.setString(2, lineNum);//设置参数
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put("style", cs.getString(3));
                        tmp.put("amount", cs.getString(4));
                        return  tmp;
                    }
                });
        return status;
    }
}
