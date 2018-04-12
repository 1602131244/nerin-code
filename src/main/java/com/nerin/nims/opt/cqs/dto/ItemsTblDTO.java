package com.nerin.nims.opt.cqs.dto;

/**
 * Created by user on 16/7/13.
 */
public class ItemsTblDTO {
    private Long itemOrderNumber;
    private Long itemId;
    private String piping_matl_class;
    private String itemCategory;
    private String itemName;
    private Double minDn;
    private Double maxDn;
    private String endFacing;
    private String thkRating;
    private String material;
    private String standardModel;
    private String note;

    public Long getItemOrderNumber() {
        return itemOrderNumber;
    }

    public void setItemOrderNumber(Long itemOrderNumber) {
        this.itemOrderNumber = itemOrderNumber;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getPiping_matl_class() {
        return piping_matl_class;
    }

    public void setPiping_matl_class(String piping_matl_class) {
        this.piping_matl_class = piping_matl_class;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Double getMinDn() {
        return minDn;
    }

    public void setMinDn(Double minDn) {
        this.minDn = minDn;
    }

    public Double getMaxDn() {
        return maxDn;
    }

    public void setMaxDn(Double maxDn) {
        this.maxDn = maxDn;
    }

    public String getEndFacing() {
        return endFacing;
    }

    public void setEndFacing(String endFacing) {
        this.endFacing = endFacing;
    }

    public String getThkRating() {
        return thkRating;
    }

    public void setThkRating(String thkRating) {
        this.thkRating = thkRating;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getStandardModel() {
        return standardModel;
    }

    public void setStandardModel(String standardModel) {
        this.standardModel = standardModel;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
