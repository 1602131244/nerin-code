package com.nerin.nims.opt.tsc.tc.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.BusinessException;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import com.nerin.nims.opt.tsc.tc.dto.*;
import com.nerin.nims.opt.tsc.tc.service.TcService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yinglgu on 11/14/2016.
 */
@RestController
@RequestMapping("/api/tsc/tc")
public class TcController {
    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    private TcService tcService;

    /**
     * 提交
     * @param customerId
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "submitHeader", method = RequestMethod.POST)
    public Map submitHeader(@RequestParam(value = "customerId", required = false, defaultValue = "") Long customerId, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = tcService.submitHeader(customerId, SessionUtil.getLdapUserInfo(request).getUserId());
        if (0 != Long.parseLong(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 审批
     * @param customerId
     * @param code APPROVED同意，REJECTED拒绝
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "auditHeader", method = RequestMethod.POST)
    public Map auditHeader(@RequestParam(value = "customerId", required = false, defaultValue = "") Long customerId,
                           @RequestParam(value = "code", required = false, defaultValue = "") String code,
                           HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = tcService.auditHeader(customerId, SessionUtil.getLdapUserInfo(request).getUserId(), code);
        if (0 != Long.parseLong(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 保存客户
     * @param customerDTO
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveTc", method = RequestMethod.POST)
    public Map saveTc(@RequestBody CustomerDTO customerDTO, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = tcService.addHeader(customerDTO, SessionUtil.getLdapUserInfo(request).getUserId());
        if (0 != Long.parseLong(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 列表
     * @param customerId
     * @param partyName
     * @param status
     * @param name
     * @param source
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "tcList", method = RequestMethod.GET)
    public DataTablesDTO queryTcList(@RequestParam(value = "customerId", required = false, defaultValue = "") Long customerId,
                                     @RequestParam(value = "partyName", required = false, defaultValue = "") String partyName,
                                     @RequestParam(value = "status", required = false, defaultValue = "") String status,
                                     @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                     @RequestParam(value = "source", required = false, defaultValue = "") String source,
                                     HttpServletRequest request,
                                     @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                     @RequestParam(value = "type", required = false, defaultValue = "0") long type) {
        return tcService.getCustomerHeaderList(customerId, partyName, status, 0 == type ? SessionUtil.getLdapUserInfo(request).getUserId() : null, name, source, "", pageNo, pageSize);
    }

    /**
     * 查询ebs列表
     *
     * @param partyName
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "tcEbsList", method = RequestMethod.GET)
    public DataTablesDTO queryTcEbsList(@RequestParam(value = "partyName", required = false, defaultValue = "") String partyName,
                                        HttpServletRequest request,
                                        @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize) {
        return tcService.getEbsHeaderList(partyName, pageNo, pageSize);
    }

    /**
     * 联系人
     *
     * @param customerId
     * @return
     */
    @RequestMapping(value = "contact", method = RequestMethod.GET)
    public List<ContactDTO> querySupplierContact(@RequestParam(value = "customerId", required = false, defaultValue = "") Long customerId) {
        return tcService.getContact(customerId);
    }

    /**
     * 附件
     *
     * @param customerId
     * @return
     */
    @RequestMapping(value = "attachmentContact", method = RequestMethod.GET)
    public List<AttachmentDTO> queryAttachmentContact(@RequestParam(value = "customerId", required = false, defaultValue = "") Long customerId,
                                                      @RequestParam(value = "ebsId", required = false, defaultValue = "") String ebsId) {
        if (StringUtils.isNotEmpty(ebsId)) {
            DataTablesDTO dt = tcService.getCustomerHeaderList(null, "", "", null, "", "", ebsId, 1, 1);
            if (0 < dt.getDataSource().size()) {
                CustomerDTO s = (CustomerDTO) dt.getDataSource().get(0);
                customerId = s.getCustomerId();
            }
        }
        return tcService.getAttachmentContact(customerId);
    }

