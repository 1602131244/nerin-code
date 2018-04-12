package com.nerin.nims.opt.wbsp.dto;

/**
 * Created by Administrator on 2016/7/24.
 */
public class PaPubilshStructureDTO {

    private String pubwbsName; //structrue_name
    private String pubTime;//publishedDate
    private String pubOperator;
    private Long pubwbsID;//structrue_id
    private String strPhaseCode;

    public String getPubwbsName() {
        return pubwbsName;
    }

    public void setPubwbsName(String pubwbsName) {
        this.pubwbsName = pubwbsName;
    }

    public String getPubTime() {
       // SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
       // String dateTime = f.format(pubTime);
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public String getPubOperator() {
        return pubOperator;
    }

    public void setPubOperator(String pubOperator) {
        this.pubOperator = pubOperator;
    }

    public Long getStructureId() {
        return pubwbsID;
    }

    public void setStructureId(Long structureId) {
        this.pubwbsID = structureId;
    }

    public String getStrPhaseCode() {
        return strPhaseCode;
    }

    public void setStrPhaseCode(String strPhaseCode) {
        this.strPhaseCode = strPhaseCode;
    }


}
