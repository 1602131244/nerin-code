package com.nerin.nims.opt.nbcc.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.util.*;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.nbcc.dto.TemplateChaptersDTO;
import com.nerin.nims.opt.nbcc.dto.TemplateHeaderDTO;
import com.nerin.nims.opt.nbcc.service.TemplateService;
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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 模板restful
 * Created by yinglgu on 6/17/2016.
 */
@RestController
@RequestMapping("/api/templateRest")
public class TemplateRestController {
    @Autowired
    private NerinProperties nerinProperties;

    @Autowired
    private TemplateService templateService;

    /**
     * 保存模板头
     * @param headerDTO TemplateHeaderDTO-模板头
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveTemplateHeader", method = RequestMethod.POST)
    public Map saveTemplateHeader(@RequestBody TemplateHeaderDTO headerDTO, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;
        if (null != headerDTO.getFromTemplateId()) {
            reMap = templateService.saveAsTemplate(headerDTO, SessionUtil.getLdapUserInfo(request).getUserId());
        } else {
            reMap = templateService.addOrUpTemplateHeader(headerDTO, SessionUtil.getLdapUserInfo(request).getUserId());
        }
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 保存章节
     * @param chaptersDTOs
     * @return
     */
    @RequestMapping(value = "saveTemplateChapter", method = RequestMethod.POST)
    public Map saveTemplateChapter(@RequestBody List<TemplateChaptersDTO> chaptersDTOs, HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        int i = 0;
        // 如果有删除id，先进行删除
        if (StringUtils.isNotEmpty(chaptersDTOs.get(0).getDelTemplateChapterIds())) {
            Map dMap = templateService.delTemplateChapters(chaptersDTOs.get(0).getDelTemplateChapterIds());
            if (1 != (Long) dMap.get(NbccParm.DB_STATE)) {
                restFulData.put(RestFulData.RETURN_CODE, dMap.get(NbccParm.DB_STATE) + "");
                restFulData.put(RestFulData.RETURN_MSG, dMap.get(NbccParm.DB_MSG) + "");
                i = 1;
            }
        }
        if (0 == i) {
            Map reMap = templateService.addOrUpTemplateChapters(chaptersDTOs, SessionUtil.getLdapUserInfo(request).getUserId());
            if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
                restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
                restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
            } else {
                restFulData.putAll(reMap);
            }
        }
        return restFulData;
    }

    /**
     * 查询模板列表
     * @param taskTypes 文本类型
     * @param templateStatus 模板状态奇
     * @param templateNameOrDesc 描述
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "templateHeaderList", method = RequestMethod.POST)
    public DataTablesDTO queryTemplateHeaderList(@RequestParam(value = "taskTypes", required = false, defaultValue = "") String taskTypes,
                                                 @RequestParam(value = "templateStatus", required = false, defaultValue = "") String templateStatus,
                                                 @RequestParam(value = "templateNameOrDesc", required = false, defaultValue = "") String templateNameOrDesc,
                                                 @RequestParam(value = "templateHeaderId", required = false, defaultValue = "") Long templateHeaderId,
                                                 @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize) {
        return templateService.getTemplateHeaderList(taskTypes, templateStatus, templateNameOrDesc,templateHeaderId, pageNo, pageSize);
    }

    /**
     * 查询状态列表
     * @param templateStatus
     * @return
     */
    @RequestMapping(value = "templateStatusList", method = RequestMethod.GET)
    public DataTablesDTO queryTemplateStatusList(@RequestParam(value = "templateStatus", required = false, defaultValue = "") String templateStatus) {
        return templateService.getTemplateStatusList(templateStatus);
    }

    /**
     * 文本类型列表
     * @param taskType
     * @return
     */
    @RequestMapping(value = "taskTypeList", method = RequestMethod.GET)
    public DataTablesDTO queryTaskTypeList(@RequestParam(value = "taskType", required = false, defaultValue = "") String taskType,
                                           @RequestParam(value = "projectPhase", required = false, defaultValue = "") String projectPhase
    ) {
        return templateService.getTaskTypeList(taskType,projectPhase);
    }

    /**
     * 项目角色列表
     * @param pRoleId
     * @return
     */
    @RequestMapping(value = "projectRoleList", method = RequestMethod.GET)
    public DataTablesDTO queryProjectRoleList(@RequestParam(value = "pRoleId", required = false, defaultValue = "") String pRoleId
                                              ,@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId
    ) {
        return templateService.getProjectRoleList(StringUtils.isEmpty(pRoleId) ? null : Long.parseLong(pRoleId),projectId);
    }

    /**
     * 专业列表
     * @param specialty
     * @return
     */
    @RequestMapping(value = "specialtyList", method = RequestMethod.GET)
    public DataTablesDTO querySpecialtyList(@RequestParam(value = "specialty", required = false, defaultValue = "") String specialty,
                                            @RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId
                                            ) {
        return templateService.getSpecialtyList(specialty,projectId);
    }

    /**
     * 系统链接列表
     * @param systemUiCode
     * @return
     */
    @RequestMapping(value = "systemUiList", method = RequestMethod.GET)
    public DataTablesDTO querySystemUiList(@RequestParam(value = "systemUiCode", required = false, defaultValue = "") String systemUiCode) {
        return templateService.getSystemUiList(systemUiCode);
    }

    /**
     * xml模板列表
     * @param xdoTemplateId
     * @return
     */
    @RequestMapping(value = "xmlTemplateList", method = RequestMethod.GET)
    public DataTablesDTO queryXmlTemplateList(@RequestParam(value = "xdoTemplateId", required = false, defaultValue = "") String xdoTemplateId) {
        return templateService.getXmlTemplateList(xdoTemplateId);
    }

    /**
     * 模板名唯一检查
     * @param templateName
     * @return
     */
    @RequestMapping(value = "valTemplateName", method = RequestMethod.GET)
    public String valTemplateName(@RequestParam(value = "templateName", required = false, defaultValue = "") String templateName,
                                  @RequestParam(value = "templateId", required = false, defaultValue = "") String templateId) {
        int v = templateService.checkTemplateName(templateName, StringUtils.isEmpty(templateId) ? null : Long.parseLong(templateId));
        if (1 == v)
            return "true";
        else
            return "false";
    }

    /**
     * 更新模板状态
     * @param templateId
     * @param templateStatus
     * @return
     */
    @RequestMapping(value = "upTemplateStatus", method = RequestMethod.POST)
    public Map updateTemplateStatus(@RequestParam(value = "templateId", required = false, defaultValue = "-1") long templateId,
                                    @RequestParam(value = "templateStatus", required = false, defaultValue = "") String templateStatus) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = templateService.updateTemplateStatus(templateId, templateStatus);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 删除模板
     * @param templateIds 支持多选，逗号隔开
     * @retur
     */
    @RequestMapping(value = "delTemplate", method = RequestMethod.POST)
    public Map delTemplate(@RequestParam(value = "templateIds", required = false, defaultValue = "-1") String templateIds) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = templateService.delTemplate(templateIds);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     *
     * @param templateId
     * @param type
     * @param lineType   行类型：COVER   封面、CHAPTER  章节
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "templateChapters", method = RequestMethod.GET)
    public List<TemplateChaptersDTO> queryTemplateChaptersList(@RequestParam(value = "templateId", required = false, defaultValue = "1") long templateId,
                                                               @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                                               @RequestParam(value = "lineType", required = false, defaultValue = "CHAPTER") String lineType,
                                                               @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                               @RequestParam(value = "pageSize", required = false, defaultValue = "1000") long pageSize) {
        return templateService.getTemplateChaptersList(templateId, lineType, pageNo, pageSize, type);
    }

    /**
     * 下载模板章节及封面信息word文件，以zip包返回
     * @param templateId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "downloadChapterWordFile", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadChapterWordFile(@RequestParam(value = "templateId", required = false, defaultValue = "0") Long templateId) throws IOException {
        DataTablesDTO headerDT = templateService.getTemplateHeaderList("", "", "",templateId, 1, 1000);
        TemplateHeaderDTO header = (TemplateHeaderDTO) headerDT.getDataSource().get(0);
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(header.getTemplateName());

        String path = nerinProperties.getNbcc().getTemplateWordFileUrl() + templateId + "/";
        String zipPath = nerinProperties.getNbcc().getTemplateWordFileUrl() + templateId + "/zip/";
        List<TemplateChaptersDTO> chapters = templateService.getTemplateChaptersList(templateId, "CHAPTER", 1, 1000, 1);
        List<TemplateChaptersDTO> cover = templateService.getTemplateChaptersList(templateId, "COVER", 1, 1000, 1);
        if (null != chapters && 0 < chapters.size())
            chapters.remove(0);
        File zipFolder = new File(zipPath);
        if(!zipFolder.exists()) {
            zipFolder.mkdirs();
        } else {
            FileUtil.deleteDirectory(zipPath);
            zipFolder.mkdirs();
        }

        //目录下
        File fileFolder = new File(path);
        // 复制文件到zip目录
        File chapterFile = null;
        String newFileName = "";
        if (null != cover && 1 == cover.size()) {
            TemplateChaptersDTO tmp = cover.get(0);
            // 检查是否来自xml模板，2017-01-07
            String suffix = ".docx";
            if (1 == templateService.isWithXml(tmp, 0))
                suffix = ".rtf";
            templateService.autoCreateWordWithXml(tmp, 0, 0, 0, path);
            chapterFile = new File(path + tmp.getChapterId() + suffix);
            newFileName = new String((zipPath + tmp.getAttribute1() + "_" + tmp.getChapterId() + "_" + tmp.getChapterNo() + "_" + p.matcher(tmp.getChapterName()).replaceAll("").trim() + suffix).getBytes("UTF-8"));
            if (!chapterFile.exists())
                new File(newFileName).createNewFile();
            else
                FileUtil.copyFile(path + tmp.getChapterId() + suffix, newFileName);
        }
        for (TemplateChaptersDTO t : chapters) {
            // 检查是否来自xml模板，2017-01-07
            String suffix = ".docx";
            if (1 == templateService.isWithXml(t, 0))
                suffix = ".rtf";
            templateService.autoCreateWordWithXml(t, 0, 0, 0, path);
            chapterFile = new File(path + t.getChapterId() +suffix);
            newFileName = new String((zipPath + t.getAttribute7() + "_" + t.getChapterId() + "_" + t.getChapterNo() + "_" + p.matcher(t.getChapterName()).replaceAll("").trim() + suffix).getBytes("UTF-8"));
            if (!chapterFile.exists())
                new File(newFileName).createNewFile();
            else
                FileUtil.copyFile(path + t.getChapterId() + suffix, newFileName);
        }
        String zipName = "template_" + templateId + "_" + m.replaceAll("").trim() + ".zip";
        File zipFile = new File(new String((path + zipName).getBytes("UTF-8")));
        if (zipFile.exists())
            zipFile.delete();
        ZipCompress.writeByApacheZipOutputStream(zipPath, new String((path + zipName).getBytes("UTF-8")), "模板压缩包");
        HttpHeaders headers = new HttpHeaders();
        String fileName= zipName;//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", java.net.URLEncoder.encode(fileName, "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(zipFile), headers, HttpStatus.OK);
    }

    /**
     * word文件上传
     * @param files 文件数组
     * @param templateId 任务头id
     * @param request
     * @return
     */
    @RequestMapping(value = "uploadChapterWordFile", method = RequestMethod.POST)
    public String uploadChapterWordFile(@RequestParam(value = "wordFile", required = false) MultipartFile[] files,
                                 @RequestParam(value = "templateId", required = false, defaultValue = "0") Long templateId,
                                 HttpServletRequest request) {
        String dir = nerinProperties.getNbcc().getTemplateWordFileUrl() + templateId + "/";
        File dirPath = new File(dir);
        if(!dirPath.exists())
            dirPath.mkdirs();
        File filePath = null;
        for (MultipartFile m : files) {
            filePath = new File(dir + m.getOriginalFilename());
            if (filePath.exists())
                filePath.delete();
            try {
                m.transferTo(new File(dir, m.getOriginalFilename()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "true";
    }

    /**
     * 上传模板附件
     * @param file
     * @param templateHeaderId
     * @param request
     * @return
     */
    @RequestMapping(value = "uploadTemplateFile", method = RequestMethod.POST)
    public String uploadWordFile(@RequestParam(value = "templateFile", required = false) MultipartFile file,
                                 @RequestParam(value = "templateHeaderId", required = false, defaultValue = "0") Long templateHeaderId,
                                 HttpServletRequest request) throws IOException {
        String dir = nerinProperties.getNbcc().getTemplateFileUrl();
        File dirPath = new File(dir);
        if(!dirPath.exists())
            dirPath.mkdirs();
        File filePath = null;
        String fileName = new String((templateHeaderId + "_" + file.getOriginalFilename()).getBytes("UTF-8"));
        filePath = new File(dir + fileName);
        if (filePath.exists())
            filePath.delete();
        try {
            file.transferTo(new File(dir, fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataTablesDTO dt = templateService.getTemplateHeaderList("", "", "",templateHeaderId, 1, 1);
        if (null != dt.getDataSource() && 0 < dt.getDataSource().size()) {
            List<TemplateHeaderDTO> l = (List<TemplateHeaderDTO>)dt.getDataSource();
            TemplateHeaderDTO t = l.get(0);
            t.setAttribute5(file.getOriginalFilename());
            templateService.addOrUpTemplateHeader(t, SessionUtil.getLdapUserInfo(request).getUserId());
        }
        return "true";
    }

    /**
     * 模板附件下载
     * @param templateHeaderId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "downloadTemplateFile", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadWordFile(@RequestParam(value = "templateHeaderId", required = false, defaultValue = "0") Long templateHeaderId) throws IOException {
        DataTablesDTO dt = templateService.getTemplateHeaderList("", "", "",templateHeaderId, 1, 1);
        List<TemplateHeaderDTO> l = (List<TemplateHeaderDTO>)dt.getDataSource();
        TemplateHeaderDTO t = l.get(0);
        String fileName = new String((templateHeaderId + "_" + t.getAttribute5()).getBytes("UTF-8"));
        String path = nerinProperties.getNbcc().getTemplateFileUrl() + fileName;
        File file=new File(path);
        HttpHeaders headers = new HttpHeaders();
        String downFileName= t.getAttribute5();
        headers.setContentDispositionFormData("attachment", java.net.URLEncoder.encode(downFileName, "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    /**
     * 检测文件是否存在
     * @param fileName
     * @return
     */
    @RequestMapping(value = "checkFile", method = RequestMethod.GET)
    public Long checkFile(@RequestParam(value = "fileName", required = false, defaultValue = "") String fileName
                            ) {
        return templateService.checkFile(fileName);
    }
}
