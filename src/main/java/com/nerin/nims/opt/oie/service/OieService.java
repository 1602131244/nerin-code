package com.nerin.nims.opt.oie.service;
import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/27.
 */
@Component
@Transactional
public class OieService {
    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    /**
     * 更新OA流程的签收人、签收日期
     * @param requestId
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map updateOaRequestOie(String requestType, Long  requestId, Long userId, String personNumber, String Type) {
        Map map = (Map) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_PA_DEPT_BUDGET_PKG.update_oa_request_oie(?,?,?,?,?,?,?)}";
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, requestType);
                        cs.setLong(2, requestId);
                        cs.setLong(3, userId);
                        cs.setString(4, personNumber);
                        cs.setString(5, Type);
                        cs.registerOutParameter(6, OracleTypes.NUMBER);
                        cs.registerOutParameter(7, OracleTypes.VARCHAR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        Map tmp = new HashMap();
                        tmp.put(NbccParm.DB_STATE, cs.getLong(6));
                        tmp.put(NbccParm.DB_MSG, cs.getString(7));
                        return tmp;
                    }
                });
        return map;
    }
}
