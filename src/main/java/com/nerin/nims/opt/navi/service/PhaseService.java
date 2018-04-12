package com.nerin.nims.opt.navi.service;

import com.nerin.nims.opt.navi.dto.*;
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
 * Created by yinglgu on 7/30/2016.
 */
@Component
@Transactional
public class PhaseService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PhaseForXmkgbgDTO> getPhaseForXmkgbgDTOList(Long projectId, String phaseCode) {
        List<PhaseForXmkgbgDTO> resultList = (List<PhaseForXmkgbgDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_project_task(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setString(2, phaseCode);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForXmkgbgDTO> results = new ArrayList<PhaseForXmkgbgDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        PhaseForXmkgbgDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForXmkgbgDTO();
                            tmp.setTaskHeaderId(rs.getString("task_header_id"));
                            tmp.setTaskName(rs.getString("task_name"));
                            tmp.setTaskStatusName(rs.getString("task_status_name"));
                            tmp.setTaskProgress(rs.getString("task_progress"));
                            tmp.setCreatedName(rs.getString("created_name"));
                            tmp.setTemplateHeaderId(rs.getString("TEMPLATE_HEADER_ID"));
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
    public List<PhaseForZykgbgDTO> getPhaseForZykgbgDTOList(Long projectId, Long userId, Long phaseCode, Long isManager) {
        List<PhaseForZykgbgDTO> resultList = (List<PhaseForZykgbgDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_speciality_task(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, userId);
                        cs.setLong(3, phaseCode);
                        cs.setLong(4, isManager);
                        cs.registerOutParameter(5, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForZykgbgDTO> results = new ArrayList<PhaseForZykgbgDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        PhaseForZykgbgDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForZykgbgDTO();
                            //tmp.setTaskHeaderId(rs.getLong("task_header_id"));
                            tmp.setSpecialityName(rs.getString("speciality_name"));
                            tmp.setSpecialityCode(rs.getString("speciality_code"));
                            tmp.setBt(rs.getString("bt"));
                            tmp.setRequestId(rs.getLong("requestId"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setCreateId(rs.getString("create_id"));
                            tmp.setCreatedName(rs.getString("created_name"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setLastOperateDate(rs.getString("lastoperatedate"));
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
    public List<PhaseForOalcDTO> getPhaseForOalcDTOList(Long taskHeaderId) {
        List<PhaseForOalcDTO> resultList = (List<PhaseForOalcDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_project_task_oa(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForOalcDTO> results = new ArrayList<PhaseForOalcDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PhaseForOalcDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForOalcDTO();
                            tmp.setRequestId(rs.getLong("requestid"));
                            tmp.setRequestName(rs.getString("requestname"));
                            tmp.setWorkflowname(rs.getString("requestname"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setCreatetime(rs.getString("createdate"));
                            tmp.setCreateBy(rs.getString("createby"));
                            tmp.setLastname(rs.getString("createby"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            tmp.setApprover(rs.getString("current_person"));
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
    public List<PhaseForGzbzxjd_zy_DTO> getPhaseForGzbzxjd_zy_DTOList(Long projectId, Long userId, Long phaseCode, Long isManager) {
        List<PhaseForGzbzxjd_zy_DTO> resultList = (List<PhaseForGzbzxjd_zy_DTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_tree_speciality(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(3, userId);
                        cs.setLong(2, phaseCode);
                        cs.setLong(4, isManager);
                        cs.registerOutParameter(5, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForGzbzxjd_zy_DTO> results = new ArrayList<PhaseForGzbzxjd_zy_DTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        PhaseForGzbzxjd_zy_DTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForGzbzxjd_zy_DTO();
                            tmp.setSpecialityCode(rs.getString("speciality_code"));
                            tmp.setSpecialityName(rs.getString("speciality_name"));
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
    public List<PhaseForGzbzxjd_zy_DTO> getPhaseForGzbzxjd_gzbTree_DTOList(Long projectId, Long userId, Long phaseCode, String speciality, Long isManager) {
        List<PhaseForGzbzxjd_zy_DTO> resultList = (List<PhaseForGzbzxjd_zy_DTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_deliverables_tree(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(3, userId);
                        cs.setLong(2, phaseCode);
                        cs.setString(4, speciality);
                        cs.setLong(5, isManager);
                        cs.registerOutParameter(6, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForGzbzxjd_zy_DTO> results = new ArrayList<PhaseForGzbzxjd_zy_DTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        PhaseForGzbzxjd_zy_DTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForGzbzxjd_zy_DTO();
                            tmp.setTitle(rs.getString("task_name"));
                            tmp.setProjElementId(rs.getLong("proj_element_id"));
                            tmp.setTypeId(rs.getLong("type_id"));
                            tmp.setTypeName(rs.getString("type_name"));
                            tmp.setObjectId(rs.getLong("object_id"));
                            tmp.setTaskName(rs.getString("task_name"));
                            tmp.setParentsId(rs.getLong("parents_id"));
                            tmp.setProgress(rs.getDouble("progress"));
                            tmp.setTaskWbsNumber(rs.getString("TASK_WBS_NUMBER"));
                            tmp.setTitle(null != tmp.getTaskWbsNumber() ? tmp.getTaskWbsNumber() : "");
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });


        if (0 < resultList.size()) {
            List<PhaseForGzbzxjd_zy_DTO> nodeList = new ArrayList<PhaseForGzbzxjd_zy_DTO>();
            List<PhaseForGzbzxjd_zy_DTO> tmpList = resultList;
            for(PhaseForGzbzxjd_zy_DTO node1 : tmpList){
                boolean mark = false;
                for(PhaseForGzbzxjd_zy_DTO node2 : tmpList){
                    if(null != node1.getParentsId() && node1.getParentsId().equals(node2.getObjectId())){
                        mark = true;
                        if(node2.getChildren() == null)
                            node2.setChildren(new ArrayList<PhaseForGzbzxjd_zy_DTO>());
                        node2.getChildren().add(node1);
                        break;
                    }
                }
                if(!mark){
                    nodeList.add(node1);
                }
            }
            return nodeList;
        } else {
            return resultList;
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PhaseForGzblxDTO> getPhaseForGzblxDTOList(Long projectId, Long userId, Long phaseCode, String speciality, Long isManager) {
        List<PhaseForGzblxDTO> resultList = (List<PhaseForGzblxDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_work_package_type(?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForGzblxDTO> results = new ArrayList<PhaseForGzblxDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        PhaseForGzblxDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForGzblxDTO();
                            tmp.setTypeCode(rs.getString("type_code"));
                            tmp.setTypeName(rs.getString("type_name"));
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
    public DataTablesDTO getPhaseForPhaseForGzbzxjdDTOList(Long projectId, Long userId, Long phaseCode, String specialitys, Long isManager,
                                                                      String workpkgTypeCode, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<PhaseForGzbzxjdDTO> resultList = (List<PhaseForGzbzxjdDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_work_package_info(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, phaseCode);
                        cs.setLong(3, userId);
                        cs.setLong(4, isManager);
                        cs.setString(5, specialitys);
                        cs.setString(6, workpkgTypeCode);
                        cs.setLong(7, pageNo);
                        cs.setLong(8, pageSize);
                        cs.registerOutParameter(9, OracleTypes.NUMBER);
                        cs.registerOutParameter(10, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForGzbzxjdDTO> results = new ArrayList<PhaseForGzbzxjdDTO>();
                        cs.execute();
                        dt.setDataTotal(cs.getLong(9));
                        ResultSet rs = (ResultSet) cs.getObject(10);// 获取游标一行的值
                        PhaseForGzbzxjdDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForGzbzxjdDTO();
                            tmp.setProjectId(rs.getLong("project_id"));
                            tmp.setProjElementId(rs.getLong("proj_element_id"));
                            tmp.setSubTaskNumber(rs.getString("sub_task_number"));
                            tmp.setSubTask(rs.getString("sub_task"));
                            tmp.setSpac_code(rs.getString("spac_code"));
                            tmp.setSpac(rs.getString("spac"));
                            tmp.setElementName(rs.getString("element_name"));
                            tmp.setDueDate(rs.getDate("due_date"));
                            tmp.setCompletionDate(rs.getDate("completion_date"));
                            tmp.setProjectStatusName(rs.getString("project_status_name"));
                            tmp.setWorkpkgTypeCode(rs.getString("workpkg_type_code"));
                            tmp.setWorkpkgType(rs.getString("workpkg_type"));
                            tmp.setCompletedPercentage(rs.getLong("completed_percentage"));
                            tmp.setActTime(rs.getString("act_time"));
                            tmp.setDesign(rs.getString("design"));
                            tmp.setChecked(rs.getString("checked"));
                            tmp.setExamine(rs.getString("examine"));
                            tmp.setAuthorize(rs.getString("authorize"));
                            tmp.setCreater(rs.getString("creater"));
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
    public List<PhaseForGzbzxjdDTO> getPhaseForPhaseForGzbzxjdDetailDTOList(Long projElementId) {
        List<PhaseForGzbzxjdDTO> resultList = (List<PhaseForGzbzxjdDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_work_time_list(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projElementId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForGzbzxjdDTO> results = new ArrayList<PhaseForGzbzxjdDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PhaseForGzbzxjdDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForGzbzxjdDTO();
                            tmp.setFullName(rs.getString("full_name"));
                            tmp.setWorkTime(rs.getLong("work_time"));
                            tmp.setRole(rs.getString("role"));
                            tmp.setNum(rs.getLong("num"));
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
    public DataTablesDTO getPhaseForJktjsjxDTOList(Long projectId, Long userId, Long phaseCode, Long isManager, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<PhaseForJktjsjxDTO> resultList = (List<PhaseForJktjsjxDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_conditions_list(?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, phaseCode);
                        cs.setLong(4, userId);
                        cs.setLong(3, isManager);
                        cs.setLong(5, pageNo);
                        cs.setLong(6, pageSize);
                        cs.registerOutParameter(7, OracleTypes.NUMBER);
                        cs.registerOutParameter(8, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForJktjsjxDTO> results = new ArrayList<PhaseForJktjsjxDTO>();
                        cs.execute();
                        dt.setDataTotal(cs.getLong(7));
                        ResultSet rs = (ResultSet) cs.getObject(8);// 获取游标一行的值
                        PhaseForJktjsjxDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForJktjsjxDTO();
                            /*
                                private String subTask;//：varchar2：子项
                                private String receiveSpecialty;//：varchar2：接收专业
                                private Long projElementId;//：number：工作包id
                                private String elementName;//：varchar2：工作包名称
                                private Date dueDate;//：date：计划完成时间
                                private Date completionDate;//：date：实际完成时间
                                private String projectStatusName;//：varchar2：状态
                                private String design;//：varchar2：设计人
                                private String checked;//：varchar2：校核人
                                private String examine;//：varchar2：审核人
                                private String authorize;//：varchar2：审定人
                                private String creater;//：varchar2：策划人
                             */
                            tmp.setSubTask(rs.getString("SUB_TASK"));
                            tmp.setReceiveSpecialty(rs.getString("RECEIVE_SPECIALTY"));
                            tmp.setProjElementId(rs.getLong("PROJ_ELEMENT_ID"));
                            tmp.setElementName(rs.getString("ELEMENT_NAME"));
                            tmp.setDueDate(rs.getDate("DUE_DATE"));
                            tmp.setCompletionDate(rs.getDate("COMPLETION_DATE"));
                            tmp.setProjectStatusName(rs.getString("PROJECT_STATUS_NAME"));
                            tmp.setDesign(rs.getString("DESIGN"));
                            tmp.setChecked(rs.getString("CHECKED"));
                            tmp.setExamine(rs.getString("EXAMINE"));
                            tmp.setAuthorize(rs.getString("AUTHORIZE"));
                            tmp.setCreater(rs.getString("CREATER"));
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
    public List<PhaseForXmkgbgDTO> getPhaseForGzbzxjd_wb_List(Long projElementId) {
        List<PhaseForXmkgbgDTO> resultList = (List<PhaseForXmkgbgDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_work_package_wb(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projElementId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForXmkgbgDTO> results = new ArrayList<PhaseForXmkgbgDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PhaseForXmkgbgDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForXmkgbgDTO();
                            tmp.setTaskHeaderId(rs.getString("task_header_id"));
                            tmp.setTaskName(rs.getString("task_name"));
                            tmp.setTaskStatusName(rs.getString("task_status"));
                            tmp.setTaskProgress(rs.getString("task_progress"));
                            tmp.setTaskProgress2(rs.getString("task_progress2"));
                            tmp.setCreatedName(rs.getString("CREATER"));
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
    public List<PhaseForOalcDTO> getPhaseForOalc_wb_DTOList(Long taskHeaderId) {
        List<PhaseForOalcDTO> resultList = (List<PhaseForOalcDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_work_package_wb_oa(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
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
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            tmp.setTaskName(rs.getString("task_name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }
//工作包进度-条件单
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PhaseForTjdDTO> getPhaseForGzbzxjd_tjd_List(Long projElementId,Long userId) {
        List<PhaseForTjdDTO> resultList = (List<PhaseForTjdDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_work_package_tjd(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projElementId);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
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
                            tmp.setCheckWrokout(rs.getString("submit"));//是否可以提交编制 S/F
                            tmp.setScheduleDate(rs.getString("schedule_date"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }
//生成条件单编码
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public String  setConditionNum(Long pConditionId) {
        String resultList = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.set_condition_number(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, pConditionId);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return cs.getString(2);
                    }
                });
        return resultList;
    }


    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<OaWorkFlowDTO> getPhaseForOalc_tjd_DTOList(Long conditionId) {
        List<OaWorkFlowDTO> resultList = (List<OaWorkFlowDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_work_package_tjd_oa(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, conditionId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<OaWorkFlowDTO> results = new ArrayList<OaWorkFlowDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        OaWorkFlowDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new OaWorkFlowDTO();
                            tmp.setRequestid(rs.getString("requestid"));
                            tmp.setWorkflowname(rs.getString("requestname"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setCreatetime(rs.getString("createdate"));
                            tmp.setLastname(rs.getString("creater"));
                            tmp.setApprover(rs.getString("current_person"));
//                            tmp.setConditionNumber(rs.getString("condition_number"));
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
    public List<PhaseForJssTDO> getPhaseForGzbzxjd_jss_List(Long projElementId) {
        List<PhaseForJssTDO> resultList = (List<PhaseForJssTDO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_work_package_jss(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projElementId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForJssTDO> results = new ArrayList<PhaseForJssTDO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PhaseForJssTDO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForJssTDO();
                            tmp.setJssbh(rs.getString("jssbh"));
                            tmp.setJssm(rs.getString("jssm"));
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

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PhaseForXzwbzxjdHeaderDTO> getPhaseForXzwbzxjdHeader(Long projectId, String phaseCode) {
        List<PhaseForXzwbzxjdHeaderDTO> resultList = (List<PhaseForXzwbzxjdHeaderDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_text_header(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setString(2, phaseCode);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForXzwbzxjdHeaderDTO> results = new ArrayList<PhaseForXzwbzxjdHeaderDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        PhaseForXzwbzxjdHeaderDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForXzwbzxjdHeaderDTO();
                            tmp.setTaskHeaderId(rs.getLong("task_header_id"));
                            tmp.setTaskName(rs.getString("task_name"));
                            tmp.setProgress(rs.getString("progress"));
                            tmp.setCreater(rs.getString("creater"));
                            tmp.setTaskProgress(rs.getString("task_progress"));
                            tmp.setTaskProgress2(rs.getString("task_progress2"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setTag(rs.getString("tag"));
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
    public List<PhaseForXzwbzxjdDTO> getPhaseForXzwbzxjd(Long TaskHeaderId){
        List<PhaseForXzwbzxjdDTO> resultList = (List<PhaseForXzwbzxjdDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_text_chapter(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, TaskHeaderId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForXzwbzxjdDTO> results = new ArrayList<PhaseForXzwbzxjdDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PhaseForXzwbzxjdDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForXzwbzxjdDTO();
                            /*
             ctc.chapter_id,
             ctc.chapter_name,
             ctc.attribute7, --排序
             fl.meaning chapter_status, --状态
             fl.description progress, --进度
             v.due_date, --计划完成时间,计划专业校审完成时间
             get_zyjs_approved_date(ctc.chapter_id) zyjs_completion_date, --实际专业校审完成时间
             get_current_person_wb(ctc.chapter_id) current_person,
             get_person_name(ctc.person_id_responsible) owner, --节点负责人
             get_person_name(ctc.person_id_design) designed, --设计人
             get_person_name(ctc.person_id_proofread) checked, --校核人
             get_person_name(ctc.person_id_audit) reviewed, --审核人
             get_person_name(ctc.person_id_approve) approved, --审定人
             get_person_name(ctc.attribute11) sqr --授权人
                             */
                            tmp.setChapterId(rs.getLong("chapter_id"));
                            tmp.setChapterName(rs.getString("CHAPTER_NO") + " " + rs.getString("chapter_name"));
                            tmp.setChapterNo(rs.getString("CHAPTER_NO"));
                            tmp.setAttribute7(rs.getString("attribute7"));
                            tmp.setChapterStatus(rs.getString("chapter_status"));
                            tmp.setStatusCode(rs.getString("STATUS_CODE"));
                            tmp.setProgress(rs.getString("progress"));
                            tmp.setDueDate(rs.getDate("due_date"));
                            tmp.setZyjsCompletionDate(rs.getDate("zyjs_completion_date"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            tmp.setOwner(rs.getString("owner"));
                            tmp.setDesigned(rs.getString("designed"));
                            tmp.setChecked(rs.getString("checked"));
                            tmp.setReviewed(rs.getString("reviewed"));
                            tmp.setApproved(rs.getString("approved"));
                            tmp.setSqr(rs.getString("sqr"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }
   //建筑院接口条件-专业
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PhaseForGzbzxjd_zy_DTO> getPhaseForJktjzyDTOList(Long projectId, Long phaseCode,Long userId, Long isManager) {
        List<PhaseForGzbzxjd_zy_DTO> resultList = (List<PhaseForGzbzxjd_zy_DTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_jzy_tree_speciality(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, phaseCode);
                        cs.setLong(3, userId);
                        cs.setLong(4, isManager);
                        cs.registerOutParameter(5, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForGzbzxjd_zy_DTO> results = new ArrayList<PhaseForGzbzxjd_zy_DTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        PhaseForGzbzxjd_zy_DTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForGzbzxjd_zy_DTO();
                            tmp.setSpecialityCode(rs.getString("speciality_code"));
                            tmp.setSpecialityName(rs.getString("speciality_name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }
    //建筑院接口条件-节点树
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PhaseForGzbzxjd_zy_DTO> getJzy_Tree_DTOList(Long projectId, Long phaseCode, Long userId, String speciality, Long isManager) {
        List<PhaseForGzbzxjd_zy_DTO> resultList = (List<PhaseForGzbzxjd_zy_DTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_phase_pkg.get_jzy_deliverables_tree(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, phaseCode);
                        cs.setLong(3, userId);
                        cs.setString(4, speciality);
                        cs.setLong(5, isManager);
                        cs.registerOutParameter(6, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<PhaseForGzbzxjd_zy_DTO> results = new ArrayList<PhaseForGzbzxjd_zy_DTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        PhaseForGzbzxjd_zy_DTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PhaseForGzbzxjd_zy_DTO();
                            tmp.setSpecialityCode(rs.getString("specialty_code"));
                            tmp.setSpecialityName(rs.getString("specialty_name"));
                            tmp.setTitle(rs.getString("specialty_code"));
                            tmp.setObjectId(rs.getLong("id"));
                            tmp.setTypeName(rs.getString("type_name"));
                            tmp.setTaskName(rs.getString("specialty_name"));
                            tmp.setParentsId(rs.getLong("pid"));
                            //tmp.setTaskWbsNumber(rs.getString("TASK_WBS_NUMBER"));
                            //tmp.setTitle(null != tmp.getTaskWbsNumber() ? tmp.getTaskWbsNumber() : "");
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });


        if (0 < resultList.size()) {
            List<PhaseForGzbzxjd_zy_DTO> nodeList = new ArrayList<PhaseForGzbzxjd_zy_DTO>();
            List<PhaseForGzbzxjd_zy_DTO> tmpList = resultList;
            for(PhaseForGzbzxjd_zy_DTO node1 : tmpList){
                boolean mark = false;
                for(PhaseForGzbzxjd_zy_DTO node2 : tmpList){
                    if(null != node1.getParentsId() && node1.getParentsId().equals(node2.getObjectId())){
                        mark = true;
                        if(node2.getChildren() == null)
                            node2.setChildren(new ArrayList<PhaseForGzbzxjd_zy_DTO>());
                        node2.getChildren().add(node1);
                        break;
                    }
                }
                if(!mark){
                    nodeList.add(node1);
                }
            }
            return nodeList;
        } else {
            return resultList;
        }
    }
}
