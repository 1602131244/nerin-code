package com.nerin.nims.opt.cadi.dto;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 16/7/15.
 */
public class DrawSizeDTO {

    private Long index;        //序号
    private String pdrawnum; //所属图号

    public Long getIndex() {
        return index;
    }

    public void setIndex(Long index) {
        this.index = index;
    }

    public String getPdrawnum() {
        return pdrawnum;
    }

    public void setPdrawnum(String pdrawnum) {
        this.pdrawnum = pdrawnum;
    }
}
