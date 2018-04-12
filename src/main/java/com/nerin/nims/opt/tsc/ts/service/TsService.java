package com.nerin.nims.opt.tsc.ts.service;

import com.nerin.nims.opt.app.domain.UserMenu;
import com.nerin.nims.opt.app.repository.UserMenuRepository;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import com.nerin.nims.opt.tsc.ts.dto.AttachmentDTO;
import com.nerin.nims.opt.tsc.ts.dto.ContactDTO;
import com.nerin.nims.opt.tsc.ts.dto.SupplierDTO;
import com.nerin.nims.opt.tsc.ts.module.ApproveEntity;
import com.nerin.nims.opt.tsc.ts.module.AttachmentEntity;
import com.nerin.nims.opt.tsc.ts.module.ContactEntity;
import com.nerin.nims.opt.tsc.ts.module.SupplierEntity;
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
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by yinglgu on 11/8/2016.
 */
@Component
@Transactional
public class TsService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserMenuRepository userMenuRepository;

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
    public Map submitHeader(Long vendorId, Long userId) {
        List<UserMenu> userMenus = userMenuRepository.findByMenuId(1680l);
        List<ApproveEntity> approveEntities = new ArrayList<ApproveEntity>();
        ApproveEntity entity = null;
        for (UserMenu u : userMenus) {
            entity = new ApproveEntity();
            entity.setApprovedUser(u.getUserNo());
            approveEntities.add(entity);
        }
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_ap_supplier_pub_pkg.supplier_submit(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor conDesc = ArrayDescriptor.createDescriptor("APPS.APPROVE_TBL", or);
                        ARRAY conArray = new ARRAY(conDesc, or, approveEntities.toArray());
                        cs.setLong(1, vendorId);
                        cs.setLong(2, userId);
                        cs.setArray(3, conArray);
                        DatabaseMetaData md = con.getMetaData();
                        String url = md.getURL();
                        if (0 <= url.indexOf("192.168.15.94"))
                            cs.setString(4, "http://192.168.15.95:8080/index.html?doWork=lxgys_spr");
                        else if (0 <= url.indexOf("192.168.15.210"))
                            cs.setString(4, "http://192.168.15.211:8080/index.html?doWork=lxgys_spr");
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(NbccParm.DB_STATE, cs.getString(6));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map auditHeader(Long vendorId, Long userId, String code) {
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_ap_supplier_pub_pkg.supplier_approve(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, vendorId);
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
    public Map addHeader(SupplierDTO supplierDTO, Long userId) {
        SupplierEntity supplier = new SupplierEntity();
        try {
            PropertyUtils.copyProperties(supplier, supplierDTO);
            supplier.setRequestDate(new Date());
            supplier.setRequestUserId(userId);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        List<SupplierEntity> supList = new ArrayList<SupplierEntity>();
        List<ContactEntity> contactList = new ArrayList<ContactEntity>();
        List<AttachmentEntity> attachmentList = new ArrayList<AttachmentEntity>();
        contactList.addAll(this.contactDtoToEntity(supplierDTO.getContacts()));
        attachmentList.addAll(this.attachmentDtoToEntity(supplierDTO.getAttachments()));

        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_ap_supplier_pub_pkg.supplier_insert(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor conDesc = ArrayDescriptor.createDescriptor("APPS.CONTACT_TBL", or);
                        ARRAY conArray = new ARRAY(conDesc, or, contactList.toArray());
                        ArrayDescriptor attDesc = ArrayDescriptor.createDescriptor("APPS.ATTACHMENT_TBL", or);
                        ARRAY attArray = new ARRAY(attDesc, or, attachmentList.toArray());

                        ArrayDescriptor supDesc = ArrayDescriptor.createDescriptor("APPS.SUPPLIER_TBL", or);
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

    public DataTablesDTO getEbsSupplierHeaderList(String p_vendor_name, String p_vendor_type, String p_nature, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<SupplierDTO> resultList = (List<SupplierDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ap_supplier_pub_pkg.get_ebs_supplier_page(?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, p_vendor_name);// 设置输入参数的值
                        cs.setString(2, p_vendor_type);// 设置输入参数的值
                        cs.setString(3, p_nature);// 设置输入参数的值
                        cs.setLong(4, pageNo);// 设置输入参数的值
                        cs.setLong(5, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(6, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<SupplierDTO> results = new ArrayList<SupplierDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(7));
                        SupplierDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SupplierDTO();
                            tmp.setVendorNumber(rs.getString("vendor_number"));
                            tmp.setVendorName(rs.getString("vendor_name"));
                            tmp.setVendorTypeCode(rs.getString("vendor_type_code"));
                            tmp.setSupplierNature(rs.getString("supplier_nature"));
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
    public DataTablesDTO getSupplierHeaderList(Long vendorId, String vendorName, String status, Long userId, String name, String attr1, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<SupplierDTO> resultList = (List<SupplierDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ap_supplier_pub_pkg.get_supplier_header_page(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == vendorId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, vendorId);// 设置输入参数的值
                        cs.setString(2, vendorName);// 设置输入参数的值
                        cs.setString(3, status);// 设置输入参数的值
                        if (null == userId)
                            cs.setBigDecimal(4, null);
                        else
                            cs.setLong(4, userId);// 设置输入参数的值
                        cs.setString(5, name);// 设置输入参数的值
                        cs.setString(6, attr1);// 设置输入参数的值
                        cs.setLong(7, pageNo);// 设置输入参数的值
                        cs.setLong(8, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(9, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(10, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<SupplierDTO> results = new ArrayList<SupplierDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(9);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(10));
                        SupplierDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SupplierDTO();
                            tmp.setVendorId(rs.getLong("vendor_id"));
                            tmp.setVendorName(rs.getString("vendor_name"));
                            tmp.setBankName(rs.getString("bank_name"));
                            tmp.setBankAccountNum(rs.getString("bank_account_num"));
                            tmp.setAddress(rs.getString("address"));
                            tmp.setVendorType(rs.getString("vendor_type"));
                            tmp.setVendorTypeCode(rs.getString("vendor_type_code"));
                            tmp.setSupplierNature(rs.getString("supplier_nature"));
                            tmp.setVendorLevel(rs.getString("vendor_level"));
                            tmp.setVatRegistrationNum(rs.getString("vat_registration_num"));
                            tmp.setNum_1099(rs.getString("NUM_1099"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setStatusCode(rs.getString("status_code"));
                            tmp.setRequestUserId(rs.getLong("request_user_id"));
                            tmp.setPersonName(rs.getString("person_name"));
                            tmp.setRequestDate(rs.getDate("request_date"));
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
    public List<ContactDTO> getSupplierContact(Long vendorId) {
        List<ContactDTO> resultList = (List<ContactDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ap_supplier_pub_pkg.get_supplier_contact(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, vendorId);
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
                            tmp.setVendorId(rs.getLong("vendor_id"));
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
    public List<AttachmentDTO> getAttachmentContact(Long vendorId) {
        List<AttachmentDTO> resultList = (List<AttachmentDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ap_supplier_pub_pkg.get_attachment_contact(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, vendorId);
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
                            tmp.setVendorId(rs.getLong("vendor_id"));
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
    public Map delContact(Long vendorId, Long contactId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ap_supplier_pub_pkg.contact_delete(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, vendorId);
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
    public Map delAttachment(Long vendorId, Long fileId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ap_supplier_pub_pkg.attachment_delete(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, vendorId);
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
    public List<String> getVendorLevel() {
        List<String> resultList = (List<String>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ap_supplier_pub_pkg.get_vendor_level(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(2, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<String> results = new ArrayList<String>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        AttachmentDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            results.add(rs.getString(1));
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<String> getSupplierNature() {
        List<String> resultList = (List<String>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_ap_supplier_pub_pkg.get_supplier_nature(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(2, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<String> results = new ArrayList<String>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        AttachmentDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            results.add(rs.getString(1));
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }


}
