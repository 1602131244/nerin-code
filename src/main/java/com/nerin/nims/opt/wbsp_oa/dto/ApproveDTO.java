package com.nerin.nims.opt.wbsp_oa.dto;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/3.
 */
public class ApproveDTO {


    private ArrayList<DlvrDetailDTO> dlvrDetail; //图纸工作包列表数组
    private String content;
    private String dlvrs;
    private Long requestid;
    private Long formid;

    public ArrayList<DlvrDetailDTO> getDlvrDetail() {
        return dlvrDetail;
    }

    public void setDlvrDetail(ArrayList<DlvrDetailDTO> dlvrDetail) {
        this.dlvrDetail = dlvrDetail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDlvrs() {
        return dlvrs;
    }

    public void setDlvrs(String dlvrs) {
        this.dlvrs = dlvrs;
    }

    public Long getRequestid() {
        return requestid;
    }

    public void setRequestid(Long requestid) {
        this.requestid = requestid;
    }

    public Long getFormid() {
        return formid;
    }

    public void setFormid(Long formid) {
        this.formid = formid;
    }
}
