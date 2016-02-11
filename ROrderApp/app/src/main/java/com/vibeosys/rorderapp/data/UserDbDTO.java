package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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
    private Date createdDate;
    private Date updatedDate;

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public static List<UserDbDTO> deserializeUser(List<String> serializedStringList) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
        ArrayList<UserDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            UserDbDTO deserializeObject = gson.fromJson(serializedString, UserDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }

}
