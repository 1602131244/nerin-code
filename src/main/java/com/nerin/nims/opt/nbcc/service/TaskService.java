package com.nerin.nims.opt.nbcc.service;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.*;
import com.nerin.nims.opt.nbcc.module.TaskChaptersEntity;
import com.nerin.nims.opt.nbcc.module.TaskHeaderEntity;
import com.nerin.nims.opt.nbcc.module.TaskResponsibleEntity;
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

import java.io.File;
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
 * Created by Administrator on 2016/7/1.
 */
@Component
@Transactional
public class TaskService {

    @Autowired
    private NerinProperties nerinProperties;
    @Autowired
    public TemplateService templateService;

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     *把TaskHeaderDto的值拷贝到TaskHeaderEntity中
     * @param taskHeaderDto
     * @return
     */
    private TaskHeaderEntity taskDtoToEntity(TaskHeaderDto taskHeaderDto) {
        TaskHeaderEntity entity = new TaskHeaderEntity();
        try {
            PropertyUtils.copyProperties(entity, taskHeaderDto);
            entity.setTaskHeaderId(taskHeaderDto.getTaskHeaderId());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private TaskChaptersEntity cdtoToEntity(TaskChaptersDto chaptersDTO) {
        TaskChaptersEntity entity = new TaskChaptersEntity();
        try {
            PropertyUtils.copyProperties(entity, chaptersDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private TaskResponsibleEntity taskResponsiblcdtoToEntity(TaskResponsibleDto responsibleDto) {
        TaskResponsibleEntity entity = new TaskResponsibleEntity();
        try {
            PropertyUtils.copyProperties(entity, responsibleDto);
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
     * 通过用户编号获取用户ID
     * @param workCode
     * @return
     */
    public Long getUserId(String workCode) {
        Long i = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.GET_USER_ID(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, workCode);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return cs.getLong(2);
                    }
                });
        return i;
    }

    /**
     * 获取任务列表
     * @param taskTypes   文本类型（支持多选，用','隔开）
     * @param designPhase  阶段（支持多绚，用','隔开）
     * @param taskName
     * @param projManager
     * @param creater
     * @param sign
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getTaskHeaderList(String taskTypes, String designPhase, String taskName, String projSearch, String projManager
                                              , String creater, long sign , Long taskHeaderId, long isoa, long userId, String respKey , long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<TaskHeaderDto> resultList = (List<TaskHeaderDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_task_headers(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, taskTypes);// 设置输入参数的值
                        cs.setString(2, designPhase);// 设置输入参数的值
                        cs.setString(3, taskName);// 设置输入参数的值
                        cs.setString(4, projSearch);// 设置输入参数的值
                        cs.setString(5, projManager);// 设置输入参数的值
                        cs.setString(6, creater);// 设置输入参数的值
                        cs.setLong(7, sign);// 设置输入参数的值
                        if (null == taskHeaderId)
                            cs.setBigDecimal(8, null);
                        else
                            cs.setLong(8, taskHeaderId);
                        //cs.setLong(8, taskHeaderId);// 设置输入参数的值
                        cs.setLong(9, isoa);// 设置输入参数的值
                        cs.setLong(10, userId);// 设置输入参数的值
                        cs.setString(11, respKey);// 设置输入参数的值
                        cs.setLong(12, pageNo);// 设置输入参数的值
                        cs.setLong(13, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(14, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(15, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskHeaderDto> results = new ArrayList<TaskHeaderDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(14);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(15));
                        TaskHeaderDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TaskHeaderDto();
                            tmp.setTaskHeaderId(rs.getLong("TASK_HEADER_ID"));
                            tmp.setVersionNumber(rs.getLong("VERSION_NUMBER"));
                            tmp.setTemplateHeaderId(rs.getLong("TEMPLATE_HEADER_ID"));
                            tmp.setTemplateHeaderName(rs.getString("TEMPLATE_HEADER_NAME"));
                            tmp.setTaskNumber(rs.getString("TASK_NUMBER"));
                            tmp.setTaskName(rs.getString("TASK_NAME"));
                            tmp.setTaskStatus(rs.getString("TASK_STATUS"));
                            tmp.setTaskStatusName(rs.getString("TASK_STATUS_NAME"));
                            tmp.setProjectId(rs.getLong("PROJECT_ID"));
                            tmp.setProjectNumber(rs.getString("PROJECT_NUMBER"));
                            tmp.setProjectName(rs.getString("PROJECT_NAME"));
                            tmp.setDesignPhase(rs.getString("DESIGN_PHASE"));
                            tmp.setDesignPhaseName(rs.getString("DESIGN_PHASE_NAME"));
                            tmp.setComments(rs.getString("COMMENTS"));
                            tmp.setTaskType(rs.getString("TASK_TYPE"));
                            tmp.setTaskTypeName(rs.getString("TASK_TYPE_NAME"));
                            tmp.setObjectVersionNumber(rs.getLong("OBJECT_VERSION_NUMBER"));
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
                            tmp.setTaskProgress(rs.getString("TASK_PROGRESS"));
                            tmp.setTaskProgress2(rs.getString("TASK_PROGRESS2"));
                            tmp.setProjectRoleName(rs.getString("MY_PROJECT_ROLE_NAME"));
                            tmp.setProjectManagerName(rs.getString("PROJECT_MANAGER"));
                            tmp.setTag(rs.getString("TAG"));

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
     * 获取历史任务列表
     * @param taskSearch
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getTaskHeaderHistoryList(String taskSearch, Long taskHeaderId,String taskType, long userId , long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<TaskHeaderDto> resultList = (List<TaskHeaderDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_task_headers_history(?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, taskSearch);// 设置输入参数的值
                        if (null == taskHeaderId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, taskHeaderId);
                        cs.setString(3, taskType);// 设置输入参数的值
                        cs.setLong(4, userId);// 设置输入参数的值
                        cs.setLong(5, pageNo);// 设置输入参数的值
                        cs.setLong(6, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(7, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(8, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskHeaderDto> results = new ArrayList<TaskHeaderDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(7);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(8));
                        TaskHeaderDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TaskHeaderDto();
                            tmp.setTaskHeaderId(rs.getLong("TASK_HEADER_ID"));
                            tmp.setVersionNumber(rs.getLong("VERSION_NUMBER"));
                            tmp.setTemplateHeaderId(rs.getLong("TEMPLATE_HEADER_ID"));
                            tmp.setTemplateHeaderName(rs.getString("TEMPLATE_HEADER_NAME"));
                            tmp.setTaskNumber(rs.getString("TASK_NUMBER"));
                            tmp.setTaskName(rs.getString("TASK_NAME"));
                            tmp.setTaskStatus(rs.getString("TASK_STATUS"));
                            tmp.setTaskStatusName(rs.getString("TASK_STATUS_NAME"));
                            tmp.setProjectId(rs.getLong("PROJECT_ID"));
                            tmp.setProjectNumber(rs.getString("PROJECT_NUMBER"));
                            tmp.setProjectName(rs.getString("PROJECT_NAME"));
                            tmp.setDesignPhase(rs.getString("DESIGN_PHASE"));
                            tmp.setDesignPhaseName(rs.getString("DESIGN_PHASE_NAME"));
                            tmp.setComments(rs.getString("COMMENTS"));
                            tmp.setTaskType(rs.getString("TASK_TYPE"));
                            tmp.setTaskTypeName(rs.getString("TASK_TYPE_NAME"));
                            tmp.setObjectVersionNumber(rs.getLong("OBJECT_VERSION_NUMBER"));
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
                            tmp.setTaskProgress(rs.getString("TASK_PROGRESS"));
                            tmp.setTaskProgress2(rs.getString("TASK_PROGRESS2"));
                            tmp.setProjectRoleName(rs.getString("MY_PROJECT_ROLE_NAME"));
                            tmp.setProjectManagerName(rs.getString("PROJECT_MANAGER"));
                            tmp.setTag(rs.getString("TAG"));

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
     * 文本任务名称序号
     * @param taskType
     * @param projectId
     * @return
     */
    public String getTaskchapterNameNo(String taskType,long projectId) {
        String i = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_task_header_name_no(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, taskType);
                        cs.setLong(2, projectId);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return cs.getString(3);
                    }
                });
        return i;
    }

    /**
     *删除文本任务（头表和明细表数据）
     * @param taskHeaderId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map delTask(String taskHeaderId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.dele_task_headers(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, taskHeaderId);
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
     *更新文本任务是够共享字段
     * @param taskHeaderId
     * @param taskShared
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map updateTaskShared(long taskHeaderId, long taskShared) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.update_task_header_shared(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.setLong(2, taskShared);
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
     *检查文本名称是够唯一
     * @param taskName
     * @param taskHeaderId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public int checkTaskName(String taskName, Long taskHeaderId) {
        int i = (int) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.check_task_header_name(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, taskName);
                        if (null == taskHeaderId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, taskHeaderId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return cs.getInt(3);
                    }
                });
        return i;
    }

    /**
     * 检测用户的权限（创建权限、检审提交等）
     * @param permissionType CREATE 创建、SUBMIT 检审提交
     * @param projectId
     * @param taskType
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public int checkTaskPermissions(String permissionType, Long projectId, String taskType, Long userId) {
        int i = (int) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.check_task_permissions(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, permissionType);
                        cs.setLong(2, projectId);
                        cs.setString(3, taskType);
                        cs.setLong(4, userId);
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return cs.getInt(5);
                    }
                });
        return i;
    }

    /**
     *插入文本任务列表，另外复制生成文本任务章节数据
     * @param taskHeaderDto
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map addTaskHeader(TaskHeaderDto taskHeaderDto, Long userId) {
        taskHeaderDto.setDateAndUser(userId);
        List<TaskHeaderEntity> dataList = new ArrayList<TaskHeaderEntity>();
        dataList.add(this.taskDtoToEntity(taskHeaderDto));
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_NBCC_TASK.insert_task_header(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.NBCC_TASK_HEADERS_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        //cs.setString(2, tasSource);
                        //cs.setLong(3, tasSourceId);
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
     *更新文本任务章节的节点负责人、工作包、设计人员、校核人员、检审人员、审定人员等信息
     * @param taskHeaderId
     * @param taskChaperId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map updateTaskElement(long taskHeaderId, Long taskChaperId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.update_task_element(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        if (null == taskChaperId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, taskChaperId);
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
     * 获取人员可查看的项目列表
     * @param projectSearch
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getProjectList(String projectSearch, long userId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<ProjectDto> resultList = (List<ProjectDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_projects(?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, projectSearch);// 设置输入参数的值
                        cs.setLong(2, userId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectDto> results = new ArrayList<ProjectDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(4));
                        ProjectDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectDto();
                            tmp.setProjectId(rs.getLong("projectId"));
                            tmp.setProjectNumber(rs.getString("projectNumber"));
                            tmp.setProjectName(rs.getString("projectName"));
                            tmp.setProjectManager(rs.getString("projectManager"));
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
     * 获取文本任务章节明细
     * @param taskHeaderId
     * @param listType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getTaskChaptersList(long taskHeaderId,String listType,String lineType, Long elementId, long userId, long isoa, long pageNo, long pageSize, int type) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<TaskChaptersDto> resultList = (List<TaskChaptersDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_task_chapters(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);// 设置输入参数的值
                        cs.setString(2, listType);// 设置输入参数的值
                        cs.setString(3, lineType);// 设置输入参数的值
                        if (null == elementId)
                            cs.setBigDecimal(4, null);
                        else
                            cs.setLong(4, elementId);// 设置输入参数的值
                        cs.setLong(5, userId);// 设置输入参数的值
                        cs.setLong(6, isoa);// 设置输入参数的值
                        cs.setLong(7, pageNo);// 设置输入参数的值
                        cs.setLong(8, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(9, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(10, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskChaptersDto> results = new ArrayList<TaskChaptersDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(9);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(10));
                        TaskChaptersDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TaskChaptersDto();
                            tmp.setChapterId(rs.getLong("CHAPTER_ID"));
                            tmp.setId(rs.getLong("CHAPTER_ID"));
                            tmp.setParentChapterId(rs.getLong("PARENT_CHAPTER_ID"));
                            tmp.setTaskHeaderId(rs.getLong("TASK_HEADER_ID"));
                            tmp.setTemplateChapterId(rs.getLong("TEMPLATE_CHAPTER_ID"));
                            tmp.setTemplateParentChapterId(rs.getLong("TEMPLATE_PARENT_CHAPTER_ID"));
                            tmp.setProjElementId(rs.getLong("PROJ_ELEMENT_ID"));
                            tmp.setProjElementNumber(rs.getString("PROJ_ELEMENT_NUMBER"));
                            tmp.setProjElementName(rs.getString("PROJ_ELEMENT_NAME"));
                            tmp.setPersonIdResponsible(rs.getLong("PERSON_ID_RESPONSIBLE"));
                            tmp.setPersonNameResponsible(rs.getString("PERSON_NAME_RESPONSIBLE"));
                            tmp.setPersonIdProofread(rs.getLong("PERSON_ID_PROOFREAD"));
                            tmp.setPersonNameProofread(rs.getString("PERSON_NAME_PROOFREAD"));
                            tmp.setPersonIdAudit(rs.getLong("PERSON_ID_AUDIT"));
                            tmp.setPersonNameAudit(rs.getString("PERSON_NAME_AUDIT"));
                            tmp.setPersonIdApprove(rs.getLong("PERSON_ID_APPROVE"));
                            tmp.setPersonNameApprove(rs.getString("PERSON_NAME_APPROVE"));
                            tmp.setSpecialty(rs.getString("SPECIALTY"));
                            tmp.setSpecialtyName(rs.getString("SPECIALTY_NAME"));
                            tmp.setChapterStatus(rs.getString("CHAPTER_STATUS"));
                            tmp.setChapterStatusName(rs.getString("CHAPTER_STATUS_NAME"));
                            tmp.setEnableFlag(rs.getLong("ENABLE_FLAG"));
                            tmp.setComments(rs.getString("COMMENTS"));
                            tmp.setObjectVersionNumber(rs.getLong("OBJECT_VERSION_NUMBER"));
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
                            tmp.setAttribute11Name(rs.getString("ATTRIBUTE11_NAME"));
                            tmp.setAttribute12(rs.getString("ATTRIBUTE12"));
                            tmp.setAttribute12Name(rs.getString("ATTRIBUTE12_NAME"));
                            tmp.setAttribute13(rs.getString("ATTRIBUTE13"));
                            tmp.setAttribute14(rs.getString("ATTRIBUTE14"));
                            tmp.setAttribute15(rs.getString("ATTRIBUTE15"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_UPDATED_BY"));
                            tmp.setLastUpdateLogin(rs.getLong("LAST_UPDATE_LOGIN"));
                            tmp.setCreatedBy(rs.getLong("CREATED_BY"));
                            tmp.setCreationDate(rs.getDate("CREATION_DATE"));
                            tmp.setProjectRoleId(rs.getLong("PROJECT_ROLE_ID"));
                            tmp.setProjectRoleName(rs.getString("PROJECT_ROLE_NAME"));
                            tmp.setChapterNo(rs.getString("CHAPTER_NO"));
                            tmp.setChapterName(rs.getString("CHAPTER_NAME"));
                            tmp.setPersonIdDesign(rs.getLong("PERSON_ID_DESIGN"));
                            tmp.setPersonNameDesign(rs.getString("PERSON_NAME_DESIGN"));
                            tmp.setDeleteFlag(rs.getLong("DELETE_FLAG"));
                            tmp.setEditFlag(rs.getLong("EDIT_FLAG"));
                            tmp.setWordFlag(rs.getString("WORD_FLAG"));
                            tmp.setXmlCode(rs.getString("XML_CODE"));
                            tmp.setTemplateCode(rs.getString("template_code"));
                            tmp.setTemplateApplShortCode(rs.getString("template_appl_short_code"));
                            tmp.setTaskChapterFileName(rs.getString("task_chapter_file_name"));
                            tmp.setSystempLineName(rs.getString("SYSTEM_LINK_NAME"));
                            tmp.setSystempLineUrl(rs.getString("SYSTEM_LINK_URL"));
                            tmp.setIconCls(rs.getString("ICONCLS"));
                            tmp.setChapterNumber(rs.getDouble("CHAPTER_NUMBER"));
                            tmp.setIsLeafNode(rs.getLong("ISLEAF_FLAG"));
                            tmp.setChapterFileFlag(templateService.checkFile(rs.getString("task_chapter_file_name")));

                            // tmp.setTaskChapterWordDownFlag(rs.getLong("WORDDOWN_FLAG"));

                            //tmp.setEditFlag(1l);
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);

        List<TaskChaptersDto> nodeList = new ArrayList<TaskChaptersDto>();
        if (0 == type) {
            if (0 < dt.getDataTotal()) {
                List<TaskChaptersDto> tmpList = dt.getDataSource();
                //由于ALL已经删除了根节点
                if (!lineType.equals("ALL")){
                    nodeList.add(tmpList.get(0));
                    tmpList.remove(0);
                }
                for(TaskChaptersDto node1 : tmpList){
                    boolean mark = false;
                    for(TaskChaptersDto node2 : tmpList){
                        if(null != node1.getParentChapterId() && node1.getParentChapterId().equals(node2.getChapterId())){
                            mark = true;
                            if(node2.getChildren() == null)
                                node2.setChildren(new ArrayList<TaskChaptersDto>());
                            node2.getChildren().add(node1);
                            break;
                        }
                    }
                    if(!mark){
                        nodeList.add(node1);
                    }
                }
            }
        } else {
            nodeList = dt.getDataSource();
        }
        dt.setDataSource(nodeList);
        return dt;
    }


    /**
     * 获取模板或文本任务章节列表(展示文本内容使用)
     * @param source
     * @param headerId
     * @param lineType
     * @param userId
     * @param isoa
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getChaptersList(String source, long headerId, String lineType, long userId, long isoa, long type) {
        DataTablesDTO dt = new DataTablesDTO();
//        dt.setPageNo(pageNo);
//        dt.setPageSize(pageSize);
        List<TaskChaptersDto> resultList = (List<TaskChaptersDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.GET_TT_CHAPTERS(?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, source);// 设置输入参数的值
                        cs.setLong(2, headerId);// 设置输入参数的值
                        cs.setString(3, lineType);// 设置输入参数的值
                        cs.setLong(4, userId);// 设置输入参数的值
                        cs.setLong(5, isoa);// 设置输入参数的值
                        cs.registerOutParameter(6, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskChaptersDto> results = new ArrayList<TaskChaptersDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(7));
                        TaskChaptersDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TaskChaptersDto();
                            tmp.setChapterId(rs.getLong("CHAPTER_ID"));
                            tmp.setId(rs.getLong("CHAPTER_ID"));
                            tmp.setParentChapterId(rs.getLong("PARENT_CHAPTER_ID"));
                            tmp.setTaskHeaderId(rs.getLong("TASK_HEADER_ID"));
                            tmp.setTemplateChapterId(rs.getLong("TEMPLATE_CHAPTER_ID"));
                            tmp.setTemplateParentChapterId(rs.getLong("TEMPLATE_PARENT_CHAPTER_ID"));
                            tmp.setProjElementId(rs.getLong("PROJ_ELEMENT_ID"));
                            tmp.setProjElementNumber(rs.getString("PROJ_ELEMENT_NUMBER"));
                            tmp.setProjElementName(rs.getString("PROJ_ELEMENT_NAME"));
                            tmp.setPersonIdResponsible(rs.getLong("PERSON_ID_RESPONSIBLE"));
                            tmp.setPersonNameResponsible(rs.getString("PERSON_NAME_RESPONSIBLE"));
                            tmp.setPersonIdProofread(rs.getLong("PERSON_ID_PROOFREAD"));
                            tmp.setPersonNameProofread(rs.getString("PERSON_NAME_PROOFREAD"));
                            tmp.setPersonIdAudit(rs.getLong("PERSON_ID_AUDIT"));
                            tmp.setPersonNameAudit(rs.getString("PERSON_NAME_AUDIT"));
                            tmp.setPersonIdApprove(rs.getLong("PERSON_ID_APPROVE"));
                            tmp.setPersonNameApprove(rs.getString("PERSON_NAME_APPROVE"));
                            tmp.setSpecialty(rs.getString("SPECIALTY"));
                            tmp.setSpecialtyName(rs.getString("SPECIALTY_NAME"));
                            tmp.setChapterStatus(rs.getString("CHAPTER_STATUS"));
                            tmp.setChapterStatusName(rs.getString("CHAPTER_STATUS_NAME"));
                            tmp.setEnableFlag(rs.getLong("ENABLE_FLAG"));
                            tmp.setComments(rs.getString("COMMENTS"));
                            tmp.setObjectVersionNumber(rs.getLong("OBJECT_VERSION_NUMBER"));
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
                            tmp.setAttribute11Name(rs.getString("ATTRIBUTE11_NAME"));
                            tmp.setAttribute12(rs.getString("ATTRIBUTE12"));
                            tmp.setAttribute12Name(rs.getString("ATTRIBUTE12_NAME"));
                            tmp.setAttribute13(rs.getString("ATTRIBUTE13"));
                            tmp.setAttribute14(rs.getString("ATTRIBUTE14"));
                            tmp.setAttribute15(rs.getString("ATTRIBUTE15"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_UPDATED_BY"));
                            tmp.setLastUpdateLogin(rs.getLong("LAST_UPDATE_LOGIN"));
                            tmp.setCreatedBy(rs.getLong("CREATED_BY"));
                            tmp.setCreationDate(rs.getDate("CREATION_DATE"));
                            tmp.setProjectRoleId(rs.getLong("PROJECT_ROLE_ID"));
                            tmp.setProjectRoleName(rs.getString("PROJECT_ROLE_NAME"));
                            tmp.setChapterNo(rs.getString("CHAPTER_NO"));
                            tmp.setChapterName(rs.getString("CHAPTER_NAME"));
                            tmp.setPersonIdDesign(rs.getLong("PERSON_ID_DESIGN"));
                            tmp.setPersonNameDesign(rs.getString("PERSON_NAME_DESIGN"));
                            tmp.setDeleteFlag(rs.getLong("DELETE_FLAG"));
                            tmp.setEditFlag(rs.getLong("EDIT_FLAG"));
                            tmp.setWordFlag(rs.getString("WORD_FLAG"));
                            tmp.setXmlCode(rs.getString("XML_CODE"));
                            tmp.setTemplateCode(rs.getString("template_code"));
                            tmp.setTemplateApplShortCode(rs.getString("template_appl_short_code"));
                            tmp.setTaskChapterFileName(rs.getString("task_chapter_file_name"));
                            tmp.setSystempLineName(rs.getString("SYSTEM_LINK_NAME"));
                            tmp.setSystempLineUrl(rs.getString("SYSTEM_LINK_URL"));
                            tmp.setIconCls(rs.getString("ICONCLS"));
                            tmp.setChapterNumber(rs.getDouble("CHAPTER_NUMBER"));
                            tmp.setChapterFileFlag(templateService.checkFile(rs.getString("task_chapter_file_name")));
                            tmp.setSourceChapterFileName(rs.getString("SOURCE_CHAPTER_FILE_NAME"));

                            //tmp.setEditFlag(1l);
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);

        List<TaskChaptersDto> nodeList = new ArrayList<TaskChaptersDto>();
        if (0 == type) {
            if (0 < dt.getDataTotal()) {
                List<TaskChaptersDto> tmpList = dt.getDataSource();
                //由于ALL已经删除了根节点
                if (!lineType.equals("ALL")){
                    nodeList.add(tmpList.get(0));
                    tmpList.remove(0);
                }
                for(TaskChaptersDto node1 : tmpList){
                    boolean mark = false;
                    for(TaskChaptersDto node2 : tmpList){
                        if(null != node1.getParentChapterId() && node1.getParentChapterId().equals(node2.getChapterId())){
                            mark = true;
                            if(node2.getChildren() == null)
                                node2.setChildren(new ArrayList<TaskChaptersDto>());
                            node2.getChildren().add(node1);
                            break;
                        }
                    }
                    if(!mark){
                        nodeList.add(node1);
                    }
                }
            }
        } else {
            nodeList = dt.getDataSource();
        }
        dt.setDataSource(nodeList);
        return dt;
    }


    /**
     * 保存文本任务章节信息
     * @param chaptersDTOs
     * @param ldapId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map addOrUpTaskChapters(List<TaskChaptersDto> chaptersDTOs, Long ldapId) {
        List<TaskChaptersEntity> inData = chaptersDTOs.stream().map(chapter -> {
            chapter.setDateAndUser(ldapId);
            TaskChaptersEntity tmp = this.cdtoToEntity(chapter);
            return tmp;
        }).collect(Collectors.toList());
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_NBCC_TASK.insert_task_chapter(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.NBCC_TASK_CHAPTER_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
                        cs.setArray(1, vArray);
                        cs.setLong(2, ldapId);
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
     * 获取文本任务ID
     * @return
     */
    public long getTaskchapterId() {
        long i = (long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_task_chapter_id(?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return cs.getLong(1);
                    }
                });
        return i;
    }

