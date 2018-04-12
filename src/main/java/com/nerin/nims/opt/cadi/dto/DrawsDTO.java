package com.nerin.nims.opt.cadi.dto;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 16/7/15.
 */
public class DrawsDTO {

    private Long did;        //did 图纸唯一内部系统id
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
    private String xDivisionNum;//子项号
    private String xDivision;//子项
    private String xSpecialty;//专业代码
    private Long xDlvrId;//工作包id
    private String xDlvrName;//工作包ID
    private String xEmFlag;//紧急放行标志
    private String xDesignDate;//设计日期
    private String xWpApprStatus;//审批状态
    private String xCountersignStatus;//对图状态
    private String xPltStatus;//出图状态
    private Date dReleaseDate;//发布日期
    private String dDocType; //文档类型
    private String ObjectCode; //树形显示编号
    private String ObjectName; //树形显示名称
    //经过进一步查找资料发现fancy tree 识别其中的 node字段有要求，所以在添加两个字段 一个作为扩展node，另外一个作为文件夹标志
    private String title;
    private boolean folder;
    //辜迎龙前端要求添加两个字段
    private String specialityName; //专业名称
    private String phaseName;   //阶段名称
    private String dlvrName;   //工作包名称

    private String xspecialtySeq; // 专业孙项

    //加入所属组织字段
    private String xOrganization; //所属组织字段
    // 节点类型
    private String nodeType;
    // 节点状态
    private String nodeStatus;

    public String getXspecialtySeq() {
        return xspecialtySeq;
    }

    public void setXspecialtySeq(String xspecialtySeq) {
        this.xspecialtySeq = xspecialtySeq;
    }

    public String getNodeStatus() {
        return nodeStatus;
    }

    public void setNodeStatus(String nodeStatus) {
        this.nodeStatus = nodeStatus;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getDlvrName() {
        return dlvrName;
    }

    public void setDlvrName(String dlvrName) {
        this.dlvrName = dlvrName;
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

    public List<DrawsDTO> children; //子节点

    public String getObjectCode() {
        return ObjectCode;
    }

    public void setObjectCode(String objectCode) {
        ObjectCode = objectCode;
    }

    public String getObjectName() {
        return ObjectName;
    }

    public void setObjectName(String objectName) {
        ObjectName = objectName;
    }

    public List<DrawsDTO> getChildren() {
        return children;
    }

    public void setChildren(List<DrawsDTO> children) {
        this.children = children;
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

    public String getxProjectNum() {
        return xProjectNum;
    }

    public void setxProjectNum(String xProjectNum) {
        this.xProjectNum = xProjectNum;
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

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getxOrganization() {
        return xOrganization;
    }

    public void setxOrganization(String xOrganization) {
        this.xOrganization = xOrganization;
    }
}
