package com.nerin.nims.opt.navi.service;

import com.nerin.nims.opt.app.web.rest.dto.DataTableDTO;
import com.nerin.nims.opt.navi.dto.*;
import com.nerin.nims.opt.navi.module.PYBLineEntity;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OracleTypes;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/8/8.
 */
@Component
@Transactional
public class PtextService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unchecked")
    private PYBLineEntity cdtoToEntity(ProjectYBlineDTO projectYBlineDTO) {
        PYBLineEntity entity = new PYBLineEntity();
        try {
            PropertyUtils.copyProperties(entity, projectYBlineDTO);
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
     * 获取项目通用文本
     * @param projectId  项目ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getptext(Long projectId) {
        DataTableDTO dt = new DataTableDTO();
        List<ProjectTextDTO> resultList = (List<ProjectTextDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_text.get_project_text(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR );// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectTextDTO> results = new ArrayList<ProjectTextDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ProjectTextDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectTextDTO();
                            tmp.setTaskheaderId(rs.getLong("task_header_id"));
                            tmp.setTaskName(rs.getString("task_name"));
                            tmp.setCreater(rs.getString("creater"));
                            tmp.setTaskProgress(rs.getString("task_progress"));
                            tmp.setStatus(rs.getString("task_status"));
                            tmp.setTaskType(rs.getString("task_type"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }
  //OA流程查询
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ProjectTextOaDTO> getPtextOA(Long taskHeaderId,String taskType) {
        List<ProjectTextOaDTO> resultList = (List<ProjectTextOaDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_text.get_project_text_line(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.setString(2, taskType);
                        cs.registerOutParameter(3, oracle.jdbc.OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectTextOaDTO> results = new ArrayList<ProjectTextOaDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        ProjectTextOaDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectTextOaDTO();
                            tmp.setRequestId(rs.getLong("requestid"));
                            tmp.setRequestName(rs.getString("requestname"));
                            tmp.setStatus(rs.getString("status"));
                            tmp.setCreateDate(rs.getString("createdate"));
                            tmp.setCreateBy(rs.getString("creater"));
                            tmp.setCurrentPerson(rs.getString("current_person"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 获取月报头表
     * @param projectId  项目ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getybhead(Long projectId) {
        DataTableDTO dt = new DataTableDTO();
        List<ProjectYBheadDTO> resultList = (List<ProjectYBheadDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_text.get_project_yb_head(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR );// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectYBheadDTO> results = new ArrayList<ProjectYBheadDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ProjectYBheadDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectYBheadDTO();
                            tmp.setHeadId(rs.getLong("head_id"));
                            tmp.setSegment1(rs.getString("segment1"));
                            tmp.setPersonName(rs.getString("person_name"));
                            tmp.setCreationDate(rs.getString("creation_date"));
                            tmp.setStatus(rs.getString("status"));
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
     * 获取月报行表
     * @param pHeadId  月报头ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public YBLDataTableDTO getybline(Long pHeadId,Long projectId) {
        YBLDataTableDTO dt = new YBLDataTableDTO();
        List<ProjectYBlineDTO> resultList = (List<ProjectYBlineDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_text.get_project_yb_line(?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, pHeadId);// 设置输入参数的值
                        cs.setLong(2, projectId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.NUMBER );// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.VARCHAR );// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR );// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.VARCHAR );// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.CURSOR );// 注册输出参数的类型
                        cs.registerOutParameter(8, OracleTypes.VARCHAR );// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectYBlineDTO> results = new ArrayList<ProjectYBlineDTO>();
                        cs.execute();
                        dt.setHeadId(cs.getLong(3));
                        dt.setBt(cs.getString(4));
                        dt.setYxqk(cs.getString(5));
                        dt.setSfqk(cs.getString(6));
                        ResultSet rs = (ResultSet) cs.getObject(7);// 获取游标一行的值
                        dt.setIsrequired(cs.getString(8));
                        ProjectYBlineDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectYBlineDTO();
                            tmp.setLineId(rs.getLong("line_id"));
                            tmp.setPhase(rs.getString("phase"));
                            tmp.setSeq(rs.getLong("seq"));
                            tmp.setPhaseWeight(rs.getFloat("phase_weight"));
                            tmp.setLastProgress(rs.getFloat("last_progress"));
                            tmp.setPhaseProgress(rs.getFloat("phase_progress"));
                            tmp.setComments(rs.getString("comments"));
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
     * 创建时，获取月报行阶段信息
     * @param projectId  项目ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public YBLDataTableDTO getybphase(Long projectId) {
        YBLDataTableDTO dt = new YBLDataTableDTO();
        List<ProjectYBlineDTO> resultList = (List<ProjectYBlineDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_text.get_project_yb_phase(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.VARCHAR );// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.VARCHAR );// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR );// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.VARCHAR );// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectYBlineDTO> results = new ArrayList<ProjectYBlineDTO>();
                        cs.execute();
                        dt.setBt(cs.getString(3));//自动生成的标题
                        dt.setMessage(cs.getString(2));//是否成功，返回‘F’提示没有已批准的里程碑
                        dt.setIsrequired(cs.getString(5));
                        dt.setSfqk(cs.getString(6));
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        ProjectYBlineDTO tmp = null;
                        if (!"F".equals(cs.getString(2))) {
                            while (rs.next()) {// 转换每行的返回值到Map中
                                tmp = new ProjectYBlineDTO();
                                tmp.setPhase(rs.getString("name"));
                                tmp.setPhaseWeight(rs.getFloat("phase_weight"));
                                tmp.setLastProgress(rs.getFloat("last_progress"));
                                tmp.setSeq(rs.getLong("seq_num"));
                                tmp.setPhaseProgress(rs.getFloat("phase_progress"));
                                results.add(tmp);
                            }
                            rs.close();
                        }
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }
//保存头信息
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Long saveybhead(Long projectId,Long pHeadId,String pBt,String pYxqk,String pSfqk,Long pUserId) {
        Long i = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_text.save_project_yb_head(?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setLong(2, pHeadId);// 设置输入参数的值
                        cs.setString(3, pBt);// 设置输入参数的值
                        cs.setString(4, pYxqk);// 设置输入参数的值
                        cs.setString(5, pSfqk);// 设置输入参数的值
                        cs.setLong(6, pUserId);// 设置输入参数的值
                        cs.registerOutParameter(7, OracleTypes.NUMBER );// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getLong(7);
                    }
                });
        return i;
    }

    //保存行信息
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public String saveybline(Long pHeadId,List<ProjectYBlineDTO> projectYBlineDTOs,Long pUserId) { //接收list，返回版本
        List<PYBLineEntity> inData = projectYBlineDTOs.stream().map(mile -> {
            PYBLineEntity tmp = this.cdtoToEntity(mile);
            return tmp;
        }).collect(Collectors.toList());
        String m = (String)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_navi_text.save_project_yb_line(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.NAVI_PROJECT_YB_LINE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
                        cs.setLong(1,pHeadId);
                        cs.setArray(2, vArray);
                        cs.setLong(3,pUserId);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return cs.getString(4);
                    }
                });
        return m;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public String delYb(Long pHeadId) {
        String m = (String)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_navi_text.delete_project_yb(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1,pHeadId);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return cs.getString(2);
                    }
                });
        return m;
    }
}
