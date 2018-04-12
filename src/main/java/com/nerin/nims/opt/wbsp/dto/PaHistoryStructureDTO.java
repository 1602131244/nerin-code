package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class PaHistoryStructureDTO {


    //private long sessionId;//会话ID
    private long id;//行ID
    private String name;
    private Date time;
    private String operator; //操作人
    private long projectId;
    private long structureId;
    private String strClass;  //PBS 还是WBS
    private String phasecode;
    private long parentId;   //父ID
    private String strClassCode; //层级路径
    private List<PaPhaseListDTO> phaseList;

    public String getPhasecode() {
        return phasecode;
    }

    public void setPhasecode(String phasecode) {
        this.phasecode = phasecode;
    }

    public List<PaPhaseListDTO> getPhaseList() {
        return phaseList;
    }

    public void setPhaseList(List<PaPhaseListDTO> phaseList) {
        this.phaseList = phaseList;
    }

   /* public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }*/

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getStructureId() {
        return structureId;
    }

    public void setStructureId(long structureId) {
        this.structureId = structureId;
    }

    public String getStrClass() {
        return strClass;
    }

    public void setStrClass(String strClass) {
        this.strClass = strClass;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public String getStrClassCode() {
        return strClassCode;
    }

    public void setStrClassCode(String strClassCode) {
        this.strClassCode = strClassCode;
    }
}
