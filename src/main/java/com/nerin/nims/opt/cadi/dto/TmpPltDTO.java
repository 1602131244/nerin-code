package com.nerin.nims.opt.cadi.dto;
import com.nerin.nims.opt.base.rest.OracleBaseDTO;

import java.util.Date;
/**
 * Created by user on 16/8/2.
 */
public class TmpPltDTO  extends OracleBaseDTO {
    private Long did;        // 图纸唯一内部系统id
    private String drawNum; //图号
    private String reviseNum;//修正号
    private String drawName;//图名
    private String xSize;//图幅
    private Long    projectId; //项目ID
    private String xProjectNum;//项目编号
    private String projectSName; //项目简称
    private String xEquipmentNum;//设备编号
    private String xEquipment; //设备名称
    private String xDesignPhase;//项目阶段
    private String xDesignPhaseName;//项目阶段
    private String xDivisionNum;//子项号
    private String xDivision;//子项
    private String xSpecialty;//专业代码
    private String xSpecialtyName;//专业代码
    private Long xDlvrId;//工作包id
    private String xDlvrName;//工作包ID
    private String xEmFlag;//紧急放行标志
    private String xDesignDate;//设计日期
    private String xWpApprStatus;//审批状态
    private String xCountersignStatus;//对图状态
    private String xPltStatus;//出图状态
    private Date dReleaseDate;//发布日期
    private String dDocType; //文档类型
    //set who 5 //从基类继承
    /*
    private Date creationDate;
    private Date lastUpdateDate;
    private Long createdBy; //创建人ID
    private Long lastupdatedBy;
    private Long lastUpdateLogin;*/
    //attribute 16
    private String attributeCategory;
    private String attribute1;
    private String attribute2;
    private String attribute3;
    private String attribute4;
    private String attribute5;
    private String attribute6;
    private String attribute7;
    private String attribute8;
    private String attribute9;
    private String attribute10;
    private String attribute11;
    private String attribute12;
    private String attribute13;
    private String attribute14;
    private String attribute15;

    private String createdByName;
    private String createdTime;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getxDesignPhaseName() {
        return xDesignPhaseName;
    }

    public void setxDesignPhaseName(String xDesignPhaseName) {
        this.xDesignPhaseName = xDesignPhaseName;
    }

    public String getxSpecialtyName() {
        return xSpecialtyName;
    }

    public void setxSpecialtyName(String xSpecialtyName) {
        this.xSpecialtyName = xSpecialtyName;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
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

    public String getxDesignPhase() {
        return xDesignPhase;
    }

    public void setxDesignPhase(String xDesignPhase) {
        this.xDesignPhase = xDesignPhase;
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

    public Date getdReleaseDate() {
        return dReleaseDate;
    }

    public void setdReleaseDate(Date dReleaseDate) {
        this.dReleaseDate = dReleaseDate;
    }

    public String getdDocType() {
        return dDocType;
    }

    public void setdDocType(String dDocType) {
        this.dDocType = dDocType;
    }

    public String getAttributeCategory() {
        return attributeCategory;
    }

    public void setAttributeCategory(String attributeCategory) {
        this.attributeCategory = attributeCategory;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    public String getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(String attribute9) {
        this.attribute9 = attribute9;
    }

    public String getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(String attribute10) {
        this.attribute10 = attribute10;
    }

    public String getAttribute11() {
        return attribute11;
    }

    public void setAttribute11(String attribute11) {
        this.attribute11 = attribute11;
    }

    public String getAttribute12() {
        return attribute12;
    }

    public void setAttribute12(String attribute12) {
        this.attribute12 = attribute12;
    }

    public String getAttribute13() {
        return attribute13;
    }

    public void setAttribute13(String attribute13) {
        this.attribute13 = attribute13;
    }

    public String getAttribute14() {
        return attribute14;
    }

    public void setAttribute14(String attribute14) {
        this.attribute14 = attribute14;
    }

    public String getAttribute15() {
        return attribute15;
    }

    public void setAttribute15(String attribute15) {
        this.attribute15 = attribute15;
    }
}
