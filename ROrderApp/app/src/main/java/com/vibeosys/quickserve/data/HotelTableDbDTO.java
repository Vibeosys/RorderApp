package com.vibeosys.quickserve.data;

import com.google.gson.Gson;

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
    private int isOccupied;

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


    public int isOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(int isOccupied) {
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
