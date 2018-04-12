package com.nerin.nims.opt.glcs.dto;

import com.nerin.nims.opt.base.rest.OracleBaseDTO;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */
public class UnitDto extends OracleBaseDTO {
    private Long rangeId;
    private Long rangeVerson;
    private Long unitId;
    private String unitNumber;
    private String unitName;
    private String unitShortName;
    private Long unitParentId;
    private String createByName;
    private String lastByName;

    private List<UnitDto> children;
    private String title;
    private boolean folder = false;
    private String tooltip;

    public Long getRangeId() {
        return rangeId;
    }

    public void setRangeId(Long rangeId) {
        this.rangeId = rangeId;
    }

    public Long getRangeVerson() {
        return rangeVerson;
    }

    public void setRangeVerson(Long rangeVerson) {
        this.rangeVerson = rangeVerson;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitShortName() {
        return unitShortName;
    }

    public void setUnitShortName(String unitShortName) {
        this.unitShortName = unitShortName;
    }

    public Long getUnitParentId() {
        return unitParentId;
    }

    public void setUnitParentId(Long unitParentId) {
        this.unitParentId = unitParentId;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getLastByName() {
        return lastByName;
    }

    public void setLastByName(String lastByName) {
        this.lastByName = lastByName;
    }

    public List<UnitDto> getChildren() {
        return children;
    }

    public void setChildren(List<UnitDto> children) {
        this.children = children;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }
}
