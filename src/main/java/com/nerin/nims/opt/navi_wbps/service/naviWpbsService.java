package com.nerin.nims.opt.navi_wbps.service;

import com.nerin.nims.opt.app.web.rest.dto.DataTableDTO;
import com.nerin.nims.opt.navi.dto.*;
import com.nerin.nims.opt.navi_wbps.dto.*;
import com.nerin.nims.opt.navi_wbps.module.DesignRevisionEntity;
import com.nerin.nims.opt.navi_wbps.module.PrintCreateDocEntity;
import com.nerin.nims.opt.navi_wbps.module.PrintCreateLineEntity;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/28.
 */
@Component
@Transactional
public class naviWpbsService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    //文本协作任务
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<naviWbpsWorkpkgwbDTO> getwbHeader(Long pElementId) {
        List<naviWbpsWorkpkgwbDTO> resultList = (List<naviWbpsWorkpkgwbDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_get_work_package_wb(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, pElementId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<naviWbpsWorkpkgwbDTO> results = new ArrayList<naviWbpsWorkpkgwbDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        naviWbpsWorkpkgwbDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new naviWbpsWorkpkgwbDTO();
                            tmp.setTaskeaderId(rs.getLong("task_header_id"));
                            tmp.setTaskName(rs.getString("task_name"));
                            tmp.setCreateName(rs.getString("creater"));
                            tmp.setTaskProgress(rs.getString("task_progress"));
                            tmp.setTaskProgress2(rs.getString("task_progress2"));
                            tmp.setTaskStatusName(rs.getString("task_status"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    //文本协作任务对应OA流程：两种流程“公司级评审申请”、“公司级评审结论”
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PhaseForOalcDTO> getwbTask(Long taskHeaderId) {
        List<PhaseForOalcDTO> resultList = (List<PhaseForOalcDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_get_workpackage_xzwb_oa(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PhaseForOalcDTO> results = new ArrayList<PhaseForOalcDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PhaseForOalcDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForOalcDTO();
                            tmp.setRequestId(rs.getLong("requestid"));
                            tmp.setRequestName(rs.getString("requestname"));
                            tmp.setCreateBy(rs.getString("creater"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            tmp.setStatus(rs.getString("status"));
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
    public List<PhaseForOalcDTO> getwbChapter(Long pElementId, Long taskHeaderId,String zhuanye) {
        List<PhaseForOalcDTO> resultList = (List<PhaseForOalcDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_get_workpackage_wb_oa(?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, pElementId);
                        cs.setLong(2, taskHeaderId);
                        cs.setString(3, zhuanye);
                        cs.registerOutParameter(4, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PhaseForOalcDTO> results = new ArrayList<PhaseForOalcDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        PhaseForOalcDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForOalcDTO();
                            tmp.setRequestId(rs.getLong("requestid"));
                            tmp.setZy(rs.getString("zy"));
                            tmp.setRequestName(rs.getString("requestname"));
                            tmp.setCreateBy(rs.getString("creater"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            tmp.setStatus(rs.getString("status"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    //计算书 OA列表
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PhaseForJssTDO> getJssOa(Long projElementId) {
        List<PhaseForJssTDO> resultList = (List<PhaseForJssTDO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_get_workpackage_jss_oa(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projElementId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PhaseForJssTDO> results = new ArrayList<PhaseForJssTDO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PhaseForJssTDO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForJssTDO();
                            tmp.setCalcuId(rs.getLong("id"));
                            tmp.setCalcuNumber(rs.getString("jssbh"));
                            tmp.setRequestName(rs.getString("requestname"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setRequestId(rs.getLong("requestid"));
                            tmp.setCreater(rs.getString("creater"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    //条件单列表
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PhaseForTjdDTO> getTjd(Long projElementId) {
        List<PhaseForTjdDTO> resultList = (List<PhaseForTjdDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_work_package_tjd(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projElementId);
                        cs.setLong(2, -1);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PhaseForTjdDTO> results = new ArrayList<PhaseForTjdDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        PhaseForTjdDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForTjdDTO();
                            tmp.setConditionId(rs.getLong("condition_id"));
                            tmp.setConditionNumber(rs.getString("condition_number"));
                            tmp.setConditionName(rs.getString("condition_name"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setSubmitDate(rs.getString("submit_date"));
                            tmp.setReceiveDate(rs.getString("receive_date"));
                            tmp.setReceivePerson(rs.getString("receive_person"));
                            tmp.setScheduleDate(rs.getString("schedule_date"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    //条件单 OA
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PhaseForOalcDTO> getTjdOa(Long conditionId) {
        List<PhaseForOalcDTO> resultList = (List<PhaseForOalcDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_work_package_tjd_oa(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, conditionId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PhaseForOalcDTO> results = new ArrayList<PhaseForOalcDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PhaseForOalcDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForOalcDTO();
                            tmp.setRequestId(rs.getLong("requestid"));
                            tmp.setRequestName(rs.getString("requestname"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setCreateBy(rs.getString("creater"));
                            tmp.setConditionNumber(rs.getString("bh"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    //生成条件单前，检查工作包数据
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public String checkGzbData(Long projectId,
                               Long elementId,
                               String submitCode,
                               String receiveCode,
                               String schleDate) {
        String resultList = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_check_gzb_data(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, elementId);
                        cs.setString(3, submitCode);
                        cs.setString(4, receiveCode);
                        cs.setString(5, schleDate);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getString(6);
                    }
                });
        return resultList;
    }

    //生成条件单
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public naviWbpsDTO createCondition(Long ProjectId,
                                       Long ElementId,
                                       Long deliverableId,
                                       String submitCode,
                                       String receiveCode,
                                       String SchleDate,
                                       Long userId,
                                       String jszyfzr) {
        naviWbpsDTO resultList = (naviWbpsDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_create_condition(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, ProjectId);
                        cs.setLong(2, ElementId);
                        cs.setLong(3, deliverableId);
                        cs.setString(4, submitCode);
                        cs.setString(5, receiveCode);
                        cs.setString(6, SchleDate);
                        cs.setLong(7, userId);
                        cs.setString(8, jszyfzr);
                        cs.registerOutParameter(9, OracleTypes.VARCHAR);
                        cs.registerOutParameter(10, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        naviWbpsDTO results = new naviWbpsDTO();
                        cs.execute();
                        results.setUrl(cs.getString(9));
                        results.setMessage(cs.getString(10));
                        return results;
                    }
                });
        return resultList;
    }

    //专业方案评审流程查询 OA
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<naviWbpsOaDTO> getZyfapsOa(Long projectId, Long phaseId) {
        List<naviWbpsOaDTO> resultList = (List<naviWbpsOaDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_zyfaps_oa(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, phaseId);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<naviWbpsOaDTO> results = new ArrayList<naviWbpsOaDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        naviWbpsOaDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new naviWbpsOaDTO();
                            tmp.setRequestId(rs.getLong("requestid"));
                            tmp.setRequestName(rs.getString("requestname"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setMajor(rs.getString("major"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setCreateBy(rs.getString("createby"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            tmp.setPhaseName(rs.getString("phasename"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    //公司级方案评审流程查询 OA
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<naviWbpsOaDTO> getGsjfapsOa(Long projectId, Long phaseId) {
        List<naviWbpsOaDTO> resultList = (List<naviWbpsOaDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_gsjfaps_oa(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, phaseId);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<naviWbpsOaDTO> results = new ArrayList<naviWbpsOaDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        naviWbpsOaDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new naviWbpsOaDTO();
                            tmp.setRequestId(rs.getLong("requestid"));
                            tmp.setRequestName(rs.getString("requestname"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setCreateBy(rs.getString("createby"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    //设计输入评审流程查询 OA
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<naviWbpsOaDTO> getSjsrpsOa(Long projectId, Long phaseId) {
        List<naviWbpsOaDTO> resultList = (List<naviWbpsOaDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_sjsrps_oa(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, phaseId);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<naviWbpsOaDTO> results = new ArrayList<naviWbpsOaDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        naviWbpsOaDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new naviWbpsOaDTO();
                            tmp.setRequestId(rs.getLong("requestid"));
                            tmp.setRequestName(rs.getString("requestname"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setCreateBy(rs.getString("createby"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            tmp.setPhaseName(rs.getString("phasename"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    //设计变更
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public naviDataDTO getSjbgOa(Long projectId, Long phaseId, String keyWords, String works, String draws, Long page, Long rows) {
        naviDataDTO dt = new naviDataDTO();

        List<naviWbpsOaDTO> resultList = (List<naviWbpsOaDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_sjbg_oa(?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, phaseId);
                        cs.setString(3, keyWords);
                        cs.setString(4, works);
                        cs.setString(5, draws);
                        cs.setLong(6, page);
                        cs.setLong(7, rows);
                        cs.registerOutParameter(8, OracleTypes.NUMBER);
                        cs.registerOutParameter(9, OracleTypes.NUMBER);
                        cs.registerOutParameter(10, OracleTypes.NUMBER);
                        cs.registerOutParameter(11, OracleTypes.VARCHAR);
                        cs.registerOutParameter(12, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<naviWbpsOaDTO> results = new ArrayList<naviWbpsOaDTO>();
                        cs.execute();
                        dt.setTotal(cs.getLong(8));
                        dt.setPageNo(cs.getLong(9));
                        dt.setPageSize(cs.getLong(10));
                        ResultSet rs = (ResultSet) cs.getObject(12);// 获取游标一行的值
                        naviWbpsOaDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new naviWbpsOaDTO();
                            tmp.setRequestId(rs.getLong("requestid"));
                            tmp.setRequestName(rs.getString("requestname"));
                            tmp.setSummary(rs.getString("summary"));
                            tmp.setZxmc(rs.getString("zxmc"));
                            tmp.setZy(rs.getString("zy"));
                            tmp.setSjbgdh(rs.getString("sjbgdh"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setCreateBy(rs.getString("createby"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            tmp.setDesignName(rs.getString("design_name"));
                            tmp.setCheckName(rs.getString("check_name"));
                            tmp.setReviewName(rs.getString("review_name"));
                            tmp.setApproveName(rs.getString("approve_name"));
                            tmp.setCertified(rs.getString("certified"));
                            tmp.setWorkPkgId(rs.getString("workpkg_id"));
                            tmp.setBgyy(rs.getString("bgyy"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setRows(resultList);
        return dt;
    }

    //图纸oa流程查询
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public naviDataDTO getPrintAllOa(Long projectId, Long formId, String userNo, Long page, Long rows) {
        naviDataDTO dt = new naviDataDTO();

        List<naviWbpsPrintDTO> resultList = (List<naviWbpsPrintDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_printall_oa(?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (projectId != -1)
                            cs.setLong(1, projectId);
                        else
                            cs.setBigDecimal(1, null);
                        cs.setLong(2, formId);
                        cs.setString(3, userNo);
                        cs.setLong(4, page);
                        cs.setLong(5, rows);
                        cs.registerOutParameter(6, OracleTypes.NUMBER);
                        cs.registerOutParameter(7, OracleTypes.NUMBER);
                        cs.registerOutParameter(8, OracleTypes.NUMBER);
                        cs.registerOutParameter(9, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<naviWbpsPrintDTO> results = new ArrayList<naviWbpsPrintDTO>();
                        cs.execute();
                        dt.setTotal(cs.getLong(6));
                        dt.setPageNo(cs.getLong(7));
                        dt.setPageSize(cs.getLong(8));
                        ResultSet rs = (ResultSet) cs.getObject(9);// 获取游标一行的值
                        naviWbpsPrintDTO tmp = null;
                        if (formId == 583) {
                            while (rs.next()) {// 转换每行的返回值到Map中
                                tmp = new naviWbpsPrintDTO();
                                tmp.setRequestId(rs.getLong("requestid"));
                                tmp.setRequestName(rs.getString("requestname"));
                                tmp.setStatus(rs.getString("status"));
                                tmp.setCreateDate(rs.getString("createdate"));
                                tmp.setCreateBy(rs.getString("createby"));
                                tmp.setCurrentPerson(rs.getString("current_person"));
                                tmp.setXmmc(rs.getString("xmmc"));
                                tmp.setCyly(rs.getString("cyly"));
                                tmp.setXgnr(rs.getString("xgnr"));

                                results.add(tmp);
                            }
                        } else if (formId == 432) {
                            while (rs.next()) {// 转换每行的返回值到Map中
                                tmp = new naviWbpsPrintDTO();
                                tmp.setRequestId(rs.getLong("requestid"));
                                tmp.setRequestName(rs.getString("requestname"));
                                tmp.setStatus(rs.getString("status"));
                                tmp.setCreateDate(rs.getString("createdate"));
                                tmp.setCreateBy(rs.getString("createby"));
                                tmp.setCurrentPerson(rs.getString("current_person"));
                                tmp.setSjjd(rs.getString("sjjd"));
                                tmp.setRwlx(rs.getString("rwlx"));
                                tmp.setPltStatus(rs.getString("pltstatus"));
                                tmp.setXmmc(rs.getString("xmmc"));
                                tmp.setZgfzg(rs.getString("zgfzg"));
                                results.add(tmp);
                            }
                        } else {
                            while (rs.next()) {// 转换每行的返回值到Map中
                                tmp = new naviWbpsPrintDTO();
                                tmp.setRequestId(rs.getLong("requestid"));
                                tmp.setRequestName(rs.getString("requestname"));
                                tmp.setMajor(rs.getString("major"));
                                tmp.setWydbh(rs.getString("wydbh"));
                                tmp.setWynr(rs.getString("wynr"));
                                tmp.setStatus(rs.getString("status"));
                                tmp.setCreateDate(rs.getString("createdate"));
                                tmp.setCreateBy(rs.getString("createby"));
                                tmp.setCurrentPerson(rs.getString("current_person"));
                                tmp.setXmmc(rs.getString("xmmc"));
                                tmp.setPltorder(rs.getLong("pltorder"));

                                results.add(tmp);
                            }
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setRows(resultList);
        return dt;
    }

    //获取项目阶段发送图纸份数
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<naviWbpsDrawingNumDTO> getDrawingNum(Long projectId) {
        List<naviWbpsDrawingNumDTO> resultList = (List<naviWbpsDrawingNumDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_get_drawing_num(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);

                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<naviWbpsDrawingNumDTO> results = new ArrayList<naviWbpsDrawingNumDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        naviWbpsDrawingNumDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new naviWbpsDrawingNumDTO();
                            tmp.setPhaseId(rs.getLong("phase_id"));
                            tmp.setDrawingNum(rs.getLong("drawing_num"));
                            tmp.setCnDrawingNum(rs.getLong("cndrawing_num"));

                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    private DesignRevisionEntity elmentsDtoToEntity(drawingDTO drawingDTO) {
        DesignRevisionEntity entity = new DesignRevisionEntity();
        try {
            PropertyUtils.copyProperties(entity, drawingDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    //提交设计变更
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map drawingCreate(DesignRevisionDTO designRevisionDTO, long userId) {
        List<DesignRevisionEntity> dataList = new ArrayList<DesignRevisionEntity>();
        for (int i = 0; i < designRevisionDTO.getDetail().size(); i++) {
            dataList.add(this.elmentsDtoToEntity(designRevisionDTO.getDetail().get(i)));
        }
        String xmjl = designRevisionDTO.getXmjl();
        String zyfzr = designRevisionDTO.getZyfzr();
        String xmbh = designRevisionDTO.getXmbh();
        String xmmc = designRevisionDTO.getXmmc();
        String sjjd = designRevisionDTO.getSjjd();
        String zxmc = designRevisionDTO.getZxmc();
        String zxh = designRevisionDTO.getZxh();
        String zy = designRevisionDTO.getZy();
        String bgyyOA = designRevisionDTO.getBgyyOA();
        String zysx = designRevisionDTO.getZysx();
        String bgyyJsp = designRevisionDTO.getBgyyJsp();
        String subName = designRevisionDTO.getSubName();
        String drawsName = designRevisionDTO.getDrawsName();
        String drawsNum = designRevisionDTO.getDrawsNum();
        String editDoc = designRevisionDTO.getEditDoc();
        String viewDoc = designRevisionDTO.getViewDoc();
        String getPdf = designRevisionDTO.getGetPdf();


        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_drawing_create(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10,:11,:12,:13,:14,:15,:16,:17,:18,:19,:20,:21)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_DRAWING_REVISION_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setString(1, xmjl);
                        cs.setString(2, zyfzr);
                        cs.setString(3, xmbh);
                        cs.setString(4, xmmc);
                        cs.setString(5, sjjd);
                        cs.setString(6, zxmc);
                        cs.setString(7, zxh);
                        cs.setString(8, zy);
                        cs.setArray(9, vArray);
                        cs.setLong(10, userId);
                        cs.setString(11, bgyyOA);
                        cs.setString(12, zysx);
                        cs.setString(13, bgyyJsp);
                        cs.setString(14, subName);
                        cs.setString(15, drawsName);
                        cs.setString(16, drawsNum);
                        cs.setString(17, editDoc);
                        cs.setString(18, viewDoc);
                        cs.setString(19, getPdf);
                        cs.registerOutParameter(20, OracleTypes.VARCHAR);
                        cs.registerOutParameter(21, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put("url", cs.getString(20));
                        tmp.put("status", cs.getString(21));
                        return tmp;
                    }
                });
        return map;
    }

    //设计变更明细
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public naviWbpsSjbgdtDTO getSjbgDtOa(float requestId) {
        naviWbpsSjbgdtDTO dt = new naviWbpsSjbgdtDTO();

        List<naviWbpsSjbgdt1DTO> resultList = (List<naviWbpsSjbgdt1DTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_sjbg_dt_oa(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setFloat(1, requestId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<naviWbpsSjbgdt1DTO> results1 = new ArrayList<naviWbpsSjbgdt1DTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取第一个游标
                        ResultSet rs1 = (ResultSet) cs.getObject(3);// 获取第二个游标
                        naviWbpsSjbgdt1DTO tmp = null;
                        while (rs.next()) {// 保存头表
                            dt.setBgdbh(rs.getString("bgdbh"));
                            dt.setBgyyjsp(rs.getString("bgyyjsp"));
                            dt.setZysx(rs.getString("zysx"));
                            dt.setSubName(rs.getString("subName"));
                            dt.setDrawsName(rs.getString("drawsName"));
                            dt.setXmmc(rs.getString("xmmc"));
                            dt.setSqr(rs.getString("sqr"));
                            dt.setDrawsNum(rs.getString("drawsNum"));
                            dt.setZy(rs.getString("zy"));
                            dt.setDqsjr(rs.getString("dqsjr"));
                            dt.setDqjhr(rs.getString("dqjhr"));
                            dt.setDqshr(rs.getString("dqshr"));
                            dt.setDqsdr(rs.getString("dqsdr"));
                            dt.setDqzcgcs(rs.getString("dqzcgcs"));
                            dt.setZrgcs(rs.getString("zrgcs"));
                            dt.setXmjl(rs.getString("xmjl"));
                            dt.setSjrApproveDate(rs.getString("sjr_approve_date"));
                            dt.setJhrApproveDate(rs.getString("jhr_approve_date"));
                            dt.setShrApproveDate(rs.getString("shr_approve_date"));
                            dt.setSdrApproveDate(rs.getString("sdr_approve_date"));
                            dt.setZcgcsApproveDate(rs.getString("zcgcs_approve_date"));
                            dt.setZrgcsApproveDate(rs.getString("zrgcs_approve_date"));

                        }
                        rs.close();
                        while (rs1.next()) {// 保存明细
                            tmp = new naviWbpsSjbgdt1DTO();
                            tmp.setZymc(rs1.getString("zymc"));
                            tmp.setZyfzrmx(rs1.getString("xgzyfzr"));
                            results1.add(tmp);
                        }
                        rs1.close();
                        return results1;
                    }
                });
        dt.setDetail(resultList);
        return dt;
    }

    private PrintCreateLineEntity elmentsDtoToEntity1(printCreateLineDTO printCreateLineDTO) {
        PrintCreateLineEntity entity = new PrintCreateLineEntity();
        try {
            PropertyUtils.copyProperties(entity, printCreateLineDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private PrintCreateDocEntity elmentsDtoToEntity2(printCreateDocDTO printCreateDocDTO) {
        PrintCreateDocEntity entity = new PrintCreateDocEntity();
        try {
            PropertyUtils.copyProperties(entity, printCreateDocDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    //提交图纸文印流程
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map printCreate(printCreateDTO printCreateDTO, long workflowId,long userId) {
        List<PrintCreateLineEntity> dataList = new ArrayList<PrintCreateLineEntity>();
        for (int i = 0; i < printCreateDTO.getDetail().size(); i++) {
            dataList.add(this.elmentsDtoToEntity1(printCreateDTO.getDetail().get(i)));
        }
        List<PrintCreateDocEntity> dataList1 = new ArrayList<PrintCreateDocEntity>();
        for (int i = 0; i < printCreateDTO.getDocList().size(); i++) {
            dataList1.add(this.elmentsDtoToEntity2(printCreateDTO.getDocList().get(i)));
        }
        String xmjl = printCreateDTO.getXmjl();
        String zyfzr = printCreateDTO.getZyfzr();
        String xmbh = printCreateDTO.getXmbh();
        String xmmc = printCreateDTO.getXmmc();
        String xmmsx = printCreateDTO.getXmmsx();
        String xmfzr = printCreateDTO.getXmfzr();
        String wynr = printCreateDTO.getWynr();
        String zipurl = printCreateDTO.getZipurl();
        String fgld = printCreateDTO.getFgld();
        String zgfzg = printCreateDTO.getZgfzg();
        String zy = printCreateDTO.getZy();
        long pltorder = printCreateDTO.getPltOrder();
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_printall_create(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10,:11,:12,:13,:14,:15,:16,:17,:18)}";
                        CallableStatement cs = con.prepareCall(storedProc);

                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_PRINT_LINE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());

                        ArrayDescriptor tabDesc1 = ArrayDescriptor.createDescriptor("APPS.CUX_PRINT_DOC_TBL", or);
                        ARRAY vArray1 = new ARRAY(tabDesc1, or, dataList1.toArray());
                        cs.setLong(1, workflowId);
                        cs.setString(2, xmbh);
                        cs.setString(3, xmmc);
                        cs.setString(4, xmjl);
                        cs.setString(5, xmfzr);
                        cs.setString(6, zyfzr);
                        cs.setString(7, xmmsx);
                        cs.setString(8, wynr);
                        cs.setString(9, zipurl);
                        cs.setString(10, zy);
                        cs.setString(11, fgld);
                        cs.setString(12, zgfzg);
                        cs.setArray(13, vArray);
                        cs.setArray(14, vArray1);
                        cs.setLong(15, userId);
                        cs.setLong(16, pltorder);
                        cs.registerOutParameter(17, OracleTypes.VARCHAR);
                        cs.registerOutParameter(18, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put("url", cs.getString(17));
                        tmp.put("status", cs.getString(18));
                        return tmp;
                    }
                });
        return map;
    }

    //获取项目角色
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<getProjectRoleDTO> getProjectRoles(Long projectId) {
        List<getProjectRoleDTO> resultList = (List<getProjectRoleDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_get_proj_roles(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<getProjectRoleDTO> results = new ArrayList<getProjectRoleDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        getProjectRoleDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new getProjectRoleDTO();
                            tmp.setNum(rs.getString("num"));
                            tmp.setName(rs.getString("name"));
                            tmp.setRole(rs.getString("role"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }
    //提交建筑院图纸校审流程
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map jzyPrintCheck( jzyPrintCheckDTO jzyPrintCheckDTO, long workflowId,long userId) {

        String xmjl = jzyPrintCheckDTO.getXmjl();
        String xmbh = jzyPrintCheckDTO.getXmbh();
        String zyjds = jzyPrintCheckDTO.getZyjds();
        String zymc = jzyPrintCheckDTO.getZymc();
        String sjjd = jzyPrintCheckDTO.getSjjd();
        String zxmc1 = jzyPrintCheckDTO.getZxmc1();
        String zyfzr = jzyPrintCheckDTO.getZyfzr();
        String sjr = jzyPrintCheckDTO.getSjr();
        String jhr = jzyPrintCheckDTO.getJhr();
        String sdr = jzyPrintCheckDTO.getSdr();
        String shr = jzyPrintCheckDTO.getShr();
        String zcs = jzyPrintCheckDTO.getZcs();
        String fasjr = jzyPrintCheckDTO.getFasjr();
        String jhkssj = jzyPrintCheckDTO.getJhkssj();
        String jhjssj = jzyPrintCheckDTO.getJhjssj();
        String tzurl = jzyPrintCheckDTO.getTzurl();
        String lcsm = jzyPrintCheckDTO.getLcsm();

        String fcsjr = jzyPrintCheckDTO.getFcsjr();
        String fcjhr = jzyPrintCheckDTO.getFcjhr();
        String fcshr = jzyPrintCheckDTO.getFcshr();
        String fcsdr = jzyPrintCheckDTO.getFcsdr();
        String fczyfzr = jzyPrintCheckDTO.getFczyfzr();
        String fczcs = jzyPrintCheckDTO.getFczcs();
        String fcfasjr = jzyPrintCheckDTO.getFcfasjr();
        String fcxmjl = jzyPrintCheckDTO.getFcxmjl();

        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_jzy_print_check(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10,:11,:12,:13,:14,:15,:16,:17,:18,:19,:20,:21,:22,:23,:24,:25,:26,:27,:28,:29)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, workflowId);
                        cs.setString(2, xmbh);
                        cs.setString(3, xmjl );
                        cs.setString(4, zyjds);
                        cs.setString(5, zymc);
                        cs.setString(6, sjjd);
                        cs.setString(7, zxmc1);
                        cs.setString(8, zyfzr);
                        cs.setString(9, sjr);
                        cs.setString(10, jhr);
                        cs.setString(11, shr);
                        cs.setString(12, sdr);
                        cs.setString(13, zcs);
                        cs.setString(14, fasjr);
                        cs.setString(15, jhkssj);
                        cs.setString(16, jhjssj);
                        cs.setString(17, tzurl);
                        cs.setString(18, lcsm);
                        cs.setLong(19, userId);
                        cs.setString(20, fcsjr);
                        cs.setString(21, fcjhr);
                        cs.setString(22, fcshr);
                        cs.setString(23, fcsdr);
                        cs.setString(24, fczyfzr);
                        cs.setString(25, fczcs);
                        cs.setString(26, fcfasjr);
                        cs.setString(27, fcxmjl);
                        cs.registerOutParameter(28, OracleTypes.VARCHAR);
                        cs.registerOutParameter(29, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put("url", cs.getString(28));
                        tmp.put("status", cs.getString(29));
                        return tmp;
                    }
                });
        return map;
    }


    //建筑院图纸校审流程查询
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public naviDataDTO getJzyPrintCheckOa(Long formId, Long xmId,String jdcode,String zyjds,Long page,Long rows,String userNo) {
        naviDataDTO dt = new naviDataDTO();
        List<jzyPrintCheckOaDTO> resultList = (List<jzyPrintCheckOaDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_get_jzy_printcheck(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10,:11,:12)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, formId);
                        cs.setLong(2, xmId);
                        cs.setString(3, jdcode);
                        cs.setString(4, zyjds);
                        cs.setLong(5, page);
                        cs.setLong(6, rows);
                        cs.setString(7, userNo);
                        cs.registerOutParameter(8,OracleTypes.NUMBER);
                        cs.registerOutParameter(9, OracleTypes.NUMBER);
                        cs.registerOutParameter(10, OracleTypes.NUMBER);
                        cs.registerOutParameter(11, OracleTypes.VARCHAR);
                        cs.registerOutParameter(12, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<jzyPrintCheckOaDTO> results = new ArrayList<jzyPrintCheckOaDTO>();
                        cs.execute();
                        dt.setTotal(cs.getLong(8));
                        dt.setPageNo(cs.getLong(9));
                        dt.setPageSize(cs.getLong(10));
                        ResultSet rs = (ResultSet) cs.getObject(12);// 获取游标一行的值
                        jzyPrintCheckOaDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new jzyPrintCheckOaDTO();
                            tmp.setRequestId(rs.getLong("requestid"));
                            tmp.setRequestName(rs.getString("requestname"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setCreateBy(rs.getString("createby"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            tmp.setZymc(rs.getString("zymc"));
                            tmp.setXmxsmc(rs.getString("xmxsmc"));
                            tmp.setZxh(rs.getString("zxh"));
                            tmp.setSjjd(rs.getString("sjjd"));
                            tmp.setSjjd1(rs.getString("sjjd1"));
                            tmp.setGzbId(rs.getString("gzbid"));
                            tmp.setLcsm(rs.getString("lcsm"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setRows(resultList);
        return dt;
    }

    //更新oa表中tzfb字段，图纸发布
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map updateOatzfb(Long requestId) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_wbps_pkg.wbps_update_oa_tzfb(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, requestId);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put("status", cs.getString(2));

                        return tmp;

                    }
                });
        return map;
    }


}
