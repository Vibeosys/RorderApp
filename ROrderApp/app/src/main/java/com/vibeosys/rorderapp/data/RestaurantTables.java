package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 11-02-2016.
 */
public class RestaurantTables extends HotelTableDTO {

    private String mUserName;

    public RestaurantTables(int tableId, int tableNo, int tableCategoryId, String tableCategoryName,
                            int capacity, boolean isOccupied, String userName) {
        super(tableId, tableNo, tableCategoryId, tableCategoryName, capacity, isOccupied);
        this.mUserName = userName;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        this.mUserName = userName;
    }
}
