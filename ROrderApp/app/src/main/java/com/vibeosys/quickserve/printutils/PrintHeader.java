package com.vibeosys.quickserve.printutils;

import android.graphics.Bitmap;

/**
 * Created by akshay on 25-03-2016.
 */
public class PrintHeader {
    private String servedBy;
    private String tableNo;
    private String time;
    private String restaurantName;
    private String billType;
    private String address;
    private String phoneNumber;
    private String number;
    private String custName;
    private String custAddress;
    private String phNo;
    private Bitmap bmpIcon;

    public PrintHeader() {
    }

    public PrintHeader(String servedBy, String tableNo, String time) {
        this.servedBy = servedBy;
        this.tableNo = tableNo;
        this.time = time;
    }

    public String getServedBy() {
        return servedBy;
    }

    public void setServedBy(String servedBy) {
        this.servedBy = servedBy;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public Bitmap getBmpIcon() {
        return bmpIcon;
    }

    public void setBmpIcon(Bitmap bmpIcon) {
        this.bmpIcon = bmpIcon;
    }

}
