package com.nerin.nims.opt.navi.dto;

/**
 * Created by Administrator on 2017/2/23.
 */
public class ProjectYBphaseDTO {
    private String name;
    private Long phase_weight;
    private Long seq_num;
    private Long phase_progress;

    public Long getPhase_progress() {
        return phase_progress;
    }

    public void setPhase_progress(Long phase_progress) {
        this.phase_progress = phase_progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhase_weight() {
        return phase_weight;
    }

    public void setPhase_weight(Long phase_weight) {
        this.phase_weight = phase_weight;
    }

    public Long getSeq_num() {
        return seq_num;
    }

    public void setSeq_num(Long seq_num) {
        this.seq_num = seq_num;
    }
}
