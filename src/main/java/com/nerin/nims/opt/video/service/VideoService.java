package com.nerin.nims.opt.video.service;


import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.video.dto.*;
import com.nerin.nims.opt.video.module.*;
import oracle.jdbc.OracleTypes;
import oracle.jdbc.driver.OracleConnection;
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

import static java.lang.Float.parseFloat;

@Component
@Transactional
public class VideoService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NerinProperties nerinProperties;

    //static String filePath = "NewFrontEnd/test/15111601NIMStraining/video/";
//我的观看记录

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<MyRecordDTO> myRecord(Long personId) {
        List<MyRecordDTO> myRecordDTOs = (List<MyRecordDTO>) jdbcTemplate.execute(
                con -> {
                    String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Get_My_Record(:1,:2)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setLong(1, personId);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    MyRecordDTO myRecordDTO;
                    cs.execute();
                    List<MyRecordDTO> tmp = new ArrayList<MyRecordDTO>();
                    ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                    while (rs.next()) {// 转换每行的返回值到Map中
                        myRecordDTO = new MyRecordDTO();
                        myRecordDTO.setId(rs.getLong("Id"));
                        myRecordDTO.setFavoriteCount(rs.getLong("Favorite_Count"));
                        myRecordDTO.setFormat(rs.getString("Format"));
                        myRecordDTO.setName(rs.getString("Name"));
                        myRecordDTO.setPlayCount(rs.getLong("Play_Count"));
                        myRecordDTO.setfId(rs.getLong("Fid"));
                        myRecordDTO.setUrl(rs.getString("Path"));
                        tmp.add(myRecordDTO);
                    }
                    rs.close();
                    return tmp;
                });
        return myRecordDTOs;
    }

    //我的收藏

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<MyFavoriteDTO> myFavorite(Long personId) {
        List<MyFavoriteDTO> myFavoriteDTOs = (List<MyFavoriteDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Get_My_Favorite(:1,:2)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, personId);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        MyFavoriteDTO myFavoriteDTO;
                        cs.execute();
                        List<MyFavoriteDTO> tmp = new ArrayList<MyFavoriteDTO>();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            myFavoriteDTO = new MyFavoriteDTO();
                            myFavoriteDTO.setId(rs.getLong("Id"));
                            myFavoriteDTO.setFavoriteCount(rs.getLong("Favorite_Count"));
                            myFavoriteDTO.setFormat(rs.getString("Format"));
                            myFavoriteDTO.setName(rs.getString("Name"));
                            myFavoriteDTO.setPlayCount(rs.getLong("Play_Count"));
                            myFavoriteDTO.setUrl(rs.getString("Path"));
                            myFavoriteDTO.setfId(rs.getLong("Fid"));
                            tmp.add(myFavoriteDTO);
                        }
                        rs.close();
                        return tmp;
                    }
                });
        return myFavoriteDTOs;
    }

    //查找视频 searchVideo
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public PageDTO searchVideo(String queryTerm, Long pageNo, Long pageSize,Long perId) {
        PageDTO result = new PageDTO();
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        List<NextAllVideoDTO> nextAllVideoDTOs = (List<NextAllVideoDTO>) jdbcTemplate.execute(
                con -> {
                    String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Search_Video(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setString(1, queryTerm);
                    cs.setLong(2, pageNo);
                    cs.setLong(3, pageSize);
                    cs.registerOutParameter(4, OracleTypes.CURSOR);
                    cs.registerOutParameter(5, OracleTypes.NUMBER);
                    cs.setLong(6, perId);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    NextAllVideoDTO allVideoDTO;
                    cs.execute();
                    List<NextAllVideoDTO> tmp = new ArrayList<NextAllVideoDTO>();
                    result.setTotal(cs.getLong(5));
                    ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                    while (rs.next()) {// 转换每行的返回值到Map中
                        allVideoDTO = new NextAllVideoDTO();
                        allVideoDTO.setId(rs.getLong("Id"));
                        allVideoDTO.setCollectCount(rs.getLong("Favorite_Count"));
                        allVideoDTO.setFormat(rs.getString("Format"));
                        allVideoDTO.setTitle(rs.getString("Name"));
                        allVideoDTO.setPlayCount(rs.getLong("Play_Count"));
                        allVideoDTO.setfId(rs.getLong("Fid"));
                        allVideoDTO.setUrl(rs.getString("Path"));
                        tmp.add(allVideoDTO);
                    }
                    rs.close();
                    return tmp;
                });
        result.setRows(nextAllVideoDTOs);
        return result;
    }

    //根据结构取下层节点

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public PageDTO getAllVideo(Long id, Long pageNo, Long pageSize,Long perId) {
        PageDTO result = new PageDTO();
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        List<NextAllVideoDTO> nextAllVideoDTOs = (List<NextAllVideoDTO>) jdbcTemplate.execute(
                con -> {
                    String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Get_All(:1,:2,:3,:4,:5,:6)}";// 调用的sql
                    CallableStatement cs = con.prepareCall(storedProc);
                    cs.setLong(1, id);
                    cs.setLong(2, pageNo);
                    cs.setLong(3, pageSize);
                    cs.registerOutParameter(4, OracleTypes.CURSOR);
                    cs.registerOutParameter(5, OracleTypes.NUMBER);
                    cs.setLong(6, perId);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    NextAllVideoDTO allVideoDTO;
                    cs.execute();
                    List<NextAllVideoDTO> tmp = new ArrayList<NextAllVideoDTO>();
                    result.setTotal(cs.getLong(5));
                    ResultSet rs = (ResultSet) cs.getObject(4);// 获取游标一行的值
                    while (rs.next()) {// 转换每行的返回值到Map中
                        allVideoDTO = new NextAllVideoDTO();
                        allVideoDTO.setId(rs.getLong("Id"));
                        allVideoDTO.setCollectCount(rs.getLong("Favorite_Count"));
                        allVideoDTO.setFormat(rs.getString("Format"));
                        allVideoDTO.setTitle(rs.getString("Name"));
                        allVideoDTO.setPlayCount(rs.getLong("Play_Count"));
                        allVideoDTO.setfId(rs.getLong("Fid"));
                        allVideoDTO.setUrl(rs.getString("Path"));
                        tmp.add(allVideoDTO);
                    }
                    rs.close();
                    return tmp;
                });
        result.setRows(nextAllVideoDTOs);
        return result;
    }

    //取播放断点
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<RecordDTO> getLastPosition(Long id, Long personId) {
        List<RecordDTO> recordDTOs = (List<RecordDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Get_Record(:1,:2,:3)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.setLong(2, personId);
                        cs.registerOutParameter(3, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        RecordDTO recordDTO;
                        cs.execute();
                        List<RecordDTO> tmp = new ArrayList<RecordDTO>();
                        ResultSet rs = (ResultSet) cs.getObject(3);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            recordDTO = new RecordDTO();
                            recordDTO.setLastPosition(parseFloat(rs.getString("Last_Position")));
                            recordDTO.setLastUpdateTime(rs.getDate("Create_Date"));
                            tmp.add(recordDTO);
                        }
                        rs.close();
                        return tmp;
                    }
                });
        return recordDTOs;
    }
    //取下层节点名称

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<NodeDTO> getNode(Long id) {
        List<NodeDTO> nodeDTOs = (List<NodeDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Get_Next_Node(:1,:2)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, id);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        NodeDTO nodeDTO;
                        cs.execute();
                        List<NodeDTO> tmp = new ArrayList<NodeDTO>();
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        while (rs.next()) {// 转换每行的返回值到Map中
                            nodeDTO = new NodeDTO();
                            nodeDTO.setId(rs.getLong("Id"));
                            nodeDTO.setName(rs.getString("Name"));
                            nodeDTO.setParentId(rs.getLong("Parent_Id"));
                            tmp.add(nodeDTO);
                        }
                        rs.close();
                        return tmp;
                    }
                });
        return nodeDTOs;
    }

    private RecordEntity recordDtoToEntity(SaveRecordDTO saveRecordDTO) {
        RecordEntity entity = new RecordEntity();
        try {
            PropertyUtils.copyProperties(entity, saveRecordDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private List<RecordEntity> initRecordEntitys(List<SaveRecordDTO> recordDTOs) {
        List<RecordEntity> dataList = new ArrayList<RecordEntity>();
        for (int i = 0; i < recordDTOs.size(); i++) {
            RecordEntity recordEntity = new RecordEntity();
            recordEntity = this.recordDtoToEntity(recordDTOs.get(i));
            dataList.add(recordEntity);
        }
        return dataList;
    }

    /**
     * 保存记录
     */

    @SuppressWarnings("unchecked")
    //@Transactional(rollbackFor = Exception.class)
    public Map saveRecord(List<SaveRecordDTO> recordDTOs) {
        List<RecordEntity> dataList = initRecordEntitys(recordDTOs);
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Save_Record(:1,:2,:3)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_V_RECORD_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        String str = "";
                        String mgr = "";
                        if (cs.getString(3).equals("S")) {
                            tmp.put("MSG", "S");
                        } else {
                            ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                            mgr = rs.getString("Mgr");
                            str = mgr + ":";
                            while (rs.next()) {// 转换每行的返回值到Map中
                                if (mgr.equals(rs.getString("Mgr"))) {
                                    str = str + rs.getLong("Id");
                                } else {
                                    mgr = rs.getString("Mgr");
                                    str = str + ";" + mgr + ":" + rs.getLong("Id");
                                }
                            }
                            rs.close();
                            tmp.put("MSG", str);
                        }
                        return tmp;
                    }
                });
        return map;
    }

    private FavoriteEntity favoriteDtoToEntity(FavoriteDTO favoriteDTO) {
        FavoriteEntity entity = new FavoriteEntity();
        try {
            PropertyUtils.copyProperties(entity, favoriteDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private List<FavoriteEntity> initFavoriteEntitys(List<FavoriteDTO> favoriteDTOs) {
        List<FavoriteEntity> dataList = new ArrayList<FavoriteEntity>();
        for (int i = 0; i < favoriteDTOs.size(); i++) {
            FavoriteEntity favoriteEntity = new FavoriteEntity();
            favoriteEntity = this.favoriteDtoToEntity(favoriteDTOs.get(i));
            dataList.add(favoriteEntity);
        }
        return dataList;
    }

    /**
     * 保存收藏
     */

    @SuppressWarnings("unchecked")
    //@Transactional(rollbackFor = Exception.class)
    public Map saveFavorite(List<FavoriteDTO> favoriteDTOs) {
        List<FavoriteEntity> dataList = initFavoriteEntitys(favoriteDTOs);
        Map map = (HashMap) jdbcTemplate.execute(
                con -> {
                    OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                    String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Save_Favorite(:1,:2,:3)}";
                    CallableStatement cs = con.prepareCall(storedProc);
                    ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_V_FAVOR_TBL", or);
                    ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                    cs.setArray(1, vArray);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);
                    cs.registerOutParameter(3, OracleTypes.VARCHAR);
                    return cs;
                }, (CallableStatementCallback) cs -> {
                    Map tmp = new HashMap();
                    String str = "";
                    String mgr = "";
                    cs.execute();
                    if (cs.getString(3).equals("S")) {
                        tmp.put("MSG", "S");
                    } else {
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        mgr = rs.getString("Mgr");
                        str = mgr + ":";
                        while (rs.next()) {// 转换每行的返回值到Map中
                            if (mgr.equals(rs.getString("Mgr"))) {
                                str = str + rs.getLong("Id");
                            } else {
                                mgr = rs.getString("Mgr");
                                str = str + ";" + mgr + ":" + rs.getLong("Id");
                            }
                        }
                        rs.close();
                        tmp.put("MSG", str);
                    }
                    return tmp;
                });
        return map;
    }

    private CommentEntity CommentDtoToEntity(CommentDTO CommentDTO) {
        CommentEntity entity = new CommentEntity();
        try {
            PropertyUtils.copyProperties(entity, CommentDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private List<CommentEntity> initCommentEntitys(List<CommentDTO> CommentDTOs) {
        List<CommentEntity> dataList = new ArrayList<CommentEntity>();
        for (int i = 0; i < CommentDTOs.size(); i++) {
            CommentEntity CommentEntity = new CommentEntity();
            CommentEntity = this.CommentDtoToEntity(CommentDTOs.get(i));
            dataList.add(CommentEntity);
        }
        return dataList;
    }

    /**
     * 保存评论
     */

    @SuppressWarnings("unchecked")
    //@Transactional(rollbackFor = Exception.class)
    public Map saveComment(List<CommentDTO> CommentDTOs) {
        List<CommentEntity> dataList = initCommentEntitys(CommentDTOs);
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Save_Comment(:1,:2,:3)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_V_COMMENT_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        String str = "";
                        String mgr = "";
                        if (cs.getString(3).equals("S")) {
                            tmp.put("MSG", "S");
                        } else {
                            ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                            mgr = rs.getString("Mgr");
                            str = mgr + ":";
                            while (rs.next()) {// 转换每行的返回值到Map中
                                if (mgr.equals(rs.getString("Mgr"))) {
                                    str = str + rs.getLong("Id");
                                } else {
                                    mgr = rs.getString("Mgr");
                                    str = str + ";" + mgr + ":" + rs.getLong("Id");
                                }
                            }
                            rs.close();
                            tmp.put("MSG", str);
                        }
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 修改评论
     */

    @SuppressWarnings("unchecked")
    //@Transactional(rollbackFor = Exception.class)
    public Map updateComment(List<CommentDTO> CommentDTOs) {
        List<CommentEntity> dataList = initCommentEntitys(CommentDTOs);
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Update_Comment(:1,:2,:3)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_V_COMMENT_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        String str = "";
                        String mgr = "";
                        if (cs.getString(3).equals("S")) {
                            tmp.put("MSG", "S");
                        } else {
                            ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                            mgr = rs.getString("Mgr");
                            str = mgr + ":";
                            while (rs.next()) {// 转换每行的返回值到Map中
                                if (mgr.equals(rs.getString("Mgr"))) {
                                    str = str + rs.getLong("Id");
                                } else {
                                    mgr = rs.getString("Mgr");
                                    str = str + ";" + mgr + ":" + rs.getLong("Id");
                                }
                            }
                            rs.close();
                            tmp.put("MSG", str);
                        }
                        return tmp;
                    }
                });
        return map;
    }

    private GradeEntity GradeDtoToEntity(GradeDTO GradeDTO) {
        GradeEntity entity = new GradeEntity();
        try {
            PropertyUtils.copyProperties(entity, GradeDTO);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private List<GradeEntity> initGradeEntitys(List<GradeDTO> GradeDTOs) {
        List<GradeEntity> dataList = new ArrayList<GradeEntity>();
        for (int i = 0; i < GradeDTOs.size(); i++) {
            GradeEntity GradeEntity = new GradeEntity();
            GradeEntity = this.GradeDtoToEntity(GradeDTOs.get(i));
            dataList.add(GradeEntity);
        }
        return dataList;
    }

    /**
     * 保存评分
     */

    @SuppressWarnings("unchecked")
    //@Transactional(rollbackFor = Exception.class)
    public Map saveGrade(List<GradeDTO> GradeDTOs) {
        List<GradeEntity> dataList = initGradeEntitys(GradeDTOs);
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Save_Grade(:1,:2,:3)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_V_GRADE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        String str = "";
                        String mgr = "";
                        if (cs.getString(3).equals("S")) {
                            tmp.put("MSG", "S");
                        } else {
                            ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                            mgr = rs.getString("Mgr");
                            str = mgr + ":";
                            while (rs.next()) {// 转换每行的返回值到Map中
                                if (mgr.equals(rs.getString("Mgr"))) {
                                    str = str + rs.getLong("Id");
                                } else {
                                    mgr = rs.getString("Mgr");
                                    str = str + ";" + mgr + ":" + rs.getLong("Id");
                                }
                            }
                            rs.close();
                            tmp.put("MSG", str);
                        }
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 修改评分
     */

    @SuppressWarnings("unchecked")
    //@Transactional(rollbackFor = Exception.class)
    public Map updateGrade(List<GradeDTO> GradeDTOs) {
        List<GradeEntity> dataList = initGradeEntitys(GradeDTOs);
        Map map = (HashMap) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                        String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Update_Grade(:1,:2,:3)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_V_GRADE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        String str = "";
                        String mgr = "";
                        if (cs.getString(3).equals("S")) {
                            tmp.put("MSG", "S");
                        } else {
                            ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                            mgr = rs.getString("Mgr");
                            str = mgr + ":";
                            while (rs.next()) {// 转换每行的返回值到Map中
                                if (mgr.equals(rs.getString("Mgr"))) {
                                    str = str + rs.getLong("Id");
                                } else {
                                    mgr = rs.getString("Mgr");
                                    str = str + ";" + mgr + ":" + rs.getLong("Id");
                                }
                            }
                            rs.close();
                            tmp.put("MSG", str);
                        }
                        return tmp;
                    }
                });
        return map;
    }


    private VIDEntity VTDDtoToEntity(VIDDTO viddto) {
        VIDEntity entity = new VIDEntity();
        try {
            PropertyUtils.copyProperties(entity, viddto);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private List<VIDEntity> initVIDEntitys(List<VIDDTO> viddtos) {
        List<VIDEntity> dataList = new ArrayList<VIDEntity>();
        for (int i = 0; i < viddtos.size(); i++) {
            VIDEntity vidEntity = new VIDEntity();
            vidEntity = VTDDtoToEntity(viddtos.get(i));
            dataList.add(vidEntity);
        }
        return dataList;
    }

    /**
     * 删除
     */

    @SuppressWarnings("unchecked")
    //@Transactional(rollbackFor = Exception.class)
    public Map deleteAll(List<VIDDTO> viddtos, String type) {
        List<VIDEntity> dataList = initVIDEntitys(viddtos);
        Map map = (HashMap) jdbcTemplate.execute(
                con -> {
                    OracleConnection or = (OracleConnection) con.getMetaData().getConnection();
                    String storedProc = "{call APPS.Cux_Nims_Video_Pkg.Delete_All(:1,:2,:3,:4)}";
                    CallableStatement cs = con.prepareCall(storedProc);
                    ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("CUX_V_IDS", or);
                    ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                    cs.setArray(1, vArray);
                    cs.registerOutParameter(2, OracleTypes.CURSOR);
                    cs.setString(3, type);
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
                        ResultSet rs = (ResultSet) cs.getObject(2);// 获取游标一行的值
                        mgr = rs.getString("Mgr");
                        str = mgr + ":";
                        while (rs.next()) {// 转换每行的返回值到Map中
                            if (mgr.equals(rs.getString("Mgr"))) {
                                str = str + rs.getLong("Id");
                            } else {
                                mgr = rs.getString("Mgr");
                                str = str + ";" + mgr + ":" + rs.getLong("Id");
                            }
                        }
                        rs.close();
                        tmp.put("MSG", str);
                    }
                    return tmp;
                });
        return map;
    }

}