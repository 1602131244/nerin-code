package com.nerin.nims.opt.navi.dto;
import java.util.Date;
/**
 * Created by Administrator on 2017/2/16.
 */
public class ProjectOverDueDTO {
    private Long projectId;//number://项目id；
    private String projName;//varchar2;//项目名称；
    private String elementNumber;//varchar2://工作包编号
    private String elementName;//varchar2;//工作包名称
    private String description;//varchar2;//工作包描述
    private Date dueDate;//varchar2;//时间
    private Long phaseCode;//varchar2//阶段
    private String specCode;//varchar2//专业
    private String workpkgType;//varchar2//工作包类型

    public String getSpecCode() {
        return specCode;
    }

    public void setSpecCode(String specCode) {
        this.specCode = specCode;
    }

    public String getWorkpkgType() {
        return workpkgType;
    }

    public void setWorkpkgType(String workpkgType) {
        this.workpkgType = workpkgType;
    }

    public Long getPhaseCode() {
        return phaseCode;
    }

    public void setPhaseCode(Long phaseCode) {
        this.phaseCode = phaseCode;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getElementNumber() {
        return elementNumber;
    }

    public void setElementNumber(String elementNumber) {
        this.elementNumber = elementNumber;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