    /**
     * 删除文本任务章节
     * @param taskHeaderId
     * @param taskChaperIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map deleteTaskchapters(long taskHeaderId, String taskChaperIds) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.dele_task_chapters(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.setString(2, taskChaperIds);
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
     * 获取该文本任务所有的状态列表
     * @param taskHeaderId
     * @param taskChapterId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getMyTaskChapterStatusList(Long taskHeaderId, Long taskChapterId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<MyTaskChapterStatusDto> resultList = (List<MyTaskChapterStatusDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_task_chapter_status(?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);// 设置输入参数的值
                        if (null == taskChapterId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, taskChapterId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<MyTaskChapterStatusDto> results = new ArrayList<MyTaskChapterStatusDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(4));
                        MyTaskChapterStatusDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new MyTaskChapterStatusDto();
                            tmp.setChapterStatus(rs.getString("CHAPTER_STATUS"));
                            tmp.setChapterStatusName(rs.getString("CHAPTER_STATUS_NAME"));
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
     * 获取该文本章节中已分配的所有的专业列表
     * @param taskHeaderId
     * @param taskChapterId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getMyTaskChapterSpecialtysList(Long taskHeaderId, Long taskChapterId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<MyTaskChapterSpecialtysDto> resultList = (List<MyTaskChapterSpecialtysDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_task_chapter_specialtys(?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);// 设置输入参数的值
                        if (null == taskChapterId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, taskChapterId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<MyTaskChapterSpecialtysDto> results = new ArrayList<MyTaskChapterSpecialtysDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(4));
                        MyTaskChapterSpecialtysDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new MyTaskChapterSpecialtysDto();
                            tmp.setSpecialty(rs.getString("SPECIALTY"));
                            tmp.setSpecialtyName(rs.getString("SPECIALTY_NAME"));
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
     *获取节点负责人列表
     * @param projectId
     * @param specialty
     * @param roleId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getTaskChapterResponsibleList(Long projectId,String specialty, Long  roleId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<TaskResponsibleDto> resultList = (List<TaskResponsibleDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_task_chapter_responsibles(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setString(2, specialty);// 设置输入参数的值
                        cs.setLong(3, roleId);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskResponsibleDto> results = new ArrayList<TaskResponsibleDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(5));
                        TaskResponsibleDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TaskResponsibleDto();
                            tmp.setPersonIdResponsible(rs.getLong("person_id_responsible"));
                            tmp.setPersonNameResponsible(rs.getString("person_name_responsible"));
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
     * 获取工作包列表
     * @param projectId
     * @param specialty
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getProjectElementList(Long projectId,String specialty,Long taskHeaderId,Long userID) {
        DataTablesDTO dt = new DataTablesDTO();
        List<projectElementDto> resultList = (List<projectElementDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_project_elements(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == projectId)
                            cs.setBigDecimal(1, null);// 设置输入参数的值
                        else
                            cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setString(2, specialty);// 设置输入参数的值
                        if (null == taskHeaderId)
                            cs.setBigDecimal(3, null);// 设置输入参数的值
                        else
                            cs.setLong(3, taskHeaderId);// 设置输入参数的值
                        if (null == userID)
                            cs.setBigDecimal(4, null);// 设置输入参数的值
                        else
                            cs.setLong(4, userID);// 设置输入参数的值
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<projectElementDto> results = new ArrayList<projectElementDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(6));
                        projectElementDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new projectElementDto();
                            tmp.setElementId(rs.getLong("proj_element_id"));
                            tmp.setElementNumber(rs.getString("element_number"));
                            tmp.setElementName(rs.getString("element_name"));
                            tmp.setPersonIdDesign(rs.getLong("person_id_design"));
                            tmp.setPersonNameDesign(rs.getString("person_name_design"));
                            tmp.setPersonIdCheck(rs.getLong("person_id_check"));
                            tmp.setPersonNameCheck(rs.getString("person_name_check"));
                            tmp.setPersonIdVerify(rs.getLong("person_id_verify"));
                            tmp.setPersonNameVerify(rs.getString("person_name_verify"));
                            tmp.setSpecialty(rs.getString("specialty"));
                            tmp.setSpecialtyName(rs.getString("specialty_name"));
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
     * 专业检审状态回退时工作包列表
     * @param projectId
     * @param taskHeaderId
     * @param userID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getRollBackProjectElementList(Long projectId,Long taskHeaderId,Long userID) {
        DataTablesDTO dt = new DataTablesDTO();
        List<projectElementDto> resultList = (List<projectElementDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.GET_PROJECT_ELEMENTS_ROLLBACK(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == projectId)
                            cs.setBigDecimal(1, null);// 设置输入参数的值
                        else
                            cs.setLong(1, projectId);// 设置输入参数的值
                        if (null == taskHeaderId)
                            cs.setBigDecimal(2, null);// 设置输入参数的值
                        else
                            cs.setLong(2, taskHeaderId);// 设置输入参数的值
                        if (null == userID)
                            cs.setBigDecimal(3, null);// 设置输入参数的值
                        else
                            cs.setLong(3, userID);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<projectElementDto> results = new ArrayList<projectElementDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(5));
                        projectElementDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new projectElementDto();
                            tmp.setElementId(rs.getLong("proj_element_id"));
                            tmp.setElementNumber(rs.getString("element_number"));
                            tmp.setElementName(rs.getString("element_name"));
                            tmp.setPersonIdDesign(rs.getLong("person_id_design"));
                            tmp.setPersonNameDesign(rs.getString("person_name_design"));
                            tmp.setPersonIdCheck(rs.getLong("person_id_check"));
                            tmp.setPersonNameCheck(rs.getString("person_name_check"));
                            tmp.setPersonIdVerify(rs.getLong("person_id_verify"));
                            tmp.setPersonNameVerify(rs.getString("person_name_verify"));
                            tmp.setSpecialty(rs.getString("specialty"));
                            tmp.setSpecialtyName(rs.getString("specialty_name"));
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
     * 获取工作包对应的设计人员、校核人员、审核人员 、审定人员值列表
     * @param elementId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public ProjectPersonsDto getProjectElementPersonList(Long elementId) {
        ProjectPersonsDto projectPersonsDto = (ProjectPersonsDto) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_proj_element_person_list(?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, elementId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        ProjectPersonsDto dt = new ProjectPersonsDto();

                        List<ProjectPersonDto> designs = new ArrayList<ProjectPersonDto>();
                        List<ProjectPersonDto> checks = new ArrayList<ProjectPersonDto>();
                        List<ProjectPersonDto> verifys = new ArrayList<ProjectPersonDto>();
                        List<ProjectPersonDto> approves = new ArrayList<ProjectPersonDto>();
                        cs.execute();

                        ResultSet rs2 = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        ProjectPersonDto tmp2 = null;
                        while (rs2.next()) {// 转换每行的返回值到Map中
                            tmp2 = new ProjectPersonDto();
                            tmp2.setPersonId(rs2.getLong("person_id_design"));
                            tmp2.setPersonName(rs2.getString("person_name_design"));
                            designs.add(tmp2);
                        }
                        dt.setDesigns(designs);
                        rs2.close();

                        ResultSet rs3 = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        ProjectPersonDto tmp3 = null;
                        while (rs3.next()) {// 转换每行的返回值到Map中
                            tmp3 = new ProjectPersonDto();
                            tmp3.setPersonId(rs3.getLong("person_id_check"));
                            tmp3.setPersonName(rs3.getString("person_name_check"));
                            checks.add(tmp3);
                        }
                        dt.setChecks(checks);
                        rs3.close();

                        ResultSet rs4 = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        ProjectPersonDto tmp4 = null;
                        while (rs4.next()) {// 转换每行的返回值到Map中
                            tmp4 = new ProjectPersonDto();
                            tmp4.setPersonId(rs4.getLong("person_id_verify"));
                            tmp4.setPersonName(rs4.getString("person_name_verify"));
                            verifys.add(tmp4);
                        }
                        dt.setVerifys(verifys);
                        rs4.close();

                        ResultSet rs5 = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        ProjectPersonDto tmp5 = null;
                        while (rs5.next()) {// 转换每行的返回值到Map中
                            tmp5 = new ProjectPersonDto();
                            tmp5.setPersonId(rs5.getLong("person_id_approve"));
                            tmp5.setPersonName(rs5.getString("person_name_approve"));
                            approves.add(tmp5);
                        }
                        dt.setApproves(approves);
                        rs5.close();

                        return dt;
                    }
                });
        return projectPersonsDto;
    }

    /**
     * 获取权限转移的节点负责人员列表
     * @param taskResponsibleDtos
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public List<TaskResponsibleDto> getTaskChapterResponsible2List(List<TaskResponsibleDto> taskResponsibleDtos) {
        List<TaskResponsibleEntity> inData = taskResponsibleDtos.stream().map(responsible -> {
            TaskResponsibleEntity tmp = this.taskResponsiblcdtoToEntity(responsible);
            return tmp;
        }).collect(Collectors.toList());
        List<TaskResponsibleDto> resultList = (List<TaskResponsibleDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_task_chapter_responsibles2(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.NBCC_TASK_RESPONSIBLE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
                        cs.setArray(1, vArray);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                },new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskResponsibleDto> results = new ArrayList<TaskResponsibleDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        //dt.setDataTotal(cs.getLong(5));
                        TaskResponsibleDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TaskResponsibleDto();
                            tmp.setPersonIdResponsible(rs.getLong("person_id_responsible"));
                            tmp.setPersonNameResponsible(rs.getString("person_name_responsible"));
                            tmp.setSpecialty(rs.getString("specialty"));
                            tmp.setSpecialtyName(rs.getString("specialty_name"));
                            tmp.setRoleId(rs.getLong("role_id"));
                            tmp.setRoleName(rs.getString("role_name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 权限转移
     * @param taskHeaderId
     * @param taskChaperIds  可以选择多个章节节点，用逗号隔开
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map updateTaskResponsible(long taskHeaderId, String taskChaperIds,List<TaskResponsibleDto> taskResponsibleDtos, Long userId) {
        List<TaskResponsibleEntity> inData = taskResponsibleDtos.stream().map(responsible -> {
            TaskResponsibleEntity tmp = this.taskResponsiblcdtoToEntity(responsible);
            return tmp;
        }).collect(Collectors.toList());
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_NBCC_TASK.update_task_responsible(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.NBCC_TASK_RESPONSIBLE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
                        cs.setLong(1, taskHeaderId);
                        cs.setString(2, taskChaperIds);
                        cs.setLong(3, userId);
                        cs.setArray(4, vArray);
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(NbccParm.DB_STATE, cs.getLong(5));
                        tmp.put(NbccParm.DB_MSG, cs.getString(6));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 修改行状态
     * @param taskHeaderId
     * @param taskChaperId
     * @param chapterStatus
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map updateTaskChapterStatus(long taskHeaderId, Long taskChaperId,String chapterStatus) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.update_task_chapter_status(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        if (null == taskChaperId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, taskChaperId);
                        cs.setString(3, chapterStatus);
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

    /**
     * 签出时提示的专业（人员有权限检出的并未检出专业）
     * @param taskHeaderId
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getSpecialtyForLockList(long taskHeaderId, long  userId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<SpecialtyDTO> resultList = (List<SpecialtyDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_specialty_for_lock(?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);// 设置输入参数的值
                        cs.setLong(2, userId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<SpecialtyDTO> results = new ArrayList<SpecialtyDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(4));
                        SpecialtyDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SpecialtyDTO();
                            tmp.setSpecialty(rs.getString("specialty"));
                            tmp.setSpecialtyName(rs.getString("specialtyName"));
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
     * 检出文本章节（锁定）
     * @param taskHeaderId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map lockTaskchapters(long taskHeaderId, String taskChaperIds,long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.lock_task_chapters(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.setString(2, taskChaperIds);
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

    /**
     * 检入章节信息（解锁）
     * @param taskHeaderId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map unLockTaskchapters(long taskHeaderId,long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.unlock_task_chapters(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
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
     * 发布时检查专业是否都已分配
     * @param taskHeaderId
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO checkTaskSpecialty(Long taskHeaderId,long userId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<SpecialtyDTO> resultList = (List<SpecialtyDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.check_task_specialty(?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);// 设置输入参数的值
                        cs.setLong(2, userId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<SpecialtyDTO> results = new ArrayList<SpecialtyDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(4));
                        SpecialtyDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SpecialtyDTO();
                            tmp.setSpecialty(rs.getString("specialty"));
                            tmp.setSpecialtyName(rs.getString("specialtyName"));
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
     * 文本任务章节发布
     * @param taskHeaderId
     * @param chapterIds 可多选，逗号隔开
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map publishTaskChapters(long taskHeaderId,String chapterIds, long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.task_chapters_publish(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.setString(2, chapterIds);
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

    /**
     * 文本任务专业检审提交
     * @param taskHeaderId
     * @param elementId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map iapprovingTaskChapters(long taskHeaderId,long elementId, long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.task_chapters_iapproving(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.setLong(2, elementId);
                        cs.setLong(3, userId);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(4));
                        tmp.put(NbccParm.DB_STATE, cs.getLong(5));
                        tmp.put(NbccParm.DB_MSG, cs.getString(6));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 专业检审状态回退
     * @param taskHeaderId
     * @param elementId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map nIapprovedTaskChapters(long taskHeaderId,long elementId, long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.UPDATE_TASK_CHAPTER_NIAPPROVED(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.setLong(2, elementId);
                        cs.setLong(3, userId);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
//                        tmp.put(NbccParm.DB_URL, cs.getString(4));
                        tmp.put(NbccParm.DB_STATE, cs.getLong(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     *  公司级评审申请
     * @param taskHeaderId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map applyTaskChapters(long taskHeaderId,long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.task_chapters_apply(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(3));
                        tmp.put(NbccParm.DB_STATE, cs.getLong(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     *  提交公司级评审
     * @param taskHeaderId
     * @param ChapterIds
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map eapprovingTaskChapters(long taskHeaderId,String ChapterIds, long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.task_chapters_eapproving(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.setString(2, ChapterIds);
                        cs.setLong(3, userId);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(4));
                        tmp.put(NbccParm.DB_STATE, cs.getLong(5));
                        tmp.put(NbccParm.DB_MSG, cs.getString(6));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 填写院审结论表
     * @param taskHeaderId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map approvingaTaskChapters(long taskHeaderId, long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.task_chapters_approving_a(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(3));
                        tmp.put(NbccParm.DB_STATE, cs.getLong(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 文本任务提交审批
     * @param taskHeaderId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map approvingTaskChapters(long taskHeaderId, long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.task_chapters_approving(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(3));
                        tmp.put(NbccParm.DB_STATE, cs.getLong(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getTaskChaptersForWord(long taskHeaderId,String listType, String lineType, Long elementId,long userId, long pageNo, long pageSize, int type) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<TaskChaptersDto> resultList = (List<TaskChaptersDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TASK.get_task_chapters(?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, taskHeaderId);// 设置输入参数的值
                        cs.setString(2, listType);// 设置输入参数的值
                        cs.setString(3, lineType);// 设置输入参数的值
                        if (null == elementId)
                            cs.setBigDecimal(4, null);
                        else
                            cs.setLong(4, elementId);// 设置输入参数的值
                        cs.setLong(5, userId);// 设置输入参数的值
                        cs.setLong(6, pageNo);// 设置输入参数的值
                        cs.setLong(7, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(8, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(9, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskChaptersDto> results = new ArrayList<TaskChaptersDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(8);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(9));
                        TaskChaptersDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TaskChaptersDto();
                            tmp.setChapterId(rs.getLong("CHAPTER_ID"));
                            tmp.setParentChapterId(rs.getLong("PARENT_CHAPTER_ID"));
                            tmp.setTaskHeaderId(rs.getLong("TASK_HEADER_ID"));
                            tmp.setTemplateChapterId(rs.getLong("TEMPLATE_CHAPTER_ID"));
                            tmp.setTemplateParentChapterId(rs.getLong("TEMPLATE_PARENT_CHAPTER_ID"));
                            tmp.setChapterStatus(rs.getString("CHAPTER_STATUS"));
                            tmp.setChapterStatusName(rs.getString("CHAPTER_STATUS_NAME"));
                            tmp.setEnableFlag(rs.getLong("ENABLE_FLAG"));
                            tmp.setComments(rs.getString("COMMENTS"));
                            tmp.setObjectVersionNumber(rs.getLong("OBJECT_VERSION_NUMBER"));
                            tmp.setAttributeCategory(rs.getString("ATTRIBUTE_CATEGORY"));
                            tmp.setAttribute7(rs.getString("ATTRIBUTE7"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_UPDATED_BY"));
                            tmp.setLastUpdateLogin(rs.getLong("LAST_UPDATE_LOGIN"));
                            tmp.setCreatedBy(rs.getLong("CREATED_BY"));
                            tmp.setCreationDate(rs.getDate("CREATION_DATE"));
                            tmp.setChapterNo(rs.getString("CHAPTER_NO"));
                            tmp.setChapterName(rs.getString("CHAPTER_NAME"));
                            tmp.setDeleteFlag(rs.getLong("DELETE_FLAG"));
                            tmp.setEditFlag(rs.getLong("EDIT_FLAG"));
                            tmp.setWordFlag(rs.getString("WORD_FLAG"));
                            tmp.setXmlCode(rs.getString("XML_CODE"));
                            tmp.setTemplateCode(rs.getString("template_code"));
                            tmp.setTemplateApplShortCode(rs.getString("template_appl_short_code"));
                            tmp.setTaskChapterFileName(rs.getString("task_chapter_file_name"));
                            String filePath = nerinProperties.getNbcc().getWordFileUrl() + tmp.getTaskHeaderId() + "/" + tmp.getChapterId() +".docx";
                            File file = new File(filePath);
                            if (file.exists())
                                tmp.setIsExistsWordFile(0);
                            else
                                tmp.setIsExistsWordFile(1);
                            //tmp.setWordFileUrl(nerinProperties.getNbcc().getWordFileUrl() + tmp.getTaskHeaderId() + "/" + tmp.getChapterId() +".docx");
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);

        List<TaskChaptersDto> nodeList = new ArrayList<TaskChaptersDto>();
        if (0 == type) {
            if (0 < dt.getDataTotal()) {
                List<TaskChaptersDto> tmpList = dt.getDataSource();
                nodeList.add(tmpList.get(0));
                tmpList.remove(0);
                for(TaskChaptersDto node1 : tmpList){
                    boolean mark = false;
                    for(TaskChaptersDto node2 : tmpList){
                        if(null != node1.getParentChapterId() && node1.getParentChapterId().equals(node2.getChapterId())){
                            mark = true;
                            if(node2.getChildren() == null)
                                node2.setChildren(new ArrayList<TaskChaptersDto>());
                            node2.getChildren().add(node1);
                            break;
                        }
                    }
                    if(!mark){
                        nodeList.add(node1);
                    }
                }
            }
        } else {
            nodeList = dt.getDataSource();
        }
        dt.setDataSource(nodeList);
        return dt;
    }
}
