package com.nerin.nims.opt.nbcc.dto;

import java.util.List;

/**
 * Created by Administrator on 2016/7/19.
 */
public class TaskResponsiblesDto {
    private long taskHeaderId;
    private String taskChaperIds;
    private List<TaskResponsibleDto> taskResponsibleDtos;

    public long getTaskHeaderId() {
        return taskHeaderId;
    }

    public void setTaskHeaderId(long taskHeaderId) {
        this.taskHeaderId = taskHeaderId;
    }

    public String getTaskChaperIds() {
        return taskChaperIds;
    }

    public void setTaskChaperIds(String taskChaperIds) {
        this.taskChaperIds = taskChaperIds;
    }

    public List<TaskResponsibleDto> getTaskResponsibleDtos() {
        return taskResponsibleDtos;
    }

    public void setTaskResponsibleDtos(List<TaskResponsibleDto> taskResponsibleDtos) {
        this.taskResponsibleDtos = taskResponsibleDtos;
    }
}