    /**
     * 删除联系人
     *
     * @param customerId
     * @param contactId
     * @return
     */
    @RequestMapping(value = "delContact", method = RequestMethod.POST)
    public Map delContact(@RequestParam(value = "customerId", required = false, defaultValue = "") Long customerId,
                          @RequestParam(value = "contactId", required = false, defaultValue = "") Long contactId) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = tcService.delContact(customerId, contactId);
        if (0 != Long.parseLong(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 删除附件
     * @param customerId
     * @param fileId
     * @return
     */
    @RequestMapping(value = "delAttachment", method = RequestMethod.POST)
    public Map delAttachment(@RequestParam(value = "customerId", required = false, defaultValue = "") Long customerId,
                             @RequestParam(value = "fileId", required = false, defaultValue = "") Long fileId) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = tcService.delAttachment(customerId, fileId);
        if (0 != Long.parseLong(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    @RequestMapping(value = "queryCustomerSource", method = RequestMethod.GET)
    public List<SourceDTO> queryCustomerSource() {
        return tcService.getCustomerSource();
    }

    @RequestMapping(value = "queryCustomerLevel", method = RequestMethod.GET)
    public List<SourceDTO> queryCustomerLevel() {
        return tcService.getCustomerLevel();
    }

    @RequestMapping(value = "queryCustomerIndustry", method = RequestMethod.GET)
    public List<SourceDTO> queryCustomerIndustry() {
        return tcService.getCustomerIndustry();
    }

    @RequestMapping(value = "queryUserOrg", method = RequestMethod.GET)
    public UserOrgDTO queryUserOrg(HttpServletRequest request) {
        return tcService.getUserOrg(SessionUtil.getLdapUserInfo(request).getUserId());
    }

    /**
     * 文件上传
     * @param files 文件数组
     * @param request
     * @return
     */
    @RequestMapping(value = "uploadZzFile", method = RequestMethod.POST)
    public List<AttachmentDTO> uploadZzFile(@RequestParam(value = "zzFile", required = false) MultipartFile[] files,
                                            @RequestParam(value = "customerId", required = false, defaultValue = "") Long customerId,
                                            HttpServletRequest request) throws UnsupportedEncodingException {
        String dir = nerinProperties.getNbcc().getTcFileUrl() + customerId + "/";
        File dirPath = new File(dir);
        if(!dirPath.exists())
            dirPath.mkdirs();
        File filePath = null;
        List<AttachmentDTO> list = new ArrayList<AttachmentDTO>();
        AttachmentDTO vo = null;
        String fileName = "";
        for (MultipartFile m : files) {
            vo = new AttachmentDTO();
            fileName = m.getOriginalFilename();
            if (0 <= fileName.lastIndexOf("\\")) {
                fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
            }
            filePath = new File(new String((dir + fileName).getBytes("UTF-8")));
            if (filePath.exists())
                filePath.delete();
            try {
                m.transferTo(new File(dir, new String(fileName.getBytes("UTF-8"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            vo.setFileName(fileName);
            vo.setFileUrl(fileName);
            vo.setCreationDate(new Date());
            list.add(vo);
        }
        return list;
    }

    /**
     * 附件下载
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "downloadZzFile", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadWordFile(@RequestParam(value = "customerId", required = false, defaultValue = "") Long customerId,
                                                   @RequestParam(value = "fileName", required = false, defaultValue = "0") String fileName) throws IOException {
        String path = nerinProperties.getNbcc().getTcFileUrl() + customerId + "/";
        String filePath = new String((path + fileName).getBytes("UTF-8"));
        File file=new File(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment",  java.net.URLEncoder.encode(fileName, "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    /**
     * OA流程启动回调（废除）
     * @param customerId
     * @param result
     * @param url
     * @return
     */
//    @RequestMapping(value = "requestForOA", method = RequestMethod.GET)
    public Map querySupplierContact(@RequestParam(value = "customerId", required = false, defaultValue = "0") Long customerId,
                                       @RequestParam(value = "result", required = false, defaultValue = "0") Long result,
                                       @RequestParam(value = "url", required = false, defaultValue = "") String url) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        DataTablesDTO dt = tcService.getCustomerHeaderList(customerId, "", "", null, "", "", "", 1, 1);
        List<CustomerDTO> resultList = dt.getDataSource();
        if (null != resultList && 1 == resultList.size()) {
            CustomerDTO tmp = resultList.get(0);
            if (1 == result) {
                tmp.setAttribute3(url);
            } else {
                tmp.setStatus("NEW");
            }
            reMap = tcService.addHeader(tmp, tmp.getRequestUserId());
            if (0 != Long.parseLong(reMap.get(NbccParm.DB_STATE) + "")) {
                restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
                restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
            } else {
                restFulData.putAll(reMap);
            }
        }
        return restFulData;
    }
    /**
     * 查询联系人列表
     *

     * @return
     */
    @RequestMapping(value = "tcLxr", method = RequestMethod.GET)
    public DataTablesDTO queryTcLxr(@RequestParam(value = "pXing", required = false, defaultValue = "") String pXing,
                                    @RequestParam(value = "pMing", required = false, defaultValue = "") String pMing,
                                    @RequestParam(value = "pAddress", required = false, defaultValue = "") String pAddress,
                                    @RequestParam(value = "pCustomerName", required = false, defaultValue = "") String pCustomerName,
                                    @RequestParam(value = "pCustomerHy", required = false, defaultValue = "") String pCustomerHy,
                                    @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize) {
        return tcService.getCustomerLxr(pXing, pMing,pAddress,pCustomerName,pCustomerHy,pageNo, pageSize);
    }
}
