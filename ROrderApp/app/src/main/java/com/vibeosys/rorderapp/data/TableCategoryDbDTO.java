package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * This DTO is use to transfer the
 * data from server to client or vice versa.
 * Created by akshay on 20-01-2016.
 */
public class TableCategoryDbDTO {

    private int tableCategoryId;
    private String categoryTitle;
    private String image;
    private Date createdDate;
    private Date updatedDate;

    public TableCategoryDbDTO() {
    }

    public int getTableCategoryId() {
        return tableCategoryId;
    }

    public void setTableCategoryId(int tableCategoryId) {
        this.tableCategoryId = tableCategoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public static List<TableCategoryDbDTO> deserializeTableCateory(List<String> serializedStringList) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
        ArrayList<TableCategoryDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            TableCategoryDbDTO deserializeObject = gson.fromJson(serializedString, TableCategoryDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
