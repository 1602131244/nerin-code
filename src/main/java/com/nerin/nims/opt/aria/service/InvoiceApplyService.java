package com.nerin.nims.opt.aria.service;

import com.nerin.nims.opt.aria.common.EventParm;
import com.nerin.nims.opt.aria.dto.*;
import com.nerin.nims.opt.aria.module.BillEventEntity;
import com.nerin.nims.opt.cqs.dto.DataTablesDTO;
import com.nerin.nims.opt.nbcc.dto.ProjectDto;
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
 * Created by Administrator on 2017/6/20.
 */
@Component
@Transactional
public class InvoiceApplyService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    private BillEventEntity billEventDtoToEntity (BillEventDto billEventDto) {
        BillEventEntity entity = new BillEventEntity();
        try {
            PropertyUtils.copyProperties(entity, billEventDto);
            entity.setBillEventId(billEventDto.getBillEventId());
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
     * 获取合同的客户列表
     * @param contHeaderId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getContCustList(Long contHeaderId) {
        DataTablesDTO dt = new DataTablesDTO();
//        dt.setPageNo(pageNo);
//        dt.setPageSize(pageSize);
        List<ContCustDto> resultList = (List<ContCustDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_CONT_CUST_LIST(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == contHeaderId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, contHeaderId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ContCustDto> results = new ArrayList<ContCustDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(3));
                        ContCustDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContCustDto();
                            tmp.setCustId(rs.getLong("CUST_ID"));
                            tmp.setCustName(rs.getString("CUST_NAME"));
                            tmp.setCustNumber(rs.getString("CUST_NUMBER"));
                            tmp.setCustStatus(rs.getString("CUST_STATUS"));

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
     * 获取项目列表
     * @param projectSearch
     * @param contLineId
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getProjectList(String projectSearch, Long projectId, String projectNumber, String contLineId, String checkProjectMember,  long userId) {
        DataTablesDTO dt = new DataTablesDTO();
//        dt.setPageNo(pageNo);
//        dt.setPageSize(pageSize);
        List<ProjectDto> resultList = (List<ProjectDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_PROJECTS(?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, projectSearch);// 设置输入参数的值
                        if (null == projectId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, projectId);
                        cs.setString(3, projectNumber);
                        cs.setString(4, contLineId);
                        cs.setString(5, checkProjectMember);
                        cs.setLong(6, userId);
                        cs.registerOutParameter(7, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(8, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectDto> results = new ArrayList<ProjectDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(7);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(8));
                        ProjectDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectDto();
                            tmp.setProjectNumber(rs.getString("PROJECTNUMBER"));
                            tmp.setProjectName(rs.getString("PROJECTNAME"));
                            tmp.setProjectManager(rs.getString("PROJECTMANAGER"));
                            tmp.setProjectId(rs.getLong("PROJECTID"));
                            tmp.setProjOrgId(rs.getLong("ORGID"));
                            tmp.setProjOrgName(rs.getString("ORGNAME"));

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
     *获取合同列表
     * @param contractId
     * @param contractNumber
     * @param contractSearch
     * @param projectId
     * @param projectNumber
     * @param projectSearch
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getContractHeaderList(Long contractId, String contractNumber, String contractSearch,
             Long projectId, String projectNumber, String projectSearch,String projContSearch,
                                               String checkManager, long userId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<ContractHeaderDto> resultList = (List<ContractHeaderDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_CONTRACTS(?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == contractId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, contractId);
                        cs.setString(2, contractNumber);// 设置输入参数的值
                        cs.setString(3, contractSearch);// 设置输入参数的值
                        if (null == projectId)
                            cs.setBigDecimal(4, null);
                        else
                            cs.setLong(4, projectId);
                        cs.setString(5, projectNumber);// 设置输入参数的值
                        cs.setString(6, projectSearch);// 设置输入参数的值
                        cs.setString(7, projContSearch);// 设置输入参数的值
                        cs.setString(8, checkManager);// 设置输入参数的值
                        cs.setLong(9, userId);// 设置输入参数的值
                        cs.setLong(10, pageNo);// 设置输入参数的值
                        cs.setLong(11, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(12, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(13, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ContractHeaderDto> results = new ArrayList<ContractHeaderDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(12);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(13));
                        ContractHeaderDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContractHeaderDto();
                            tmp.setContHeaderId(rs.getLong("CONT_ID"));
                            tmp.setContNumber(rs.getString("CONT_NUMBER"));
                            tmp.setContName(rs.getString("CONT_NAME"));
                            tmp.setContLongName(rs.getString("CONT_LONGNAME"));
                            tmp.setContType(rs.getString("CONT_TYPE"));
                            tmp.setContTypeCode(rs.getString("CONT_TYPECODE"));
                            tmp.setContStatus(rs.getString("STATUS"));
                            tmp.setContStatusCode(rs.getString("STATUS_CODE"));
                            tmp.setOrgId(rs.getLong("ORG_ID"));
                            tmp.setOrgName(rs.getString("ORG_NAME"));
                            tmp.setCurr(rs.getString("CURR"));
                            tmp.setAmount(rs.getDouble("AMOUNT"));
                            tmp.setAmountDis(rs.getString("AMOUNTDIS"));
                            tmp.setHeaderProjectID(rs.getLong("PROJECT_ID"));
                            tmp.setHeaderPorjectName(rs.getString("PROJECT_NAME"));
                            tmp.setHeaderProjectNumber(rs.getString("PROJECT_NUMBER"));
                            tmp.setLineProjects(rs.getString("LINE_PROJECTS"));
                            tmp.setContCust(rs.getString("CONT_CUST"));

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
     * 获取市场活动物品列表
     * @param contractId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getDeliverablesList(Long contractId, Long deliverablesId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<BillDeliverablesDto> resultList = (List<BillDeliverablesDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_CONTRACT_DELIVERABLE(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == contractId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, contractId);
                        if (null == deliverablesId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, deliverablesId);
                        cs.setLong(3, pageNo);// 设置输入参数的值
                        cs.setLong(4, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<BillDeliverablesDto> results = new ArrayList<BillDeliverablesDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(6));
                        BillDeliverablesDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new BillDeliverablesDto();
                            tmp.setLineNumber(rs.getString("LINE_NUMBER"));
                            tmp.setDeliverableNumber(rs.getString("DELIVERABLE_NUM"));
                            tmp.setInventoryOrgCode(rs.getString("INVENTORY_ORG_CODE"));
                            tmp.setItemNumber(rs.getString("ITEM_NUMBER"));
                            tmp.setQuantity(rs.getDouble("QUANTITY"));
                            tmp.setShippedQuantity(rs.getDouble("SHIPPED_QUANTITY"));
                            tmp.setUomCode(rs.getString("UOM_CODE"));
                            tmp.setUnitPrice(rs.getDouble("UNIT_PRICE"));
                            tmp.setCurr(rs.getString("CURRENCY_CODE"));
                            tmp.setDeliveryDate(rs.getDate("DELIVERY_DATE"));
                            tmp.setRowId(rs.getString("ROW_ID"));
                            tmp.setContHeaderId(rs.getLong("K_HEADER_ID"));
                            tmp.setContLineId(rs.getString("K_LINE_ID"));
                            tmp.setDeliverableId(rs.getLong("DELIVERABLE_ID"));
                            tmp.setInventoryOrgId(rs.getLong("INVENTORY_ORG_ID"));
                            tmp.setItemId(rs.getLong("ITEM_ID"));
                            tmp.setProjectId(rs.getLong("PROJECT_ID"));
                            tmp.setTaskId(rs.getLong("TASK_ID"));
                            tmp.setLineStyle(rs.getString("LINE_STYLE"));
                            tmp.setProjectName(rs.getString("PROJECT_NAME"));
                            tmp.setProjectNumber(rs.getString("PROJECT_NUMBER"));
                            tmp.setLineDescription(rs.getString("LINE_DESCRIPTION"));

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
     * 获取开票申请列表
     * @param deliverablesId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getBillEventList(Long contId,Long deliverablesId, Long billEventId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<BillEventDto> resultList = (List<BillEventDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_CONTRACT_BILL_EVENTS(?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == contId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, contId);
                        if (null == deliverablesId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, deliverablesId);
                        if (null == billEventId)
                            cs.setBigDecimal(3, null);
                        else
                            cs.setLong(3, billEventId);
                        cs.setLong(4, pageNo);// 设置输入参数的值
                        cs.setLong(5, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(6, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<BillEventDto> results = new ArrayList<BillEventDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(7));
                        BillEventDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new BillEventDto();
                            tmp.setBillEventId(rs.getLong("BILLING_EVENT_ID"));
                            tmp.setContHeaderId(rs.getLong("K_HEADER_ID"));
                            tmp.setContLineId(rs.getString("K_LINE_ID"));
                            tmp.setDeliverableId(rs.getLong("DELIVERABLE_ID"));
                            tmp.setEventDate(rs.getDate("BILL_EVENT_DATE"));
                            tmp.setEventType(rs.getString("BILL_EVENT_TYPE"));
                            tmp.setPaEventId(rs.getLong("PA_EVENT_ID"));
                            tmp.setItemId(rs.getLong("BILL_ITEM_ID"));
                            tmp.setLineId(rs.getString("BILL_LINE_ID"));
                            tmp.setChgReqId(rs.getLong("BILL_CHG_REQ_ID"));
                            tmp.setProjectId(rs.getLong("BILL_PROJECT_ID"));
                            tmp.setTaskId(rs.getLong("BILL_TASK_ID"));
                            tmp.setOrgId(rs.getLong("BILL_ORGANIZATION_ID"));
                            tmp.setFundRef1(rs.getString("BILL_FUND_REF1"));
                            tmp.setFundRef2(rs.getString("BILL_FUND_REF2"));
                            tmp.setFundRef3(rs.getString("BILL_FUND_REF3"));
                            tmp.setBillOfLading(rs.getString("BILL_BILL_OF_LADING"));
                            tmp.setSerialNum(rs.getString("BILL_SERIAL_NUM"));
                            tmp.setCurr(rs.getString("BILL_CURRENCY_CODE"));
                            tmp.setRateType(rs.getString("BILL_RATE_TYPE"));
                            tmp.setRateDate(rs.getDate("BILL_RATE_DATE"));
                            tmp.setExchangeRate(rs.getDouble("BILL_EXCHANGE_RATE"));
                            tmp.setDescription(rs.getString("BILL_DESCRIPTION"));
                            tmp.setQuantity(rs.getDouble("BILL_QUANTITY"));
                            tmp.setUnitPrice(rs.getDouble("BILL_UNIT_PRICE"));
                            tmp.setRevenueAmount(rs.getDouble("REVENUE_AMOUNT"));
                            tmp.setInitiatedFlag(rs.getString("INITIATED_FLAG"));
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
                            tmp.setProjectNumber(rs.getString("PROJECT_NUMBER"));
                            tmp.setProjectName(rs.getString("PROJECT_NAME"));
                            tmp.setBillEventNumber(rs.getString("BILLEVENTNUMBER"));
                            tmp.setLineNumber(rs.getString("LINENUMBER"));
                            tmp.setLineDescription(rs.getString("LINE_DESCRIPTION"));
                            tmp.setLineStyle(rs.getString("LINE_STYLE"));
                            tmp.setTaskName(rs.getString("TASKNAME"));
                            tmp.setProcessedFlag(rs.getString("PROCESSEDFLAG"));
                            tmp.setAmount(rs.getString("AMOUNT"));
                            tmp.setDeliverablePrice(rs.getString("DELIVERABLE_PRICE"));
                            tmp.setState(rs.getString("STATE"));
                            tmp.setCustName(rs.getString("CUST_NAME"));

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
     * 获取开票类型
     * @param projectId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getEventTypeList(long projectId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<EventTypeDto> resultList = (List<EventTypeDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_BILL_EVENT_TYPE(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<EventTypeDto> results = new ArrayList<EventTypeDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(3));
                        EventTypeDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new EventTypeDto();
                            tmp.setEventType(rs.getString("EVENT_TYPE"));
                            tmp.setEventTypeName(rs.getString("EVENT_TYPE_NAME"));

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
     * 获取发票类型
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getInvoiceTypeList() {
        DataTablesDTO dt = new DataTablesDTO();
        List<InvoiceTypeDto> resultList = (List<InvoiceTypeDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_BILL_INVOICE_TYPE(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(2, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<InvoiceTypeDto> results = new ArrayList<InvoiceTypeDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(2));
                        InvoiceTypeDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new InvoiceTypeDto();
                            tmp.setInvoiceType(rs.getString("INVOICE_TYPE"));

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
     * 获取任务列表
     * @param projectId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getTaskList(long projectId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<TaskDto> resultList = (List<TaskDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_TASK(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskDto> results = new ArrayList<TaskDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(3));
                        TaskDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TaskDto();
                            tmp.setTaskId(rs.getLong("TASK_ID"));
                            tmp.setTaskNumber(rs.getString("TASK_NUMBER"));
                            tmp.setTaskName(rs.getString("TASK_NAME"));

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
     * 获取币别列表
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getCurrList() {
        DataTablesDTO dt = new DataTablesDTO();
        List<CurrDto> resultList = (List<CurrDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_CURRENCY(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(2, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<CurrDto> results = new ArrayList<CurrDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(2));
                        CurrDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new CurrDto();
                            tmp.setCurrCode(rs.getString("CURRENCY_CODE"));
                            tmp.setCurrName(rs.getString("DESCRIPTION"));

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
     * 获取汇率类型
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getRateTypeList() {
        DataTablesDTO dt = new DataTablesDTO();
        List<RateTypeDto> resultList = (List<RateTypeDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_RATE_TYPE(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(2, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<RateTypeDto> results = new ArrayList<RateTypeDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(2));
                        RateTypeDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new RateTypeDto();
                            tmp.setRateType(rs.getString("CONVERSION_TYPE"));
                            tmp.setRateTypeName(rs.getString("USER_CONVERSION_TYPE"));
                            tmp.setRateTypeDesc(rs.getString("DESCRIPTION"));

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
     * 获取组织列表
     * @param orgId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getOrgList(long orgId) {
        DataTablesDTO dt = new DataTablesDTO();
//        dt.setPageNo(pageNo);
//        dt.setPageSize(pageSize);
        List<OrgDto> resultList = (List<OrgDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_ORG(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, orgId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<OrgDto> results = new ArrayList<OrgDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(3));
                        OrgDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new OrgDto();
                            tmp.setOrgName(rs.getString("NAME"));
                            tmp.setOrgId(rs.getLong("ORGANIZATION_ID"));

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
     * 获取汇率
     * @param fromCurr
     * @param toCurr
     * @param rateType
     * @param rateDate
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public String getRate(String fromCurr, String toCurr, String rateType, String rateDate) {
        String i = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_DAY_RATE(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, fromCurr);
                        cs.setString(2, toCurr);
                        cs.setString(3, rateType);
                        cs.setString(4,rateDate);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return cs.getString(5);
                    }
                });
        return i;
    }

    /**
     * 获取合同开票金额等信息
     * @param contId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getContInvoices(Long contId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<ContInvoiceDto> resultList = (List<ContInvoiceDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.GET_CONT_INVOICE_AMOUNT(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == contId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, contId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ContInvoiceDto> results = new ArrayList<ContInvoiceDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(3));
                        ContInvoiceDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContInvoiceDto();
                            tmp.setContAmount(rs.getLong("contAmount"));
                            tmp.setInvoiceAmount(rs.getLong("invoiceAmount"));
                            tmp.setInvoicePro(rs.getString("invoicePro"));
                            tmp.setWayAmount(rs.getLong("wayAmount"));
                            tmp.setWayPro(rs.getString("wayPro"));

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
     * 删除开票申请
     * @param billEventId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map delBillEvent(Long  billEventId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.DELETE_BILL_EVENT(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, billEventId);
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

    /**
     * 保存或提交开票申请
     * @param billEventDto
     * @param insertType
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map addOrUpBillEvent(BillEventDto billEventDto, String insertType, Long userId) {
        billEventDto.setDateAndUser(userId);
        List<BillEventEntity> dataList = new ArrayList<BillEventEntity>();
        dataList.add(this.billEventDtoToEntity(billEventDto));
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_OKE_K_BILLING_EVENTS_PKG.INSERT_BILL_EVENT(?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_OKE_K_BILLING_EVENTS_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.setString(2, insertType);
                        cs.setLong(3, userId);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        cs.registerOutParameter(6, OracleTypes.NUMBER);
                        cs.registerOutParameter(7, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(EventParm.DB_SID, cs.getLong(4));
                        tmp.put(EventParm.DB_URL, cs.getString(5));
                        tmp.put(EventParm.DB_STATE, cs.getLong(6));
                        tmp.put(EventParm.DB_MSG, cs.getString(7));
                        return tmp;
                    }
                });
        return map;
    }
}
