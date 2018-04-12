package com.nerin.nims.opt.glcs.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.glcs.service.EntryService;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/3/2.
 */
public class EntryController {

    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    private EntryService entryService;

    /**
     * 查报表行项目
     * @param reportID
     * @param reportName
     * @param reportRowId
     * @param reportItemNo
     * @param reportItemName
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "rangeList", method = RequestMethod.GET)
    public DataTablesDTO queryReportItemList(@RequestParam(value = "reportID", required = false, defaultValue = "") Long reportID,
                                        @RequestParam(value = "reportName", required = false, defaultValue = "") String reportName,
                                        @RequestParam(value = "reportRowId", required = false, defaultValue = "") Long reportRowId,
                                        @RequestParam(value = "reportItemNo", required = false, defaultValue = "") String reportItemNo,
                                        @RequestParam(value = "reportItemName", required = false, defaultValue = "0") String reportItemName,
                                        @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                        HttpServletRequest request
    ) {
        return entryService.getReportItemList(reportID, reportName, reportRowId, reportItemNo, reportItemName,  pageNo, pageSize);
    }

    /**
     * 查报表金额类型
     * @param amountTypeName
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "amountTypeList", method = RequestMethod.GET)
    public DataTablesDTO queryAmountTypeList(@RequestParam(value = "amountTypeName", required = false, defaultValue = "") String amountTypeName,
                                        @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                        HttpServletRequest request
    ) {
        return entryService.getReportAmountTypeList(amountTypeName, pageNo, pageSize);
    }

}
