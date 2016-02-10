package com.vibeosys.rorderapp.data;

/**
 * Created by mahesh on 10/23/2015.
 */
public class UploadUser extends BaseDTO {
    protected int userId;
    protected String emailId;
    protected String userName;
    protected String password;
    protected int restaurantId;

    public UploadUser() {

    }

    public UploadUser(int userId, int restaurantId) {
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public UploadUser(int userId, int restaurantId, String password) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }
}
