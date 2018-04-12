package com.nerin.nims.opt.video.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class DeleteDTO {

    private String type;
    private List<VIDDTO> rows = new ArrayList<VIDDTO>();


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<VIDDTO> getRows() {
        return rows;
    }

    public void setRows(List<VIDDTO> rows) {
        this.rows = rows;
    }
}
