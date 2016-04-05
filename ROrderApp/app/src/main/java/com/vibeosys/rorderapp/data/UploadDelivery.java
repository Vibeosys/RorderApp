package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 05-04-2016.
 */
public class UploadDelivery {

    private String deliveryId;
    private double discount;
    private double deliveryCharges;
    private String custId;
    private int sourceId;

    public UploadDelivery(String deliveryId, double discount, double deliveryCharges, String custId, int sourceId) {
        this.deliveryId = deliveryId;
        this.discount = discount;
        this.deliveryCharges = deliveryCharges;
        this.custId = custId;
        this.sourceId = sourceId;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
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
