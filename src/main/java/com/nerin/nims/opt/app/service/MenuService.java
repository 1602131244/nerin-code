package com.nerin.nims.opt.app.service;

import com.nerin.nims.opt.app.domain.Menu;
import com.nerin.nims.opt.app.domain.UserMenu;
import com.nerin.nims.opt.app.repository.MenuRepository;
import com.nerin.nims.opt.app.repository.UserMenuRepository;
import com.nerin.nims.opt.app.security.SecurityUtils;
import com.nerin.nims.opt.app.web.rest.dto.EbsUserDTO;
import com.nerin.nims.opt.app.web.rest.dto.MenuDTO;
import com.nerin.nims.opt.app.web.rest.dto.MenuTreeDTO;
import com.nerin.nims.opt.base.service.NerinServiceBase;

import com.nerin.nims.opt.base.util.SessionUtil;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Zach on 16/5/22.
 */
@Service
@Transactional
public class MenuService extends NerinServiceBase {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private UserMenuRepository userMenuRepository;

    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    public List<Menu> getAllMenusByParentId() {
        return menuRepository.findByParentId(0l);
    }

    @Transactional(readOnly = true)
    public List<EbsUserDTO> getEbsUser(String userNo, String userName) {
        String sql = "SELECT T.USER_NAME as SYS_ACCOUNT, T.PERSON_NAME as LAST_NAME from CUX_USER_V T WHERE 1 = 1"; //CUX_HR_EMPLOYEE_V2 t.HIRE_DATE_ATTR IS NOT NULL AND t.primary_flag = 'Y'
        if (StringUtils.isNotEmpty(userNo))
            sql += " and t.USER_NAME like '%" + userNo + "%'";
        if (StringUtils.isNotEmpty(userName))
            sql += " and t.PERSON_NAME like '%" + userName + "%'";
        List<EbsUserDTO> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
            public List<EbsUserDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<EbsUserDTO> result = new ArrayList<EbsUserDTO>();
                EbsUserDTO tmp = null;
                while(rs.next()) {
                    tmp = new EbsUserDTO();
//                    tmp.setEmpNum(rs.getString("EMP_NUM"));
                    tmp.setLastName(rs.getString("LAST_NAME"));
                    tmp.setSysAccount(rs.getString("SYS_ACCOUNT"));
//                    tmp.setOrgName(rs.getString("ORG_NAME"));
                    result.add(tmp);
                }
                return result;
            }});
        return data;
    }

    public void saveUserMenuId(List<UserMenu> datas) {
        if (null != datas && 0 < datas.size()) {
            userMenuRepository.deleteByUserNo(datas.get(0).getUserNo());
            userMenuRepository.save(datas);
        } else {
            userMenuRepository.deleteByUserNo(datas.get(0).getUserNo());
        }
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findOne(0l).getChildren().stream().filter(menu -> menu.getId() != 0).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Long> getUserMenuIds(String userNo, HttpServletRequest request) {
        String sql = "select t.menu_id from cux.NERIN_USER_MENU t where t.user_no ='" +  userNo + "'";
        List<Long> data = jdbcTemplate.query(sql, new ResultSetExtractor<List>() {
            public List<Long> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Long> result = new ArrayList<Long>();
                while(rs.next()) {
                    result.add(rs.getLong("MENU_ID"));
                }
                return result;
            }});
        return data;
    }

    public List<MenuTreeDTO> getBackMenuForTree(String type) {
        List<Menu> data = menuRepository.findByType(type);
        List<MenuTreeDTO> tmpList = new ArrayList<MenuTreeDTO>();
        for (Menu m : data) {
            MenuTreeDTO m1 = new MenuTreeDTO();
            try {
                PropertyUtils.copyProperties(m1, m);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            m1.setTitle(m1.getName());
            m1.setIcon(null);
            m1.setChildren(new ArrayList<MenuTreeDTO>());
            tmpList.add(m1);
        }
        List<MenuTreeDTO> newList = new ArrayList<MenuTreeDTO>();
        for(MenuTreeDTO node1 : tmpList){
            boolean mark = false;
            for(MenuTreeDTO node2 : tmpList){
                if(null != node1.getParentId() && node1.getParentId().equals(node2.getId())){
                    mark = true;
                    if(node2.getChildren() == null)
                        node2.setChildren(new ArrayList<MenuTreeDTO>());
                    node2.getChildren().add(node1);
                    break;
                }
            }
            if(!mark){
                newList.add(node1);
            }
        }
        return newList;
    }

    public List<Menu> getMenusByType(String type, HttpServletRequest request) {
        if ("back".equals(type)) {
            List<Menu> menuIds = menuRepository.findByUserNo(SessionUtil.getLdapUserInfo(request).getEmployeeNo());
            List<Menu> tmpList = new ArrayList<Menu>();
            for (Menu m : menuIds) {
                Menu m1 = new Menu();
                try {
                    PropertyUtils.copyProperties(m1, m);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                m1.setChildren(new ArrayList<Menu>());
                tmpList.add(m1);
            }
            List<Menu> newList = new ArrayList<Menu>();
            for(Menu node1 : tmpList){
                boolean mark = false;
                for(Menu node2 : tmpList){
                    if(null != node1.getParentId() && node1.getParentId().equals(node2.getId())){
                        mark = true;
                        if(node2.getChildren() == null)
                            node2.setChildren(new ArrayList<Menu>());
                        node2.getChildren().add(node1);
                        break;
                    }
                }
                if(!mark){
                    newList.add(node1);
                }
            }
            String loginName = SecurityUtils.getCurrentUserLogin();
            if (null != loginName && !"anonymousUser".equals(loginName)) {
                if (this.isInteger(loginName.substring(0, 1))) {
                    return newList;
                } else {
                    return getAllMenus().stream().filter(menu -> type.equals(menu.getType())).collect(Collectors.toList());
                }
            } else {
                return newList;
            }
        } else {
            return getAllMenus().stream().filter(menu -> type.equals(menu.getType())).collect(Collectors.toList());
        }
    }

    private boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<MenuDTO> getAllMenuList(Pageable pageable) {
        Pageable pageRequest = new PageRequest(0, 300, new Sort(Sort.Direction.ASC, "orderNo"));
        return menuRepository.findAll(pageRequest).getContent().stream().map(menu -> {
            MenuDTO menuDTO = new MenuDTO();
            BeanUtils.copyProperties(menu, menuDTO);
            menuDTO.setParentName(menu.getParent().getName());
            return menuDTO;
        }).collect(Collectors.toList());
    }

    public Menu saveMenu(MenuDTO menuDTO) {
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        menu.setParent(menuRepository.findOne(menuDTO.getParentId()));
        return menuRepository.save(menu);
    }

    public boolean deleteMenus(List<MenuDTO> menuDTOss) {
        this.getLogger().error("in delete" + menuDTOss.get(0).getId());
        try {
            menuDTOss.stream().forEach(menuDTO -> {
                this.getLogger().error("in 1 delete" + menuDTO.getId());
                Menu menu = menuRepository.findOne(menuDTO.getId());
                this.getLogger().error("in 2 delete" + menu.getId());
                menuRepository.delete(menu);
            });

        } catch (Exception e) {
            this.getLogger().error(ExceptionUtils.getFullStackTrace(e));
            return false;
        }
        return true;
    }
}
