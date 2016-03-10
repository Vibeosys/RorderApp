package com.vibeosys.rorderapp.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 10-03-2016.
 */
public class OrderTypeDbDTO extends BaseDTO {

    private int orderTypeId;
    private String orderTypeTitle;
    private int active;

    public OrderTypeDbDTO() {
    }

    public int getOrderTypeId() {
        return orderTypeId;
    }

    public void setOrderTypeId(int orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    public String getOrderTypeTitle() {
        return orderTypeTitle;
    }

    public void setOrderTypeTitle(String orderTypeTitle) {
        this.orderTypeTitle = orderTypeTitle;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public static List<OrderTypeDbDTO> deserializeOrderType(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<OrderTypeDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                OrderTypeDbDTO deserializeObject = gson.fromJson(serializedString, OrderTypeDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
