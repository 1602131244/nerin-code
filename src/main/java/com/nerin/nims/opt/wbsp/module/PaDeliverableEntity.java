package com.nerin.nims.opt.wbsp.module;


import com.nerin.nims.opt.wbsp.dto.PaOthers;
import com.nerin.nims.opt.wbsp.dto.PaOthersList;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 16/8/2.
 */
public class PaDeliverableEntity implements ORAData {

    private long specId;  //专业ELEMENTID
    private long parentId;//专业ELEMENTID的版本ID
    private long id; //工作包ID
    private String name;//工作包名称
    private String dlvrNumber;
    private long taskTypeId;//element type 工作包 系统 子项 专业
    private Date endDate;//计划完成日期
    private Date startDate;//计划起始日期
    private String designNum;//设计人员ID
    private String checkNum;//校核人员
    private String reviewNum;//审核人员
    private String approveNum;//审定人员
    private String certifiedNum;//注册
    private double workHour;//计划工时
    private String majorCode;//专业代字
    private String grandNum;//专业孙项号
    private String workgrandNum;//工作包孙项号
    private String workCode;//工作包类型
    private String description;//说明
    private String statusCode;//工作包状态
    private String grandName;//专业孙项名称
    private String matCode;//关联物料编码
    private String matName;//关联物料名称
    private String srcID;//引用版本ID
    private String srcRow;//引用行ID或verID
    private String srcClass;//引用类型 PBS还是WBS
    private String authorClass;//创建人角色
    private long dlvrExtId;//扩展表ID
    private String amountTrial;//送审额
    private String amountAppr; //核定额 Check_Amt
    private String schemeNum; //方案设计人ID Scheme_Designer scheme_id
    private String dwgNum;//图纸数量
    private String budget;//预估额
    private PaOthersList otherslist;
    private String otherslistStr;
    private Long userId;
    private Long projectId;
    private String status;//生效、失效
    private String operate;//INSERT,UPDATE,DELETE
    private String recvSpec;//接受专业

    public String getRecvSpec() {
        return recvSpec;
    }

