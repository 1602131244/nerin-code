package com.nerin.nims.opt.budget.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

import java.util.Date;

/**
 * Created by Administrator on 2017/7/21.
 */
public class ProjectCostDto extends OracleBaseDTO {
    private String costType;      //成本类型  1
    private Long projectId;
    private String elementNumber;
    private String elementName;
    private String expenditureType;   //支出类型 2
    private String parentTaskName;   //    顶层任务 3
    private String taskName;       //    任务名称 4
    private String invnoType;     //   发票来源 6
    private Date expenditureDate;    //    支出日期 5
    private String invnoNumber;    //   发票编号  7
    private String vendorName;   // 供应商名称  10
    private String quantity;   // 数量 8
    private String cost;    //金额 9
    private String note;   //摘要  11

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getElementNumber() {
        return elementNumber;
    }

    public void setElementNumber(String elementNumber) {
        this.elementNumber = elementNumber;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getExpenditureType() {
        return expenditureType;
    }

    public void setExpenditureType(String expenditureType) {
        this.expenditureType = expenditureType;
    }

    public String getParentTaskName() {
        return parentTaskName;
    }

    public void setParentTaskName(String parentTaskName) {
        this.parentTaskName = parentTaskName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getInvnoType() {
        return invnoType;
    }

    public void setInvnoType(String invnoType) {
        this.invnoType = invnoType;
    }

    public Date getExpenditureDate() {
        return expenditureDate;
    }

    public void setExpenditureDate(Date expenditureDate) {
        this.expenditureDate = expenditureDate;
    }

    public String getInvnoNumber() {
        return invnoNumber;
    }

    public void setInvnoNumber(String invnoNumber) {
        this.invnoNumber = invnoNumber;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
