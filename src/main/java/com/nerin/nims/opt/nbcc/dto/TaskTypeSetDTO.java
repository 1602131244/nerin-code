package com.nerin.nims.opt.nbcc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nerin.nims.opt.base.rest.OracleBaseDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yinglgu on 6/24/2016.
 */
public class TaskTypeSetDTO extends OracleBaseDTO {
    private String lookupCode;//任务类型编码
    private String meaning;//任务类型名称
    private String description;//OA流程编号
    private String enabledFlag = "1";//是否启用
    private Date startDateActive;//开始日期
    private Date endDateActive;//结束日期
    private String tag; // A、B、C类
    private String attributeCategory;//上下文值
    private String attribute1;//对应项目阶段
    private String attribute1Name;
    private String attribute2;//对应项目阶段
    private String attribute2Name;
    private String attribute3;//对应项目阶段
    private String attribute3Name;
    private String attribute4;//对应项目阶段
    private String attribute4Name;
    private String attribute5;//对应项目阶段
    private String attribute5Name;
    private String attribute6;//对应项目阶段
    private String attribute6Name;
    private String attribute7;//对应项目阶段
    private String attribute7Name;
    private String attribute8;//协作模板是否必备专业和工作包信息
    private Long attribute9;//任务类型模板
    private String attribute9Name;//任务类型模板
    private String attribute10;
    private String attribute11;
    private String attribute12;
    private String attribute13;
    private String attribute14;
    private String attribute15;

    private List<TaskTypeAssignmentsDTO> assignmentsDTOList = new ArrayList<TaskTypeAssignmentsDTO>();
    private List<TaskTypeElementsDTO> elementsDTOList = new ArrayList<TaskTypeElementsDTO>();

    private String sel_proRole;
    private String sel_specialty;

    private String sel_proRole2;
    private String sel_specialty2;

    private String delRoles;
    private String delElements;

    public String getDelElements() {
        return delElements;
    }

    public void setDelElements(String delElements) {
        this.delElements = delElements;
    }

    public List<TaskTypeElementsDTO> getElementsDTOList() {
        return elementsDTOList;
    }

    public void setElementsDTOList(List<TaskTypeElementsDTO> elementsDTOList) {
        this.elementsDTOList = elementsDTOList;
    }

    public List<TaskTypeAssignmentsDTO> getAssignmentsDTOList() {
        return assignmentsDTOList;
    }

    public String getDelRoles() {
        return delRoles;
    }

    public void setDelRoles(String delRoles) {
        this.delRoles = delRoles;
    }

    public void setAssignmentsDTOList(List<TaskTypeAssignmentsDTO> assignmentsDTOList) {
        this.assignmentsDTOList = assignmentsDTOList;
    }

    public String getSel_proRole() {
        return sel_proRole;
    }

    public void setSel_proRole(String sel_proRole) {
        this.sel_proRole = sel_proRole;
    }

    public String getSel_specialty() {
        return sel_specialty;
    }

    public void setSel_specialty(String sel_specialty) {
        this.sel_specialty = sel_specialty;
    }

    public String getSel_proRole2() {
        return sel_proRole2;
    }

    public void setSel_proRole2(String sel_proRole2) {
        this.sel_proRole2 = sel_proRole2;
    }

    public String getSel_specialty2() {
        return sel_specialty2;
    }

    public void setSel_specialty2(String sel_specialty2) {
        this.sel_specialty2 = sel_specialty2;
    }

    public String getLookupCode() {
        return lookupCode;
    }

    public void setLookupCode(String lookupCode) {
        this.lookupCode = lookupCode;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEnabledFlag() {
        return enabledFlag;
    }

    public void setEnabledFlag(String enabledFlag) {
        this.enabledFlag = enabledFlag;
    }

    //@JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    public Date getStartDateActive() {
        return startDateActive;
    }

    public void setStartDateActive(Date startDateActive) {
        this.startDateActive = startDateActive;
    }

    //@JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    public Date getEndDateActive() {
        return endDateActive;
    }

    public void setEndDateActive(Date endDateActive) {
        this.endDateActive = endDateActive;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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

    public String getAttribute1Name() {
        return attribute1Name;
    }

    public void setAttribute1Name(String attribute1Name) {
        this.attribute1Name = attribute1Name;
    }

    public String getAttribute2() {
        return attribute2;
    }

    public void setAttribute2(String attribute2) {
        this.attribute2 = attribute2;
    }

    public String getAttribute2Name() {
        return attribute2Name;
    }

    public void setAttribute2Name(String attribute2Name) {
        this.attribute2Name = attribute2Name;
    }

    public String getAttribute3() {
        return attribute3;
    }

    public void setAttribute3(String attribute3) {
        this.attribute3 = attribute3;
    }

    public String getAttribute3Name() {
        return attribute3Name;
    }

    public void setAttribute3Name(String attribute3Name) {
        this.attribute3Name = attribute3Name;
    }

    public String getAttribute4() {
        return attribute4;
    }

    public void setAttribute4(String attribute4) {
        this.attribute4 = attribute4;
    }

    public String getAttribute4Name() {
        return attribute4Name;
    }

    public void setAttribute4Name(String attribute4Name) {
        this.attribute4Name = attribute4Name;
    }

    public String getAttribute5() {
        return attribute5;
    }

    public void setAttribute5(String attribute5) {
        this.attribute5 = attribute5;
    }

    public String getAttribute5Name() {
        return attribute5Name;
    }

    public void setAttribute5Name(String attribute5Name) {
        this.attribute5Name = attribute5Name;
    }

    public String getAttribute6() {
        return attribute6;
    }

    public void setAttribute6(String attribute6) {
        this.attribute6 = attribute6;
    }

    public String getAttribute6Name() {
        return attribute6Name;
    }

    public void setAttribute6Name(String attribute6Name) {
        this.attribute6Name = attribute6Name;
    }

    public String getAttribute7() {
        return attribute7;
    }

    public void setAttribute7(String attribute7) {
        this.attribute7 = attribute7;
    }

    public String getAttribute7Name() {
        return attribute7Name;
    }

    public void setAttribute7Name(String attribute7Name) {
        this.attribute7Name = attribute7Name;
    }

    public String getAttribute8() {
        return attribute8;
    }

    public void setAttribute8(String attribute8) {
        this.attribute8 = attribute8;
    }

    public Long getAttribute9() {
        return attribute9;
    }

    public void setAttribute9(Long attribute9) {
        this.attribute9 = attribute9;
    }

    public String getAttribute9Name() {
        return attribute9Name;
    }

    public void setAttribute9Name(String attribute9Name) {
        this.attribute9Name = attribute9Name;
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
