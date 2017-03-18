package com.vibeosys.quickserve.data;

/**
 * Created by akshay on 13-02-2016.
 */
public class PaymentModeDTO {

    private int mPaymentModeId;
    private String mPaymentModeTitle;
    private boolean mActive;

    public PaymentModeDTO(int paymentModeId, String paymentModeTitle, boolean active) {
        this.mPaymentModeId = paymentModeId;
        this.mPaymentModeTitle = paymentModeTitle;
        this.mActive = active;
    }

    public int getPaymentModeId() {
        return mPaymentModeId;
    }

    public void setPaymentModeId(int mPaymentModeId) {
        this.mPaymentModeId = mPaymentModeId;
    }

    public String getPaymentModeTitle() {
        return mPaymentModeTitle;
    }

    public void setPaymentModeTitle(String mPaymentModeTitle) {
        this.mPaymentModeTitle = mPaymentModeTitle;
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean mActive) {
        this.mActive = mActive;
    }
}
