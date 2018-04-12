package com.nerin.nims.opt.wh.dto;



import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class PhaseDTO {



    private Long id;
    private String taskName;
    private String state;
    private ArrayList children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
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
