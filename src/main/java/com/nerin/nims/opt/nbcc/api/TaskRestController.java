package com.nerin.nims.opt.nbcc.api;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.rest.RestFulData;
import com.nerin.nims.opt.base.util.BusinessException;
import com.nerin.nims.opt.base.util.FileUtil;
import com.nerin.nims.opt.base.util.SessionUtil;
import com.nerin.nims.opt.base.util.ZipCompress;
import com.nerin.nims.opt.nbcc.common.NbccParm;
import com.nerin.nims.opt.nbcc.dto.*;

import com.nerin.nims.opt.nbcc.service.TaskService;
import com.nerin.nims.opt.nbcc.service.TaskWordAsyncTask;
import com.nerin.nims.opt.nbcc.service.TemplateService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
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
 * Created by Administrator on 2016/7/1.
 */
@RestController
@RequestMapping("/api/taskRest")
public class TaskRestController {

    @Autowired
    private NerinProperties nerinProperties;
    @Autowired
    public TaskService taskService;
    @Autowired
    public TemplateService templateService;
    @Autowired
    public TaskWordAsyncTask taskWordAsyncTask;

    /**
     * 获取用户ID
     * @param workCode
     * @return
     */
    @RequestMapping(value = "getUserId", method = RequestMethod.GET)
    public Long getUserId(@RequestParam(value = "workCode", required = false, defaultValue = "") String workCode
                             ) {
        return taskService.getUserId(workCode);
    }

    /**
     * 查询文本任务列表
     * @param taskTypes  任务类型，可以多选，用，隔开
     * @param designPhase  阶段，可以多选，用，隔开
     * @param taskName      任务名称
     * @param projManager   项目经理
     * @param creater       创建者
     * @param sign          是否完成
     * @param pageNo        查询页码
     * @param pageSize      查询行
     * @return
     */
    /**
     *
     * @param taskTypes
     * @param designPhase
     * @param taskName
     * @param projSearch
     * @param projManager
     * @param creater
     * @param sign
     * @param taskHeaderId
     * @param request
     * @param respKey
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "taskHeaderList", method = RequestMethod.POST)
    public DataTablesDTO queryTaskHeaderList(@RequestParam(value = "taskTypes", required = false, defaultValue = "") String taskTypes,
                                                 @RequestParam(value = "designPhase", required = false, defaultValue = "") String designPhase,
                                                 @RequestParam(value = "taskName", required = false, defaultValue = "") String taskName,
                                                 @RequestParam(value = "projSearch", required = false, defaultValue = "") String projSearch,
                                                 @RequestParam(value = "projManager", required = false, defaultValue = "") String projManager,
                                                 @RequestParam(value = "creater", required = false, defaultValue = "") String creater,
                                                 @RequestParam(value = "sign", required = false, defaultValue = "0") Long sign,
                                                 @RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                                 @RequestParam(value = "isoa", required = false, defaultValue = "0") Long isoa,
                                                 HttpServletRequest request,
                                                 @RequestParam(value = "respKey", required = false, defaultValue = "") String respKey,
                                                 @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                 @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize
                                                ) {
        return taskService.getTaskHeaderList(taskTypes, designPhase, taskName, projSearch, projManager, creater, sign, taskHeaderId, isoa, SessionUtil.getLdapUserInfo(request).getUserId(),respKey, pageNo, pageSize);
    }

    /**
     * 查询文本历史列表
     * @param taskSearch
     * @param taskHeaderId
     * @param taskType
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "taskHeaderHistoryList", method = RequestMethod.GET)
    public DataTablesDTO queryTaskHeaderHistoryList(@RequestParam(value = "taskSearch", required = false, defaultValue = "") String taskSearch,
                                                     @RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                                     @RequestParam(value = "taskType", required = false, defaultValue = "") String taskType,
                                                     HttpServletRequest request,
                                                     @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                     @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize) {
        return taskService.getTaskHeaderHistoryList(taskSearch, taskHeaderId,taskType, SessionUtil.getLdapUserInfo(request).getUserId(), pageNo, pageSize);
    }

    /**
     * 获取文本任务名称序号
     * @param taskType
     * @param projectId
     * @return
     */
    @RequestMapping(value = "getTaskchapterNameNo", method = RequestMethod.POST)
    public String getTaskchapterNameNo(@RequestParam(value = "taskType", required = false, defaultValue = "") String taskType,
                                       @RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId
                                       ) {
        String i = taskService.getTaskchapterNameNo(taskType,projectId);
        return i;
    }

