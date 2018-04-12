package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/25.
 */
public class PaHistoryPbsDetailsDTO {
    private Long id;
    private Long pbsID;
    private String level;
    private String code;
    private String name;
    private String dualName;
    //private String pbsOrWbs;
    private Long parentId;



    public void setLevel(String level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getPbsID() {
        return pbsID;
    }

    public void setPbsID(Long pbsID) {
        this.pbsID = pbsID;
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
}
