package com.nerin.nims.opt.wh.dto;


/**
 * Created by Administrator on 2016/8/3. *
 */
public class DlvrDTO {

    private long id;    //	任务工时记录主键id
    private long taskId;    //	任务主键id
    private long code;    //	工作包号
    private String taskName;    //	任务名称（工作包名称）
    private String projName;    //	项目名称
    private long projID;    //	项目主键id
    private String phaseID;    //	项目阶段id
    private long updateWorkHour;    //	当日当前任务填报工时
    private String progress;    //	当日当前任务填报进度
    private String description;    //	工作包说明字段
    private String phaseName;    //	项目阶段名称
    private String division;    //	子项号+子项名称
    private String projNum;    //	项目编号
    private String system;    //	系统号+系统名称
    private String majorCode;    //	专业代号
    private String specialityName;    //	专业名称
    private long specId;    //	所属专业节点id
    private float accuWorkHour;    //	累计已填报工时
    private String workHour;    //	计划工时
    private String startDate;    //	计划开始日期
    private String endDate;    //	计划完成日期
    private String designName;    //	设计人名
    private long designPerson;    //	设计人perId
    private String checkName;    //	校核人名
    private String reviewName;    //	审核人名
    private String status;    //	工作包状态
    private String authorName;    //	创建人名
    private String approveName;    //	审定人名
    private String certifiedName;    //	注册工程师名
    private String schemeName;    //	方案设计人名
    private String otherName;    //	其他设计人名串
    private String grandNum;    //	专业孙项号
    private String grandName;    //	专业孙项名
    private String workgrandNum;    //	工作包孙项号
    private String matCode;    //	关联物料编码
    private String worktypeID;    //	工作包类型Id
    private String recvSpec;    //	接口条件接收专业代号
    private String matName;    //	关联物料名称

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public long getProjID() {
        return projID;
    }

    public void setProjID(long projID) {
        this.projID = projID;
    }

    public String getPhaseID() {
        return phaseID;
    }

    public void setPhaseID(String phaseID) {
        this.phaseID = phaseID;
    }

    public long getUpdateWorkHour() {
        return updateWorkHour;
    }

    public void setUpdateWorkHour(long updateWorkHour) {
        this.updateWorkHour = updateWorkHour;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getProjNum() {
        return projNum;
    }

    public void setProjNum(String projNum) {
        this.projNum = projNum;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getSpecialityName() {
        return specialityName;
    }

    public void setSpecialityName(String specialityName) {
        this.specialityName = specialityName;
    }

    public long getSpecId() {
        return specId;
    }

    public void setSpecId(long specId) {
        this.specId = specId;
    }

    public float getAccuWorkHour() {
        return accuWorkHour;
    }

    public void setAccuWorkHour(float accuWorkHour) {
        this.accuWorkHour = accuWorkHour;
    }

    public String getWorkHour() {
        return workHour;
    }

    public void setWorkHour(String workHour) {
        this.workHour = workHour;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDesignName() {
        return designName;
    }

    public void setDesignName(String designName) {
        this.designName = designName;
    }

    public long getDesignPerson() {
        return designPerson;
    }

    public void setDesignPerson(long designPerson) {
        this.designPerson = designPerson;
    }

    public String getCheckName() {
        return checkName;
    }

    public void setCheckName(String checkName) {
        this.checkName = checkName;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getApproveName() {
        return approveName;
    }

    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }

    public String getCertifiedName() {
        return certifiedName;
    }

    public void setCertifiedName(String certifiedName) {
        this.certifiedName = certifiedName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public String getGrandNum() {
        return grandNum;
    }

    public void setGrandNum(String grandNum) {
        this.grandNum = grandNum;
    }

    public String getGrandName() {
        return grandName;
    }

    public void setGrandName(String grandName) {
        this.grandName = grandName;
    }

    public String getWorkgrandNum() {
        return workgrandNum;
    }

    public void setWorkgrandNum(String workgrandNum) {
        this.workgrandNum = workgrandNum;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public String getWorktypeID() {
        return worktypeID;
    }

    public void setWorktypeID(String worktypeID) {
        this.worktypeID = worktypeID;
    }

    public String getRecvSpec() {
        return recvSpec;
    }

    public void setRecvSpec(String recvSpec) {
        this.recvSpec = recvSpec;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }
}
