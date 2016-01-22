package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 22-01-2016.
 */
public class UserDTO {

    private int mUserId;
    private String mUserName;
    private String mPassword;
    private boolean mActive;
    private int mRoleId;
    private int mRestaurantId;

    public UserDTO(int mUserId, String mUserName, String mPassword, boolean mActive, int mRoleId, int mRestaurantId) {
        this.mUserId = mUserId;
        this.mUserName = mUserName;
        this.mPassword = mPassword;
        this.mActive = mActive;
        this.mRoleId = mRoleId;
        this.mRestaurantId = mRestaurantId;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public boolean ismActive() {
        return mActive;
    }

    public void setmActive(boolean mActive) {
        this.mActive = mActive;
    }

    public int getmRoleId() {
        return mRoleId;
    }

    public void setmRoleId(int mRoleId) {
        this.mRoleId = mRoleId;
    }

    public int getmRestaurantId() {
        return mRestaurantId;
    }

    public void setmRestaurantId(int mRestaurantId) {
        this.mRestaurantId = mRestaurantId;
    }
}
