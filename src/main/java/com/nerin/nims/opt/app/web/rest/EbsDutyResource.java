package com.nerin.nims.opt.app.web.rest;

import com.nerin.nims.opt.app.domain.EbsDuty;
import com.nerin.nims.opt.app.domain.EbsDutyCode;
import com.nerin.nims.opt.app.service.EbsDutyService;
import com.nerin.nims.opt.app.web.rest.dto.EbsDutySourceDTO;
import com.nerin.nims.opt.base.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yinglgu on 2016/11/24.
 */
@RestController
@RequestMapping("/api/ebsDuty")
public class EbsDutyResource {

    @Autowired
    private EbsDutyService ebsDutyService;

    @RequestMapping(value = "queryAllEbsDuty", method = RequestMethod.GET)
    public List<EbsDuty> queryAllEbsDuty() {
        return ebsDutyService.getAllEbsDuty();
    }

    @RequestMapping(value = "queryEbsDuty", method = RequestMethod.GET)
    public Map queryEbsDuty(@RequestParam(value = "id", required = false, defaultValue = "") long id) {
        List<EbsDutySourceDTO> sourceData = ebsDutyService.getEbsDutySource();
        EbsDuty data = ebsDutyService.getEbsDutyById(id);
        List<EbsDutyCode> ebsCodes = data.getEbsCodes();
        List<EbsDutySourceDTO> dList = new ArrayList<EbsDutySourceDTO>();
        List<EbsDutySourceDTO> yList = new ArrayList<EbsDutySourceDTO>();

        for (EbsDutyCode e : ebsCodes) {
            for (EbsDutySourceDTO t : sourceData) {
                if (e.getDutyCode().equals(t.getResponsibilityKey())) {
                    yList.add(t);
                    sourceData.remove(t);
                    break;
                }
            }
        }
        dList.addAll(sourceData);
        Map d = new HashMap();
        d.put("data", data);
        d.put("dList", dList);
        d.put("yList", yList);
        return d;
    }

    @RequestMapping(value = "queryEbsDutySource", method = RequestMethod.GET)
    public List<EbsDutySourceDTO> queryEbsDutySource() {
        return ebsDutyService.getEbsDutySource();
    }

    @RequestMapping(value = "saveEbsDuty", method = RequestMethod.POST)
    public String saveEbsDuty(@RequestBody EbsDuty ebsDuty) {
        ebsDutyService.insertDuty(ebsDuty);
        return "true";
    }

    @RequestMapping(value = "delEbsDuty", method = RequestMethod.POST)
    public String delEbsDuty(@RequestParam(value = "ids", required = false, defaultValue = "") String ids) {
        String[] id = ids.split(",");
        for (String s : id)
            ebsDutyService.delDuty(Long.parseLong(s));
        return "true";
    }

    @RequestMapping(value = "getUserEbsDutySource", method = RequestMethod.GET)
    public List<EbsDutySourceDTO> getUserEbsDutySource(@RequestParam(value = "identity", required = false, defaultValue = "") String identity,
                                                       HttpServletRequest request) {
        return ebsDutyService.getUserEbsDutySource(SessionUtil.getLdapUserInfo(request).getUserId(), identity);
    }
}
