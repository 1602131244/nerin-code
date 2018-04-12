package com.nerin.nims.opt.cadi.dto;

/**
 * Created by user on 16/8/29.
 */
import java.util.Date;
public class PrintOrderHeaderDTO {
    Long pltOrderHeaderId; //出图单内部ID
    String projectNum; //项目编号
    String projectName; //项目全称
    String phaseCode;
    String phaseName;
    String equipmentName;
    Long carryingOutOrganizationId; //项目所属组织
    String taskName; //子项
    String taskCode; //子项号
    String mainSpecialityCode;
    String mainSpeciality; //专业
    String projectManager; //项目经理用户名
    String specManager; //专业负责人用户名
    String SPECManagerEm; //专业负责人员工编号
    String SPECManagerName; //专业负责人姓名
    String projectSecretary; //项目秘书用户名
    String PROJECTSecretaryEm; //项目秘书员工编号
    String PROJECTSecretaryName; //项目秘书姓名
    String pltNum; //问印单编号
    String pltContent; //文印内容
    String pltStatus; //文印状态
    String pltCategory; //文印类型
    String isTypeset; //是否排版
    Long PltDepartmentId; //部门
    String pltRequestor; //文印人用户名
    String PLTRequestorEm; //文印人员工编号
    String PLTRequestorName; //文印人姓名
    Date creationDate; //创建日期（文印日期）
    Long totalNum; //总份数
    Long sendOwner; //发送业主份（套）数
    Long sgService; //施工服务份（套）数
    Long archiveFlag; //归档份（套）数
    Double pltTotalPrice; //文（晒）印总价
    String attribute5;
    String attribute2;//添晒关联的新晒单

    public Long getPltOrderHeaderId() {
        return pltOrderHeaderId;
    }

    public void setPltOrderHeaderId(Long pltOrderHeaderId) {
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

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Long getCarryingOutOrganizationId() {
        return carryingOutOrganizationId;
    }

    public void setCarryingOutOrganizationId(Long carryingOutOrganizationId) {
        this.carryingOutOrganizationId = carryingOutOrganizationId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getMainSpecialityCode() {
        return mainSpecialityCode;
    }

    public void setMainSpecialityCode(String mainSpecialityCode) {
        this.mainSpecialityCode = mainSpecialityCode;
    }

    public String getMainSpeciality() {
        return mainSpeciality;
    }

    public void setMainSpeciality(String mainSpeciality) {
        this.mainSpeciality = mainSpeciality;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getSpecManager() {
        return specManager;
    }

    public void setSpecManager(String specManager) {
        this.specManager = specManager;
    }

    public String getSPECManagerEm() {
        return SPECManagerEm;
    }

    public void setSPECManagerEm(String SPECManagerEm) {
        this.SPECManagerEm = SPECManagerEm;
    }

    public String getSPECManagerName() {
        return SPECManagerName;
    }

    public void setSPECManagerName(String SPECManagerName) {
        this.SPECManagerName = SPECManagerName;
    }

    public String getProjectSecretary() {
        return projectSecretary;
    }

    public void setProjectSecretary(String projectSecretary) {
        this.projectSecretary = projectSecretary;
    }

    public String getPROJECTSecretaryEm() {
        return PROJECTSecretaryEm;
    }

    public void setPROJECTSecretaryEm(String PROJECTSecretaryEm) {
        this.PROJECTSecretaryEm = PROJECTSecretaryEm;
    }

    public String getPROJECTSecretaryName() {
        return PROJECTSecretaryName;
    }

    public void setPROJECTSecretaryName(String PROJECTSecretaryName) {
        this.PROJECTSecretaryName = PROJECTSecretaryName;
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

    public String getPltStatus() {
        return pltStatus;
    }

    public void setPltStatus(String pltStatus) {
        this.pltStatus = pltStatus;
    }

    public String getPltCategory() {
        return pltCategory;
    }

    public void setPltCategory(String pltCategory) {
        this.pltCategory = pltCategory;
    }

    public String getIsTypeset() {
        return isTypeset;
    }

    public void setIsTypeset(String isTypeset) {
        this.isTypeset = isTypeset;
    }

    public Long getPltDepartmentId() {
        return PltDepartmentId;
    }

    public void setPltDepartmentId(Long pltDepartmentId) {
        PltDepartmentId = pltDepartmentId;
    }

    public String getPltRequestor() {
        return pltRequestor;
    }

    public void setPltRequestor(String pltRequestor) {
        this.pltRequestor = pltRequestor;
    }

    public String getPLTRequestorEm() {
        return PLTRequestorEm;
    }

    public void setPLTRequestorEm(String PLTRequestorEm) {
        this.PLTRequestorEm = PLTRequestorEm;
    }

    public String getPLTRequestorName() {
        return PLTRequestorName;
    }

    public void setPLTRequestorName(String PLTRequestorName) {
        this.PLTRequestorName = PLTRequestorName;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getSendOwner() {
        return sendOwner;
    }

    public void setSendOwner(Long sendOwner) {
        this.sendOwner = sendOwner;
    }

    public Long getSgService() {
        return sgService;
    }

    public void setSgService(Long sgService) {
        this.sgService = sgService;
    }

    public Long getArchiveFlag() {
        return archiveFlag;
    }

    public void setArchiveFlag(Long archiveFlag) {
        this.archiveFlag = archiveFlag;
    }

    public Double getPltTotalPrice() {
        return pltTotalPrice;
    }

    public void setPltTotalPrice(Double pltTotalPrice) {
        this.pltTotalPrice = pltTotalPrice;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }
}
