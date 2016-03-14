package com.vibeosys.rorderapp.data;

import android.util.Log;

import com.google.gson.Gson;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by akshay on 10-03-2016.
 */
public class TakeAwayDbDTO extends BaseDTO {

    private String takeawayId;
    private int takeawayNo;
    private double discount;
    private double deliveryCharges;
    private String custId;
    private int userId;
    private int sourceId;
    private String createdDate;
    private Date createdDt;

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

    public Date getOrderDt() {
        if (this.createdDate == null || this.createdDate.isEmpty())
            return this.createdDt;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        //formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        java.util.Date value = null;
        try {
            value = formatter.parse(this.createdDate);
            createdDt = new Date(value.getTime());
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTime(value);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return createdDt;
    }
}
