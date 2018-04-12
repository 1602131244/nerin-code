package com.nerin.nims.opt.navi.service;

import com.nerin.nims.opt.app.web.rest.dto.DataTableDTO;
import com.nerin.nims.opt.navi.dto.*;
import com.nerin.nims.opt.navi.module.MilestoneEntity;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import com.nerin.nims.opt.nbcc.dto.TemplateChaptersDTO;
import com.nerin.nims.opt.nbcc.dto.TemplateHeaderDTO;
import com.nerin.nims.opt.nbcc.module.TemplateChaptersEntity;
import com.nerin.nims.opt.nbcc.module.TemplateHeaderEntity;
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
import org.springframework.jdbc.core.ResultSetExtractor;
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
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/7/7.
 */
@Component
@Transactional
public class MilestoneService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unchecked")
    private MilestoneEntity cdtoToEntity(MilestoneDto milestoneDto) {
        MilestoneEntity entity = new MilestoneEntity();
        try {
            PropertyUtils.copyProperties(entity, milestoneDto);
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
     * 检查工作计划版本
     *
     * @param projectId
     * @return 返回版本号
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Long checkworkplanStatus(Long projectId) {
        Long i = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.check_workplan_status(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);//设置参数
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getLong(2);
                    }
                });
        return i;
    }

    /**
     * 获取里程碑项目信息，树形结构第一行
     *
     * @param projectId 项目ID
     *  @param version 项目ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public MilestoneProjectDTO getMilestoneProject(Long projectId,Long version) {
        MilestoneProjectDTO dt = new MilestoneProjectDTO();

        jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.get_milestone_project(?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setLong(2, version);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);// 注册输出参数的类型
                        cs.registerOutParameter(4, OracleTypes.DATE);// 注册输出参数的类型
                        cs.registerOutParameter(5, OracleTypes.DATE);// 注册输出参数的类型
                        cs.registerOutParameter(6, OracleTypes.NUMBER);// 注册输出参数的类型
                        cs.registerOutParameter(7, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        dt.setProjectName(cs.getString(3));
                        dt.setStartDate(cs.getDate(4));
                        dt.setEndDate(cs.getDate(5));
                        dt.setSchedule(cs.getLong(6));
                        dt.setCompletePercent(cs.getString(7));
                        return dt;
                    }
                });

        return dt;
    }

    /**
     * 获取里程碑状态列表
     *
     * @param projectId 项目ID
     *  @param version 项目ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public MDataTableDTO getMilestoneStatus(Long projectId,Long version) {
        MDataTableDTO dt = new MDataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.get_milestone_status(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setLong(2, version);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {

                        cs.execute();
                        dt.setStatus(cs.getString(3));

                        return dt;
                    }
                });

        return dt;
    }

    /**
     * 获取里程碑列表
     *
     * @param projectId 项目ID
     * @param version   里程碑版本
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getMilestoneList(Long projectId, Long version) {
        DataTableDTO dt = new DataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<MilestoneDto> resultList = (List<MilestoneDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.get_milestone(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.setLong(2, version);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型

                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<MilestoneDto> results = new ArrayList<MilestoneDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值

                        MilestoneDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new MilestoneDto();
                            tmp.setMilestoneId(rs.getLong("milestone_id"));
                            tmp.setLineId(rs.getLong("line_id"));//里程碑行ID
                            tmp.setProjectId(rs.getLong("project_id"));
                            tmp.setName(rs.getString("name"));
                            tmp.setPhaseCode(rs.getLong("phase_code"));
                            tmp.setPhasePercent(rs.getLong("phase_percent"));//估算进度
                            tmp.setPercent(rs.getString("percent"));//里程完成百分比
                            tmp.setPhaseWeight(rs.getFloat("phase_weight"));
                            tmp.setPlanStartDate(rs.getDate("plan_start_date"));
                            tmp.setPlanEndDate(rs.getDate("plan_end_date"));
                            tmp.setActualCompleteDate(rs.getDate("actual_complete_date"));
                            tmp.setMilestoneStatus(rs.getString("milestone_status"));
                            tmp.setApproveStatus(rs.getString("approve_status"));
                            tmp.setVersion(rs.getLong("version"));
                            tmp.setParentId(rs.getLong("parent_id"));
                            tmp.setSeqNum(rs.getLong("seq_num"));
                            tmp.setIsmark(rs.getString("ismark"));
                            tmp.setAttribute1(rs.getString("attribute1"));
                            tmp.setAttribute2(rs.getString("attribute2"));
                            tmp.setAttribute3(rs.getString("attribute3"));
                            tmp.setAttribute4(rs.getString("attribute4"));
                            tmp.setAttribute5(rs.getString("attribute5"));
                            tmp.setAttribute6(rs.getString("attribute6"));
                            tmp.setAttribute7(rs.getString("attribute7"));
                            tmp.setAttribute8(rs.getString("attribute8"));
                            tmp.setAttribute9(rs.getString("attribute9"));
                            tmp.setAttribute10(rs.getString("attribute10"));
                            tmp.setCreatedBy(rs.getLong("created_by"));
                            tmp.setCreationDate(rs.getDate("creation_date"));
                            tmp.setLastUpdateDate(rs.getDate("last_update_date"));
                            tmp.setLastUpdatedBy(rs.getLong("last_update_by"));
                            tmp.setUrl(rs.getString("url"));
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
     * 获取里程碑列表，树形结构
     *
     * @param projectId 项目ID
     * @param version   里程碑版本
     * @return
     */
    @Transactional(readOnly = true)
    public List<MilestoneDto> getMilestoneList1(Long projectId, Long version) {
        DataTableDTO dt = this.getMilestoneList(projectId, version);
        MilestoneProjectDTO dd=this.getMilestoneProject(projectId,version);//取项目信息，树形结构第一行
        List<MilestoneDto> nodeList = new ArrayList<MilestoneDto>();

        MilestoneDto init = new MilestoneDto();//添加项目节点，第一级
        init.setName(dd.getProjectName());
        init.setPercent(dd.getCompletePercent());//里程碑完成情况
        init.setPhasePercent(dd.getSchedule());//估算进度
        init.setPlanStartDate(dd.getStartDate());//开始时间
        init.setPlanEndDate(dd.getEndDate());//结束时间
        init.setParentId((long) -2);
        init.setPlanStartDate(dd.getStartDate());
        init.setPlanEndDate(dd.getEndDate());
        init.setChildren(new ArrayList<MilestoneDto>());
        if (null != dt.getDataSource() && 0 < dt.getDataSource().size()) {
            List<MilestoneDto> tmpList = dt.getDataSource();

            for (MilestoneDto node1 : tmpList) {  //一次循环，取里程碑
                boolean mark = false;
                for (MilestoneDto node2 : tmpList) {  //二次循环，取阶段
                    if (-1 != node1.getParentId() && node1.getParentId().equals(node2.getMilestoneId())) {//如果node1的parentId<>-1并且parentID=node2.milestoneId，node1是node2的子集
                        mark = true;
                        if (node2.getChildren() == null)
                            node2.setChildren(new ArrayList<MilestoneDto>());
                        node2.getChildren().add(node1);//阶段下添加里程碑
                        break;
                    }
                }
                if (!mark) {
                  //  nodeList.add(node1);//添加阶段
                    init.getChildren().add(node1);
                }
            }
            nodeList.add(init);
        }
// else {
//            MilestoneDto init = new MilestoneDto();
//            init.setChapterName("root");
//            init.setTemplateHeaderId(templateId);
//            init.setChapterNo("-1");
//            init.setParentChapterNo("-1");
//            nodeList.add(init);
//            init = new MilestoneDto();
//            init.setChapterName("初始");
//            init.setTemplateHeaderId(templateId);
//            nodeList.add(init);
//        }
        return nodeList;
    }

    /**
     * 获取项目角色列表
     *
     * @param personId  人员ID
     * @param projectId 项目ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getProjectRole(Long personId, Long projectId) {
        DataTableDTO dt = new DataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<ProjectRoleDTO> resultList = (List<ProjectRoleDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.get_my_project_role(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, personId);// 设置输入参数的值
                        cs.setLong(2, projectId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectRoleDTO> results = new ArrayList<ProjectRoleDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        ProjectRoleDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectRoleDTO();
                            tmp.setProjectRoleId(rs.getLong("project_role_id"));
                            tmp.setProjectRole(rs.getString("project_role"));

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
     * 获取角色组
     *
     * @param personId  人员ID
     * @param projectId 项目ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getRoleGroup(Long personId, Long projectId) {
        DataTableDTO dt = new DataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<ProjectRoleGroupDTO> resultList = (List<ProjectRoleGroupDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.get_role_group(?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, personId);// 设置输入参数的值
                        cs.setLong(2, projectId);// 设置输入参数的值
                        cs.registerOutParameter(3, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ProjectRoleGroupDTO> results = new ArrayList<ProjectRoleGroupDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        ProjectRoleGroupDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjectRoleGroupDTO();
                            tmp.setSeq(rs.getLong("seq"));
                            tmp.setRoleGroup(rs.getString("group_name"));
                            tmp.setRole_name(rs.getString("role_name"));
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
     * 获取里程碑版本列表
     *
     * @param projectId 项目ID
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTableDTO getMilestoneVersion(Long projectId) {
        DataTableDTO dt = new DataTableDTO();
        // dt.setPageNo(pageNo);
        //dt.setPageSize(pageSize);
        List<MilestoneVersionDTO> resultList = (List<MilestoneVersionDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.get_milestone_version(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);// 设置输入参数的值
                        cs.registerOutParameter(2, OracleTypes.CURSOR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<MilestoneVersionDTO> results = new ArrayList<MilestoneVersionDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        MilestoneVersionDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new MilestoneVersionDTO();
                            tmp.setVersion(rs.getLong("version"));
                            tmp.setApproveStatus(rs.getString("approve_status"));
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
     * 调整计划按钮，返回版本号
     *
     * @param projectId
     * @param userId
     * @return 返回版本号
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Long updatePlan(Long projectId, Long userId) {
        Long i = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.update_plan(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);//设置参数
                        cs.setLong(2, userId);//设置参数
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getLong(3);
                    }
                });
        return i;
    }

    /**
     * 保存按钮，返回版本号
     *
     * @param milestoneDtos
    // * @param uerId
     * @return 返回版本号
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Long saveMilestone(List<MilestoneDto> milestoneDtos) { //接收list，返回版本
        List<MilestoneEntity> inData = milestoneDtos.stream().map(mile -> {
            MilestoneEntity tmp = this.cdtoToEntity(mile);
            return tmp;
        }).collect(Collectors.toList());
        Long i = (Long)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.save_plan(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.NAVI_MILESTONE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
                        cs.setArray(1, vArray);
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
     * 更新里程碑状态
     * @param milestoneIds
     * @param status
     * @return 返回消息
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public String updateMilestoneStatus( String milestoneIds, String status) {
        String message = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.update_milestone_status(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, milestoneIds);//设置参数
                        cs.setString(2, status);//设置参数
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getString(3);
                    }
                });
        return message;
    }

    /**
     * 提交审批
      * @param projectId 项目ID
     * @param version   里程碑版本
     * @return 返回消息
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public String submit( Long projectId, Long version) {
        String message = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.submit_approve(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);//设置参数
                        cs.setLong(2, version);//设置参数
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getString(3);
                    }
                });
        return message;
    }

    @SuppressWarnings("unchecked")
    public Map submitOaApproved(long id, String userNo, String status) {
        Map result = (Map)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Pa_Oa_Navi_Milestone_Pkg.Submit_Oa_Approved(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.setString(2, userNo);
                        cs.setString(3, status);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(4));
                        tmp.put(NbccParm.DB_STATE, cs.getString(5));
                        tmp.put(NbccParm.DB_MSG, cs.getString(6));
                        return tmp;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public String isMilestoneIncomeNode(long id) {
        String sql = "SELECT APPS.Cux_Pa_Oa_Navi_Milestone_Pkg.is_Milestone_income_Node(" + id + ") as S FROM dual";
        String data = jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
            public String extractData(ResultSet rs) throws SQLException, DataAccessException {
                String result = "";
                while(rs.next()) {
                    result = rs.getString("S");
                }
                return result;
            }});
        return data;
    }

    @SuppressWarnings("unchecked")
    public Map selfApproval(long proId, long no, long userId) {
        Map result = (Map)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Pa_Oa_Navi_Milestone_Pkg.Self_Approval(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, proId);
                        cs.setLong(2, no);
                        cs.setLong(3, userId);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getString(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return result;
    }

    public Map validataPlanDate(long proId, String phaseCode, String date) {
        Map result = (Map)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.Validata_Plan_Date(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, proId);
                        cs.setString(2, phaseCode);
                        cs.setString(3, date);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getString(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return result;
    }
//删除里程碑
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map deleteMilestone( long projectId, long version) {
        Map message = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_navi_lcb_pkg.delete_milestone(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);//设置参数
                        cs.setLong(2, version);//设置参数
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(3));
                        return tmp;
                    }
                });
        return message;
    }
}
