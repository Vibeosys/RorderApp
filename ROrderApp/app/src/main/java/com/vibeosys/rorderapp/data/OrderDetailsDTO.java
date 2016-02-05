package com.vibeosys.rorderapp.data;

import java.sql.Date;

/**
 * Created by akshay on 04-02-2016.
 */
public class OrderDetailsDTO {
    private int mOrderDetailsId;
    private double mOrderPrice;
    private int mOrderQuantity;
    private Date mCreatedDate;
    private Date mUpdatedDate;
    private String mOrderId;
    private int mMenuId;
    private double mMenuUnitPrice;
    private String mMenuTitle;

    public OrderDetailsDTO(int mOrderDetailsId, double mOrderPrice, int mOrderQuantity,
                           Date mCreatedDate, Date mUpdatedDate, String mOrderId, int mMenuId,
                           String mMenuTitle,double mMenuUnitPrice) {
        this.mOrderDetailsId = mOrderDetailsId;
        this.mOrderPrice = mOrderPrice;
        this.mOrderQuantity = mOrderQuantity;
        this.mCreatedDate = mCreatedDate;
        this.mUpdatedDate = mUpdatedDate;
        this.mOrderId = mOrderId;
        this.mMenuId = mMenuId;
        this.mMenuTitle = mMenuTitle;
        this.mMenuUnitPrice=mMenuUnitPrice;
    }

    public int getOrderDetailsId() {
        return mOrderDetailsId;
    }

    public void setOrderDetailsId(int mOrderDetailsId) {
        this.mOrderDetailsId = mOrderDetailsId;
    }

    public double getOrderPrice() {
        return mOrderPrice;
    }

    public void setOrderPrice(double mOrderPrice) {
        this.mOrderPrice = mOrderPrice;
    }

    public int getOrderQuantity() {
        return mOrderQuantity;
    }

    public void setOrderQuantity(int mOrderQuantity) {
        this.mOrderQuantity = mOrderQuantity;
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

    public String getOrderId() {
        return mOrderId;
    }

    public void setOrderId(String mOrderId) {
        this.mOrderId = mOrderId;
    }

    public int getMenuId() {
        return mMenuId;
    }

    public void setMenuId(int mMenuId) {
        this.mMenuId = mMenuId;
    }

    public String getMenuTitle() {
        return mMenuTitle;
    }

    public void setMenuTitle(String mMenuTitle) {
        this.mMenuTitle = mMenuTitle;
    }

    public double getMenuUnitPrice() {
        return mMenuUnitPrice;
    }

    public void setMenuUnitPrice(double mMenuUnitPrice) {
        this.mMenuUnitPrice = mMenuUnitPrice;
    }
}
