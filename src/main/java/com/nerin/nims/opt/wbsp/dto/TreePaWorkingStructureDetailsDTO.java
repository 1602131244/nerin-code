package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class TreePaWorkingStructureDetailsDTO {
    private Long projElementId;
    private Long id;
    //private Long taskWbsLevel;
    private String iconCls;
    private String level;
    private String code;
    private String name;
    private Long taskTypeId;
    private String taskType;
    private Date startDate;
    private Date endDate;
    private String dualName;
    private String workCoef;
    private String status;
    private String srcStru;//taskSourceClass
    private String srcID;//taskSourceStructureVerId
    private String src;//taskSourceStructurename
    private String srcRow;//staskSourceEleVerId
    private String flag;//staskSourceEleVerId
    private Long parentId;
    private List<TreePaWorkingStructureDetailsDTO> children = new ArrayList<TreePaWorkingStructureDetailsDTO>();

   /* public Long getTaskWbsLevel() {
        return taskWbsLevel;
    }

    public void setTaskWbsLevel(Long taskWbsLevel) {
        this.taskWbsLevel = taskWbsLevel;
    }*/

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Long getProjElementId() {
        return projElementId;
    }

    public void setProjElementId(Long projElementId) {
        this.projElementId = projElementId;
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

    public void setLevel(String level) {
        this.level = level;
        String icon = new String();
        if (!level.equals("root")){
            icon = "icon-"+level+"br";}
        this.setIconCls(icon);
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

    public Long getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(Long taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
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

    public String getWorkCoef() {
        return workCoef;
    }

    public void setWorkCoef(String workCoef) {
        this.workCoef = workCoef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;

    }

    public String getSrcStru() {
        return srcStru;
    }

    public void setSrcStru(String srcStru) {
        this.srcStru = srcStru;
    }

    public String getSrcID() {
        return srcID;
    }

    public void setSrcID(String srcID) {
        this.srcID = srcID;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getSrcRow() {
        return srcRow;
    }

    public void setSrcRow(String srcRow) {
        this.srcRow = srcRow;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<TreePaWorkingStructureDetailsDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreePaWorkingStructureDetailsDTO> children) {
        this.children = children;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }
}
