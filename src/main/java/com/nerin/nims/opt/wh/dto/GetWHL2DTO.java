package com.nerin.nims.opt.wh.dto;





import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3. *
 */
public class GetWHL2DTO {

    private long id;
    private String fulldate;  //日期
    private float totalHour;    //总工时
    private ArrayList<GetWHL3DTO> tasks; //明细

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFulldate() {
        return fulldate;
    }

    public void setFulldate(String fulldate) {
        this.fulldate = fulldate;
    }

    public float getTotalHour() {
        return totalHour;
    }

    public void setTotalHour(float totalHour) {
        this.totalHour = totalHour;
    }

    public ArrayList<GetWHL3DTO> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<GetWHL3DTO> tasks) {
        this.tasks = tasks;
    }
}
