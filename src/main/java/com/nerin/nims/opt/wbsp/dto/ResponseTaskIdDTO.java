package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class ResponseTaskIdDTO extends WbspBaseDTO {

    private long Id;//task_ver_id
    private long taskElementId;//task_id

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getTaskElementId() {
        return taskElementId;
    }

    public void setTaskElementId(long taskElementId) {
        this.taskElementId = taskElementId;
    }
}
