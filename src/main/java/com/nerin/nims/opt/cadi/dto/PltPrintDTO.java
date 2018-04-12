package com.nerin.nims.opt.cadi.dto;

/**
 * Created by yinglgu on 2017/3/8.
 */
public class PltPrintDTO {
    private long plt_print_set_id;//,
    private long plt_order_header_id;//,
    private String charge_item;//,
    private String print_set_size;//,
    private int amount;//,
    private String unit;//,
    private double unit_price;//,
    private double total_price;//

    private String unitPrice;

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public long getPlt_print_set_id() {
        return plt_print_set_id;
    }

    public void setPlt_print_set_id(long plt_print_set_id) {
        this.plt_print_set_id = plt_print_set_id;
    }

    public long getPlt_order_header_id() {
        return plt_order_header_id;
    }

    public void setPlt_order_header_id(long plt_order_header_id) {
        this.plt_order_header_id = plt_order_header_id;
    }

    public String getCharge_item() {
        return charge_item;
    }

    public void setCharge_item(String charge_item) {
        this.charge_item = charge_item;
    }

    public String getPrint_set_size() {
        return print_set_size;
    }

    public void setPrint_set_size(String print_set_size) {
        this.print_set_size = print_set_size;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}