    public void setRecvSpec(String recvSpec) {
        this.recvSpec = recvSpec;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getOtherslistStr() {
        if (otherslist == null) {
            {
                otherslistStr = null;
            }
        } else {
            PaOthersList paOthersList = otherslist;
            List<PaOthers> paOtherses = new ArrayList<PaOthers>();
            paOtherses = paOthersList.getPaOthers();
            String str =new String();
            if (null!=paOtherses && !paOtherses.isEmpty()) {
                for (int i = 0; i < paOtherses.size(); i++) {
                    if (str.length()==0) {
                        str = paOtherses.get(i).getOtherName() + ',' +
                                paOtherses.get(i).getOtherNum() + ',' +
                                paOtherses.get(i).getOtherRatio() + ';';
                    } else {
                        str = str + paOtherses.get(i).getOtherName() + ',' +
                                paOtherses.get(i).getOtherNum() + ',' +
                                paOtherses.get(i).getOtherRatio() + ';';
                    }
                }
            }
            otherslistStr =str;
        }

        return otherslistStr;
    }


    public long getSpecId() {
        return specId;
    }

    public void setSpecId(long specId) {
        this.specId = specId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

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

    public long getTaskTypeId() {
        return taskTypeId;
    }

    public void setTaskTypeId(long taskTypeId) {
        this.taskTypeId = taskTypeId;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getDesignNum() {
        return designNum;
    }

    public void setDesignNum(String designNum) {
        this.designNum = designNum;
    }

    public String getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }

    public String getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(String reviewNum) {
        this.reviewNum = reviewNum;
    }

    public String getApproveNum() {
        return approveNum;
    }

    public void setApproveNum(String approveNum) {
        this.approveNum = approveNum;
    }

    public String getCertifiedNum() {
        return certifiedNum;
    }

    public void setCertifiedNum(String certifiedNum) {
        this.certifiedNum = certifiedNum;
    }

    public double getWorkHour() {
        return workHour;
    }

    public void setWorkHour(double workHour) {
        this.workHour = workHour;
    }

    public String getMajorCode() {
        return majorCode;
    }

    public void setMajorCode(String majorCode) {
        this.majorCode = majorCode;
    }

    public String getGrandNum() {
        return grandNum;
    }

    public void setGrandNum(String grandNum) {
        this.grandNum = grandNum;
    }

    public String getWorkgrandNum() {
        return workgrandNum;
    }

    public void setWorkgrandNum(String workgrandNum) {
        this.workgrandNum = workgrandNum;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getGrandName() {
        return grandName;
    }

    public void setGrandName(String grandName) {
        this.grandName = grandName;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getSrcID() {
        return srcID;
    }

    public void setSrcID(String srcID) {
        this.srcID = srcID;
    }

    public String getSrcRow() {
        return srcRow;
    }

    public void setSrcRow(String srcRow) {
        this.srcRow = srcRow;
    }

    public String getSrcClass() {
        return srcClass;
    }

    public void setSrcClass(String srcClass) {
        this.srcClass = srcClass;
    }

    public String getAuthorClass() {
        return authorClass;
    }

    public void setAuthorClass(String authorClass) {
        this.authorClass = authorClass;
    }

    public long getDlvrExtId() {
        return dlvrExtId;
    }

    public void setDlvrExtId(long dlvrExtId) {
        this.dlvrExtId = dlvrExtId;
    }

    public String getAmountTrial() {
        return amountTrial;
    }

    public void setAmountTrial(String amountTrial) {
        this.amountTrial = amountTrial;
    }

    public String getAmountAppr() {
        return amountAppr;
    }

    public void setAmountAppr(String amountAppr) {
        this.amountAppr = amountAppr;
    }

    public String getSchemeNum() {
        return schemeNum;
    }

    public void setSchemeNum(String schemeNum) {
        this.schemeNum = schemeNum;
    }

    public String getDwgNum() {
        return dwgNum;
    }

    public void setDwgNum(String dwgNum) {
        this.dwgNum = dwgNum;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public PaOthersList getOtherslist() {
        return otherslist;
    }

    public void setOtherslist(PaOthersList otherslist) {
        this.otherslist = otherslist;
    }

    public void setOtherslistStr(String otherslistStr) {
        this.otherslistStr = otherslistStr;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getDlvrNumber() {
        return dlvrNumber;
    }

    public void setDlvrNumber(String dlvrNumber) {
        this.dlvrNumber = dlvrNumber;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public static final String _ORACLE_TYPE_NAME = "APPS.WBSP_PA_DLVR_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER
            , OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.DATE, OracleTypes.NUMBER
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.DATE
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.VARCHAR

    };
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public PaDeliverableEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getUserId());
        _struct.setAttribute(1, this.getId());
        _struct.setAttribute(2, this.getName());
        _struct.setAttribute(3, this.getDlvrNumber());
        _struct.setAttribute(4, this.getSpecId());
        _struct.setAttribute(5, this.getParentId());
        _struct.setAttribute(6, this.getProjectId());
        _struct.setAttribute(7, this.getStatusCode());
        _struct.setAttribute(8, this.getEndDate());
        _struct.setAttribute(9, this.getWorkHour());
        _struct.setAttribute(10, this.getDesignNum());
        _struct.setAttribute(11, this.getCheckNum());
        _struct.setAttribute(12, this.getReviewNum());
        _struct.setAttribute(13, this.getApproveNum());
        _struct.setAttribute(14, this.getStartDate());
        _struct.setAttribute(15, this.getMajorCode());
        _struct.setAttribute(16, this.getGrandNum());
        _struct.setAttribute(17, this.getWorkgrandNum());
        _struct.setAttribute(18, this.getWorkCode());
        _struct.setAttribute(19, this.getDescription());
        _struct.setAttribute(20, this.getDwgNum());
        _struct.setAttribute(21, this.getCertifiedNum());
        _struct.setAttribute(22, this.getAmountTrial());
        _struct.setAttribute(23, this.getAmountAppr());
        _struct.setAttribute(24, this.getSchemeNum());
        _struct.setAttribute(25, this.getOtherslistStr());
        _struct.setAttribute(26, this.getGrandName());
        _struct.setAttribute(27, this.getMatCode());
        _struct.setAttribute(28, this.getMatName());
        _struct.setAttribute(29, this.getSrcID());
        _struct.setAttribute(30, this.getSrcRow());
        _struct.setAttribute(31, this.getSrcClass());
        _struct.setAttribute(32, this.getAuthorClass());
        _struct.setAttribute(33, this.getStatus());
        _struct.setAttribute(34, this.getBudget());
        _struct.setAttribute(35, this.getRecvSpec());
        _struct.setAttribute(36, this.getDlvrExtId());
        _struct.setAttribute(37, this.getOperate());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }

}
