package com.nerin.nims.opt.dsin.service;


import com.nerin.nims.opt.dsin.dto.*;
import com.nerin.nims.opt.dsin.module.ApproveIdEntity;
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
import java.util.*;


/**
 * Created by Administrator on 2016/7/7.
 */
@Component
@Transactional
public class DesignInputService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    //静态变量
    static String iconprof = "icon-input_prof";
    static String iconcont = "icon-input_cont";
    static String icondocu = "icon-input_docu";
    //专业输入使用的变量
    private ArrayList levellist = new ArrayList() {
    };
    private ArrayList tmpSpecJson = new ArrayList();
    private Map preSpecTypeValus = new HashMap();
    private Map specTypeValus = new HashMap();


    //设计输入权限

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map diAuthority(Long projectId,
                           Long personId) {
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Design_Input.Design_Input_Allow(:1,:2,:3,:4)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, personId);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map result = new HashMap();
                        cs.execute();
                        result.put("role", cs.getString(3));
                        result.put("spec", cs.getString(4));
                        return result;
                    }
                });
        return map;
    }

    //项目输入文件
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ProjDocListDTO> projInputDoc(Long projectId, Long phaseCode) {
        List<ProjDocListDTO> list = (List<ProjDocListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Design_Input.Proj_Input_Doc(:1,:2,:3)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, phaseCode);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        List<ProjDocListDTO> tmpList = new ArrayList<ProjDocListDTO>();
                        ProjDocListDTO tmp;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new ProjDocListDTO();
                            tmp.setId(rs.getLong("Id"));
                            tmp.setName(rs.getString("NAME"));
                            tmp.setDescribe(rs.getString("Sm"));
                            tmp.setReceiveDate(rs.getString("Jsrq"));
                            tmp.setApproveStauts(rs.getString("Pszt"));
                            tmp.setCreateBy(rs.getString("User_Name"));
                            tmp.setCreateById(rs.getLong("Cjz"));
                            tmp.setPersonId(rs.getLong("Per_Id"));
                            tmp.setEnabled(rs.getString("Yxx"));
                            tmp.setPsyj(rs.getString("Psyj"));
                            tmpList.add(tmp);
                        }
                        rs.close();
                        return tmpList;
                    }
                });
        return list;
    }

    //专业输入输入类别

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public String specInputType(String role, Long projectId, Long phaseCode, Long personId) {
        String jsonObj = (String) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Design_Input.Spec_Input_Type(:1,:2,:3,:4,:5)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, role);
                        cs.setLong(2, projectId);
                        cs.setLong(3, phaseCode);
                        cs.setLong(4, personId);
                        cs.registerOutParameter(5, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        int i = 0;
                        int j = 0;
                        levellist.clear();
                        levellist.add("ZY");
                        levellist.add("LB");
                        levellist.add("WJ");
                        SpecAttrDTO ZY = new SpecAttrDTO();
                        SpecAttrDTO LB = new SpecAttrDTO();
                        SpecAttrDTO WJ = new SpecAttrDTO();
                        tmpSpecJson.clear();
                        tmpSpecJson.add(ZY);
                        tmpSpecJson.add(LB);
                        tmpSpecJson.add(WJ);
                        //将数据库的字段临时保存，以便树使用
                        SpecTypeListDTO zyPerColumn = new SpecTypeListDTO();
                        SpecTypeListDTO lbPerColumn = new SpecTypeListDTO();
                        SpecTypeListDTO wjPerColumn = new SpecTypeListDTO();
                        SpecTypeListDTO zyColumn = new SpecTypeListDTO();
                        SpecTypeListDTO lbColumn = new SpecTypeListDTO();
                        SpecTypeListDTO wjColumn = new SpecTypeListDTO();
                        preSpecTypeValus.clear();
                        preSpecTypeValus.put("ZY", zyPerColumn);
                        preSpecTypeValus.put("LB", lbPerColumn);
                        preSpecTypeValus.put("WJ", wjPerColumn);
                        specTypeValus.clear();
                        specTypeValus.put("ZY", zyColumn);
                        specTypeValus.put("LB", lbColumn);
                        specTypeValus.put("WJ", wjColumn);
                        while (rs.next()) {// 转换每行的返回值到Map中

                            zyColumn.setText(rs.getString("Mbzy"));
                            zyColumn.setId("ZY" + i);
                            zyColumn.setIconCls(iconprof);
                            lbColumn.setText(rs.getString("Wjlb"));
                            lbColumn.setId("LB" + i);
                            lbColumn.setIconCls(iconcont);
                            wjColumn.setText(rs.getString("Wjmc"));
                            wjColumn.setId(String.valueOf(rs.getLong("Id")));
                            wjColumn.setIconCls(icondocu);
                            wjColumn.setColor(rs.getString("color"));
                            if (j > 0) {
                                specRecursionDown(specTypeValus, preSpecTypeValus, "ZY");
                            }
                            zyPerColumn.setText(rs.getString("Mbzy"));
                            zyPerColumn.setId("ZY" + i);
                            zyPerColumn.setIconCls(iconprof);
                            lbPerColumn.setText(rs.getString("Wjlb"));
                            lbPerColumn.setId("LB" + i);
                            lbPerColumn.setIconCls(iconcont);
                            wjPerColumn.setText(rs.getString("Wjmc"));
                            wjPerColumn.setId(String.valueOf(rs.getLong("Id")));
                            wjPerColumn.setIconCls(icondocu);
                            wjPerColumn.setColor(rs.getString("color"));
                            i++;
                            j++;
                        }
                        rs.close();
                        zyColumn.setText("");
                        lbColumn.setText("");
                        wjColumn.setText("");
                        specRecursionDown(specTypeValus, preSpecTypeValus, "ZY");
                        String json = "[{\"id\":0,\"text\":\"专业输入类别" +
                                //((SpecAttrDTO) tmpSpecJson.get(0)).getCounter()+
                                "\"";
                        json = json + ",\"color\":\"" + ((SpecAttrDTO) tmpSpecJson.get(0)).getColor() + "\",\"children\":";
                        json = json + ((SpecAttrDTO) tmpSpecJson.get(0)).getJsonStr() + "]}]";
                        // JSONObject result = new JSONObject(json);
                        return json;
                    }
                });
        return jsonObj;
    }


    //向下递归 进行比较 发现不同的层次
    private void specRecursionDown(Map specType, Map preSpecType, String level) {
        int i = levellist.indexOf(level);
        String nextLevel = new String();
        if (specChange(specType.get(level), level).equals("")) {
            //如果
            specJsonRecursionDown(level, preSpecType);
        } else {
            if (specChange(specType.get(level), level).equals(specChange(preSpecType.get(level), level))) {
                nextLevel = (levellist.get(i + 1)).toString();
                specRecursionDown(specType, preSpecType, nextLevel);
            } else {
                specJsonRecursionDown(level, preSpecType);
            }
        }
    }

    //强制转换类型
    private String specChange(Object obj, String level) {
        String str = new String();
        switch (level) {
            case "ZY":
                str = ((SpecTypeListDTO) obj).getText();
                break;
            case "LB":
                str = ((SpecTypeListDTO) obj).getText();
                break;
            case "WJ":
                str = ((SpecTypeListDTO) obj).getText();
                break;
        }
        return str;
    }

    //json字段拼接 -- 向下递归
    //使用递归 原则是指向上一组数据，并按层级递归至底部取得底部数据，把该层级临时存储字符和底部据进行同级合并，
    // 归集到上一层级临时存储字符然后递归返回
    private void specJsonRecursionDown(String level, Map preSpecType) {
        int i = levellist.indexOf(level);
        Object typeValue;
        typeValue = preSpecType.get(level);
        String nextLevel = new String();
        Object nextTypeValue = new String();
        SpecAttrDTO specAttrDTO = new SpecAttrDTO();
        SpecAttrDTO nextspecAttrDTO = new SpecAttrDTO();
        if (i < levellist.size() - 1) {
            nextLevel = (levellist.get(i + 1)).toString();
            specJsonRecursionDown(nextLevel, preSpecType);
        }
        if (i == levellist.size() - 1) {
            //处理末级
            specAttrDTO = (SpecAttrDTO) tmpSpecJson.get(i);
            specAttrDTO.setJsonStr(specAttrDTO.getJsonStr().equals("") ? ("[" + dealSpecStr(level, typeValue)) : (specAttrDTO.getJsonStr() + ',') + dealSpecStr(level, typeValue));
            specAttrDTO.setCounter(specAttrDTO.getCounter() + 1);
            tmpSpecJson.set(i, specAttrDTO);
            if (("#000000").equals(specAttrDTO.getColor()) || ("").equals(specAttrDTO.getColor())) {
                specAttrDTO.setColor(((SpecTypeListDTO) typeValue).getColor());
            }
        } else {
            //先处理下层节点，拼json
            nextLevel = (levellist.get(i + 1)).toString();
            nextTypeValue = preSpecType.get(nextLevel);
            nextspecAttrDTO = (SpecAttrDTO) tmpSpecJson.get(i + 1);
            //封闭下层
            nextspecAttrDTO.setJsonStr(nextspecAttrDTO.getJsonStr() + "]}");
            tmpSpecJson.set(i + 1, nextspecAttrDTO);
            //处理当层节点，拼json
            nextspecAttrDTO = (SpecAttrDTO) tmpSpecJson.get(i + 1);
            specAttrDTO = (SpecAttrDTO) tmpSpecJson.get(i);
            specAttrDTO.setJsonStr(specAttrDTO.getJsonStr().equals("") ? "[" + dealSpecStr(level, typeValue) + nextspecAttrDTO.getJsonStr() : (specAttrDTO.getJsonStr() + ',') + dealSpecStr(level, typeValue) + nextspecAttrDTO.getJsonStr());
            //根据下层颜色决定上层颜色
           /* if (("#000000").equals(specAttrDTO.getColor()) || ("").equals(specAttrDTO.getColor())) {
                specAttrDTO.setColor(nextspecAttrDTO.getColor());
            }*/
            //取消上层颜色
            specAttrDTO.setColor("#000000");
            //计数
            specAttrDTO.setCounter(specAttrDTO.getCounter() + 1);
            tmpSpecJson.set(i, specAttrDTO);
            //清空下层节点
            nextspecAttrDTO.setJsonStr("");
            nextspecAttrDTO.setCounter(0);
            nextspecAttrDTO.setColor("");
            tmpSpecJson.set(i + 1, nextspecAttrDTO);
        }
    }

    //json字段拼接 -- 拼接数据
    private String dealSpecStr(String level, Object SpecType) {
        String str = new String();
        SpecTypeListDTO zy = new SpecTypeListDTO();
        SpecTypeListDTO lb = new SpecTypeListDTO();
        SpecTypeListDTO wj = new SpecTypeListDTO();
        SpecAttrDTO nextspecAttrDTO = new SpecAttrDTO();
        int i;
        String count = "";
        String color = "";
        //计数
        i = levellist.indexOf(level);
        if (i < levellist.size() - 1) {
            nextspecAttrDTO = (SpecAttrDTO) tmpSpecJson.get(i + 1);
            count = "--" + nextspecAttrDTO.getCounter();
            //取消上层颜色
            /*color = nextspecAttrDTO.getColor();*/
            color = "#000000";
        }
        switch (level) {
            case "ZY":
                zy = ((SpecTypeListDTO) SpecType);
                str = "{\"id\":\"" + zy.getId() + "\",\"text\": \"" + zy.getText() + "\",\"iconCls\": \"" + zy.getIconCls() + "\",\"color\": \"" + color + "\",\"state\":\"closed\",\"children\":";
                break;
            case "LB":
                lb = ((SpecTypeListDTO) SpecType);
                str = "{\"id\": \"" + lb.getId() + "\",\"text\": \"" + lb.getText() + "\",\"iconCls\": \"" + lb.getIconCls() + "\",\"color\": \"" + color + "\",\"state\":\"closed\",\"children\":";
                break;
            case "WJ":
                wj = ((SpecTypeListDTO) SpecType);
                str = "{\"id\": \"" + wj.getId() + "\",\"text\": \"" + wj.getText() + "\",\"iconCls\": \"" + wj.getIconCls() + "\",\"color\": \"" + wj.getColor() + "\"}";
                break;
        }
        return str;

    }
