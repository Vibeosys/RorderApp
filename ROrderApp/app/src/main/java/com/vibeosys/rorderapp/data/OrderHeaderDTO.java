package com.vibeosys.rorderapp.data;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 * Created by akshay on 04-02-2016.
 */
public class OrderHeaderDTO {
    private int mOrderId;
    private int mOrderNo;
    private boolean mOrderStatus;
    private Date mOrderDate;
    private Time mOrderTime;
    private Date mCreatedDate;
    private Date mUpdatedDate;
    private int mTableNo;
    private int mUserId;
    private double mOrderAmount;
    private int mItemCount;

    private List<OrderDetailsDTO> orderDetailsDTOs;

    public OrderHeaderDTO(int mOrderId, int mOrderNo, boolean mOrderStatus, Date mOrderDate,
                          Time mOrderTime, Date mCreatedDate, Date mUpdatedDate, int mTableNo,
                          int mUserId, double mOrderAmount) {
        this.mOrderId = mOrderId;
        this.mOrderNo = mOrderNo;
        this.mOrderStatus = mOrderStatus;
        this.mOrderDate = mOrderDate;
        this.mOrderTime = mOrderTime;
        this.mCreatedDate = mCreatedDate;
        this.mUpdatedDate = mUpdatedDate;
        this.mTableNo = mTableNo;
        this.mUserId = mUserId;
        this.mOrderAmount = mOrderAmount;
    }

    public int getOrderId() {
        return mOrderId;
    }

    public void setOrderId(int mOrderId) {
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

    public Date getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(Date mCreatedDate) {
        this.mCreatedDate = mCreatedDate;
    }

    public Date getUpdatedDate() {
        return mUpdatedDate;
    }

    public void setUpdatedDate(Date mUpdatedDate) {
        this.mUpdatedDate = mUpdatedDate;
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
}
