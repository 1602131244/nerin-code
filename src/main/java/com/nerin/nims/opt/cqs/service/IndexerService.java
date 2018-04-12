package com.nerin.nims.opt.cqs.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.nerin.nims.opt.cqs.module.*;
import com.nerin.nims.opt.cqs.dto.*;
import oracle.sql.*;

import java.lang.reflect.InvocationTargetException;

import oracle.jdbc.OracleTypes;
import oracle.jdbc.OracleConnection;

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
 * Created by user on 16/7/4.
 */
@Component
@Transactional
public class IndexerService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;


    private IndexerEntity dtoToEntity(IndexerDTO indexerDTO) {
        IndexerEntity entity = new IndexerEntity();
        try {
            PropertyUtils.copyProperties(entity, indexerDTO);
            entity.setIndexId(indexerDTO.getIndexId());
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
    public List<ServiceDTO> getServiceList() {
        List<ServiceDTO> resultList = (List<ServiceDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cqs_util_pkg.get_service_list(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ServiceDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);
                        ServiceDTO tmp = null;
                        while (rs.next()) {
                            tmp = new ServiceDTO();
                            tmp.setService(rs.getString("services"));
                            tmp.setCategory(rs.getString("catagory"));
                            tmp.setPyszm(rs.getString("pyszm"));
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
    public List<BasicMaterialDTO> getBasicMaterialList() {
        List<BasicMaterialDTO> resultList = (List<BasicMaterialDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cqs_util_pkg.get_basic_material_list(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<BasicMaterialDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);
                        BasicMaterialDTO tmp = null;
                        while (rs.next()) {
                            tmp = new BasicMaterialDTO();
                            tmp.setBasicMaterial(rs.getString("basic_material"));
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
    public List<RatingDTO> getRatingList() {
        List<RatingDTO> resultList = (List<RatingDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cqs_util_pkg.get_rating_list(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<RatingDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);
                        RatingDTO tmp = null;
                        while (rs.next()) {
                            tmp = new RatingDTO();
                            tmp.setRating(rs.getString("rating"));
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
    public List<PipingMatlClassDTO> getPipingMatlClassList() {
        List<PipingMatlClassDTO> resultList = (List<PipingMatlClassDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cqs_util_pkg.get_piping_matl_class_list(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.registerOutParameter(1, OracleTypes.CURSOR);
                        cs.registerOutParameter(2, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PipingMatlClassDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(1);
                        PipingMatlClassDTO tmp = null;
                        while (rs.next()) {
                            tmp = new PipingMatlClassDTO();
                            tmp.setPipingMatlClass(rs.getString("piping_matl_class"));
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
    public List<IndexerDTO> getIndexerList(String service,
                                        Double designTempValue,
                                        String basicMaterial,
                                        String pipingMatlClass,
                                           Double designPresValue,
                                        String rating) {
        List<IndexerDTO> resultList = (List<IndexerDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cqs_util_pkg.get_indexer_list(?,?,?,?,?,?,?,?)}";   //8
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1,service);
                        if(designTempValue!= null)
                            cs.setDouble(2,designTempValue);
                        else
                            cs.setBigDecimal(2,null);
                        cs.setString(3,basicMaterial);
                        cs.setString(4,pipingMatlClass);
                        if(designPresValue != null)
                            cs.setDouble(5,designPresValue);
                        else
                            cs.setBigDecimal(5,null);
                        cs.setString(6,rating);
                        cs.registerOutParameter(7, OracleTypes.CURSOR);
                        cs.registerOutParameter(8, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<IndexerDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(7);
                        IndexerDTO tmp = null;
                        while (rs.next()) {
                            tmp = new IndexerDTO();
                            tmp.setIndexId(rs.getLong("index_id"));
                            tmp.setIndexOrder(rs.getLong("index_order"));
                            tmp.setCatagory(rs.getString("catagory"));
                            tmp.setServices(rs.getString("services"));
                            tmp.setDesignTempSource(rs.getString("design_temp_source"));
                            tmp.setDesignTempMin(rs.getDouble("design_temp_min"));
                            tmp.setDesignTempMax(rs.getDouble("design_temp_max"));
                            tmp.setDesignTempSpec(rs.getString("design_temp_spec"));
                            tmp.setDesignPresSource(rs.getString("design_pres_source"));
                            tmp.setDesignPresMin(rs.getDouble("design_pres_min"));
                            tmp.setDesignPresMax(rs.getDouble("design_pres_max"));
                            tmp.setDesignPresSpec(rs.getString("design_pres_spec"));
                            tmp.setPipingMatlClass(rs.getString("piping_matl_class"));
                            tmp.setBasicMaterial(rs.getString("basic_material"));
                            tmp.setRating(rs.getString("rating"));
                            tmp.setFlangeFacing(rs.getString("flange_facing"));
                            tmp.setCa(rs.getDouble("ca"));
                            tmp.setNote(rs.getString("note"));
                            tmp.setBatchId(rs.getLong("batch_id"));
                            tmp.setAttributeCcategory(rs.getString("attribute_category"));
                            tmp.setAttribute1(rs.getString("attribute1"));
                            tmp.setAttribute1(rs.getString("attribute2"));
                            tmp.setAttribute1(rs.getString("attribute3"));
                            tmp.setAttribute1(rs.getString("attribute4"));
                            tmp.setAttribute1(rs.getString("attribute5"));
                            tmp.setAttribute1(rs.getString("attribute6"));
                            tmp.setAttribute1(rs.getString("attribute7"));
                            tmp.setAttribute1(rs.getString("attribute8"));
                            tmp.setAttribute1(rs.getString("attribute9"));
                            tmp.setAttribute1(rs.getString("attribute10"));
                            tmp.setAttribute1(rs.getString("attribute11"));
                            tmp.setAttribute1(rs.getString("attribute12"));
                            tmp.setAttribute1(rs.getString("attribute13"));
                            tmp.setAttribute1(rs.getString("attribute14"));
                            tmp.setAttribute1(rs.getString("attribute15"));
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
    public List<PipingMatlClassHdrDTO> getPipingMatlClassHdr(String pipingMatlClass) {
        List<PipingMatlClassHdrDTO> resultList = (List<PipingMatlClassHdrDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cqs_util_pkg.get_piping_matl_class_hdr(?,?,?)}";   //3
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1,pipingMatlClass);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PipingMatlClassHdrDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        PipingMatlClassHdrDTO tmp = null;
                        while (rs.next()) {
                            tmp = new PipingMatlClassHdrDTO();
                            tmp.setPipingMatlClass(rs.getString("piping_matl_class"));
                            tmp.setFlangeFacing(rs.getString("flange_facing"));
                            tmp.setBasicMaterial(rs.getString("basic_material"));
                            tmp.setService(rs.getString("services"));
                            tmp.setCa(rs.getDouble("ca"));
                            tmp.setDesignTempSource(rs.getString("design_temp_source"));
                            tmp.setDesignPresSource(rs.getString("design_pres_source"));
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
    public List<PtRatingTblDTO> getPtRatingTbl(String pipingMatlClass) {
        List<PtRatingTblDTO> resultList = (List<PtRatingTblDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cqs_util_pkg.get_pt_rating_tbl(?,?,?)}";   //3
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1,pipingMatlClass);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PtRatingTblDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        PtRatingTblDTO tmp = null;
                        while (rs.next()) {
                            tmp = new PtRatingTblDTO();
                            tmp.setPtOrderNumber(rs.getLong("pt_order_number"));
                            tmp.setPtId(rs.getLong("pt_id"));
                            tmp.setPipingMatlClass(rs.getString("piping_matl_class"));
                            tmp.setTemperature(rs.getDouble("temperature"));
                            tmp.setPressure(rs.getDouble("pressure"));
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
    public List<ItemsTblDTO> getItemsTbl(String pipingMatlClass) {
        List<ItemsTblDTO> resultList = (List<ItemsTblDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cqs_util_pkg.get_items_tbl(?,?,?)}";   //3
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1,pipingMatlClass);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<ItemsTblDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        ItemsTblDTO tmp = null;
                        while (rs.next()) {
                            tmp = new ItemsTblDTO();
                            tmp.setItemOrderNumber(rs.getLong("item_order_number"));
                            tmp.setItemId(rs.getLong("item_id"));
                            tmp.setPiping_matl_class(rs.getString("piping_matl_class"));
                            tmp.setItemCategory(rs.getString("item_category"));
                            tmp.setItemName(rs.getString("item_name"));
                            tmp.setMinDn(rs.getDouble("min_dn"));
                            tmp.setMaxDn(rs.getDouble("max_dn"));
                            tmp.setEndFacing(rs.getString("end_facing"));
                            tmp.setThkRating(rs.getString("thk_rating"));
                            tmp.setMaterial(rs.getString("material"));
                            tmp.setStandardModel(rs.getString("standard_model"));
                            tmp.setNote(rs.getString("note"));
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
    public List<PipeThicknessTblDTO> getPipeThicknessTbl(String pipingMatlClass) {
        List<PipeThicknessTblDTO> resultList = (List<PipeThicknessTblDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cqs_util_pkg.get_pipe_thickness_tbl(?,?,?)}";   //3
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1,pipingMatlClass);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<PipeThicknessTblDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        PipeThicknessTblDTO tmp = null;
                        while (rs.next()) {
                            tmp = new PipeThicknessTblDTO();
                            tmp.setPipeOrderNumber(rs.getLong("pipe_order_number"));
                            tmp.setPipeId(rs.getLong("pipe_id"));
                            tmp.setPipingMatlClass(rs.getString("piping_matl_class"));
                            tmp.setPipeDn(rs.getDouble("pipe_dn"));
                            tmp.setPipeOuter(rs.getDouble("pipe_outer"));
                            tmp.setPipeThickness(rs.getDouble("pipe_thickness"));
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
    public List<BranchConnectTblDTO> getBranchConnectTbl(String pipingMatlClass) {
        List<BranchConnectTblDTO> resultList = (List<BranchConnectTblDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.cux_cqs_util_pkg.get_branch_connect_tbl(?,?,?)}";   //3
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1,pipingMatlClass);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        List<BranchConnectTblDTO> results = new ArrayList<>(); //may be wrong
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        BranchConnectTblDTO tmp = null;
                        while (rs.next()) {
                            tmp = new BranchConnectTblDTO();
                            tmp.setConnOrderNumber(rs.getLong("conn_order_number"));
                            tmp.setConnectionId(rs.getLong("connection_id"));
                            tmp.setPipingMatlClass(rs.getString("piping_matl_class"));
                            tmp.setBranchDn(rs.getDouble("branch_dn"));
                            tmp.setHeaderDn(rs.getDouble("header_dn"));
                            tmp.setConnectionType(rs.getString("connection_type"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return resultList;
    }

    @SuppressWarnings("unchecked")
    public void addFavorite(String p_user_name, String p_piping_matl_class, String p_flange_facing
            , String p_basic_material, String p_services, String p_ca, String p_design_temp_source, String p_design_pres_source) {
        jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_CQS_UTIL_PKG.ADD_TO_FAVORITE(?,?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, p_user_name);
                        cs.setString(2, p_piping_matl_class);
                        cs.setString(3, p_flange_facing);
                        cs.setString(4, p_basic_material);
                        cs.setString(5, p_services);
                        cs.setString(6, p_ca);
                        cs.setString(7, p_design_temp_source);
                        cs.setString(8, p_design_pres_source);
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
    public void delFavorite(String p_user_name, String p_piping_matl_class) {
        jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_CQS_UTIL_PKG.DELETE_FROM_FAVORITE(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, p_user_name);
                        cs.setString(2, p_piping_matl_class);
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
    @Transactional(readOnly = true)
    public List<IndexerDTO> getFavorite(String p_user_name) {
        List<IndexerDTO> list = (List<IndexerDTO>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_CQS_UTIL_PKG.SEARCH_MY_FAVORITE(?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, p_user_name);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        List<IndexerDTO> results = new ArrayList<IndexerDTO>();
                        IndexerDTO tmp = null;
                        while (rs.next()) {
                            tmp = new IndexerDTO();
                            tmp.setPipingMatlClass(rs.getString("PIPING_MATL_CLASS"));
                            tmp.setFlangeFacing(rs.getString("FLANGE_FACING"));
                            tmp.setBasicMaterial(rs.getString("BASIC_MATERIAL"));
                            tmp.setServices(rs.getString("SERVICES"));
                            tmp.setDesignTempSource(rs.getString("DESIGN_TEMP_SOURCE"));
                            tmp.setDesignPresSource(rs.getString("DESIGN_PRES_SOURCE"));
                            tmp.setAttribute15(rs.getString("CA"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        return list;
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public int exsitsFavorite(String p_user_name, String p_piping_matl_class) {
        int i = (int) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_CQS_UTIL_PKG.EXISTS_MYFAVORITE(?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, p_user_name);
                        cs.setString(2, p_piping_matl_class);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getInt(3);
                    }
                });
        return i;
    }

    @Transactional(readOnly = true)
    public List<StandardNumberDTO> getStandardNumberSource() {
        String sql = "select * from cux.CUX_CQS_STANDARD_NUMBER_T t";
        List<StandardNumberDTO> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
            public List<StandardNumberDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<StandardNumberDTO> result = new ArrayList<StandardNumberDTO>();
                StandardNumberDTO tmp = null;
                while(rs.next()) {
                    tmp = new StandardNumberDTO();
                    tmp.setStandard_number(rs.getString("standard_number"));
                    tmp.setNumber_inexistent_year(rs.getString("number_inexistent_year"));
                    tmp.setSearch_key(rs.getString("search_key"));
                    result.add(tmp);
                }
                return result;
            }});
        return data;
    }
}
