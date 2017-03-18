package com.vibeosys.quickserve.data;

/**
 * Created by akshay on 13-02-2016.
 */
public class PaymentModeDbDTO extends BaseDTO {

    private int paymentModeId;
    private String paymentModeTitle;
    private boolean active;

    public PaymentModeDbDTO(int paymentModeId, String paymentModeTitle, boolean active) {
        this.paymentModeId = paymentModeId;
        this.paymentModeTitle = paymentModeTitle;
        this.active = active;
    }

    public int getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(int paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public String getPaymentModeTitle() {
        return paymentModeTitle;
    }

    public void setPaymentModeTitle(String paymentModeTitle) {
        this.paymentModeTitle = paymentModeTitle;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
