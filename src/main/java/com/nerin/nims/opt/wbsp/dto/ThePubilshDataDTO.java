package com.nerin.nims.opt.wbsp.dto;

import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class ThePubilshDataDTO extends WbspBaseDTO {

    private boolean divison;
    private List<TreePaPublishStructureDetailsDTO> paDetailsDTO;
    private boolean exists;

    public boolean isDivison() {
        return divison;
    }

    public void setDivison(boolean divison) {
        this.divison = divison;
    }

    public List<TreePaPublishStructureDetailsDTO> getPaDetailsDTO() {
        return paDetailsDTO;
    }

    public void setPaDetailsDTO(List<TreePaPublishStructureDetailsDTO> paDetailsDTO) {
        this.paDetailsDTO = paDetailsDTO;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
