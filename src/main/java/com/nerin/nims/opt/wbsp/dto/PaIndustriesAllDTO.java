package com.nerin.nims.opt.wbsp.dto;



/**
 * Created by Administrator on 2016/7/24.
 */
public class PaIndustriesAllDTO  {
    private String code; //代字
    private String name;//专业
    private String industryCode;    //实际代字

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

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }
}
