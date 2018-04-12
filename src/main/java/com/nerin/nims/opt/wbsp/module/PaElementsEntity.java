package com.nerin.nims.opt.wbsp.module;

import com.nerin.nims.opt.base.rest.OracleBaseEntity;
import oracle.jdbc.OracleTypes;
import oracle.jpub.runtime.MutableStruct;
import oracle.sql.Datum;
import oracle.sql.ORAData;
import oracle.sql.ORADataFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/14.
 */
public class PaElementsEntity implements ORAData {
    private long projElementId;
    private long id;//elementVersionId;
   // private long outlineLevel;//refTaskVersionId 与被引用的节点之间的层级关系 0 同级 父子
    private String code; //taskNumber;
    private String name;//taskName;
    private Date startDate;//scheduledStartDate;
    private Date endDate;//scheduledFinishDate;
    private long typeId;
    private String dualName; //bilingual;    //双语
    private double workCoef;//workLoad;    //工作量
    private String status;//statusCode;    //节点状态
    private String srcStru;//sourceClass;   //复制来源
    private Long srcID;//sourceStructureVerId; //复制来源版本VERID
    private Long srcRow;//sourceElementId; //复制来源elementID
    private Long parentId; //上层节点
    private String operate; //insert delete update

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
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

     /*  public long getOutlineLevel() {
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

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
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
        String statusCode = new String();
        switch (status)
        {
            case "新增" :
                statusCode = "N";
                break;
            case "生效" :
                statusCode = "E";
                break;
            case "失效" :
                statusCode = "L";
                break;
        }

        this.status = statusCode;
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

    public static final String _ORACLE_TYPE_NAME = "APPS.WBSP_PA_ELEMENTS_REC";
    protected MutableStruct _struct;
    static int[] _sqlType = {OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.DATE, OracleTypes.DATE, OracleTypes.NUMBER, OracleTypes.VARCHAR, OracleTypes.VARCHAR
            , OracleTypes.VARCHAR, OracleTypes.VARCHAR, OracleTypes.NUMBER, OracleTypes.NUMBER, OracleTypes.NUMBER
            , OracleTypes.VARCHAR};
    static ORADataFactory[] _factory = new ORADataFactory[_sqlType.length];

    public PaElementsEntity() {
        _struct = new MutableStruct(new Object[_sqlType.length], _sqlType, _factory);
    }

    @Override
    public Datum toDatum(Connection connection) throws SQLException {
        _struct.setAttribute(0, this.getProjElementId());
        _struct.setAttribute(1, this.getId());
      //  _struct.setAttribute(2, this.getOutlineLevel());
        _struct.setAttribute(2, this.getCode());
        _struct.setAttribute(3, this.getName());
        _struct.setAttribute(4, this.getStartDate());
        _struct.setAttribute(5, this.getEndDate());
        _struct.setAttribute(6, this.getTypeId());
        _struct.setAttribute(7, this.getDualName());
        _struct.setAttribute(8, String.valueOf(this.getWorkCoef()));
        _struct.setAttribute(9, this.getStatus());
        _struct.setAttribute(10, this.getSrcStru());
        _struct.setAttribute(11, this.getSrcID());
        _struct.setAttribute(12, this.getSrcRow());
        _struct.setAttribute(13, this.getParentId());
        _struct.setAttribute(14, this.getOperate());
        return _struct.toDatum(connection, _ORACLE_TYPE_NAME);
    }
}
