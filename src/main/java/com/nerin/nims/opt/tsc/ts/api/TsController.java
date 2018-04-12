package com.nerin.nims.opt.tsc.ts.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.BusinessException;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import com.nerin.nims.opt.tsc.ts.dto.AttachmentDTO;
import com.nerin.nims.opt.tsc.ts.dto.ContactDTO;
import com.nerin.nims.opt.tsc.ts.dto.SupplierDTO;
import com.nerin.nims.opt.tsc.ts.service.TsService;
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
 * Created by yinglgu on 11/9/2016.
 */
@RestController
@RequestMapping("/api/tsc/ts")
public class TsController {
    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    private TsService tsService;

    /**
     * 提交
     * @param vendorId
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "submitHeader", method = RequestMethod.POST)
    public Map submitHeader(@RequestParam(value = "vendorId", required = false, defaultValue = "") Long vendorId, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = tsService.submitHeader(vendorId, SessionUtil.getLdapUserInfo(request).getUserId());
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
     * @param vendorId
     * @param code APPROVED同意，REJECTED拒绝
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "auditHeader", method = RequestMethod.POST)
    public Map auditHeader(@RequestParam(value = "vendorId", required = false, defaultValue = "") Long vendorId,
                           @RequestParam(value = "code", required = false, defaultValue = "") String code,
                           HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = tsService.auditHeader(vendorId, SessionUtil.getLdapUserInfo(request).getUserId(), code);
        if (0 != Long.parseLong(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 保存文本任务头信息、另外复制生成文本章节信息
     *
     * @param supplierDTO
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveTs", method = RequestMethod.POST)
    public Map saveTs(@RequestBody SupplierDTO supplierDTO, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        reMap = tsService.addHeader(supplierDTO, SessionUtil.getLdapUserInfo(request).getUserId());
        if (0 != Long.parseLong(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 查询列表
     *
     * @param vendorId
     * @param vendorName
     * @param status
     * @param name
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "tsList", method = RequestMethod.GET)
    public DataTablesDTO queryTsList(@RequestParam(value = "vendorId", required = false, defaultValue = "") Long vendorId,
                                     @RequestParam(value = "vendorName", required = false, defaultValue = "") String vendorName,
                                     @RequestParam(value = "status", required = false, defaultValue = "") String status,
                                     @RequestParam(value = "name", required = false, defaultValue = "") String name,
                                     HttpServletRequest request,
                                     @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "1000") long pageSize,
                                     @RequestParam(value = "type", required = false, defaultValue = "0") long type) {
        return tsService.getSupplierHeaderList(vendorId, vendorName, status, 0 == type ? SessionUtil.getLdapUserInfo(request).getUserId() : null, name, "", pageNo, pageSize);
    }

    /**
     * 查询ebs列表
     *
     * @param vendorName
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "tsEbsList", method = RequestMethod.GET)
    public DataTablesDTO queryTsEbsList(@RequestParam(value = "vendorName", required = false, defaultValue = "") String vendorName,
                                     @RequestParam(value = "vendorType", required = false, defaultValue = "") String vendorType,
                                     @RequestParam(value = "nature", required = false, defaultValue = "") String nature,
                                     HttpServletRequest request,
                                     @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize) {
        return tsService.getEbsSupplierHeaderList(vendorName, vendorType, nature, pageNo, pageSize);
    }

    /**
     * 联系人
     *
     * @param vendorId
     * @return
     */
    @RequestMapping(value = "supplierContact", method = RequestMethod.GET)
    public List<ContactDTO> querySupplierContact(@RequestParam(value = "vendorId", required = false, defaultValue = "") Long vendorId) {
        return tsService.getSupplierContact(vendorId);
    }

    /**
     * 附件
     *
     * @param vendorId
     * @return
     */
    @RequestMapping(value = "attachmentContact", method = RequestMethod.GET)
    public List<AttachmentDTO> queryAttachmentContact(@RequestParam(value = "vendorId", required = false, defaultValue = "") Long vendorId,
                                                      @RequestParam(value = "ebsId", required = false, defaultValue = "") String ebsId) {
        if (StringUtils.isNotEmpty(ebsId)) {
            DataTablesDTO dt = tsService.getSupplierHeaderList(null, "", "", null, "", ebsId, 1, 1);
            if (0 < dt.getDataSource().size()) {
                SupplierDTO s = (SupplierDTO) dt.getDataSource().get(0);
                vendorId = s.getVendorId();
            }
        }
        return tsService.getAttachmentContact(vendorId);
    }

    /**
     * 删除联系人
     *
     * @param vendorId
     * @param contactId
     * @return
     */
    @RequestMapping(value = "delContact", method = RequestMethod.POST)
    public Map delContact(@RequestParam(value = "vendorId", required = false, defaultValue = "") Long vendorId,
                          @RequestParam(value = "contactId", required = false, defaultValue = "") Long contactId) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = tsService.delContact(vendorId, contactId);
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
     * @param vendorId
     * @param fileId
     * @return
     */
    @RequestMapping(value = "delAttachment", method = RequestMethod.POST)
    public Map delAttachment(@RequestParam(value = "vendorId", required = false, defaultValue = "") Long vendorId,
                          @RequestParam(value = "fileId", required = false, defaultValue = "") Long fileId) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = tsService.delAttachment(vendorId, fileId);
        if (0 != Long.parseLong(reMap.get(NbccParm.DB_STATE) + "")) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    @RequestMapping(value = "querySupplierNature", method = RequestMethod.GET)
    public List<String> querySupplierNature() {
        return tsService.getSupplierNature();
    }

    @RequestMapping(value = "getVendorLevel", method = RequestMethod.GET)
    public List<String> getVendorLevel() {
        return tsService.getVendorLevel();
    }

    /**
     * 文件上传
     * @param files 文件数组
     * @param request
     * @return
     */
    @RequestMapping(value = "uploadZzFile", method = RequestMethod.POST)
    public List<AttachmentDTO> uploadZzFile(@RequestParam(value = "zzFile", required = false) MultipartFile[] files,
                                        @RequestParam(value = "vendorId", required = false, defaultValue = "") Long vendorId,
                                        HttpServletRequest request) throws UnsupportedEncodingException {
        String dir = nerinProperties.getNbcc().getTsFileUrl() + vendorId + "/";
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
    public ResponseEntity<byte[]> downloadWordFile(@RequestParam(value = "vendorId", required = false, defaultValue = "") Long vendorId,
                                                   @RequestParam(value = "fileName", required = false, defaultValue = "0") String fileName) throws IOException {
        String path = nerinProperties.getNbcc().getTsFileUrl() + vendorId + "/";
        String filePath = new String((path + fileName).getBytes("UTF-8"));
        File file=new File(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment",  java.net.URLEncoder.encode(fileName, "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

}

