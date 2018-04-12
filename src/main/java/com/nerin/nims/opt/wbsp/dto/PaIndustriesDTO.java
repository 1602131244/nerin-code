package com.nerin.nims.opt.wbsp.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2016/7/24.
 */
public class PaIndustriesDTO {
    private String code; //代字
    private String name;//专业 industryName
    private Long id; //ID  projectIndustryId
    private String industryCode;    //代字
    private double ratio;    //比例//industryRate
    private String dual;    //双语

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    public String getDual() {
        return dual;
    }

    public void setDual(String dual) {
        this.dual = dual;
    }
}
