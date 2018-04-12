package com.nerin.nims.opt.nbcc.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2016/7/1.
 */
public class TaskHeaderDto extends OracleBaseDTO {

    private Long taskHeaderId;                             //文本任务ID
    private Long templateHeaderId;                        //模板ID
    private String templateHeaderName;                  //模板名称
    private String taskNumber;   //文本任务编码
    private String taskName;    //文本任务名称
    private String taskStatus;  //文本任务状体编码
    private String taskStatusName; //文本任务状态名称
    private Long projectId; //项目ID
    private String projectNumber; //项目编码
    private String projectName;  //项目名称
    private String designPhase;  //文本任务设计阶段
    private String designPhaseName; //文本任务设计阶段名称
    private String comments;  //备注
    private String taskType;   //文本类型
    private String taskTypeName;   //文本类型名称
    private Long objectVersionNumber;   //版本号
    private String attributeCategory;   //备用分类
    private String attribute1;  //是否复制文本内容    0 不包含内容  1 包含内容
    private String attribute2; //备用属性2
    private String attribute3; //备用属性3
    private String attribute4; //备用属性4
    private String attribute5; //备用属性5
    private String attribute6; //备用属性6
    private String attribute7; //备用属性7
    private String attribute8; //备用属性8
    private String attribute9; //备用属性9
    private Long attribute10;  //是否共享
    private String attribute11; //来源地：TEMPLATE 文本模板  TASK  文本任务
    private String attribute12; //来源编号
    private String attribute13; //备用属性13
    private String attribute14; //备用属性14
    private String attribute15; //备用属性15
    private String createByName; //创建人姓名
    private Long versionNumber; //版本号
    private String taskProgress;  //章节完成情况
    private String taskProgress2; //专业检审完成情况
    private String projectRoleName; //我的角色
    private String projectManagerName;  //项目经理
    private String tag; //文本类型对应的A\B\C类

    public Long getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Long versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getDesignPhase() {
        return designPhase;
    }

    public void setDesignPhase(String designPhase) {
        this.designPhase = designPhase;
    }

    public Long getTaskHeaderId() {
        return taskHeaderId;
    }

    public void setTaskHeaderId(Long taskHeaderId) {
        this.taskHeaderId = taskHeaderId;
    }

    public Long getTemplateHeaderId() {
        return templateHeaderId;
    }

    public void setTemplateHeaderId(Long templateHeaderId) {
        this.templateHeaderId = templateHeaderId;
    }

    public String getTemplateHeaderName() {
        return templateHeaderName;
    }

    public void setTemplateHeaderName(String templateHeaderName) {
        this.templateHeaderName = templateHeaderName;
    }

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getTaskStatusName() {
        return taskStatusName;
    }

    public void setTaskStatusName(String taskStatusName) {
        this.taskStatusName = taskStatusName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDesignPhaseName() {
        return designPhaseName;
    }

    public void setDesignPhaseName(String designPhaseName) {
        this.designPhaseName = designPhaseName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getTaskProgress() {
        return taskProgress;
    }

    public void setTaskProgress(String taskProgress) {
        this.taskProgress = taskProgress;
    }

    public String getTaskProgress2() {
        return taskProgress2;
    }

    public void setTaskProgress2(String taskProgress2) {
        this.taskProgress2 = taskProgress2;
    }

    public String getProjectRoleName() {
        return projectRoleName;
    }

    public void setProjectRoleName(String projectRoleName) {
        this.projectRoleName = projectRoleName;
    }

    public String getProjectManagerName() {
        return projectManagerName;
    }

    public void setProjectManagerName(String projectManagerName) {
        this.projectManagerName = projectManagerName;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
