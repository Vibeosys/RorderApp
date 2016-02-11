package com.vibeosys.rorderapp.data;

import java.util.Date;

/**
 * Created by shrinivas on 11-02-2016.
 */
public class ChefOrderDetailsDTO {




    private int mChefOrderId;
    private int mUserId;
    private String mUserName;
    private int mTableNo;
    private Date mplacedOrderDate;

    public ChefOrderDetailsDTO(int mChefOrderId, int mUserId, String mUserName, int mTableNo, Date mplacedOrderDate) {
        this.mChefOrderId = mChefOrderId;
        this.mUserId = mUserId;
        this.mUserName = mUserName;
        this.mTableNo = mTableNo;
        this.mplacedOrderDate = mplacedOrderDate;
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
}