    /**
     * 删除文本任务（头表、行表）
     * @param taskHeaderIds
     * @return
     */
    @RequestMapping(value = "delTask", method = RequestMethod.POST)
    public Map delTemplate(@RequestParam(value = "taskHeaderIds", required = false, defaultValue = "-1") String taskHeaderIds) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.delTask(taskHeaderIds);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 更新文本任务是否共享
     * @param taskHeaderId
     * @param taskShared
     * @return
     */
    @RequestMapping(value = "upTaskSharde", method = RequestMethod.POST)
    public Map upTaskSharde(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "-1") long taskHeaderId,
                             @RequestParam(value = "taskShared", required = false, defaultValue = "0") long taskShared) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.updateTaskShared(taskHeaderId, taskShared);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 检查文本名称唯一性
     * @param taskName
     * @param taskHeaderId
     * @return
     */
    @RequestMapping(value = "valTaskName", method = RequestMethod.GET)
    public String valTaskName(@RequestParam(value = "taskName", required = false, defaultValue = "") String taskName,
                               @RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId) {
        int v = taskService.checkTaskName(taskName, taskHeaderId);
        if (1 == v)
            return "true";
        else
            return "false";
    }

    /**
     * 检查用户是否有创建权限
     * @param projectId
     * @param taskType
     * @return
     */
    @RequestMapping(value = "checkTaskCreate", method = RequestMethod.GET)
    public String checkTaskCreate(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                   @RequestParam(value = "taskType", required = false, defaultValue = "") String taskType,
                                  HttpServletRequest request) {
        int v = taskService.checkTaskPermissions("CREATE", projectId, taskType, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 == v)
            return "true";
        else
            return "false";
    }

    /**
     * 检查用户是否有提交检审的权限
     * @param projectId
     * @param taskType
     * @return
     */
    @RequestMapping(value = "checkTaskSubmit", method = RequestMethod.GET)
    public String checkTaskSubmit(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                  @RequestParam(value = "taskType", required = false, defaultValue = "") String taskType,
                                  HttpServletRequest request) {
        int v = taskService.checkTaskPermissions("SUBMIT", projectId, taskType, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 == v)
            return "true";
        else
            return "false";
    }

    /**
     * 保存文本任务头信息、另外复制生成文本章节信息
     * @param taskHeaderDTO
     * @param request
     * @return
     * @throws BusinessException
     */
    @RequestMapping(value = "saveTaskHeader", method = RequestMethod.POST)
    public Map saveTaskHeader(@RequestBody TaskHeaderDto taskHeaderDTO, HttpServletRequest request) throws BusinessException {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = null;

        reMap = taskService.addTaskHeader(taskHeaderDTO, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
            AsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
            taskWordAsyncTask.setTaskId(Long.parseLong(reMap.get(NbccParm.DB_SID) + ""));
            taskWordAsyncTask.setUserId(SessionUtil.getLdapUserInfo(request).getUserId());
            taskWordAsyncTask.setPersonId(SessionUtil.getLdapUserInfo(request).getPersonId());
            executor.execute(taskWordAsyncTask, 50000L);
        }
        return restFulData;
    }

    /**
     * 更新文本任务章节的节点负责人、工作包、设计人员、校核人员、检审人员、审定人员等信息
     * @param taskHeaderId
     * @param tasChaperId
     * @return
     */
    @RequestMapping(value = "updateTaskElement", method = RequestMethod.POST)
    public Map updateTaskElement(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                  @RequestParam(value = "tasChaperId", required = false, defaultValue = "") Long tasChaperId) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.updateTaskElement(taskHeaderId, tasChaperId);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 获取用户可选的项目列表
     * @param projectSearch
     * @param request
     * @return
     */
    @RequestMapping(value = "projectList", method = RequestMethod.GET)
    public DataTablesDTO queryProjectList(@RequestParam(value = "projectSearch", required = false, defaultValue = "") String projectSearch,
                                          HttpServletRequest request) {
        return taskService.getProjectList(projectSearch,SessionUtil.getLdapUserInfo(request).getUserId());
    }

    /**
     * 获取文本任务章节列表
     * @param taskHeaderId
     * @param listType
     * @param lineType
     * @param elementId
     * @param userId
     * @param workCode
     * @param isoa
     * @param pageNo
     * @param pageSize
     * @param type   是否转出树结构  0 表示树形结构  1表示列表
     * @param request
     * @return
     */
    @RequestMapping(value = "taskChaptersList", method = RequestMethod.GET)
    public DataTablesDTO queryTaskChaptersList(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                               @RequestParam(value = "listType", required = false, defaultValue = "LIST") String listType,
                                               @RequestParam(value = "lineType", required = false, defaultValue = "CHAPTER") String lineType,
                                               @RequestParam(value = "elementId", required = false, defaultValue = "") Long elementId,
                                               @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                               @RequestParam(value = "workCode", required = false, defaultValue = "") String workCode,
                                               @RequestParam(value = "isoa", required = false, defaultValue = "0") long isoa,
                                               @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                               @RequestParam(value = "pageSize", required = false, defaultValue = "1000") long pageSize,
                                               @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                               HttpServletRequest request) {
        if (0 == userId) {
            //if (null == workCode)
            if (workCode != null && workCode.equals(""))
                userId = SessionUtil.getLdapUserInfo(request).getUserId();
            else
                userId = taskService.getUserId(workCode);
        }

        return taskService.getTaskChaptersList(taskHeaderId, listType, lineType, elementId,userId, isoa, pageNo, pageSize, type);
    }


    /**
     * 获取模板或文本任务章节列表(展示文本内容使用)
     * @param source   来源 TASK 文本任务 TEMPLATE 模板
     * @param headerId  文本ID或者模板ID
     * @param lineType  行类型：COVER   封面、CHAPTER  章节 ALL 所有数据
     * @param userId    用户ID
     * @param workCode   用户的员工工号
     * @param isoa      是否OA
     * @param type     是否转出树结构 默认0  0 表示树形结构  1表示列表
     * @param request
     * @return
     */
    @RequestMapping(value = "chaptersList", method = RequestMethod.GET)
    public DataTablesDTO queryChaptersList(@RequestParam(value = "source", required = false, defaultValue = "TASK") String source,
                                           @RequestParam(value = "headerId", required = false, defaultValue = "") Long headerId,
                                               @RequestParam(value = "lineType", required = false, defaultValue = "ALL") String lineType,
                                               @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                               @RequestParam(value = "workCode", required = false, defaultValue = "") String workCode,
                                               @RequestParam(value = "isoa", required = false, defaultValue = "0") long isoa,
                                               @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                               HttpServletRequest request) {
        if (0 == userId) {
            //if (null == workCode)
            if (workCode != null && workCode.equals(""))
                userId = SessionUtil.getLdapUserInfo(request).getUserId();
            else
                userId = taskService.getUserId(workCode);
        }

        return taskService.getChaptersList(source, headerId, lineType, userId, isoa, type);
    }

    /**
     * word插件，加载章节数据（权限控制）
     * @param taskHeaderId 任务头id
     * @param lineType 行类型：COVER   封面、CHAPTER  章节
     * @param userId 用户id
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "taskChaptersForWord", method = RequestMethod.GET)
    public DataTablesDTO queryTaskChaptersForWord(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                                  @RequestParam(value = "lineType", required = false, defaultValue = "CHAPTER") String lineType,
                                                  @RequestParam(value = "userId", required = false, defaultValue = "") Long userId,
                                               @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                               @RequestParam(value = "pageSize", required = false, defaultValue = "1000") long pageSize,
                                                  @RequestParam(value = "type", required = false, defaultValue = "0") int type) {
        return taskService.getTaskChaptersForWord(taskHeaderId, "REPO", lineType, null, userId, pageNo, pageSize, type);
    }

    /**
     * word文件下载
     * @param taskHeaderId 任务头id
     * @param userId
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "downloadWordFile", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadWordFile(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "0") Long taskHeaderId,
                                                   @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                                   HttpServletRequest request
                                                    ) throws IOException {
        if (0 == userId)
             userId = SessionUtil.getLdapUserInfo(request).getUserId();

        DataTablesDTO headerDT = taskService.getTaskHeaderList("", "", "", "", "", "", 0, taskHeaderId, 0, userId,null, 1, 1000);
        TaskHeaderDto header = (TaskHeaderDto) headerDT.getDataSource().get(0);
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(header.getTaskName());

        String path = nerinProperties.getNbcc().getWordFileUrl() + taskHeaderId + "/";
        String zipPath = nerinProperties.getNbcc().getWordFileUrl() + taskHeaderId + "/zip/";
        DataTablesDTO chapter = taskService.getTaskChaptersList(taskHeaderId, "REPO", "CHAPTER", null, userId ,0, 1, 1000, 1);
        DataTablesDTO t_cover = taskService.getTaskChaptersList(taskHeaderId, "LIST", "COVER", null, userId , 0, 1, 1000, 1);
        List<TaskChaptersDto> chapters = chapter.getDataSource();
        List<TaskChaptersDto> cover = t_cover.getDataSource();
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
            TaskChaptersDto tmp = cover.get(0);
            // 检查是否来自xml模板，2017-01-07
            String suffix = ".docx";
            if (1 == templateService.isWithXml(tmp, 1))
                suffix = ".rtf";
            templateService.autoCreateWordWithXml(tmp, header.getTaskHeaderId(), header.getProjectId(), 1, path);
            chapterFile = new File(path + tmp.getChapterId() + suffix);
            newFileName = new String((zipPath + tmp.getAttribute1() + "_" + tmp.getChapterId() + "_" + tmp.getChapterNo() + "_" + p.matcher(tmp.getChapterName()).replaceAll("").trim() + suffix).getBytes("UTF-8"));
            if (!chapterFile.exists())
                new File(newFileName).createNewFile();
            else
                FileUtil.copyFile(path + tmp.getChapterId() + suffix, newFileName);
        }
        for (TaskChaptersDto t : chapters) {
            // 检查是否来自xml模板，2017-01-07
            String suffix = ".docx";
            if (1 == templateService.isWithXml(t, 1))
                suffix = ".rtf";
            templateService.autoCreateWordWithXml(t, header.getTaskHeaderId(), header.getProjectId(), 1, path);
            chapterFile = new File(path + t.getChapterId() + suffix);
            newFileName = new String((zipPath + t.getAttribute7() + "_" + t.getChapterId() + "_" + t.getChapterNo() + "_" + p.matcher(t.getChapterName()).replaceAll("").trim() + suffix).getBytes("UTF-8"));
            if (!chapterFile.exists())
                new File(newFileName).createNewFile();
            else
                FileUtil.copyFile(path + t.getChapterId() + suffix, newFileName);
        }
        String zipName = "task_" + taskHeaderId + "_" + m.replaceAll("").trim() + ".zip";
        File zipFile = new File(new String((path + zipName).getBytes("UTF-8")));
        if (zipFile.exists())
            zipFile.delete();
        ZipCompress.writeByApacheZipOutputStream(zipPath, new String((path + zipName).getBytes("UTF-8")), "模板压缩包");
        HttpHeaders headers = new HttpHeaders();
        String fileName= zipName;//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", java.net.URLEncoder.encode(fileName, "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(zipFile), headers, HttpStatus.OK);
//
//
//
//        String path = nerinProperties.getNbcc().getWordFileUrl() + taskHeaderId + "/" + chapterId + ".docx";
//        File file=new File(path);
//        HttpHeaders headers = new HttpHeaders();
//        String fileName= new String(("tmp_" + chapterId + ".docx").getBytes("UTF-8"), "iso-8859-1");//为了解决中文名称乱码问题
//        headers.setContentDispositionFormData("attachment", fileName);
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }


    /**
     * 下载单个章节文本
     * @param taskHeaderId
     * @param taskChapterId
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "downloadWordFileC", method = RequestMethod.POST)
    public ResponseEntity<byte[]> downloadWordFileC(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "0") Long taskHeaderId,
                                                    @RequestParam(value = "taskChapterId", required = false, defaultValue = "0") Long taskChapterId,
                                                   //@RequestParam(value = "userId", required = false, defaultValue = "0") Long userId
                                                    HttpServletRequest request
                                                    ) throws IOException {

        Long userId = SessionUtil.getLdapUserInfo(request).getUserId();

        DataTablesDTO headerDT = taskService.getTaskHeaderList("", "", "", "", "", "", 0, taskHeaderId, 0, userId,null, 1, 1000);
        TaskHeaderDto header = (TaskHeaderDto) headerDT.getDataSource().get(0);
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(header.getTaskName());

        String path = nerinProperties.getNbcc().getWordFileUrl() + taskHeaderId + "/";
        String zipPath = nerinProperties.getNbcc().getWordFileUrl() + taskHeaderId + "/zip/";
        DataTablesDTO chapter = taskService.getTaskChaptersList(taskHeaderId, "REPO", "CHAPTER", null, userId , 0, 1, 1000, 1);
        DataTablesDTO t_cover = taskService.getTaskChaptersList(taskHeaderId, "LIST", "COVER", null, userId , 0, 1, 1000, 1);
        List<TaskChaptersDto> chapters = chapter.getDataSource();
        List<TaskChaptersDto> cover = t_cover.getDataSource();

        String retuenFileName = path;
        String fileName = taskChapterId + ".DOCX";

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
            TaskChaptersDto tmp = cover.get(0);
            // 检查是否来自xml模板，2017-01-07
            String suffix = ".docx";
            if (1 == templateService.isWithXml(tmp, 1))
                suffix = ".rtf";
            templateService.autoCreateWordWithXml(tmp, header.getTaskHeaderId(), header.getProjectId(), 1, path);
            chapterFile = new File(path + tmp.getChapterId() + suffix);
            newFileName = new String((zipPath + tmp.getAttribute1() + "_" + tmp.getChapterId() + "_" + tmp.getChapterNo() + "_" + p.matcher(tmp.getChapterName()).replaceAll("").trim() + suffix).getBytes("UTF-8"));
            if (!chapterFile.exists())
                new File(newFileName).createNewFile();
            else
                FileUtil.copyFile(path + tmp.getChapterId() + suffix, newFileName);

            if (taskChapterId.equals(tmp.getChapterId())) {
                retuenFileName = newFileName;
                fileName = tmp.getChapterId() + suffix;
            }
        }
        for (TaskChaptersDto t : chapters) {
            // 检查是否来自xml模板，2017-01-07
            String suffix = ".docx";
            if (1 == templateService.isWithXml(t, 1))
                suffix = ".rtf";
            templateService.autoCreateWordWithXml(t, header.getTaskHeaderId(), header.getProjectId(), 1, path);
            chapterFile = new File(path + t.getChapterId() + suffix);
            newFileName = new String((zipPath + t.getAttribute7() + "_" + t.getChapterId() + "_" + t.getChapterNo() + "_" + p.matcher(t.getChapterName()).replaceAll("").trim() + suffix).getBytes("UTF-8"));
            if (!chapterFile.exists())
                new File(newFileName).createNewFile();
            else
                FileUtil.copyFile(path + t.getChapterId() + suffix, newFileName);

            if (taskChapterId.equals(t.getChapterId())) {
                retuenFileName = newFileName;
                fileName = t.getChapterId() + suffix;
            }
        }
        HttpHeaders headers = new HttpHeaders();
        File file= new File(retuenFileName);
        headers.setContentDispositionFormData("attachment", java.net.URLEncoder.encode(retuenFileName, "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);

    }

    /**
     * 自动根据XNL模板创建文件
     * @param taskHeaderId
     * @param userId
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "autoCreateWordWithXml", method = RequestMethod.POST)
    public String autoCreateWordWithXml(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "0") Long taskHeaderId,
                                         @RequestParam(value = "userId", required = false, defaultValue = "0") Long userId,
                                        HttpServletRequest request
                                        ) throws IOException {
        if (0 == userId)
            userId = SessionUtil.getLdapUserInfo(request).getUserId();

        DataTablesDTO headerDT = taskService.getTaskHeaderList("", "", "", "", "", "", 0, taskHeaderId, 0, userId,null, 1, 1000);
        TaskHeaderDto header = (TaskHeaderDto) headerDT.getDataSource().get(0);

        String path = nerinProperties.getNbcc().getWordFileUrl() + taskHeaderId + "/";
        String zipPath = nerinProperties.getNbcc().getWordFileUrl() + taskHeaderId + "/zip/";
        DataTablesDTO chapter = taskService.getTaskChaptersList(taskHeaderId, "REPO", "CHAPTER", null, userId , 0 , 1, 1000, 1);
        List<TaskChaptersDto> chapters = chapter.getDataSource();

        for (TaskChaptersDto t : chapters) {
            if (1 == templateService.isWithXml(t, 1))
                templateService.autoCreateWordWithXmlNoCopy(t, header.getTaskHeaderId(), header.getProjectId(), 1, path);
        };
        return "true";
    }

    /**
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "checkFile", method = RequestMethod.POST)
    public Long checkFile(@RequestParam(value = "fileName", required = false, defaultValue = "") String fileName ) throws IOException {
        return templateService.checkFile(fileName);
    }

    /**
     * 全文本件下载
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "downloadAllWord", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadWordFile(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "0") Long taskHeaderId) throws IOException {
        String path = nerinProperties.getNbcc().getWordFileUrl() + taskHeaderId + "/" + "task_" + taskHeaderId + ".docx";
        File file= new File(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", java.net.URLEncoder.encode("全文本", "UTF-8"));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    /**
     * word文件上传
     * @param files 文件数组
     * @param taskHeaderId 任务头id
     * @param request
     * @return
     */
    @RequestMapping(value = "uploadWordFile", method = RequestMethod.POST)
    public String uploadWordFile(@RequestParam(value = "wordFile", required = false) MultipartFile[] files,
                                 @RequestParam(value = "taskHeaderId", required = false, defaultValue = "0") Long taskHeaderId,
                                 HttpServletRequest request) {
        String dir = nerinProperties.getNbcc().getWordFileUrl() + taskHeaderId + "/";
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
     * 获取历史文本章节列表
     * @param taskHeaderId
     * @param listType LIST 显示全部数据 REPO 显示启用的数据
      * @param lineType 行类型：COVER   封面、CHAPTER  章节
     * @param elementId   工作包ID（在专业检审提交时工作包预览时必输）*
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "taskChaptersHistoryList", method= RequestMethod.GET)
    public DataTablesDTO queryTaskChaptersHistoryList(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                                       @RequestParam(value = "listType", required = false, defaultValue = "REPO") String listType,
                                                      @RequestParam(value = "lineType", required = false, defaultValue = "CHAPTER") String lineType,
                                                      @RequestParam(value = "elementId", required = false, defaultValue = "") Long elementId,
                                                       @RequestParam(value = "pageNo", required = false, defaultValue = "1") long pageNo,
                                                       @RequestParam(value = "pageSize", required = false, defaultValue = "300") long pageSize,
                                                      @RequestParam(value = "type", required = false, defaultValue = "0") int type,
                                                      HttpServletRequest request) {
        return taskService.getTaskChaptersList(taskHeaderId, listType, lineType, elementId,SessionUtil.getLdapUserInfo(request).getUserId(), 0, pageNo, pageSize, type);
    }

    /**
     * 保存文本任务章节数据
     * @param chaptersDTOs
     * @param request
     * @return
     */
    @RequestMapping(value = "saveTaskChapter", method = RequestMethod.POST)
    public Map saveTaskChapter(@RequestBody List<TaskChaptersDto> chaptersDTOs, HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();

        int i = 0;
        // 如果有删除id，先进行删除
        if (StringUtils.isNotEmpty(chaptersDTOs.get(0).getDelTaskChapterIds())) {
            Map dMap = taskService.deleteTaskchapters(chaptersDTOs.get(0).getTaskHeaderId(),chaptersDTOs.get(0).getDelTaskChapterIds());
            if (1 != (Long) dMap.get(NbccParm.DB_STATE)) {
                restFulData.put(RestFulData.RETURN_CODE, dMap.get(NbccParm.DB_STATE) + "");
                restFulData.put(RestFulData.RETURN_MSG, dMap.get(NbccParm.DB_MSG) + "");
                i = 1;
            }
        }
        if (0 == i) {
            Map reMap = taskService.addOrUpTaskChapters(chaptersDTOs, SessionUtil.getLdapUserInfo(request).getUserId());
            if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
                restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
                restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
            } else {
                restFulData.putAll(reMap);
            }

            return restFulData;
            }
        return restFulData;

    }

    /**
     * 获取文本章节ID
     * @return
     */
    @RequestMapping(value = "getTaskChapterId", method = RequestMethod.POST)
    public long getTaskChapterId() {
        long i = taskService.getTaskchapterId();
        return i;
    }

    /**
     * 删除文本信息列表
     * @param taskHeaderId
     * @param taskChaperIds
     * @return
     */
    @RequestMapping(value = "deleteTaskchapters", method = RequestMethod.POST)
    public Map deleteTaskchapters(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") long taskHeaderId,
                                  @RequestParam(value = "taskChaperIds", required = false, defaultValue = "") String taskChaperIds
    ) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.deleteTaskchapters(taskHeaderId, taskChaperIds);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }

        return restFulData;
    }


    /**
     * 获取该文本任务章节中所有的状态列表
     * @param taskHeaderId
     * @param taskChapterId
     * @return
     */
    @RequestMapping(value = "getMyTaskChapterStatusList", method = RequestMethod.GET)
    public DataTablesDTO queryMyTaskChapterStatusList(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") long taskHeaderId,
                                                      @RequestParam(value = "taskChapterId", required = false, defaultValue = "") Long taskChapterId
                                                      ) {
        return taskService.getMyTaskChapterStatusList(taskHeaderId,taskChapterId);
    }

    /**
     * 获取该文本任务章节中所有的专业列表
     * @param taskHeaderId
     * @param taskChapterId
     * @return
     */
    @RequestMapping(value = "getMyTaskChapterSpecialtysList", method = RequestMethod.GET)
    public DataTablesDTO queryMyTaskChapterSpecialtysList(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") long taskHeaderId,
                                                           @RequestParam(value = "taskChapterId", required = false, defaultValue = "") Long taskChapterId
    ) {
        return taskService.getMyTaskChapterSpecialtysList(taskHeaderId,taskChapterId);
    }

    /**
     * 获取节点负责人列表
     * @param projectId
     * @param specialty
     * @param roleId
     * @return
     */
    @RequestMapping(value = "getTaskChapterResponsibleList", method = RequestMethod.GET)
    public DataTablesDTO queryTaskChapterResponsibleList(@RequestParam(value = "projectId", required = false, defaultValue = "") long projectId,
                                                          @RequestParam(value = "specialty", required = false, defaultValue = "") String specialty,
                                                         @RequestParam(value = "roleId", required = false, defaultValue = "") long roleId
    ) {
        return taskService.getTaskChapterResponsibleList(projectId,specialty,roleId);
    }

    /**
     * 获取工作包列表 （当文本章节选择的时候 projectId、specialty必填，当专业检审查询的时候则 taskHeaderId 必填）
     * @param projectId
     * @param specialty      当专业检审查询的时候必须为空
     * @param taskHeaderId  当文本章节选择的时候必须为空
     * @param request
     * @return
     */
    @RequestMapping(value = "getProjectElementList", method = RequestMethod.GET)
    public DataTablesDTO queryProjectElementList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                 @RequestParam(value = "specialty", required = false, defaultValue = "") String specialty,
                                                 @RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                                 HttpServletRequest request
    ) {
        return taskService.getProjectElementList(projectId,specialty,taskHeaderId,SessionUtil.getLdapUserInfo(request).getUserId());
    }


    /**
     * 专业检审状态回退时工作包列表
     * @param projectId
     * @param taskHeaderId
     * @param request
     * @return
     */
    @RequestMapping(value = "getRollBackProjectElementList", method = RequestMethod.GET)
    public DataTablesDTO getRollBackProjectElementList(@RequestParam(value = "projectId", required = false, defaultValue = "") Long projectId,
                                                 @RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                                 HttpServletRequest request
    ) {
        return taskService.getRollBackProjectElementList(projectId,taskHeaderId,SessionUtil.getLdapUserInfo(request).getUserId());
    }

    /**
     * 获取工作包对应的设计人员、校核人员、审核人员 、审定人员值列表
     * @param elementId
     * @return
     */
    @RequestMapping(value = "getProjectElementPersonList", method = RequestMethod.GET)
    public ProjectPersonsDto queryProjectElementPersonList(@RequestParam(value = "elementId", required = false, defaultValue = "") long elementId
    ) {
        return taskService.getProjectElementPersonList(elementId);
    }

    /**
     * 获取权限转移的节点负责人员列表
     * @param taskResponsibleDtos
     * @return
     */
    @RequestMapping(value = "getTaskChapterResponsible2List", method = RequestMethod.POST)
    public List<TaskResponsibleDto> queryTaskChapterResponsible2List(@RequestBody List<TaskResponsibleDto> taskResponsibleDtos
    ) {
        return taskService.getTaskChapterResponsible2List(taskResponsibleDtos);
    }
    /**
     * 权限转移
     * @param taskResponsiblesDto
     * @param request
     * @return
     */
    @RequestMapping(value = "updateTaskResponsible", method = RequestMethod.POST)
    public Map updateTaskResponsible(@RequestBody TaskResponsiblesDto taskResponsiblesDto, HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.updateTaskResponsible(taskResponsiblesDto.getTaskHeaderId(),
                                                        taskResponsiblesDto.getTaskChaperIds(),
                                                        taskResponsiblesDto.getTaskResponsibleDtos(),
                                                        SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }


    /**
     * 修改文本任务行状态
     * @param taskHeaderId
     * @param taskChaperId
     * @param chapterStatus
     * @return
     */
    /*
    @RequestMapping(value = "updateTaskChapterStatus", method = RequestMethod.POST)
    public Map updateTaskChapterStatus(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                       @RequestParam(value = "taskChaperId", required = false, defaultValue = "") Long taskChaperId,
                                       @RequestParam(value = "chapterStatus", required = false, defaultValue = "") String chapterStatus
                                       ) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.updateTaskChapterStatus(taskHeaderId, taskChaperId, chapterStatus);
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }
    */

    /**
     * 签出时提示的专业（人员有权限检出的并未检出专业）
     * @param taskHeaderId
     * @param request
     * @return
     */
    @RequestMapping(value = "getSpecialtyForLockList", method = RequestMethod.GET)
    public DataTablesDTO querySpecialtyForLockList(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") long taskHeaderId,
                                                        HttpServletRequest request
    ) {
        return taskService.getSpecialtyForLockList(taskHeaderId, SessionUtil.getLdapUserInfo(request).getUserId());
    }

    /**
     * 检出章节信息 （先执行更新节点负责人、工作包、设计、校核、审核、审定人等，在执行锁定操作）
     * @param taskHeaderId
     * @param taskChaperIds
     * @param request
     * @return
     */
    @RequestMapping(value = "lockTaskchapters", method = RequestMethod.POST)
    public Map lockTaskchapters(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                @RequestParam(value = "taskChaperIds", required = false, defaultValue = "") String taskChaperIds,
                                HttpServletRequest request) {
        //先执行更新节点负责人、工作包、设计、校核、审核、审定人等操作
        Map rMap = taskService.updateTaskElement(taskHeaderId,null);

        //执行锁定操
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.lockTaskchapters(taskHeaderId,taskChaperIds, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 检入章节信息（解除锁定）
     * @param taskHeaderId
     * @param request
     * @return
     */
    @RequestMapping(value = "unlockTaskchapters", method = RequestMethod.POST)
    public Map unlockTaskchapters(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                  HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.unLockTaskchapters(taskHeaderId, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 发布时检查专业是否都已分配  当setDataTotal的值不等于 0 时则说明存在未分配的专业
     * @param taskHeaderId
     * @param request
     * @return
     */
    @RequestMapping(value = "checkTaskSpecialty", method = RequestMethod.GET)
    public DataTablesDTO queryCheckTaskSpecialtyList(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") long taskHeaderId,
                                                     HttpServletRequest request
    ) {
        return taskService.checkTaskSpecialty(taskHeaderId,SessionUtil.getLdapUserInfo(request).getUserId());
    }

    /**
     * 文本任务章节发布
     * @param taskHeaderId
     * @param chapterIds 可多选，逗号隔开
     * @return
     */
    @RequestMapping(value = "publishTaskChapters", method = RequestMethod.POST)
    public Map publishTaskChapters(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                   @RequestParam(value = "chapterIds", required = false, defaultValue = "") String chapterIds,
                                  HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.publishTaskChapters(taskHeaderId, chapterIds, SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 文本任务专业检审提交
     * @param taskHeaderId
     * @param elementId
     * @param request
     * @return
     */
    @RequestMapping(value = "iapprovingTaskChapters", method = RequestMethod.POST)
    public Map iapprovingTaskChapters(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                      @RequestParam(value = "elementId", required = false, defaultValue = "") Long elementId,
                                       HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.iapprovingTaskChapters(taskHeaderId,elementId,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 专业检审状态回退
     * @param taskHeaderId
     * @param elementId
     * @param request
     * @return
     */
    @RequestMapping(value = "nIapprovedTaskChapters", method = RequestMethod.POST)
    public Map nIapprovedTaskChapters(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                      @RequestParam(value = "elementId", required = false, defaultValue = "") Long elementId,
                                      HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.nIapprovedTaskChapters(taskHeaderId,elementId,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 公司级评审申请
     * @param taskHeaderId
     * @param request
     * @return
     */
    @RequestMapping(value = "applyTaskChapters", method = RequestMethod.POST)
    public Map applyTaskChapters(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                      HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.applyTaskChapters(taskHeaderId,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 提交公司级评审
     * @param taskHeaderId
     * @param ChapterIds
     * @param request
     * @return
     */
    @RequestMapping(value = "eapprovingTaskChapters", method = RequestMethod.POST)
    public Map eapprovingTaskChapters(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                       @RequestParam(value = "ChapterIds", required = false, defaultValue = "") String ChapterIds,
                                       HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.eapprovingTaskChapters(taskHeaderId,ChapterIds,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     * 填写院审结论表
     * @param taskHeaderId
     * @param request
     * @return
     */
    @RequestMapping(value = "approvingaTaskChapters", method = RequestMethod.POST)
    public Map approvingaTaskChapters(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                      HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.approvingaTaskChapters(taskHeaderId,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }

    /**
     *文本任务提交审批
     * @param taskHeaderId
     * @param request
     * @return
     */
    @RequestMapping(value = "approvingTaskChapters", method = RequestMethod.POST)
    public Map approvingTaskChapters(@RequestParam(value = "taskHeaderId", required = false, defaultValue = "") Long taskHeaderId,
                                      HttpServletRequest request) {
        Map restFulData = RestFulData.getRestInitData();
        Map reMap = taskService.approvingTaskChapters(taskHeaderId,SessionUtil.getLdapUserInfo(request).getUserId());
        if (1 != (Long) reMap.get(NbccParm.DB_STATE)) {
            restFulData.put(RestFulData.RETURN_CODE, reMap.get(NbccParm.DB_STATE) + "");
            restFulData.put(RestFulData.RETURN_MSG, reMap.get(NbccParm.DB_MSG) + "");
        } else {
            restFulData.putAll(reMap);
        }
        return restFulData;
    }
}
