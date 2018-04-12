package com.nerin.nims.opt.card.dto;

/**
 * Created by Administrator on 2017/11/15.
 */
public class costDTO {

    private String base_pername;
    private String base_perno;
    private String base_groupid;
    private String base_groupname;
    private Float cost;

    public String getBase_pername() {
        return base_pername;
    }

    public void setBase_pername(String base_pername) {
        this.base_pername = base_pername;
    }

    public String getBase_perno() {
        return base_perno;
    }

    public void setBase_perno(String base_perno) {
        this.base_perno = base_perno;
    }

    public String getBase_groupid() {
        return base_groupid;
    }

    public void setBase_groupid(String base_groupid) {
        this.base_groupid = base_groupid;
    }

    public String getBase_groupname() {
        return base_groupname;
    }

    public void setBase_groupname(String base_groupname) {
        this.base_groupname = base_groupname;
    }

    public Float getCost() {
        return cost;
    }

    public void setCost(Float cost) {
        this.cost = cost;
    }
}
