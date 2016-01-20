package com.vibeosys.rorderapp.data;

import java.util.ArrayList;

/**
 * This DTO is use to transfer the
 * data from server to client or vice versa.
 * Created by akshay on 20-01-2016.
 */
public class TableCategoryDbDTO {

    private int categoryId;
    private String title;
    private String image;
    private String createdDate;
    private String updatedDate;

    public TableCategoryDbDTO() {
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }


}
