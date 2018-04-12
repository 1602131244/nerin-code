package com.nerin.nims.opt.cadi.service;

import com.nerin.nims.opt.cadi.dto.*;
import com.nerin.nims.opt.cadi.module.*;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.module.TemplateChaptersEntity;
import com.nerin.nims.opt.nbcc.module.TemplateHeaderEntity;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by user on 16/7/15.
 */
@Component
@Transactional
public class DrawPrintService {
    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplateOne;

    private DrawSizeEntity cdtoToEntity(DrawSizeDTO DrawSizeDTO) {
        DrawSizeEntity entity = new DrawSizeEntity();
        try {
            PropertyUtils.copyProperties(entity, DrawSizeDTO);
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
    @Transactional(readOnly = true)
    public List<DrawsDTO> getDrawList(String employeeNo, Long projectId, String phaseCode, String unitTaskCode, String specialityCode,
                                      Long dlvrId, String drawStatusCode, String docType
    ) {
        List<DrawsDTO> resultList = (List<DrawsDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call cux_cadi_po1_pkg.get_draws_list(?,?,?,?,?,?,?,?,?)}";   //7
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, employeeNo);
                        cs.setLong(2, projectId);
                        cs.setString(3, phaseCode);
                        cs.setString(4, unitTaskCode);
                        cs.setString(5, specialityCode);
                        if (dlvrId != null)
                            cs.setLong(6, dlvrId);
                        else
                            cs.setBigDecimal(6, null);
                        cs.setString(7, drawStatusCode);
                        cs.setString(8, docType);
                        cs.registerOutParameter(9, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DrawsDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(9);
                        DrawsDTO tmp = null;
                        while (rs.next()) {
                            tmp = new DrawsDTO();
                            tmp.setDid(rs.getLong("did"));
                            tmp.setDrawNum(rs.getString("ddocname"));
                            tmp.setReviseNum(rs.getString("xpjt_revise_num"));
                            tmp.setDrawName(rs.getString("ddoctitle"));
                            tmp.setxSize(rs.getString("xsize"));
                            tmp.setxProjectNum(rs.getString("xproject_num"));
                            tmp.setxDesignPhase(rs.getString("xdesign_phase"));
                            tmp.setxDivisionNum(rs.getString("xdivision_num"));
                            tmp.setxDivision(rs.getString("xdivision"));
                            tmp.setxSpecialty(rs.getString("xspecialty"));
                            tmp.setSpecialityName(rs.getString("Xspecialty_Name"));
                            tmp.setxDlvrId(rs.getLong("xdlvr_id"));
                            tmp.setxDlvrName(rs.getString("xdlvr_name"));
                            tmp.setxEmFlag(rs.getString("xem_flag"));
                            tmp.setxDesignDate(rs.getString("xdesign_date"));
                            tmp.setxWpApprStatus(rs.getString("xwp_appr_status"));
                            tmp.setxCountersignStatus(rs.getString("xcountersign_status"));
                            tmp.setxPltStatus(rs.getString("xplt_status"));
                            tmp.setdReleaseDate(rs.getDate("dreleasedate"));
                            tmp.setdDocType(rs.getString("ddoctype"));
                            tmp.setProjectId(rs.getLong("project_id"));
                            tmp.setProjectSName(rs.getString("project_sname"));
                            tmp.setxEquipmentNum(rs.getString("xequipment_num"));
                            tmp.setxEquipment(rs.getString("xequipment"));
                            tmp.setSpecialityName(rs.getString("xspecialty_name"));
                            tmp.setPhaseName(rs.getString("xdesign_phase_name"));
//                            tmp.setxOrganization(rs.getString("xorganization"));
                            tmp.setDlvrName(rs.getString("xdlvr_name"));
                            tmp.setXspecialtySeq(rs.getString("Xspecialty_Seq"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return formatTree(resultList);
    }

    /**
     * PROJECT - PHASE 1
     * |_PHASE 2
     *
     * @param drawDTOsList
     * @return
     */
    public List<DrawsDTO> formatTree(List<DrawsDTO> drawDTOsList) {
        List<String> projectList = new ArrayList<>();
        List<DrawsDTO> projectTree = new ArrayList<>();
        List<String> phaseList = new ArrayList<>();
        List<DrawsDTO> phaseTree = new ArrayList<>();
        List<String> unitTaskList = new ArrayList<>();
        List<DrawsDTO> unitTaskTree = new ArrayList<>();

        List<String> specialList = new ArrayList<>();
        List<DrawsDTO> specialTree = new ArrayList<>();

        List<Long> dlvrList = new ArrayList<>();
        List<DrawsDTO> dlvrTree = new ArrayList<>();
        DrawsDTO tmp = null;
        DrawsDTO saveTmp = null;

        //待重构代码
        for (int i = 0; i < drawDTOsList.size(); i++) {   //临时寄存数据
            tmp = drawDTOsList.get(i);
            //处理项目数据L1
            if (!projectList.contains(tmp.getxProjectNum())) {
                projectList.add(tmp.getxProjectNum());
                saveTmp = new DrawsDTO();
                saveTmp.setxProjectNum(tmp.getxProjectNum());
                saveTmp.setObjectCode(tmp.getxProjectNum());
                saveTmp.setChildren(new ArrayList<>());
                saveTmp.setTitle(saveTmp.getObjectCode());
                saveTmp.setFolder(true);
                saveTmp.setObjectName(tmp.getProjectSName());
                saveTmp.setNodeType("xm");
                projectTree.add(saveTmp);
            }
            //处理阶段数据L2 find bug 20160731
            if (!phaseList.contains(tmp.getxProjectNum() + ":" + tmp.getxDesignPhase())) {
                phaseList.add(tmp.getxProjectNum() + ":" + tmp.getxDesignPhase());
                saveTmp = new DrawsDTO();
                saveTmp.setObjectCode(tmp.getxDesignPhase());
                saveTmp.setxProjectNum(tmp.getxProjectNum());
                saveTmp.setxDesignPhase(tmp.getxDesignPhase());
                saveTmp.setChildren(new ArrayList<>());
                saveTmp.setTitle("<div style='background: #77923c; padding: 2px; color: white; border-radius: 4px; float: left;'>阶段</div>"
                        + "<div style='float: left; padding-top: 2px;'>" + saveTmp.getObjectCode() + "</div>");
                saveTmp.setFolder(true);
                saveTmp.setObjectName(tmp.getPhaseName());
                saveTmp.setPhaseName(tmp.getPhaseName());
                saveTmp.setNodeType("jd");
                phaseTree.add(saveTmp);
            }
            //处理子项数据L3
            if (!unitTaskList.contains(tmp.getxProjectNum() + ":" + tmp.getxDesignPhase() + ":" + tmp.getxDivisionNum())) {
                unitTaskList.add(tmp.getxProjectNum() + ":" + tmp.getxDesignPhase() + ":" + tmp.getxDivisionNum());
                saveTmp = new DrawsDTO();
                saveTmp.setObjectCode(tmp.getxDivisionNum());
                saveTmp.setObjectName(tmp.getxDivision());
                saveTmp.setxProjectNum(tmp.getxProjectNum());
                saveTmp.setxDesignPhase(tmp.getxDesignPhase());
                saveTmp.setxDivisionNum(tmp.getxDivisionNum());
                saveTmp.setxDivision(tmp.getxDivision());
                saveTmp.setChildren(new ArrayList<>());
                saveTmp.setTitle("<div style='background: #FDBF01; padding: 2px; color: white; border-radius: 4px; float: left;'>子项</div>"
                        + "<div style='float: left; padding-top: 2px;'>" + saveTmp.getObjectCode() + "</div>");
                saveTmp.setFolder(true);
                saveTmp.setPhaseName(tmp.getPhaseName());
                saveTmp.setObjectName(tmp.getxDivision());
                saveTmp.setNodeType("zx");
                unitTaskTree.add(saveTmp);
            }
            //处理专业数据L4
            if (!specialList.contains(tmp.getxProjectNum() + ":" + tmp.getxDesignPhase() + ":" + tmp.getxDivisionNum() + ":" + tmp.getxSpecialty())) {
                specialList.add(tmp.getxProjectNum() + ":" + tmp.getxDesignPhase() + ":" + tmp.getxDivisionNum() + ":" + tmp.getxSpecialty());
                saveTmp = new DrawsDTO();
                saveTmp.setObjectCode(tmp.getxSpecialty());
                saveTmp.setxProjectNum(tmp.getxProjectNum());
                saveTmp.setxDesignPhase(tmp.getxDesignPhase());
                saveTmp.setxDivisionNum(tmp.getxDivisionNum());
                saveTmp.setxDivision(tmp.getxDivision());
                saveTmp.setxSpecialty(tmp.getxSpecialty());
                saveTmp.setChildren(new ArrayList<>());
                saveTmp.setTitle("<div style='background: #00AF50; padding: 2px; color: white; border-radius: 4px; float: left;'>专业</div>"
                        + "<div style='float: left; padding-top: 2px;'>" + saveTmp.getObjectCode() + "</div>");
                saveTmp.setFolder(true);
                saveTmp.setObjectName(tmp.getSpecialityName());
                saveTmp.setPhaseName(tmp.getPhaseName());
                saveTmp.setSpecialityName(tmp.getSpecialityName());
                saveTmp.setNodeType("zy");
                specialTree.add(saveTmp);
            }
            //处理工作包L5
            if (!dlvrList.contains(tmp.getxDlvrId())) {
                dlvrList.add(tmp.getxDlvrId());
                saveTmp = new DrawsDTO();
                saveTmp.setObjectCode(String.valueOf(tmp.getxDlvrId()));
                saveTmp.setObjectName(tmp.getxDlvrName());
                saveTmp.setxProjectNum(tmp.getxProjectNum());
                saveTmp.setxDesignPhase(tmp.getxDesignPhase());
                saveTmp.setxDivisionNum(tmp.getxDivisionNum());
                saveTmp.setxDivision(tmp.getxDivision());
                saveTmp.setxSpecialty(tmp.getxSpecialty());
                saveTmp.setxDlvrId(tmp.getxDlvrId());
                saveTmp.setxDlvrName(tmp.getxDlvrName());
                saveTmp.setChildren(new ArrayList<>());
                saveTmp.setTitle("<div style='background: #B2A1C6; padding: 2px; color: white; border-radius: 4px; float: left;'>工作包</div>"
                        + "<div style='float: left; padding-top: 2px;'>" + saveTmp.getObjectCode() + "</div>");
                saveTmp.setFolder(true);
                saveTmp.setObjectName(tmp.getDlvrName());
                saveTmp.setPhaseName(tmp.getPhaseName());
                saveTmp.setSpecialityName(tmp.getSpecialityName());
                saveTmp.setNodeType("gzb");
                saveTmp.setXspecialtySeq(tmp.getXspecialtySeq());
                dlvrTree.add(saveTmp);
            }
            //处理图纸
            if (tmp.getxDlvrId() != null) {
                tmp.setObjectCode(tmp.getDrawNum());
                tmp.setObjectName(tmp.getDrawName());
                if ("PJT-DRAW".equals(tmp.getdDocType())) {
                    tmp.setTitle("<div style='background: #F9BF8F; padding: 2px; color: white; border-radius: 4px; float: left;'>图号</div>"
                            + "<div style='float: left; padding-top: 2px;'>" + tmp.getObjectCode() + "</div>");
                } else if ("PJT-DESIGN-DOC".equals(tmp.getdDocType())) {
                    tmp.setTitle("<div style='background: #92CCDC; padding: 2px; color: white; border-radius: 4px; float: left;'>文本</div>"
                            + "<div style='float: left; padding-top: 2px;'>" + tmp.getObjectCode() + "</div>");
                }
                tmp.setFolder(false);
                tmp.setObjectName(tmp.getDrawName());
                tmp.setNodeType("tz");
                if (StringUtils.isNotEmpty(tmp.getxPltStatus())) {
                    tmp.setNodeStatus(tmp.getxPltStatus());
                } else if (StringUtils.isEmpty(tmp.getxPltStatus()) && StringUtils.isNotEmpty(tmp.getxCountersignStatus())) {
                    tmp.setNodeStatus(tmp.getxCountersignStatus());
                } else if (StringUtils.isEmpty(tmp.getxPltStatus()) && StringUtils.isEmpty(tmp.getxCountersignStatus()) && StringUtils.isNotEmpty(tmp.getxWpApprStatus())) {
                    tmp.setNodeStatus(tmp.getxWpApprStatus());
                } else {
                    tmp.setNodeStatus(null);
                }
                dlvrTree.get(dlvrList.indexOf(tmp.getxDlvrId())).children.add(tmp);
            }
        }

        for (int i = 0; i < dlvrTree.size(); i++) {
            tmp = dlvrTree.get(i);
            specialTree.get(specialList.indexOf(tmp.getxProjectNum() + ":" + tmp.getxDesignPhase() + ":" + tmp.getxDivisionNum() + ":" + tmp.getxSpecialty())).children.add(tmp);
        }

        for (int i = 0; i < specialTree.size(); i++) {
            tmp = specialTree.get(i);
            if (tmp.getxDivisionNum() != null) {
                unitTaskTree.get(unitTaskList.indexOf(tmp.getxProjectNum() + ":" + tmp.getxDesignPhase() + ":" + tmp.getxDivisionNum())).children.add(tmp);
            } else {
                phaseTree.get(phaseList.indexOf(tmp.getxProjectNum() + ":" + tmp.getxDesignPhase())).children.add(tmp);
            }
        }
        for (int i = 0; i < unitTaskTree.size(); i++) {
            tmp = unitTaskTree.get(i);
            phaseTree.get(phaseList.indexOf(tmp.getxProjectNum() + ":" + tmp.getxDesignPhase())).children.add(tmp);
        }
        for (int i = 0; i < phaseTree.size(); i++) {
            tmp = phaseTree.get(i);
            projectTree.get(projectList.indexOf(tmp.getxProjectNum())).children.add(tmp);
        }
        //printTree(projectTree,">");
        List<DrawsDTO> resultList = projectTree;
        return resultList;
    }

    public static void printTree(List<DrawsDTO> obj, String prefix) {
        for (DrawsDTO a : obj
                ) {
            System.out.println(prefix + " objectCode = " + a.getObjectCode() + ";");
            if (a.children != null) {
                System.out.println("children count = " + a.children.size());
                prefix = prefix + prefix;
                printTree(a.children, prefix);
            }

        }
    }

    private TmpPltEntity DTO2Entity(TmpPltDTO tmpPltDTO) {
        TmpPltEntity entity = new TmpPltEntity();
        try {
            PropertyUtils.copyProperties(entity, tmpPltDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    public List<TmpPltDTO> fillTmpPlt(ResultSet rs) throws SQLException {
        TmpPltDTO tmp = null;
        List<TmpPltDTO> results = new ArrayList();
        while (rs.next()) {
            tmp = new TmpPltDTO();
            tmp.setDid(rs.getLong("did"));
            tmp.setDrawNum(rs.getString("drawNum"));
            tmp.setReviseNum(rs.getString("reviseNum"));
            tmp.setDrawName(rs.getString("drawName"));
            tmp.setxSize(rs.getString("xSize"));
            tmp.setProjectId(rs.getLong("projectId"));
            tmp.setxProjectNum(rs.getString("xProjectNum"));
            tmp.setProjectSName(rs.getString("projectSName"));
            tmp.setxEquipment(rs.getString("xEquipment"));
            tmp.setxEquipmentNum(rs.getString("xEquipmentNum"));
            tmp.setxDesignPhase(rs.getString("xDesignPhase"));
            tmp.setxDivisionNum(rs.getString("xDivisionNum"));
            tmp.setxDivision(rs.getString("xDivision"));
            tmp.setxSpecialty(rs.getString("xSpecialty"));
            tmp.setxDlvrId(rs.getLong("xDlvrId"));
            tmp.setxDlvrName(rs.getString("xDlvrName"));
            tmp.setxEmFlag(rs.getString("xEmFlag"));
            tmp.setxDesignDate(rs.getString("xDesignDate"));
            tmp.setxWpApprStatus(rs.getString("xWpApprStatus"));
            tmp.setxCountersignStatus(rs.getString("xCountersignStatus"));
            tmp.setxPltStatus(rs.getString("xPltStatus"));
            tmp.setdReleaseDate(rs.getDate("dReleaseDate"));
            tmp.setdDocType(rs.getString("dDocType"));
            tmp.setCreationDate(rs.getDate("creationDate"));
            tmp.setLastUpdateDate(rs.getDate("lastUpdateDate"));
            tmp.setCreatedBy(rs.getLong("createdBy"));
            tmp.setLastUpdatedBy(rs.getLong("lastUpdatedBy"));
            tmp.setLastUpdateLogin(rs.getLong("lastUpdateLogin"));
            tmp.setAttributeCategory(rs.getString("attributeCategory"));
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
            tmp.setAttribute11(rs.getString("attribute11"));
            tmp.setAttribute12(rs.getString("attribute12"));
            tmp.setAttribute13(rs.getString("attribute13"));
            tmp.setAttribute14(rs.getString("attribute14"));
            tmp.setAttribute15(rs.getString("attribute15"));
            tmp.setCreatedByName(rs.getString("created_by_name"));
            results.add(tmp);
        }
        return results;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<TmpPltDTO> getTmpPlt(Long ldapId) {
        List<TmpPltDTO> resultList = (List<TmpPltDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call cux_cadi_po1_pkg.get_tmp_plt(?,?)}";   //7
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (ldapId != null)
                            cs.setLong(1, ldapId);
                        else
                            cs.setBigDecimal(1, null);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        List<TmpPltDTO> results = fillTmpPlt(rs);
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<TmpPltDTO> getDrawCatalogObj(Long did) {
        List<TmpPltDTO> resultList = (List<TmpPltDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call cux_cadi_po1_pkg.get_draw_catalog(?,?)}";   //7
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (did != null)
                            cs.setLong(1, did);
                        else
                            cs.setBigDecimal(1, null);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        List<TmpPltDTO> results = fillTmpPlt(rs);
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }


    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public List<TmpPltDTO> RefreshTmpPlt(Long ldapId,
                                         List<TmpPltDTO> tmpPltDTO) {

        List<TmpPltEntity> inData = tmpPltDTO.stream().map(tmpPlt -> {
            tmpPlt.setDateAndUser(ldapId);
            TmpPltEntity tmp = this.DTO2Entity(tmpPlt);
            return tmp;
        }).collect(Collectors.toList());
        List<TmpPltDTO> resultSet = (List<TmpPltDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call WCC_OCS.cux_cadi_po1_pkg.refresh_tmp_plt(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_CADI_TMP_PLT_ITEMS_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
                        cs.setLong(1, ldapId);
                        cs.setArray(2, vArray);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmpMap = new HashMap();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);
                        List<TmpPltDTO> results = fillTmpPlt(rs);
                        rs.close();
                        return results;
                    }
                });
        return resultSet;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public String getDrawRevision(String drawNum) {
        String resultList = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call cux_cadi_po1_pkg.get_draw_revision(?,?)}";   //2
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, drawNum);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        String results = cs.getString(2); //may be wrong
                        return results;
                    }
                });
        return resultList;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Long generateDrawsCatalog(DrawsCatalogDTO drawsCatalogDTO,
                                     String employeeNum
    ) {
        Long result = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call cux_cat_contents2_pkg.generate_cat_contents_main(?,?,?,?)}";   //4
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, drawsCatalogDTO.getDidStr());
                        cs.setString(2, drawsCatalogDTO.getIsAllEm());
                        cs.setString(3, employeeNum);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Long result = cs.getLong(4); //may be wrong
                        return result;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Long generatePrintOrder(String didStr,
                                   String pltCategory,
                                   String username
    ) {
        Long result = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.generate_plt_order_main(?,?,?,?)}";   //4
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, didStr);
                        cs.setString(2, pltCategory);
                        cs.setString(3, username);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Long rs = (Long) cs.getLong(4);
                        return rs;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    public long generateCatContentsMain(String dids, String isallem, String userNo) {
        Long result = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.Cux_Cat_Contents2_Pkg.Generate_Cat_Contents_Main(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, dids);
                        cs.setString(2, isallem);
                        cs.setString(3, userNo);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Long rs = (Long) cs.getLong(4);
                        return rs;
                    }
                });
        return result;
    }

