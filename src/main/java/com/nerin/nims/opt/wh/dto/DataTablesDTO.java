package com.nerin.nims.opt.wh.dto;

import com.nerin.nims.opt.wbsp.dto.WbspBaseDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class DataTablesDTO  {

    private long pageNo;
    private long pageSize;
    private long total;
    private List rows = new ArrayList();

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public long getPageNo() {
        return pageNo;
    }

    public void setPageNo(long pageNo) {
        this.pageNo = pageNo;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }


}
