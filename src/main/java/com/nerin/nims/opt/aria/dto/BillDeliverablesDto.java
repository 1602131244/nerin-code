package com.nerin.nims.opt.aria.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

import java.util.Date;

/**
 * Created by Administrator on 2017/6/21.
 */
public class BillDeliverablesDto extends OracleBaseDTO {
    private String lineNumber;
    private String deliverableNumber;
    private Long deliverableId;
    private String inventoryOrgCode;
    private Long inventoryOrgId;
    private String itemNumber;
    private Long itemId;
    private double quantity;
    private double shippedQuantity;
    private String uomCode;
    private double unitPrice;
    private String curr;
    private Date deliveryDate;
    private String rowId;
    private Long contHeaderId;
    private String contLineId;
    private Long projectId;
    private Long taskId;
    private String lineStyle;
    private String projectName;
    private String projectNumber;
    private String lineDescription;

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getDeliverableNumber() {
        return deliverableNumber;
    }

    public void setDeliverableNumber(String deliverableNumber) {
        this.deliverableNumber = deliverableNumber;
    }

    public Long getDeliverableId() {
        return deliverableId;
    }

    public void setDeliverableId(Long deliverableId) {
        this.deliverableId = deliverableId;
    }

    public String getInventoryOrgCode() {
        return inventoryOrgCode;
    }

    public void setInventoryOrgCode(String inventoryOrgCode) {
        this.inventoryOrgCode = inventoryOrgCode;
    }

    public Long getInventoryOrgId() {
        return inventoryOrgId;
    }

    public void setInventoryOrgId(Long inventoryOrgId) {
        this.inventoryOrgId = inventoryOrgId;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getShippedQuantity() {
        return shippedQuantity;
    }

    public void setShippedQuantity(double shippedQuantity) {
        this.shippedQuantity = shippedQuantity;
    }

    public String getUomCode() {
        return uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCurr() {
        return curr;
    }

    public void setCurr(String curr) {
        this.curr = curr;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public Long getContHeaderId() {
        return contHeaderId;
    }

    public void setContHeaderId(Long contHeaderId) {
        this.contHeaderId = contHeaderId;
    }

    public String getContLineId() {
        return contLineId;
    }

    public void setContLineId(String contLineId) {
        this.contLineId = contLineId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getLineStyle() {
        return lineStyle;
    }

    public void setLineStyle(String lineStyle) {
        this.lineStyle = lineStyle;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getLineDescription() {
        return lineDescription;
    }

    public void setLineDescription(String lineDescription) {
        this.lineDescription = lineDescription;
    }
}
