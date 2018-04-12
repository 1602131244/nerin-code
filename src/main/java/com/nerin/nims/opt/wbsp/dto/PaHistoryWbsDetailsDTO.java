package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/25.
 */
public class PaHistoryWbsDetailsDTO {
    private Long id;
    private Long WbsId;
    //private Long taskWbsLevel;
    private String level;
    private String code;
    private String name;
    private Date startDate;
    private Date endDate;
    private String dualName;
    private String workCoef;
    //private String pbsOrWbs;
    private Long parentId;
    private String taskStatusCode;
    private String defaultFlag;

    public String getTaskStatusCode() {
        return taskStatusCode;
    }

    public void setTaskStatusCode(String taskStatusCode) {
        String statusCode = new String();
        if (taskStatusCode == null) {
            statusCode = "生效";
        } else {
            switch (taskStatusCode) {
                case "N":
                    statusCode = "新增";
                    break;
                case "E":
                    statusCode = "生效";
                    break;
                case "L":
                    statusCode = "失效";
                    break;
                default:
                    statusCode = "生效";
                    break;
            }
        }
        this.taskStatusCode = statusCode;
    }
    public String getWorkCoef() {
        return workCoef;
    }

    public void setWorkCoef(String workCoef) {
        this.workCoef = workCoef;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getWbsId() {
        return WbsId;
    }

    public void setWbsId(Long wbsId) {
        WbsId = wbsId;
    }

    public String getLevel() {

        return level;
    }

    public void setLevel(long taskTypeId) {
      String typeId =  Long.toString(taskTypeId);
      String level = new String();
        switch (typeId)
        {
            case "0" :
                level = "root";
                break;
            case "1" :
                level = "sys";
                break;
            case "2" :
                level = "div";
                break;
            case "3" :
                level = "spe";
                break;
            case "10003" :
                level = "root";
                break;
            case "10004" :
                level = "sys";
                break;
            case "10005" :
                level = "div";
                break;
            case "10006" :
                level = "spe";
                break;
        }
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDualName() {
        return dualName;
    }

    public void setDualName(String dualName) {
        this.dualName = dualName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }
}
