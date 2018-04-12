package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/14.
 */
public class PaElementsDTO {
    private long projElementId;
    private long id;//elementVersionId;
   // private long outlineLevel;//refTaskVersionId 与被引用的节点之间的层级关系 比如 0代表了 与 refTaskVersionId节点同级，1代表了子节点
    private String code; //taskNumber;
    private String name;//taskName;
    private Date startDate;//scheduledStartDate;
    private Date endDate;//scheduledFinishDate;
    private String dualName; //bilingual;    //双语
    private double workCoef;//workLoad;    //工作量
    private String status;//statusCode;    //节点状态
    private String srcStru;//sourceClass;   //复制来源
    private Long srcID;//sourceStructureVerId; //复制来源版本VERID
    private Long srcRow;//sourceElementId; //复制来源elementID
    private Long parentId; //上层节点
    private String level;


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public long getProjElementId() {
        return projElementId;
    }

    public void setProjElementId(long projElementId) {
        this.projElementId = projElementId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    /*public long getOutlineLevel() {
        return outlineLevel;
    }

    public void setOutlineLevel(long outlineLevel) {
        this.outlineLevel = outlineLevel;
    }*/

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDualName() {
        return dualName;
    }

    public void setDualName(String dualName) {
        this.dualName = dualName;
    }

    public double getWorkCoef() {
        return workCoef;
    }

    public void setWorkCoef(double workCoef) {
        this.workCoef = workCoef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSrcStru() {
        return srcStru;
    }

    public void setSrcStru(String srcStru) {
        this.srcStru = srcStru;
    }

    public Long getSrcID() {
        return srcID;
    }

    public void setSrcID(Long srcID) {
        this.srcID = srcID;
    }

    public Long getSrcRow() {
        return srcRow;
    }

    public void setSrcRow(Long srcRow) {
        this.srcRow = srcRow;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
