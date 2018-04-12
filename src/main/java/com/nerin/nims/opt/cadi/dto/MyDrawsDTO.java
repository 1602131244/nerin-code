package com.nerin.nims.opt.cadi.dto;

/**
 * Created by yinglgu on 2016/12/20.
 */
public class MyDrawsDTO {
    private String projectNum;//, -- 项目编号
    private String projectName;//, -- 项目名称
    private String equipmentName;//, -- 设备名称
    private String phaseCode;//, -- 阶段代码
    private String phaseName;//, -- 阶段名称
    private String taskCode;//, -- 子项
    private String taskName;//, -- 子项名称
    private String mainSpeciality;//, -- 专业
    private String pltNum;//, -- 文印单号
    private String pltContent;//, -- 文印内容
    private String pltCategory;//, -- 文印类型
    private String pltCategoryName;//, -- 文印类型名称
    private String pltStatus;//, -- 文印状态
    private String pltsStatusName;//, --文印状态名称
    private String docPrintPerson;//, -- 文印归档人工号（文印人暂取)
    private String docPrintPersonName;//, -- 文印归档人名称
    private String docPrintDate;//, -- 文印日期
    private String archiveFlag;//, -- 是否归档
    private String projectId;
    private String paperType;
    private String pltOrderHeaderId;

    public String getPaperType() {
        return paperType;
    }

    public void setPaperType(String paperType) {
        this.paperType = paperType;
    }

    public String getPltOrderHeaderId() {
        return pltOrderHeaderId;
    }

    public void setPltOrderHeaderId(String pltOrderHeaderId) {
        this.pltOrderHeaderId = pltOrderHeaderId;
    }

    public String getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(String projectNum) {
        this.projectNum = projectNum;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getPhaseCode() {
        return phaseCode;
    }

    public void setPhaseCode(String phaseCode) {
        this.phaseCode = phaseCode;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getMainSpeciality() {
        return mainSpeciality;
    }

    public void setMainSpeciality(String mainSpeciality) {
        this.mainSpeciality = mainSpeciality;
    }

    public String getPltNum() {
        return pltNum;
    }

    public void setPltNum(String pltNum) {
        this.pltNum = pltNum;
    }

    public String getPltContent() {
        return pltContent;
    }

    public void setPltContent(String pltContent) {
        this.pltContent = pltContent;
    }

    public String getPltCategory() {
        return pltCategory;
    }

    public void setPltCategory(String pltCategory) {
        this.pltCategory = pltCategory;
    }

    public String getPltCategoryName() {
        return pltCategoryName;
    }

    public void setPltCategoryName(String pltCategoryName) {
        this.pltCategoryName = pltCategoryName;
    }

    public String getPltStatus() {
        return pltStatus;
    }

    public void setPltStatus(String pltStatus) {
        this.pltStatus = pltStatus;
    }

    public String getPltsStatusName() {
        return pltsStatusName;
    }

    public void setPltsStatusName(String pltsStatusName) {
        this.pltsStatusName = pltsStatusName;
    }

    public String getDocPrintPerson() {
        return docPrintPerson;
    }

    public void setDocPrintPerson(String docPrintPerson) {
        this.docPrintPerson = docPrintPerson;
    }

    public String getDocPrintPersonName() {
        return docPrintPersonName;
    }

    public void setDocPrintPersonName(String docPrintPersonName) {
        this.docPrintPersonName = docPrintPersonName;
    }

    public String getDocPrintDate() {
        return docPrintDate;
    }

    public void setDocPrintDate(String docPrintDate) {
        this.docPrintDate = docPrintDate;
    }

    public String getArchiveFlag() {
        return archiveFlag;
    }

    public void setArchiveFlag(String archiveFlag) {
        this.archiveFlag = archiveFlag;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
