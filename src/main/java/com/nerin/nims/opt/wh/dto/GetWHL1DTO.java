package com.nerin.nims.opt.wh.dto;



import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class GetWHL1DTO {



    private String applied;
    private ArrayList<GetWHL2DTO> rows; //每天

    public String getApplied() {
        return applied;
    }

    public void setApplied(String applied) {
        this.applied = applied;
    }

    public ArrayList<GetWHL2DTO> getRows() {
        return rows;
    }

    public void setRows(ArrayList<GetWHL2DTO> rows) {
        this.rows = rows;
    }
}
