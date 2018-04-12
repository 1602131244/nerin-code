package com.nerin.nims.opt.nbcc.dto;


import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by yinglgu on 6/17/2016.
 */
public class TemplateHeaderDTO extends OracleBaseDTO {

    private Long templateId;
    /**
     * 模板名称
     */
    private String templateName; //模板名称
    private String templateDescription; // 模板说明
    private String templateStatus; // 模板状态编码
    private String templateStatusName;//模板状态名称
    private String taskType;//文本类型编码
    private String taskTypeName;//文本类型名称
    private String comments;//备注（废弃）
    private Long objectVersionNumber;//版本号（默认值=1）
    private String attributeCategory;//备用分类
    private String attribute1;//备用属性1
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
    private String attribute15;//备用属性15（OA审批流程ID）

    private Long fromTemplateId;

    public Long getFromTemplateId() {
        return fromTemplateId;
    }

    public void setFromTemplateId(Long fromTemplateId) {
        this.fromTemplateId = fromTemplateId;
    }

    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateDescription() {
        return templateDescription;
    }

    public void setTemplateDescription(String templateDescription) {
        this.templateDescription = templateDescription;
    }

    public String getTemplateStatus() {
        return templateStatus;
    }

    public void setTemplateStatus(String templateStatus) {
        this.templateStatus = templateStatus;
    }

    public String getTemplateStatusName() {
        return templateStatusName;
    }

    public void setTemplateStatusName(String templateStatusName) {
        this.templateStatusName = templateStatusName;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
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

}
