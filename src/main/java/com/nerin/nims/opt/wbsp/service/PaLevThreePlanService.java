package com.nerin.nims.opt.wbsp.service;

import com.nerin.nims.opt.wbsp.common.WbspParm;
import com.nerin.nims.opt.wbsp.dto.*;
import com.nerin.nims.opt.wbsp.module.LockSpecEntity;
import com.nerin.nims.opt.wbsp.module.PaDeliverableEntity;
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
 * Created by Administrator on 2016/8/3.
 */
@Component
@Transactional
public class PaLevThreePlanService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PaLevTwoPlanService paLevTwoPlanService;

    /**
     * 初始化项目所需信息
     *
     * @param projectId
     * @param phaseCode
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public PaInfo3RepDTO getProjectInfo(long projectId, String phaseCode, long userId) {
        PaInfo3RepDTO ResultsDTO = (PaInfo3RepDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_Project_Info(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, phaseCode);// 设置输入参数的值
                        cs.setLong(3, userId);// 设置输入项目ID
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        PaInfo3DTO paInfo3DTO = new PaInfo3DTO();
                        PaInfo3RepDTO paInfo3RepDTO = new PaInfo3RepDTO();
                        cs.execute();
                        paInfo3RepDTO.setReturnStatus(cs.getString(5));
                        paInfo3RepDTO.setMsgData(cs.getString(6));
                        if (cs.getString(5).equals("S")) {
                            ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                            while (rs.next()) {// 转换每行的返回值到Map中
                                paInfo3DTO.setProjID(rs.getLong("Project_Id")); //获取对应输出结果
                                paInfo3DTO.setProjName(rs.getString("Project_Name")); //获取对应输出结果
                                paInfo3DTO.setProjNumber(rs.getString("Project_Number")); //获取对应输出结果
                                paInfo3DTO.setPhaseName(rs.getString("Phase_Name")); //获取对应输出结果
                                paInfo3DTO.setMgr(rs.getString("Project_Manager_Name")); //获取对应输出结果
                                paInfo3DTO.setMgrNum(rs.getLong("Project_Manager_Id")); //获取对应输出结果
                                paInfo3DTO.setGmgrNum(rs.getString("Project_Gmanager_Id")); //获取对应输出结果
                                paInfo3DTO.setVmgrNum(rs.getString("Project_Vmanager_Id")); //获取对应输出结果
                                paInfo3DTO.setPubwbsID(rs.getLong("Pub_Ver_Id")); //获取对应输出结果
                                paInfo3DTO.setHeadOrbranch(rs.getString("Structure_Class")); //获取对应输出结果
                                paInfo3DTO.setDivision(rs.getString("Only_Spec")); //获取对应输出结果
                                paInfo3DTO.setPubwbsName(rs.getString("Str_Name")); //获取对应输出结果
                                paInfo3DTO.setOrgID(rs.getLong("Org_Id")); //获取对应输出结果
                                paInfo3DTO.setPerID(rs.getLong("Per_Id")); //获取对应输出结果
                                paInfo3DTO.setUserID(userId); //获取对应输出结果
                                paInfo3DTO.setUserName(rs.getString("User_Name")); //获取对应输出结果
                                paInfo3DTO.setUserNum(rs.getString("Employee_Number")); //获取对应输出结果
                                paInfo3DTO.setSpecialty(getProjSpec(projectId, phaseCode, userId));
                            }
                        rs.close();
                        }
                        paInfo3RepDTO.setData(paInfo3DTO);
                        return paInfo3RepDTO;
                    }
                });
        return ResultsDTO;
    }

    /**
     * 取项目的专业 专业负责人
     *
     * @param projectId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaInfo3SPECDTO> getProjSpec(long projectId, String phaseCode, long userId) {
        List<PaInfo3SPECDTO> ResultsDTO = (List<PaInfo3SPECDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_Spec_List(:1,:2,:3,:4)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setLong(2, userId);//
                        cs.setString(3, phaseCode);//
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaInfo3SPECDTO> tmpList = new ArrayList<PaInfo3SPECDTO>();
                        PaInfo3SPECDTO tmp;
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaInfo3SPECDTO();
                            String ids;
                            tmp.setId(rs.getString("Spec_Code")); //获取对应输出结果
                            tmp.setName(rs.getString("Spec_Name")); //获取对应输出结果
                            tmp.setEdit(rs.getString("Flag_Edit")); //获取对应输出结果
                            tmp.setLockBy(rs.getLong("Lock_By")); //获取对应输出结果
                            tmp.setLockName(rs.getString("Lock_Name")); //获取对应输出结果
                            ids = rs.getString("Person_Id"); //获取对应输出结果
                            if (ids != null && ids.indexOf(",") > 0) {
                                String a[] = ids.split(",");
                                tmp.setChgrNum(a);
                            } else {
                                String a[] = {ids};
                                tmp.setChgrNum(a);
                            }

                            tmpList.add(tmp);
                        }
                        rs.close();
                        return tmpList;
                    }
                });
        return ResultsDTO;
    }

    /**
     * 取有资格查看的所有专业信息
     *
     * @param projectId
     * @param userId
     * @param right
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<DeliverableSpecListDTO> getDeliverableSpecList(long projectId, long userId, String right) {
        List<DeliverableSpecListDTO> resultList = (List<DeliverableSpecListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_Spec_List(:1,:2,:3,:4,:5)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setLong(2, userId);// 设置输入项目ID
                        cs.setString(3, right);// 设置输入参数的值
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DeliverableSpecListDTO> results = new ArrayList<DeliverableSpecListDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        DeliverableSpecListDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new DeliverableSpecListDTO();
                            tmp.setIndustryCode(rs.getString("Industry_Code"));
                            tmp.setIndustryName(rs.getString("Industry_Name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 工作包类型列表
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<DeliverableClassDTO> getDeliverableClass() {
        List<DeliverableClassDTO> resultList = (List<DeliverableClassDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_Deliverable_Class(:1)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DeliverableClassDTO> results = new ArrayList<DeliverableClassDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);// 获取游标一行的值
                        DeliverableClassDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new DeliverableClassDTO();
                            tmp.setId(rs.getString("Class_Id"));
                            tmp.setType(rs.getString("Class_Name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 模糊查找 工作包列表
     *
     * @param queryTerm
     * @param drlvClass
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getDeliverableTypeList(String queryTerm, String drlvClass, long orgId, long pageNo, long pageSize,String spec,String phaseID) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<DeliverableTypeDTO> resultList = (List<DeliverableTypeDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_Deliverable_Type_List(:1,:2,:3,:4,:5,:6,:7,:8,:9)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, queryTerm);// 设置查询条件
                        cs.setLong(2, orgId);// 组织ID
                        cs.setString(3, drlvClass);// 设置工作包类别
                        cs.setLong(4, pageNo);// 设置输入参数的值
                        cs.setLong(5, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(6, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.setString(8, spec);
                        cs.setString(9, phaseID);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DeliverableTypeDTO> results = new ArrayList<DeliverableTypeDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        dt.setTotal(cs.getLong(7));
                        DeliverableTypeDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new DeliverableTypeDTO();
                            tmp.setClassName(rs.getString("Class_Name"));
                            tmp.setDlvrCode(rs.getString("Dlvr_Code"));
                            tmp.setDlvrName(rs.getString("Dlvr_Name"));
                            tmp.setDlvrID(rs.getLong("Dlvr_Id"));
                            tmp.setClassID(rs.getLong("Class_Id"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setRows(resultList);
        return dt;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getDeliverableTypeListForTaskType(String queryTerm, String drlvClass, long orgId, long pageNo, long pageSize,String spec,String phaseID, String taskTypeCode) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<DeliverableTypeDTO> resultList = (List<DeliverableTypeDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.nbcc_get_deliverable_type_list(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, queryTerm);// 设置查询条件
                        cs.setLong(2, orgId);// 组织ID
                        cs.setString(3, drlvClass);// 设置工作包类别
                        cs.setLong(4, pageNo);// 设置输入参数的值
                        cs.setLong(5, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(6, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.setString(8, spec);
                        cs.setString(9, phaseID);
                        cs.setString(10, taskTypeCode);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DeliverableTypeDTO> results = new ArrayList<DeliverableTypeDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        dt.setTotal(cs.getLong(7));
                        DeliverableTypeDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new DeliverableTypeDTO();
                            tmp.setClassName(rs.getString("Class_Name"));
                            tmp.setDlvrCode(rs.getString("Dlvr_Code"));
                            tmp.setDlvrName(rs.getString("Dlvr_Name"));
                            tmp.setDlvrID(rs.getLong("Dlvr_Id"));
                            tmp.setClassID(rs.getLong("Class_Id"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setRows(resultList);
        return dt;
    }

    /**
     * 条件单使用 --取所有专业信息
     *
     * @param projectId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaIndustriesAllDTO> getDeliverableSpecList(long projectId) {
        List<PaIndustriesAllDTO> resultList = (List<PaIndustriesAllDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_Spec_List_All(:1,:2)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaIndustriesAllDTO> results = new ArrayList<PaIndustriesAllDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PaIndustriesAllDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaIndustriesAllDTO();
                            tmp.setCode(rs.getString("Disp_Code"));
                            tmp.setIndustryCode(rs.getString("Industry_Code"));
                            tmp.setName(rs.getString("Industry_Name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 取所有设校审人员
     *
     * @param projectId
     * @param spec
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PAAssignmentDTO> getAssignmentListAll(long projectId, String spec) {
        List<PAAssignmentDTO> resultList = (List<PAAssignmentDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_Assignment_List_All(:1,:2,:3)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, spec);// 设置输入项目ID
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PAAssignmentDTO> results = new ArrayList<PAAssignmentDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        PAAssignmentDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PAAssignmentDTO();
                            tmp.setPerId(rs.getLong("Perid"));
                            tmp.setPerName(rs.getString("Pname"));
                            tmp.setPerNum(rs.getString("Perno"));
                            tmp.setSpecialty(rs.getString("Spec"));
                            tmp.setDuty(rs.getString("Duty_Name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }
    /**
     * 取所有专业负责人
     *
     * @param projectId
     * @param spec
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PAAssignmentDTO> getProfessionListAll(long projectId, String spec) {
        List<PAAssignmentDTO> resultList = (List<PAAssignmentDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_Profession_List_All(:1,:2,:3)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, spec);// 设置输入项目ID
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PAAssignmentDTO> results = new ArrayList<PAAssignmentDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        PAAssignmentDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PAAssignmentDTO();
                            tmp.setPerId(rs.getLong("Perid"));
                            tmp.setPerName(rs.getString("Pname"));
                            tmp.setPerNum(rs.getString("Perno"));
                            tmp.setSpecialty(rs.getString("Spec"));
                            tmp.setDuty(rs.getString("Duty_Name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }
    /**
     * 取所有分管领导项目经理项目秘书
     *
     * @param projectId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PAAssignmentDTO> getMemberList(long projectId) {
        List<PAAssignmentDTO> resultList = (List<PAAssignmentDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Utils.Get_Members_List(:1,:2)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PAAssignmentDTO> results = new ArrayList<PAAssignmentDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        PAAssignmentDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PAAssignmentDTO();
                            tmp.setPerId(rs.getLong("Perid"));
                            tmp.setPerName(rs.getString("Pname"));
                            tmp.setPerNum(rs.getString("Perno"));
                            tmp.setDuty(rs.getString("Duty_Name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }
    /**
     * 取最新发布版本的所有工作包
     *
     * @param projectId
     * @param phaseCode
     * @param StructureVerId 正在编辑结构版本ID
     * @param spec           专业代字 如2AR
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaPublishDeliverablesDTO> getPublishDeliverables(long projectId, String phaseCode, long StructureVerId, String spec, long userId) {
        List<PaPublishDeliverablesDTO> resultList = (List<PaPublishDeliverablesDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_Deliverables(:1,:2,:3,:4,:5,:6,:7,:8)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入项目ID
                        cs.setString(2, phaseCode);//
                        cs.setLong(3, StructureVerId);// 设置输入项目ID
                        cs.setString(4, spec);// 设置专业
                        cs.setLong(5, userId);// 设置专业
                        cs.registerOutParameter(6, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.VARCHAR);
                        cs.registerOutParameter(8, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaPublishDeliverablesDTO> results = new ArrayList<PaPublishDeliverablesDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);// 获取游标一行的值
                        String tmpStr;
                        PaPublishDeliverablesDTO tmp = null;
                        String lev;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmpStr = new String();
                            lev = new String();
                            tmp = new PaPublishDeliverablesDTO();
                            tmp.setSpecId(rs.getLong("Spec_Id"));
                            tmp.setParentId(rs.getLong("Spec_Ver_Id"));
                            tmp.setId(rs.getLong("Dlvr_Id"));
                            tmp.setName(rs.getString("Dlvr_Name"));
                            tmp.setCode(rs.getString("Dlvr_Num"));
                            tmp.setTaskTypeId(rs.getLong("Task_Type_Id"));
                            tmp.setEndDate(rs.getDate("Due_Date"));
                            tmp.setStartDate(rs.getDate("Start_Date"));
                            tmp.setDesignNum(rs.getString("Design_Id"));
                            tmp.setDesignName(rs.getString("Design_Name"));
                            tmp.setCheckNum(rs.getString("Check_Id"));
                            tmp.setCheckName(rs.getString("Check_Name"));
                            tmp.setReviewNum(rs.getString("Verify_Id"));
                            tmp.setReviewName(rs.getString("Verify_Name"));
                            tmp.setApproveNum(rs.getString("Approve_Id"));
                            tmp.setApproveName(rs.getString("Approve_Name"));
                            tmp.setCertifiedNum(rs.getString("Reg_Engineer_Id"));
                            tmp.setCertifiedName(rs.getString("Reg_Engineer_Name"));
                            tmp.setWorkHour(rs.getDouble("Workhour"));
                            tmp.setMajorCode(rs.getString("Major_Code"));
                            tmp.setGrandNum(rs.getString("Grandnum"));
                            tmp.setWorkgrandNum(rs.getString("Workgrandnum"));
                            tmp.setWorkCode(rs.getString("WorkCode"));
                            tmp.setWorktypeID(rs.getString("WorkClass"));
                            tmp.setAuthorNum(rs.getString("Create_Id"));
                            tmp.setAuthorName(rs.getString("Create_Name"));
                            tmp.setDescription(rs.getString("Description"));
                            tmp.setStatusCode(rs.getString("Status_Code"));
                            tmp.setStatus(rs.getString("Status_Name"));
                            tmp.setAmountTrial(rs.getString("Attribute1"));
                            tmp.setAmountAppr(rs.getString("Attribute2"));
                            tmp.setSchemeNum(rs.getString("Attribute3"));
                            tmp.setSchemeName(rs.getString("Scheme_Name"));
                            tmp.setGrandName(rs.getString("Attribute5"));
                            tmp.setMatCode(rs.getString("Attribute6"));
                            tmp.setMatName(rs.getString("Attribute7"));
                            tmp.setSrcID(rs.getString("Attribute8"));
                            tmp.setSrcRow(rs.getString("Attribute9"));
                            tmp.setSrcClass(rs.getString("Attribute10"));
                            tmp.setSrc(rs.getString("Name"));
                            tmp.setAuthorClass(rs.getString("Attribute11"));
                            tmp.setDwgNum(rs.getString("Dwgnum"));
                            tmp.setBudget(rs.getString("Attribute13"));
                            tmp.setDlvrExtId(rs.getLong("Dlvr_Ext_Id"));
                            tmp.setDesignId(rs.getString("Design_Num"));
                            tmp.setCheckId(rs.getString("Check_Num"));
                            tmp.setReviewId(rs.getString("Verify_Num"));
                            tmp.setApproveId(rs.getString("Approve_Num"));
                            tmp.setCertifiedId(rs.getString("Reg_Engineer_Num"));
                            tmp.setSchemeId(rs.getString("Scheme_Num"));
                            tmp.setAuthorId(rs.getString("Create_Num"));
                            tmp.setRecvSpec(rs.getString("Receive_Specialty"));
                            tmp.setDefaultFlag(rs.getString("Default_Flag"));
                            tmp.setSchedule(rs.getString("Schedule"));
                            tmpStr = rs.getString("Attribute4");
                            lev = getLevel((int) rs.getLong("Task_Type_Id"));
                            if (tmpStr != null && tmpStr.length() > 0) {
                                tmp.setOtherslist(getPaOtherlist(tmpStr));
                            } else {
                                PaOthersList otherslist = new PaOthersList();
                                otherslist.setOthersCount("无");
                                tmp.setOtherslist(otherslist);
                            }
                            tmp.setLevel(lev);
                            if (lev == "work") {
                                tmp.setIconCls("icon-" + lev);
                            } else {
                                tmp.setIconCls("icon-" + lev + "br");
                            }
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });

        return resultList;
    }

    /**
     * 取历史项目各版
     *
     * @param queryTerm
     * @param pClass
     * @param tgId
     * @param userId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO queryHistoryProjectList(String queryTerm, String pClass, long tgId, long userId, long pageNo, long pageSize) {
        DataTablesDTO dataDTO = new DataTablesDTO();
        List<PaHistoryStructureDTO> resultList = (List<PaHistoryStructureDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_History_Projcet_List(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, queryTerm);// 设置查询条件
                        cs.setString(2, pClass);// 设置行业类别
                        cs.setLong(3, userId);// 设置行业类别
                        cs.setLong(4, pageNo);// 设置输入参数的值
                        cs.setLong(5, pageSize);// 设置输入参数的值
                        cs.setLong(6, tgId);// 设置输入参数的值
                        cs.registerOutParameter(7, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(8, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(9, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(10, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaHistoryStructureDTO> results = new ArrayList<PaHistoryStructureDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(7);// 获取游标一行的值
                        dataDTO.setTotal(cs.getLong(8));
                        PaHistoryStructureDTO tmp = null;
                        List<PaPhaseListDTO> phaseListDTOs = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new PaHistoryStructureDTO();
                            phaseListDTOs = new ArrayList<PaPhaseListDTO>();
                            //tmp.setSessionId(rs.getLong("SESSION_ID"));
                            tmp.setId(rs.getLong("LINEID"));
                            tmp.setName(rs.getString("LINENAME"));
                            tmp.setTime(rs.getDate("LINEDATE"));
                            tmp.setOperator(rs.getString("PERSONNAME"));
                            //tmp.setProjID(rs.getLong("PROJECTID"));
                            tmp.setStructureId(rs.getLong("STRUCTUREID"));
                            tmp.setStrClass(rs.getString("STRCLASS"));
                            tmp.setPhasecode(rs.getString("Phasecode"));
                            tmp.setParentId(rs.getLong("PARENTID"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        //dt.setRows(resultList);
        List<TreePaHistoryStructureDTO> treePaHistoryStructureDTOs = new ArrayList<TreePaHistoryStructureDTO>();
        treePaHistoryStructureDTOs = paLevTwoPlanService.treePaHistoryList(resultList);
        //paHistoryStructureListDTO.setRows(treePaHistoryStructureDTOs);
        dataDTO.setRows(treePaHistoryStructureDTOs);
        return dataDTO;
    }

    /**
     * 取历史项目WBS工作包
     *
     * @param structureId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaHistoryWbsDeliverablesDTO> getHisWbsDeliverables(long structureId, String spec) {
        List<PaHistoryWbsDeliverablesDTO> resultList = (List<PaHistoryWbsDeliverablesDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_History_Wbs_Deliverables(:1,:2,:3,:4,:5)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);

                        cs.setLong(1, structureId);// 设置输入项目ID
                        cs.setString(2, spec);//
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaHistoryWbsDeliverablesDTO> results = new ArrayList<PaHistoryWbsDeliverablesDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        PaHistoryWbsDeliverablesDTO tmp = null;
                        String lev;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            lev = new String();
                            tmp = new PaHistoryWbsDeliverablesDTO();
                            tmp.setParentId(rs.getLong("Parent_Id"));
                            tmp.setId(rs.getLong("Id"));
                            tmp.setCode(rs.getString("Num"));
                            tmp.setDlvrName(rs.getString("NAME"));
                            tmp.setEndDate(rs.getDate("Due_Date"));
                            tmp.setStartDate(rs.getDate("Start_Date"));
                            tmp.setWorkHour(rs.getDouble("Workhour"));
                            tmp.setGrandNum(rs.getString("Grandnum"));
                            tmp.setWorktypeID(rs.getString("Workclass"));
                            tmp.setWorkCode(rs.getString("Workcode"));
                            tmp.setDescription(rs.getString("Description"));
                            tmp.setGrandName(rs.getString("Grandname"));
                            tmp.setMatCode(rs.getString("Matcode"));
                            tmp.setMatName(rs.getString("Matname"));
                            tmp.setStatus(rs.getString("Status_Name"));
                            tmp.setDefaultFlag(rs.getString("Default_Flag"));
                            lev = getLevel((int) (rs.getLong("Lev")));
                            tmp.setLevel(lev);
                            if (lev == "work") {
                                tmp.setIconCls("icon-" + lev);
                            } else {
                                tmp.setIconCls("icon-" + lev + "br");
                            }
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 取历史项目PBS工作包
     *
     * @param structureId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PaHistoryPbsDeliverablesDTO> getHisPbsDeliverables(long structureId, String spec, long orgId) {
        List<PaHistoryPbsDeliverablesDTO> resultList = (List<PaHistoryPbsDeliverablesDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Get_History_Pbs_Deliverables(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, structureId);// 设置输入项目ID
                        cs.setString(2, spec);//
                        cs.setLong(3, orgId);//
                        cs.registerOutParameter(4, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PaHistoryPbsDeliverablesDTO> results = new ArrayList<PaHistoryPbsDeliverablesDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                        PaHistoryPbsDeliverablesDTO tmp = null;
                        String lev;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            lev = new String();
                            tmp = new PaHistoryPbsDeliverablesDTO();
                            tmp.setParentId(rs.getLong("Parent_Id"));
                            tmp.setId(rs.getLong("Id"));
                            tmp.setCode(rs.getString("Code"));
                            tmp.setDlvrName(rs.getString("NAME"));
                            tmp.setWorkHour(rs.getDouble("Workhour"));
                            tmp.setGrandNum(rs.getString("Grandnum"));
                            tmp.setWorktypeID(rs.getString("Workclass"));
                            tmp.setWorkCode(rs.getString("Workcode"));
                            tmp.setMatCode(rs.getString("Matcode"));
                            tmp.setMatName(rs.getString("Matname"));
                            lev = getLevel((int) (rs.getLong("Lev")));
                            tmp.setLevel(lev);
                            if (lev == "work") {
                                tmp.setIconCls("icon-" + lev);
                            } else {
                                tmp.setIconCls("icon-" + lev + "br");
                            }
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    /**
     * 其他人员 规则  （张三,101,3;李四,102,5）
     *
     * @param otherlist
     * @return
     */
    public PaOthersList getPaOtherlist(String otherlist) {
        PaOthersList paOthersList = new PaOthersList();
        List<PaOthers> paOther = new ArrayList<PaOthers>();
        PaOthers paOthers;
        String a[] = otherlist.split(";");
        for (int i = 0; i < a.length; i++) {
            paOthers = new PaOthers();
            String b[] = a[i].split(",");
            paOthers.setOtherName(b[0]);
            paOthers.setOtherId(getUserNO(Long.parseLong(b[1])));//Long.parseLong(b[1])
            paOthers.setOtherRatio(Float.parseFloat(b[2]));
            paOthers.setOtherNum(b[1]);
            paOther.add(paOthers);
        }
        String othersCount;
        if (a.length > 1) {
            othersCount = a.length + "人";
        } else {
            othersCount = "无";
        }
        paOthersList.setPaOthers(paOther);
        paOthersList.setOthersCount(othersCount);
        return paOthersList;
    }

    /**
     * 其他人员 规则  （张三,101,3;李四,102,5）
     *
     * @param paOthersList
     * @return
     */
    public String setPaOtherlist(PaOthersList paOthersList) {
        List<PaOthers> tmp = paOthersList.getPaOthers();
        PaOthers paOthers;
        String a = new String();
        String b;
        for (int i = 0; i < tmp.size(); i++) {
            b = new String();
            paOthers = new PaOthers();
            paOthers = tmp.get(i);
            paOthers.getOtherName();
            paOthers.getOtherId();
            paOthers.getOtherRatio();
            b = paOthers.getOtherName() + ',' + paOthers.getOtherId() + ',' + paOthers.getOtherRatio();
            a = a + ';' + b;
        }
        a.substring(1, a.length() - 1);
        return a;
    }

    public List<TreePaPubDeliverableDTO> treePublishDeliverableList(List<PaPublishDeliverablesDTO> paPublishDeliverablesDTOs, String level) {
        List<TreePaPubDeliverableDTO> treePaPubDeliverableDTOs = new ArrayList<TreePaPubDeliverableDTO>();
        TreePaPubDeliverableDTO treePaPubDeliverableDTO = new TreePaPubDeliverableDTO();
        Long parentId;
        for (int i = 0; i < paPublishDeliverablesDTOs.size(); i++) {
            Long id = paPublishDeliverablesDTOs.get(i).getId();
            Long pId = paPublishDeliverablesDTOs.get(i).getParentId();
            if (id.equals(pId)) {
                try {
                    PropertyUtils.copyProperties(treePaPubDeliverableDTO, paPublishDeliverablesDTOs.get(i));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                parentId = id;
                treePaPubDeliverableDTO = findChild(treePaPubDeliverableDTO, paPublishDeliverablesDTOs, parentId, level);
                treePaPubDeliverableDTOs.add(treePaPubDeliverableDTO);
            }

        }
        return treePaPubDeliverableDTOs;
    }

    private TreePaPubDeliverableDTO findChild(TreePaPubDeliverableDTO treePaPubDeliverableDTO, List<PaPublishDeliverablesDTO> paPublishDeliverablesDTOs, Long parentId, String level) {
        TreePaPubDeliverableDTO tmpData = new TreePaPubDeliverableDTO();
        //TreePaPubDeliverableDTO tmpData1;
        List<TreePaPubDeliverableDTO> tmpList = new ArrayList<TreePaPubDeliverableDTO>();
        Long nextParent;
        for (int i = 0; i < paPublishDeliverablesDTOs.size(); i++) {
            if (parentId.equals(paPublishDeliverablesDTOs.get(i).getParentId()) && !parentId.equals(paPublishDeliverablesDTOs.get(i).getId())) {
                tmpData = new TreePaPubDeliverableDTO();
                nextParent = paPublishDeliverablesDTOs.get(i).getId();
                if ((paPublishDeliverablesDTOs.get(i).getLevel().equals("sys") && level.equals("SUB"))
                        || ((paPublishDeliverablesDTOs.get(i).getLevel().equals("sys") || paPublishDeliverablesDTOs.get(i).getLevel().equals("div"))
                        && level.equals("SPEC"))
                        ) {
                    if (paPublishDeliverablesDTOs.get(i).getDefaultFlag() != null && paPublishDeliverablesDTOs.get(i).getDefaultFlag().equals("default")) {
                        TreePaPubDeliverableDTO tmp = findChild(treePaPubDeliverableDTO, paPublishDeliverablesDTOs, nextParent, level);
                         return tmp;
                    }
                } else {
                    try {
                        PropertyUtils.copyProperties(tmpData, paPublishDeliverablesDTOs.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    if (tmpData.getLevel().equals("work")) {
                        tmpList = treePaPubDeliverableDTO.getSpeChildren();
                        tmpList.add(findChild(tmpData, paPublishDeliverablesDTOs, nextParent, level));
                        treePaPubDeliverableDTO.setSpeChildren(tmpList);
                    } else {
                        if (treePaPubDeliverableDTO.getId() != null) {
                            tmpList = treePaPubDeliverableDTO.getChildren();
                            tmpList.add(findChild(tmpData, paPublishDeliverablesDTOs, nextParent, level));
                            treePaPubDeliverableDTO.setChildren(tmpList);
                        } else {
                            return tmpData;
                        }
                    }
                }
            }
        }

        return treePaPubDeliverableDTO;
    }

    public List<TreePaHistoryWbsDeliverablesDTO> treeHistoryWbsDeliverableList(List<PaHistoryWbsDeliverablesDTO> paHistoryWbsDeliverablesDTOs, String level) {
        List<TreePaHistoryWbsDeliverablesDTO> treePaHistoryWbsDeliverablesDTOs = new ArrayList<TreePaHistoryWbsDeliverablesDTO>();
        TreePaHistoryWbsDeliverablesDTO treePaHistoryWbsDeliverablesDTO = new TreePaHistoryWbsDeliverablesDTO();
        Long parentId;
        for (int i = 0; i < paHistoryWbsDeliverablesDTOs.size(); i++) {
            Long id = paHistoryWbsDeliverablesDTOs.get(i).getId();
            Long pId = paHistoryWbsDeliverablesDTOs.get(i).getParentId();
            if (id.equals(pId)) {
                try {
                    PropertyUtils.copyProperties(treePaHistoryWbsDeliverablesDTO, paHistoryWbsDeliverablesDTOs.get(i));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                parentId = id;
                treePaHistoryWbsDeliverablesDTO = findChild(treePaHistoryWbsDeliverablesDTO, paHistoryWbsDeliverablesDTOs, parentId, level);
                treePaHistoryWbsDeliverablesDTOs.add(treePaHistoryWbsDeliverablesDTO);
            }

        }
        return treePaHistoryWbsDeliverablesDTOs;
    }

    private TreePaHistoryWbsDeliverablesDTO findChild(TreePaHistoryWbsDeliverablesDTO treePaHistoryWbsDeliverablesDTO, List<PaHistoryWbsDeliverablesDTO> paHistoryWbsDeliverablesDTOs, Long parentId, String level) {
        TreePaHistoryWbsDeliverablesDTO tmpData = new TreePaHistoryWbsDeliverablesDTO();
        List<TreePaHistoryWbsDeliverablesDTO> tmpList = new ArrayList<TreePaHistoryWbsDeliverablesDTO>();
        tmpList = treePaHistoryWbsDeliverablesDTO.getChildren();
        Long nextParent;
        for (int i = 0; i < paHistoryWbsDeliverablesDTOs.size(); i++) {
            if (parentId.equals(paHistoryWbsDeliverablesDTOs.get(i).getParentId()) && !parentId.equals(paHistoryWbsDeliverablesDTOs.get(i).getId())) {
                tmpData = new TreePaHistoryWbsDeliverablesDTO();
                nextParent = paHistoryWbsDeliverablesDTOs.get(i).getId();
                if ((paHistoryWbsDeliverablesDTOs.get(i).getLevel().equals("sys") && level.equals("SUB"))
                        || ((paHistoryWbsDeliverablesDTOs.get(i).getLevel().equals("sys") || paHistoryWbsDeliverablesDTOs.get(i).getLevel().equals("div"))
                        && level.equals("SPEC"))
                        ) {
                    if (paHistoryWbsDeliverablesDTOs.get(i).getDefaultFlag() != null && paHistoryWbsDeliverablesDTOs.get(i).getDefaultFlag().equals("default")) {
                        TreePaHistoryWbsDeliverablesDTO tmp = findChild(treePaHistoryWbsDeliverablesDTO, paHistoryWbsDeliverablesDTOs, nextParent, level);
                        return tmp;
                    }
                } else {
                    try {
                        PropertyUtils.copyProperties(tmpData, paHistoryWbsDeliverablesDTOs.get(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                    //tmpList.add(findChild(tmpData, paHistoryWbsDeliverablesDTOs, nextParent));
                    //treePaHistoryWbsDeliverablesDTO.setChildren(tmpList);
                    if (tmpData.getLevel().equals("work")) {
                        tmpList = treePaHistoryWbsDeliverablesDTO.getSpeChildren();
                        tmpList.add(findChild(tmpData, paHistoryWbsDeliverablesDTOs, nextParent, level));
                        treePaHistoryWbsDeliverablesDTO.setSpeChildren(tmpList);
                    } else {
                        if (treePaHistoryWbsDeliverablesDTO.getId() != null) {
                            tmpList = treePaHistoryWbsDeliverablesDTO.getChildren();
                            tmpList.add(findChild(tmpData, paHistoryWbsDeliverablesDTOs, nextParent, level));
                            treePaHistoryWbsDeliverablesDTO.setChildren(tmpList);
                        } else {
                            return tmpData;
                        }
                    }

                }
            }
        }
        return treePaHistoryWbsDeliverablesDTO;
    }

    public List<TreePaHistoryPbsDeliverablesDTO> treeHistoryPbsDeliverableList(List<PaHistoryPbsDeliverablesDTO> paHistoryPbsDeliverablesDTOs) {
        List<TreePaHistoryPbsDeliverablesDTO> treePaHistoryPbsDeliverablesDTOs = new ArrayList<TreePaHistoryPbsDeliverablesDTO>();
        TreePaHistoryPbsDeliverablesDTO treePaHistoryPbsDeliverablesDTO = new TreePaHistoryPbsDeliverablesDTO();
        Long parentId;
        for (int i = 0; i < paHistoryPbsDeliverablesDTOs.size(); i++) {
            Long id = paHistoryPbsDeliverablesDTOs.get(i).getId();
            Long pId = paHistoryPbsDeliverablesDTOs.get(i).getParentId();
            if (id.equals(pId)) {
                try {
                    PropertyUtils.copyProperties(treePaHistoryPbsDeliverablesDTO, paHistoryPbsDeliverablesDTOs.get(i));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                parentId = id;
                treePaHistoryPbsDeliverablesDTO = findChild(treePaHistoryPbsDeliverablesDTO, paHistoryPbsDeliverablesDTOs, parentId);
                treePaHistoryPbsDeliverablesDTOs.add(treePaHistoryPbsDeliverablesDTO);
            }

        }
        return treePaHistoryPbsDeliverablesDTOs;
    }

    private TreePaHistoryPbsDeliverablesDTO findChild(TreePaHistoryPbsDeliverablesDTO treePaHistoryPbsDeliverablesDTO, List<PaHistoryPbsDeliverablesDTO> paHistoryPbsDeliverablesDTOs, Long parentId) {
        TreePaHistoryPbsDeliverablesDTO tmpData = new TreePaHistoryPbsDeliverablesDTO();
        List<TreePaHistoryPbsDeliverablesDTO> tmpList = new ArrayList<TreePaHistoryPbsDeliverablesDTO>();
        tmpList = treePaHistoryPbsDeliverablesDTO.getChildren();
        Long nextParent;
        for (int i = 0; i < paHistoryPbsDeliverablesDTOs.size(); i++) {
            if (parentId.equals(paHistoryPbsDeliverablesDTOs.get(i).getParentId()) && !parentId.equals(paHistoryPbsDeliverablesDTOs.get(i).getId())) {
                tmpData = new TreePaHistoryPbsDeliverablesDTO();
                try {
                    PropertyUtils.copyProperties(tmpData, paHistoryPbsDeliverablesDTOs.get(i));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                nextParent = paHistoryPbsDeliverablesDTOs.get(i).getId();
                //tmpList.add(findChild(tmpData, paHistoryPbsDeliverablesDTOs, nextParent));
                //treePaHistoryPbsDeliverablesDTO.setChildren(tmpList);
                if (tmpData.getLevel().equals("work")) {
                    tmpList = treePaHistoryPbsDeliverablesDTO.getSpeChildren();
                    tmpList.add(findChild(tmpData, paHistoryPbsDeliverablesDTOs, nextParent));
                    treePaHistoryPbsDeliverablesDTO.setSpeChildren(tmpList);
                } else {
                    tmpList = treePaHistoryPbsDeliverablesDTO.getChildren();
                    tmpList.add(findChild(tmpData, paHistoryPbsDeliverablesDTOs, nextParent));
                    treePaHistoryPbsDeliverablesDTO.setChildren(tmpList);
                }
            }
        }
        return treePaHistoryPbsDeliverablesDTO;
    }

    private PaDeliverableEntity dlvrDtoToEntity(PaPublishDeliverablesDTO paPublishDeliverablesDTO) {
        PaDeliverableEntity entity = new PaDeliverableEntity();
        try {
            PropertyUtils.copyProperties(entity, paPublishDeliverablesDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private LockSpecEntity specDtoToEntity(LockSpecDTO lockSpecDTO) {
        LockSpecEntity entity = new LockSpecEntity();
        try {
            PropertyUtils.copyProperties(entity, lockSpecDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private List<PaDeliverableEntity> initDlvrEntitys(List<PaPublishDeliverablesDTO> paPublishDeliverablesDTOs, Long projectId, Long userId, String Operate) {
        List<PaDeliverableEntity> dataList = new ArrayList<PaDeliverableEntity>();
        for (int i = 0; i < paPublishDeliverablesDTOs.size(); i++) {
            PaDeliverableEntity paDeliverableEntity = new PaDeliverableEntity();
            paDeliverableEntity = this.dlvrDtoToEntity(paPublishDeliverablesDTOs.get(i));
            paDeliverableEntity.setProjectId(projectId);
            paDeliverableEntity.setUserId(userId);
            paDeliverableEntity.setOperate(Operate);
            dataList.add(paDeliverableEntity);
            if (Operate == "INSERT") {
                paDeliverableEntity.setId(0);
            }
        }
        return dataList;
    }

    /**
     * 保存工作包
     *
     * @param dlvrSaveDTO
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map saveDlvr(DlvrSaveDTO dlvrSaveDTO, Long userId) {
        Long projectId = dlvrSaveDTO.getProjectId();
        PaPublishDeliverablesDTO paPublishDeliverablesDTO = new PaPublishDeliverablesDTO();
        List<PaDeliverableEntity> dataList = new ArrayList<PaDeliverableEntity>();
        if (dlvrSaveDTO.getAddRows().size() > 0) {
            dataList.addAll(initDlvrEntitys(dlvrSaveDTO.getAddRows(), projectId, userId, "INSERT"));
        }
        if (dlvrSaveDTO.getUpdateRows().size() > 0) {
            dataList.addAll(initDlvrEntitys(dlvrSaveDTO.getUpdateRows(), projectId, userId, "UPDATE"));
        }
        if (dlvrSaveDTO.getDeleteRows().size() > 0) {
            dataList.addAll(initDlvrEntitys(dlvrSaveDTO.getDeleteRows(), projectId, userId, "DELETE"));
        }
        DlvrResponseDTO dlvrResponseDTO = new DlvrResponseDTO();
        Map map1 = new HashMap();
        if (dataList.size() < 1) {
            map1.put("saveWork", "none");
            return map1;
        }
        ;
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Save_Deliverables(:1,:2,:3)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.WBSP_PA_DLVR_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        // cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        PaPublishDeliverablesDTO dlvrdto;
                        List<PaPublishDeliverablesDTO> results = new ArrayList<PaPublishDeliverablesDTO>();
                        cs.execute();
                        String tmpStr;
                        if (cs.getString(2).equals("S")) {
                            tmp.put("saveWork", "done");
                        } else {
                            tmp.put("saveWork", cs.getString(3));
                        }
/*                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            dlvrdto = new PaPublishDeliverablesDTO();
                            dlvrdto.setParentId(rs.getLong("Spec_Ver_Id"));
                            tmpStr = new String();
                            dlvrdto = new PaPublishDeliverablesDTO();
                            dlvrdto.setSpecId(rs.getLong("Spec_Id"));
                            dlvrdto.setParentId(rs.getLong("Spec_Ver_Id"));
                            dlvrdto.setId(rs.getLong("Dlvr_Id"));
                            dlvrdto.setName(rs.getString("Dlvr_Name"));
                            dlvrdto.setCode(rs.getString("Dlvr_Num"));
                            dlvrdto.setTaskTypeId(rs.getLong("Task_Type_Id"));
                            dlvrdto.setEndDate(rs.getDate("Due_Date"));
                            dlvrdto.setStartDate(rs.getDate("Start_Date"));
                            dlvrdto.setDesignNum(rs.getString("Design_Id"));
                            dlvrdto.setDesignName(rs.getString("Design_Name"));
                            dlvrdto.setCheckNum(rs.getString("Check_Id"));
                            dlvrdto.setCheckName(rs.getString("Check_Name"));
                            dlvrdto.setReviewNum(rs.getString("Verify_Id"));
                            dlvrdto.setReviewName(rs.getString("Verify_Name"));
                            dlvrdto.setApproveNum(rs.getString("Approve_Id"));
                            dlvrdto.setApproveName(rs.getString("Approve_Name"));
                            dlvrdto.setCertifiedNum(rs.getString("Reg_Engineer_Id"));
                            dlvrdto.setCertifiedName(rs.getString("Reg_Engineer_Name"));
                            dlvrdto.setWorkHour(rs.getString("Workhour"));
                            dlvrdto.setMajorCode(rs.getString("Major_Code"));
                            dlvrdto.setGrandNum(rs.getString("Grandnum"));
                            dlvrdto.setWorkgrandNum(rs.getString("Workgrandnum"));
                            dlvrdto.setWorkCode(rs.getString("Worktype"));
                            dlvrdto.setAuthorNum(rs.getString("Create_Num"));
                            dlvrdto.setAuthorName(rs.getString("Create_Name"));
                            dlvrdto.setDescription(rs.getString("Description"));
                            dlvrdto.setStatusCode(rs.getString("Status_Code"));
                            dlvrdto.setStatus(rs.getString("Status_Name"));
                            dlvrdto.setAmountTrial(rs.getString("Attribute1"));
                            dlvrdto.setAmountAppr(rs.getString("Attribute2"));
                            dlvrdto.setSchemeNum(rs.getString("Attribute3"));
                            dlvrdto.setSchemeName(rs.getString("Scheme_Name"));
                            dlvrdto.setGrandName(rs.getString("Attribute5"));
                            dlvrdto.setMatCode(rs.getString("Attribute6"));
                            dlvrdto.setMatName(rs.getString("Attribute7"));
                            dlvrdto.setSrcID(rs.getString("Attribute8"));
                            dlvrdto.setSrcRow(rs.getString("Attribute9"));
                            dlvrdto.setSrcClass(rs.getString("Attribute10"));
                            dlvrdto.setSrc(rs.getString("Name"));
                            dlvrdto.setAuthorClass(rs.getString("Attribute11"));
                            dlvrdto.setDwgNum(rs.getString("Dwgnum"));
                            dlvrdto.setBudget(rs.getString("Attribute13"));
                            dlvrdto.setDlvrExtId(rs.getLong("Dlvr_Ext_Id"));
                            tmpStr = rs.getString("Attribute4");
                            if (tmpStr != null && tmpStr.length() > 0) {
                                dlvrdto.setOtherslist(getPaOtherlist(tmpStr));
                            }
                            results.add(dlvrdto);
                        }
                        rs.close();*/
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 生成图纸状态变更列表
     *
     * @param userId
     * @param dlvrSaveDTO
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public DwgStatusChangeResponseDTO saveChange(Long userId, DlvrSaveDTO dlvrSaveDTO) {
        Long projectId = dlvrSaveDTO.getProjectId();
        String phaseCode = dlvrSaveDTO.getPhaseCode();
        DwgStatusChangeResponseDTO results = new DwgStatusChangeResponseDTO();
        PaPublishDeliverablesDTO paPublishDeliverablesDTO = new PaPublishDeliverablesDTO();
        List<PaDeliverableEntity> dataList = new ArrayList<PaDeliverableEntity>();
        if (dlvrSaveDTO.getAddRows().size() > 0) {
            dataList.addAll(initDlvrEntitys(dlvrSaveDTO.getAddRows(), projectId, userId, "INSERT"));
        }
        if (dlvrSaveDTO.getUpdateRows().size() > 0) {
            dataList.addAll(initDlvrEntitys(dlvrSaveDTO.getAddRows(), projectId, userId, "UPDATE"));
        }
        if (dlvrSaveDTO.getDeleteRows().size() > 0) {
            dataList.addAll(initDlvrEntitys(dlvrSaveDTO.getAddRows(), projectId, userId, "DELETE"));
        }
        DlvrResponseDTO dlvrResponseDTO = new DlvrResponseDTO();
        if (dataList.size() < 1) {
            results.setStatus("NULL");
            return results;
        }
        ;
        results = (DwgStatusChangeResponseDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Save_Change(:1,:2,:3,:4,:5,:6)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.Wbsp_Pa_Deliverables_Tbl", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setLong(1, projectId);
                        cs.setLong(2, userId);
                        cs.setString(3, phaseCode);
                        cs.setArray(4, vArray);
                        cs.registerOutParameter(5, OracleTypes.CURSOR);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        DwgStatusChangeDTO tmp;
                        List<DwgStatusChangeDTO> dwgStatusChangeDTOs = new ArrayList<DwgStatusChangeDTO>();
                        DwgStatusChangeResponseDTO resultsDTO = new DwgStatusChangeResponseDTO();
                        cs.execute();
                        resultsDTO.setStatus(cs.getString(6));
                        if (!cs.getString(6).equals("S")) {
                            return resultsDTO;
                        }
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new DwgStatusChangeDTO();
                            tmp.setWorkId(rs.getLong("Dlvr_Id"));//工作包号
                            tmp.setDivisionNum(rs.getString("Sub_Num"));//工作包所属子项号变更描述
                            tmp.setDivisionName(rs.getString("Sub_Name"));//工作包所属子项名称变更描述
                            tmp.setSpecialty(rs.getString("Spec_Name")); //工作包所属专业
                            tmp.setWorkName(rs.getString("Dlvr_Name")); //工作包名称变更描述
                            tmp.setStatus(rs.getString("Status_Name")); //图纸状态变更描述
                            tmp.setDesign(rs.getString("Design_Name")); //设计人变更描述
                            tmp.setCheck(rs.getString("Check_Name")); //校核人变更描述
                            tmp.setReview(rs.getString("Verify_Name")); //审核人变更描述
                            tmp.setApprove(rs.getString("Approve_Name")); //审定人变更描述
                            tmp.setCertified(rs.getString("Reg_Engineer_Name")); //注册工程师变更描述
                            tmp.setGrandNum(rs.getString("Grandnum")); //专业孙项号变更描述
                            tmp.setGrandNum(rs.getString("Workgrandnum")); //工作包孙项
                            tmp.setDual(rs.getString("Bilingual")); //子项双语名称变更描述
                            tmp.setMatCode(rs.getString("Matcode")); //关联物料编码变更描述
                            tmp.setMatName(rs.getString("Matname")); //关联物料名称变更描述
                            dwgStatusChangeDTOs.add(tmp);
                        }
                        rs.close();
                        resultsDTO.setRows(dwgStatusChangeDTOs);
                        return resultsDTO;
                    }
                });
        return results;
    }


    /**
     * 确认图纸状态变更列表
     *
     * @param projectId
     * @param phaseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map confirmChange(Long projectId, String phaseCode) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Confirm_Change(:1,:2,:3,:4)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setString(2, phaseCode);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(WbspParm.DB_STATE, cs.getString(3));
                        tmp.put(WbspParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 取消图纸状态变更列表
     *
     * @param projectId
     * @param phaseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map cancelChange(Long projectId, String phaseCode) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Cancel_Change(:1,:2,:3,:4)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setString(2, phaseCode);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(WbspParm.DB_STATE, cs.getString(3));
                        tmp.put(WbspParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 锁定
     *
     * @param userId
     * @param lockSpecReqDTO
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map lockSpec(Long userId, String sessionId, LockSpecReqDTO lockSpecReqDTO) {
        List<LockSpecDTO> lockSpecDTOs = lockSpecReqDTO.getLockSpecs();
        String phaseCode = lockSpecReqDTO.getPhaseID();
        Long projectId = lockSpecReqDTO.getProjID();
        List<LockSpecEntity> lockSpecEntities = new ArrayList<LockSpecEntity>();
        for (int i = 0; i < lockSpecDTOs.size(); i++) {
            LockSpecEntity lockSpecEntity = new LockSpecEntity();
            lockSpecEntity = this.specDtoToEntity(lockSpecDTOs.get(i));
            lockSpecEntities.add(lockSpecEntity);
        }
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Lock_Spec(:1,:2,:3,:4,:5,:6)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.WBSP_LOCK_SPEC_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, lockSpecEntities.toArray());
                        cs.setArray(1, vArray);
                        cs.setString(2, sessionId);
                        cs.setLong(3, userId);
                        cs.setLong(4, projectId);
                        cs.setString(5, phaseCode);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        if (cs.getString(6).equals("S")) {
                            tmp.put("checkOut", "done");
                        } else {
                            tmp.put("fail", cs.getString(6));
                        }
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 删除锁定
     *
     * @param lockSpecReqDTO
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map unlockSpec(LockSpecReqDTO lockSpecReqDTO) {
        List<LockSpecDTO> lockSpecDTOs = lockSpecReqDTO.getLockSpecs();
        String phaseCode = lockSpecReqDTO.getPhaseID();
        Long projectId = lockSpecReqDTO.getProjID();
        List<LockSpecEntity> lockSpecEntities = new ArrayList<LockSpecEntity>();
        for (int i = 0; i < lockSpecDTOs.size(); i++) {
            LockSpecEntity lockSpecEntity = new LockSpecEntity();
            lockSpecEntity = this.specDtoToEntity(lockSpecDTOs.get(i));
            lockSpecEntities.add(lockSpecEntity);
        }
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Delete_Spec(:1,:2,:3,:4)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.WBSP_LOCK_SPEC_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, lockSpecEntities.toArray());
                        cs.setArray(1, vArray);
                        cs.setLong(2, projectId);
                        cs.setString(3, phaseCode);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(WbspParm.DB_STATE, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 刷新锁定时间
     *
     * @param speccode
     * @param userID
     * @param projID
     * @param phaseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map updateLockSpec(String speccode, long userID
            , long projID, String phaseCode) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Update_Spec(:1,:2,:3,:4,:5)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projID);
                        cs.setString(2, phaseCode);
                        cs.setString(3, speccode);
                        cs.setLong(4, userID);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(WbspParm.DB_STATE, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 判断 锁定专业并清空
     *
     * @param userId
     * @param sessionId
     * @param projId
     * @param phaseCode
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map saveLockSpec(Long userId, String sessionId, long projId, String phaseCode) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Save_Lock_Spec(:1,:2,:3,:4,:5)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, sessionId);
                        cs.setLong(2, userId);
                        cs.setLong(3, projId);
                        cs.setString(4, phaseCode);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put("result", cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 删除锁定（工作包）
     *
     * @param sessionId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map deleteLockDlvr(String sessionId) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Deleted_Spec(:1,:2)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, sessionId);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(WbspParm.DB_STATE, cs.getString(2));
                        return tmp;
                    }
                });
        return map;
    }


    /**
     * 更新锁定（工作包）时间
     *
     * @param sessionId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map updateLockDlvr(String sessionId) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Lev3_Plan.Update_Spec(:1,:2)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, sessionId);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(WbspParm.DB_STATE, cs.getString(2));
                        return tmp;
                    }
                });
        return map;
    }

    public String getUserNO(Long userId) {
        String userNo = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Wbsp_Utils.Get_Uer_No(:1,:2)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, userId);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        String tmp;
                        cs.execute();
                        tmp = cs.getString(2);
                        return tmp;
                    }
                });
        return userNo;

    }

    private String getLevel(int type) {
        String level = new String();
        switch (type) {
            case 10003:
                level = "root";
                break;
            case 10004:
                level = "sys";
                break;
            case 10005:
                level = "div";
                break;
            case 10006:
                level = "spe";
                break;
            case 10000:
                level = "work";
                break;
            case 0:
                level = "root";
                break;
            case 1:
                level = "sys";
                break;
            case 2:
                level = "div";
                break;
            case 3:
                level = "spe";
                break;
            case 4:
                level = "work";
                break;
        }

        return level;
    }


}
