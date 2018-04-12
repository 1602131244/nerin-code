package com.nerin.nims.opt.nbcc.service;

import com.nerin.nims.opt.app.config.NerinProperties;
import com.nerin.nims.opt.base.util.FileUtil;
import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;
import com.nerin.nims.opt.nbcc.dto.TaskChaptersDto;
import com.nerin.nims.opt.nbcc.dto.TaskHeaderDto;
import com.nerin.nims.opt.nbcc.dto.TemplateChaptersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by yinglgu on 2016/12/7.
 */
@Component
public class TaskWordAsyncTask implements Runnable {

    private long userId;
    private long personId;
    private long taskId;

    @Autowired
    public TaskService taskService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private NerinProperties nerinProperties;

    @Override
    public void run() {
        DataTablesDTO headerDT = taskService.getTaskHeaderList("", "", "", "", "", "", 0, taskId, 0, userId, null, 1, 1000);
        TaskHeaderDto header = (TaskHeaderDto) headerDT.getDataSource().get(0);
        // 章节
        DataTablesDTO chapter = taskService.getTaskChaptersList(taskId, "REPO", "CHAPTER", null, userId , 0 , 1, 1000, 1);
        DataTablesDTO cover = taskService.getTaskChaptersList(taskId, "LIST", "COVER", null, userId , 0, 1, 1000, 1);
        if ("TEMPLATE".equals(header.getAttribute11())) {
            this.createWordWithTemplate(Long.parseLong(header.getAttribute12()), header.getTaskHeaderId(), (TaskChaptersDto)cover.getDataSource().get(0), chapter.getDataSource());
        } else {
            DataTablesDTO hisHeaderDT = taskService.getTaskHeaderList("", "", "", "", "", "", 0, Long.parseLong(header.getAttribute12()), 0, userId, null, 1, 1000);
            if (null != hisHeaderDT.getDataSource() && 0 < hisHeaderDT.getDataSource().size()) {
                TaskHeaderDto Hisheader = (TaskHeaderDto) hisHeaderDT.getDataSource().get(0);
                if (0 <= Hisheader.getProjectRoleName().indexOf("项目经理") || Hisheader.getCreatedBy() == userId) {
                    this.createWordWithTaskHistory(Hisheader.getTaskHeaderId(), header.getTaskHeaderId(), (TaskChaptersDto)cover.getDataSource().get(0), chapter.getDataSource(), personId);
                }
            }
        }
    }

    private void createWordWithTaskHistory(long historyId, long taskId, TaskChaptersDto taskCover, List<TaskChaptersDto> taskChapters, long personId) {
        DataTablesDTO chapterTmp = taskService.getTaskChaptersList(historyId, "REPO", "CHAPTER", null, userId , 0 , 1, 1000, 1);
        DataTablesDTO coverTmp = taskService.getTaskChaptersList(historyId, "LIST", "COVER", null, userId , 0 , 1, 1000, 1);
        List<TaskChaptersDto> chapters = chapterTmp.getDataSource();
        // 去掉root
        chapters.remove(0);
        taskChapters.remove(0);
        TaskChaptersDto cover = (TaskChaptersDto)coverTmp.getDataSource().get(0);
        String h_path = nerinProperties.getNbcc().getWordFileUrl() + historyId + "/";
        String path = nerinProperties.getNbcc().getWordFileUrl() + taskId + "/";
        File dirPath = new File(path);
        if(!dirPath.exists())
            dirPath.mkdirs();
        dirPath = new File(h_path);
        if(!dirPath.exists())
            dirPath.mkdirs();
        // 需要复制到哪的文件名
        String newFileName = "";
        // 源文件
        String sourceFileName = h_path + cover.getChapterId() + ".docx";
        File templateFile = new File(sourceFileName);
//        if (templateFile.exists()) {
//            newFileName = new String(path + taskCover.getChapterId() + ".docx");
//            FileUtil.copyFile(sourceFileName, newFileName);
//        }
        for (TaskChaptersDto t : taskChapters) {
            for (TaskChaptersDto th : chapters) {
                if (th.getChapterNo().equals(t.getChapterNo()) && th.getPersonIdResponsible() == personId) {
                    sourceFileName = h_path + th.getChapterId() + ".docx";
                    templateFile = new File(sourceFileName);
                    if (templateFile.exists()) {
                        newFileName = new String(path + t.getChapterId() + ".docx");
                        FileUtil.copyFile(sourceFileName, newFileName);
                        break;
                    }
                }
            }
        }
    }


    private void createWordWithTemplate(long templateId, long taskId, TaskChaptersDto taskCover, List<TaskChaptersDto> taskChapters) {
        List<TemplateChaptersDTO> chapters = templateService.getTemplateChaptersList(templateId, "CHAPTER", 1, 1000, 1);
        // 去掉root
        chapters.remove(0);
        taskChapters.remove(0);
        List<TemplateChaptersDTO> cover = templateService.getTemplateChaptersList(templateId, "COVER", 1, 1000, 1);
        String t_path = nerinProperties.getNbcc().getTemplateWordFileUrl() + templateId + "/";
        String path = nerinProperties.getNbcc().getWordFileUrl() + taskId + "/";
        File dirPath = new File(path);
        if(!dirPath.exists())
            dirPath.mkdirs();
        dirPath = new File(t_path);
        if(!dirPath.exists())
            dirPath.mkdirs();

        TemplateChaptersDTO tmp_cover = cover.get(0);
        // 需要复制到哪的文件名
        String newFileName = "";
        // 源文件
        String sourceFileName = t_path + tmp_cover.getChapterId() + ".docx";
        File templateFile = new File(sourceFileName);
//        if (templateFile.exists()) {
//            newFileName = new String(path + taskCover.getChapterId() + ".docx");
//            FileUtil.copyFile(sourceFileName, newFileName);
//        }
        for (TaskChaptersDto t : taskChapters) {
            for (TemplateChaptersDTO tm : chapters) {
                if (tm.getChapterNo().equals(t.getChapterNo())) {
                    sourceFileName = t_path + tm.getChapterId() + ".docx";
                    templateFile = new File(sourceFileName);
                    if (templateFile.exists()) {
                        newFileName = new String(path + t.getChapterId() + ".docx");
                        FileUtil.copyFile(sourceFileName, newFileName);
                        break;
                    }
                }
            }
        }
    }

    public long getPersonId() {
        return personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
}
