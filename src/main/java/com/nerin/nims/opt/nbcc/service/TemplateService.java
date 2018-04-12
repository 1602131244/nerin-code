package com.nerin.nims.opt.nbcc.service;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.util.BusinessException;
import com.nerin.nims.opt.base.util.BusinessExceptionParm;
import com.nerin.nims.opt.base.util.FileUtil;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.*;
import com.nerin.nims.opt.nbcc.module.TemplateChaptersEntity;
import com.nerin.nims.opt.nbcc.module.TemplateHeaderEntity;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
import oracle.sql.*;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
 * Created by yinglgu on 6/17/2016.
 */
@Component
@Transactional
public class TemplateService {

    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("secondaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate2;

    public int isWithXml(Object o, int type) {
        String xmlCode = "";
        String taskChapterFileName = "";
        String templateCode = "";
        String templateApplShortCode = "";
        // 模板
        String tempChapterFileName = "";
        String nlsLanguage = "";
        String nlsTerritory = "";
        if (0 == type) {
            TemplateChaptersDTO dto = (TemplateChaptersDTO) o;
            templateCode = dto.getTemplateCode();
            templateApplShortCode = dto.getTemplateApplShortCode();
            tempChapterFileName = dto.getTemplateChapterFileName();
            nlsLanguage = dto.getNLSLanguage();
            nlsTerritory = dto.getNLSTerritory();
            if (StringUtils.isNotEmpty(tempChapterFileName) && StringUtils.isNotEmpty(templateCode)
                    && StringUtils.isNotEmpty(templateApplShortCode) && StringUtils.isNotEmpty(nlsLanguage) && StringUtils.isNotEmpty(nlsTerritory)) {
                return 1;
            } else {
                return 0;
            }
        } else if (1 == type) {
            TaskChaptersDto dto = (TaskChaptersDto) o;
            xmlCode = dto.getXmlCode();
            taskChapterFileName = dto.getTaskChapterFileName();
            templateCode = dto.getTemplateCode();
            templateApplShortCode = dto.getTemplateApplShortCode();
            if (StringUtils.isNotEmpty(xmlCode) && StringUtils.isNotEmpty(taskChapterFileName)
                    && StringUtils.isNotEmpty(templateCode) && StringUtils.isNotEmpty(templateApplShortCode)) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    public void autoCreateWordWithXml(Object o, long taskId, long proId, int type, String localPath) {
        // 任务
        String xmlCode = "";
        String processXdoTemplate = "processXdoTemplate";
        String taskHeaderId = "";
        String projectId = "";
        String taskChapterFileName = "";
        String templateCode = "";
        String templateApplShortCode = "";
        String chapterId = "";
        // 模板
        String tempChapterFileName = "";
        String nlsLanguage = "";
        String nlsTerritory = "";
        if (0 == type) {
            processXdoTemplate = "getTemplateSourceFile";
            TemplateChaptersDTO dto = (TemplateChaptersDTO) o;
            templateCode = dto.getTemplateCode();
            templateApplShortCode = dto.getTemplateApplShortCode();
            tempChapterFileName = dto.getTemplateChapterFileName();
            nlsLanguage = dto.getNLSLanguage();
            nlsTerritory = dto.getNLSTerritory();
            chapterId = dto.getChapterId() + "";
            // 只有param1~5不为空的时候，才去生成word
            if (StringUtils.isNotEmpty(tempChapterFileName) && StringUtils.isNotEmpty(templateCode)
                    && StringUtils.isNotEmpty(templateApplShortCode) && StringUtils.isNotEmpty(nlsLanguage) && StringUtils.isNotEmpty(nlsTerritory)) {
                String rtfNameUrl = this.createXMLForRtf(processXdoTemplate, tempChapterFileName, templateCode, templateApplShortCode, nlsLanguage, nlsTerritory);
                if (StringUtils.isNotEmpty(rtfNameUrl))
                    this.downloadRTF(rtfNameUrl, localPath, chapterId);
            }
        } else if (1 == type) {
            TaskChaptersDto dto = (TaskChaptersDto) o;
            xmlCode = dto.getXmlCode();
            taskHeaderId = taskId + "";
            projectId = proId + "";
            taskChapterFileName = dto.getTaskChapterFileName();
            templateCode = dto.getTemplateCode();
            templateApplShortCode = dto.getTemplateApplShortCode();
            chapterId = dto.getChapterId() + "";
            // 只有param3~5不为空的时候，才去生成word
            if (StringUtils.isNotEmpty(xmlCode) && StringUtils.isNotEmpty(taskChapterFileName)
                    && StringUtils.isNotEmpty(templateCode) && StringUtils.isNotEmpty(templateApplShortCode)) {
                String rtfNameUrl = this.createXMLForRtf(processXdoTemplate, taskHeaderId, projectId, taskChapterFileName, templateCode, templateApplShortCode);
                if (StringUtils.isNotEmpty(rtfNameUrl))
                    this.downloadRTF(rtfNameUrl, localPath, chapterId);
            }
        }
    }
    //创建根据XML模板创建文件
    public void autoCreateWordWithXmlNoCopy(Object o, long taskId, long proId, int type, String localPath) {
        // 任务
        String xmlCode = "";
        String processXdoTemplate = "processXdoTemplate";
        String taskHeaderId = "";
        String projectId = "";
        String taskChapterFileName = "";
        String templateCode = "";
        String templateApplShortCode = "";
        String chapterId = "";
        // 模板
        String tempChapterFileName = "";
        String nlsLanguage = "";
        String nlsTerritory = "";
        if (0 == type) {
            processXdoTemplate = "getTemplateSourceFile";
            TemplateChaptersDTO dto = (TemplateChaptersDTO) o;
            templateCode = dto.getTemplateCode();
            templateApplShortCode = dto.getTemplateApplShortCode();
            tempChapterFileName = dto.getTemplateChapterFileName();
            nlsLanguage = dto.getNLSLanguage();
            nlsTerritory = dto.getNLSTerritory();
            chapterId = dto.getChapterId() + "";
            // 只有param1~5不为空的时候，才去生成word
            if (StringUtils.isNotEmpty(tempChapterFileName) && StringUtils.isNotEmpty(templateCode)
                    && StringUtils.isNotEmpty(templateApplShortCode) && StringUtils.isNotEmpty(nlsLanguage) && StringUtils.isNotEmpty(nlsTerritory)) {
                String rtfNameUrl = this.createXMLForRtf(processXdoTemplate, tempChapterFileName, templateCode, templateApplShortCode, nlsLanguage, nlsTerritory);
                if (StringUtils.isNotEmpty(rtfNameUrl))
                    this.downloadRTF(rtfNameUrl, localPath, chapterId);
            }
        } else if (1 == type) {
            TaskChaptersDto dto = (TaskChaptersDto) o;
            xmlCode = dto.getXmlCode();
            taskHeaderId = taskId + "";
            projectId = proId + "";
            taskChapterFileName = dto.getTaskChapterFileName();
            templateCode = dto.getTemplateCode();
            templateApplShortCode = dto.getTemplateApplShortCode();
            chapterId = dto.getChapterId() + "";
            // 只有param3~5不为空的时候，才去生成word
            if (StringUtils.isNotEmpty(xmlCode) && StringUtils.isNotEmpty(taskChapterFileName)
                    && StringUtils.isNotEmpty(templateCode) && StringUtils.isNotEmpty(templateApplShortCode)) {
                String rtfNameUrl = this.createXMLForRtf(processXdoTemplate, taskHeaderId, projectId, taskChapterFileName, templateCode, templateApplShortCode);
//                if (StringUtils.isNotEmpty(rtfNameUrl))
//                    this.downloadRTF(rtfNameUrl, localPath, chapterId);
            }
        }
    }

    private String createXMLForRtf(String method, String param1, String param2, String param3, String param4, String param5) {
        HttpClient httpClient = new DefaultHttpClient();
        CloseableHttpResponse response = null;
        try {
            String url = nerinProperties.getNbcc().getEbsServer() + "OA_HTML/getXdo.jsp?method=" + method + "&param1=" + param1 + "&param2=" + param2 + "&param3=" + param3
                    + "&param4=" + param4 + "&param5=" + param5;
            HttpGet getMethod = new HttpGet(url);
            getMethod.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
            response = (CloseableHttpResponse) httpClient.execute(getMethod);
            if(response.getStatusLine().getStatusCode() == 200) {
                HttpEntity repEntity = response.getEntity();
                String content = EntityUtils.toString(repEntity);
                content = content.trim();
                if (0 <= content.indexOf("rtf"))
                    return nerinProperties.getNbcc().getEbsServer() + "OA_HTML" + content.split("OA_HTML")[1];
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private void downloadRTF(String remoteUrl, String localPath, String chapterId) {
        FileUtil.downloadFile(remoteUrl, localPath + chapterId + ".rtf");
    }

    /**
     * 检测服务器文件是否存在
     * @param fileName
     * @return
     */
    public long checkFile(String fileName) {
        HttpClient httpClient = new DefaultHttpClient();
        CloseableHttpResponse response = null;
        try {
            String url = nerinProperties.getNbcc().getEbsServer() + "OA_HTML/cux_cooperate/checkFile.jsp?filename=" + fileName;
            HttpGet getMethod = new HttpGet(url);

            getMethod.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.111 Safari/537.36");
            response = (CloseableHttpResponse) httpClient.execute(getMethod);
            if(response.getStatusLine().getStatusCode() == 200) {
                HttpEntity repEntity = response.getEntity();
                String content = EntityUtils.toString(repEntity);
                content = content.trim();
                if (0 <= content.indexOf("true"))
                    return 1;
                else
                    return 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }


    private TemplateHeaderEntity dtoToEntity(TemplateHeaderDTO templateHeaderDTO) {
        TemplateHeaderEntity entity = new TemplateHeaderEntity();
        try {
            PropertyUtils.copyProperties(entity, templateHeaderDTO);
            entity.setTemplateHeadeId(templateHeaderDTO.getTemplateId());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private TemplateChaptersEntity cdtoToEntity(TemplateChaptersDTO chaptersDTO) {
        TemplateChaptersEntity entity = new TemplateChaptersEntity();
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

//    public void test() {
//        List<UkbnovCTCOrder> orderList = new ArrayList<UkbnovCTCOrder>();
//        for(int i=0;i<100000;i++){
//            orderList.add(new UkbnovCTCOrder("losingLEName"+i,"losingLECode+"+i));
//        }
//
//        jdbcTemplate.execute(
//                new CallableStatementCreator() {
//                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
//                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
//                        StructDescriptor recDesc = StructDescriptor.createDescriptor("BUT_UKBNOV_CTC_ORDER_REC", or);
//                        ArrayList<STRUCT> pstruct = new ArrayList<STRUCT>();
//                        for (UkbnovCTCOrder ord:orderList) {
//                            Object[] record = new Object[2];
//                            record[0] = ord.getLosingLEName();
//                            record[1] = ord.getLosingLECode();
//                            STRUCT item = new STRUCT(recDesc, or, record);
//                            pstruct.add(item);
//                        }
//                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("BUT_UKBNOV_CTC_ORDER_TAB", or);
//                        ARRAY vArray = new ARRAY(tabDesc, or, pstruct.toArray());
//                        CallableStatement cs = con.prepareCall("{call bulkInsertCTDORDER(?)}");
//                        cs.setArray(1, vArray);
//                        return cs;
//                    }
//                }, new CallableStatementCallback() {
//                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
//                        Map tmp = new HashMap();
//                        cs.execute();
//                        //cs.getConnection().rollback();
//                        return tmp;
//                    }
//                });
//    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map addOrUpTemplateHeader(TemplateHeaderDTO templateHeaderDTO, Long ldapId) {
        templateHeaderDTO.setDateAndUser(ldapId);
        List<TemplateHeaderEntity> dataList = new ArrayList<TemplateHeaderEntity>();
        dataList.add(this.dtoToEntity(templateHeaderDTO));
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.insert_nbcc_template_header(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.NBCC_TEMPLATE_HEADER_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(NbccParm.DB_SID, cs.getLong(2));
                        tmp.put(NbccParm.DB_STATE, cs.getLong(3));
                        tmp.put(NbccParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map addOrUpTemplateChapters(List<TemplateChaptersDTO> chaptersDTOs, Long ldapId) {
        List<TemplateChaptersEntity> inData = chaptersDTOs.stream().map(chapter -> {
            chapter.setDateAndUser(ldapId);
            TemplateChaptersEntity tmp = this.cdtoToEntity(chapter);
            return tmp;
        }).collect(Collectors.toList());
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.insert_nbcc_template_chapter(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.NBCC_TEMPLATE_CHAPTER_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
                        cs.setArray(1, vArray);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(NbccParm.DB_STATE, cs.getLong(2));
                        tmp.put(NbccParm.DB_MSG, cs.getString(3));
                        return tmp;
                    }
                });
        return map;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getTemplateHeaderList(String taskTypes, String templateStatus, String templateNameOrDesc, Long templateHeaderId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<TemplateHeaderDTO> resultList = (List<TemplateHeaderDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_nbcc_template_headers(?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, taskTypes);// 设置输入参数的值
                        cs.setString(2, templateStatus);// 设置输入参数的值
                        cs.setString(3, templateNameOrDesc);// 设置输入参数的值
                        if (null == templateHeaderId)
                            cs.setBigDecimal(4, null);
                        else
                            cs.setLong(4, templateHeaderId);
                        cs.setLong(5, pageNo);// 设置输入参数的值
                        cs.setLong(6, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(7, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(8, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TemplateHeaderDTO> results = new ArrayList<TemplateHeaderDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(7);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(8));
                        TemplateHeaderDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TemplateHeaderDTO();
                            tmp.setTemplateId(rs.getLong("TEMPLATE_HEADER_ID"));
                            tmp.setTemplateName(rs.getString("TEMPLATE_NAME"));
                            tmp.setTemplateDescription(rs.getString("TEMPLATE_DESCRIPTION"));
                            tmp.setTemplateStatus(rs.getString("TEMPLATE_STATUS"));
                            tmp.setTemplateStatusName(rs.getString("TEMPLATE_STATUS_NAME"));
                            tmp.setTaskType(rs.getString("TASK_TYPE"));
                            tmp.setTaskTypeName(rs.getString("TASK_TYPE_NAME"));
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
    public DataTablesDTO getTemplateStatusList(String code) {
        DataTablesDTO dt = new DataTablesDTO();
        List<TemplateStatusDTO> resultList = (List<TemplateStatusDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_nbcc_status(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, code);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TemplateStatusDTO> results = new ArrayList<TemplateStatusDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        dt.setDataTotal(cs.getLong(3));
                        TemplateStatusDTO tmp = null;
                        while (rs.next()) {
                            tmp = new TemplateStatusDTO();
                            tmp.setTemplateStatus(rs.getString("templateStatus"));
                            tmp.setTemplateStatusName(rs.getString("templateStatusName"));
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
    public DataTablesDTO getTaskTypeList(String code,String projectPhase) {
        DataTablesDTO dt = new DataTablesDTO();
        List<TaskTypeDTO> resultList = (List<TaskTypeDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_nbcc_task_type(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, code);
                        cs.setString(2, projectPhase);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TaskTypeDTO> results = new ArrayList<TaskTypeDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);
                        dt.setDataTotal(cs.getLong(4));
                        TaskTypeDTO tmp = null;
                        while (rs.next()) {
                            tmp = new TaskTypeDTO();
                            tmp.setTaskType(rs.getString("taskType"));
                            tmp.setTaskTypeName(rs.getString("taskTypeName"));
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
    public DataTablesDTO getProjectRoleList(Long id, Long projectId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<ProjectRoleDTO> resultList = (List<ProjectRoleDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_proj_role(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == id)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, id);
                        if (null == projectId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, projectId);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<ProjectRoleDTO> results = new ArrayList<ProjectRoleDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);
                        dt.setDataTotal(cs.getLong(4));
                        ProjectRoleDTO tmp = null;
                        while (rs.next()) {
                            tmp = new ProjectRoleDTO();
                            tmp.setProjectRoleId(rs.getString("projectRoleId"));
                            tmp.setProjectRoleName(rs.getString("projectRoleName"));
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
    public DataTablesDTO getSpecialtyList(String id,Long projectId) {
        DataTablesDTO dt = new DataTablesDTO();
        List<SpecialtyDTO> resultList = (List<SpecialtyDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_specialty(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, id);
                        if (null == projectId)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, projectId);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<SpecialtyDTO> results = new ArrayList<SpecialtyDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);
                        dt.setDataTotal(cs.getLong(4));
                        SpecialtyDTO tmp = null;
                        while (rs.next()) {
                            tmp = new SpecialtyDTO();
                            tmp.setSpecialty(rs.getString("specialty"));
                            tmp.setSpecialtyName(rs.getString("specialtyName"));
                            tmp.setStartDate(rs.getString("startdate"));
                            tmp.setEndDate(rs.getString("enddate"));
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
    public DataTablesDTO getSystemUiList(String id) {
        DataTablesDTO dt = new DataTablesDTO();
        List<SystemUiDTO> resultList = (List<SystemUiDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_system_link(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, id);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<SystemUiDTO> results = new ArrayList<SystemUiDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        dt.setDataTotal(cs.getLong(3));
                        SystemUiDTO tmp = null;
                        while (rs.next()) {
                            tmp = new SystemUiDTO();
                            tmp.setSystemUiCode(rs.getString("systemUiCode"));
                            tmp.setSystemUiName(rs.getString("systemUiName"));
                            tmp.setSystemUiUrl(rs.getString("systemUiUrl"));
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
    public DataTablesDTO getXmlTemplateList(String id) {
        DataTablesDTO dt = new DataTablesDTO();
        List<XmlTemplateDTO> resultList = (List<XmlTemplateDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_xdo_template(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, id);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<XmlTemplateDTO> results = new ArrayList<XmlTemplateDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        dt.setDataTotal(cs.getLong(3));
                        XmlTemplateDTO tmp = null;
                        while (rs.next()) {
                            tmp = new XmlTemplateDTO();
                            tmp.setXdoTemplateId(rs.getString("XDO_TEMPLATE_ID"));
                            tmp.setXdoTemplateCode(rs.getString("XDO_TEMPLATE_CODE"));
                            tmp.setXdoTemplateName(rs.getString("XDO_TEMPLATE_NAME"));
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
    public int checkTemplateName(String name, Long id) {
        int i = (int) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.check_nbcc_template_name(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == id)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, id);
                        cs.setString(2, name);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
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

    @Transactional(rollbackFor = Exception.class)
    public Map updateTemplateStatus(long templateId, String templateStatus) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.update_nbcc_template_status(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, templateId);
                        cs.setString(2, templateStatus);
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

    @Transactional(rollbackFor = Exception.class)
    public Map delTemplate(String templateIds) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.dele_nbcc_template(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, templateIds);
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
    public Map saveAsTemplate(TemplateHeaderDTO templateHeaderDTO, Long ldapId) throws BusinessException {
        Map tMap = this.addOrUpTemplateHeader(templateHeaderDTO, ldapId);
        if (1 != (Long) tMap.get(NbccParm.DB_STATE)) {
            return tMap;
        }
        Map cMap = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.copy_nbcc_template_chapter(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, templateHeaderDTO.getFromTemplateId());
                        cs.setLong(2, (long) tMap.get(NbccParm.DB_SID));
                        cs.setLong(3, ldapId);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp1 = new HashMap();
                        tmp1.put(NbccParm.DB_STATE, cs.getLong(4));
                        tmp1.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp1;
                    }
                });
        if (1 != (Long) cMap.get(NbccParm.DB_STATE)) {
            throw new BusinessException(BusinessExceptionParm.DB_PACKAGE_ERROR[0], BusinessExceptionParm.DB_PACKAGE_ERROR[1] + "另存NBCC模板章节");
        } else {
            return tMap;
        }
    }

    @SuppressWarnings("unchecked")
    public DataTablesDTO getTemplateChaptersList_2(long templateId, String lineType, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<TemplateChaptersDTO> resultList = (List<TemplateChaptersDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.get_nbcc_template_chapters(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, templateId);
                        cs.setString(2, lineType);
                        cs.setLong(3, pageNo);
                        cs.setLong(4, pageSize);
                        cs.registerOutParameter(5, OracleTypes.CURSOR);
                        cs.registerOutParameter(6, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<TemplateChaptersDTO> results = new ArrayList<TemplateChaptersDTO>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(6));
                        TemplateChaptersDTO tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TemplateChaptersDTO();
                            tmp.setChapterId(rs.getLong("CHAPTER_ID"));
                            tmp.setParentChapterId(rs.getLong("PARENT_CHAPTER_ID"));
                            tmp.setParentChapterNo(rs.getString("PARENT_CHAPTER_NO"));
                            tmp.setTemplateHeaderId(rs.getLong("TEMPLATE_HEADER_ID"));
                            tmp.setChapterNo(rs.getString("CHAPTER_NO"));
                            tmp.setChapterName(rs.getString("CHAPTER_NAME"));
                            tmp.setProjectRoleId(rs.getLong("PROJECT_ROLE_ID"));
                            tmp.setProjectRoleName(rs.getString("PROJECT_ROLE_NAME"));
                            tmp.setXdoTemplateId(rs.getLong("XDO_TEMPLATE_ID"));
                            tmp.setXdoTemplateName(rs.getString("XDO_TEMPLATE_NAME"));
                            tmp.setSystemLink(rs.getString("SYSTEM_LINK"));
                            tmp.setSystemLinkName(rs.getString("SYSTEM_LINK_NAME"));
                            tmp.setSystemLinkUrl(rs.getString("SYSTEM_LINK_URL"));
                            tmp.setSpecialty(rs.getString("SPECIALTY"));
                            tmp.setSpecialtyName(rs.getString("SPECIALTY_NAME"));
                            tmp.setPublicFlag(rs.getLong("PUBLIC_FLAG"));
                            tmp.setDeleteFlag(rs.getLong("DELETE_FLAG"));
                            //tmp.setMandatoryFlag(rs.getString("MANDATORY_FLAG"));
                            tmp.setOaApproveFlow(rs.getString("OA_APPROVE_FLOW"));
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
                            tmp.setTemplateCode(rs.getString("template_code"));
                            tmp.setTemplateApplShortCode(rs.getString("template_appl_short_code"));
                            tmp.setTemplateChapterFileName(rs.getString("template_chapter_file_name"));
                            tmp.setNLSLanguage(rs.getString("LANGUAGE"));
                            tmp.setNLSTerritory(rs.getString("territory"));

                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);
        return dt;
    }

    @Transactional(readOnly = true)
    public List<TemplateChaptersDTO> getTemplateChaptersList(long templateId, String lineType, long pageNo, long pageSize, int type) {
        DataTablesDTO dt = this.getTemplateChaptersList_2(templateId, lineType, pageNo, pageSize);
        List<TemplateChaptersDTO> nodeList = new ArrayList<TemplateChaptersDTO>();
        if (0 < dt.getDataTotal()) {
            if (0 == type) {
                List<TemplateChaptersDTO> tmpList = dt.getDataSource();
                nodeList.add(tmpList.get(0));
                tmpList.remove(0);
                for(TemplateChaptersDTO node1 : tmpList){
                    boolean mark = false;
                    for(TemplateChaptersDTO node2 : tmpList){
                        if(null != node1.getParentChapterId() && node1.getParentChapterId().equals(node2.getChapterId())){
                            mark = true;
                            if(node2.getChildren() == null)
                                node2.setChildren(new ArrayList<TemplateChaptersDTO>());
                            node2.getChildren().add(node1);
                            break;
                        }
                    }
                    if(!mark){
                        nodeList.add(node1);
                    }
                }
            } else {
                nodeList = dt.getDataSource();
            }
        } else {
            if (!"COVER".equals(lineType)) {
                TemplateChaptersDTO init = new TemplateChaptersDTO();
                init.setChapterName("root");
                init.setTemplateHeaderId(templateId);
                init.setChapterNo("-1");
                init.setParentChapterNo("-1");
                nodeList.add(init);
                init = new TemplateChaptersDTO();
                init.setChapterName("初始");
                init.setTemplateHeaderId(templateId);
                nodeList.add(init);
            }
        }
        return nodeList;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map delTemplateChapters(String templateIds) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NBCC_TEMPLATE.dele_nbcc_template_chapters(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, templateIds);
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

}
