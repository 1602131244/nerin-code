package com.nerin.nims.opt.navi_wbps.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/12.
 */
public class naviDataDTO {

    private long pageNo;
    private long pageSize;
    private long total;
    private List rows = new ArrayList();

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

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }
}
