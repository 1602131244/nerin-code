package com.nerin.nims.opt.glcs.service;

import com.nerin.nims.opt.aria.common.EventParm;
import com.nerin.nims.opt.cqs.dto.DataTablesDTO;
import com.nerin.nims.opt.glcs.dto.*;
import com.nerin.nims.opt.glcs.module.CompanyEntity;
import com.nerin.nims.opt.glcs.module.RangeEntity;
import com.nerin.nims.opt.glcs.module.UnitEntity;
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
 * Created by Administrator on 2017/9/12.
 */
@Component
@Transactional
public class RangeApplyService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private RangeEntity rangeDtoToEntity (RangeDto rangeDto) {
        RangeEntity entity = new RangeEntity();
        try {
            PropertyUtils.copyProperties(entity, rangeDto);
            entity.setRangeId(rangeDto.getRangeId());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private UnitEntity unitDtoToEntity (UnitDto unitDto) {
        UnitEntity entity = new UnitEntity();
        try {
            PropertyUtils.copyProperties(entity, unitDto);
            entity.setUnitId(unitDto.getUnitId());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return entity;
    }

    private CompanyEntity companyDtoToEntity (CompanyDto companyDto) {
        CompanyEntity entity = new CompanyEntity();
        try {
            PropertyUtils.copyProperties(entity, companyDto);
            entity.setCompanyCode(companyDto.getCompanyCode());
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
     * 获取合并范围列表
     * @param rangeId
     * @param rangeNumber
     * @param rangeName
     * @param rangeVerson
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getRangeList(Long rangeId, String rangeNumber, String rangeName, Long rangeVerson, String rangeDate, String checkSign, String checkCreate, long userId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<RangeDto> resultList = (List<RangeDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.GET_RANGE(?,?,?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == rangeId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, rangeId);
                        cs.setString(2, rangeNumber);// 设置输入参数的值
                        cs.setString(3, rangeName);// 设置输入参数的值
                        if (null == rangeVerson)
                            cs.setBigDecimal(4, null);
                        else
                            cs.setLong(4, rangeVerson);
                        cs.setString(5, rangeDate);// 设置输入参数的值
                        cs.setString(6, checkSign);// 设置输入参数的值
                        cs.setString(7, checkCreate);// 设置输入参数的值
                        cs.setLong(8, userId);// 设置输入参数的值
                        cs.setLong(9, pageNo);// 设置输入参数的值
                        cs.setLong(10, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(11, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(12, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<RangeDto> results = new ArrayList<RangeDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(11);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(12));
                        RangeDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new RangeDto();
                            tmp.setRangeId(rs.getLong("RANGE_ID"));
                            tmp.setRangeNumber(rs.getString("RANGE_NUMBER"));
                            tmp.setRangeName(rs.getString("RANGE_NAME"));
                            tmp.setDescription(rs.getString("DESCRIPTION"));
                            tmp.setVerson(rs.getLong("VERSON"));
                            tmp.setStartDate(rs.getDate("START_DATE"));
                            tmp.setEndDate(rs.getDate("END_DATE"));
                            tmp.setCreatedBy(rs.getLong("CREATE_BY"));
                            tmp.setCreateByName(rs.getString("CREATE_BY_NAME"));
                            tmp.setCreationDate(rs.getDate("CREATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_BY"));
                            tmp.setLastByName(rs.getString("LAST_BY_NAME"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_DATE"));

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
     * 保存合并范围
     * @param rangeDto
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map saveRange(RangeDto rangeDto,  Long userId) {
        rangeDto.setDateAndUser(userId);
        List<RangeEntity> dataList = new ArrayList<RangeEntity>();
        dataList.add(this.rangeDtoToEntity(rangeDto));
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.INSERT_RANGE(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_GLCS_RANGE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(EventParm.DB_SID, cs.getLong(3));
                        tmp.put(EventParm.DB_STATE, cs.getLong(4));
                        tmp.put(EventParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 删除合并范围
     * @param rangeId
     * @param verson
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map delRange(Long  rangeId, Long verson) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.DELETE_RANGE(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, rangeId);
                        cs.setLong(2, verson);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_STATE, cs.getLong(3));
                        tmp.put(EventParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 复制新版本
     * @param rangeId
     * @param verson
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map copyRange(Long  rangeId, Long verson, Long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.COPY_RANGE(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, rangeId);
                        cs.setLong(2, verson);
                        cs.setLong(3, userId);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_SID, cs.getLong(4));
                        tmp.put(EventParm.DB_STATE, cs.getLong(5));
                        tmp.put(EventParm.DB_MSG, cs.getString(6));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 启用合并范围
     * @param rangeId
     * @param verson
     * @param startDate
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map rangeEnable(Long  rangeId, Long verson, String startDate, Long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.RANGE_ENABLE(?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, rangeId);
                        cs.setLong(2, verson);
                        cs.setString(3, startDate);
                        cs.setLong(4, userId);
                        cs.registerOutParameter(5, OracleTypes.NUMBER);
                        cs.registerOutParameter(6, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_STATE, cs.getLong(5));
                        tmp.put(EventParm.DB_MSG, cs.getString(6));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 反启用合并范围
     * @param rangeId
     * @param verson
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map rangeDisable(Long  rangeId, Long verson, Long userId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.RANGE_DISABLE(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, rangeId);
                        cs.setLong(2, verson);
                        cs.setLong(3, userId);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_STATE, cs.getLong(4));
                        tmp.put(EventParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 获取合并单元
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getUnitListTree(Long rangeId, Long rangeVerson, Long unitId, String checkSign, String checkCreate, long userId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<UnitDto> resultList = (List<UnitDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.GET_UNIT(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == rangeId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, rangeId);
                        if (null == rangeVerson)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, rangeVerson);
                        if (null == unitId)
                            cs.setBigDecimal(3, null);
                        else
                            cs.setLong(3, unitId);
                        cs.setString(4, checkSign);// 设置输入参数的值
                        cs.setString(5, checkCreate);// 设置输入参数的值
                        cs.setLong(6, userId);// 设置输入参数的值
                        cs.setLong(7, pageNo);// 设置输入参数的值
                        cs.setLong(8, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(9, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(10, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<UnitDto> results = new ArrayList<UnitDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(9);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(10));
                        UnitDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new UnitDto();
                            tmp.setRangeId(rs.getLong("RANGE_ID"));
                            tmp.setRangeVerson(rs.getLong("RANGE_VERSON"));
                            tmp.setUnitId(rs.getLong("UNIT_ID"));
                            tmp.setUnitNumber(rs.getString("UNIT_NUMBER"));
                            tmp.setUnitName(rs.getString("UNIT_NAME"));
                            tmp.setUnitShortName(rs.getString("UNIT_SHORT_NAME"));
                            tmp.setUnitParentId(rs.getLong("UNIT_PARENT_ID"));
                            tmp.setCreatedBy(rs.getLong("CREATE_BY"));
                            tmp.setCreateByName(rs.getString("CREATE_BY_NAME"));
                            tmp.setCreationDate(rs.getDate("CREATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_BY"));
                            tmp.setLastByName(rs.getString("LAST_BY_NAME"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_DATE"));

                            tmp.setTitle(rs.getString("UNIT_NAME"));
                            results.add(tmp);
                        }
                        rs.close();
                        return results;
                    }
                });
        dt.setDataSource(resultList);

        List<UnitDto> nodeList = new ArrayList<UnitDto>();
        if (0 < dt.getDataTotal()) {
            List<UnitDto> tmpList = dt.getDataSource();
//            nodeList.add(tmpList.get(0));
//            tmpList.remove(0);
            for(UnitDto node1 : tmpList){
                boolean mark = false;
                for(UnitDto node2 : tmpList){
                    if(null != node1.getUnitParentId() && node1.getUnitParentId().equals(node2.getUnitId())){
                        mark = true;
                        if(node2.getChildren() == null)
                            node2.setChildren(new ArrayList<UnitDto>());
                        node2.getChildren().add(node1);
                        break;
                    }
                }
                if(!mark){
                    nodeList.add(node1);
                }
            }
        }

        dt.setDataSource(nodeList);
        return dt;
    }

    /**
     * 获取合并单元
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getUnitList(Long rangeId, Long rangeVerson, Long unitId,  String checkSign, String checkCreate, long userId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<UnitDto> resultList = (List<UnitDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.GET_UNIT(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == rangeId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, rangeId);
                        if (null == rangeVerson)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, rangeVerson);
                        if (null == unitId)
                            cs.setBigDecimal(3, null);
                        else
                            cs.setLong(3, unitId);
                        cs.setString(4, checkSign);// 设置输入参数的值
                        cs.setString(5, checkCreate);// 设置输入参数的值
                        cs.setLong(6, userId);// 设置输入参数的值
                        cs.setLong(7, pageNo);// 设置输入参数的值
                        cs.setLong(8, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(9, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(10, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<UnitDto> results = new ArrayList<UnitDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(9);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(10));
                        UnitDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new UnitDto();
                            tmp.setRangeId(rs.getLong("RANGE_ID"));
                            tmp.setRangeVerson(rs.getLong("RANGE_VERSON"));
                            tmp.setUnitId(rs.getLong("UNIT_ID"));
                            tmp.setUnitNumber(rs.getString("UNIT_NUMBER"));
                            tmp.setUnitName(rs.getString("UNIT_NAME"));
                            tmp.setUnitShortName(rs.getString("UNIT_SHORT_NAME"));
                            tmp.setUnitParentId(rs.getLong("UNIT_PARENT_ID"));
                            tmp.setCreatedBy(rs.getLong("CREATE_BY"));
                            tmp.setCreateByName(rs.getString("CREATE_BY_NAME"));
                            tmp.setCreationDate(rs.getDate("CREATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_BY"));
                            tmp.setLastByName(rs.getString("LAST_BY_NAME"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_DATE"));

                            tmp.setTitle(rs.getString("UNIT_NAME"));
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
     * 保存合并单元
     * @param unitDto
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map saveUnit(UnitDto unitDto,  Long userId) {
        unitDto.setDateAndUser(userId);
        List<UnitEntity> dataList = new ArrayList<UnitEntity>();
        dataList.add(this.unitDtoToEntity(unitDto));
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.INSERT_UNIT(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_GLCS_UNIT_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(EventParm.DB_SID, cs.getLong(3));
                        tmp.put(EventParm.DB_STATE, cs.getLong(4));
                        tmp.put(EventParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 删除合并单元
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map delUnit(Long  rangeId, Long rangeVerson, Long unitId) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.DELETE_UNIT(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, rangeId);
                        cs.setLong(2, rangeVerson);
                        cs.setLong(3, unitId);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_STATE, cs.getLong(4));
                        tmp.put(EventParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 查询公司
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param ledgerId
     * @param companyCode
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getCompanyList(Long rangeId, Long rangeVerson, Long unitId, String unitFlag, Long ledgerId, String companyCode, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<CompanyDto> resultList = (List<CompanyDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.GET_COMPANY(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == rangeId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, rangeId);
                        if (null == rangeVerson)
                            cs.setBigDecimal(2, null);
                        else
                            cs.setLong(2, rangeVerson);
                        if (null == unitId)
                            cs.setBigDecimal(3, null);
                        else
                            cs.setLong(3, unitId);
                        cs.setString(4, unitFlag);
                        if (null == ledgerId)
                            cs.setBigDecimal(5, null);
                        else
                            cs.setLong(5, ledgerId);
                        cs.setString(6,companyCode);
                        cs.setLong(7, pageNo);// 设置输入参数的值
                        cs.setLong(8, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(9, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(10, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<CompanyDto> results = new ArrayList<CompanyDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(9);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(10));
                        CompanyDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new CompanyDto();
                            tmp.setRangeId(rs.getLong("RANGE_ID"));
                            tmp.setRangeVerson(rs.getLong("RANGE_VERSON"));
                            tmp.setUnitId(rs.getLong("UNIT_ID"));
                            tmp.setCompanyName(rs.getString("COMPANY_NAME"));
                            tmp.setDescription(rs.getString("DESCRIPTION"));
                            tmp.setLedgerId(rs.getLong("LEDGER_ID"));
                            tmp.setCompanyCode(rs.getString("COMPANY_CODE"));
                            tmp.setCreatedBy(rs.getLong("CREATE_BY"));
                            tmp.setCreateByName(rs.getString("CREATE_BY_NAME"));
                            tmp.setCreationDate(rs.getDate("CREATE_DATE"));
                            tmp.setLastUpdatedBy(rs.getLong("LAST_BY"));
                            tmp.setLastByName(rs.getString("LAST_BY_NAME"));
                            tmp.setLastUpdateDate(rs.getDate("LAST_DATE"));
                            tmp.setLedgerNameList(rs.getString("LEDGER_NAME_LIST"));
                            tmp.setCompanyNameList(rs.getString("COMPANY_NAME_LIST"));

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
     * 保存公司
     * @param companyDto
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map saveCompany(CompanyDto companyDto,  Long userId) {
        companyDto.setDateAndUser(userId);
        List<CompanyEntity> dataList = new ArrayList<CompanyEntity>();
        dataList.add(this.companyDtoToEntity(companyDto));
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.INSERT_COMPANY(?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_GLCS_COMPANY_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, dataList.toArray());
                        cs.setArray(1, vArray);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.NUMBER);
                        cs.registerOutParameter(4, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        Map tmp = new HashMap();
                        cs.execute();
                        tmp.put(EventParm.DB_STATE, cs.getLong(3));
                        tmp.put(EventParm.DB_MSG, cs.getString(4));
                        return tmp;
                    }
                });
        return map;
    }


    /**
     * 删除公司
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param ledgerId
     * @param CompanyCode
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map delCompany(Long  rangeId, Long rangeVerson, Long unitId, Long ledgerId, String CompanyCode ) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.DELETE_COMPANY(?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setLong(1, rangeId);
                        cs.setLong(2, rangeVerson);
                        cs.setLong(3, unitId);
                        cs.setLong(4, ledgerId);
                        cs.setString(5, CompanyCode);
                        cs.registerOutParameter(6, OracleTypes.NUMBER);
                        cs.registerOutParameter(7, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(EventParm.DB_STATE, cs.getLong(6));
                        tmp.put(EventParm.DB_MSG, cs.getString(7));
                        return tmp;
                    }
                });
        return map;
    }

    /**
     * 查询分类账
     * @param ledgerId
     * @param ledgerName
     * @param ledgerType
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getLedgerList(Long ledgerId, String ledgerName, String ledgerType,  String checkSign, String checkCreate,
                                       long userId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<LedgerDto> resultList = (List<LedgerDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.GET_LEDGERS(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == ledgerId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, ledgerId);
                        cs.setString(2,ledgerName);
                        cs.setString(3,ledgerType);
                        cs.setString(4,checkSign);
                        cs.setString(5,checkCreate);
                        cs.setLong(6, userId);// 设置输入参数的值
                        cs.setLong(7, pageNo);// 设置输入参数的值
                        cs.setLong(8, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(9, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(10, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<LedgerDto> results = new ArrayList<LedgerDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(9);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(10));
                        LedgerDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new LedgerDto();
                            tmp.setLedgerId(rs.getLong("LEDGER_ID"));
                            tmp.setLedgerName(rs.getString("NAME"));
                            tmp.setLedgerShortName(rs.getString("SHORT_NAME"));
                            tmp.setLedgerdescrition(rs.getString("DESCRIPTION"));
                            tmp.setLedgerType(rs.getString("OBJECT_TYPE_CODE"));

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
     * 获取EBS的公司段值
     * @param ledgerId
     * @param companyCode
     * @param companyName
     * @param pageNo
     * @param pageSize
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public DataTablesDTO getEbsCompanyList(Long ledgerId, String companyCode, String companyName, String checkSign, String checkCreate,
                                           long userId, long pageNo, long pageSize) {
        DataTablesDTO dt = new DataTablesDTO();
        dt.setPageNo(pageNo);
        dt.setPageSize(pageSize);
        List<EbsCompanyDto> resultList = (List<EbsCompanyDto>) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_GLCS_RANGE_T_PKG.GET_EBS_COMPANYS(?,?,?,?,?,?,?,?,?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        if (null == ledgerId)
                            cs.setBigDecimal(1, null);
                        else
                            cs.setLong(1, ledgerId);
                        cs.setString(2,companyCode);
                        cs.setString(3,companyName);
                        cs.setString(4,checkSign);
                        cs.setString(5,checkCreate);
                        cs.setLong(6, userId);// 设置输入参数的值
                        cs.setLong(7, pageNo);// 设置输入参数的值
                        cs.setLong(8, pageSize);// 设置输入参数的值
                        cs.registerOutParameter(9, OracleTypes.CURSOR);// 注册输出参数的类型
                        cs.registerOutParameter(10, OracleTypes.NUMBER);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        List<EbsCompanyDto> results = new ArrayList<EbsCompanyDto>();
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(9);// 获取游标一行的值
                        dt.setDataTotal(cs.getLong(10));
                        EbsCompanyDto tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new EbsCompanyDto();
                            tmp.setCompanyCode(rs.getString("COMPANY_CODE"));
                            tmp.setCompanyName(rs.getString("COMPANY_NAME"));

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
