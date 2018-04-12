package com.nerin.nims.opt.dsin.dto;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class EQHeadDTO {
    private String id;
    private String text;
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

    public ArrayList getChildren() {
        return children;
    }

    public void setChildren(ArrayList children) {
        this.children = children;
    }
}
