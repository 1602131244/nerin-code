package com.nerin.nims.opt.cadi.dto;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 16/7/15.
 */
public class DesignChangeDTO {

    private Long id;        //did 图纸唯一内部系统id
    private String drawNum; //图号
    private String reviseNum;//修正号
    private String drawName;//图名
    private String xSize;//图幅
    private Long   projectId; //项目ID
    private String xProjectNum;//项目编号
    private String projectSName; //项目简称

    private String xDesignPhase;//项目阶段
    private String phaseName;   //阶段名称
    private String xDivisionNum;//子项号
    private String xDivision;//子项
    private String xSpecialty;//专业代码
    private String specialityName; //专业名称
    private Long xDlvrId;//工作包id
    private String xDlvrName;//工作包
    private String xEmFlag;//紧急放行标志
    private String xDesignDate;//设计日期
    private String xWpApprStatus;//审批状态
    private String xCountersignStatus;//对图状态
    private String xPltStatus;//出图状态
    private String xEquipmentNum;//设备编号
    private String xEquipment; //设备名称

    private String XDESIGNED;//设计人工号
    private String XDESIGNED_NAME;//设计人
    private String XCHECKED;//校核人工号
    private String XCHECKED_NAME;//校核人
    private String XREVIEWED;//审核人工号
    private String XREVIEWED_NAME;//审核人
    private String XAPPROVED;//审定人工号
    private String XAPPROVED_NAME; //审定人
    private String XSPECIALTY_MANAGER_NAME;//专业负责人名
    private String XPROJECT_MANAGER;//项目经理工号
    private String XPROJECT_MANAGER_NAME;//项目经理
    private String Xregistered_Engineer;//注册工程师工号
    private String Xregistered_Engineer_Name;//注册工程师
    private String xfangan;//方案设计人工号
    private String xfangan_name; //方案设计人
    private String xspecialtySeq; // 专业孙项
    private String dlvr_status_code; //工作包状态代字
    private String dlvr_status; // 工作包状态
    private Long dlvr_version;//工作包版本


    //加入所属组织字段
    private String xOrganization; //所属组织字段

    public String getXDESIGNED() {
        return XDESIGNED;
    }

    public void setXDESIGNED(String XDESIGNED) {
        this.XDESIGNED = XDESIGNED;
    }

    public String getXDESIGNED_NAME() {
        return XDESIGNED_NAME;
    }

    public void setXDESIGNED_NAME(String XDESIGNED_NAME) {
        this.XDESIGNED_NAME = XDESIGNED_NAME;
    }

    public String getXCHECKED() {
        return XCHECKED;
    }

    public void setXCHECKED(String XCHECKED) {
        this.XCHECKED = XCHECKED;
    }

    public String getXCHECKED_NAME() {
        return XCHECKED_NAME;
    }

    public void setXCHECKED_NAME(String XCHECKED_NAME) {
        this.XCHECKED_NAME = XCHECKED_NAME;
    }

    public String getXREVIEWED() {
        return XREVIEWED;
    }

    public void setXREVIEWED(String XREVIEWED) {
        this.XREVIEWED = XREVIEWED;
    }

    public String getXREVIEWED_NAME() {
        return XREVIEWED_NAME;
    }

    public void setXREVIEWED_NAME(String XREVIEWED_NAME) {
        this.XREVIEWED_NAME = XREVIEWED_NAME;
    }

    public String getXAPPROVED() {
        return XAPPROVED;
    }

    public void setXAPPROVED(String XAPPROVED) {
        this.XAPPROVED = XAPPROVED;
    }

    public String getXAPPROVED_NAME() {
        return XAPPROVED_NAME;
    }

    public void setXAPPROVED_NAME(String XAPPROVED_NAME) {
        this.XAPPROVED_NAME = XAPPROVED_NAME;
    }

    public String getXSPECIALTY_MANAGER_NAME() {
        return XSPECIALTY_MANAGER_NAME;
    }

    public void setXSPECIALTY_MANAGER_NAME(String XSPECIALTY_MANAGER_NAME) {
        this.XSPECIALTY_MANAGER_NAME = XSPECIALTY_MANAGER_NAME;
    }

    public String getXPROJECT_MANAGER() {
        return XPROJECT_MANAGER;
    }

    public void setXPROJECT_MANAGER(String XPROJECT_MANAGER) {
        this.XPROJECT_MANAGER = XPROJECT_MANAGER;
    }

    public String getXPROJECT_MANAGER_NAME() {
        return XPROJECT_MANAGER_NAME;
    }

    public void setXPROJECT_MANAGER_NAME(String XPROJECT_MANAGER_NAME) {
        this.XPROJECT_MANAGER_NAME = XPROJECT_MANAGER_NAME;
    }

