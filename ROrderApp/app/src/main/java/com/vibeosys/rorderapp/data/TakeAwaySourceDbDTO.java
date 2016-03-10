package com.vibeosys.rorderapp.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 10-03-2016.
 */
public class TakeAwaySourceDbDTO extends BaseDTO {

    private int sourceId;
    private String sourceName;
    private String sourceImg;
    private double discount;
    private int active;

    public TakeAwaySourceDbDTO() {
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getSourceImg() {
        return sourceImg;
    }

    public void setSourceImg(String sourceImg) {
        this.sourceImg = sourceImg;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public static List<TakeAwaySourceDbDTO> deserializeTakeSource(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<TakeAwaySourceDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                TakeAwaySourceDbDTO deserializeObject = gson.fromJson(serializedString, TakeAwaySourceDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }

}
