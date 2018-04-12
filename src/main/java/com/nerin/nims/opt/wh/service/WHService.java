package com.nerin.nims.opt.wh.service;


import com.nerin.nims.opt.wbsp_oa.module.DrawNumEntity;
import com.nerin.nims.opt.wh.dto.*;
import com.nerin.nims.opt.wh.dto.DlvrDTO;
import com.nerin.nims.opt.wh.module.SaveEntity;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class WHService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    //取工时信息
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public GetWHL1DTO getWHInfo(String currDate, Long perId) {
        GetWHL1DTO getWHL1DTO = (GetWHL1DTO) jdbcTemplate.execute(
                con -> {
                    String storedProc = "{call APPS.Cux_Nims_Wh_Interface_Pkg.Get_Day_Wh_Info(:1,:2,:3,:4,:5)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setString(1, currDate);
                    cs.setLong(2, perId);
                    cs.registerOutParameter(3, OracleTypes.VARCHAR);
                    cs.registerOutParameter(4, OracleTypes.CURSOR);
                    cs.registerOutParameter(5, OracleTypes.CURSOR);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    cs.execute();
                    ResultSet rs3 = (ResultSet) cs.getObject(4);// 获取游标一行的值
                    ResultSet rs2 = (ResultSet) cs.getObject(5);// 获取游标一行的值
                    GetWHL2DTO level2;
                    ArrayList<GetWHL2DTO> getWHL2DTOArrayList = new ArrayList<>();
                    while (rs2.next()) {// 转换每行的返回值到Map中
                        level2 = new GetWHL2DTO();
                        level2.setId(rs2.getLong("Id"));
                        level2.setFulldate(rs2.getString("Fulldate"));
                        level2.setTotalHour(rs2.getFloat("Totalhour"));
                        getWHL2DTOArrayList.add(level2);
                    }
                    rs2.close();
                    GetWHL3DTO level3;
                    ArrayList<GetWHL3DTO> getWHL3DTOArrayList = new ArrayList<>();
                    Long id = -1L;
                    Long pid = -1L; //上一个ID
                    while (rs3.next()) {// 转换每行的返回值到Map中
                        id = rs3.getLong("Day_Id");
                        level3 = new GetWHL3DTO();
                        level3.setId(rs3.getLong("Id"));
                        level3.setTaskId(rs3.getLong("TaskId"));
                        level3.setCode(rs3.getLong("Code"));
                        level3.setTaskName(rs3.getString("TaskName"));
                        level3.setProjName(rs3.getString("ProjName"));
                        level3.setProjID(rs3.getLong("ProjID"));
                        level3.setPhaseID(rs3.getString("PhaseID"));
                        level3.setUpdateWorkHour(rs3.getFloat("UpdateWorkHour"));
                        level3.setProgress(rs3.getString("Progress"));
                        level3.setComment(rs3.getString("Remark"));
                        level3.setDescription(rs3.getString("Description"));
                        level3.setPhaseName(rs3.getString("PhaseName"));
                        level3.setDivision(rs3.getString("Division"));
                        level3.setProjNum(rs3.getString("ProjNum"));
                        level3.setSystem(rs3.getString("System"));
                        level3.setMajorCode(rs3.getString("MajorCode"));
                        level3.setSpecialityName(rs3.getString("SpecialityName"));
                        level3.setSpecId(rs3.getLong("SpecId"));
                        level3.setAccuWorkHour(rs3.getFloat("AccuWorkHour"));
                        level3.setWorkHour(rs3.getString("WorkHour"));
                        level3.setStartDate(rs3.getString("StartDate"));
                        level3.setEndDate(rs3.getString("EndDate"));
                        level3.setDesignName(rs3.getString("DesignName"));
                        level3.setDesignPerson(rs3.getLong("DesignPerson"));
                        level3.setCheckName(rs3.getString("CheckName"));
                        level3.setReviewName(rs3.getString("ReviewName"));
                        level3.setStatus(rs3.getString("Status"));
                        level3.setAuthorName(rs3.getString("AuthorName"));
                        level3.setApproveName(rs3.getString("ApproveName"));
                        level3.setCertifiedName(rs3.getString("CertifiedName"));
                        level3.setSchemeName(rs3.getString("SchemeName"));
                        level3.setOtherName(rs3.getString("OtherName"));
                        level3.setGrandNum(rs3.getString("GrandNum"));
                        level3.setGrandName(rs3.getString("GrandName"));
                        level3.setWorkgrandNum(rs3.getString("WorkgrandNum"));
                        level3.setMatCode(rs3.getString("MatCode"));
                        level3.setWorktypeID(rs3.getString("WorktypeID"));
                        level3.setRecvSpec(rs3.getString("RecvSpec"));
                        level3.setMatName(rs3.getString("MatName"));
                        level3.setExpire(rs3.getString("Expire"));
                        if (pid == -1L || id.equals(pid)) {
                            getWHL3DTOArrayList.add(level3);
                        } else {
                            for (int i = 0; i < getWHL2DTOArrayList.size(); i++
                                    ) {
                                if (pid == getWHL2DTOArrayList.get(i).getId()) {
                                    getWHL2DTOArrayList.get(i).setTasks(getWHL3DTOArrayList);
                                    getWHL3DTOArrayList = new ArrayList<>();
                                    getWHL3DTOArrayList.add(level3);
                                }
                            }
                        }
                        pid = id;
                    }

                    //处理最后剩下的行
                    for (int i = 0; i < getWHL2DTOArrayList.size(); i++
                            ) {
                        if (pid == getWHL2DTOArrayList.get(i).getId()) {
                            getWHL2DTOArrayList.get(i).setTasks(getWHL3DTOArrayList);
                        }
                    }
                    rs3.close();
                    GetWHL1DTO level1 = new GetWHL1DTO();
                    level1.setApplied(cs.getString(3));
                    level1.setRows(getWHL2DTOArrayList);
                    return level1;
                });
        return getWHL1DTO;
    }


    //取工时信息
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public InterDTO getEnableDlvr(Long perId, long projId, long startId, String currDate, String flag) {
        InterDTO interDTO = (InterDTO) jdbcTemplate.execute(
                con -> {
                    String storedProc = "{call APPS.Cux_Nims_Wh_Interface_Pkg.Get_Dlvr_Info(:1,:2,:3,:4,:5)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setLong(1, perId);
                    cs.setLong(2, projId);
                    cs.registerOutParameter(3, OracleTypes.CURSOR);
                    cs.setString(4, currDate);
                    cs.setString(5, flag);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    cs.execute();
                    ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                    InterDTO tmp = new InterDTO();
                    PhaseDTO phaseDTO = new PhaseDTO();
                    StructureDTO sysDTO = new StructureDTO();
                    StructureDTO subDTO = new StructureDTO();
                    StructureDTO specDTO = new StructureDTO();
                    DlvrDTO dlvrDTO = new DlvrDTO();

                    ArrayList<PhaseDTO> phaseDTOS = new ArrayList<>();
                    ArrayList<StructureDTO> sysDTOS = new ArrayList<>();
                    ArrayList<StructureDTO> subDTOS = new ArrayList<>();
                    ArrayList<StructureDTO> specDTOS = new ArrayList<>();
                    ArrayList<DlvrDTO> dlvrDTOS = new ArrayList<>();

                    String phaseIndex = "";
                    String sysIndex = "";
                    String subIndex = "";
                    String specIndex = "";
                    String subName = "";
                    int count = 0;
                    long id = startId;
                    final String state = "closed";
                    while (rs.next()) {// 转换每行的返回值到Map中
                        count = count + 1;
                        if (specIndex != "" && !specIndex.equals(rs.getString("SPEC_INDEX"))) {
                            id = id + 1;
                            specDTO.setId(id);
                            specDTO.setChildren(dlvrDTOS);
                            dlvrDTOS = new ArrayList<>();
                            specDTOS.add(specDTO);
                            specDTO = new StructureDTO();
                            if (subIndex != "" && !subIndex.equals(rs.getString("SUB_INDEX"))) {
                                if (subDTO.getTaskName() != null) {
                                    id = id + 1;
                                    subDTO.setId(id);
                                    subDTO.setChildren(specDTOS);
                                    specDTOS = new ArrayList<>();
                                    subDTOS.add(subDTO);
                                    subName = subDTO.getTaskName();
                                    subDTO = new StructureDTO();
                                }


                                if (sysIndex != "" && !sysIndex.equals(rs.getString("SYS_INDEX"))) {
                                    if (sysDTO.getTaskName() != null) {
                                        id = id + 1;
                                        sysDTO.setId(id);
                                        sysDTO.setChildren(subDTOS);
                                        subDTOS = new ArrayList<>();
                                        sysDTOS.add(sysDTO);
                                        sysDTO = new StructureDTO();
                                    }


                                    if (phaseIndex != "" && !phaseIndex.equals(rs.getString("PHASE_INDEX"))) {
                                        if (null == subName || subName.length() <= 0) {
                                            phaseDTO.setChildren(specDTOS);
                                            specDTOS = new ArrayList<>();
                                        } else {
                                            phaseDTO.setChildren(sysDTOS);
                                            subName = "";
                                        }
                                        sysDTOS = new ArrayList<>();
                                        id = id + 1;
                                        phaseDTO.setId(id);
                                        phaseDTOS.add(phaseDTO);
                                        phaseDTO = new PhaseDTO();
                                    }
                                }
                            }
                        }
                        phaseIndex = rs.getString("PHASE_INDEX");
                        sysIndex = rs.getString("SYS_INDEX");
                        subIndex = rs.getString("SUB_INDEX");
                        specIndex = rs.getString("SPEC_INDEX");

                        dlvrDTO.setId(rs.getLong("Dlvr_Id"));
                        dlvrDTO.setTaskId(rs.getLong("TaskId"));
                        dlvrDTO.setCode(rs.getLong("Code"));
                        dlvrDTO.setTaskName(rs.getString("DLVR_NAME"));
                        dlvrDTO.setProjName(rs.getString("ProjName"));
                        dlvrDTO.setProjID(rs.getLong("ProjID"));
                        dlvrDTO.setPhaseID(rs.getString("PhaseID"));
                        dlvrDTO.setProgress(rs.getString("Progress"));
                        dlvrDTO.setDescription(rs.getString("Description"));
                        dlvrDTO.setPhaseName(rs.getString("PhaseName"));
                        dlvrDTO.setDivision(rs.getString("Division"));
                        dlvrDTO.setProjNum(rs.getString("ProjNum"));
                        dlvrDTO.setSystem(rs.getString("System"));
                        dlvrDTO.setMajorCode(rs.getString("MajorCode"));
                        dlvrDTO.setSpecialityName(rs.getString("SPEC_NAME"));
                        dlvrDTO.setSpecId(rs.getLong("Spec_Id"));
                        dlvrDTO.setAccuWorkHour(rs.getFloat("AccuWorkHour"));
                        dlvrDTO.setWorkHour(rs.getString("WorkHour"));
                        dlvrDTO.setStartDate(rs.getString("StartDate"));
                        dlvrDTO.setEndDate(rs.getString("EndDate"));
                        dlvrDTO.setDesignName(rs.getString("DesignName"));
                        dlvrDTO.setDesignPerson(rs.getLong("DesignPerson"));
                        dlvrDTO.setCheckName(rs.getString("CheckName"));
                        dlvrDTO.setReviewName(rs.getString("ReviewName"));
                        dlvrDTO.setStatus(rs.getString("Status"));
                        dlvrDTO.setAuthorName(rs.getString("AuthorName"));
                        dlvrDTO.setApproveName(rs.getString("ApproveName"));
                        dlvrDTO.setCertifiedName(rs.getString("CertifiedName"));
                        dlvrDTO.setSchemeName(rs.getString("SchemeName"));
                        dlvrDTO.setOtherName(rs.getString("OtherName"));
                        dlvrDTO.setGrandNum(rs.getString("GrandNum"));
                        dlvrDTO.setGrandName(rs.getString("GrandName"));
                        dlvrDTO.setWorkgrandNum(rs.getString("WorkgrandNum"));
                        dlvrDTO.setMatCode(rs.getString("MatCode"));
                        dlvrDTO.setWorktypeID(rs.getString("WorktypeID"));
                        dlvrDTO.setRecvSpec(rs.getString("RecvSpec"));
                        dlvrDTO.setMatName(rs.getString("MatName"));
                        dlvrDTOS.add(dlvrDTO);
                        dlvrDTO = new DlvrDTO();

                        specDTO.setId(rs.getLong("SPEC_ID"));
                        specDTO.setTaskName(rs.getString("SPEC_NAME"));
                        specDTO.setIconCls("icon-spebr");
                        specDTO.setState(state);

                        subDTO.setId(rs.getLong("SUB_ID"));
                        subDTO.setTaskName(rs.getString("SUB_NAME"));
                        subDTO.setIconCls("icon-divbr");
                        subDTO.setState(state);

                        sysDTO.setId(rs.getLong("SYS_ID"));
                        sysDTO.setTaskName(rs.getString("SYS_NAME"));
                        sysDTO.setIconCls("icon-sysbr");
                        sysDTO.setState(state);

                        phaseDTO.setState(state);
                        phaseDTO.setTaskName(rs.getString("PhaseName"));

                    }
                    rs.close();
                    //处理最后剩下的行
                    if (count > 0) {
                        id = id + 1;
                        specDTO.setId(id);
                        specDTO.setChildren(dlvrDTOS);
                        specDTOS.add(specDTO);

                        if (subDTO.getTaskName() != null) {
                            id = id + 1;
                            subDTO.setId(id);
                            subDTO.setChildren(specDTOS);
                            subDTOS.add(subDTO);
                            if (sysDTO.getTaskName() != null) {
                                id = id + 1;
                                sysDTO.setId(id);
                                sysDTO.setChildren(subDTOS);
                                sysDTOS.add(sysDTO);
                            }
                            phaseDTO.setChildren(sysDTOS);
                        } else {
                            phaseDTO.setChildren(specDTOS);
                        }
                        id = id + 1;
                        phaseDTO.setId(id);
                        phaseDTOS.add(phaseDTO);
                    }
                    tmp.setId(id + count);
                    tmp.setChildren(phaseDTOS);
                    return tmp;
                });
        return interDTO;
    }

    //管理工时task
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public InterDTO getManger(Long projId, Long startId) {
        InterDTO result = (InterDTO) jdbcTemplate.execute(
                con -> {
                    String storedProc = "{call APPS.Cux_Nims_Wh_Interface_Pkg.Get_Manger_Info(:1,:2)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setLong(1, projId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    cs.execute();
                    ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                    TaskMDTO taskMDTO;
                    InterDTO interDTO = new InterDTO();
                    ArrayList<TaskMDTO> taskMDTOS = new ArrayList<TaskMDTO>();
                    Long id = startId;
                    while (rs.next()) {// 转换每行的返回值到Map中
                        taskMDTO = new TaskMDTO();
                        id = id + 1;
                        taskMDTO.setId(id);
                        taskMDTO.setTaskId(rs.getLong("TASK_ID"));
                        taskMDTO.setTaskName(rs.getString("TASK_NAME"));
                        taskMDTO.setAccuWorkHour(rs.getFloat("ACCUWORKHOUR"));
                        taskMDTO.setProjID(rs.getLong("Project_Id"));
                        taskMDTO.setProjName(rs.getString("Name"));
                        taskMDTO.setProjNum(rs.getString("Segment1"));
                        taskMDTOS.add(taskMDTO);
                    }
                    rs.close();
                    interDTO.setId(id);
                    interDTO.setChildren(taskMDTOS);
                    return interDTO;
                });
        return result;
    }

    //取工时信息
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getProject(String currDate, Long perId, String keywords, String projType, Long pageNo, Long pageSize, String flag) {
        DataTablesDTO dataTablesDTO = (DataTablesDTO) jdbcTemplate.execute(
                con -> {
                    String storedProc = "{call APPS.Cux_Nims_Wh_Interface_Pkg.Get_Proj_Info(:1,:2,:3,:4,:5,:6,:7,:8,:9)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setLong(1, perId);
                    cs.setString(2, currDate);
                    cs.setString(3, keywords);
                    cs.setString(4, projType);
                    cs.setLong(5, pageNo);// 设置输入参数的值
                    cs.setLong(6, pageSize);// 设置输入参数的值
                    cs.setString(7, flag);// 设置输入参数的值
                    cs.registerOutParameter(8, OracleTypes.CURSOR);
                    cs.registerOutParameter(9, OracleTypes.NUMBER);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    cs.execute();
                    ResultSet rs = (ResultSet) cs.getObject(8);// 获取游标一行的值
                    DataTablesDTO tmp = new DataTablesDTO();
                    ProjectDTO projectDTO;
                    TypeDTO typeDTO;
                    //ArrayList<TypeDTO> typeDTOS ;
                    ArrayList<ProjectDTO> projectDTOS = new ArrayList<>();
                    long id = 0L;
                    final String state = "closed";

                    while (rs.next()) {// 转换每行的返回值到Map中
                        ArrayList typeDTOS = new ArrayList<>();
                        projectDTO = new ProjectDTO();
                        id = id + 1;
                        projectDTO.setId(id);
                        projectDTO.setProjID(rs.getLong("PROJECT_ID"));
                        projectDTO.setState(state);
                        projectDTO.setTaskName(rs.getString("P_TASK_NAME"));
                        if ("Y".equals(rs.getString("EXISTS_MANGER"))) {
                            //  typeDTO = new TypeDTO();
                            InterDTO interDTO = getManger(projectDTO.getProjID(), id);
                            id = interDTO.getId();
                            //typeDTO.setType("管理工时");
                            //typeDTO.setChildren(interDTO.getChildren());
                            //typeDTOS.add(typeDTO);
                            for (int i = 0; i < interDTO.getChildren().size(); i++) {
                                typeDTOS.add(interDTO.getChildren().get(i));
                            }

                        }
                        if ("Y".equals(rs.getString("EXISTS_DLVR"))) {
                            //typeDTO = new TypeDTO();
                            InterDTO interDTO = getEnableDlvr(perId, projectDTO.getProjID(), id, currDate, flag);
                            id = interDTO.getId();
                            //typeDTO.setType("工作包工时");
                            //typeDTO.setChildren(interDTO.getChildren());
                            //typeDTOS.add(typeDTO);
                            for (int i = 0; i < interDTO.getChildren().size(); i++) {
                                typeDTOS.add(interDTO.getChildren().get(i));
                            }
                        }
                        projectDTO.setChildren(typeDTOS);
                        projectDTOS.add(projectDTO);
                    }
                    rs.close();
                    tmp.setTotal(cs.getLong(9));
                    tmp.setPageNo(pageNo);
                    tmp.setPageSize(pageSize);
                    tmp.setRows(projectDTOS);
                    return tmp;
                });
        return dataTablesDTO;
    }

    private SaveEntity saveDtotoToEntity(SaveDTO saveDTO) {
        SaveEntity entity = new SaveEntity();
        try {
            PropertyUtils.copyProperties(entity, saveDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private List<SaveEntity> initSaveEntitys(DataSaveDTO dataSaveDTO) {
        List<SaveEntity> dataList = new ArrayList<SaveEntity>();
        for (int i = 0; i < dataSaveDTO.getTasks().size(); i++) {
            SaveEntity saveEntity;
            saveEntity = this.saveDtotoToEntity(dataSaveDTO.getTasks().get(i));
            if (null == dataSaveDTO.getTasks().get(i).getDlvrId() || 0 == dataSaveDTO.getTasks().get(i).getDlvrId()) {
                saveEntity.setDlvrId(null);
            }
            dataList.add(saveEntity);
        }
        return dataList;
    }

    /**
     * 保存工时
     *
     * @param dataSaveDTO
     * @param perId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map saveWH(DataSaveDTO dataSaveDTO, Long perId) {

        Map map = (Map) jdbcTemplate.execute(
                con -> {
                    List<SaveEntity> dataList = new ArrayList<SaveEntity>();
                    dataList = initSaveEntitys(dataSaveDTO);
                    String storedProc = "{call APPS.Cux_Nims_Wh_Interface_Pkg.Save_Wh(:1,:2,:3,:4,:5)}";// 调用的sql
                    OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                    CallableStatement cs = con.prepareCall(storedProc);
                    ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_SAVE_WH_TBL", or);
                    ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                    cs.setLong(1, perId);
                    cs.setArray(2, vArray);
                    cs.setString(3, dataSaveDTO.getDate());
                    cs.registerOutParameter(4, OracleTypes.VARCHAR);
                    cs.registerOutParameter(5, OracleTypes.VARCHAR);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    cs.execute();
                    Map tmp = new HashMap();
                    if (cs.getString(4).equals("S")) {
                        tmp.put("Status", "Success");
                    } else {
                        tmp.put("Status", "Error");
                        tmp.put("ErrMsg", cs.getString(5));
                    }
                    return tmp;
                });
        return map;
    }

}
