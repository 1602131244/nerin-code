package com.nerin.nims.opt.wbsp_oa.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/3.
 */
public class OaDrawNumDTO {
    private long requestid;
    private List<String> drawNums;

    public long getRequestid() {
        return requestid;
    }

    public void setRequestid(long requestid) {
        this.requestid = requestid;
    }

    public List<String> getDrawNums() {
        return drawNums;
    }

    public void setDrawNums(List<String> drawNums) {
        this.drawNums = drawNums;
    }
}
