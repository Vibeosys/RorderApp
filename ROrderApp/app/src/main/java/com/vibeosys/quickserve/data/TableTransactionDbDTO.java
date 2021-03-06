package com.vibeosys.quickserve.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 08-02-2016.
 */
public class TableTransactionDbDTO extends BaseDTO {

    private int tableId;
    private int userId;
    private String custId;
    private int isWaiting;
    private String arrivalTime;
    private int occupancy;

    public TableTransactionDbDTO() {
    }

    public TableTransactionDbDTO(int tableId, String custId) {
        this.tableId = tableId;
        this.custId = custId;
    }

    public TableTransactionDbDTO(String custId, int isWaiting, int occupancy) {
        this.custId = custId;
        this.isWaiting = isWaiting;
        this.occupancy = occupancy;
    }

    public TableTransactionDbDTO(String custId, int isWaiting, String arrivalTime, int occupancy) {
        this.custId = custId;
        this.isWaiting = isWaiting;
        this.arrivalTime = arrivalTime;
        this.occupancy = occupancy;
    }

    public TableTransactionDbDTO(int tableId, int userId, String custId, int isWaiting, String arrivalTime, int occupancy) {
        this.tableId = tableId;
        this.userId = userId;
        this.custId = custId;
        this.isWaiting = isWaiting;
        this.arrivalTime = arrivalTime;
        this.occupancy = occupancy;
    }

    public TableTransactionDbDTO(int tableId, int userId, String custId, int isWaiting, String arrivalTime) {
        this.tableId = tableId;
        this.userId = userId;
        this.custId = custId;
        this.isWaiting = isWaiting;
        this.arrivalTime = arrivalTime;

    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public int isWaiting() {
        return isWaiting;
    }

    public void setIsWaiting(int isWaiting) {
        this.isWaiting = isWaiting;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }

    public static List<TableTransactionDbDTO> deserializeTableTransaction(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<TableTransactionDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                TableTransactionDbDTO deserializeObject = gson.fromJson(serializedString, TableTransactionDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.e("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
