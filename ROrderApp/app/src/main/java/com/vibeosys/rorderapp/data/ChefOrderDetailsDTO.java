package com.vibeosys.rorderapp.data;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

/**
 * Created by shrinivas on 11-02-2016.
 */
public class ChefOrderDetailsDTO {


    private int mChefOrderId;
    private int mUserId;
    private String mNewOrderId;
    private String mUserName;
    private int mTableNo;
    private int mNewOrderStatus;
    private int mOrderNumner;
    private String mOrderDate;
    private String mOrderTime;
    private Time orderTm;
    private Date orderDt;
    /* private Date mplacedOrderDate;
     private Time morderTime;*/
    private ArrayList<ChefMenuDetailsDTO> mMenuChild;


    private boolean mOrderStatus;

    public ChefOrderDetailsDTO(String mNewOrderId, int mTableNo, String mUserName, int mOrderNumner,
                               int mNewOrderStatus, String orderDate, String orderTime) {
        this.mNewOrderId = mNewOrderId;
        this.mTableNo = mTableNo;
        this.mUserName = mUserName;
        this.mOrderNumner = mOrderNumner;
        this.mNewOrderStatus = mNewOrderStatus;
        this.mOrderDate = orderDate;
        this.mOrderTime =orderTime;

    }

    public int getmChefOrderId() {
        return mChefOrderId;
    }

    public void setmChefOrderId(int mChefOrderId) {
        this.mChefOrderId = mChefOrderId;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public int getmTableNo() {
        return mTableNo;
    }

    public void setmTableNo(int mTableNo) {
        this.mTableNo = mTableNo;
    }


    public boolean ismOrderStatus() {
        return mOrderStatus;
    }

    public void setmOrderStatus(boolean mOrderStatus) {
        this.mOrderStatus = mOrderStatus;
    }

    public String getmNewOrderId() {
        return mNewOrderId;
    }

    public void setmNewOrderId(String mNewOrderId) {
        this.mNewOrderId = mNewOrderId;
    }

    public ArrayList<ChefMenuDetailsDTO> getmMenuChild() {
        return mMenuChild;
    }

    public void setmMenuChild(ArrayList<ChefMenuDetailsDTO> mMenuChild) {
        this.mMenuChild = mMenuChild;
    }

    public int getmOrderNumner() {
        return mOrderNumner;
    }

    public void setmOrderNumner(int mOrderNumner) {
        this.mOrderNumner = mOrderNumner;
    }

    public int getmNewOrderStatus() {
        return mNewOrderStatus;
    }

    public void setmNewOrderStatus(int mNewOrderStatus) {
        this.mNewOrderStatus = mNewOrderStatus;
    }

    public String getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(String orderDate) {
        this.mOrderDate = orderDate;
    }

    public String getOrderTime() {
        return mOrderTime;
    }

    public void setOrderTime(String orderTime) {
        this.mOrderTime = orderTime;
    }

    public java.sql.Date getOrderDt() {
        if (this.mOrderDate == null || this.mOrderDate.isEmpty())
            return this.orderDt;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date value = null;
        try {
            value = formatter.parse(this.mOrderDate);
            orderDt = new java.sql.Date(value.getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return orderDt;
    }

    public Time getOrderTm() {
        if (this.mOrderTime == null || this.mOrderTime.isEmpty())
            return this.orderTm;

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date value = null;
        try {
            value = formatter.parse(this.mOrderTime);
            orderTm = new Time(value.getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return orderTm;
    }
}
