package com.vibeosys.quickserve.data;

/**
 * Created by akshay on 11-02-2016.
 */
public class RestaurantTables extends HotelTableDTO {

    private String mUserName;
    private int mUserId;

    public RestaurantTables(int tableId, int tableNo, int tableCategoryId, String tableCategoryName,
                            int capacity, boolean isOccupied, String userName,int userId) {
        super(tableId, tableNo, tableCategoryId, tableCategoryName, capacity, isOccupied);
        this.mUserName = userName;
        this.mUserId=userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }
}
