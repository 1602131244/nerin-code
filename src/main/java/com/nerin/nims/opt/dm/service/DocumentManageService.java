package com.nerin.nims.opt.dm.service;

import com.nerin.nims.opt.dm.dto.DocumentManageDto;
import com.nerin.nims.opt.dm.module.DocumentManageEntity;
import com.nerin.nims.opt.nbcc.common.NbccParm;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2016/12/13.
 */
@Component
@Transactional
public class DocumentManageService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    private DocumentManageEntity documentManageDtoToEntity (DocumentManageDto documentManage) {
        DocumentManageEntity entity = new DocumentManageEntity();
        try {
            PropertyUtils.copyProperties(entity, documentManage);
            entity.setHeaderId(documentManage.getHeaderId());
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
     * 保存文档下载申请列表
     * @param documentManageDtos
     * @param userId
     * @return
     */
    @SuppressWarnings("unchecked")
    @Transactional(rollbackFor = Exception.class)
    public Map addDocumentManage(List<DocumentManageDto> documentManageDtos, Long userId) {
        List<DocumentManageEntity> inData = documentManageDtos.stream().map(chapter -> {
            chapter.setDateAndUser(userId);
            DocumentManageEntity tmp = this.documentManageDtoToEntity(chapter);
            return tmp;
        }).collect(Collectors.toList());
        Map map = (HashMap)  jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        OracleConnection or = (OracleConnection)con.getMetaData().getConnection();
                        String storedProc = "{call APPS.CUX_DOCUMENT_MANAGE_PKG.insert_document_manage(?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        ArrayDescriptor tabDesc = ArrayDescriptor.createDescriptor("APPS.CUX_DOCUMENT_MANAGE_TBL", or);
                        ARRAY vArray = new ARRAY(tabDesc, or, inData.toArray());
                        cs.setArray(1, vArray);
                        cs.setLong(2, userId);
                        cs.registerOutParameter(3, OracleTypes.VARCHAR);
                        cs.registerOutParameter(4, OracleTypes.NUMBER);
                        cs.registerOutParameter(5, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_URL, cs.getString(3));
                        tmp.put(NbccParm.DB_STATE, cs.getLong(4));
                        tmp.put(NbccParm.DB_MSG, cs.getString(5));
                        return tmp;
                    }
                });
        return map;
    }


}
