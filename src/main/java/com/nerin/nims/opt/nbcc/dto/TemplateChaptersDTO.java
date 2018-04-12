package com.nerin.nims.opt.nbcc.dto;


import com.nerin.nims.opt.base.rest.OracleBaseDTO;

import java.util.List;

/**
 * Created by yinglgu on 6/21/2016.
 */
public class TemplateChaptersDTO extends OracleBaseDTO {
    private String title;
    private boolean folder = false;
    private String tooltip;

    private String delTemplateChapterIds;

    private Long chapterId;//模板章节id
    private Long parentChapterId;//模板父章节id
    private String parentChapterNo;//模板父章节编号
    private Long templateHeaderId;//模板头id
    private String chapterNo;//章节号（启用）
    private String chapterName;//章节名称
    private Long projectRoleId;//项目角色编号
    private String projectRoleName;//项目角色名称
    private Long xdoTemplateId;//xdo模板id
    private String xdoTemplateName;//xdo模板名称
    private String systemLink;//协作章节系统界面链接编码
    private String systemLinkName;//协作章节系统界面链接名称
    private String systemLinkUrl;//协作章节系统界面链接
    private String specialty;//专业编码
    private String specialtyName;//专业名称
    private Long publicFlag;//是否公开
    private Long deleteFlag;//是否删除
    private String mandatoryFlag;//是否强制要求任务章节必须为章节已确认（废弃）
    private String oaApproveFlow;//oa审批流程配置
    private String comments;//备注
    private Long objectVersionNumber;//版本号（默认=1）
    private String attributeCategory;//备用分类
    private String attribute1;//行类型：COVER   封面、CHAPTER  章节
    private String attribute2;//备用属性2
    private String attribute3;//备用属性3
    private String attribute4;//备用属性4
    private String attribute5;//备用属性5
    private String attribute6;//备用属性6
    private String attribute7;//备用属性7
    private String attribute8;//备用属性8
    private String attribute9;//备用属性9
    private String attribute10;//备用属性10
    private String attribute11;//备用属性11
    private String attribute12;//备用属性12
    private String attribute13;//备用属性13
    private String attribute14;//备用属性14
    private String attribute15;//备用属性15
//    private Long isLeafNode; //是否叶子节点 1 是  0 不是

    private String templateCode; //XML模板编码
    private String templateApplShortCode; // XML模板对应APPL短码
    private String templateChapterFileName; //
    private String NLSLanguage;
    private String NLSTerritory;


    private List<TemplateChaptersDTO> children;

    public String getDelTemplateChapterIds() {
        return delTemplateChapterIds;
    }

    public void setDelTemplateChapterIds(String delTemplateChapterIds) {
        this.delTemplateChapterIds = delTemplateChapterIds;
    }

    public String getXdoTemplateName() {
        return xdoTemplateName;
    }

    public void setXdoTemplateName(String xdoTemplateName) {
        this.xdoTemplateName = xdoTemplateName;
    }

    public String getSystemLinkUrl() {
        return systemLinkUrl;
    }

    public void setSystemLinkUrl(String systemLinkUrl) {
        this.systemLinkUrl = systemLinkUrl;
    }

    public String getSystemLinkName() {
        return systemLinkName;
    }

    public void setSystemLinkName(String systemLinkName) {
        this.systemLinkName = systemLinkName;
    }

    public Long getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(Long parentChapterId) {
        this.parentChapterId = parentChapterId;
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

    public Long getChapterId() {
        return chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public String getParentChapterNo() {
        return parentChapterNo;
    }

    public void setParentChapterNo(String parentChapterNo) {
        this.parentChapterNo = parentChapterNo;
    }

    public Long getTemplateHeaderId() {
        return templateHeaderId;
    }

    public void setTemplateHeaderId(Long templateHeaderId) {
        this.templateHeaderId = templateHeaderId;
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

    public Long getXdoTemplateId() {
        return xdoTemplateId;
    }

    public void setXdoTemplateId(Long xdoTemplateId) {
        this.xdoTemplateId = xdoTemplateId;
    }

    public String getSystemLink() {
        return systemLink;
    }

    public void setSystemLink(String systemLink) {
        this.systemLink = systemLink;
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

    public Long getPublicFlag() {
        return publicFlag;
    }

    public void setPublicFlag(Long publicFlag) {
        this.publicFlag = publicFlag;
    }

    public Long getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getMandatoryFlag() {
        return mandatoryFlag;
    }

    public void setMandatoryFlag(String mandatoryFlag) {
        this.mandatoryFlag = mandatoryFlag;
    }

    public String getOaApproveFlow() {
        return oaApproveFlow;
    }

    public void setOaApproveFlow(String oaApproveFlow) {
        this.oaApproveFlow = oaApproveFlow;
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

    public List<TemplateChaptersDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TemplateChaptersDTO> children) {
        this.children = children;
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

    public String getTemplateChapterFileName() {
        return templateChapterFileName;
    }

    public void setTemplateChapterFileName(String templateChapterFileName) {
        this.templateChapterFileName = templateChapterFileName;
    }

    public String getNLSLanguage() {
        return NLSLanguage;
    }

    public void setNLSLanguage(String NLSLanguage) {
        this.NLSLanguage = NLSLanguage;
    }

    public String getNLSTerritory() {
        return NLSTerritory;
    }

    public void setNLSTerritory(String NLSTerritory) {
        this.NLSTerritory = NLSTerritory;
    }
}
