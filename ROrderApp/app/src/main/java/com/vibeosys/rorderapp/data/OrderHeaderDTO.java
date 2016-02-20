package com.vibeosys.rorderapp.data;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Created by akshay on 04-02-2016.
 */
public class OrderHeaderDTO {
    private String mOrderId;
    private int mOrderNo;
    private boolean mOrderStatus;
    private Date mOrderDate;
    private Time mOrderTime;
    private int mTableNo;
    private int mUserId;
    private double mOrderAmount;
    private int mItemCount;
    private boolean mCurrent;
    private String custId;

    private List<OrderDetailsDTO> orderDetailsDTOs;

    public OrderHeaderDTO(int mTableNo, int mUserId, double mOrderAmount, int mItemCount,
                          boolean mCurrent, List<OrderDetailsDTO> orderDetailsDTOs) {
        this.mTableNo = mTableNo;
        this.mUserId = mUserId;
        this.mOrderAmount = mOrderAmount;
        this.mItemCount = mItemCount;
        this.mCurrent = mCurrent;
        this.orderDetailsDTOs = orderDetailsDTOs;
    }

    public OrderHeaderDTO(String mOrderId, int mOrderNo, boolean mOrderStatus,int mTableNo,
                          int mUserId, double mOrderAmount, boolean mCurrent) {
        this.mOrderId = mOrderId;
        this.mOrderNo = mOrderNo;
        this.mOrderStatus = mOrderStatus;
        this.mTableNo = mTableNo;
        this.mUserId = mUserId;
        this.mOrderAmount = mOrderAmount;
        this.mCurrent = mCurrent;
    }

    public String getOrderId() {
        return mOrderId;
    }

    public void setOrderId(String mOrderId) {
        this.mOrderId = mOrderId;
    }

    public int getOrderNo() {
        return mOrderNo;
    }

    public void setOrderNo(int mOrderNo) {
        this.mOrderNo = mOrderNo;
    }

    public boolean isOrderStatus() {
        return mOrderStatus;
    }

    public void setOrderStatus(boolean mOrderStatus) {
        this.mOrderStatus = mOrderStatus;
    }

    public Date getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(Date mOrderDate) {
        this.mOrderDate = mOrderDate;
    }

    public Time getOrderTime() {
        return mOrderTime;
    }

    public void setOrderTime(Time mOrderTime) {
        this.mOrderTime = mOrderTime;
    }


    public int getTableNo() {
        return mTableNo;
    }

    public void setTableNo(int mTableNo) {
        this.mTableNo = mTableNo;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public double getOrderAmount() {
        return mOrderAmount;
    }

    public void setOrderAmount(double mOrderAmount) {
        this.mOrderAmount = mOrderAmount;
    }

    public int getItemCount() {
        return mItemCount;
    }

    public void setItemCount(int itemCount) {
        this.mItemCount = itemCount;
    }

    public List<OrderDetailsDTO> getOrderDetailsDTOs() {
        return orderDetailsDTOs;
    }

    public void setOrderDetailsDTOs(List<OrderDetailsDTO> orderDetailsDTOs) {
        this.orderDetailsDTOs = orderDetailsDTOs;
    }

    public boolean isCurrent() {
        return mCurrent;
    }

    public void setCurrent(boolean mCurrent) {
        this.mCurrent = mCurrent;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }
}
