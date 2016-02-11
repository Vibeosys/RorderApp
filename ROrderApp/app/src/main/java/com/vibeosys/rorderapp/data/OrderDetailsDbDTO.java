package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 23-01-2016.
 */
public class OrderDetailsDbDTO extends BaseDTO {

    private int orderDetailsId;
    private double orderPrice;
    private int orderQuantity;
    private Date createdDate;
    private Date updatedDate;
    private String orderId;
    private int menuId;
    private String menuTitle;

    public OrderDetailsDbDTO() {
    }

    public OrderDetailsDbDTO(int orderDetailsId, double orderPrice, int orderQuantity,
                             Date createdDate, Date updatedDate, String orderId, int menuId,
                             String menuTitle) {
        this.orderDetailsId = orderDetailsId;
        this.orderPrice = orderPrice;
        this.orderQuantity = orderQuantity;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.orderId = orderId;
        this.menuId = menuId;
        this.menuTitle = menuTitle;
    }

    public int getOrderDetailsId() {
        return orderDetailsId;
    }

    public void setOrderDetailsId(int orderDetailsId) {
        this.orderDetailsId = orderDetailsId;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public static List<OrderDetailsDbDTO> deserializeOrderDetail(List<String> serializedStringList) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
        ArrayList<OrderDetailsDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            OrderDetailsDbDTO deserializeObject = gson.fromJson(serializedString, OrderDetailsDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
