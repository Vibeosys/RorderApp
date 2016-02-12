package com.vibeosys.rorderapp.data;

import com.vibeosys.rorderapp.util.ROrderDateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shrinivas on 06-02-2016.
 */
public class BillDetailsDTO {


    private int billNo;
    private String billDate;
    private double netAmount;
    private double totalTax;
    private double totalPayableTaxAmt;
    private String servedByName;
    private int tableNo;
    private Date billInDate;
    public BillDetailsDTO() {
    }

    public BillDetailsDTO(int billNo, String billDate, double netAmount, double totalTax,
                          double totalPayableTaxAmt, String servedByName, int tableNo) {
        this.billNo = billNo;
        this.billDate = billDate;
        this.netAmount = netAmount;
        this.totalTax = totalTax;
        this.totalPayableTaxAmt = totalPayableTaxAmt;
        this.servedByName = servedByName;
        this.tableNo = tableNo;
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

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }


    public void setBillInDate(Date billInDate) {
        this.billInDate = billInDate;
    }
}
