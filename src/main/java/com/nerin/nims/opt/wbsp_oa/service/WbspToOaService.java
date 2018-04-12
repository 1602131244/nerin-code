package com.nerin.nims.opt.wbsp_oa.service;


import com.nerin.nims.opt.wbsp_oa.dto.*;
import com.nerin.nims.opt.wbsp_oa.module.DlvrDetailEntity;
import com.nerin.nims.opt.wbsp_oa.module.DrawNumEntity;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleConnection;
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
public class WbspToOaService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    private List<DrawNumEntity> initDrawEntitys(List<String> drawNumDTOs) {
        List<DrawNumEntity> dataList = new ArrayList<>();
        for (int i = 0; i < drawNumDTOs.size(); i++) {
            DrawNumEntity drawNumEntity = new DrawNumEntity();
            drawNumEntity.setDrawNum(drawNumDTOs.get(i));
            dataList.add(drawNumEntity);
        }
        return dataList;
    }

    /**
     * 保存图号
     */

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map saveDrawNum(OaDrawNumDTO oaDrawNumDTO) {
        List<DrawNumEntity> dataList = initDrawEntitys(oaDrawNumDTO.getDrawNums());
        Map map = (HashMap) jdbcTemplate.execute(
                con -> {
                    OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                    String storedProc = "{call APPS.Cux_Wbsp_To_Oa.saveDrawNum(:1,:2,:3,:4)}";
                    CallableStatement cs = con.prepareCall(storedProc);
                    ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_DRAWNUMS", or);
                    ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                    cs.setArray(1, vArray);
                    cs.setLong(2, oaDrawNumDTO.getRequestid());
                    cs.registerOutParameter(3, OracleTypes.VARCHAR);
                    cs.registerOutParameter(4, OracleTypes.VARCHAR);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    Map tmp = new HashMap();
                    cs.execute();
                    String str = "";
                    String mgr = "";
                    if (cs.getString(4).equals("S")) {
                        tmp.put("MSG", "S");
                    } else {
                        tmp.put("MSG", cs.getObject(3));
                    }
                    return tmp;
                });
        return map;
    }

    //取工作包信息
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DlvrDTO getDlvr(Long dlvrId) {
        DlvrDTO dlvrDTO = (DlvrDTO) jdbcTemplate.execute(
                con -> {
                    String storedProc = "{call APPS.Cux_Wbsp_To_Oa.Get_Deliverable(:1,:2)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setLong(1, dlvrId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    cs.execute();
                    ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                    DlvrDTO tmp = new DlvrDTO();
                    while (rs.next()) {// 转换每行的返回值到Map中
                        tmp.setDesign(rs.getString("Designnum"));
                        tmp.setCheck(rs.getString("Checknum"));
                        tmp.setReview(rs.getString("Reviewnum"));
                        tmp.setApprove(rs.getString("Approvenum"));
                        tmp.setCertified(rs.getString("Certifiednum"));
                        tmp.setScheme(rs.getString("Schemenum"));
                        tmp.setMajorCode(rs.getString("Majorcode"));
                        tmp.setWorktypeID(rs.getString("Worktypeid"));
                    }
                    rs.close();
                    return tmp;
                });
        return dlvrDTO;
    }

    //查询OA校审流程表单数据
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public ApproveFromDTO getApproveForm(Long requestId) {
        ApproveFromDTO approveFromDTO = (ApproveFromDTO) jdbcTemplate.execute(
                con -> {
                    String storedProc = "{call APPS.Cux_Wbsp_To_Oa.Get_Oa_Form(:1,:2)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setLong(1, requestId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    cs.execute();
                    ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                    ApproveFromDTO tmp = new ApproveFromDTO();
                    while (rs.next()) {// 转换每行的返回值到Map中
                        tmp.setDesign(rs.getString("Sjr"));
                        tmp.setCheck(rs.getString("Jhr"));
                        tmp.setReview(rs.getString("Shr"));
                        tmp.setApprove(rs.getString("Sdr"));
                        tmp.setCertified(rs.getString("Zcs"));
                        tmp.setScheme(rs.getString("Fasjr"));
                        tmp.setMajorCode(rs.getString("Zyjd"));
                        tmp.setDlvrs(rs.getString("Dlvrs"));
                        tmp.setStatus(rs.getString("Status"));
                        tmp.setCurrentPersonNum(rs.getString("Currentpersonnum"));
                        tmp.setFormid(rs.getLong("Formid"));
                        tmp.setNbccTaskName(rs.getString("Nbcctaskname"));
                        tmp.setLcsm(rs.getString("lcsm"));
                        tmp.setZyjds(rs.getString("Zyjds"));
                        tmp.setStartDate(rs.getString("StartDate"));
                        tmp.setEndDate(rs.getString("EndDate"));
                        tmp.setErpid(rs.getString("Erpid"));
                    }
                    rs.close();
                    return tmp;
                });
        return approveFromDTO;
    }

    private DlvrDetailEntity HeadDtoToEntity(DlvrDetailDTO dlvrDetailDTO) {
        DlvrDetailEntity entity = new DlvrDetailEntity();
        try {
            PropertyUtils.copyProperties(entity, dlvrDetailDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private List<DlvrDetailEntity> initHeadEntitys(List<DlvrDetailDTO> dlvrDetailDTOs) {
        List<DlvrDetailEntity> dataList = new ArrayList<>();
        for (int i = 0; i < dlvrDetailDTOs.size(); i++) {
            DlvrDetailEntity dlvrDetailEntity;
            dlvrDetailEntity = this.HeadDtoToEntity(dlvrDetailDTOs.get(i));
            dataList.add(dlvrDetailEntity);
        }
        return dataList;
    }


    /**
     * 发起本部设计成果校审流程
     */

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map saveHead(HeadAchievementOaDTO headAchievementOaDTO, String userNo, String flag) {
        List<DlvrDetailEntity> dataList = initHeadEntitys(headAchievementOaDTO.getDlvrDetail());
        Map map = (HashMap) jdbcTemplate.execute(
                con -> {
                    OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                    String storedProc = "{call APPS.Cux_Wbsp_To_Oa.Savehead(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10,:11,:12,:13,:14,:15,:16,:17,:18,:19,:20,:21,:22)}";
                    CallableStatement cs = con.prepareCall(storedProc);
                    ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_WBSPOA_DT_TBL", or);
                    ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                    cs.setArray(1, vArray);
                    cs.setString(2, headAchievementOaDTO.getXmbh());
                    cs.setString(3, headAchievementOaDTO.getXmmc());
                    cs.setString(4, headAchievementOaDTO.getContent());
                    cs.setString(5, headAchievementOaDTO.getZy());
                    cs.setString(6, headAchievementOaDTO.getWb());
                    cs.setString(7, headAchievementOaDTO.getTz());
                    cs.setString(8, headAchievementOaDTO.getDesign());
                    cs.setString(9, headAchievementOaDTO.getCheck());
                    cs.setString(10, headAchievementOaDTO.getReview());
                    cs.setString(11, headAchievementOaDTO.getApprove());
                    cs.setString(12, headAchievementOaDTO.getCertified());
                    cs.setString(13, headAchievementOaDTO.getScheme());
                    cs.setString(14, headAchievementOaDTO.getMajorCode());
                    cs.setString(15, headAchievementOaDTO.getDlvrs());
                    cs.setString(16, headAchievementOaDTO.getNbccTaskName());
                    if (null == headAchievementOaDTO.getNbccDlvr())
                        cs.setBigDecimal(17, null);
                    else
                        cs.setLong(17, headAchievementOaDTO.getNbccDlvr());
                    if (null == headAchievementOaDTO.getTaskHeaderId())
                        cs.setBigDecimal(18, null);
                    else
                        cs.setLong(18, headAchievementOaDTO.getTaskHeaderId());
                    cs.registerOutParameter(19, OracleTypes.VARCHAR);
                    cs.registerOutParameter(20, OracleTypes.VARCHAR);
                    cs.setString(21, userNo);
                    cs.setString(22, flag);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    Map tmp = new HashMap();
                    cs.execute();
                    String str = "";
                    String mgr = "";
                    if (cs.getString(20).equals("S")) {
                        tmp.put("URL", cs.getObject(19));
                    } else {
                        tmp.put("MSG", cs.getObject(19));
                    }
                    return tmp;
                });
        return map;
    }


    /**
     * 更新审批流程
     */

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map updateApprove(ApproveDTO approveDTO) {
        List<DlvrDetailEntity> dataList = initHeadEntitys(approveDTO.getDlvrDetail());
        Map map = (HashMap) jdbcTemplate.execute(
                con -> {
                    OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                    String storedProc = "{call APPS.Cux_Wbsp_To_Oa.Update_Approve(:1,:2,:3,:4,:5,:6,:7)}";
                    CallableStatement cs = con.prepareCall(storedProc);
                    ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_WBSPOA_DT_TBL", or);
                    ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                    cs.setArray(1, vArray);
                    cs.setLong(2, approveDTO.getFormid());
                    cs.setString(3, approveDTO.getContent());
                    cs.setString(4, approveDTO.getDlvrs());
                    cs.setLong(5, approveDTO.getRequestid());
                    cs.registerOutParameter(6, OracleTypes.VARCHAR);
                    cs.registerOutParameter(7, OracleTypes.VARCHAR);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    Map tmp = new HashMap();
                    cs.execute();
                    String str = "";
                    String mgr = "";
                    if (cs.getString(7).equals("S")) {
                        tmp.put("MSG", "S");
                    } else {
                        tmp.put("MSG", cs.getObject(6));
                    }
                    return tmp;
                });
        return map;
    }

    //工作包校核流程 OA列表
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<DlvrOAProcessDTO> getDlvrOa(Long dlvrId) {
        List<DlvrOAProcessDTO> resultList = (List<DlvrOAProcessDTO>) jdbcTemplate.execute(
                con -> {
                    String storedProc = "{call APPS.Cux_Wbsp_To_Oa.Get_Oa_Process(?,?)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setLong(1, dlvrId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    List<DlvrOAProcessDTO> results = new ArrayList<DlvrOAProcessDTO>();
                    cs.execute();
                    ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                    DlvrOAProcessDTO tmp = null;
                    while (rs.next()) {// 转换每行的返回值到Map中
                        tmp = new DlvrOAProcessDTO();
                        tmp.setJsnr(rs.getString("Jsnr"));
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
                });
        return resultList;
    }

    /**
     * 创建工作包
     */

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map createDlvr(CreateDlvrDTO createDlvrDTO) {
        Map map = (HashMap) jdbcTemplate.execute(
                con -> {
                    OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                    String storedProc = "{call APPS.Cux_Pa_To_Oa_Pkg.Oa_Create_Dlvr1(:1,:2,:3,:4,:5,:6,:7,:8,:9,:10,:11,:12,:13,:14,:15)}";
                    CallableStatement cs = con.prepareCall(storedProc);
                    ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_WBSPOA_DT_TBL", or);
                    cs.setLong(1, createDlvrDTO.getRequestId());
                    cs.setLong(2, createDlvrDTO.getProjectId());
                    cs.setString(3, createDlvrDTO.getMajor());
                    cs.setString(4, createDlvrDTO.getSpecId());
                    cs.setString(5, createDlvrDTO.getStartDate());
                    cs.setString(6, createDlvrDTO.getEndDate());
                    cs.setLong(7, createDlvrDTO.getDesignId());
                    cs.setLong(8, createDlvrDTO.getCheckId());
                    cs.setLong(9, createDlvrDTO.getReviewId());
                    cs.setLong(10, createDlvrDTO.getApproveId());
                    cs.setLong(11, createDlvrDTO.getSchemeDesignerId());
                    cs.setLong(12, createDlvrDTO.getRegEngineerId());
                    cs.setLong(13, createDlvrDTO.getUserId());
                    cs.registerOutParameter(14, OracleTypes.VARCHAR);
                    cs.registerOutParameter(15, OracleTypes.VARCHAR);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    Map tmp = new HashMap();
                    cs.execute();
                    String str = "";
                    String mgr = "";
                    if (cs.getString(14).equals("E")) {
                        tmp.put("MSG", cs.getObject(15));
                    } else {
                        tmp.put("MSG", 'S');
                    }
                    return tmp;
                });
        return map;
    }
}