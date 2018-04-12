package com.nerin.nims.opt.glcs.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.aria.common.EventParm;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.BusinessException;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.glcs.dto.ReportHeaderDto;
import com.nerin.nims.opt.glcs.dto.ReportLineDto;
import com.nerin.nims.opt.glcs.service.ReportService;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/10/30.
 */
@RestController
@RequestMapping("/api/report")
public class ReportController  {

    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    private ReportService reportService;

    /**
     * 获取期间
     * @param periodName
     * @param ledgerId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "periodNameList", method = RequestMethod.GET)
    public DataTablesDTO queryPeriodNameList(@RequestParam(value = "periodName", required = false, defaultValue = "") String periodName,
                                               @RequestParam(value = "ledgerId", required = false, defaultValue = "") Long ledgerId,
                                               @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                               @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                               HttpServletRequest request
    ) {
        return reportService.getPeriodNameList(periodName, ledgerId, pageNo, pageSize);
    }

    /**
     * 报表头列表
     * @param headerId
     * @param reportId
     * @param reportName
     * @param periodName
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param ledgerId
     * @param companyCode
     * @param type
     * @param reportStatus
     * @param submitStatus
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "reportHeaderList", method = RequestMethod.GET)
    public DataTablesDTO queryReportHeaderList(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                        @RequestParam(value = "reportId", required = false, defaultValue = "") Long reportId,
                                        @RequestParam(value = "reportName", required = false, defaultValue = "") String reportName,
                                        @RequestParam(value = "periodName", required = false, defaultValue = "") String periodName,
                                        @RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                                        @RequestParam(value = "rangeVerson", required = false, defaultValue = "") Long rangeVerson,
                                        @RequestParam(value = "unitId", required = false, defaultValue = "") Long unitId,
                                        @RequestParam(value = "ledgerId", required = false, defaultValue = "") Long ledgerId,
                                        @RequestParam(value = "companyCode", required = false, defaultValue = "") String companyCode,
                                        @RequestParam(value = "type", required = false, defaultValue = "") String type,
                                        @RequestParam(value = "reportStatus", required = false, defaultValue = "") String reportStatus,
                                        @RequestParam(value = "submitStatus", required = false, defaultValue = "") String submitStatus,
                                       @RequestParam(value = "checkSign", required = false, defaultValue = "1") String checkSign,
                                       @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                        @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                        HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();
        return reportService.getReportHeaderList(headerId, reportId, reportName, periodName, rangeId, rangeVerson, unitId,
                                                   ledgerId, companyCode, type, reportStatus, submitStatus, checkSign, userId,
                 pageNo, pageSize
                                                    );
    }

    /**
     * 保存报表头记录
     * @param reportHeaderDto
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveReportHeader", method = RequestMethod.POST)
    public Map saveReportHeader(@RequestBody ReportHeaderDto reportHeaderDto, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = reportService.saveReportHeader (reportHeaderDto, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }


    /**
     * 删除报表头记录
      * @param headerId
     * @return
     */
    @RequestMapping(value = "delReportHeader", method = RequestMethod.POST)
    public Map delReportHeader(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                               @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                               HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = reportService.delReportHeader(headerId,userId);
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 审批报表
     * @param headerId
     * @return
     */
    @RequestMapping(value = "reportHeaderApproved", method = RequestMethod.POST)
    public Map upReportHeaderApproved(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                      @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                      HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = reportService.upReportHeaderApproved(headerId,userId);
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 反审批
     * @param headerId
     * @return
     */
    @RequestMapping(value = "reportHeaderReject", method = RequestMethod.POST)
    public Map upReportHeaderReject(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                    @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                    HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = reportService.upReportHeaderReject(headerId,userId);
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 上报报表
     * @param headerId
     * @return
     */
    @RequestMapping(value = "reportHeaderSubmit", method = RequestMethod.POST)
    public Map upReportHeaderSubmit(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                    @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                    HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = reportService.upReportHeaderSubmit(headerId,userId);
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 获取报表列抬头集合
     * @param colnumNumber
     * @param reportId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "reportColumnList", method = RequestMethod.GET)
    public DataTablesDTO queryReportColumnList(@RequestParam(value = "colnumNumber", required = false, defaultValue = "") String colnumNumber,
                                             @RequestParam(value = "reportId", required = false, defaultValue = "") Long reportId,
                                             @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                             HttpServletRequest request
    ) {
        return reportService.getReportColnumList(reportId,colnumNumber, pageNo, pageSize);
    }

    /**
     * 获取报表行记录
     * @param headerId
     * @param request
     * @return
     */
    @RequestMapping(value = "reportLineList", method = RequestMethod.GET)
    public DataTablesDTO queryReportLineList(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                               HttpServletRequest request
    ) {
        return reportService.getReportLinesList(headerId);
    }

    /**
     * 重新计算报表
     * @param headerId
     * @param userId
     * @param request
     * @return
     */
    @RequestMapping(value = "createReportLines", method = RequestMethod.POST)
    public Map createReportLines(@RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                 @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                 HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();

        Map restFulData = RestFulData.getRestInitData();
        Map reMap = reportService.createReportLines(headerId,userId);
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 保存报表
     * @param reportLineDtos
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveReportLine", method = RequestMethod.POST)
    public Map saveReportLine(@RequestBody List<ReportLineDto> reportLineDtos, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = reportService.addReportLine (reportLineDtos, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

}
