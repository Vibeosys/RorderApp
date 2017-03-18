package com.vibeosys.quickserve.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 18-03-2016.
 */
public class RoomsDbDTO extends BaseDTO {
    private int roomId;
    private String description;
    private int active;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
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

    public static List<RoomsDbDTO> deserializeRooms(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<RoomsDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                RoomsDbDTO deserializeObject = gson.fromJson(serializedString, RoomsDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
