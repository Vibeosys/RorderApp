package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 23-01-2016.
 */
public class OrdersDbDTO extends BaseDTO {

    private String orderId;
    private int OrderNo;
    private String custId;
    private boolean OrderStatus;
    private Date orderDate;
    private Time orderTime;
    private Date createdDate;
    private Date updatedDate;
    private int tableNo;
    private String userId;
    private double orderAmount;

    public OrdersDbDTO() {
    }

    public OrdersDbDTO(String orderId, int orderNo, boolean orderStatus, Date orderDate,
                       Time orderTime, Date createdDate, Date updatedDate, int tableNo,
                       String userId, double orderAmount) {
        this.orderId = orderId;
        OrderNo = orderNo;
        OrderStatus = orderStatus;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.tableNo = tableNo;
        this.userId = userId;
        this.orderAmount = orderAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(int orderNo) {
        OrderNo = orderNo;
    }

    public boolean isOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(boolean orderStatus) {
        OrderStatus = orderStatus;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Time getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Time orderTime) {
        this.orderTime = orderTime;
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

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public static List<OrdersDbDTO> deserializeOrders(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<OrdersDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            OrdersDbDTO deserializeObject = gson.fromJson(serializedString, OrdersDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
