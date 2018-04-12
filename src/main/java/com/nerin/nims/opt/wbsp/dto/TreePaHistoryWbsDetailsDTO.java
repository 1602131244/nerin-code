package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class TreePaHistoryWbsDetailsDTO {
    private Long id;
   // private Long taskWbsLevel;
    private Long WbsId;
    private String iconCls;
    private String level;
    private String code;
    private String name;
    private Date startDate;
    private Date endDate;
    private String dualName;
    private String workCoef;
    //private String pbsOrWbs;
    private Long parentId;
    private String status;
    private List<TreePaHistoryWbsDetailsDTO> children = new ArrayList<TreePaHistoryWbsDetailsDTO>();

  /*  public Long getTaskWbsLevel() {
        return taskWbsLevel;
    }

    public void setTaskWbsLevel(Long taskWbsLevel) {
        this.taskWbsLevel = taskWbsLevel;
    }*/

    public String getWorkCoef() {
        return workCoef;
    }

    public void setWorkCoef(String workCoef) {
        this.workCoef = workCoef;
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

    public void setLevel(String level) {
        this.level = level;
        String icon = new String();
        if (!level.equals("root")){
            icon = "icon-"+level+"br";}
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

    public List<TreePaHistoryWbsDetailsDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreePaHistoryWbsDetailsDTO> children) {
        this.children = children;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
