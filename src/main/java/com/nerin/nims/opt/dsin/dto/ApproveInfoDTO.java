package com.nerin.nims.opt.dsin.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class ApproveInfoDTO {
    private long projectId;
    private String phaseName;
    private ArrayList<ApproveIdDTO> rows;
    private List<String> psry;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public ArrayList<ApproveIdDTO> getRows() {
        return rows;
    }

    public void setRows(ArrayList<ApproveIdDTO> rows) {
        this.rows = rows;
    }

    public String getPhaseName() {
        return phaseName;
    }

    public void setPhaseName(String phaseName) {
        this.phaseName = phaseName;
    }

    public List<String> getPsry() {
        return psry;
    }

    public void setPsry(List<String> psry) {
        this.psry = psry;
    }
}
