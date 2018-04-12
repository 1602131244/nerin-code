package com.nerin.nims.opt.nbcc.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

import java.util.List;

/**
 * Created by Administrator on 2016/7/7.
 */
public class TaskChaptersDto extends OracleBaseDTO {

    private Long chapterId;     //章节ID
    private Long id;     //章节ID 为方便了EASYUI使用 等同于 chapterId
    private Long  parentChapterId;  //父章节ID
    private Long taskHeaderId;  //文本任务头ID
    private Long templateChapterId; //模板章节ID
    private Long templateParentChapterId;  //模板章节父ID
    private Long projElementId; //工作包ID
    private String projElementNumber; //工作包编号
    private String projElementName; //工作包名称
    private Long personIdResponsible;   //节点负责人ID
    private String personNameResponsible; //节点负责人名称
    private Long personIdProofread; //校审人ID
    private String personNameProofread; //校审人名称
    private Long personIdAudit; //审核人ID
    private String personNameAudit; //审核人名称
    private Long personIdApprove; //审定人ID
    private String personNameApprove; //审定人名称
    private String specialty;  //专业编码
    private String specialtyName; //专业名称
    private String chapterStatus;  //状体编码
    private String chapterStatusName; //状态名称
    private Long enableFlag; //是否启用
    private String comments; //摘要
    private Long objectVersionNumber; //版本编号
    private String attributeCategory;   //备用分类k
    private String attribute1;  //行类型：COVER   封面、CHAPTER  章节
    private String attribute2; //存放提交审批、院审结论表时提交前的状态
    private String attribute3; //备用属性3
    private String attribute4; //备用属性4
    private String attribute5; //备用属性5
    private String attribute6; //备用属性6
    private String attribute7; //排序编号
    private String attribute8; //来源历史文本行ID
    private String attribute9; //来源历史文本行父ID
    private Long attribute10;  //是否公开
    private String attribute11; //授权人ID
    private String attribute11Name; //授权人名称
    private String attribute12; //检出人ID
    private String attribute12Name; //检出人名称
    private String attribute13; //备用属性13(batch_id, OA审批)
    private String attribute14; //备用属性14(oa_flow_id)
    private String attribute15; //备用属性15(chapter_status 备份状态)
    private Long projectRoleId; //项目角色ID
    private String projectRoleName; //项目角色名称
    private String chapterNo;   //章节编码
    private String chapterName; //章节名称
    private Long personIdDesign;    //设计人员ID
    private String personNameDesign; //设计人员名称
    private Long deleteFlag;    // 模板章节是否可删除标示
    private Long editFlag;// 人员是否有修改权限标示  1、标示可修改 0表示无权修改
    private String wordFlag;// E 有编辑权限 S 查看权限 N 无权限
    private String xmlCode; //有值表示该章节存在XML模板
    private String templateCode; //XML模板编码
    private String templateApplShortCode; // XML模板对应APPL短码
    private String taskChapterFileName; // 章节文本名称
    private String systempLineName; //
    private String systempLineUrl; //
    private String iconCls;  //逻辑是：一级章节：icon-nbcc_01    二级章节: icon-nbcc_02    三级章节：icon-nbcc_03 依此类推
    private double chapterNumber; //原NBCC系统使用现在作废，主要用于历史数据的文本名称命名
    private Long chapterFileFlag; //word文件是否存在  1表示存在  0表示不存在
    private String sourceChapterFileName; //来源文本章节或模板章节的文本名称
    private Long isLeafNode; //是否叶子节点 1 是  0 不是

    private int isExistsWordFile; // word文件是否存在,0存在，1不存在  （作废）

    private String delTaskChapterIds;

    private List<TaskChaptersDto> children;
    private String title;
    private boolean folder = false;
    private String tooltip;

    public int getIsExistsWordFile() {
        return isExistsWordFile;
    }

    public void setIsExistsWordFile(int isExistsWordFile) {
        this.isExistsWordFile = isExistsWordFile;
    }

    public List<TaskChaptersDto> getChildren() {
        return children;
    }

