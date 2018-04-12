package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class ElementSaveDTO {
    private List<PaElementsDTO> addRows = new ArrayList<PaElementsDTO>();
    private List<PaElementsDTO> updateRows = new ArrayList<PaElementsDTO>();
    private List<PaElementsIdDTO> deleteRows = new ArrayList<PaElementsIdDTO>();
    private Long projID;
    private String phaseID;
    private Long draftwbsID;
    private String headOrbranch; //当前项目是否总部或分公司类型项目
    private String divisin;//当前阶段是否分子项管理

    public String getHeadOrbranch() {
        return headOrbranch;
    }

    public void setHeadOrbranch(String headOrbranch) {
        this.headOrbranch = headOrbranch;
    }

    public String getDivisin() {
        return divisin;
    }

    public void setDivisin(String divisin) {
        this.divisin = divisin;
    }

    public Long getProjID() {
        return projID;
    }

    public void setProjID(Long projID) {
        this.projID = projID;
    }

    public String getPhaseID() {
        return phaseID;
    }

    public void setPhaseID(String phaseID) {
        this.phaseID = phaseID;
    }

    public Long getDraftwbsID() {
        return draftwbsID;
    }

    public void setDraftwbsID(Long draftwbsID) {
        this.draftwbsID = draftwbsID;
    }

    public List<PaElementsDTO> getAddRows() {
        return addRows;
    }

    public void setAddRows(List<PaElementsDTO> addRows) {
        this.addRows = addRows;
    }

    public List<PaElementsDTO> getUpdateRows() {
        return updateRows;
    }

    public void setUpdateRows(List<PaElementsDTO> updateRows) {
        this.updateRows = updateRows;
    }

    public List<PaElementsIdDTO> getDeleteRows() {
        return deleteRows;
    }

    public void setDeleteRows(List<PaElementsIdDTO> deleteRows) {
        this.deleteRows = deleteRows;
    }
}
