package com.vibeosys.quickserve.data;

/**
 * Created by akshay on 20-01-2016.
 */
public class HotelTableDTO {

    private int mTableId;
    private int mTableNo;
    private int mTableCategoryId;
    private String mTableCategoryName;
    private int mCapacity;
    private boolean mIsOccupied;

    public HotelTableDTO() {
    }


    public HotelTableDTO(int tableId, int tableNo, int tableCategoryId,
                         String tableCategoryName, int capacity, boolean isOccupied) {
        this.mTableId = tableId;
        this.mTableNo = tableNo;
        this.mTableCategoryId = tableCategoryId;
        this.mTableCategoryName = tableCategoryName;
        this.mCapacity = capacity;
        this.mIsOccupied = isOccupied;
    }


    public int getmTableId() {
        return mTableId;
    }

    public void setmTableId(int mTableId) {
        this.mTableId = mTableId;
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


    public boolean ismIsOccupied() {
        return mIsOccupied;
    }

    public void setmIsOccupied(boolean mIsOccupied) {
        this.mIsOccupied = mIsOccupied;
    }
    public String getmTableCategoryName() {
        return mTableCategoryName;
    }

    public void setmTableCategoryName(String mTableCategoryName) {
        this.mTableCategoryName = mTableCategoryName;
    }
}
