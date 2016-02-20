package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    private String colour;
    private String imgUrl;

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


    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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
