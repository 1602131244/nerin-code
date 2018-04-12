package com.nerin.nims.opt.card.service;

import com.nerin.nims.opt.card.dto.costDTO;
import com.nerin.nims.opt.card.dto.departmentDTO;
import com.nerin.nims.opt.card.dto.personDTO;
import com.nerin.nims.opt.card.dto.testDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/7.
 */
@Component
@Transactional
public class cardService {

    public List<costDTO> test(String groupId,String  personId) {
        String url = "jdbc:sqlserver://192.168.15.150:1433;databaseName=yikatong;user=sa;password=nerin_123";//sa身份连接
       // String url2 = "jdbc:sqlserver://127.0.0.1:1433;databaseName=DBLinkTest;integratedSecurity=true;";//windows集成模式连接
        // Declare the JDBC objects.
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<costDTO> results = new ArrayList<costDTO>();
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(url);

            String SQL = "SELECT  gp.base_perid,gp.base_pername,gp.base_perno,gp.base_groupid,gg.base_groupname " +
                    "FROM general_personnel gp, general_group gg where gp.base_groupid=gg.base_groupid and gg.base_groupid= '" + groupId + "'"+
                    " and gp.base_perid='"+personId+"'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);
            costDTO tmp = null;
            while (rs.next()) {
                tmp = new costDTO();
                //tmp.put("name", rs.getString(1));
                tmp.setBase_pername(rs.getString("base_pername"));
                tmp.setBase_perno(rs.getString("base_perno"));
                tmp.setBase_groupid(rs.getString("base_groupid"));
                tmp.setBase_groupname(rs.getString("base_groupname"));
               // tmp.setCost(rs.getFloat("cost"));
                results.add(tmp);
            }
            rs.close();

        }


        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception e) {
                }
            if (stmt != null)
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            if (con != null)
                try {
                    con.close();
                } catch (Exception e) {
                }
        }
        return results;
    }
//部门下拉列表
    public List<departmentDTO> departmentList() {
        String url = "jdbc:sqlserver://192.168.15.150:1433;databaseName=yikatong;user=sa;password=nerin_123";//sa身份连接

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<departmentDTO> results = new ArrayList<departmentDTO>();
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(url);
            String SQL = "select base_groupid,base_groupname from dbo.general_group g where (g.base_isdel<>'1' and base_level=2) or g.base_groupid='0002'";
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);
            departmentDTO tmp = null;
            while (rs.next()) {
                tmp = new departmentDTO();
                tmp.setGroupId(rs.getString("base_groupid"));
                tmp.setGroupName(rs.getString("base_groupname"));
                results.add(tmp);
            }
            rs.close();
        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception e) {
                }
            if (stmt != null)
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            if (con != null)
                try {
                    con.close();
                } catch (Exception e) {
                }
        }
        return results;
    }
//人员下拉列表
    public List<personDTO> personList(String departmentId) {
        String url = "jdbc:sqlserver://192.168.15.150:1433;databaseName=yikatong;user=sa;password=nerin_123";//sa身份连接

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        List<personDTO> results = new ArrayList<personDTO>();
        try {

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(url);
            String SQL;
            if (departmentId != "-2") {
                 SQL = "select g.base_pername,g.base_perid from general_personnel g where g.base_groupid='" + departmentId + "' and base_state=1";
            }else{
                 SQL = "select g.base_pername,g.base_perid from general_personnel g where   base_state=1";
            }
            stmt = con.createStatement();
            rs = stmt.executeQuery(SQL);
            personDTO tmp = null;
            while (rs.next()) {
                tmp = new personDTO();
                tmp.setName(rs.getString("base_pername"));
                tmp.setId(rs.getString("base_perid"));
                results.add(tmp);
            }
            rs.close();
        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null)
                try {
                    rs.close();
                } catch (Exception e) {
                }
            if (stmt != null)
                try {
                    stmt.close();
                } catch (Exception e) {
                }
            if (con != null)
                try {
                    con.close();
                } catch (Exception e) {
                }
        }
        return results;
    }
}
