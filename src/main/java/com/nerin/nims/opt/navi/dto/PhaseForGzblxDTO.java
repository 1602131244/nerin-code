package com.nerin.nims.opt.navi.dto;

/**
 * Created by yinglgu on 7/30/2016.
 */
public class PhaseForGzblxDTO {
    private String typeCode;//：varchar2：标记
    private String typeName;//varchar2:类型

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
