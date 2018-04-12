package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class DlvrResponseListDTO {

    private long workID; //工作包ID
    private String status;//返回状态

    public long getWorkID() {
        return workID;
    }

    public void setWorkID(long workID) {
        this.workID = workID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
