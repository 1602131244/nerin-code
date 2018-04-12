package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wancong on 2016/7/7.
 */
public class DwgStatusChangeResponseDTO {



    private String status;//关联物料名称变更描述
    private List<DwgStatusChangeDTO> rows = new ArrayList<DwgStatusChangeDTO>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DwgStatusChangeDTO> getRows() {
        return rows;
    }

    public void setRows(List<DwgStatusChangeDTO> rows) {
        this.rows = rows;
    }
}
