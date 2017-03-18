package com.vibeosys.quickserve.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 18-03-2016.
 */
public class PermissionSetDbDTO extends BaseDTO {

    private int permissionId;
    private String permissionKey;
    private String description;
    private int active;

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionKey() {
        return permissionKey;
    }

    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public static List<PermissionSetDbDTO> deserializePermission(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<PermissionSetDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                PermissionSetDbDTO deserializeObject = gson.fromJson(serializedString, PermissionSetDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
