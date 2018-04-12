package com.nerin.nims.opt.navi.dto;

import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;

/**
 * Created by Administrator on 2016/7/7.
 */
public class MDataTableDTO extends DataTablesDTO {
    private String projectPercent;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getProjectPercent() {
        return projectPercent;
    }

    public void setProjectPercent(String projectPercent) {
        this.projectPercent = projectPercent;
    }
}
