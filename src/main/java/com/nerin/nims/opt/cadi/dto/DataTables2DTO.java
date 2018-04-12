package com.nerin.nims.opt.cadi.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwl  20170705
 */
public class DataTables2DTO {


    private long pageNo;
    private long pageSize;
    private long total;

    private List rows = new ArrayList();


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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
