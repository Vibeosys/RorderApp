package com.vibeosys.rorderapp.data;

import java.util.Date;

/**
 * Created by shrinivas on 06-02-2016.
 */
public class BillDetailsDTO {


    private int billNo;
    private String billDate;
    private double netAmount;
    private double totalTax;
    private double totalPayableAmt;
    private String servedByName;
    private int tableNo;
    private Date billInDate;
    private String billTime;
    private int billPayed;

    public BillDetailsDTO() {
    }

    public BillDetailsDTO(int billNo, String billDate, double netAmount, double totalTax,
                          double totalPayableTaxAmt, String servedByName, int tableNo, String billTime) {
        this.billNo = billNo;
        this.billDate = billDate;
        this.netAmount = netAmount;
        this.totalTax = totalTax;
        this.totalPayableAmt = totalPayableTaxAmt;
        this.servedByName = servedByName;
        this.tableNo = tableNo;
        this.billTime = billTime;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
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

    public double getTotalPayableAmt() {
        return totalPayableAmt;
    }

    public void setTotalPayableAmt(double totalPayableAmt) {
        this.totalPayableAmt = totalPayableAmt;
    }

    public String getServedByName() {
        return servedByName;
    }

    public void setServedByName(String servedByName) {
        this.servedByName = servedByName;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }


    public void setBillInDate(Date billInDate) {
        this.billInDate = billInDate;
    }

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String billTime) {
        this.billTime = billTime;
    }

    public int getBillPayed() {
        return billPayed;
    }

    public void setBillPayed(int billPayed) {
        this.billPayed = billPayed;
    }
}