    public void setChildren(List<TaskChaptersDto> children) {
        this.children = children;
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

    public Long getPersonIdProofread() {
        return personIdProofread;
    }

    public void setPersonIdProofread(Long personIdProofread) {
        this.personIdProofread = personIdProofread;
    }

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Long getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(Long parentChapterId) {
        this.parentChapterId = parentChapterId;
    }

    public Long getTaskHeaderId() {
        return taskHeaderId;
    }

    public void setTaskHeaderId(Long taskHeaderId) {
        this.taskHeaderId = taskHeaderId;
    }

    public Long getTemplateChapterId() {
        return templateChapterId;
    }

    public void setTemplateChapterId(Long templateChapterId) {
        this.templateChapterId = templateChapterId;
    }

    public Long getTemplateParentChapterId() {
        return templateParentChapterId;
    }

    public void setTemplateParentChapterId(Long templateParentChapterId) {
        this.templateParentChapterId = templateParentChapterId;
    }

    public Long getProjElementId() {
        return projElementId;
    }

    public void setProjElementId(Long projElementId) {
        this.projElementId = projElementId;
    }

    public String getProjElementNumber() {
        return projElementNumber;
    }

    public void setProjElementNumber(String projElementNumber) {
        this.projElementNumber = projElementNumber;
    }

    public String getProjElementName() {
        return projElementName;
    }

    public void setProjElementName(String projElementName) {
        this.projElementName = projElementName;
    }

    public Long getPersonIdResponsible() {
        return personIdResponsible;
    }

    public void setPersonIdResponsible(Long personIdResponsible) {
        this.personIdResponsible = personIdResponsible;
    }

    public String getPersonNameResponsible() {
        return personNameResponsible;
    }

    public void setPersonNameResponsible(String personNameResponsible) {
        this.personNameResponsible = personNameResponsible;
    }

    public String getPersonNameProofread() {
        return personNameProofread;
    }

    public void setPersonNameProofread(String personNameProofread) {
        this.personNameProofread = personNameProofread;
    }

    public Long getPersonIdAudit() {
        return personIdAudit;
    }

    public void setPersonIdAudit(Long personIdAudit) {
        this.personIdAudit = personIdAudit;
    }

    public String getPersonNameAudit() {
        return personNameAudit;
    }

    public void setPersonNameAudit(String personNameAudit) {
        this.personNameAudit = personNameAudit;
    }

    public Long getPersonIdApprove() {
        return personIdApprove;
    }

    public void setPersonIdApprove(Long personIdApprove) {
        this.personIdApprove = personIdApprove;
    }

    public String getPersonNameApprove() {
        return personNameApprove;
    }

    public void setPersonNameApprove(String personNameApprove) {
        this.personNameApprove = personNameApprove;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }

    public String getChapterStatus() {
        return chapterStatus;
    }

    public void setChapterStatus(String chapterStatus) {
        this.chapterStatus = chapterStatus;
    }

    public String getChapterStatusName() {
        return chapterStatusName;
    }

    public void setChapterStatusName(String chapterStatusName) {
        this.chapterStatusName = chapterStatusName;
    }

    public Long getEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(Long enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
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

    public Long getAttribute10() {
        return attribute10;
    }

    public void setAttribute10(Long attribute10) {
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

    public Long getProjectRoleId() {
        return projectRoleId;
    }

    public void setProjectRoleId(Long projectRoleId) {
        this.projectRoleId = projectRoleId;
    }

    public String getProjectRoleName() {
        return projectRoleName;
    }

    public void setProjectRoleName(String projectRoleName) {
        this.projectRoleName = projectRoleName;
    }

    public String getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(String chapterNo) {
        this.chapterNo = chapterNo;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public Long getPersonIdDesign() {
        return personIdDesign;
    }

    public void setPersonIdDesign(Long personIdDesign) {
        this.personIdDesign = personIdDesign;
    }

    public String getPersonNameDesign() {
        return personNameDesign;
    }

    public void setPersonNameDesign(String personNameDesign) {
        this.personNameDesign = personNameDesign;
    }

    public Long getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getAttribute11Name() {
        return attribute11Name;
    }

    public void setAttribute11Name(String attribute11Name) {
        this.attribute11Name = attribute11Name;
    }

    public String getAttribute12Name() {
        return attribute12Name;
    }

    public void setAttribute12Name(String attribute12Name) {
        this.attribute12Name = attribute12Name;
    }

    public Long getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(Long editFlag) {
        this.editFlag = editFlag;
    }

    public String getDelTaskChapterIds() {
        return delTaskChapterIds;
    }

    public void setDelTaskChapterIds(String delTaskChapterIds) {
        this.delTaskChapterIds = delTaskChapterIds;
    }

    public String getWordFlag() {
        return wordFlag;
    }

    public void setWordFlag(String wordFlag) {
        this.wordFlag = wordFlag;
    }

    public String getXmlCode() {
        return xmlCode;
    }

    public void setXmlCode(String xmlCode) {
        this.xmlCode = xmlCode;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTemplateApplShortCode() {
        return templateApplShortCode;
    }

    public void setTemplateApplShortCode(String templateApplShortCode) {
        this.templateApplShortCode = templateApplShortCode;
    }

    public String getTaskChapterFileName() {
        return taskChapterFileName;
    }

    public void setTaskChapterFileName(String taskChapterFileName) {
        this.taskChapterFileName = taskChapterFileName;
    }

    public String getSystempLineName() {
        return systempLineName;
    }

    public void setSystempLineName(String systempLineName) {
        this.systempLineName = systempLineName;
    }

    public String getSystempLineUrl() {
        return systempLineUrl;
    }

    public void setSystempLineUrl(String systempLineUrl) {
        this.systempLineUrl = systempLineUrl;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public double getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(double chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChapterFileFlag() {
        return chapterFileFlag;
    }

    public void setChapterFileFlag(Long chapterFileFlag) {
        this.chapterFileFlag = chapterFileFlag;
    }

    public String getSourceChapterFileName() {
        return sourceChapterFileName;
    }

    public void setSourceChapterFileName(String sourceChapterFileName) {
        this.sourceChapterFileName = sourceChapterFileName;
    }

    public Long getIsLeafNode() {
        return isLeafNode;
    }

    public void setIsLeafNode(Long isLeafNode) {
        this.isLeafNode = isLeafNode;
    }
}
