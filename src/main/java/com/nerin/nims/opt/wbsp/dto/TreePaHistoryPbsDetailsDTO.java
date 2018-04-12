package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class TreePaHistoryPbsDetailsDTO {
    private Long id;
    // private Long taskWbsLevel;
    private Long PbsId;
    private String iconCls;
    private String level;
    private String code;
    private String name;
    private String dualName;
    //private String pbsOrWbs;
    private Long parentId;
    private List<TreePaHistoryPbsDetailsDTO> children = new ArrayList<TreePaHistoryPbsDetailsDTO>();

  /*  public Long getTaskWbsLevel() {
        return taskWbsLevel;
    }

    public void setTaskWbsLevel(Long taskWbsLevel) {
        this.taskWbsLevel = taskWbsLevel;
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPbsId() {
        return PbsId;
    }

    public void setPbsId(Long pbsId) {
        PbsId = pbsId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
        String icon = new String();
        if (!level.equals("root")) {
            icon = "icon-" + level + "br";
        }
        this.setIconCls(icon);
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        if(code == null || code.length() ==0){this.code = "";}
        else{this.code = code;}
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

   /* public String getPbsOrWbs() {
        return pbsOrWbs;
    }

    public void setPbsOrWbs(String pbsOrWbs) {
        this.pbsOrWbs = pbsOrWbs;
    }*/

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<TreePaHistoryPbsDetailsDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreePaHistoryPbsDetailsDTO> children) {
        this.children = children;
    }
}
