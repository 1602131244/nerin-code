package com.nerin.nims.opt.app.web.rest;

import com.nerin.nims.opt.app.domain.Menu;
import com.nerin.nims.opt.app.domain.UserMenu;
import com.nerin.nims.opt.app.repository.AuthorityRepository;
import com.nerin.nims.opt.app.repository.MenuRepository;
import com.nerin.nims.opt.app.repository.UserRepository;
import com.nerin.nims.opt.app.security.SecurityUtils;
import com.nerin.nims.opt.app.service.MenuService;
import com.nerin.nims.opt.app.web.rest.dto.EbsUserDTO;
import com.nerin.nims.opt.app.web.rest.dto.MenuDTO;
import com.nerin.nims.opt.app.web.rest.dto.MenuTreeDTO;
import com.nerin.nims.opt.base.rest.NerinRestBase;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Zach on 16/5/22.
 */
@RestController
@RequestMapping("/api")
public class MenuResource extends NerinRestBase {
    @Autowired
    private MenuService menuService;

    @RequestMapping(value = "/menus/tree", method = RequestMethod.GET)
    public List<Menu> getAllMenus() {
        return menuService.getAllMenus();
    }

    @RequestMapping(value = "/menus/tree/front", method = RequestMethod.GET)
    public List<Menu> getFontMenus(HttpServletRequest request) {
        return menuService.getMenusByType("front", request);
    }

    @RequestMapping(value = "/menus/tree/back", method = RequestMethod.GET)
    public List<Menu> getBackMenus(HttpServletRequest request) {
        return menuService.getMenusByType("back", request);
    }

    @RequestMapping(value = "/menus/list", method = RequestMethod.GET)
    public List<MenuDTO> getAllMenusList(Pageable pageable) {
        return menuService.getAllMenuList(pageable);
    }

    @RequestMapping(value = "/menu/add", method = RequestMethod.POST)
    public Menu addMenu(@RequestBody MenuDTO menuDTO) {
        return menuService.saveMenu(menuDTO);

    }

    @RequestMapping(value = "/menus/delete/{menuIds}", method = RequestMethod.DELETE)
    public Map deleteMenu(@PathVariable("menuIds") String menuIds) {
        if (StringUtils.isNotBlank(menuIds)) {
            String[] ids = menuIds.split(",");
            boolean opration = menuService.deleteMenus(Arrays.stream(ids).map(id -> {
                MenuDTO menuDTO = new MenuDTO();
                menuDTO.setId(Long.parseLong(id));
                return menuDTO;
            }).collect(Collectors.toList()));
            return responseMessageByOpration(opration);
        } else {
            return responseMessage(-1, "请传入菜单ID");
        }
    }

    @RequestMapping(value = "/menus/allBackMenuForTree", method = RequestMethod.GET)
    public List<MenuTreeDTO> queryAllBackMenuForTree() {
        return menuService.getBackMenuForTree("back");
    }

    @RequestMapping(value = "/menus/queryBackMenuId", method = RequestMethod.GET)
    public List<Long> queryUserMenuId(@RequestParam(value = "userNo", required = false, defaultValue = "") String userNo,
                                      HttpServletRequest request) {
        return menuService.getUserMenuIds(userNo, request);
    }

    @RequestMapping(value = "/menus/saveUserMenuId", method = RequestMethod.POST)
    public String saveUserMenuId(@RequestBody List<UserMenu> datas) {
        menuService.saveUserMenuId(datas);
        return "true";
    }

    @RequestMapping(value = "/menus/ebsUser", method = RequestMethod.GET)
    public List<EbsUserDTO> queryEbsUser(@RequestParam(value = "userNo", required = false, defaultValue = "") String userNo,
                                         @RequestParam(value = "userName", required = false, defaultValue = "") String userName) {
        return menuService.getEbsUser(userNo, userName);
    }
}
