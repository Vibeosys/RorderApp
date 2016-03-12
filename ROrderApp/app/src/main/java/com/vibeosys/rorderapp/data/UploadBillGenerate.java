package com.vibeosys.rorderapp.data;

/**
 * Created by shrinivas on 11-02-2016.
 */
public class UploadBillGenerate extends BaseDTO {

    String custId;
    int tableId;
    int takeawayNo;

    public UploadBillGenerate(int tableId, String custId, int takeawayNo) {
        this.custId = custId;
        this.tableId = tableId;
        this.takeawayNo = takeawayNo;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getTakeawayNo() {
        return takeawayNo;
    }

    public void setTakeawayNo(int takeawayNo) {
        this.takeawayNo = takeawayNo;
    }
}
