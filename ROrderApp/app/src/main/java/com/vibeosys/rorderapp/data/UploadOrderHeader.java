package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 10-02-2016.
 */
public class UploadOrderHeader extends BaseDTO {

    private String orderId;
    private int tableId;
    private String custId;
    private ArrayList<UploadOrderDetails> orderDetails;

    public UploadOrderHeader(String orderId, int tableId, String custId, ArrayList<UploadOrderDetails> orderDetails) {
        this.orderId = orderId;
        this.tableId = tableId;
        this.custId = custId;
        this.orderDetails = orderDetails;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public ArrayList<UploadOrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(ArrayList<UploadOrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public static List<UploadOrderHeader> deserializeOrders(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<UploadOrderHeader> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            UploadOrderHeader deserializeObject = gson.fromJson(serializedString, UploadOrderHeader.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
