package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class TreePaHistoryPbsDeliverablesDTO {

    private long parentId;//专业ELEMENTID的版本ID
    private long id; //工作包ID
    private String dlvrName;//工作包名称
    private String level;//element type 工作包 系统 子项 专业
    private String iconCls;//图标
    private double workHour;//计划工时
    private String grandNum;//专业孙项号
    private String workCode;//工作包代字ID  对应后台 WDDA 的ID
    private String worktypeID;//工作包类型ID 图纸 文本
    private String description;//说明
    private String grandName;//专业孙项名称
    private String matCode;//关联物料编码
    private String matName;//关联物料名称
    private String code;//编号
    private String status;//编号
    private List<TreePaHistoryPbsDeliverablesDTO> children = new ArrayList<TreePaHistoryPbsDeliverablesDTO>();
    private List<TreePaHistoryPbsDeliverablesDTO> speChildren = new ArrayList<TreePaHistoryPbsDeliverablesDTO>();

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDlvrName() {
        return dlvrName;
    }

    public void setDlvrName(String dlvrName) {
        this.dlvrName = dlvrName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(double workHour) {
        this.workHour = workHour;
    }

    public String getGrandNum() {
        return grandNum;
    }

    public void setGrandNum(String grandNum) {
        this.grandNum = grandNum;
    }

    public String getWorktypeID() {
        return worktypeID;
    }

    public void setWorktypeID(String worktypeID) {
        this.worktypeID = worktypeID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGrandName() {
        return grandName;
    }

    public void setGrandName(String grandName) {
        this.grandName = grandName;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public List<TreePaHistoryPbsDeliverablesDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TreePaHistoryPbsDeliverablesDTO> children) {
        this.children = children;
    }

    public List<TreePaHistoryPbsDeliverablesDTO> getSpeChildren() {
        return speChildren;
    }

    public void setSpeChildren(List<TreePaHistoryPbsDeliverablesDTO> speChildren) {
        this.speChildren = speChildren;
    }

    public String getStatus() {
        return "生效";
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }
}