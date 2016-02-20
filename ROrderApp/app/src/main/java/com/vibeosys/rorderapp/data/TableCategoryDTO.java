package com.vibeosys.rorderapp.data;

import android.util.Log;

import java.util.ArrayList;

/**
 * This is use in internal data operations.
 * Created by akshay on 20-01-2016.
 */
public class TableCategoryDTO {

    private final String TAG = TableCategoryDTO.class.getSimpleName();
    private int mCategoryId;
    private String mTitle;
    private String mImage;
    private boolean mSelected;

    public TableCategoryDTO() {
    }

    public int getmCategoryId() {
        return mCategoryId;
    }

    public void setmCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }

    public ArrayList<RestaurantTables> filterTable(ArrayList<RestaurantTables> hotelTables, int categoryId) {
        ArrayList<RestaurantTables> sortedTable = new ArrayList<>();
        for (RestaurantTables table : hotelTables) {

            if (table.getmTableCategoryId() == categoryId) {
                sortedTable.add(table);
                Log.i(TAG, "" + table.toString());
            }
        }
        return sortedTable;
    }
    public ArrayList<RestaurantTables> filterTable(ArrayList<RestaurantTables> hotelTables, int categoryId,boolean unOccupied) {
        ArrayList<RestaurantTables> sortedTable = new ArrayList<>();
        for (RestaurantTables table : hotelTables) {

            if(table.ismIsOccupied()!=unOccupied){
                if (table.getmTableCategoryId() == categoryId) {
                    sortedTable.add(table);
                    Log.i(TAG, "" + table.toString());
                }
            }

        }
        return sortedTable;
    }
    public ArrayList<RestaurantTables> filterTable(ArrayList<RestaurantTables> hotelTables,boolean unOccupied) {
        ArrayList<RestaurantTables> sortedTable = new ArrayList<>();
        for (RestaurantTables table : hotelTables) {

            if(table.ismIsOccupied()!=unOccupied) {
                sortedTable.add(table);
                Log.i(TAG, "" + table.toString());
            }

        }
        return sortedTable;
    }
}
