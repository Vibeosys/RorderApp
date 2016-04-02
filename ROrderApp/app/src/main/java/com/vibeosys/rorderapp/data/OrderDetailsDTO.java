package com.vibeosys.rorderapp.data;

import java.sql.Date;

/**
 * Created by akshay on 04-02-2016.
 */
public class OrderDetailsDTO {
    private int mOrderDetailsId;
    private double mOrderPrice;
    private int mOrderQuantity;
    private String mOrderId;
    private int mMenuId;
    private double mMenuUnitPrice;
    private String mMenuTitle;
    private String mNote;
    private int mRoomId;
    private int mSubMenuId;


    public OrderDetailsDTO(int mOrderDetailsId, double mOrderPrice, int mOrderQuantity, String mOrderId, int mMenuId,
                           String mMenuTitle, double mMenuUnitPrice, String mNote) {
        this.mOrderDetailsId = mOrderDetailsId;
        this.mOrderPrice = mOrderPrice;
        this.mOrderQuantity = mOrderQuantity;
        this.mOrderId = mOrderId;
        this.mMenuId = mMenuId;
        this.mMenuTitle = mMenuTitle;
        this.mMenuUnitPrice = mMenuUnitPrice;
        this.mNote = mNote;
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

    public String getmNote() {
        return mNote;
    }

    public void setmNote(String mNote) {
        this.mNote = mNote;
    }

    public int getRoomId() {
        return mRoomId;
    }

    public void setRoomId(int mRoomId) {
        this.mRoomId = mRoomId;
    }

    public int getmSubMenuId() {
        return mSubMenuId;
    }

    public void setmSubMenuId(int mSubMenuId) {
        this.mSubMenuId = mSubMenuId;
    }
}
