package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 20-01-2016.
 */
public class HotelTableDbDTO {

    private int tableId;
    private int tableNo;
    private int tableCategoryId;
    private int capacity;
    private Date createdDate;
    private Date updatedDate;
    private boolean isOccupied;

    public HotelTableDbDTO() {
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public int getTableCategoryId() {
        return tableCategoryId;
    }

    public void setTableCategoryId(int tableCategoryId) {
        this.tableCategoryId = tableCategoryId;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public static List<HotelTableDbDTO> deserializeHotelTables(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<HotelTableDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            HotelTableDbDTO deserializeObject = gson.fromJson(serializedString, HotelTableDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
