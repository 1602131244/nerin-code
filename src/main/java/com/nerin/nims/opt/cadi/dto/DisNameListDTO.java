package com.nerin.nims.opt.cadi.dto;

/**
 * Created by user on 16/7/15.
 */
public class DisNameListDTO {

    private String dname; //工号
    private String dfullname; //中文名
    private String organizationcode; //所属组织代字
    private String DISNAME; //显示名称

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDfullname() {
        return dfullname;
    }

    public void setDfullname(String dfullname) {
        this.dfullname = dfullname;
    }

    public String getOrganizationcode() {
        return organizationcode;
    }

    public void setOrganizationcode(String organizationcode) {
        this.organizationcode = organizationcode;
    }

    public String getDISNAME() {
        return DISNAME;
    }

    public void setDISNAME(String DISNAME) {
        this.DISNAME = DISNAME;
    }
}
