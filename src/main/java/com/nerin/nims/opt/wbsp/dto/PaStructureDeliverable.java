package com.nerin.nims.opt.wbsp.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/4.
 */
public class PaStructureDeliverable {
    private List<Object> dataSource = new ArrayList<Object>();

    public List<Object> getDataSource() {

        return dataSource;
    }

    public void setDataSource(List listA,List listB) {
        dataSource.add(listA);
        dataSource.add(listB);
        this.dataSource = dataSource;
    }
}
