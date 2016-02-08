package com.vibeosys.rorderapp.data;

import java.util.Date;

/**
 * Created by shrinivas on 06-02-2016.
 */
public class BillDetailsDTO {




    private int billNo;
    private Date billDate;
    private double  netAmount;
    private double totalTax;
    private double totalPayableTaxAmt;
    private String servedByName;

    public BillDetailsDTO() {
    }

    public BillDetailsDTO(int billNo, Date billDate, double netAmount, double totalTax, double totalPayableTaxAmt, String servedByName) {
        this.billNo = billNo;
        this.billDate = billDate;
        this.netAmount = netAmount;
        this.totalTax = totalTax;
        this.totalPayableTaxAmt = totalPayableTaxAmt;
        this.servedByName = servedByName;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
    }

    public double getTotalPayableTaxAmt() {
        return totalPayableTaxAmt;
    }

    public void setTotalPayableTaxAmt(double totalPayableTaxAmt) {
        this.totalPayableTaxAmt = totalPayableTaxAmt;
    }

    public String getServedByName() {
        return servedByName;
    }

    public void setServedByName(String servedByName) {
        this.servedByName = servedByName;
    }
}
