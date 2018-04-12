package com.nerin.nims.opt.wbsp.dto;

import java.util.List;

/**
 * Created by Administrator on 2016/8/8.
 */
public class PaOthersList {
    private String othersCount;
    private List<PaOthers> paOthers;

    public String getOthersCount() {
        return othersCount;
    }

    public void setOthersCount(String othersCount) {
        this.othersCount = othersCount;
    }

    public List<PaOthers> getPaOthers() {
        return paOthers;
    }

    public void setPaOthers(List<PaOthers> paOthers) {
        this.paOthers = paOthers;
    }
}
