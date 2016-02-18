package com.vibeosys.rorderapp.data;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

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
    private Date mplacedOrderDate;
    private Time morderTime;
    private ArrayList<ChefMenuDetailsDTO> mMenuChild;



    private boolean mOrderStatus;

    public ChefOrderDetailsDTO(int mChefOrderId, int mUserId, String mUserName, int mTableNo, Date mplacedOrderDate,Time morderTime,boolean mOrderStatus) {
        this.mChefOrderId = mChefOrderId;
        this.mUserId = mUserId;
        this.mUserName = mUserName;
        this.mTableNo = mTableNo;
        this.mplacedOrderDate = mplacedOrderDate;
        this.morderTime = morderTime;
        this.mOrderStatus =mOrderStatus;
    }
    public ChefOrderDetailsDTO(String mNewOrderId,  int mTableNo,String mUserName,int mOrderNumner,int mNewOrderStatus ) {
        this.mNewOrderId =mNewOrderId;
        this.mTableNo = mTableNo;
        this.mUserName =mUserName;
        this.mOrderNumner = mOrderNumner;
        this.mNewOrderStatus =mNewOrderStatus;

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

    public Date getMplacedOrderDate() {
        return mplacedOrderDate;
    }

    public void setMplacedOrderDate(Date mplacedOrderDate) {
        this.mplacedOrderDate = mplacedOrderDate;
    }
    public Time getMorderTime() {
        return morderTime;
    }

    public void setMorderTime(Time morderTime) {
        this.morderTime = morderTime;
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

}
