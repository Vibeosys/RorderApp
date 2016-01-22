package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 22-01-2016.
 */
public class UserDbDTO extends BaseDTO {

    private int userId;
    private String userName;
    private String password;
    private boolean active;
    private int roleId;
    private int restaurantId;

    public UserDbDTO() {
    }

    public UserDbDTO(int userId, String userName, boolean active, int roleId, int restaurantId,String password) {
        this.userId = userId;
        this.userName = userName;
        this.active = active;
        this.roleId = roleId;
        this.restaurantId = restaurantId;
        this.password=password;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isActive() {
        return active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}