package com.vibeosys.rorderapp.data;

/**
 * Created by shrinivas on 11-02-2016.
 */
public class UploadBillGenerate extends BaseDTO {

    String custId;
    int tableId;
    public UploadBillGenerate( int tableId,String custId) {
        this.custId = custId;
        this.tableId = tableId;
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


}
