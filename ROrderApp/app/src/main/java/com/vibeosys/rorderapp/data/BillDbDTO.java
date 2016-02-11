package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 23-01-2016.
 */
public class BillDbDTO extends BaseDTO {

    private int billNo;
    private Date billDate;
    private Time billTime;
    private double netAmount;
    private double totalTaxAmount;
    private double totalPayAmount;
    private Date createdDate;
    private Date updatedDate;
    private int userId;


    public BillDbDTO() {
    }

    public BillDbDTO(int billNo, Date billDate, Time billTime, double netAmount, double totalTaxAmount, double totalPayAmount, Date createdDate, Date updatedDate, int userId) {
        this.billNo = billNo;
        this.billDate = billDate;
        this.billTime = billTime;
        this.netAmount = netAmount;
        this.totalTaxAmount = totalTaxAmount;
        this.totalPayAmount = totalPayAmount;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.userId = userId;
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

    public Time getBillTime() {
        return billTime;
    }

    public void setBillTime(Time billTime) {
        this.billTime = billTime;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public double getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public double getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(double totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static List<BillDbDTO> deserializeBill(List<String> serializedStringList) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
        ArrayList<BillDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            BillDbDTO deserializeObject = gson.fromJson(serializedString, BillDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
