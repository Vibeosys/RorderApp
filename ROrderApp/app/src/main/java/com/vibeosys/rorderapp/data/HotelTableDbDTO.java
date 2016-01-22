package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 20-01-2016.
 */
public class HotelTableDbDTO {

    private int tableId;
    private int tableNo;
    private int tableCategoryId;
    private int capacity;
    private String createdDate;
    private String updatedDate;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(boolean isOccupied) {
        this.isOccupied = isOccupied;
    }
}
