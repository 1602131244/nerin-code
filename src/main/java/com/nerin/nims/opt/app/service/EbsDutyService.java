package com.nerin.nims.opt.app.service;

import com.nerin.nims.opt.app.domain.EbsDuty;
import com.nerin.nims.opt.app.repository.EbsDutyCodeRepository;
import com.nerin.nims.opt.app.repository.EbsDutyRepository;
import com.nerin.nims.opt.app.web.rest.dto.EbsDutySourceDTO;
import com.nerin.nims.opt.app.web.rest.dto.EbsUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 2016/11/24.
 */
@Service
@Transactional
public class EbsDutyService {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EbsDutyRepository ebsDutyRepository;
    @Autowired
    private EbsDutyCodeRepository ebsDutyCodeRepository;

    public void delDuty(long id) {
        ebsDutyRepository.delete(id);
    }

    public void insertDuty(EbsDuty ebsDuty) {
        ebsDutyRepository.save(ebsDuty);
        ebsDutyCodeRepository.deleteCodeWithNull();
    }

    public EbsDuty getEbsDutyById(long id) {
        return ebsDutyRepository.findOne(id);
    }

    public List<EbsDuty> getAllEbsDuty() {
        return ebsDutyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<EbsDutySourceDTO> getEbsDutySource() {
        String sql = "SELECT RESPONSIBILITY_NAME,RESPONSIBILITY_KEY,DESCRIPTION,START_DATE,END_DATE, VERSION,WEB_HOST_NAME,WEB_AGENT_NAME,RESPONSIBILITY_ID,"
                + "ROW_ID,APPLICATION_ID,DATA_GROUP_ID,DATA_GROUP_APPLICATION_ID,MENU_ID,REQUEST_GROUP_ID,GROUP_APPLICATION_ID,CREATION_DATE,CREATED_BY,"
                + "LAST_UPDATE_DATE,LAST_UPDATED_BY,LAST_UPDATE_LOGIN" +
                "  FROM FND_RESPONSIBILITY_VL" +
                "  WHERE (VERSION = '4' OR VERSION = 'W' OR VERSION = 'M')" +
                " ORDER BY RESPONSIBILITY_NAME";
        List<EbsDutySourceDTO> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
            public List<EbsDutySourceDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<EbsDutySourceDTO> result = new ArrayList<EbsDutySourceDTO>();
                EbsDutySourceDTO tmp = null;
                while(rs.next()) {
                    tmp = new EbsDutySourceDTO();
                    tmp.setResponsibilityKey(rs.getString("RESPONSIBILITY_KEY"));
                    tmp.setResponsibilityName(rs.getString("RESPONSIBILITY_NAME"));
                    result.add(tmp);
                }
                return result;
            }});
        return data;
    }

    public List<EbsDutySourceDTO> getUserEbsDutySource(long userId, String identity) {
        String sql = "SELECT FU.USER_NAME, FUR.USER_ID, FR.RESPONSIBILITY_NAME, FR.RESPONSIBILITY_KEY, FR.RESPONSIBILITY_ID"
                + " FROM FND_USER_RESP_GROUPS_DIRECT FUR, FND_RESPONSIBILITY_VL FR, FND_USER FU WHERE FUR.USER_ID = FU.USER_ID"
                + " AND FUR.RESPONSIBILITY_ID = FR.RESPONSIBILITY_ID AND FUR.RESPONSIBILITY_APPLICATION_ID = FR.APPLICATION_ID"
                + " AND (VERSION = '4' OR VERSION = 'W' OR VERSION = 'M' OR VERSION = 'H') AND SYSDATE BETWEEN NVL(FUR.START_DATE, SYSDATE) AND"
                + " NVL(FUR.END_DATE, SYSDATE) AND SYSDATE BETWEEN NVL(FR.START_DATE, SYSDATE) AND NVL(FR.END_DATE, SYSDATE) AND (FUR.USER_ID = " + userId +")"
                + " AND EXISTS (SELECT 'X' FROM (SELECT T2.DUTY_CODE FROM CUX.NERIN_EBS_DUTY T, CUX.NERIN_EBS_DUTYCODE T2 WHERE T.ID = T2.EBS_DUTY_ID"
                + " AND T.NERIN_IDENTITY = '" + identity + "') T WHERE T.DUTY_CODE = FR.RESPONSIBILITY_KEY) ORDER BY USER_ID, RESPONSIBILITY_APPLICATION_ID, FR.RESPONSIBILITY_ID,"
                + " SECURITY_GROUP_ID";
        List<EbsDutySourceDTO> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
            public List<EbsDutySourceDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<EbsDutySourceDTO> result = new ArrayList<EbsDutySourceDTO>();
                EbsDutySourceDTO tmp = null;
                while(rs.next()) {
                    tmp = new EbsDutySourceDTO();
                    tmp.setResponsibilityKey(rs.getString("RESPONSIBILITY_KEY"));
                    tmp.setResponsibilityName(rs.getString("RESPONSIBILITY_NAME"));
                    result.add(tmp);
                }
                return result;
            }});
        return data;
    }
}