//专业输入输入文件

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<SpecTypeDocListDTO> specInputDoc(String role, Long projectId, Long phaseCode, Long personId) {
        List<SpecTypeDocListDTO> list = (List<SpecTypeDocListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Design_Input.Spec_Input_Doc(:1,:2,:3,:4,:5)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, role);
                        cs.setLong(2, projectId);
                        cs.setLong(3, phaseCode);
                        cs.setLong(4, personId);
                        cs.registerOutParameter(5, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        List<SpecTypeDocListDTO> tmpList = new ArrayList<SpecTypeDocListDTO>();
                        SpecTypeDocListDTO tmp;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SpecTypeDocListDTO();
                            tmp.setId(rs.getLong("Id"));
                            tmp.setHid(rs.getLong("Hid"));
                            tmp.setName(rs.getString("NAME"));
                            tmp.setDescribe(rs.getString("Sm"));
                            tmp.setSpeciality(rs.getString("Zy"));
                            tmp.setReceiveDate(rs.getString("Jsrq"));
                            tmp.setApproveStauts(rs.getString("Pszt"));
                            tmp.setCreateBy(rs.getString("User_Name"));
                            tmp.setCreateById(rs.getLong("Cjz"));
                            tmp.setPersonId(rs.getLong("Per_Id"));
                            tmp.setEnabled(rs.getString("Yxx"));
                            tmp.setRequired(rs.getString("Sfbx1"));
                            tmp.setPsyj(rs.getString("Psyj"));
                            tmpList.add(tmp);
                        }
                        rs.close();
                        return tmpList;
                    }
                });
        return list;
    }

    //设备输入输入类别
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map equiInput(String role, Long projectId, Long phaseCode, Long personId) {
        Map jsonObj = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Design_Input.Equi_Input_Doc(:1,:2,:3,:4,:5)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, role);
                        cs.setLong(2, projectId);
                        cs.setLong(3, phaseCode);
                        cs.setLong(4, personId);
                        cs.registerOutParameter(5, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        int i = 0;
                        int j = 0;
                        int k = 0;
                        //将数据库的字段临时保存，以便树使用
                        String preSpec = "";
                        String curSpec = "";
                        EQDocListDTO eqDocListDTO;
                        EQTreeListDTO eqTreeListDTO = new EQTreeListDTO();
                        EQChildListDTO eqChildListDTO = new EQChildListDTO();
                        ArrayList<EQTreeListDTO> eqTreeListDTOs = new ArrayList<EQTreeListDTO>();
                        ArrayList<EQDocListDTO> eqDocListDTOs = new ArrayList<EQDocListDTO>();
                        ArrayList<EQChildListDTO> tmplist = new ArrayList<EQChildListDTO>();
                        while (rs.next()) {// 转换每行的返回值到Map中
                            eqDocListDTO = new EQDocListDTO();
                            curSpec = rs.getString("zy");
                            //列表值
                            eqDocListDTO.setId(rs.getLong("ID"));
                            eqDocListDTO.setName(rs.getString("NAME"));
                            eqDocListDTO.setDescribe(rs.getString("SM"));
                            eqDocListDTO.setSpeciality(rs.getString("ZY"));
                            eqDocListDTO.setItem(rs.getString("SBWH"));
                            eqDocListDTO.setEquipName(rs.getString("SBMC"));
                            eqDocListDTO.setOrderNO(rs.getString("DHDH"));
                            eqDocListDTO.setOrderName(rs.getString("DHDM"));
                            eqDocListDTO.setRequiredDate(rs.getString("YQRQ"));
                            eqDocListDTO.setReceiveDate(rs.getString("JSRQ"));
                            eqDocListDTO.setApproveStauts(rs.getString("PSZT"));
                            eqDocListDTO.setCreateBy(rs.getString("USER_NAME"));
                            eqDocListDTO.setPersonId(rs.getLong("PER_ID"));
                            eqDocListDTO.setEnabled(rs.getString("YXX"));
                            eqDocListDTO.setPsyj(rs.getString("Psyj"));
                            eqDocListDTOs.add(eqDocListDTO);
                            //
                            eqChildListDTO = new EQChildListDTO();
                            eqChildListDTO.setId(String.valueOf(rs.getLong("ID")));
                            eqChildListDTO.setIconCls(icondocu);
                            eqChildListDTO.setText(rs.getString("SBWH") + " " + rs.getString("SBMC"));
                            //计数
                            j++;
                            if (!curSpec.equals(preSpec) && !preSpec.equals("")) {
                                //树 插入前一个专业
                                eqTreeListDTO = new EQTreeListDTO();
                                eqTreeListDTO.setId("ZY" + k);
                                eqTreeListDTO.setText(preSpec + "--" + i);
                                eqTreeListDTO.setIconCls(iconprof);
                                eqTreeListDTO.setState("closed");
                                eqTreeListDTO.setChildren(tmplist);
                                eqTreeListDTOs.add(eqTreeListDTO);
                                tmplist = new ArrayList<EQChildListDTO>();
                                i = 0;
                                k++;
                            }
                            i++;
                            tmplist.add(eqChildListDTO);
                            preSpec = rs.getString("zy");
                        }
                        rs.close();
                        //最后一个专业
                        eqTreeListDTO = new EQTreeListDTO();
                        eqTreeListDTO.setId("ZY" + k);
                        eqTreeListDTO.setText(preSpec + "--" + i);
                        eqTreeListDTO.setIconCls(iconprof);
                        eqTreeListDTO.setState("closed");
                        eqTreeListDTO.setChildren(tmplist);
                        eqTreeListDTOs.add(eqTreeListDTO);
                        //String json = "[{\"id\":0,\"text\":\"设备所属专业--" + j + "\",\"children\":";
                        //  json = json + JSONObject.valueToString(eqTreeListDTOs) + "}]";
                        Map result = new HashMap();
                        EQHeadDTO eqHeadDTO = new EQHeadDTO();
                        ArrayList<EQHeadDTO> tmp = new ArrayList<EQHeadDTO>();
                        if (eqDocListDTOs.size() > 0) {
                            eqHeadDTO.setId("0");
                            eqHeadDTO.setText("设备所属专业--" + j);
                            eqHeadDTO.setChildren(eqTreeListDTOs);
                            tmp.add(eqHeadDTO);
                        }
                        result.put("tree", tmp);
                        result.put("grid", eqDocListDTOs);
                        return result;
                    }
                });
        return jsonObj;
    }

    //代管输入文件

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<TmpDocListDTO> tempInputDoc(Long projectId) {
        List<TmpDocListDTO> list = (List<TmpDocListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Design_Input.Temp_Input_Doc(:1,:2)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        List<TmpDocListDTO> tmpList = new ArrayList<TmpDocListDTO>();
                        TmpDocListDTO tmp;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new TmpDocListDTO();
                            tmp.setId(rs.getLong("Id"));
                            tmp.setName(rs.getString("NAME"));
                            tmp.setDescribe(rs.getString("Sm"));
                            tmp.setReceiveDate(rs.getString("Jsrq"));
                            tmp.setStauts(rs.getString("Zt"));
                            tmp.setCreateBy(rs.getString("Creat_Name"));
                            tmp.setCreateById(rs.getLong("Cjz"));
                            tmp.setContact(rs.getString("Contact_Name"));
                            tmp.setContactId(rs.getLong("Jbr"));
                            tmp.setBackDate(rs.getString("Ghrq"));
                            tmp.setPlanBackDate(rs.getString("Nghrq"));
                            tmp.setPersonId(rs.getLong("PER_ID"));
                            tmpList.add(tmp);
                        }
                        rs.close();
                        return tmpList;
                    }
                });
        return list;
    }

    //输入视图
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public long getErpId(Long projectId, Long personId, Long docId, String phaseName, String specName) {
        long erpId = (long) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Design_Input.Create_Input_View(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, projectId);
                        cs.setLong(2, personId);
                        cs.setLong(3, docId);
                        cs.setString(4, phaseName);
                        cs.setString(5, specName);
                        cs.registerOutParameter(6, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        long tmp = cs.getLong(6);
                        return tmp;
                    }
                });
        return erpId;
    }

    //获取专业

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<SpecListDTO> getSpecList(String role, Long projectId, Long phaseCode, Long personId) {
        List<SpecListDTO> list = (List<SpecListDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Design_Input.Get_Spec(:1,:2,:3,:4,:5)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, role);
                        cs.setLong(2, projectId);
                        cs.setLong(3, phaseCode);
                        cs.setLong(4, personId);
                        cs.registerOutParameter(5, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(5);// 获取游标一行的值
                        List<SpecListDTO> tmpList = new ArrayList<SpecListDTO>();
                        SpecListDTO tmp;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new SpecListDTO();
                            tmp.setSpec(rs.getString("Industry_Code"));
                            tmp.setSpecName(rs.getString("Industry_Name"));
                            tmpList.add(tmp);
                        }
                        rs.close();
                        return tmpList;
                    }
                });
        return list;
    }

    private ApproveIdEntity elmentsDtoToEntity(ApproveIdDTO approveIdDTO) {
        ApproveIdEntity entity = new ApproveIdEntity();
        try {
            PropertyUtils.copyProperties(entity, approveIdDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    //list to string
    public String listToString(List list, char separator) {
        return org.apache.commons.lang.StringUtils.join(list.toArray(), separator);
    }

    //@Transactional(rollbackFor = Exception.class)
    public Map createApproveView(ApproveInfoDTO approveInfoDTO) {
        List<ApproveIdEntity> dataList = new ArrayList<ApproveIdEntity>();
        for (int i = 0; i < approveInfoDTO.getRows().size(); i++) {
            dataList.add(this.elmentsDtoToEntity(approveInfoDTO.getRows().get(i)));
        }
        long projectId = approveInfoDTO.getProjectId();
        String phaseName = approveInfoDTO.getPhaseName();
        String userNum = listToString(approveInfoDTO.getPsry(),',');
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Design_Input.Create_Approve_View(:1,:2,:3,:4,:5,:6,:7)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_DSIP_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.setLong(2, projectId);
                        cs.setString(3, phaseName);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        cs.setString(7, userNum);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put("status", cs.getString(6));
                        tmp.put("errInfo", cs.getString(5));
                        tmp.put("erpId", cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    //输入视图
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map changeStauts(Long tabIndex, Long id) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Design_Input.Change_Approve_Stauts(:1,:2,:3,:4)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, tabIndex);
                        cs.setLong(2, id);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put("stauts", cs.getLong(4));
                        return tmp;
                    }
                });
        return map;
    }

    //删除数据
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Map deleteDoc(ApproveInfoDTO approveInfoDTO) {
        List<ApproveIdEntity> dataList = new ArrayList<ApproveIdEntity>();
        for (int i = 0; i < approveInfoDTO.getRows().size(); i++) {
            dataList.add(this.elmentsDtoToEntity(approveInfoDTO.getRows().get(i)));
        }
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Design_Input.Delete_Doc(:1,:2,:3)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_DSIP_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.registerOutParameter(2, OracleTypes.VARCHAR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put("status", cs.getString(3));
                        tmp.put("errInfo", cs.getString(2));
                        return tmp;
                    }
                });
        return map;
    }

    //输入视图
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<ApproveRequestDTO> getApproveRequest(Long tabIndex, Long id) {
        List<ApproveRequestDTO> approveRequestDTOList = (List<ApproveRequestDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Design_Input.Get_Request(:1,:2,:3)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, tabIndex);
                        cs.setLong(2, id);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        ApproveRequestDTO approveRequestDTO;
                        List<ApproveRequestDTO> tmp = new ArrayList<ApproveRequestDTO>();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            approveRequestDTO = new ApproveRequestDTO();
                            approveRequestDTO.setRequestId(rs.getLong("Id"));
                            approveRequestDTO.setRequestName(rs.getString("REQUESTNAME"));
                            approveRequestDTO.setCreateBy(rs.getString("CREATEBY"));
                            approveRequestDTO.setCreateDate(rs.getString("CREATEDATE"));
                            approveRequestDTO.setCurrentPerson(rs.getString("CURRENT_PERSON"));
                            approveRequestDTO.setStatus(rs.getString("STATUS"));
                            tmp.add(approveRequestDTO);
                        }
                        rs.close();
                        return tmp;
                    }
                });
        return approveRequestDTOList;
    }
}