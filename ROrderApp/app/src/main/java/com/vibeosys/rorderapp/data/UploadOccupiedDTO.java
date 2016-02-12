package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 12-02-2016.
 */
public class UploadOccupiedDTO {

    private int tableId;
    private int isOccupied;

    public UploadOccupiedDTO(int tableId, int isOccupied) {
        this.tableId = tableId;
        this.isOccupied = isOccupied;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(int isOccupied) {
        this.isOccupied = isOccupied;
    }
}
