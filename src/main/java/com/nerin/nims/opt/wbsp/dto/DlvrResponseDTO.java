package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class DlvrResponseDTO {

    private String saveWork;
    private List<PaPublishDeliverablesDTO> echoUpdate = new ArrayList<PaPublishDeliverablesDTO>();

    public String getSaveWork() {
        return saveWork;
    }

    public void setSaveWork(String saveWork) {
        this.saveWork = saveWork;
    }

    public List<PaPublishDeliverablesDTO> getEchoUpdate() {
        return echoUpdate;
    }

    public void setEchoUpdate(List<PaPublishDeliverablesDTO> echoUpdate) {
        this.echoUpdate = echoUpdate;
    }
}
