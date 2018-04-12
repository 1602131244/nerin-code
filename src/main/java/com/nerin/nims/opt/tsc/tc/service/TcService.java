package com.nerin.nims.opt.tsc.tc.service;

import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import com.nerin.nims.opt.tsc.tc.dto.*;
import com.nerin.nims.opt.tsc.tc.module.AttachmentEntity;
import com.nerin.nims.opt.tsc.tc.module.ContactEntity;
import com.nerin.nims.opt.tsc.tc.module.CustomerEntity;
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
 * Created by yinglgu on 11/14/2016.
 */
@Component
@Transactional
public class TcService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private List<ContactEntity> contactDtoToEntity(List<ContactDTO> datas) {
        ContactEntity entity = null;
        List<ContactEntity> newList = new ArrayList<ContactEntity>();
        for (ContactDTO c : datas) {
            entity = new ContactEntity();
            try {
                PropertyUtils.copyProperties(entity, c);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            newList.add(entity);
        }
        return newList;
    }

    private List<AttachmentEntity> attachmentDtoToEntity(List<AttachmentDTO> datas) {
        AttachmentEntity entity = null;
        List<AttachmentEntity> newList = new ArrayList<AttachmentEntity>();
        for (AttachmentDTO a : datas) {
            entity = new AttachmentEntity();
            try {
                PropertyUtils.copyProperties(entity, a);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            newList.add(entity);
        }
        return newList;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map submitHeader(Long customerId, Long userId) {
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.customer_submit(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, customerId);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(NbccParm.DB_STATE, cs.getString(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(3));
                        return tmp;
                    }
                });
        return map;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map auditHeader(Long customerId, Long userId, String code) {
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.customer_approve(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, customerId);
                        cs.setLong(2, userId);
                        cs.setString(3, code);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(NbccParm.DB_STATE, cs.getString(5));
                        tmp.put(NbccParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map addHeader(CustomerDTO customerDTO, Long userId) {
        CustomerEntity supplier = new CustomerEntity();
        try {
            PropertyUtils.copyProperties(supplier, customerDTO);
            supplier.setRequestDate(new Date());
            supplier.setRequestUserId(userId);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        List<CustomerEntity> supList = new ArrayList<CustomerEntity>();
        List<ContactEntity> contactList = new ArrayList<ContactEntity>();
        List<AttachmentEntity> attachmentList = new ArrayList<AttachmentEntity>();
        contactList.addAll(this.contactDtoToEntity(customerDTO.getContacts()));
        attachmentList.addAll(this.attachmentDtoToEntity(customerDTO.getAttachments()));
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.customer_insert(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor conDesc = ArrayDescriptor.createDescriptor("APPS.CUSTOMER_CONTACT_TBL", or);
                        ARRAY conArray = new ARRAY(conDesc, or, contactList.toArray());
                        ArrayDescriptor attDesc = ArrayDescriptor.createDescriptor("APPS.CUSTOMER_ATTACHMENT_TBL", or);
                        ARRAY attArray = new ARRAY(attDesc, or, attachmentList.toArray());

                        ArrayDescriptor supDesc = ArrayDescriptor.createDescriptor("APPS.CUSTOMER_TBL", or);
                        supList.add(supplier);
                        ARRAY supArray = new ARRAY(supDesc, or, supList.toArray());

                        cs.setArray(1, supArray);
                        cs.setArray(2, conArray);
                        cs.setArray(3, attArray);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(NbccParm.DB_SID, cs.getLong(4));
                        tmp.put(NbccParm.DB_STATE, cs.getString(6));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getCustomerHeaderList(Long customerId, String partyName, String status, Long userId, String name, String source, String attr1, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<CustomerDTO> resultList = (List<CustomerDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.get_customer_header_page(?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == customerId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, customerId);// 设置输入参数的值
                        cs.setString(2, partyName);// 设置输入参数的值
                        cs.setString(3, status);// 设置输入参数的值
                        if (null == userId)
                            cs.setBigDecimal(4, null);
                        else
                            cs.setLong(4, userId);// 设置输入参数的值
                        cs.setString(5, name);// 设置输入参数的值
                        cs.setString(6, source);// 设置输入参数的值
                        cs.setString(7, attr1);// 设置输入参数的值
                        cs.setLong(8, pageNo);// 设置输入参数的值
                        cs.setLong(9, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(10, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(11, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<CustomerDTO> results = new ArrayList<CustomerDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(10);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(11));
                        CustomerDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new CustomerDTO();
                            tmp.setCustomerId(rs.getLong("customer_id"));
                            tmp.setPartyName(rs.getString("party_name"));
                            tmp.setOrganizationName(rs.getString("organization_name"));
                            tmp.setCustomerSource(rs.getString("customer_source"));
                            tmp.setCustomerCategory(rs.getString("customer_category"));
                            tmp.setCustomerLevel(rs.getString("customer_level"));
                            tmp.setCustomerIndustry(rs.getString("customer_industry"));
                            tmp.setAddress(rs.getString("address"));
                            tmp.setVatRegistrationNum(rs.getString("vat_registration_num"));
                            tmp.setKnownAs(rs.getString("known_as"));
                            tmp.setCustomerUrl(rs.getString("customer_url"));
                            tmp.setCommets(rs.getString("commets"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setOaUrl(rs.getString("oa_url"));
                            tmp.setRequestUserId(rs.getLong("request_user_id"));
                            tmp.setRequestDate(rs.getDate("request_date"));
                            tmp.setApprovedUserId(rs.getLong("approved_user_id"));
                            tmp.setApprovedDate(rs.getDate("approved_date"));
                            tmp.setOrgId(rs.getLong("org_id"));
                            tmp.setStatusCode(rs.getString("status_code"));
                            tmp.setCustomerCategoryCode(rs.getString("customer_category_code"));
                            tmp.setCustomerSourceCode(rs.getString("customer_source_code"));
                            tmp.setCustomerLevelCode(rs.getString("customer_level_code"));
                            tmp.setCustomerIndustryCode(rs.getString("customer_industry_code"));
                            tmp.setOrgId(rs.getLong("org_id"));
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
                            tmp.setAttachmentExists(rs.getString("attachment_exists"));
                            tmp.setPersonName(rs.getString("person_name"));

                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getEbsHeaderList(String p_party_name, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<CustomerDTO> resultList = (List<CustomerDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.get_ebs_customer_page2(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, p_party_name);// 设置输入参数的值
                        cs.setLong(2, pageNo);// 设置输入参数的值
                        cs.setLong(3, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<CustomerDTO> results = new ArrayList<CustomerDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(5));
                        CustomerDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new CustomerDTO();
                            tmp.setPartyNumber(rs.getString("party_number"));
                            tmp.setPartyName(rs.getString("party_name"));
                            tmp.setOrganizationName(rs.getString("organization_name"));
                            tmp.setKnownAs(rs.getString("known_as"));
                            tmp.setCustomerCategoryCode(rs.getString("customer_category"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ContactDTO> getContact(Long customerId) {
        List<ContactDTO> resultList = (List<ContactDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.get_customer_contact(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, customerId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ContactDTO> results = new ArrayList<ContactDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ContactDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ContactDTO();
                            tmp.setCustomerId(rs.getLong("customer_id"));
                            tmp.setContactId(rs.getLong("contact_id"));
                            tmp.setLineNum(rs.getLong("line_num"));
                            tmp.setPersonName(rs.getString("person_name"));
                            tmp.setPersonTitle(rs.getString("person_title"));
                            tmp.setDepartment(rs.getString("department"));
                            tmp.setMobile(rs.getString("mobile"));
                            tmp.setPhone(rs.getString("phone"));
                            tmp.setEmailAddress(rs.getString("email_address"));
                            tmp.setContactPersonName(rs.getString("contact_person_name"));
                            tmp.setComments(rs.getString("comments"));
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
    public List<AttachmentDTO> getAttachmentContact(Long customerId) {
        List<AttachmentDTO> resultList = (List<AttachmentDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.get_attachment_contact(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, customerId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<AttachmentDTO> results = new ArrayList<AttachmentDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        AttachmentDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new AttachmentDTO();
                            tmp.setCustomerId(rs.getLong("customer_id"));
                            tmp.setFileId(rs.getLong("file_id"));
                            tmp.setLineNum(rs.getLong("line_num"));
                            tmp.setFileName(rs.getString("file_name"));
                            tmp.setFileUrl(rs.getString("file_url"));
                            tmp.setCreationDate(rs.getTimestamp("creation_date"));
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

                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map delContact(Long customerId, Long contactId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.contact_delete(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, customerId);
                        cs.setLong(2, contactId);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getString(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(3));
                        return tmp;
                    }
                });
        return map;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map delAttachment(Long customerId, Long fileId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.attachment_delete(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, customerId);
                        cs.setLong(2, fileId);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getString(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(3));
                        return tmp;
                    }
                });
        return map;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<SourceDTO> getCustomerSource() {
        List<SourceDTO> resultList = (List<SourceDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.get_customer_source(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(2, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<SourceDTO> results = new ArrayList<SourceDTO>();
                        cs.execute();
                        SourceDTO tmp = null;
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SourceDTO();
                            tmp.setSourceKey(rs.getString("flex_value"));
                            tmp.setSourceValue(rs.getString("description"));
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
    public List<SourceDTO> getCustomerLevel() {
        List<SourceDTO> resultList = (List<SourceDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.get_customer_level(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(2, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<SourceDTO> results = new ArrayList<SourceDTO>();
                        cs.execute();
                        SourceDTO tmp = null;
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SourceDTO();
                            tmp.setSourceKey(rs.getString("flex_value"));
                            tmp.setSourceValue(rs.getString("description"));
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
    public List<SourceDTO> getCustomerIndustry() {
        List<SourceDTO> resultList = (List<SourceDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.get_customer_industry(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(2, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<SourceDTO> results = new ArrayList<SourceDTO>();
                        cs.execute();
                        SourceDTO tmp = null;
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SourceDTO();
                            tmp.setSourceKey(rs.getString("flex_value"));
                            tmp.setSourceValue(rs.getString("description"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    public UserOrgDTO getUserOrg(Long userId) {
        UserOrgDTO dto = (UserOrgDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.get_org(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, userId);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        UserOrgDTO tmp = new UserOrgDTO();
                        tmp.setOrganizationId(cs.getLong(2));
                        tmp.setOrganizationName(cs.getString(3));
                        tmp.setOrgId(cs.getLong(4));
                        tmp.setOrgName(cs.getString(5));
                        return tmp;
                    }
                });
        return dto;
    }
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getCustomerLxr(String pXing, String pMing,String pAddress,String pCustomerName,String pCustomerHy,long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<LxrDTO> resultList = (List<LxrDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ar_customer_pub_pkg.get_customer_lxr(?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, pXing);// 设置输入参数的值
                        cs.setString(2, pMing);// 设置输入参数的值
                        cs.setString(3, pAddress);// 设置输入参数的值
                        cs.setString(4, pCustomerName);// 设置输入参数的值
                        cs.setString(5, pCustomerHy);// 设置输入参数的值
                        cs.setLong(6, pageNo);// 设置输入参数的值
                        cs.setLong(7, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(8, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(9, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<LxrDTO> results = new ArrayList<LxrDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(8);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(9));
                        LxrDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new LxrDTO();
                            tmp.setPersonName(rs.getString("person_name"));
                            tmp.setCustomerName(rs.getString("customer_name"));
                            tmp.setAttribute1(rs.getString("attribute1"));
                            tmp.setAttribute2(rs.getString("attribute2"));
                            tmp.setEmail(rs.getString("email"));
                            tmp.setPhone(rs.getString("phone"));
                            tmp.setAddress(rs.getString("address"));
                            tmp.setCustI(rs.getString("cust_i"));
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
