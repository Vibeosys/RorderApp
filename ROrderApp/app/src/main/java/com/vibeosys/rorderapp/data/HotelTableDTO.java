package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 20-01-2016.
 */
public class HotelTableDTO {

    private int mTableNo;
    private int mTableCategoryId;
    private int mCapacity;
    private String mCreatedDate;
    private String mUpdatedDate;
    private boolean mIsOccupied;

    public HotelTableDTO() {
    }

    public int getmTableNo() {
        return mTableNo;
    }

    public void setmTableNo(int mTableNo) {
        this.mTableNo = mTableNo;
    }

    public int getmTableCategoryId() {
        return mTableCategoryId;
    }

    public void setmTableCategoryId(int mTableCategoryId) {
        this.mTableCategoryId = mTableCategoryId;
    }

    public int getmCapacity() {
        return mCapacity;
    }

    public void setmCapacity(int mCapacity) {
        this.mCapacity = mCapacity;
    }

    public String getmCreatedDate() {
        return mCreatedDate;
    }

    public void setmCreatedDate(String mCreatedDate) {
        this.mCreatedDate = mCreatedDate;
    }

    public String getmUpdatedDate() {
        return mUpdatedDate;
    }

    public void setmUpdatedDate(String mUpdatedDate) {
        this.mUpdatedDate = mUpdatedDate;
    }

    public boolean ismIsOccupied() {
        return mIsOccupied;
    }

    public void setmIsOccupied(boolean mIsOccupied) {
        this.mIsOccupied = mIsOccupied;
    }
}