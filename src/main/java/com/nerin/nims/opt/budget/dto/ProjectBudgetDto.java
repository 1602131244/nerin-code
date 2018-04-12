package com.nerin.nims.opt.budget.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

/**
 * Created by Administrator on 2017/7/20.
 */
public class ProjectBudgetDto extends OracleBaseDTO {
    private Long projectId;
    private String projectNumber;
    private String projectName;
    private Long rbsVersionId;
    private String budgetItemNumber;
    private String budgetItemName;
    private String alias;
    private String outLineNumber;
    private String budgetName;
    private String parentTaskNumber;
    private String parentTaskName;
    private Long taskId;
    private String taskName;
    private String budgetAmount;    //预算金额
    private String cost;              //占用预算金额
    private String occurrenceAmount;  //发生金额
    private String transitAmount;    //  在途成本
    private String surplusAmount;    //  可用预算金额

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getRbsVersionId() {
        return rbsVersionId;
    }

    public void setRbsVersionId(Long rbsVersionId) {
        this.rbsVersionId = rbsVersionId;
    }

    public String getBudgetItemNumber() {
        return budgetItemNumber;
    }

    public void setBudgetItemNumber(String budgetItemNumber) {
        this.budgetItemNumber = budgetItemNumber;
    }

    public String getBudgetItemName() {
        return budgetItemName;
    }

    public void setBudgetItemName(String budgetItemName) {
        this.budgetItemName = budgetItemName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getOutLineNumber() {
        return outLineNumber;
    }

    public void setOutLineNumber(String outLineNumber) {
        this.outLineNumber = outLineNumber;
    }

    public String getBudgetName() {
        return budgetName;
    }

    public void setBudgetName(String budgetName) {
        this.budgetName = budgetName;
    }

    public String getParentTaskNumber() {
        return parentTaskNumber;
    }

    public void setParentTaskNumber(String parentTaskNumber) {
        this.parentTaskNumber = parentTaskNumber;
    }

    public String getParentTaskName() {
        return parentTaskName;
    }

    public void setParentTaskName(String parentTaskName) {
        this.parentTaskName = parentTaskName;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(String budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getOccurrenceAmount() {
        return occurrenceAmount;
    }

    public void setOccurrenceAmount(String occurrenceAmount) {
        this.occurrenceAmount = occurrenceAmount;
    }

    public String getTransitAmount() {
        return transitAmount;
    }

    public void setTransitAmount(String transitAmount) {
        this.transitAmount = transitAmount;
    }

    public String getSurplusAmount() {
        return surplusAmount;
    }

    public void setSurplusAmount(String surplusAmount) {
        this.surplusAmount = surplusAmount;
    }
}
