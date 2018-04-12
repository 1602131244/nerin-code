package com.nerin.nims.opt.app.web.rest.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 6/1/2016.
 */
public class FancyTreeDTO {

    private String title;
    private String key;
    private boolean folder = false;
    private boolean selected = false;
    private boolean hideCheckbox = false;
    private boolean unselectable = false;
    private String tooltip;
    private List<FancyTreeDTO> children = new ArrayList<FancyTreeDTO>();

    public boolean isHideCheckbox() {
        return hideCheckbox;
    }

    public void setHideCheckbox(boolean hideCheckbox) {
        this.hideCheckbox = hideCheckbox;
    }

    public boolean isUnselectable() {
        return unselectable;
    }

    public void setUnselectable(boolean unselectable) {
        this.unselectable = unselectable;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isFolder() {
        return folder;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public List<FancyTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<FancyTreeDTO> children) {
        this.children = children;
    }
}
