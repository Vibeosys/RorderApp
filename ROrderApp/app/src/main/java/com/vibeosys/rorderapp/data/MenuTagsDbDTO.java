package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 23-01-2016.
 */
public class MenuTagsDbDTO extends BaseDTO {

    private int tagId;
    private String tagTitle;

    public MenuTagsDbDTO() {
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagTitle() {
        return tagTitle;
    }

    public void setTagTitle(String tagTitle) {
        this.tagTitle = tagTitle;
    }

    public static List<MenuTagsDbDTO> deserializeMenuTag(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<MenuTagsDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            MenuTagsDbDTO deserializeObject = gson.fromJson(serializedString, MenuTagsDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
