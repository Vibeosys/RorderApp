package com.vibeosys.quickserve.data;

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
    private int takeawayNo;
    private int deliveryNo;
    private int orderType;
    private ArrayList<UploadOrderDetails> orderDetails;

    public UploadOrderHeader(String orderId, int tableId, String custId, ArrayList<UploadOrderDetails>
            orderDetails, int takeawayNo, int orderType, int deliveryNo) {
        this.orderId = orderId;
        this.tableId = tableId;
        this.custId = custId;
        this.orderDetails = orderDetails;
        this.takeawayNo = takeawayNo;
        this.orderType = orderType;
        this.deliveryNo = deliveryNo;
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

    public int getTakeawayNo() {
        return takeawayNo;
    }

    public void setTakeawayNo(int takeawayNo) {
        this.takeawayNo = takeawayNo;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(int deliveryNo) {
        this.deliveryNo = deliveryNo;
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
