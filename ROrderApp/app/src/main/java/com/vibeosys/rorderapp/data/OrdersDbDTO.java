package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by akshay on 23-01-2016.
 */
public class OrdersDbDTO extends BaseDTO {

    private String orderId;
    private int orderNo;
    private String custId;
    private int orderStatus;
    private Date orderDt;
    private String orderDate;
    private Time orderTm;
    private String orderTime;
    //private Date createdDate;
    //private Date updatedDate;
    private int tableId;
    private int userId;
    private double orderAmt;
    private int restaurantId;

    public OrdersDbDTO() {
    }


    public OrdersDbDTO(String orderId, int orderNo, String custId, Date orderDt, Time orderTm,
                       int tableId, int userId) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.custId = custId;
        this.orderDt = orderDt;
        this.orderTm = orderTm;
        //this.createdDate = createdDate;
        //this.updatedDate = updatedDate;
        this.tableId = tableId;
        this.userId = userId;
    }

    public OrdersDbDTO(String orderId, int orderNo, int orderStatus, Date orderDt,
                       Time orderTm, Date createdDate, Date updatedDate, int tableId,
                       int userId, double orderAmt) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.orderStatus = orderStatus;
        this.orderDt = orderDt;
        this.orderTm = orderTm;
        //this.createdDate = createdDate;
        //this.updatedDate = updatedDate;
        this.tableId = tableId;
        this.userId = userId;
        this.orderAmt = orderAmt;
    }

    public OrdersDbDTO(String orderId, int orderNo, String custId, int orderStatus, int tableId, int userId) {
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.custId = custId;
        this.orderStatus = orderStatus;
        this.tableId = tableId;
        this.userId = userId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int isOrderStatus() {
        return orderStatus;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Date getOrderDt() {
        if(this.orderDate == null || this.orderDate.isEmpty())
            return this.orderDt;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        //formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        java.util.Date value = null;
        try {
            value = formatter.parse(this.orderDate);
            orderDt = new Date(value.getTime());
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(value);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return orderDt;
    }

    /*public void setOrderDt(Date orderDt) {
        this.orderDt = orderDt;
    }*/
    public Time getOrderTm() {
        if(this.orderTime == null || this.orderTime.isEmpty())
            return this.orderTm;


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        java.util.Date value = null;
        try {
            value = formatter.parse(this.orderTime);
            orderTm = new Time(value.getTime());
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(value);

        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return orderTm;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    /*public Date getCreatedDate() {
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
    }*/

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

    public double getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(double orderAmt) {
        this.orderAmt = orderAmt;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public static List<OrdersDbDTO> deserializeOrders(List<String> serializedStringList) {
        Gson gson = new Gson();
        //gson.fromJson(serializedStringList,)
        ArrayList<OrdersDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            OrdersDbDTO deserializeObject = gson.fromJson(serializedString, OrdersDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
