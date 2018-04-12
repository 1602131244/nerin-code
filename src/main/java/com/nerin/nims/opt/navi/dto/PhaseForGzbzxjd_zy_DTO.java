package com.nerin.nims.opt.navi.dto;

import java.util.List;

/**
 * Created by yinglgu on 7/30/2016.
 */
public class PhaseForGzbzxjd_zy_DTO {
    private String specialityCode;//varchar2：专业code
    private String specialityName;//varchar2：专业名称
    private String typeName;//varchar2: 类型，系统/子项/专业
    private Long projElementId;//：number：结构ID
    private Long typeId;//--节点类型: 10006--专业
    private Long objectId;//：number：节点id--树形结构
    private String taskName;//varchar2：节点/专业名称
    private Long parentsId;//number:父节点
    private double progress;//number：估算进度
    private String taskWbsNumber;

    // fancyTree
    private String title;
    private boolean folder = false;
    private String tooltip;
    private List<PhaseForGzbzxjd_zy_DTO> children;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTaskWbsNumber() {
        return taskWbsNumber;
    }

    public void setTaskWbsNumber(String taskWbsNumber) {
        this.taskWbsNumber = taskWbsNumber;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getProjElementId() {
        return projElementId;
    }

    public void setProjElementId(Long projElementId) {
        this.projElementId = projElementId;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public String getSpecialityCode() {
        return specialityCode;
    }

    public void setSpecialityCode(String specialityCode) {
        this.specialityCode = specialityCode;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public List<PhaseForGzbzxjd_zy_DTO> getChildren() {
        return children;
    }

    public void setChildren(List<PhaseForGzbzxjd_zy_DTO> children) {
        this.children = children;
    }

    public Long getParentsId() {
        return parentsId;
    }

    public void setParentsId(Long parentsId) {
        this.parentsId = parentsId;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
}
