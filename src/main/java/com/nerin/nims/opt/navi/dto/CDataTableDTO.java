package com.nerin.nims.opt.navi.dto;

import com.nerin.nims.opt.nbcc.dto.DataTablesDTO;

/**
 * Created by Administrator on 2016/7/11.
 */
public class CDataTableDTO extends DataTablesDTO {
    private  double total;
    private  double totalCNY;
    private  Long headerId;
    private  Long percent;
    private  String message;

    public double getTotalCNY() {
        return totalCNY;
    }

    public void setTotalCNY(double totalCNY) {
        this.totalCNY = totalCNY;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Long getPercent() {
        return percent;
    }

    public void setPercent(Long percent) {
        this.percent = percent;
    }
}
