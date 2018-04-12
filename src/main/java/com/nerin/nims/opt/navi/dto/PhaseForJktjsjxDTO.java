package com.nerin.nims.opt.navi.dto;

import java.util.Date;

/**
 * Created by yinglgu on 8/3/2016.
 */
public class PhaseForJktjsjxDTO {

    private String subTask;//：varchar2：子项
    private String receiveSpecialty;//：varchar2：接收专业
    private Long projElementId;//：number：工作包id
    private String elementName;//：varchar2：工作包名称
    private Date dueDate;//：date：计划完成时间
    private Date completionDate;//：date：实际完成时间
    private String projectStatusName;//：varchar2：状态
    private String design;//：varchar2：设计人
    private String checked;//：varchar2：校核人
    private String examine;//：varchar2：审核人
    private String authorize;//：varchar2：审定人
    private String creater;//：varchar2：策划人

    public String getSubTask() {
        return subTask;
    }

    public void setSubTask(String subTask) {
        this.subTask = subTask;
    }

    public String getReceiveSpecialty() {
        return receiveSpecialty;
    }

    public void setReceiveSpecialty(String receiveSpecialty) {
        this.receiveSpecialty = receiveSpecialty;
    }

    public Long getProjElementId() {
        return projElementId;
    }

    public void setProjElementId(Long projElementId) {
        this.projElementId = projElementId;
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

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }
}
