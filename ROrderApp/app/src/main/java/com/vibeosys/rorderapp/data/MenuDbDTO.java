package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;

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
    private boolean availabilityStatus;
    private boolean active;
    private boolean foodType;
    private Date createdDate;
    private Date updatedDate;
    private int categoryId;

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

    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isFoodType() {
        return foodType;
    }

    public void setFoodType(boolean foodType) {
        this.foodType = foodType;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
