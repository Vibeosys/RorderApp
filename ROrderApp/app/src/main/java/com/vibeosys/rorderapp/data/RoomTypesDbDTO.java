package com.vibeosys.rorderapp.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 18-03-2016.
 */
public class RoomTypesDbDTO extends BaseDTO {

    private int roomTypeId;
    private String roomType;
    private int active;

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public static List<RoomTypesDbDTO> deserializeBill(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<RoomTypesDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                RoomTypesDbDTO deserializeObject = gson.fromJson(serializedString, RoomTypesDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
