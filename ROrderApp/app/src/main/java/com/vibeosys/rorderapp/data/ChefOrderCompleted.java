package com.vibeosys.rorderapp.data;

/**
 * Created by shrinivas on 13-02-2016.
 */
public class ChefOrderCompleted extends BaseDTO {


    private String orderId;

    public ChefOrderCompleted(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
