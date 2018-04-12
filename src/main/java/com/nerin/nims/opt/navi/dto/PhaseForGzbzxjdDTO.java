package com.nerin.nims.opt.navi.dto;

import java.util.Date;

/**
 * Created by yinglgu on 7/30/2016.
 */
public class PhaseForGzbzxjdDTO {
    private Long projectId;//number：项目id
    private Long projElementId;//number：工作包id
    private String subTaskNumber;//varchar2：子项编号
    private String spac_code;
    private String subTask;//varchar2：子项名称
    private String spac;//varchar2：专业名称
    private String elementName;//varchar2：工作包名称
    private Date dueDate;//date：计划完成时间
    private Date completionDate;//date：实际完成时间
    private String projectStatusName;//varchar2：状态
    private String workpkgTypeCode;//varchar2：工作包类型code;
    private String workpkgType;//varchar2：工作包类型
    private Long completedPercentage;//number：进度
    private String actTime;//varchar2:工时对比
    private String design;//varchar2：设计人
    private String checked;//varcahr2：校核人
    private String examine;//varchar2：审核人
    private String authorize;//varchar2：审定人
    private String creater;//varchar2：策划人

    //
    private String fullName;//varchar2:人员名称
    private Long workTime;//number：工时
    private String role;//varchar2：角色
    private Long num;//number：排序id --后台排序用

    public String getSpac_code() {
        return spac_code;
    }

    public void setSpac_code(String spac_code) {
        this.spac_code = spac_code;
    }

    public String getWorkpkgTypeCode() {
        return workpkgTypeCode;
    }

    public void setWorkpkgTypeCode(String workpkgTypeCode) {
        this.workpkgTypeCode = workpkgTypeCode;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getWorkTime() {
        return workTime;
    }

    public void setWorkTime(Long workTime) {
        this.workTime = workTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getProjElementId() {
        return projElementId;
    }

    public void setProjElementId(Long projElementId) {
        this.projElementId = projElementId;
    }

    public String getSubTaskNumber() {
        return subTaskNumber;
    }

    public void setSubTaskNumber(String subTaskNumber) {
        this.subTaskNumber = subTaskNumber;
    }

    public String getSubTask() {
        return subTask;
    }

    public void setSubTask(String subTask) {
        this.subTask = subTask;
    }

    public String getSpac() {
        return spac;
    }

    public void setSpac(String spac) {
        this.spac = spac;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public String getProjectStatusName() {
        return projectStatusName;
    }

    public void setProjectStatusName(String projectStatusName) {
        this.projectStatusName = projectStatusName;
    }

    public String getWorkpkgType() {
        return workpkgType;
    }

    public void setWorkpkgType(String workpkgType) {
        this.workpkgType = workpkgType;
    }

    public Long getCompletedPercentage() {
        return completedPercentage;
    }

    public void setCompletedPercentage(Long completedPercentage) {
        this.completedPercentage = completedPercentage;
    }

    public String getActTime() {
        return actTime;
    }

    public void setActTime(String actTime) {
        this.actTime = actTime;
    }

    public String getDesign() {
        return design;
    }

    public void setDesign(String design) {
        this.design = design;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getExamine() {
        return examine;
    }

    public void setExamine(String examine) {
        this.examine = examine;
    }

    public String getAuthorize() {
        return authorize;
    }

    public void setAuthorize(String authorize) {
        this.authorize = authorize;
    }
}
