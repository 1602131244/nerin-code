package com.nerin.nims.opt.wbsp.dto;

import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class TheHistoryDataDTO extends WbspBaseDTO {

    private boolean divison;
    private List<TreePaHistoryWbsDetailsDTO> paDetailsDTO;
    private boolean exists;

    public boolean isDivison() {
        return divison;
    }

    public void setDivison(boolean divison) {
        this.divison = divison;
    }

    public List<TreePaHistoryWbsDetailsDTO> getPaDetailsDTO() {
        return paDetailsDTO;
    }

    public void setPaDetailsDTO(List<TreePaHistoryWbsDetailsDTO> paDetailsDTO) {
        this.paDetailsDTO = paDetailsDTO;
    }

    public boolean isExists() {
        return exists;
    }

    public void setExists(boolean exists) {
        this.exists = exists;
    }
}
