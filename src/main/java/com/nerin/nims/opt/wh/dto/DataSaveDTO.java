package com.nerin.nims.opt.wh.dto;

import java.util.ArrayList;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class DataSaveDTO {

    private  String date;
    private ArrayList<SaveDTO> tasks;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<SaveDTO> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<SaveDTO> tasks) {
        this.tasks = tasks;
    }
}