    public String getXregistered_Engineer() {
        return Xregistered_Engineer;
    }

    public void setXregistered_Engineer(String xregistered_Engineer) {
        Xregistered_Engineer = xregistered_Engineer;
    }

    public String getXregistered_Engineer_Name() {
        return Xregistered_Engineer_Name;
    }

    public void setXregistered_Engineer_Name(String xregistered_Engineer_Name) {
        Xregistered_Engineer_Name = xregistered_Engineer_Name;
    }

    public String getXfangan() {
        return xfangan;
    }

    public void setXfangan(String xfangan) {
        this.xfangan = xfangan;
    }

    public String getXfangan_name() {
        return xfangan_name;
    }

    public void setXfangan_name(String xfangan_name) {
        this.xfangan_name = xfangan_name;
    }

    public String getDrawNum() {
        return drawNum;
    }

    public void setDrawNum(String drawNum) {
        this.drawNum = drawNum;
    }

    public String getReviseNum() {
        return reviseNum;
    }

    public void setReviseNum(String reviseNum) {
        this.reviseNum = reviseNum;
    }

    public String getDrawName() {
        return drawName;
    }

    public void setDrawName(String drawName) {
        this.drawName = drawName;
    }

    public String getxSize() {
        return xSize;
    }

    public void setxSize(String xSize) {
        this.xSize = xSize;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getxProjectNum() {
        return xProjectNum;
    }

    public void setxProjectNum(String xProjectNum) {
        this.xProjectNum = xProjectNum;
    }

    public String getProjectSName() {
        return projectSName;
    }

    public void setProjectSName(String projectSName) {
        this.projectSName = projectSName;
    }

    public String getxDesignPhase() {
        return xDesignPhase;
    }

    public void setxDesignPhase(String xDesignPhase) {
        this.xDesignPhase = xDesignPhase;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getxDivisionNum() {
        return xDivisionNum;
    }

    public void setxDivisionNum(String xDivisionNum) {
        this.xDivisionNum = xDivisionNum;
    }

    public String getxDivision() {
        return xDivision;
    }

    public void setxDivision(String xDivision) {
        this.xDivision = xDivision;
    }

    public String getxSpecialty() {
        return xSpecialty;
    }

    public void setxSpecialty(String xSpecialty) {
        this.xSpecialty = xSpecialty;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public Long getxDlvrId() {
        return xDlvrId;
    }

    public void setxDlvrId(Long xDlvrId) {
        this.xDlvrId = xDlvrId;
    }

    public String getxDlvrName() {
        return xDlvrName;
    }

    public void setxDlvrName(String xDlvrName) {
        this.xDlvrName = xDlvrName;
    }

    public String getxEmFlag() {
        return xEmFlag;
    }

    public void setxEmFlag(String xEmFlag) {
        this.xEmFlag = xEmFlag;
    }

    public String getxDesignDate() {
        return xDesignDate;
    }

    public void setxDesignDate(String xDesignDate) {
        this.xDesignDate = xDesignDate;
    }

    public String getxWpApprStatus() {
        return xWpApprStatus;
    }

    public void setxWpApprStatus(String xWpApprStatus) {
        this.xWpApprStatus = xWpApprStatus;
    }

    public String getxCountersignStatus() {
        return xCountersignStatus;
    }

    public void setxCountersignStatus(String xCountersignStatus) {
        this.xCountersignStatus = xCountersignStatus;
    }

    public String getxPltStatus() {
        return xPltStatus;
    }

    public void setxPltStatus(String xPltStatus) {
        this.xPltStatus = xPltStatus;
    }

    public String getxEquipmentNum() {
        return xEquipmentNum;
    }

    public void setxEquipmentNum(String xEquipmentNum) {
        this.xEquipmentNum = xEquipmentNum;
    }

    public String getxEquipment() {
        return xEquipment;
    }

    public void setxEquipment(String xEquipment) {
        this.xEquipment = xEquipment;
    }

    public String getXspecialtySeq() {
        return xspecialtySeq;
    }

    public void setXspecialtySeq(String xspecialtySeq) {
        this.xspecialtySeq = xspecialtySeq;
    }

    public String getxOrganization() {
        return xOrganization;
    }

    public void setxOrganization(String xOrganization) {
        this.xOrganization = xOrganization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDlvr_status_code() {
        return dlvr_status_code;
    }

    public void setDlvr_status_code(String dlvr_status_code) {
        this.dlvr_status_code = dlvr_status_code;
    }

    public String getDlvr_status() {
        return dlvr_status;
    }

    public void setDlvr_status(String dlvr_status) {
        this.dlvr_status = dlvr_status;
    }

    public Long getDlvr_version() {
        return dlvr_version;
    }

    public void setDlvr_version(Long dlvr_version) {
        this.dlvr_version = dlvr_version;
    }
}
