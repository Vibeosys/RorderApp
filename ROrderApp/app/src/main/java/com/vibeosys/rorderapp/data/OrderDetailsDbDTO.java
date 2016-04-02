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
    private String orderId;
    private int menuId;
    private int subMenuId;
    private String menuTitle;
    private String note;

    public OrderDetailsDbDTO() {
    }

    public OrderDetailsDbDTO(int orderDetailsId, double orderPrice, int orderQuantity,
                             String orderId, int menuId,
                             String menuTitle, String note) {
        this.orderDetailsId = orderDetailsId;
        this.orderPrice = orderPrice;
        this.orderQuantity = orderQuantity;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSubMenuId() {
        return subMenuId;
    }

    public void setSubMenuId(int subMenuId) {
        this.subMenuId = subMenuId;
    }

    public static List<OrderDetailsDbDTO> deserializeOrderDetail(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<OrderDetailsDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            OrderDetailsDbDTO deserializeObject = gson.fromJson(serializedString, OrderDetailsDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
