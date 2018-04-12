package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class TreePaPublishStructureDetailsDTO {
    private Long taskElementId; //taskElementId
    //private Long taskWbsLevel;
    private Long taskStructureVerId;
   // private String taskStructureName;//taskStructureName
    private String iconCls;
    private String level;//level
    private String code;//code
    private String name;//name
    private Long taskTypeId;//taskTypeId
    private String taskType;//taskType
    private Date startDate;//startDate
    private Date endDate;//endDate
    private String dualName;//dualName
    private String workCoef;//workCoef
    private String status;//status
    private Long id;
    private Long parentId;
    private List<TreePaPublishStructureDetailsDTO> children = new ArrayList<TreePaPublishStructureDetailsDTO>();

    public Long getTaskStructureVerId() {
        return taskStructureVerId;
    }

    public void setTaskStructureVerId(Long taskStructureVerId) {
        this.taskStructureVerId = taskStructureVerId;
    }

    public Long getTaskElementId() {
        return taskElementId;
    }

    public void setTaskElementId(Long taskElementId) {
        this.taskElementId = taskElementId;
    }

   /* public Long getTaskWbsLevel() {
        return taskWbsLevel;
    }

    public void setTaskWbsLevel(Long taskWbsLevel) {
        this.taskWbsLevel = taskWbsLevel;
    }
*/
    /*public String getTaskStructureName() {
        return taskStructureName;
    }

    public void setTaskStructureName(String taskStructureName) {
        this.taskStructureName = taskStructureName;
    }*/

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public List<TreePaPublishStructureDetailsDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreePaPublishStructureDetailsDTO> children) {
        this.children = children;
    }
}
