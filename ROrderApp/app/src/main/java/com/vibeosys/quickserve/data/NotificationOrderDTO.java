package com.vibeosys.quickserve.data;

/**
 * Created by akshay on 16-02-2016.
 */
public class NotificationOrderDTO {

    private int mOrderNo;
    private int mUserId;
    private int mStatus;
    private String mMessage;
    private int mTableNo;

    public NotificationOrderDTO(int orderNo, int userid, int status, String message, int tableNo) {
        this.mOrderNo = orderNo;
        this.mUserId = userid;
        this.mStatus = status;
        this.mMessage = message;
        this.mTableNo = tableNo;
    }

    public int getOrderNo() {
        return mOrderNo;
    }

    public void setOrderNo(int orderNo) {
        this.mOrderNo = orderNo;
    }

    public int getUserid() {
        return mUserId;
    }

    public void setUserid(int userid) {
        this.mUserId = userid;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        this.mStatus = status;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        this.mMessage = message;
    }

    public int getTableNo() {
        return mTableNo;
    }

    public void setTableNo(int tableNo) {
        this.mTableNo = tableNo;
    }
}
