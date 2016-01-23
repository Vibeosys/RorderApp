package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 23-01-2016.
 */
public class MenuCateoryDbDTO extends BaseDTO {

    private int categoryId;
    private String categoryTitle;
    private String categoryImage;
    private boolean active;
    private Date createdDate;
    private Date updatedDate;

    public MenuCateoryDbDTO() {
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public static List<MenuCateoryDbDTO> deserializeMenuCateory(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<MenuCateoryDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            MenuCateoryDbDTO deserializeObject = gson.fromJson(serializedString, MenuCateoryDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
