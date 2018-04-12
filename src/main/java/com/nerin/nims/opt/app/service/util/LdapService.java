package com.nerin.nims.opt.app.service.util;

import com.nerin.nims.opt.base.rest.LdapUser;
import oracle.jdbc.OracleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by yinglgu on 7/28/2016.
 */
@Service
public class LdapService {

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public LdapUser getLdapUser(String no) {
        LdapUser user = (LdapUser) jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call APPS.CUX_NAVI_BASE_INFO_PKG.person_base_info(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, no);
                        cs.registerOutParameter(2, OracleTypes.CURSOR);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException,DataAccessException {
                        cs.execute();
                        ResultSet rs = (ResultSet) cs.getObject(2);
                        LdapUser tmp = null;
                        while (rs.next()) {// 转换每行的返回值到Map中
                            tmp = new LdapUser();
                            tmp.setUserId(rs.getLong("USER_ID"));
                            tmp.setPersonId(rs.getLong("PERSON_ID"));
                            tmp.setUserName(rs.getString("USER_NAME"));
                            tmp.setFullName(rs.getString("FULL_NAME"));
                            tmp.setDeptOrgId(rs.getString("DEPT_ORG_ID"));
                            tmp.setDeptOrgName(rs.getString("DEPT_ORG_NAME"));
                            tmp.setMobile(rs.getString("MOBILE"));
                            tmp.setEmail(rs.getString("EMAIL"));
                            tmp.setEmployeeNo(rs.getString("EMPLOYEE_NO"));
                        }
                        rs.close();
                        return tmp;
                    }
                });
        return user;
    }
}
