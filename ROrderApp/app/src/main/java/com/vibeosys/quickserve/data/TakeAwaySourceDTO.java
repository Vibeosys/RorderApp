package com.vibeosys.quickserve.data;

/**
 * Created by akshay on 10-03-2016.
 */
public class TakeAwaySourceDTO {

    private int mTakeAwayId;
    private String mName;
    private String mImgUrl;
    private double mDiscount;

    public TakeAwaySourceDTO(int mTakeAwayId, String mName, String mImgUrl, double mDiscount) {
        this.mTakeAwayId = mTakeAwayId;
        this.mName = mName;
        this.mImgUrl = mImgUrl;
        this.mDiscount = mDiscount;
    }

    public int getTakeAwayId() {
        return mTakeAwayId;
    }

    public void setTakeAwayId(int mTakeAwayId) {
        this.mTakeAwayId = mTakeAwayId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String mImgUrl) {
        this.mImgUrl = mImgUrl;
    }

    public double getDiscount() {
        return mDiscount;
    }

    public void setDiscount(double mDiscount) {
        this.mDiscount = mDiscount;
    }
}
