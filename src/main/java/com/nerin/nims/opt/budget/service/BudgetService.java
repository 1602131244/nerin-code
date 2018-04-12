package com.nerin.nims.opt.budget.service;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.budget.dto.*;
import com.nerin.nims.opt.budget.module.BudgetHeaderEntity;
import com.nerin.nims.opt.budget.module.BudgetLineEntity;
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
 * Created by Administrator on 2016/8/29.
 */
@Component
@Transactional
public class BudgetService {

    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     *把BudgetHeaderDto的值拷贝到BudgetHeaderEntity中
     * @param budgetHeaderDto
     * @return
     */
    private BudgetHeaderEntity budgetHeaderDtoToEntity (BudgetHeaderDto budgetHeaderDto) {
        BudgetHeaderEntity entity = new BudgetHeaderEntity();
        try {
            PropertyUtils.copyProperties(entity, budgetHeaderDto);
            entity.setHeaderId(budgetHeaderDto.getHeaderId());
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
     * BudgetLineDto的值拷贝到BudgetLineEntity中
     * @param budgetLineDto
     * @return
     */
    private BudgetLineEntity budgetLineDtoToEntity (BudgetLineDto budgetLineDto) {
        BudgetLineEntity entity = new BudgetLineEntity();
        try {
            PropertyUtils.copyProperties(entity, budgetLineDto);
            entity.setHeaderId(budgetLineDto.getHeaderId());
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
     * 获取组织列表
     * @param orgName
     * @param orgId
     * @param sign        Y 标示限定为财务组织，N 标示所有组织
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getOrgList(String orgName, Long orgId, String sign, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        List<OrganizationDto> resultList = (List<OrganizationDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.get_org_list(?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, orgName);// 设置输入参数的值
                        if (null == orgId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, orgId);// 设置输入参数的值
                        cs.setString(3, sign);// 设置输入参数的值
                        cs.setLong(4, pageNo);// 设置输入参数的值
                        cs.setLong(5, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(6, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<OrganizationDto> results = new ArrayList<OrganizationDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(7));
                        OrganizationDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new OrganizationDto();
                            tmp.setOrgId(rs.getLong("org_id"));
                            tmp.setOrgName(rs.getString("org_name"));
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
     * 获取年份列表
     * @param year
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getYearList(Long year, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        List<YearDto> resultList = (List<YearDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.get_year_list(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == year)
                            cs.setBigDecimal(1, null);// 设置输入参数的值
                        else
                            cs.setLong(1, year);// 设置输入参数的值
                        cs.setLong(2, pageNo);// 设置输入参数的值
                        cs.setLong(3, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<YearDto> results = new ArrayList<YearDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(5));
                        YearDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new YearDto();
                            tmp.setYear(rs.getLong("period_year"));
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
     * 获取预算项目类型
     * @param subType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getSubTypeList(String  subType, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        List<SubTypeDto> resultList = (List<SubTypeDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.get_sub_type_list(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, subType);// 设置输入参数的值
                        cs.setLong(2, pageNo);// 设置输入参数的值
                        cs.setLong(3, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<SubTypeDto> results = new ArrayList<SubTypeDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(5));
                        SubTypeDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SubTypeDto();
                            tmp.setSubType(rs.getString("SUB_TYPE"));
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
     * 获取支出类型
     * @param subType
     * @param costType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getCostTypeList(String  subType,String  costType,  long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        List<CostTypeDto> resultList = (List<CostTypeDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.get_cost_type_list(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, subType);// 设置输入参数的值
                        cs.setString(2, costType);// 设置输入参数的值
                        cs.setLong(3, pageNo);// 设置输入参数的值
                        cs.setLong(4, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<CostTypeDto> results = new ArrayList<CostTypeDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(6));
                        CostTypeDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new CostTypeDto();
                            tmp.setSubType(rs.getString("SUB_TYPE"));
                            tmp.setCostType(rs.getString("COST_TYPE"));
                            tmp.setAlias(rs.getString("ALIAS"));
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
     * 获取状态列表
     * @param statusType
     * @param statusCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getStatusList(String  statusType,String  statusCode,  long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        List<StatusDto> resultList = (List<StatusDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.get_status_list(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, statusType);// 设置输入参数的值
                        cs.setString(2, statusCode);// 设置输入参数的值
                        cs.setLong(3, pageNo);// 设置输入参数的值
                        cs.setLong(4, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<StatusDto> results = new ArrayList<StatusDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(6));
                        StatusDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new StatusDto();
                            tmp.setCode(rs.getString("lookup_code"));
                            tmp.setName(rs.getString("meaning"));
                            tmp.setDescription(rs.getString("description"));
                            tmp.setTag(rs.getString("tag"));
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
     * 获取标识列表
     * @param signCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getSignList(String  signCode,long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        List<SignDto> resultList = (List<SignDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.get_sign_list(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, signCode);// 设置输入参数的值
                        cs.setLong(2, pageNo);// 设置输入参数的值
                        cs.setLong(3, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<SignDto> results = new ArrayList<SignDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(5));
                        SignDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SignDto();
                            tmp.setCode(rs.getString("CODE"));
                            tmp.setName(rs.getString("NAME"));
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
     * 获取部门预算取头列表
     * @param orgName
     * @param year
     * @param budgetName
     * @param createName
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getBudgetHeaderList(String orgName, String year, String budgetName, String createName,
                                             Long headerId, long userId , long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<BudgetHeaderDto> resultList = (List<BudgetHeaderDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.get_budget_headers(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, orgName);// 设置输入参数的值
                        cs.setString(2, year);// 设置输入参数的值
                        cs.setString(3, budgetName);// 设置输入参数的值
                        cs.setString(4, createName);// 设置输入参数的值
                        if (null == headerId)
                            cs.setBigDecimal(5, null);// 设置输入参数的值
                        else
                            cs.setLong(5, headerId);// 设置输入参数的值
                        cs.setLong(6, userId);// 设置输入参数的值
                        cs.setLong(7, pageNo);// 设置输入参数的值
                        cs.setLong(8, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(9, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(10, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<BudgetHeaderDto> results = new ArrayList<BudgetHeaderDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(9);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(10));
                        BudgetHeaderDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new BudgetHeaderDto();
                            tmp.setHeaderId(rs.getLong("HEADER_ID"));
                            tmp.setOrgId(rs.getLong("ORG_ID"));
                            tmp.setOrgName(rs.getString("ORG_NAME"));
                            tmp.setYear(rs.getString("YEAR"));
                            tmp.setName(rs.getString("NAME"));
                            tmp.setStatus(rs.getString("STATUS"));
                            tmp.setStatusName(rs.getString("STATUS_NAME"));
                            tmp.setAttributeCategory(rs.getString("ATTRIBUTE_CATEGORY"));
                            tmp.setAttribute1(rs.getString("ATTRIBUTE1"));
                            tmp.setAttribute2(rs.getString("ATTRIBUTE2"));
                            tmp.setAttribute3(rs.getString("ATTRIBUTE3"));
                            tmp.setAttribute4(rs.getString("ATTRIBUTE4"));
                            tmp.setAttribute5(rs.getString("ATTRIBUTE5"));
                            tmp.setAttribute6(rs.getString("ATTRIBUTE6"));
                            tmp.setAttribute7(rs.getString("ATTRIBUTE7"));
                            tmp.setAttribute8(rs.getString("ATTRIBUTE8"));
                            tmp.setAttribute9(rs.getString("ATTRIBUTE9"));
                            tmp.setAttribute10(rs.getLong("ATTRIBUTE10"));
                            tmp.setAttribute11(rs.getString("ATTRIBUTE11"));
                            tmp.setAttribute12(rs.getString("ATTRIBUTE12"));
                            tmp.setAttribute13(rs.getString("ATTRIBUTE13"));
                            tmp.setAttribute14(rs.getString("ATTRIBUTE14"));
                            tmp.setAttribute15(rs.getString("ATTRIBUTE15"));
                            tmp.setCreatedBy(rs.getLong("CREATED_BY"));
                            tmp.setCreateByName(rs.getString("CREATED_BY_NAME"));
                            tmp.setCreationDate(rs.getDate("CREATION_DATE"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_UPDATED_BY"));
                            tmp.setLastUpdateLogin(rs.getLong("LAST_UPDATE_LOGIN"));

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
     * 保存预算任务主表
     * @param budgetHeaderDto
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map addOrUpBudget(BudgetHeaderDto budgetHeaderDto, Long userId) {
        budgetHeaderDto.setDateAndUser(userId);
        List<BudgetHeaderEntity> dataList = new ArrayList<BudgetHeaderEntity>();
        dataList.add(this.budgetHeaderDtoToEntity(budgetHeaderDto));
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.insert_budget_header(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_DEPT_BUDGET_HEADERS_TBL", or);
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
                        tmp.put(NbccParm.DB_SID, cs.getLong(3));
                        tmp.put(NbccParm.DB_STATE, cs.getLong(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     *删除部门年度预算任务
     * @param headerIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map delBudget(String headerIds) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.dele_budget_headers(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, headerIds);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getLong(2));
                        tmp.put(NbccParm.DB_MSG, cs.getString(3));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 导入预算任务明细表数据
     * @param headerId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map importBudgetLines(long headerId, long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.Import_budget_lines(?,?,?,?)}";
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
                        tmp.put(NbccParm.DB_STATE, cs.getLong(3));
                        tmp.put(NbccParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 预算任务明细列表
     * @param headerId
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getBudgetLineList(Long headerId, long userId , long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<BudgetLineDto> resultList = (List<BudgetLineDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.get_budget_lines(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, headerId);// 设置输入参数的值
                        cs.setLong(2, userId);// 设置输入参数的值
                        cs.setLong(3, pageNo);// 设置输入参数的值
                        cs.setLong(4, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<BudgetLineDto> results = new ArrayList<BudgetLineDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(6));
                        BudgetLineDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new BudgetLineDto();
                            tmp.setLineId(rs.getLong("line_id"));
                            tmp.setHeaderId(rs.getLong("header_id"));
                            tmp.setDeptid(rs.getString("dept_id"));
                            tmp.setDeptName(rs.getString("dept_name"));
                            tmp.setStatus(rs.getString("STATUS"));
                            tmp.setStatusName(rs.getString("STATUS_NAME"));
                            tmp.setOldProjectId(rs.getLong("old_project_id"));
                            tmp.setOldProjectNumber(rs.getString("old_project_number"));
                            tmp.setOldProjectName(rs.getString("old_project_name"));
                            tmp.setProjectNumber(rs.getString("project_number"));
                            tmp.setProjectName(rs.getString("project_name"));
                            tmp.setType(rs.getString("type"));
                            tmp.setExpenditureType(rs.getString("expenditure_type"));
                            tmp.setOutlineNumber(rs.getLong("outline_number"));
                            tmp.setRbsVersionId(rs.getLong("rbs_version_id"));
                            tmp.setAlias(rs.getString("alias"));
                            tmp.setOldAmount(rs.getDouble("old_amount"));
                            tmp.setOldCost(rs.getDouble("old_cost"));
                            tmp.setSign(rs.getString("sign"));
                            tmp.setTotalPersons(rs.getLong("total_number_person"));
                            tmp.setPerCapitaAmount(rs.getDouble("per_capita_amount"));
                            tmp.setAmount(rs.getDouble("amount"));
                            tmp.setAuditAmount1(rs.getDouble("audit_amount1"));
                            tmp.setPersonIdAudit1(rs.getLong("person_id_audit1"));
                            tmp.setAuditAmount2(rs.getDouble("audit_amount2"));
                            tmp.setPersonIdAudit2(rs.getLong("person_id_audit2"));
                            tmp.setAuditAmount3(rs.getDouble("audit_amount3"));
                            tmp.setPersonIdAudit3(rs.getLong("person_id_audit3"));
                            tmp.setAuditAmount4(rs.getDouble("audit_amount4"));
                            tmp.setPersonIdAudit4(rs.getLong("person_id_audit4"));
                            tmp.setAuditAmount5(rs.getDouble("audit_amount5"));
                            tmp.setPersonIdAudit5(rs.getLong("person_id_audit5"));
                            tmp.setAuditAmount6(rs.getDouble("audit_amount6"));
                            tmp.setPersonIdAudit6(rs.getLong("person_id_audit6"));
                            tmp.setDescription(rs.getString("description"));
                            tmp.setAttributeCategory(rs.getString("ATTRIBUTE_CATEGORY"));
                            tmp.setAttribute1(rs.getString("ATTRIBUTE1"));
                            tmp.setAttribute2(rs.getString("ATTRIBUTE2"));
                            tmp.setAttribute3(rs.getString("ATTRIBUTE3"));
                            tmp.setAttribute4(rs.getString("ATTRIBUTE4"));
                            tmp.setAttribute5(rs.getString("ATTRIBUTE5"));
                            tmp.setAttribute6(rs.getString("ATTRIBUTE6"));
                            tmp.setAttribute7(rs.getString("ATTRIBUTE7"));
                            tmp.setAttribute8(rs.getString("ATTRIBUTE8"));
                            tmp.setAttribute9(rs.getString("ATTRIBUTE9"));
                            tmp.setAttribute10(rs.getString("ATTRIBUTE10"));
                            tmp.setAttribute11(rs.getString("ATTRIBUTE11"));
                            tmp.setAttribute12(rs.getString("ATTRIBUTE12"));
                            tmp.setAttribute13(rs.getString("ATTRIBUTE13"));
                            tmp.setAttribute14(rs.getString("ATTRIBUTE14"));
                            tmp.setAttribute15(rs.getString("ATTRIBUTE15"));
                            tmp.setCreatedBy(rs.getLong("CREATED_BY"));
                            tmp.setCreationDate(rs.getDate("CREATION_DATE"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_UPDATED_BY"));
                            tmp.setLastUpdateLogin(rs.getLong("LAST_UPDATE_LOGIN"));

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
     * 保存预算任务明细表数据
     * @param budgetLineDtos
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map addOrUpBudgetLine(List<BudgetLineDto> budgetLineDtos, Long userId) {
        List<BudgetLineEntity> inData = budgetLineDtos.stream().map(chapter -> {
            chapter.setDateAndUser(userId);
            BudgetLineEntity tmp = this.budgetLineDtoToEntity(chapter);
            return tmp;
        }).collect(Collectors.toList());
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.insert_budget_lines(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_DEPT_BUDGET_LINES_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
                        cs.setArray(1, vArray);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(NbccParm.DB_STATE, cs.getLong(3));
                        tmp.put(NbccParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 删除预算任务明细数据
     * @param lineIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map delBudgetLines(String lineIds) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.dele_budget_lines(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, lineIds);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getLong(2));
                        tmp.put(NbccParm.DB_MSG, cs.getString(3));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 发起填报流程时获取OA的链接地址
     * @param headerId
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getFillProcessList(Long headerId, Long userId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<OaUrlDto> resultList = (List<OaUrlDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.get_oa_url(?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, headerId);// 设置输入参数的值
                        cs.setLong(2, userId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<OaUrlDto> results = new ArrayList<OaUrlDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(4));
                        OaUrlDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new OaUrlDto();
                            tmp.setUrl(rs.getString("oa_url"));
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
     * 更新OA流程的签收人、签收日期
     * @param requestId
     * @param userId
     * @return
     */
//    @Transactional(rollbackFor = Exception.class)
//    public Map updateOaRequestOie(Long  requestId,Long userId) {
//        Map map = (Map) jdbcTemplate.execute(
//                new CallableStatementCreator() {
//                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
//                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.update_oa_request_oie(?,?,?,?)}";
//                        CallableStatement cs = con.prepareCall(storedProc);
//                        cs.setLong(1, requestId);
//                        cs.setLong(2, userId);
//                        cs.registerOutParameter(3, OracleTypes.NUMBER);
//                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
//                        return cs;
//                    }
//                }, new CallableStatementCallback() {
//                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
//                        cs.execute();
//                        Map tmp = new HashMap();
//                        tmp.put(NbccParm.DB_STATE, cs.getLong(3));
//                        tmp.put(NbccParm.DB_MSG, cs.getString(4));
//                        return tmp;
//                    }
//                });
//        return map;
//    }

    /**
     * 获取项目预算列表
     * @param projectId
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getProjBudgetList(Long  projectId, long userId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<ProjectBudgetDto> resultList = (List<ProjectBudgetDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_BUDGET_INFO_RPT.GET_PROJ_BUDGET(?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setLong(2, userId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectBudgetDto> results = new ArrayList<ProjectBudgetDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(4));
                        ProjectBudgetDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectBudgetDto();
                            tmp.setProjectId(rs.getLong("PROJECT_ID"));
                            tmp.setProjectNumber(rs.getString("PROJ_NUMBER"));
                            tmp.setProjectName(rs.getString("PROJ_NAME"));
                            tmp.setRbsVersionId(rs.getLong("RBS_VERSION_ID"));
                            tmp.setBudgetItemNumber(rs.getString("BUDGET_ITEM_NUMBER"));
                            tmp.setBudgetItemName(rs.getString("BUDGET_ITEM_NAME"));
                            tmp.setAlias(rs.getString("ALIAS"));
                            tmp.setOutLineNumber(rs.getString("OUTLINE_NUMBER"));
                            tmp.setBudgetName(rs.getString("BUDGET_NAME"));
                            tmp.setParentTaskNumber(rs.getString("PARENT_TASK_NUMBER"));
                            tmp.setParentTaskName(rs.getString("PARENT_TASK_NAME"));
                            tmp.setTaskId(rs.getLong("TASK_ID"));
                            tmp.setTaskName(rs.getString("TASK_NAME"));
                            tmp.setBudgetAmount(rs.getString("BUDGET_AMOUNT"));
                            tmp.setCost(rs.getString("COST"));
                            tmp.setOccurrenceAmount(rs.getString("AMOUNT"));
                            tmp.setTransitAmount(rs.getString("TransitAmount"));
                            tmp.setSurplusAmount(rs.getString("SurplusAmount"));

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
     * 项目成本列表
     * @param projectId
     * @param rbsVersionId
     * @param expenditureType
     * @param taskId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getProjCostList(Long  projectId, Long rbsVersionId, String expenditureType, Long taskId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<ProjectCostDto> resultList = (List<ProjectCostDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_BUDGET_INFO_RPT.GET_PROJ_COST(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setLong(2, rbsVersionId);// 设置输入参数的值
                        cs.setString(3, expenditureType);// 设置输入参数的值
                        if (null == taskId)
                            cs.setBigDecimal(4, null);// 设置输入参数的值
                        else
                            cs.setLong(4, taskId);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectCostDto> results = new ArrayList<ProjectCostDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(6));
                        ProjectCostDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectCostDto();
                            tmp.setProjectId(rs.getLong("PROJ_ID"));
                            tmp.setCostType(rs.getString("COST_TYPE"));
                            tmp.setElementNumber(rs.getString("ELEMENT_NUMBER"));
                            tmp.setElementName(rs.getString("ELEMENT_NAME"));
                            tmp.setExpenditureType(rs.getString("EXPENDITURE_TYPE"));
                            tmp.setParentTaskName(rs.getString("PARENT_TASK_NAME"));
                            tmp.setTaskName(rs.getString("TASK_NAME"));
                            tmp.setInvnoType(rs.getString("INVNO_TYPE"));
                            tmp.setExpenditureDate(rs.getDate("EXPE_DATE"));
                            tmp.setInvnoNumber(rs.getString("INVNO_NO"));
                            tmp.setVendorName(rs.getString("VENDOR_NAME"));
                            tmp.setQuantity(rs.getString("QUANTITY"));
                            tmp.setCost(rs.getString("COST"));
                            tmp.setNote(rs.getString("NOTE"));

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
     *   查询项目工时信息
     * @param projectId    项目ID
     * @param projectSearch  项目检索信息
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getprojectInfo(Long projectId, String projectSearch, long userId , long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<ProjectDto> resultList = (List<ProjectDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_PROJECTS_ERP_EXT_B.GET_PROJ_INFO(?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, projectSearch);// 设置输入参数的值
                        if (null == projectId)
                            cs.setBigDecimal(2, null);// 设置输入参数的值
                        else
                            cs.setLong(2, projectId);// 设置输入参数的值
                        cs.setLong(3, userId);// 设置输入参数的值
                        cs.setLong(4, pageNo);// 设置输入参数的值
                        cs.setLong(5, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(6, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectDto> results = new ArrayList<ProjectDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(7));
                        ProjectDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectDto();
                            tmp.setProjectId(rs.getLong("PROJECT_ID"));
                            tmp.setProjectNumber(rs.getString("PROJ_NUMBER"));
                            tmp.setProjectName(rs.getString("PROJ_NAME"));
                            tmp.setProjectStartDate(rs.getString("PROJ_START_DATE"));
                            tmp.setProjectEndDate(rs.getString("PROJ_END_DATE"));
                            tmp.setProjectType(rs.getString("PROJ_TYPE"));
                            tmp.setProjectStatus(rs.getString("PROJ_STATUS"));
                            tmp.setProjectOrgName(rs.getString("PROJ_ORG"));
                            tmp.setProjectManager(rs.getString("PROJ_MANAGER"));
                            tmp.setProjectClass(rs.getString("PROJ_CLASS"));
                            tmp.setProjectCustName(rs.getString("CUST_NAME"));
                            tmp.setExtensioId(rs.getLong("EXTENSION_ID"));
                            tmp.setcExtAttr4(rs.getString("C_EXT_ATTR4"));
                            tmp.setEditFlag(rs.getString("EDIT_FLAG"));
                            tmp.setOrgName(rs.getString("ORG_NAME"));
//                            tmp.setAttribute1(rs.getString("ATTRIBUTE1"));
                            tmp.setAttribute2(rs.getString("ATTRIBUTE2"));
                            tmp.setAttribute3(rs.getString("ATTRIBUTE3"));
                            tmp.setAttribute4(rs.getString("ATTRIBUTE4"));
                            tmp.setAttribute5(rs.getString("ATTRIBUTE5"));
                            tmp.setAttribute6(rs.getString("ATTRIBUTE6"));
//                            tmp.setAttribute7(rs.getString("ATTRIBUTE7"));
                            tmp.setAttribute8(rs.getString("ATTRIBUTE8"));
//                            tmp.setAttribute9(rs.getString("ATTRIBUTE9"));
//                            tmp.setAttribute10(rs.getString("ATTRIBUTE10"));
//                            tmp.setAttribute11(rs.getString("ATTRIBUTE11"));
//                            tmp.setAttribute12(rs.getString("ATTRIBUTE12"));
//                            tmp.setAttribute13(rs.getString("ATTRIBUTE13"));
//                            tmp.setAttribute14(rs.getString("ATTRIBUTE14"));
//                            tmp.setAttribute15(rs.getString("ATTRIBUTE15"));

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
     *更新总工时
     * @param projectId  项目ID
     * @param cExtAttr4  总工时
     * @param userId      用户ID
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map upProjInfo(Long projectId, String cExtAttr4, long userId ) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_PROJECTS_ERP_EXT_B.UPDATE_PROJ_INFO(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setString(2, cExtAttr4);
                        cs.setLong(3, userId);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getLong(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }


}
