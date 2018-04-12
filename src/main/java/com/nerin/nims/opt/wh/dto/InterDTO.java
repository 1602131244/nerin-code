package com.nerin.nims.opt.wh.dto;


import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class InterDTO {
    private Long id;
    private ArrayList children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArrayList getChildren() {
        return children;
    }

    public void setChildren(ArrayList children) {
        this.children = children;
    }
}
