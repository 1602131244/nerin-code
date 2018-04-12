package com.nerin.nims.opt.wh.dto;


import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class TypeDTO {

    private String type;
    private ArrayList children;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList getChildren() {
        return children;
    }

    public void setChildren(ArrayList children) {
        this.children = children;
    }
}
