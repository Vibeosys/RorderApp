package com.vibeosys.quickserve.data;

/**
 * Created by akshay on 05-04-2016.
 */
public class DeliveryDTO {

    private String mDeliveryId;
    private int mDeliveryNo;
    private double mDiscount;
    private double mDeliveryCharges;
    private String mCustId;
    private int mUserId;
    private int mSourceId;
    private String mCustName;
    private String mCustAddress;
    private String userName;
    private String custPhone;
    private String sourceName;
    private int orderStatus;

    public DeliveryDTO(String mDeliveryId, int mDeliveryNo, double mDiscount, double mDeliveryCharges,
                       String mCustId, int mUserId, int mSourceId, String mCustName, String mCustAddress,
                       String userName, String custPhone, String sourceName) {
        this.mDeliveryId = mDeliveryId;
        this.mDeliveryNo = mDeliveryNo;
        this.mDiscount = mDiscount;
        this.mDeliveryCharges = mDeliveryCharges;
        this.mCustId = mCustId;
        this.mUserId = mUserId;
        this.mSourceId = mSourceId;
        this.mCustName = mCustName;
        this.mCustAddress = mCustAddress;
        this.userName = userName;
        this.custPhone = custPhone;
        this.sourceName = sourceName;
        this.orderStatus = orderStatus;
    }

    public String getmDeliveryId() {
        return mDeliveryId;
    }

    public void setmDeliveryId(String mDeliveryId) {
        this.mDeliveryId = mDeliveryId;
    }

    public int getmDeliveryNo() {
        return mDeliveryNo;
    }

    public void setmDeliveryNo(int mDeliveryNo) {
        this.mDeliveryNo = mDeliveryNo;
    }

    public double getmDiscount() {
        return mDiscount;
    }

    public void setmDiscount(double mDiscount) {
        this.mDiscount = mDiscount;
    }

    public double getmDeliveryCharges() {
        return mDeliveryCharges;
    }

    public void setmDeliveryCharges(double mDeliveryCharges) {
        this.mDeliveryCharges = mDeliveryCharges;
    }

    public String getmCustId() {
        return mCustId;
    }

    public void setmCustId(String mCustId) {
        this.mCustId = mCustId;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public int getmSourceId() {
        return mSourceId;
    }

    public void setmSourceId(int mSourceId) {
        this.mSourceId = mSourceId;
    }

    public String getmCustName() {
        return mCustName;
    }

    public void setmCustName(String mCustName) {
        this.mCustName = mCustName;
    }

    public String getmCustAddress() {
        return mCustAddress;
    }

    public void setmCustAddress(String mCustAddress) {
        this.mCustAddress = mCustAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }
}
