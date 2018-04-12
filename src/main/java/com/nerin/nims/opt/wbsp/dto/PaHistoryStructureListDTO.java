package com.nerin.nims.opt.wbsp.dto;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/25.
 */
public class PaHistoryStructureListDTO {



    private List<TreePaHistoryStructureDTO> rows;//返前台行



    public List<TreePaHistoryStructureDTO> getRows() {
        return rows;
    }

    public void setRows(List<TreePaHistoryStructureDTO> rows) {
        this.rows = rows;
    }
}
