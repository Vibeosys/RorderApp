package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 02-02-2016.
 */
public class CustomerDTO {

    private int mCustomerId;
    private String mCustomerName;
    private long mSessionId;

    public CustomerDTO() {
    }

    public CustomerDTO(int mCustomerId, String mCustomerName, long mSessionId) {
        this.mCustomerId = mCustomerId;
        this.mCustomerName = mCustomerName;
        this.mSessionId = mSessionId;
    }

    public int getmCustomerId() {
        return mCustomerId;
    }

    public void setmCustomerId(int mCustomerId) {
        this.mCustomerId = mCustomerId;
    }

    public String getmCustomerName() {
        return mCustomerName;
    }

    public void setmCustomerName(String mCustomerName) {
        this.mCustomerName = mCustomerName;
    }

    public long getmSessionId() {
        return mSessionId;
    }

    public void setmSessionId(long mSessionId) {
        this.mSessionId = mSessionId;
    }
}
