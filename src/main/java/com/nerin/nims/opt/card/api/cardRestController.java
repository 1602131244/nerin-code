package com.nerin.nims.opt.card.api;

import com.nerin.nims.opt.card.dto.costDTO;
import com.nerin.nims.opt.card.dto.departmentDTO;
import com.nerin.nims.opt.card.dto.personDTO;
import com.nerin.nims.opt.card.dto.testDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nerin.nims.opt.card.service.*;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/7.
 */
@RestController
@RequestMapping("/api/card")
public class cardRestController {
    @Autowired
    private  cardService cardService;

    @RequestMapping(value = "testsql", method = RequestMethod.GET)
    public List<costDTO> test(@RequestParam(value = "groupId", required = false, defaultValue = "") String groupId,
                              @RequestParam(value = "personId", required = false, defaultValue = "") String personId) {
        return cardService.test(groupId,personId);
    }
    //部门下拉列表
    @RequestMapping(value = "departmentList", method = RequestMethod.GET)
    public List<departmentDTO> departmentList() {
        return cardService.departmentList();
    }
    //人员下拉列表
    @RequestMapping(value = "person", method = RequestMethod.GET)
    public List<personDTO> person(@RequestParam(value = "departmentId", required = false, defaultValue = "") String departmentId) {
        return cardService.personList(departmentId);
    }
}
