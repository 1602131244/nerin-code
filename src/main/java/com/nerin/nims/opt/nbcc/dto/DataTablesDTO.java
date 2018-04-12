package com.nerin.nims.opt.nbcc.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yinglgu on 5/23/2016.
 */
public class DataTablesDTO {
    private long draw;
    private long recordsTotal;
    private long recordsFiltered;
    private long pageNo;
    private long pageSize;
    private long dataTotal;
    private List dataSource = new ArrayList();

    public long getDraw() {
        return draw;
    }

    public void setDraw(long draw) {
        this.draw = draw;
    }

    public long getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public long getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List getDataSource() {
        return dataSource;
    }

    public void setDataSource(List dataSource) {
        this.dataSource = dataSource;
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

    public long getDataTotal() {
        return dataTotal;
    }

    public void setDataTotal(long dataTotal) {
        this.dataTotal = dataTotal;
    }
}
