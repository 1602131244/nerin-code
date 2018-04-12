package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class DlvrSaveDTO {
    private List<PaPublishDeliverablesDTO> addRows = new ArrayList<PaPublishDeliverablesDTO>();
    private List<PaPublishDeliverablesDTO> deleteRows = new ArrayList<PaPublishDeliverablesDTO>();
    private List<PaPublishDeliverablesDTO> updateRows = new ArrayList<PaPublishDeliverablesDTO>();
    private Long projectId;
    private String phaseCode;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPhaseCode() {
        return phaseCode;
    }

    public void setPhaseCode(String phaseCode) {
        this.phaseCode = phaseCode;
    }

    public List<PaPublishDeliverablesDTO> getAddRows() {
        return addRows;
    }

    public void setAddRows(List<PaPublishDeliverablesDTO> addRows) {
        this.addRows = addRows;
    }

    public List<PaPublishDeliverablesDTO> getDeleteRows() {
        return deleteRows;
    }

    public void setDeleteRows(List<PaPublishDeliverablesDTO> deleteRows) {
        this.deleteRows = deleteRows;
    }

    public List<PaPublishDeliverablesDTO> getUpdateRows() {
        return updateRows;
    }

    public void setUpdateRows(List<PaPublishDeliverablesDTO> updateRows) {
        this.updateRows = updateRows;
    }
}
