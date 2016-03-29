package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 23-01-2016.
 */
public class MenuDbDTO extends BaseDTO {

    private int menuId;
    private String menuTitle;
    private String image;
    private double price;
    private String ingredients;
    private String tags;
    private int availabilityStatus;
    private int active;
    private int foodType;
    private int isSpicy;
    private int categoryId;
    private int roomId;
    private int fbTypeId;

    public MenuDbDTO() {
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(int availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getFoodType() {
        return foodType;
    }

    public void setFoodType(int foodType) {
        this.foodType = foodType;
    }

    public int getIsSpicy() {
        return isSpicy;
    }

    public void setIsSpicy(int isSpicy) {
        this.isSpicy = isSpicy;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getFbTypeId() {
        return fbTypeId;
    }

    public void setFbTypeId(int fbTypeId) {
        this.fbTypeId = fbTypeId;
    }

    public static List<MenuDbDTO> deserializeMenu(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<MenuDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            MenuDbDTO deserializeObject = gson.fromJson(serializedString, MenuDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
