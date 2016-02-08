package com.vibeosys.rorderapp.data;

import java.sql.Date;

/**
 * Created by akshay on 08-02-2016.
 */
public class WaitingUserDTO {
    private String mCustomerId;
    private int mOccupancy;
    private Date mArrivalTime;
    private String mCustomerName;

    public WaitingUserDTO() {
    }

    public WaitingUserDTO(String mCustomerId, int mOccupancy, Date mArrivalTime, String mCustomerName) {
        this.mCustomerId = mCustomerId;
        this.mOccupancy = mOccupancy;
        this.mArrivalTime = mArrivalTime;
        this.mCustomerName = mCustomerName;
    }

    public String getmCustomerId() {
        return mCustomerId;
    }

    public void setmCustomerId(String mCustomerId) {
        this.mCustomerId = mCustomerId;
    }

    public int getmOccupancy() {
        return mOccupancy;
    }

    public void setmOccupancy(int mOccupancy) {
        this.mOccupancy = mOccupancy;
    }

    public Date getmArrivalTime() {
        return mArrivalTime;
    }

    public void setmArrivalTime(Date mArrivalTime) {
        this.mArrivalTime = mArrivalTime;
    }

    public String getmCustomerName() {
        return mCustomerName;
    }

    public void setmCustomerName(String mCustomerName) {
        this.mCustomerName = mCustomerName;
    }
}