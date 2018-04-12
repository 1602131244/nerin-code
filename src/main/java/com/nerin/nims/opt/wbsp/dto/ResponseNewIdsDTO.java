package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class ResponseNewIdsDTO extends WbspBaseDTO {

    private long id;
    private long taskId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }
}
