package com.vibeosys.rorderapp.data;

import android.util.Log;

import com.google.gson.Gson;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 05-04-2016.
 */
public class DeliveryDbDTO extends BaseDTO {

    private String deliveryId;
    private int deliveryNo;
    private double discount;
    private double deliveryCharges;
    private String custId;
    private int userId;
    private int sourceId;
    private String createdDate;
    private Date createdDt;

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(int deliveryNo) {
        this.deliveryNo = deliveryNo;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCreatedDt() {
        return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
        this.createdDt = createdDt;
    }

    public static List<DeliveryDbDTO> deserializeDelivery(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<DeliveryDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                DeliveryDbDTO deserializeObject = gson.fromJson(serializedString, DeliveryDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
