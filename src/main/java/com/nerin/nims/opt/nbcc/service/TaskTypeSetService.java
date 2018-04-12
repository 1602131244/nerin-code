package com.nerin.nims.opt.nbcc.service;

import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.*;
import com.nerin.nims.opt.nbcc.module.TaskTypeAssignmentsEntity;
import com.nerin.nims.opt.nbcc.module.TaskTypeElementsEntity;
import com.nerin.nims.opt.nbcc.module.TaskTypeEntity;
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

/**
 * Created by yinglgu on 6/26/2016.
 */
@Component
@Transactional
public class TaskTypeSetService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private TaskTypeEntity dtoToEntity(TaskTypeSetDTO typeSetDTO) {
        TaskTypeEntity entity = new TaskTypeEntity();
        try {
            PropertyUtils.copyProperties(entity, typeSetDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private TaskTypeAssignmentsEntity dtoToEntity2(TaskTypeAssignmentsDTO typeAssignmentsDTO) {
        TaskTypeAssignmentsEntity entity = new TaskTypeAssignmentsEntity();
        try {
            PropertyUtils.copyProperties(entity, typeAssignmentsDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private TaskTypeElementsEntity dtoToEntity3(TaskTypeElementsDTO taskTypeElementsDTO) {
        TaskTypeElementsEntity entity = new TaskTypeElementsEntity();
        try {
            PropertyUtils.copyProperties(entity, taskTypeElementsDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map addOrUpTaskType(TaskTypeSetDTO taskTypeSetDTO, Long ldapId) {
        taskTypeSetDTO.setDateAndUser(ldapId);
        List<TaskTypeEntity> dataList = new ArrayList<TaskTypeEntity>();
        dataList.add(this.dtoToEntity(taskTypeSetDTO));

        List<TaskTypeAssignmentsEntity> assignmentsList = new ArrayList<TaskTypeAssignmentsEntity>();
        for (TaskTypeAssignmentsDTO dto : taskTypeSetDTO.getAssignmentsDTOList())
            assignmentsList.add(this.dtoToEntity2(dto));

        List<TaskTypeElementsEntity> elementsList = new ArrayList<TaskTypeElementsEntity>();
        for (TaskTypeElementsDTO dto : taskTypeSetDTO.getElementsDTOList())
            elementsList.add(this.dtoToEntity3(dto));

        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.insert_task_types(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.NBCC_TASK_TYPE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        ArrayDescriptor tab2Desc = ArrayDescriptor.createDescriptor("APPS.NBCC_TASK_TYPE_ASSIGNMENTS_TBL", or);
                        ARRAY v2Array = new ARRAY(tab2Desc, or, assignmentsList.toArray());
                        ArrayDescriptor tab3Desc = ArrayDescriptor.createDescriptor("APPS.CUX_TASK_TYPE_ELEMENTS_TBL", or);
                        ARRAY v3Array = new ARRAY(tab3Desc, or, elementsList.toArray());
                        cs.setArray(1, vArray);
                        cs.setArray(2, v2Array);
                        cs.setArray(3, v3Array);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(NbccParm.DB_STATE, cs.getLong(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getTaskTypeList(String taskTypeDesc, String taskTypeCode, Long userId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<TaskTypeSetDTO> resultList = (List<TaskTypeSetDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_task_types(?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, taskTypeDesc);
                        cs.setString(2, taskTypeCode);
                        if (null == userId)
                            cs.setBigDecimal(3, null);
                        else
                            cs.setLong(3, userId);
                        cs.setLong(4, pageNo);
                        cs.setLong(5, pageSize);
                        cs.registerOutParameter(6, OracleTypes.CURSOR);
                        cs.registerOutParameter(7, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskTypeSetDTO> results = new ArrayList<TaskTypeSetDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);
                        dt.setDataTotal(cs.getLong(7));
                        TaskTypeSetDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TaskTypeSetDTO();
                            tmp.setLookupCode(rs.getString("LOOKUP_CODE"));
                            tmp.setMeaning(rs.getString("MEANING"));
                            tmp.setDescription(rs.getString("DESCRIPTION"));
                            tmp.setEnabledFlag(rs.getString("ENABLED_FLAG"));
                            tmp.setStartDateActive(rs.getDate("START_DATE_ACTIVE"));
                            tmp.setEndDateActive(rs.getDate("END_DATE_ACTIVE"));
                            tmp.setTag(rs.getString("TAG"));
                            tmp.setAttributeCategory(rs.getString("ATTRIBUTE_CATEGORY"));
                            tmp.setAttribute1(rs.getString("ATTRIBUTE1"));
                            tmp.setAttribute1Name(rs.getString("ATTRIBUTE1_NAME"));
                            tmp.setAttribute2(rs.getString("ATTRIBUTE2"));
                            tmp.setAttribute2Name(rs.getString("ATTRIBUTE2_NAME"));
                            tmp.setAttribute3(rs.getString("ATTRIBUTE3"));
                            tmp.setAttribute3Name(rs.getString("ATTRIBUTE3_NAME"));
                            tmp.setAttribute4(rs.getString("ATTRIBUTE4"));
                            tmp.setAttribute4Name(rs.getString("ATTRIBUTE4_NAME"));
                            tmp.setAttribute5(rs.getString("ATTRIBUTE5"));
                            tmp.setAttribute5Name(rs.getString("ATTRIBUTE5_NAME"));
                            tmp.setAttribute6(rs.getString("ATTRIBUTE6"));
                            tmp.setAttribute6Name(rs.getString("ATTRIBUTE6_NAME"));
                            tmp.setAttribute7(rs.getString("ATTRIBUTE7"));
                            tmp.setAttribute7Name(rs.getString("ATTRIBUTE7_NAME"));
                            tmp.setAttribute8(rs.getString("ATTRIBUTE8"));
                            tmp.setAttribute9(rs.getLong("ATTRIBUTE9"));
                            tmp.setAttribute9Name(rs.getString("ATTRIBUTE9_NAME"));
                            tmp.setAttribute10(rs.getString("ATTRIBUTE10"));
                            tmp.setAttribute11(rs.getString("ATTRIBUTE11"));
                            tmp.setAttribute12(rs.getString("ATTRIBUTE12"));
                            tmp.setAttribute13(rs.getString("ATTRIBUTE13"));
                            tmp.setAttribute14(rs.getString("ATTRIBUTE14"));
                            tmp.setAttribute15(rs.getString("ATTRIBUTE15"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_UPDATED_BY"));
                            tmp.setLastUpdateLogin(rs.getLong("LAST_UPDATE_LOGIN"));
                            tmp.setCreatedBy(rs.getLong("CREATED_BY"));
                            tmp.setCreationDate(rs.getDate("CREATION_DATE"));
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
    public DataTablesDTO getTaskTypeAssignments(String taskTypeCode, String type, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<TaskTypeAssignmentsDTO> resultList = (List<TaskTypeAssignmentsDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_task_type_assignments(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, taskTypeCode);
                        cs.setString(2, type);
                        cs.setLong(3, pageNo);
                        cs.setLong(4, pageSize);
                        cs.registerOutParameter(5, OracleTypes.CURSOR);
                        cs.registerOutParameter(6, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskTypeAssignmentsDTO> results = new ArrayList<TaskTypeAssignmentsDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);
                        dt.setDataTotal(cs.getLong(6));
                        TaskTypeAssignmentsDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TaskTypeAssignmentsDTO();
                            tmp.setAssignmentId(rs.getLong("ASSIGNMENT_ID"));
                            tmp.setTaskTypeCode(rs.getString("TASK_TYPE_CODE"));
                            tmp.setAssignmentType(rs.getString("ASSIGNMENT_TYPE"));
                            tmp.setProjectRoleId(rs.getLong("PROJECT_ROLE_ID"));
                            tmp.setProjectRoleName(rs.getString("PROJECT_ROLE_NAME"));
                            tmp.setSpecialty(rs.getString("SPECIALTY"));
                            tmp.setSpecialtyName(rs.getString("SPECIALTY_NAME"));
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
                            tmp.setAttribute10(rs.getString("ATTRIBUTE10"));
                            tmp.setAttribute11(rs.getString("ATTRIBUTE11"));
                            tmp.setAttribute12(rs.getString("ATTRIBUTE12"));
                            tmp.setAttribute13(rs.getString("ATTRIBUTE13"));
                            tmp.setAttribute14(rs.getString("ATTRIBUTE14"));
                            tmp.setAttribute15(rs.getString("ATTRIBUTE15"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_UPDATED_BY"));
                            tmp.setLastUpdateLogin(rs.getLong("LAST_UPDATE_LOGIN"));
                            tmp.setCreatedBy(rs.getLong("CREATED_BY"));
                            tmp.setCreationDate(rs.getDate("CREATION_DATE"));
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
    public DataTablesDTO getTaskTypeElements(String taskTypeCode, Long elementTypeId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<TaskTypeElementsDTO> resultList = (List<TaskTypeElementsDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.GET_TASK_TYPE_ELEMENTS(?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, taskTypeCode);
                        if (null == elementTypeId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, elementTypeId);
                        cs.setLong(3, pageNo);
                        cs.setLong(4, pageSize);
                        cs.registerOutParameter(5, OracleTypes.CURSOR);
                        cs.registerOutParameter(6, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskTypeElementsDTO> results = new ArrayList<TaskTypeElementsDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);
                        dt.setDataTotal(cs.getLong(6));
                        TaskTypeElementsDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TaskTypeElementsDTO();
                            tmp.setAssignmentId(rs.getLong("ASSIGNMENT_ID"));
                            tmp.setTaskTypeCode(rs.getString("TASK_TYPE_CODE"));
                            tmp.setElementTypeId(rs.getLong("ELEMENT_TYPE_ID"));
                            tmp.setElementTypeName(rs.getString("ELEMENT_TYPE_NAME"));
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
                            tmp.setLastUpdateDate(rs.getDate("LAST_UPDATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_UPDATED_BY"));
                            tmp.setLastUpdateLogin(rs.getLong("LAST_UPDATE_LOGIN"));
                            tmp.setCreatedBy(rs.getLong("CREATED_BY"));
                            tmp.setCreationDate(rs.getDate("CREATION_DATE"));
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
    public DataTablesDTO getProjectPhaseList(String code, Long projectId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<ProjectPhaseDTO> resultList = (List<ProjectPhaseDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_project_phase(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, code);
                        if (null == projectId)
                            cs.setBigDecimal(2,null);
                        else
                            cs.setLong(2, projectId);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectPhaseDTO> results = new ArrayList<ProjectPhaseDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);
                        dt.setDataTotal(cs.getLong(4));
                        ProjectPhaseDTO tmp = null;
                        while (rs.next()) {
                            tmp = new ProjectPhaseDTO();
                            tmp.setPhasesCode(rs.getString("phasesCode"));
                            tmp.setPhasesName(rs.getString("phasesName"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map delTaskType(String code) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.dele_task_type(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, code);
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

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public int checkTaskTypeCodeAndName(String code, String name, int work) {
        int i = (int) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.check_task_type(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (0 == work)
                            cs.setString(1, "I");
                        else
                            cs.setString(1, "U");
                        cs.setString(2, code);
                        cs.setString(3, name);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        return cs.getInt(4);
                    }
                });
        return i;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map delTaskTypeAssignments(String assignments) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.dele_task_type_assignments(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, assignments);
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

    @Transactional(rollbackFor = Exception.class)
    public Map delTaskTypeElements(String assignments) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.DELE_TASK_TYPE_ELEMENTS(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, assignments);
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

    @Transactional(rollbackFor = Exception.class)
    public Map updateTaskTypeStatus(String code, int status) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.update_task_type_status(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, code);
                        cs.setInt(2, status);
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

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getProjectPhaseCategorList(String phaseCategorCode) {
        DataTablesDTO dt = new DataTablesDTO();
        List<ProjectPhaseCategorDto> resultList = (List<ProjectPhaseCategorDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_project_phase_categor(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, phaseCategorCode);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectPhaseCategorDto> results = new ArrayList<ProjectPhaseCategorDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        dt.setDataTotal(cs.getLong(3));
                        ProjectPhaseCategorDto tmp = null;
                        while (rs.next()) {
                            tmp = new ProjectPhaseCategorDto();
                            tmp.setPhaseCategorCode(rs.getString("phaseCategorCode"));
                            tmp.setPhaseCategorName(rs.getString("phaseCategorName"));
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
