package com.vibeosys.rorderapp.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 10-03-2016.
 */
public class TakeAwayDbDTO extends BaseDTO {

    private String takeawayId;
    private int takeawayNo;
    private double discount;
    private double deliveryCharges;
    private String custId;
    private int restaurantId;
    private int userId;
    private int sourceId;

    public TakeAwayDbDTO() {
    }

    public String getTakeawayId() {
        return takeawayId;
    }

    public void setTakeawayId(String takeawayId) {
        this.takeawayId = takeawayId;
    }

    public int getTakeawayNo() {
        return takeawayNo;
    }

    public void setTakeawayNo(int takeawayNo) {
        this.takeawayNo = takeawayNo;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public static List<TakeAwayDbDTO> deserializeTakeAway(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<TakeAwayDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                TakeAwayDbDTO deserializeObject = gson.fromJson(serializedString, TakeAwayDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
