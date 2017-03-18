package com.vibeosys.quickserve.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 10-02-2016.
 */
public class UploadOrderDetails extends BaseDTO {
    private int menuId;
    private int orderQty;
    private String note;
    private int subMenuId;

    public UploadOrderDetails(int menuId, int orderQty, String note, int subMenuId) {
        this.menuId = menuId;
        this.orderQty = orderQty;
        this.note = note;
        this.subMenuId = subMenuId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
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

    public static List<UploadOrderDetails> deserializeOrders(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<UploadOrderDetails> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            UploadOrderDetails deserializeObject = gson.fromJson(serializedString, UploadOrderDetails.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
