package com.nerin.nims.opt.aria.dto;

/**
 * Created by Administrator on 2017/9/27.
 */
public class ContInvoiceDto {
    private double contAmount;
    private double invoiceAmount;
    private String invoicePro;
    private double wayAmount;
    private String wayPro;

    public double getContAmount() {
        return contAmount;
    }

    public void setContAmount(double contAmount) {
        this.contAmount = contAmount;
    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoicePro() {
        return invoicePro;
    }

    public void setInvoicePro(String invoicePro) {
        this.invoicePro = invoicePro;
    }

    public double getWayAmount() {
        return wayAmount;
    }

    public void setWayAmount(double wayAmount) {
        this.wayAmount = wayAmount;
    }

    public String getWayPro() {
        return wayPro;
    }

    public void setWayPro(String wayPro) {
        this.wayPro = wayPro;
    }
}
