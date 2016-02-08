package com.vibeosys.rorderapp.data;

import java.sql.Date;

/**
 * Created by akshay on 08-02-2016.
 */
public class TableTransactionDbDTO extends BaseDTO {

    private int tableId;
    private String userId;
    private String custId;
    private boolean isWaiting;
    private Date arrivalTime;
    private int occupancy;

    public TableTransactionDbDTO() {
    }

    public TableTransactionDbDTO(int tableId, String userId, String custId, boolean isWaiting, Date arrivalTime, int occupancy) {
        this.tableId = tableId;
        this.userId = userId;
        this.custId = custId;
        this.isWaiting = isWaiting;
        this.arrivalTime = arrivalTime;
        this.occupancy = occupancy;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public boolean isWaiting() {
        return isWaiting;
    }

    public void setIsWaiting(boolean isWaiting) {
        this.isWaiting = isWaiting;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getOccupancy() {
        return occupancy;
    }

    public void setOccupancy(int occupancy) {
        this.occupancy = occupancy;
    }
}