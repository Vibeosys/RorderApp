package com.vibeosys.quickserve.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 23-01-2016.
 */
public class BillDbDTO extends BaseDTO {

    private int billNo;
    private String billDate;
    private String billTime;
    private double netAmt;
    private double totalTaxAmt;
    private double totalPayAmt;
    private int userId;
    private String custId;
    private int tableId;
    private int isPayed;
    private int payedBy;
    private double discount;
    private int takeawayNo;

    public BillDbDTO() {
    }

    public BillDbDTO(int billNo, String billDate, String billTime, double netAmt, double totalTaxAmt, double totalPayAmt, int userId) {
        this.billNo = billNo;
        this.billDate = billDate;
        this.billTime = billTime;
        this.netAmt = netAmt;
        this.totalTaxAmt = totalTaxAmt;
        this.totalPayAmt = totalPayAmt;
        this.userId = userId;
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

    public String getBillTime() {
        return billTime;
    }

    public void setBillTime(String billTime) {
        this.billTime = billTime;
    }

    public double getNetAmt() {
        return netAmt;
    }

    public void setNetAmt(double netAmt) {
        this.netAmt = netAmt;
    }

    public double getTotalTaxAmt() {
        return totalTaxAmt;
    }

    public void setTotalTaxAmt(double totalTaxAmt) {
        this.totalTaxAmt = totalTaxAmt;
    }

    public double getTotalPayAmt() {
        return totalPayAmt;
    }

    public void setTotalPayAmt(double totalPayAmt) {
        this.totalPayAmt = totalPayAmt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int isPayed() {
        return isPayed;
    }

    public void setIsPayed(int isPayed) {
        this.isPayed = isPayed;
    }

    public int getPayedBy() {
        return payedBy;
    }

    public void setPayedBy(int payedBy) {
        this.payedBy = payedBy;
    }

    public int getIsPayed() {
        return isPayed;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getTakeawayNo() {
        return takeawayNo;
    }

    public void setTakeawayNo(int takeawayNo) {
        this.takeawayNo = takeawayNo;
    }

    public static List<BillDbDTO> deserializeBill(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<BillDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                BillDbDTO deserializeObject = gson.fromJson(serializedString, BillDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
