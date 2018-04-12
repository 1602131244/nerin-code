package com.nerin.nims.opt.wh.dto;

/**
 * Created by Administrator on 2016/8/3.
 */
public class TaskMDTO {


    private Long id;
    private Long taskId;
    private String taskName;
    private float accuWorkHour;
    private String projNum;
    private String projName;
    private long projID;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public float getAccuWorkHour() {
        return accuWorkHour;
    }

    public void setAccuWorkHour(float accuWorkHour) {
        this.accuWorkHour = accuWorkHour;
    }

    public String getProjNum() {
        return projNum;
    }

    public void setProjNum(String projNum) {
        this.projNum = projNum;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public long getProjID() {
        return projID;
    }

    public void setProjID(long projID) {
        this.projID = projID;
    }
}
