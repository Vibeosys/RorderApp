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
    private String mCreatedDate;
    private String mUpdatedDate;

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




    public ArrayList<HotelTableDTO> filterByCategory(ArrayList<HotelTableDTO> hotelTables, int categoryId) {
        ArrayList<HotelTableDTO> sortedTable = new ArrayList<>();
        for (HotelTableDTO table : hotelTables) {

            if (table.getmTableCategoryId() == categoryId) {
                sortedTable.add(table);
                Log.i(TAG, "" + table.toString());
            }
        }
        return sortedTable;
    }
}
