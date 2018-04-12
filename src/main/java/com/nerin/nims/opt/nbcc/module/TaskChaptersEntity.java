package com.nerin.nims.opt.nbcc.module;


import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Created by Administrator on 2016/7/11.
 */
public class TaskChaptersEntity extends OracleBaseEntity implements ORAData {

    private Long chapterId;     //章节ID
    private Long  parentChapterId;  //父章节ID
    private Long taskHeaderId;  //文本任务头ID
    private Long templateChapterId; //模板章节ID
    private Long templateParentChapterId;  //模板章节父ID
    private Long projElementId; //工作包ID
    private Long personIdResponsible;   //节点负责人ID
    private Long personIdProofread; //校审人ID
    private Long personIdAudit; //审核人ID
    private Long personIdApprove; //审定人ID
    private String specialty;  //专业编码
    private String chapterStatus;  //状体编码
    private Long enableFlag; //是否启用
    private String comments; //摘要
    private Long objectVersionNumber; //版本编号
    private String attributeCategory;   //备用分类
    private String attribute1;  //备用属性1
    private String attribute2; //备用属性2
    private String attribute3; //备用属性3
    private String attribute4; //备用属性4
    private String attribute5; //备用属性5
    private String attribute6; //备用属性6
    private String attribute7; //备用属性7
    private String attribute8; //备用属性8
    private String attribute9; //备用属性9
    private Long attribute10;  //是否公开
    private String attribute11; //备用属性11
    private String attribute12; //备用属性12
    private String attribute13; //备用属性13
    private String attribute14; //备用属性14
    private String attribute15; //备用属性15
    private Long projectRoleId; //项目角色ID
    private String chapterNo;   //章节编码
    private String chapterName; //章节名称
    private Long personIdDesign;    //设计人员ID
    private Long deleteFlag;    //模板章节是否可删除标示
    private double chapterNumber; //原NBCC系统使用现在作废，主要用于历史数据的文本名称命名

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
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

    public Long getPersonIdResponsible() {
        return personIdResponsible;
    }

    public void setPersonIdResponsible(Long personIdResponsible) {
        this.personIdResponsible = personIdResponsible;
    }

    public Long getPersonIdProofread() {
        return personIdProofread;
    }

    public void setPersonIdProofread(Long personIdProofread) {
        this.personIdProofread = personIdProofread;
    }

    public Long getPersonIdAudit() {
        return personIdAudit;
    }

    public void setPersonIdAudit(Long personIdAudit) {
        this.personIdAudit = personIdAudit;
    }

    public Long getPersonIdApprove() {
        return personIdApprove;
    }

    public void setPersonIdApprove(Long personIdApprove) {
        this.personIdApprove = personIdApprove;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getChapterStatus() {
        return chapterStatus;
    }

    public void setChapterStatus(String chapterStatus) {
        this.chapterStatus = chapterStatus;
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

    public String getChapterNo() {
        return chapterNo;
    }

    public void setChapterNo(String chapterNo) {
        this.chapterNo = chapterNo;
    }

    public Long getPersonIdDesign() {
        return personIdDesign;
    }

    public void setPersonIdDesign(Long personIdDesign) {
        this.personIdDesign = personIdDesign;
    }

    public Long getDeleteFlag() {
        return deleteFlag;
    }

    public double getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(double chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public void setDeleteFlag(Long deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
    public static final String _ORACLE_TYPE_NAME = "APPS.NBCC_TASK_CHAPTER_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = { OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.NUMBER , OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.VARCHAR , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER
            , OracleTypes.VARCHAR , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.VARCHAR
            };
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public TaskChaptersEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    };
    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getChapterId());
        _struct.setAttribute(1, this.getParentChapterId());
        _struct.setAttribute(2, this.getTaskHeaderId());
        _struct.setAttribute(3, this.getTemplateChapterId());
        _struct.setAttribute(4, this.getTemplateParentChapterId());
        _struct.setAttribute(5, this.getProjElementId());
        _struct.setAttribute(6, this.getPersonIdResponsible());
        _struct.setAttribute(7, this.getPersonIdProofread());
        _struct.setAttribute(8, this.getPersonIdAudit());
        _struct.setAttribute(9, this.getPersonIdApprove());
        _struct.setAttribute(10, this.getSpecialty());
        _struct.setAttribute(11, this.getChapterStatus());
        _struct.setAttribute(12, this.getEnableFlag());
        _struct.setAttribute(13, this.getComments());
        _struct.setAttribute(14, this.getObjectVersionNumber());
        _struct.setAttribute(15, this.getAttributeCategory());
        _struct.setAttribute(16, this.getAttribute1());
        _struct.setAttribute(17, this.getAttribute2());
        _struct.setAttribute(18, this.getAttribute3());
        _struct.setAttribute(19, this.getAttribute4());
        _struct.setAttribute(20, this.getAttribute5());
        _struct.setAttribute(21, this.getAttribute6());
        _struct.setAttribute(22, this.getAttribute7());
        _struct.setAttribute(23, this.getAttribute8());
        _struct.setAttribute(24, this.getAttribute9());
        _struct.setAttribute(25, this.getAttribute10());
        _struct.setAttribute(26, this.getAttribute11());
        _struct.setAttribute(27, this.getAttribute12());
        _struct.setAttribute(28, this.getAttribute13());
        _struct.setAttribute(29, this.getAttribute14());
        _struct.setAttribute(30, this.getAttribute15());
        _struct.setAttribute(31, this.getLastUpdateDate());
        _struct.setAttribute(32, this.getLastUpdatedBy());
        _struct.setAttribute(33, this.getLastUpdateLogin());
        _struct.setAttribute(34, this.getCreatedBy());
        _struct.setAttribute(35, this.getCreationDate());
        _struct.setAttribute(36, this.getProjectRoleId());
        _struct.setAttribute(37, this.getChapterName());
        _struct.setAttribute(38, this.getChapterNumber());
        _struct.setAttribute(39, this.getPersonIdDesign());
        _struct.setAttribute(40, this.getChapterNo());

        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
