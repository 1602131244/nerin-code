package com.nerin.nims.opt.glcs.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.aria.common.EventParm;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.BusinessException;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.cqs.dto.DataTablesDTO;
import com.nerin.nims.opt.glcs.dto.CompanyDto;
import com.nerin.nims.opt.glcs.dto.RangeDto;
import com.nerin.nims.opt.glcs.dto.UnitDto;
import com.nerin.nims.opt.glcs.service.RangeApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/12.
 */
@RestController
@RequestMapping("/api/rangeRest")
public class RangeApplyController {

    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    private RangeApplyService rangeApplyService;

    /**
     * 获取合并范围列表
     * @param rangeId
     * @param rangeNumber
     * @param rangeName
     * @param rangeVerson
     * @param checkSign  判定是否检查查询权限  1 检查  0 不检查
     * @param checkCreate  判定是否检查创建报表权限  1 检查  0 不检查
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "rangeList", method = RequestMethod.GET)
    public DataTablesDTO queryRangeList(@RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                                         @RequestParam(value = "rangeNumber", required = false, defaultValue = "") String rangeNumber,
                                         @RequestParam(value = "rangeName", required = false, defaultValue = "") String rangeName,
                                         @RequestParam(value = "rangeVerson", required = false, defaultValue = "") Long rangeVerson,
                                        @RequestParam(value = "rangeDate", required = false, defaultValue = "") String rangeDate,
                                        @RequestParam(value = "checkSign", required = false, defaultValue = "0") String checkSign,
                                        @RequestParam(value = "checkCreate", required = false, defaultValue = "0") String checkCreate,
                                        @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                         @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                         HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();
        return rangeApplyService.getRangeList(rangeId, rangeNumber, rangeName, rangeVerson, rangeDate, checkSign, checkCreate, userId, pageNo, pageSize);
    }

    /**
     * 保存合并范围
     * @param rangeDto
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveRange", method = RequestMethod.POST)
    public Map saveRange(@RequestBody RangeDto rangeDto, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = rangeApplyService.saveRange (rangeDto,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 删除合并范围
     * @param rangeId
     * @param verson
     * @return
     */
    @RequestMapping(value = "delRange", method = RequestMethod.POST)
    public Map delRange(@RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                            @RequestParam(value = "verson", required = false, defaultValue = "") Long verson) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = rangeApplyService.delRange(rangeId,verson);
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 增加新版本
     * @param rangeId
     * @param verson
     * @param request
     * @return
     */
    @RequestMapping(value = "copyRange", method = RequestMethod.POST)
    public Map copyRange(@RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                        @RequestParam(value = "verson", required = false, defaultValue = "") Long verson ,
                         HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = rangeApplyService.copyRange(rangeId,verson,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_DATA, reMap.get(EventParm.DB_SID) + "");
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 启用合并范围
     * @param rangeId
     * @param verson
     * @param startDate
     * @param request
     * @return
     */
    @RequestMapping(value = "rangeEnable", method = RequestMethod.POST)
    public Map rangeEnable(@RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                        @RequestParam(value = "verson", required = false, defaultValue = "") Long verson,
                           @RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
                           HttpServletRequest request
    ) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = rangeApplyService.rangeEnable(rangeId,verson,startDate,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 反启用合并范围
     * @param rangeId
     * @param verson
     * @param request
     * @return
     */
    @RequestMapping(value = "rangeDisable", method = RequestMethod.POST)
    public Map rangeDisable(@RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                           @RequestParam(value = "verson", required = false, defaultValue = "") Long verson,
                           HttpServletRequest request
    ) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = rangeApplyService.rangeDisable(rangeId,verson,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 查询合并单元
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param checkSign  判定是否检查查询权限  1 检查  0 不检查
     * @param checkCreate  判定是否检查创建报表权限  1 检查  0 不检查
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "unitListTree", method = RequestMethod.GET)
    public DataTablesDTO queryUnitListTree(@RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                                        @RequestParam(value = "rangeVerson", required = false, defaultValue = "") Long rangeVerson,
                                       @RequestParam(value = "unitId", required = false, defaultValue = "") Long unitId,
                                           @RequestParam(value = "checkSign", required = false, defaultValue = "0") String checkSign,
                                           @RequestParam(value = "checkCreate", required = false, defaultValue = "0") String checkCreate,
                                           @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                        @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                        HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();
        return rangeApplyService.getUnitListTree(rangeId, rangeVerson, unitId, checkSign, checkCreate, userId, pageNo, pageSize);
    }

    /**
     * 查询合并单元
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param checkSign  判定是否检查查询权限  1 检查  0 不检查
     * @param checkCreate  判定是否检查创建报表权限  1 检查  0 不检查
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "unitList", method = RequestMethod.GET)
    public DataTablesDTO queryUnitList(@RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                                       @RequestParam(value = "rangeVerson", required = false, defaultValue = "") Long rangeVerson,
                                       @RequestParam(value = "unitId", required = false, defaultValue = "") Long unitId,
                                       @RequestParam(value = "checkSign", required = false, defaultValue = "0") String checkSign,
                                       @RequestParam(value = "checkCreate", required = false, defaultValue = "0") String checkCreate,
                                       @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                       @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                       HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();
        return rangeApplyService.getUnitList(rangeId, rangeVerson, unitId, checkSign, checkCreate, userId, pageNo, pageSize);
    }

    /**
     * 保存合并单元
     * @param unitDto
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveUnit", method = RequestMethod.POST)
    public Map saveUnit(@RequestBody UnitDto unitDto, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = rangeApplyService.saveUnit (unitDto,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 删除合并单元
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @return
     */
    @RequestMapping(value = "delUnit", method = RequestMethod.POST)
    public Map delUnit(@RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                        @RequestParam(value = "rangeVerson", required = false, defaultValue = "") Long rangeVerson,
                       @RequestParam(value = "unitId", required = false, defaultValue = "") Long unitId
    ) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = rangeApplyService.delUnit(rangeId,rangeVerson,unitId);
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 查询公司列表
     * @param rangeId
     * @param rangeVerson
     * @param unitId
     * @param ledgerId
     * @param companyCode
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "companyList", method = RequestMethod.GET)
    public DataTablesDTO queryCompanyList(@RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                                        @RequestParam(value = "rangeVerson", required = false, defaultValue = "") Long rangeVerson,
                                          @RequestParam(value = "unitId", required = false, defaultValue = "") Long unitId,
                                          @RequestParam(value = "unitFlag", required = false, defaultValue = "0") String unitFlag,
                                          @RequestParam(value = "ledgerId", required = false, defaultValue = "") Long ledgerId,
                                          @RequestParam(value = "companyCode", required = false, defaultValue = "") String companyCode,
                                        @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                        HttpServletRequest request
    ) {
        return rangeApplyService.getCompanyList(rangeId, rangeVerson, unitId, unitFlag, ledgerId, companyCode, pageNo, pageSize);
    }


    /**
     * 保存公司
     * @param companyDto
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveCompany", method = RequestMethod.POST)
    public Map saveCompany(@RequestBody CompanyDto companyDto, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = rangeApplyService.saveCompany (companyDto,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 删除公司
     * @param rangeId
     * @param rangeVerson
     * @param unit
     * @param ledgerId
     * @param companyCode
     * @return
     */
    @RequestMapping(value = "delCompany", method = RequestMethod.POST)
    public Map delCompany(@RequestParam(value = "rangeId", required = false, defaultValue = "") Long rangeId,
                        @RequestParam(value = "rangeVerson", required = false, defaultValue = "") Long rangeVerson,
                          @RequestParam(value = "unit", required = false, defaultValue = "") Long unit,
                          @RequestParam(value = "ledgerId", required = false, defaultValue = "") Long ledgerId,
                          @RequestParam(value = "companyCode", required = false, defaultValue = "") String companyCode
                          ) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = rangeApplyService.delCompany(rangeId, rangeVerson, unit, ledgerId, companyCode);
        if (1 != (Long) reMap.get(EventParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(EventParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(EventParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 获取分类账
     * @param ledgerId
     * @param ledgerName
     * @param ledgerType
     * @param checkSign  判定是否检查查询权限  1 检查  0 不检查
     * @param checkCreate  判定是否检查创建报表权限  1 检查  0 不检查
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "ledgerList", method = RequestMethod.GET)
    public DataTablesDTO queryLedgerList(@RequestParam(value = "ledgerId", required = false, defaultValue = "") Long ledgerId,
                                          @RequestParam(value = "ledgerName", required = false, defaultValue = "") String ledgerName,
                                          @RequestParam(value = "ledgerType", required = false, defaultValue = "L") String ledgerType,
                                         @RequestParam(value = "checkSign", required = false, defaultValue = "0") String checkSign,
                                         @RequestParam(value = "checkCreate", required = false, defaultValue = "0") String checkCreate,
                                         @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                          @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                          HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();

        return rangeApplyService.getLedgerList(ledgerId, ledgerName, ledgerType, checkSign, checkCreate, userId,  pageNo, pageSize);
    }

    /**
     * 获取EBS公司断值集
     * @param ledgerId
     * @param companyCode
     * @param companyName
     * @param checkSign  判定是否检查查询权限  1 检查  0 不检查
     * @param checkCreate  判定是否检查创建报表权限  1 检查  0 不检查
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param request
     * @return
     */
    @RequestMapping(value = "ebsCompanyList", method = RequestMethod.GET)
    public DataTablesDTO queryEbsCompanyList(@RequestParam(value = "ledgerId", required = false, defaultValue = "") Long ledgerId,
                                         @RequestParam(value = "companyCode", required = false, defaultValue = "") String companyCode,
                                         @RequestParam(value = "companyName", required = false, defaultValue = "") String companyName,
                                             @RequestParam(value = "checkSign", required = false, defaultValue = "0") String checkSign,
                                             @RequestParam(value = "checkCreate", required = false, defaultValue = "0") String checkCreate,
                                             @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                             @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                         HttpServletRequest request
    ) {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();

        return rangeApplyService.getEbsCompanyList(ledgerId, companyCode, companyName, checkSign, checkCreate, userId, pageNo, pageSize);
    }
}
