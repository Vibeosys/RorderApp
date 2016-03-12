package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 11-03-2016.
 */
public class UploadTakeAway {

    private String takeawayId;
    private double discount;
    private double deliveryCharges;
    private String custId;
    private int sourceId;

    public UploadTakeAway(String takeawayId, double discount, double deliveryCharges, String custId, int sourceId) {
        this.takeawayId = takeawayId;
        this.discount = discount;
        this.deliveryCharges = deliveryCharges;
        this.custId = custId;
        this.sourceId = sourceId;
    }

    public String getTakeawayId() {
        return takeawayId;
    }

    public void setTakeawayId(String takeawayId) {
        this.takeawayId = takeawayId;
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

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }
}
