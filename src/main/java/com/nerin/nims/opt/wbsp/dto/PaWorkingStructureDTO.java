package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/25.
 */
public class PaWorkingStructureDTO extends WbspBaseDTO {
    private String draftwbsName; //版本名称
    private Long structrueId; //工作计划版本ID
    private Long structrueVersionId; //工作计划版本ID
    private String time; //最后更新日期
    private String operator;//最后更新人
    private String checkout;//锁定人
    private String checkoutId; //锁定人ID
    private String headOrbranch;//总部还是分公司
    private String division;//是否启用子项管理

    public String getCheckoutId() {
        return checkoutId;
    }

    public void setCheckoutId(String checkoutId) {
        this.checkoutId = checkoutId;
    }

    public String getDraftwbsName() {
        return draftwbsName;
    }

    public void setDraftwbsName(String draftwbsName) {
        this.draftwbsName = draftwbsName;
    }

    public Long getStructrueVersionId() {
        return structrueVersionId;
    }

    public void setStructrueVersionId(Long structrueVersionId) {
        this.structrueVersionId = structrueVersionId;
    }

    public Long getStructrueId() {
        return structrueId;
    }

    public void setStructrueId(Long structrueId) {
        this.structrueId = structrueId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getHeadOrbranch() {
        return headOrbranch;
    }

    public void setHeadOrbranch(String headOrbranch) {
        this.headOrbranch = headOrbranch;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }
}