    public String getDrawsListOrderSort(String dids, String orderBy) {
        String result = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.Cux_Cadi_Po1_Pkg.Get_Draws_List_Order_Sort(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, dids);
                        cs.setString(2, orderBy);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        StringBuffer s = new StringBuffer("");
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        while (rs.next()) {
                            s.append(rs.getString("Did")).append(",");
                        }
                        String allS = s.toString();
                        if (0 < allS.length()) {
                            allS = allS.substring(0, allS.length() - 1);
                        }
                        return allS;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public CatHeaderDTO searchCatHeaders(long id) {
        CatHeaderDTO result = (CatHeaderDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.Cux_Cat_Contents3_Pkg.Search_Cat_Headers(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        CatHeaderDTO tmp = new CatHeaderDTO();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        while (rs.next()) {
                            tmp.setCatHeaderId(rs.getLong("CAT_HEADER_ID"));
                            tmp.setProjectId(rs.getLong("PROJECT_ID"));
                            tmp.setPhaseCode(rs.getString("PHASE_CODE"));
                            tmp.setPhaseName(rs.getString("PHASE_NAME"));
                            tmp.setSpecialityClassNum(rs.getString("SPECIALITY_CLASS_NUM"));
                            tmp.setSpecialityClass(rs.getString("SPECIALITY_CLASS"));
                            tmp.setDrawingNum(rs.getString("DRAWING_NUM"));
                            tmp.setDrawingLnum(rs.getString("DRAWING_LNUM"));
                            tmp.setCatDate(rs.getString("CAT_DATE"));
                            tmp.setVersionNum(rs.getString("VERSION_NUM"));
                            tmp.setProjectNum(rs.getString("PROJECT_NUM"));
                            tmp.setProjectSname(rs.getString("PROJECT_SNAME"));
                            tmp.setProjectLname(rs.getString("PROJECT_LNAME"));
                            tmp.setProjectDname(rs.getString("PROJECT_DNAME"));
                            tmp.setParentPrjName(rs.getString("PARENT_PRJ_NAME"));
                            tmp.setParentPrjNum(rs.getString("PARENT_PRJ_NUM"));
                            tmp.setUnitTaskCode(rs.getString("UNIT_TASK_CODE"));
                            tmp.setUnitTaskName(rs.getString("UNIT_TASK_NAME"));
                            tmp.setEquipmentC(rs.getString("XEQUIPMENT_NAME"));
                            tmp.setFguId(rs.getLong("FGUID"));
                            tmp.setCatStatus(rs.getString("CAT_STATUS"));
                            tmp.setCatStatusFlag(rs.getString("CAT_STATUS_FLAG"));
                            tmp.setCustomerName(rs.getString("CUSTOMER_NAME"));
                            tmp.setTotalNum(rs.getLong("TOTAL_NUM"));
                            tmp.setCopyNum(rs.getLong("COPY_NUM"));
                            tmp.setCopyA1(rs.getLong("COPY_A1"));
                            tmp.setNewNum(rs.getLong("NEW_NUM"));
                            tmp.setTotalA1Nocat(rs.getDouble("TOTAL_A1_NOCAT"));
                            tmp.setDlvrType(rs.getString("DLVR_TYPE"));
                            tmp.setTotalPage(rs.getLong("TOTAL_PAGE"));
                            tmp.setTotalA1(rs.getDouble("TOTAL_A1"));
                            tmp.setNewA1(rs.getDouble("NEW_A1"));
                            tmp.setUnitSeq(rs.getLong("UNIT_SEQ"));
                            tmp.setManagerName(rs.getString("MANAGER_NAME"));
                        }
                        rs.close();
                        return tmp;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<CatLineDTO> searchCatLines(long id) {
        List<CatLineDTO> result = (List<CatLineDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.Cux_Cat_Contents3_Pkg.Search_Cat_Lines(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        List<CatLineDTO> results = new ArrayList<CatLineDTO>();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        CatLineDTO tmp = null;
                        while (rs.next()) {
                            tmp = new CatLineDTO();
                            tmp.setCatLineId(rs.getLong("CAT_LINE_ID"));
                            tmp.setCatHeaderId(rs.getLong("CAT_HEADER_ID"));
                            tmp.setDid(rs.getLong("DID"));
                            tmp.setCatSeq(rs.getLong("CAT_SEQ"));
                            tmp.setDdocname(rs.getString("DDOCNAME"));
                            tmp.setDdoctitle(rs.getString("DDOCTITLE"));
                            tmp.setXsize(rs.getString("XSIZE"));
                            tmp.setVersionNum(rs.getString("VERSION_NUM"));
                            tmp.setXsizeNum(rs.getString("XSIZE_NUM"));
                            tmp.setComments(rs.getString("COMMENTS"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map getSourceFileId(String infoStr, String proId) {
        Map result = (Map)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_reports_pkg.get_source_file_id(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, infoStr);
                        cs.setString(2, proId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put("p_xml_id",  cs.getLong(3));
                        tmp.put("p_rtf_id",  cs.getLong(4));
                        return tmp;
                    }
                });
        return result;
    }
    @SuppressWarnings("unchecked")
    public Map submitCatHeaders(long catHeaderId, long did, String templateCode, String tw) {
        Map result = (Map)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.Cux_Cat_Contents2_Pkg.Submit_Cat_Headers(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, catHeaderId);
                        cs.setLong(2, did);
                        cs.setString(3, templateCode);
                        cs.setString(4, tw);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getString(5));
                        tmp.put(NbccParm.DB_MSG, cs.getString(6));
                        return tmp;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<TmpPltDTO> searchTmpPlts(String userNo, Long projectId, String phaseCode, String unitTaskCode, String specialityCode, Long dlvrId, String drawStatusCode,
                                         String tw) {
        List<TmpPltDTO> resultList = (List<TmpPltDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call Cux_Cadi_Po1_Pkg.Search_Tmp_Plts(?,?,?,?,?,?,?,?,?)}";   //7
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, userNo);
                        if (projectId != null)
                            cs.setLong(2, projectId);
                        else
                            cs.setBigDecimal(2, null);
                        cs.setString(3, phaseCode);
                        cs.setString(4, unitTaskCode);
                        cs.setString(5, specialityCode);
                        if (dlvrId != null)
                            cs.setLong(6, dlvrId);
                        else
                            cs.setBigDecimal(6, null);
                        cs.setString(7, drawStatusCode);
                        cs.setString(8, tw);
                        cs.registerOutParameter(9, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(9);
                        TmpPltDTO tmp = null;
                        List<TmpPltDTO> results = new ArrayList();
                        while (rs.next()) {
                            tmp = new TmpPltDTO();
                            tmp.setDid(rs.getLong("did"));
                            tmp.setDrawNum(rs.getString("drawNum"));
                            tmp.setReviseNum(rs.getString("reviseNum"));
                            tmp.setDrawName(rs.getString("drawName"));
                            tmp.setProjectId(rs.getLong("projectId"));
                            tmp.setxProjectNum(rs.getString("xProjectNum"));
                            tmp.setProjectSName(rs.getString("projectSName"));
                            tmp.setxEquipment(rs.getString("xEquipment"));
                            tmp.setxEquipmentNum(rs.getString("xEquipmentNum"));
                            tmp.setxDesignPhase(rs.getString("xDesignPhase"));
                            tmp.setxDesignPhaseName(rs.getString("xdesign_phase_name"));
                            tmp.setxDivisionNum(rs.getString("xDivisionNum"));
                            tmp.setxDivision(rs.getString("xDivision"));
                            tmp.setxSpecialty(rs.getString("xSpecialty"));
                            tmp.setxSpecialtyName(rs.getString("xspecialty_name"));
                            tmp.setCreatedTime(rs.getString("creationDate"));
                            tmp.setCreatedBy(rs.getLong("createdBy"));
                            tmp.setCreatedByName(rs.getString("created_by_name"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    @SuppressWarnings("unchecked")
    public void deleteTmpPlts(String dids) {
        jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call Cux_Cadi_Po1_Pkg.delete_Tmp_Plts(?)}";   //7
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, dids);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return null;
                    }
                });
    }

    @SuppressWarnings("unchecked")
    public long generatePltOrderMain(String dids, String pltCategory, String userNo) {
        Long result = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.generate_plt_order_main(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, dids);
                        cs.setString(2, pltCategory);
                        cs.setString(3, userNo);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Long rs = (Long) cs.getLong(4);
                        return rs;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public CatConfigHeaderDTO searchDrawPrintInfo(long id) {
        CatConfigHeaderDTO result = (CatConfigHeaderDTO) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.Search_DrawPrint_Info(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        CatConfigHeaderDTO tmp = new CatConfigHeaderDTO();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        while (rs.next()) {
                            tmp.setPlt_Order_Header_Id(rs.getString("Plt_Order_Header_Id"));
                            tmp.setProject_Num(rs.getString("Project_Num"));
                            tmp.setProject_Name(rs.getString("Project_Name"));
                            tmp.setTask_Name(rs.getString("Task_Name"));
                            tmp.setTask_Code(rs.getString("Task_Code"));
                            tmp.setMain_Speciality(rs.getString("Main_Speciality"));
                            tmp.setPlt_Num(rs.getString("Plt_Num"));
                            tmp.setPlt_Content(rs.getString("Plt_Content"));
                            tmp.setPlt_Status(rs.getString("Plt_Status"));
                            tmp.setPlt_Category(rs.getString("Plt_Category"));
                            tmp.setPlt_Department(rs.getString("org_name"));
                            tmp.setPlt_Requestor(rs.getString("Plt_Requestor"));
                            tmp.setCreation_Date(rs.getString("Creation_Date"));
                            tmp.setProject_Res_By(rs.getString("Project_Res_By"));
                            tmp.setSpec_Manager(rs.getString("Spec_Manager"));
                            tmp.setSpec_Manager_Name(rs.getString("Spec_Manager_Name"));
                            tmp.setProject_Secretary(rs.getString("Project_Secretary_name"));
                            tmp.setEquipment_Name(rs.getString("Equipment_Name"));
                            tmp.setPrint_Channel(rs.getString("attribute5"));
                            tmp.setPlt_Requestor_Name(rs.getString("Plt_Requestor_Name"));
                            tmp.setContract_Num(rs.getString("Contract_Num"));
                            tmp.setSend_Owner(rs.getString("Send_Owner"));
                            tmp.setSg_Service(rs.getString("Sg_Service"));
                            tmp.setArchive_Flag(rs.getString("Archive_Flag"));
                            tmp.setPaper_Type(rs.getString("Paper_Type"));
                            tmp.setNew_Num(rs.getString("New_Num"));
                            tmp.setNew_Std_Num(rs.getDouble("New_Std_Num") + "");
                            tmp.setInk_Num(rs.getString("Ink_Num"));
                            tmp.setInk_Std_Num(rs.getDouble("Ink_Std_Num") + "");
                            tmp.setPrint_size(rs.getString("print_size"));
                            tmp.setHis_typeset(rs.getString("is_typeset"));
                            tmp.setBindiing_type(rs.getString("bindiing_type"));
                            tmp.setIs_inparts(rs.getString("is_inparts"));
                            tmp.setPlt_comment(rs.getString("plt_comment"));
                            tmp.setTypeset_Total_Price(rs.getString("Typeset_Total_Price"));
                            tmp.setPlt_Total_Price(rs.getString("Plt_Total_Price"));
                            tmp.setPlt_Status_Name(rs.getString("Plt_Status_Name"));
                            tmp.setPlt_Category_Name(rs.getString("Plt_Category_Name"));
                            tmp.setViewoa_Link(rs.getString("Viewoa_Link"));
                            tmp.setProject_Id(rs.getString("Project_Id"));
                        }
                        rs.close();
                        return tmp;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<CatLineDTO> searchDrawprintLines(long id) {
        List<CatLineDTO> result = (List<CatLineDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.Search_Drawprint_Lines(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        List<CatLineDTO> results = new ArrayList<CatLineDTO>();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        CatLineDTO tmp = null;
                        while (rs.next()) {
                            tmp = new CatLineDTO();
                            tmp.setDid(rs.getLong("DOC_ID"));
                            tmp.setProjectNum(rs.getString("PROJECT_NUM"));
                            tmp.setDdoctitle(rs.getString("DDOCTITLE"));
                            tmp.setDdocname(rs.getString("DDOCNAME"));
                            tmp.setPjtReviseNum(rs.getString("PJT_REVISE_NUM"));
                            tmp.setCommQty(rs.getString("COMM_QTY"));
                            tmp.setStdQty(rs.getDouble("STD_QTY") + "");
                            tmp.setTotalNum(rs.getString("TOTAL_NUM"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return result;
    }


    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<String> getPrintWayList() {
        List<String> result = (List)jdbcTemplateOne.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cadi_utils1_pkg.get_print_way_list(?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        List<String> results = new ArrayList<String>();
                        ResultSet rs = (ResultSet) cs.getObject(1);
                        while (rs.next()) {
                            results.add(rs.getString("LOOKUP_CODE") + "," + rs.getString("MEANING"));
                        }
                        rs.close();
                        return results;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<String> getProjectManagerList(long proId) {
        List<String> result = (List)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_cadi_po1_pkg.get_project_manager_list(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, proId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        List<String> results = new ArrayList<String>();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        while (rs.next()) {
                            results.add(rs.getString("USER_NAME") + "," + rs.getString("RESOURCE_NAME"));
                        }
                        rs.close();
                        return results;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    public Map submitDrawprints(long id, String pltContent, String projectResBy, String printChannel, int sendOwner, int sgService, String archiveFlag, String paperType,
                                String operationType, String userNo) {
        Map result = (Map)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.Submit_Drawprints(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.setString(2, pltContent);
                        cs.setString(3, projectResBy);
                        cs.setString(4, printChannel);
                        cs.setInt(5, sendOwner);
                        cs.setInt(6, sgService);
                        cs.setString(7, archiveFlag);
                        cs.setString(8, paperType);
                        cs.setString(9, operationType);
                        cs.setString(10, userNo);
                        cs.registerOutParameter(11, OracleTypes.VARCHAR);
                        cs.registerOutParameter(12, OracleTypes.VARCHAR);
                        cs.registerOutParameter(13, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(11));
                        tmp.put(NbccParm.DB_STATE, cs.getString(12));
                        tmp.put(NbccParm.DB_MSG, cs.getString(13));
                        return tmp;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<MyDrawsDTO> searchPltOrders(String projectNum, String xequipmentName, String xdivisionNum, String xdivisionName, String specialty, String pltNum, String pltCategory,
                                String designPhase, String pltContent, String archiveFlag, String pltStatus, String startDate, String endDate, String pltRequestorName,
                                String pltDepartment, String userNo) {
        List<MyDrawsDTO> result = (List<MyDrawsDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.Search_Plt_Orders(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, projectNum);
                        cs.setString(2, xequipmentName);
                        cs.setString(3, xdivisionNum);
                        cs.setString(4, xdivisionName);
                        cs.setString(5, specialty);
                        cs.setString(6, pltNum);
                        cs.setString(7, pltCategory);
                        cs.setString(8, designPhase);
                        cs.setString(9, pltContent);
                        cs.setString(10, archiveFlag);
                        cs.setString(11, pltStatus);
                        try {
                            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
                            if (StringUtils.isNotEmpty(startDate))
                                cs.setDate(12, new java.sql.Date(sf.parse(startDate).getTime()));
                            else
                                cs.setDate(12, null);
                            if (StringUtils.isNotEmpty(endDate))
                                cs.setDate(13, new java.sql.Date(sf.parse(endDate).getTime()));
                            else
                                cs.setDate(13, null);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        cs.setString(14, pltRequestorName);
                        cs.setString(15, pltDepartment);
                        cs.setString(16, userNo);
                        cs.registerOutParameter(17, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        List<MyDrawsDTO> results = new ArrayList<MyDrawsDTO>();
                        ResultSet rs = (ResultSet) cs.getObject(17);
                        MyDrawsDTO tmp = null;
                        while (rs.next()) {
                            tmp = new MyDrawsDTO();
                            tmp.setProjectNum(rs.getString("PROJECT_NUM"));
                            tmp.setProjectName(rs.getString("PROJECT_NAME"));
                            tmp.setEquipmentName(rs.getString("EQUIPMENT_NAME"));
                            tmp.setPhaseCode(rs.getString("PHASE_CODE"));
                            tmp.setPhaseName(rs.getString("PHASE_NAME"));
                            tmp.setTaskCode(rs.getString("TASK_CODE"));
                            tmp.setTaskName(rs.getString("TASK_NAME"));
                            tmp.setMainSpeciality(rs.getString("MAIN_SPECIALITY"));
                            tmp.setPltNum(rs.getString("PLT_NUM"));
                            tmp.setPltContent(rs.getString("PLT_CONTENT"));
                            tmp.setPltCategory(rs.getString("PLT_CATEGORY"));
                            tmp.setPltCategoryName(rs.getString("PLT_CATEGORY_NAME"));
                            tmp.setPltStatus(rs.getString("PLT_STATUS"));
                            tmp.setPltsStatusName(rs.getString("PLT_STATUS_NAME"));
                            tmp.setDocPrintPerson(rs.getString("DOC_PRINT_PERSON"));
                            tmp.setDocPrintPersonName(rs.getString("Plt_Requestor_name"));
                            tmp.setDocPrintDate(rs.getString("Creation_Date"));
                            tmp.setArchiveFlag(rs.getString("ARCHIVE_FLAG"));
                            tmp.setProjectId(rs.getString("PROJECT_ID"));
                            tmp.setPltOrderHeaderId(rs.getString("PLT_ORDER_HEADER_ID"));
                            tmp.setPaperType(rs.getString("PAPER_TYPE"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    public Map deletePltOrders(long id) {
        Map result = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.Delete_Plt_Orders(?,?,?)}";   //7
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getString(2));
                        tmp.put(NbccParm.DB_MSG, cs.getString(3));
                        return tmp;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    public long generatePltOrderAppend(String ids, String userNo) {
        Long result = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.Generate_Plt_Order_Append(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, ids);
                        cs.setString(2, userNo);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Long rs = (Long) cs.getLong(3);
                        return rs;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<String> getPrintStatusList() {
        List<String> result = (List)jdbcTemplateOne.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Cadi_Utils1_Pkg.Get_Print_Status_List(?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        List<String> results = new ArrayList<String>();
                        ResultSet rs = (ResultSet) cs.getObject(1);
                        while (rs.next()) {
                            results.add(rs.getString("LOOKUP_CODE") + "," + rs.getString("MEANING"));
                        }
                        rs.close();
                        return results;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<String> getDrawingPrintTypeList() {
        List<String> result = (List)jdbcTemplateOne.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Cadi_Utils1_Pkg.Get_Drawing_Print_Type_List(?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        List<String> results = new ArrayList<String>();
                        ResultSet rs = (ResultSet) cs.getObject(1);
                        while (rs.next()) {
                            results.add(rs.getString("LOOKUP_CODE") + "," + rs.getString("MEANING"));
                        }
                        rs.close();
                        return results;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    public Map genPjtDesignDocPltTmp(String dids, String userNo) {
        Map result = (Map)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.Cux_Cat_Contents2_Pkg.Gen_Pjt_Design_Doc_Plt_Tmp(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, dids);
                        cs.setString(2, userNo);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getString(3));
                        tmp.put(NbccParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    public Map submitTextprints(long id, String pltContent, String projectResBy, String printChannel, int sendOwner, int sgService, String archiveFlag,
                                String printSize, String hisTypeset, String bindiingType, String isInparts, String pltComment,
                                String operationType, String userNo) {
        Map result = (Map)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.submit_textprints(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.setString(2, pltContent);
                        cs.setString(3, projectResBy);
                        cs.setString(4, printChannel);
                        cs.setInt(5, sendOwner);
                        cs.setInt(6, sgService);
                        cs.setString(7, archiveFlag);
                        cs.setString(8, printSize);
                        cs.setString(9, hisTypeset);
                        cs.setString(10, bindiingType);
                        cs.setString(11, isInparts);
                        cs.setString(12, pltComment);
                        cs.setString(13, operationType);
                        cs.setString(14, userNo);
                        cs.registerOutParameter(15, OracleTypes.VARCHAR);
                        cs.registerOutParameter(16, OracleTypes.VARCHAR);
                        cs.registerOutParameter(17, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(15));
                        tmp.put(NbccParm.DB_STATE, cs.getString(16));
                        tmp.put(NbccParm.DB_MSG, cs.getString(17));
                        return tmp;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    public Map addPltRint(Long plt_print_set_id, long plt_order_header_id, String charge_item, String print_set_size, int amount, String unit, double unit_price,
                          double total_price) {
        Map result = (Map)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.modify_print_set(?,?,?,?,?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null != plt_print_set_id) {
                            cs.setLong(1, plt_print_set_id);
                        } else {
                            cs.setBigDecimal(1, null);
                        }
                        cs.setLong(2, plt_order_header_id);
                        cs.setString(3, charge_item);
                        cs.setString(4, print_set_size);
                        cs.setInt(5, amount);
                        cs.setString(6, unit);
                        cs.setDouble(7, unit_price);
                        cs.setDouble(8, total_price);
                        cs.registerOutParameter(9, OracleTypes.NUMBER);
                        cs.registerOutParameter(10, OracleTypes.VARCHAR);
                        cs.registerOutParameter(11, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_SID, cs.getLong(9));
                        tmp.put(NbccParm.DB_STATE, cs.getString(11));
                        tmp.put(NbccParm.DB_MSG, cs.getString(10));
                        return tmp;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PltPrintDTO> getPltRint(long id) {
        List<PltPrintDTO> result = (List<PltPrintDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.search_print_set(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        List<PltPrintDTO> results = new ArrayList<PltPrintDTO>();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        PltPrintDTO tmp = null;
                        while (rs.next()) {
                            tmp = new PltPrintDTO();
                            tmp.setPlt_print_set_id(rs.getLong("plt_print_set_id"));
                            tmp.setPlt_order_header_id(rs.getLong("plt_order_header_id"));
                            tmp.setCharge_item(rs.getString("charge_item"));
                            tmp.setPrint_set_size(rs.getString("print_set_size"));
                            tmp.setAmount(rs.getInt("amount"));
                            tmp.setUnit(rs.getString("unit"));
                            tmp.setUnit_price(rs.getDouble("unit_price"));
                            tmp.setTotal_price(rs.getDouble("total_price"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    public Map delPltRint(long id) {
        Map result = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.DELETE_PRINT_SET(?,?,?)}";   //7
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getString(3));
                        tmp.put(NbccParm.DB_MSG, cs.getString(2));
                        return tmp;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    public Map addPltAtt(Long p_plt_order_header_id, Long p_file_id, String p_title, String p_comments, String p_author, String p_doc_info, Timestamp creation_date) {
        Map result = (Map)jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.MODIFY_PLT_ATTACHMENT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null != p_plt_order_header_id) {
                            cs.setLong(1, p_plt_order_header_id);
                        } else {
                            cs.setBigDecimal(1, null);
                        }
                        if (null != p_file_id) {
                            cs.setLong(2, p_file_id);
                        } else {
                            cs.setBigDecimal(2, null);
                        }
                        cs.setString(3, p_title);
                        cs.setString(4, p_comments);
                        cs.setString(5, "PJT-DESIGN-DOC");
                        cs.setString(6, p_author);
                        cs.setTimestamp(7, creation_date);
                        cs.setString(8, p_doc_info);
                        cs.setString(9, "项目-设计文本");
                        cs.setString(10, "");
                        cs.setString(11, "");
                        cs.setString(12, "");
                        cs.setString(13, "");
                        cs.setString(13, "");
                        cs.registerOutParameter(14, OracleTypes.NUMBER);
                        cs.registerOutParameter(15, OracleTypes.VARCHAR);
                        cs.registerOutParameter(16, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_SID, cs.getLong(14));
                        tmp.put(NbccParm.DB_STATE, cs.getString(16));
                        tmp.put(NbccParm.DB_MSG, cs.getString(15));
                        return tmp;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<PltAttDTO> getPltAtt(long id) {
        List<PltAttDTO> result = (List<PltAttDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.SEARCH_PLT_ATTACHMENT(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        List<PltAttDTO> results = new ArrayList<PltAttDTO>();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        PltAttDTO tmp = null;
                        while (rs.next()) {
                            tmp = new PltAttDTO();
                            tmp.setPlt_order_header_id(rs.getLong("PLT_ORDER_HEADER_ID"));
                            tmp.setFile_id(rs.getLong("FILE_ID"));
                            tmp.setTitle(rs.getString("TITLE"));
                            tmp.setComments(rs.getString("COMMENTS"));
                            tmp.setDoc_type(rs.getString("DOC_TYPE"));
                            tmp.setAuthor(rs.getString("AUTHOR"));
                            tmp.setCreation_date(rs.getTimestamp("CREATION_DATE"));
                            tmp.setDoc_info(rs.getString("DOC_INFO"));
                            tmp.setAttribute1(rs.getString("ATTRIBUTE1"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    public Map delPltAtt(long id, long p_file_id) {
        Map result = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.cux_plt_order1_pkg.DELETE_PLT_ATTACHMENT(?,?,?,?)}";   //7
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.setLong(2, p_file_id);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getString(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(3));
                        return tmp;
                    }
                });
        return result;
    }

    @Transactional(readOnly = true)
    public List<PltPrintDTO> getSfList() {
        String sql = "SELECT T2.PRINT_SET_NAME, T2.PRINT_SET_SEQ, TO_CHAR(WM_CONCAT(T2.PRINT_SET_SIZE)) AS PRINT_SET_SIZE, TO_CHAR(WM_CONCAT(T2.PRINT_SET_UNIT)) AS PRINT_SET_UNIT"
                + ", TO_CHAR(WM_CONCAT(T2.PRINT_UNIT_PRICE)) AS PRINT_UNIT_PRICE"
                + " FROM (SELECT DISTINCT T.PRINT_SET_SEQ, T.PRINT_SET_NAME, T.PRINT_SET_SIZE, T.PRINT_SET_UNIT, to_char(T.PRINT_UNIT_PRICE,'fm9999999990.00') AS PRINT_UNIT_PRICE"
                + " FROM CUX_PRINT_PRICE_SET_T T ORDER BY T.PRINT_SET_SEQ) T2  GROUP BY T2.PRINT_SET_NAME, T2.PRINT_SET_SEQ ORDER BY  T2.PRINT_SET_SEQ ASC" ;
        List<PltPrintDTO> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
            public List<PltPrintDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<PltPrintDTO> result = new ArrayList<PltPrintDTO>();
                PltPrintDTO tmp = null;
                while(rs.next()) {
                    tmp = new PltPrintDTO();
                    tmp.setCharge_item(rs.getString("PRINT_SET_NAME"));
                    tmp.setPrint_set_size(rs.getString("PRINT_SET_SIZE"));
                    tmp.setUnit(rs.getString("PRINT_SET_UNIT"));
                    tmp.setUnitPrice(rs.getString("PRINT_UNIT_PRICE"));
                    result.add(tmp);
                }
                return result;
            }});
        return data;
    }
    
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTables2DTO getDesignChangeList(String dlvrIds, String ddocname, Long page, Long rows
    ) { DataTables2DTO dt = new DataTables2DTO();
        List<DesignChangeDTO> resultList = (List<DesignChangeDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call cux_cadi_po1_pkg.Get_DesignChange_List1(?,?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (dlvrIds != null)
                            cs.setString(1, dlvrIds);
                        else
                            cs.setBigDecimal(1, null);
                        if (ddocname != null)
                            cs.setString(2, ddocname);
                        else
                            cs.setBigDecimal(2, null);
                        if (page != null)
                            cs.setLong(3, page);
                        else
                            cs.setBigDecimal(3, null);
                        if (rows != null)
                            cs.setLong(4, rows);
                        else
                            cs.setBigDecimal(4, null);
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.NUMBER);
                        cs.registerOutParameter(7, OracleTypes.NUMBER);
                        cs.registerOutParameter(8, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DesignChangeDTO> results = new ArrayList<DesignChangeDTO>();
                        cs.execute();
                        dt.setTotal(cs.getLong(5));
                        dt.setPageNo(cs.getLong(6));
                        dt.setPageSize(cs.getLong(7));
                        ResultSet rs = (ResultSet) cs.getObject(8);
                        DesignChangeDTO tmp = null;
                        while (rs.next()) {
                            tmp = new DesignChangeDTO();
                            tmp.setId(rs.getLong("did"));
                            tmp.setDrawNum(rs.getString("ddocname"));
                            tmp.setReviseNum(rs.getString("xpjt_revise_num"));
                            tmp.setDrawName(rs.getString("ddoctitle"));
                            tmp.setxSize(rs.getString("xsize"));
                            tmp.setxProjectNum(rs.getString("xproject_num"));
                            tmp.setxDesignPhase(rs.getString("xdesign_phase"));
                            tmp.setxDesignPhase(rs.getString("Xdesign_Phase_Name"));
                            tmp.setxDivisionNum(rs.getString("xdivision_num"));
                            tmp.setxDivision(rs.getString("xdivision"));
                            tmp.setxSpecialty(rs.getString("xspecialty"));
                            tmp.setSpecialityName(rs.getString("xspecialty_name"));
                            tmp.setxDlvrId(rs.getLong("xdlvr_id"));
                            tmp.setxDlvrName(rs.getString("xdlvr_name"));

                            tmp.setxWpApprStatus(rs.getString("xwp_appr_status"));
                            tmp.setxCountersignStatus(rs.getString("xcountersign_status"));
                            tmp.setxPltStatus(rs.getString("xplt_status"));

                            tmp.setxEquipmentNum(rs.getString("xequipment_num"));
                            tmp.setxEquipment(rs.getString("xequipment"));

                            tmp.setXDESIGNED(rs.getString("XDESIGNED"));
                            tmp.setXDESIGNED_NAME(rs.getString("XDESIGNED_NAME"));
                            tmp.setXCHECKED(rs.getString("XCHECKED"));
                            tmp.setXCHECKED_NAME(rs.getString("XCHECKED_NAME"));
                            tmp.setXREVIEWED(rs.getString("XREVIEWED"));
                            tmp.setXREVIEWED_NAME(rs.getString("XREVIEWED_NAME"));
                            tmp.setXAPPROVED(rs.getString("XAPPROVED"));
                            tmp.setXAPPROVED_NAME(rs.getString("XAPPROVED_NAME"));

                            tmp.setXSPECIALTY_MANAGER_NAME(rs.getString("XSPECIALTY_MANAGER_NAME"));
                            tmp.setXPROJECT_MANAGER(rs.getString("XPROJECT_MANAGER"));
                            tmp.setXPROJECT_MANAGER_NAME(rs.getString("XPROJECT_MANAGER_NAME"));
                            tmp.setXregistered_Engineer(rs.getString("Xregistered_Engineer"));
                            tmp.setXregistered_Engineer_Name(rs.getString("Xregistered_Engineer_Name"));
                            tmp.setXfangan(rs.getString("xfangan"));
                            tmp.setXfangan_name(rs.getString("xfangan_name"));
                            tmp.setXspecialtySeq(rs.getString("Xspecialty_Seq"));
//                            tmp.setxOrganization(rs.getString("xorganization"));
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
    public DataTables2DTO getDesignChangeList2(String userNo,String dlvrIds, String ddocname, Long page, Long rows, Long ispm
    ) { DataTables2DTO dt = new DataTables2DTO();
        List<DesignChangeDTO> resultList = (List<DesignChangeDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call cux_cadi_po1_pkg.Get_DesignChange_List2(?,?,?,?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (dlvrIds != null)
                            cs.setString(1, dlvrIds);
                        else
                            cs.setBigDecimal(1, null);
                        if (ddocname != null)
                            cs.setString(2, ddocname);
                        else
                            cs.setBigDecimal(2, null);
                        if (page != null)
                            cs.setLong(3, page);
                        else
                            cs.setBigDecimal(3, null);
                        if (rows != null)
                            cs.setLong(4, rows);
                        else
                            cs.setBigDecimal(4, null);
                        cs.setString(5, userNo);
                        cs.setLong(6, ispm);
                        cs.registerOutParameter(7, OracleTypes.NUMBER);
                        cs.registerOutParameter(8, OracleTypes.NUMBER);
                        cs.registerOutParameter(9, OracleTypes.NUMBER);
                        cs.registerOutParameter(10, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DesignChangeDTO> results = new ArrayList<DesignChangeDTO>();
                        cs.execute();
                        dt.setTotal(cs.getLong(7));
                        dt.setPageNo(cs.getLong(8));
                        dt.setPageSize(cs.getLong(9));
                        ResultSet rs = (ResultSet) cs.getObject(10);
                        DesignChangeDTO tmp = null;
                        while (rs.next()) {
                            tmp = new DesignChangeDTO();
                            tmp.setId(rs.getLong("did"));
                            tmp.setDrawNum(rs.getString("ddocname"));
                            tmp.setReviseNum(rs.getString("xpjt_revise_num"));
                            tmp.setDrawName(rs.getString("ddoctitle"));
                            tmp.setxSize(rs.getString("xsize"));
                            tmp.setxProjectNum(rs.getString("xproject_num"));
                            tmp.setxDesignPhase(rs.getString("xdesign_phase"));
                            tmp.setxDesignPhase(rs.getString("Xdesign_Phase_Name"));
                            tmp.setxDivisionNum(rs.getString("xdivision_num"));
                            tmp.setxDivision(rs.getString("xdivision"));
                            tmp.setxSpecialty(rs.getString("xspecialty"));
                            tmp.setSpecialityName(rs.getString("xspecialty_name"));
                            tmp.setxDlvrId(rs.getLong("xdlvr_id"));
                            tmp.setxDlvrName(rs.getString("xdlvr_name"));

                            tmp.setxWpApprStatus(rs.getString("xwp_appr_status"));
                            tmp.setxCountersignStatus(rs.getString("xcountersign_status"));
                            tmp.setxPltStatus(rs.getString("xplt_status"));

                            tmp.setxEquipmentNum(rs.getString("xequipment_num"));
                            tmp.setxEquipment(rs.getString("xequipment"));

                            tmp.setXDESIGNED(rs.getString("XDESIGNED"));
                            tmp.setXDESIGNED_NAME(rs.getString("XDESIGNED_NAME"));
                            tmp.setXCHECKED(rs.getString("XCHECKED"));
                            tmp.setXCHECKED_NAME(rs.getString("XCHECKED_NAME"));
                            tmp.setXREVIEWED(rs.getString("XREVIEWED"));
                            tmp.setXREVIEWED_NAME(rs.getString("XREVIEWED_NAME"));
                            tmp.setXAPPROVED(rs.getString("XAPPROVED"));
                            tmp.setXAPPROVED_NAME(rs.getString("XAPPROVED_NAME"));

                            tmp.setXSPECIALTY_MANAGER_NAME(rs.getString("XSPECIALTY_MANAGER_NAME"));
                            tmp.setXPROJECT_MANAGER(rs.getString("XPROJECT_MANAGER"));
                            tmp.setXPROJECT_MANAGER_NAME(rs.getString("XPROJECT_MANAGER_NAME"));
                            tmp.setXregistered_Engineer(rs.getString("Xregistered_Engineer"));
                            tmp.setXregistered_Engineer_Name(rs.getString("Xregistered_Engineer_Name"));
                            tmp.setXfangan(rs.getString("xfangan"));
                            tmp.setXfangan_name(rs.getString("xfangan_name"));
                            tmp.setXspecialtySeq(rs.getString("Xspecialty_Seq"));
                            tmp.setDlvr_status(rs.getString("status"));
                            tmp.setDlvr_status_code(rs.getString("status_code"));
                            tmp.setDlvr_version(rs.getLong("dlvr_version"));
//                            tmp.setxOrganization(rs.getString("xorganization"));
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
    public long generateCatContentsMain1(String dids, String isallem, Long ispm, String userNo) {
        Long result = (Long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call WCC_OCS.Cux_Cat_Contents3_Pkg.Generate_Cat_Contents_Main(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, dids);
                        if (isallem != null)
                            cs.setString(2, isallem);
                        else
                            cs.setBigDecimal(2, null);
                        cs.setString(3, userNo);
                        cs.setLong(4, ispm);
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Long rs = (Long) cs.getLong(5);
                        return rs;
                    }
                });
        return result;
    }

    /**
     *
     * @param DrawSizeDTOs
     * @return
     *
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<DrawSizeReturnDTO> DrawSize(List<DrawSizeDTO> DrawSizeDTOs) {
        //contractApLineDTO.setDateAndUser(userId);
        List<DrawSizeEntity> inData = DrawSizeDTOs.stream().map(mile -> {
            DrawSizeEntity tmp = this.cdtoToEntity(mile);
            return tmp;
        }).collect(Collectors.toList());
        List<DrawSizeReturnDTO> result = (List<DrawSizeReturnDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call wcc_ocs.cux_cat_contents3_pkg.search_drawlist_size(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("WCC_OCS.CAT_DRAWLIST_SIZE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
                        cs.setArray(1, vArray);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DrawSizeReturnDTO> results = new ArrayList<>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        DrawSizeReturnDTO tmp = null;
                        while (rs.next()) {
                            tmp = new DrawSizeReturnDTO();
                            tmp.setIndex(rs.getLong("index_ID"));
                            tmp.setDrawnum(rs.getString("DRAWING_NUM"));
                            tmp.setPages(rs.getLong("TOTAL_PAGE"));
                            tmp.setXsize(rs.getString("xsize"));
                            tmp.setDid(rs.getLong("did"));
                            tmp.setVersion_num(rs.getLong("version_num"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return result;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<CHUTULIANG1DTO> getChutuliangList1(String ProjectNum, String SpecClassNum, String userNo, String DesignedID, String FromDate, String ToDate, Long Code
    ) {
        List<CHUTULIANG1DTO> resultList = (List<CHUTULIANG1DTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call wcc_ocs.Cux_Cat_Contents3_Pkg.chutuliang(?,?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (ProjectNum != null)
                            cs.setString(1, ProjectNum);
                        else
                            cs.setBigDecimal(1, null);
                        if (SpecClassNum != null)
                            cs.setString(2, SpecClassNum);
                        else
                            cs.setBigDecimal(2, null);
                        cs.setString(3, userNo);
                        if (DesignedID != null)
                            cs.setString(4, DesignedID);
                        else
                            cs.setBigDecimal(4, null);
                        if (FromDate != null)
                            cs.setString(5, FromDate);
                        else
                            cs.setBigDecimal(5, null);
                        if (ToDate != null)
                            cs.setString(6, ToDate);
                        else
                            cs.setBigDecimal(6, null);
                        cs.setLong(7, Code);
                        cs.registerOutParameter(8, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<CHUTULIANG1DTO> results = new ArrayList<>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);
                        CHUTULIANG1DTO tmp = null;
                        while (rs.next()) {
                            tmp = new CHUTULIANG1DTO();
                            tmp.setProject_num(rs.getString("project_num"));
                            tmp.setProject_name(rs.getString("project_name"));
                            tmp.setDesigned_name(rs.getString("designed_name"));
                            tmp.setZrzs(rs.getLong("zrzs"));
                            tmp.setA1(rs.getLong("a1"));
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
    public List<CHUTULIANG2DTO> getChutuliangList2(String ProjectNum, String SpecClassNum, String userNo, Long Code
    ) {
        List<CHUTULIANG2DTO> resultList = (List<CHUTULIANG2DTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call wcc_ocs.Cux_Cat_Contents3_Pkg.chutuliang(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (ProjectNum != null)
                            cs.setString(1, ProjectNum);
                        else
                            cs.setBigDecimal(1, null);
                        if (SpecClassNum != null)
                            cs.setString(2, SpecClassNum);
                        else
                            cs.setBigDecimal(2, null);
                        cs.setString(3, userNo);
                        cs.setBigDecimal(4, null);
                        cs.setLong(5, Code);
                        cs.registerOutParameter(6, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<CHUTULIANG2DTO> results = new ArrayList<>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);
                        CHUTULIANG2DTO tmp = null;
                        while (rs.next()) {
                            tmp = new CHUTULIANG2DTO();
                            tmp.setProject_num(rs.getString("project_num"));
                            tmp.setProject_name(rs.getString("project_name"));
                            tmp.setSpecialty_name(rs.getString("specialty_name"));
                            tmp.setZrzs(rs.getLong("zrzs"));
                            tmp.setA1(rs.getLong("a1"));
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
    public List<CHUTULIANG3DTO> getChutuliangList3(String ProjectNum, String userNo, Long Code
    ) {
        List<CHUTULIANG3DTO> resultList = (List<CHUTULIANG3DTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call wcc_ocs.Cux_Cat_Contents3_Pkg.chutuliang(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (ProjectNum != null)
                            cs.setString(1, ProjectNum);
                        else
                            cs.setBigDecimal(1, null);
                        cs.setBigDecimal(2, null);
                        cs.setString(3, userNo);
                        cs.setBigDecimal(4, null);
                        cs.setLong(5, Code);
                        cs.registerOutParameter(6, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<CHUTULIANG3DTO> results = new ArrayList<>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(6);
                        CHUTULIANG3DTO tmp = null;
                        while (rs.next()) {
                            tmp = new CHUTULIANG3DTO();
                            tmp.setProject_num(rs.getString("project_num"));
                            tmp.setProject_name(rs.getString("project_name"));
                            tmp.setOrganization_name(rs.getString("organization_name"));
                            tmp.setZrzs(rs.getLong("zrzs"));
                            tmp.setA1(rs.getLong("a1"));
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
    public List<DisprojectListDTO> getDISProjectList(String projectId
    ) {
        List<DisprojectListDTO> resultList = (List<DisprojectListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call wcc_ocs.cux_cat_contents3_pkg.get_project_list(?)}";   //3
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DisprojectListDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);
                        DisprojectListDTO tmp = null;
                        while (rs.next()) {
                            tmp = new DisprojectListDTO();
                            tmp.setDisname(rs.getString("disname"));
                            tmp.setName(rs.getString("name"));
                            tmp.setProject_id(rs.getString("project_id"));
                            tmp.setSegment1(rs.getString("segment1"));
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
    public List<DisNameListDTO> getDISNameList(String userNo
    ) {
        List<DisNameListDTO> resultList = (List<DisNameListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call wcc_ocs.cux_cat_contents3_pkg.Get_DesignName_List(?,?)}";   //3
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DisNameListDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        cs.setString(1, userNo);
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        DisNameListDTO tmp = null;
                        while (rs.next()) {
                            tmp = new DisNameListDTO();
                            tmp.setDname(rs.getString("dname"));
                            tmp.setDfullname(rs.getString("dfullname"));
                            tmp.setOrganizationcode(rs.getString("organizationcode"));
                            tmp.setDISNAME(rs.getString("DISNAME"));
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
    public DataTables2DTO getDesignChangeList4(String requestids, Long page, Long rows
    ) { DataTables2DTO dt = new DataTables2DTO();
        List<DesignChangeDTO> resultList = (List<DesignChangeDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call cux_cadi_po1_pkg.Get_DesignChange_List4(?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (requestids != null)
                            cs.setString(1, requestids);
                        else
                            cs.setBigDecimal(1, null);
                        if (page != null)
                            cs.setLong(2, page);
                        else
                            cs.setBigDecimal(2, null);
                        if (rows != null)
                            cs.setLong(3, rows);
                        else
                            cs.setBigDecimal(3, null);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.NUMBER);
                        cs.registerOutParameter(7, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<DesignChangeDTO> results = new ArrayList<DesignChangeDTO>();
                        cs.execute();
                        dt.setTotal(cs.getLong(4));
                        dt.setPageNo(cs.getLong(5));
                        dt.setPageSize(cs.getLong(6));
                        ResultSet rs = (ResultSet) cs.getObject(7);
                        DesignChangeDTO tmp = null;
                        while (rs.next()) {
                            tmp = new DesignChangeDTO();
                            tmp.setId(rs.getLong("did"));
                            tmp.setDrawNum(rs.getString("ddocname"));
                            tmp.setReviseNum(rs.getString("xpjt_revise_num"));
                            tmp.setDrawName(rs.getString("ddoctitle"));
                            tmp.setxSize(rs.getString("xsize"));
                            tmp.setxProjectNum(rs.getString("xproject_num"));
                            tmp.setxDesignPhase(rs.getString("xdesign_phase"));
                            tmp.setxDesignPhase(rs.getString("Xdesign_Phase_Name"));
                            tmp.setxDivisionNum(rs.getString("xdivision_num"));
                            tmp.setxDivision(rs.getString("xdivision"));
                            tmp.setxSpecialty(rs.getString("xspecialty"));
                            tmp.setSpecialityName(rs.getString("xspecialty_name"));
                            tmp.setxDlvrId(rs.getLong("xdlvr_id"));
                            tmp.setxDlvrName(rs.getString("xdlvr_name"));

                            tmp.setxWpApprStatus(rs.getString("xwp_appr_status"));
                            tmp.setxCountersignStatus(rs.getString("xcountersign_status"));
                            tmp.setxPltStatus(rs.getString("xplt_status"));

                            tmp.setxEquipmentNum(rs.getString("xequipment_num"));
                            tmp.setxEquipment(rs.getString("xequipment"));

                            tmp.setXDESIGNED(rs.getString("XDESIGNED"));
                            tmp.setXDESIGNED_NAME(rs.getString("XDESIGNED_NAME"));
                            tmp.setXCHECKED(rs.getString("XCHECKED"));
                            tmp.setXCHECKED_NAME(rs.getString("XCHECKED_NAME"));
                            tmp.setXREVIEWED(rs.getString("XREVIEWED"));
                            tmp.setXREVIEWED_NAME(rs.getString("XREVIEWED_NAME"));
                            tmp.setXAPPROVED(rs.getString("XAPPROVED"));
                            tmp.setXAPPROVED_NAME(rs.getString("XAPPROVED_NAME"));

                            tmp.setXSPECIALTY_MANAGER_NAME(rs.getString("XSPECIALTY_MANAGER_NAME"));
                            tmp.setXPROJECT_MANAGER(rs.getString("XPROJECT_MANAGER"));
                            tmp.setXPROJECT_MANAGER_NAME(rs.getString("XPROJECT_MANAGER_NAME"));
                            tmp.setXregistered_Engineer(rs.getString("Xregistered_Engineer"));
                            tmp.setXregistered_Engineer_Name(rs.getString("Xregistered_Engineer_Name"));
                            tmp.setXfangan(rs.getString("xfangan"));
                            tmp.setXfangan_name(rs.getString("xfangan_name"));
                            tmp.setXspecialtySeq(rs.getString("Xspecialty_Seq"));
                            tmp.setDlvr_status(rs.getString("status"));
                            tmp.setDlvr_status_code(rs.getString("status_code"));
                            tmp.setDlvr_version(rs.getLong("dlvr_version"));
//                            tmp.setxOrganization(rs.getString("xorganization"));
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
    public void InsertCuxQueuesDlvr(String dlvrs) {
        jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call Cux_Ecm_Queue_Sync_Pkg.Insert_Cux_Queues_Dlvr(?)}";   //7
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, dlvrs);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return null;
                    }
                });
    }



}
