package com.nerin.nims.opt.navi.service;

import com.nerin.nims.opt.navi.dto.OaWorkFlowDTO;
import com.nerin.nims.opt.navi.dto.ProjectListDTO;
import com.nerin.nims.opt.navi.dto.ProjectOverDueDTO;
import com.nerin.nims.opt.navi.dto.ProjectPhaseDTO;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import oracle.jdbc.OracleTypes;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 7/15/2016.
 */
@Component
@Transactional
public class NaviIndexService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public String getOAurl() {
        String sql = "SELECT b.profile_option_value FROM fnd_profile_options a, fnd_profile_option_values b WHERE a.profile_option_id = b.profile_option_id"
                + " AND a.application_id = b.application_id AND a.profile_option_name IN ('CUX_OA_APPROVE_URL') AND level_id = 10001";
        String data = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                String result = "";
                while(rs.next()) {
                    result = rs.getString("profile_option_value");
                }
                return result;
            }});
        return data;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getProJoinLogList(Long userId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<ProjectListDTO> resultList = (List<ProjectListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NAVI_PROJ_PKG.last_project_list(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, userId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectListDTO> results = new ArrayList<ProjectListDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ProjectListDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectListDTO();
                            tmp.setProjectId(rs.getLong("PROJECT_ID"));
                            tmp.setProjectNumber(rs.getString("PROJECT_NUMBER"));
                            tmp.setProjectManager(rs.getString("PROJECT_MANAGER"));
                            tmp.setProjectName(rs.getString("PROJECT_NAME"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }
  //任务超期列表--已取消
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getProOverdueList(Long userId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        List<ProjectListDTO> resultList = (List<ProjectListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NAVI_PROJ_PKG.task_overdue(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, userId);
                        cs.setLong(2, pageNo);
                        cs.setLong(3, pageSize);
                        cs.setBigDecimal(4, new BigDecimal("-1"));
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectListDTO> results = new ArrayList<ProjectListDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(5));
                        ProjectListDTO tmp = null;
                        while (rs.next()) {
                            tmp = new ProjectListDTO();
                            tmp.setProjectId(rs.getLong("PROJECT_ID"));
                            tmp.setProjectNumber(rs.getString("PROJECT_NUMBER"));
                            tmp.setProjectManager(rs.getString("PROJECT_MANAGER"));
                            tmp.setProjectName(rs.getString("PROJECT_NAME"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }

  //任务超期列表
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getProOverdueList1(Long userId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        List<ProjectOverDueDTO> resultList = (List<ProjectOverDueDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NAVI_PROJ_PKG.task_overdue1(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, userId);
                        cs.setLong(2, pageNo);
                        cs.setLong(3, pageSize);
                        cs.setBigDecimal(4, new BigDecimal("-1"));
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectOverDueDTO> results = new ArrayList<ProjectOverDueDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(5));
                        ProjectOverDueDTO tmp = null;
                        while (rs.next()) {
                            tmp = new ProjectOverDueDTO();
                            tmp.setProjectId(rs.getLong("project_id"));
                            tmp.setProjName(rs.getString("proj_name"));
                            tmp.setElementNumber(rs.getString("element_number"));
                            tmp.setElementName(rs.getString("element_name"));
                            tmp.setDescription(rs.getString("description"));
                            tmp.setPhaseCode(rs.getLong("phase_code"));
                            tmp.setSpecCode(rs.getString("spec_code"));
                            tmp.setWorkpkgType(rs.getString("workpkg_type"));
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
    public DataTablesDTO getProMyAllList(Long userId, String desc, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        List<ProjectListDTO> resultList = (List<ProjectListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NAVI_PROJ_PKG.my_project_list(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, userId);
                        cs.setString(2, desc.toLowerCase());
                        cs.setLong(3, pageNo);
                        cs.setLong(4, pageSize);
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectListDTO> results = new ArrayList<ProjectListDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(5));
                        ProjectListDTO tmp = null;
                        while (rs.next()) {
                            tmp = new ProjectListDTO();
                            tmp.setProjectId(rs.getLong("PROJECT_ID"));
                            tmp.setProjectNumber(rs.getString("PROJECT_NUMBER"));
                            tmp.setProjectManager(rs.getString("PROJECT_MANAGER"));
                            tmp.setProjectName(rs.getString("PROJECT_NAME"));
                            tmp.setProjectLongName(rs.getString("PROJECT_LONG_NAME"));
                            tmp.setCustomer(rs.getString("CUSTOMER"));
                            tmp.setProjectStatus(rs.getString("PROJECT_STATUS"));
                            tmp.setProjectType(rs.getString("PROJECT_TYPE"));
                            tmp.setProjectClass(rs.getString("PROJECT_CLASS"));
                            tmp.setProjectOrgName(rs.getString("PROJECT_ORG_NAME"));
                            tmp.setProjStartDate(rs.getDate("PROJ_START_DATE"));
                            tmp.setProjEndDate(rs.getDate("PROJ_END_DATE"));
                            tmp.setStartDate(rs.getDate("START_DATE"));
                            tmp.setCompletionDate(rs.getDate("COMPLETION_DATE"));
                            tmp.setOverdue(rs.getString("OVERDUE"));
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
    public List<ProjectPhaseDTO> getProPhaseList(Long proId) {
        List<ProjectPhaseDTO> resultList = (List<ProjectPhaseDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NAVI_PROJ_PKG.project_phase(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, proId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectPhaseDTO> results = new ArrayList<ProjectPhaseDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ProjectPhaseDTO tmp = null;
                        while (rs.next()) {
                            tmp = new ProjectPhaseDTO();
                            tmp.setProjectId(rs.getLong("PROJECT_ID"));
                            tmp.setPhaseCode(rs.getString("PHASE_CODE"));
                            tmp.setPhaseName(rs.getString("PHASE_NAME"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    @SuppressWarnings("unchecked")
    public void insertVisitPro(Long userId, Long proId, String proNum, String proManager, String proName) {
        jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NAVI_PROJ_PKG.insert_browse(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, userId);
                        cs.setLong(2, proId);
                        cs.setString(3, proNum);
                        cs.setString(4, proManager);
                        cs.setString(5, proName);
                        cs.registerOutParameter(6, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return null;
                    }
                });
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<OaWorkFlowDTO> getOaOverAll(String xmbh, Long id, Long formId, String status) {
        String sql = "SELECT * FROM workflow_jzy_view@OAEBS t where t.xmbh = '" + xmbh + "'";
        if (null != id)
            sql += " and t.id = " + id;
        if (null != formId)
            sql += " and t.formid = " + formId;
        if (StringUtils.isNotEmpty(status))
            sql += " and t.status in (" + status + ")";
        List<OaWorkFlowDTO> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
            public List<OaWorkFlowDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<OaWorkFlowDTO> result = new ArrayList<OaWorkFlowDTO>();
                OaWorkFlowDTO tmp = null;
                while(rs.next()) {
                    tmp = new OaWorkFlowDTO();
                    tmp.setWorkflowname(rs.getString("workflowname"));
                    tmp.setId(rs.getString("id"));
                    tmp.setFormid(rs.getString("formid"));
                    tmp.setLastname(rs.getString("lastname"));
                    tmp.setStatus(rs.getString("status"));
                    tmp.setCreatetime(rs.getString("createtime"));
                    tmp.setEndtime(rs.getString("endtime"));
                    tmp.setRequestid(rs.getString("requestid"));
                    tmp.setWorkcode(rs.getString("workcode"));
                    tmp.setXmbh(rs.getString("xmbh"));
                    tmp.setXmmc(rs.getString("xmmc"));
                    result.add(tmp);
                }
                return result;
            }});
        return data;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<OaWorkFlowDTO> getBuildingInstitutePro(String xmbh, Long id, Long formId, String zy, String status) {
        String sql = "SELECT * FROM workflow_jzyjd1_view@OAEBS t where t.xmbh = '" + xmbh + "'";
        if (null != id)
            sql += " and t.id = " + id;
        if (null != formId)
            sql += " and t.formid = " + formId;
        if (StringUtils.isNotEmpty(status))
            sql += " and t.status in (" + status + ")";
        if (StringUtils.isNotEmpty(zy))
            sql += " and t.zy in (" + zy + ")";
        List<OaWorkFlowDTO> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
            public List<OaWorkFlowDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<OaWorkFlowDTO> result = new ArrayList<OaWorkFlowDTO>();
                OaWorkFlowDTO tmp = null;
                while(rs.next()) {
                    tmp = new OaWorkFlowDTO();
                    tmp.setWorkflowname(rs.getString("workflowname"));
                    tmp.setId(rs.getString("id"));
                    tmp.setFormid(rs.getString("formid"));
                    tmp.setLastname(rs.getString("lastname"));
                    tmp.setStatus(rs.getString("status"));
                    tmp.setCreatetime(rs.getString("createtime"));
                    tmp.setEndtime(rs.getString("endtime"));
                    tmp.setRequestid(rs.getString("requestid"));
                    tmp.setWorkcode(rs.getString("workcode"));
                    tmp.setXmbh(rs.getString("xmbh"));
                    tmp.setXmmc(rs.getString("xmmc"));
                    tmp.setZy(rs.getString("zy"));
                    result.add(tmp);
                }
                return result;
            }});
        return data;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<OaWorkFlowDTO> getBuildingInstitutePro23(String xmbh, String zx, String zy, String status, String types) {
        List<OaWorkFlowDTO> allData = new ArrayList<OaWorkFlowDTO>();
        if (0 <= types.indexOf("01")) {
            String sql = "SELECT * FROM workflow_jzytzjs_view@OAEBS t where t.xmbh = '" + xmbh + "'";
                sql += " and t.id = " + "4233";
                sql += " and t.formid = " + "324";
            if (StringUtils.isNotEmpty(status))
                sql += " and t.status in (" + status + ")";
            if (StringUtils.isNotEmpty(zy))
                sql += " and t.zy in (" + zy + ")";
            if (StringUtils.isNotEmpty(zx))
                sql += " and t.zxbh in (" + zx + ")";
            List<OaWorkFlowDTO> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
                public List<OaWorkFlowDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<OaWorkFlowDTO> result = new ArrayList<OaWorkFlowDTO>();
                    OaWorkFlowDTO tmp = null;
                    while(rs.next()) {
                        tmp = new OaWorkFlowDTO();
                        tmp.setWorkflowname(rs.getString("workflowname"));
                        tmp.setId(rs.getString("id"));
                        tmp.setFormid(rs.getString("formid"));
                        tmp.setLastname(rs.getString("lastname"));
                        tmp.setStatus(rs.getString("status"));
                        tmp.setCreatetime(rs.getString("createtime"));
                        tmp.setEndtime(rs.getString("endtime"));
                        tmp.setRequestid(rs.getString("requestid"));
                        tmp.setWorkcode(rs.getString("workcode"));
                        tmp.setXmbh(rs.getString("xmbh"));
                        tmp.setXmmc(rs.getString("xmmc"));
                        tmp.setZy(rs.getString("zy"));
                        tmp.setZxh(rs.getString("zxh"));
                        tmp.setZxm(rs.getString("zxm"));
                        tmp.setSjr(rs.getString("sjr"));
                        tmp.setJhr(rs.getString("jhr"));
                        tmp.setShr(rs.getString("shr"));
                        tmp.setSdr(rs.getString("sdr"));
                        tmp.setZcs(rs.getString("zcs"));
                        tmp.setFasjr(rs.getString("fasjr"));
                        result.add(tmp);
                    }
                    return result;
                }});
            allData.addAll(data);
        }
        if (0 <= types.indexOf("03")) {
            String sql = "SELECT * FROM workflow_jzyjssjs_view@OAEBS t where t.xmbh = '" + xmbh + "'";
                sql += " and t.id = " + "4300";
                sql += " and t.formid = " + "340";
            if (StringUtils.isNotEmpty(status))
                sql += " and t.status in (" + status + ")";
            if (StringUtils.isNotEmpty(zy))
                sql += " and t.zy in (" + zy + ")";
            if (StringUtils.isNotEmpty(zx))
                sql += " and t.zxbh in (" + zx + ")";
            List<OaWorkFlowDTO> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
                public List<OaWorkFlowDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<OaWorkFlowDTO> result = new ArrayList<OaWorkFlowDTO>();
                    OaWorkFlowDTO tmp = null;
                    while(rs.next()) {
                        tmp = new OaWorkFlowDTO();
                        tmp.setWorkflowname(rs.getString("workflowname"));
                        tmp.setId(rs.getString("id"));
                        tmp.setFormid(rs.getString("formid"));
                        tmp.setLastname(rs.getString("lastname"));
                        tmp.setStatus(rs.getString("status"));
                        tmp.setCreatetime(rs.getString("createtime"));
                        tmp.setEndtime(rs.getString("endtime"));
                        tmp.setRequestid(rs.getString("requestid"));
                        tmp.setWorkcode(rs.getString("workcode"));
                        tmp.setXmbh(rs.getString("xmbh"));
                        tmp.setXmmc(rs.getString("xmmc"));
                        tmp.setZy(rs.getString("zy"));
                        tmp.setZxh(rs.getString("zxh"));
                        tmp.setZxm(rs.getString("zxm"));
                        tmp.setSjr(rs.getString("sjr"));
                        tmp.setJhr(rs.getString("jhr"));
                        tmp.setShr(rs.getString("shr"));
                        tmp.setSdr(rs.getString("sdr"));
                        tmp.setZcs(rs.getString("zcs"));
                        tmp.setFasjr(rs.getString("fasjr"));
                        result.add(tmp);
                    }
                    return result;
                }});
            allData.addAll(data);
        }
        if (0 <= types.indexOf("05")) {
            String sql = "SELECT * FROM workflow_jzytjdsp_view@OAEBS t where t.xmbh = '" + xmbh + "'";
                sql += " and t.id = " + "4301";
                sql += " and t.formid = " + "340";
            if (StringUtils.isNotEmpty(status))
                sql += " and t.status in (" + status + ")";
            if (StringUtils.isNotEmpty(zy))
                sql += " and t.zy in (" + zy + ")";
            if (StringUtils.isNotEmpty(zx))
                sql += " and t.zxbh in (" + zx + ")";
            List<OaWorkFlowDTO> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
                public List<OaWorkFlowDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                    List<OaWorkFlowDTO> result = new ArrayList<OaWorkFlowDTO>();
                    OaWorkFlowDTO tmp = null;
                    while(rs.next()) {
                        tmp = new OaWorkFlowDTO();
                        tmp.setWorkflowname(rs.getString("workflowname"));
                        tmp.setId(rs.getString("id"));
                        tmp.setFormid(rs.getString("formid"));
                        tmp.setLastname(rs.getString("lastname"));
                        tmp.setStatus(rs.getString("status"));
                        tmp.setCreatetime(rs.getString("createtime"));
                        tmp.setEndtime(rs.getString("endtime"));
                        tmp.setRequestid(rs.getString("requestid"));
                        tmp.setWorkcode(rs.getString("workcode"));
                        tmp.setXmbh(rs.getString("xmbh"));
                        tmp.setXmmc(rs.getString("xmmc"));
                        tmp.setZy(rs.getString("zy"));
                        tmp.setZxh(rs.getString("zxh"));
                        tmp.setZxm(rs.getString("zxm"));
                        tmp.setSjr(rs.getString("sjr"));
                        tmp.setJhr(rs.getString("jhr"));
                        tmp.setShr(rs.getString("shr"));
                        tmp.setSdr(rs.getString("sdr"));
                        tmp.setTjjsr(rs.getString("tjjsr"));
                        result.add(tmp);
                    }
                    return result;
                }});
            allData.addAll(data);
        }
        return allData;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<OaWorkFlowDTO> getDepartmentOA(String id, String formId, Long erpId, Long erpId2) {
        String sql = "SELECT DISTINCT T.WORKFLOWNAME, T.LASTNAME, T.STATUS, T.CREATETIME, T.ENDTIME, T.REQUESTID, T.WORKCODE, T.ERPID, T.ERPID2,"
                + "  T.APPROVER, T.APPROVERID FROM WORKFLOW_XZPT_VIEW@OAEBS T WHERE T.FLOWID in (" + id + ")"
                + " and T.FORMID in (" + formId + ") and T.erpId = " + erpId + " and t.erpId2 = " + erpId2;
        List<OaWorkFlowDTO> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
            public List<OaWorkFlowDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<OaWorkFlowDTO> result = new ArrayList<OaWorkFlowDTO>();
                OaWorkFlowDTO tmp = null;
                while(rs.next()) {
                    tmp = new OaWorkFlowDTO();
                    tmp.setWorkflowname(rs.getString("workflowname"));
                    tmp.setLastname(rs.getString("lastname"));
                    tmp.setStatus(rs.getString("status"));
                    tmp.setCreatetime(rs.getString("createtime"));
                    tmp.setEndtime(rs.getString("endtime"));
                    tmp.setRequestid(rs.getString("requestid"));
                    tmp.setWorkcode(rs.getString("workcode"));
                    tmp.setErpId(rs.getString("ERPID"));
                    tmp.setErpId2(rs.getString("ERPID2"));
                    tmp.setApprover(rs.getString("APPROVER"));
                    tmp.setApproverId(rs.getString("APPROVERID"));
                    result.add(tmp);
                }
                return result;
            }});
        return data;
    }
}
