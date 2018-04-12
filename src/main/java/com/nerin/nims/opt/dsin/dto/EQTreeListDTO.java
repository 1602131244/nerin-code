package com.nerin.nims.opt.dsin.dto;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class EQTreeListDTO {
    private String id;
    private String text;
    private String iconCls;
    private String color;
    private String state;
    private ArrayList children;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList getChildren() {
        return children;
    }

    public void setChildren(ArrayList children) {
        this.children = children;
    }
}
