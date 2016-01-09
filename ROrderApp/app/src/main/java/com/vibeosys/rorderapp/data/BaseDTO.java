package com.vibeosys.rorderapp.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 07-11-2015.
 */
public class BaseDTO {
    public String serializeString() {
        Gson gson = new Gson();

        String serializedString = gson.toJson(this);
        Log.i("object Serialized ", serializedString);
        return serializedString;
    }

    public static String serializeString(Object objectToSerialize) {
        Gson gson = new Gson();

        String serializedString = gson.toJson(objectToSerialize);
        Log.i("object Serialized ", serializedString);
        return serializedString;
    }

    public static List<BaseDTO> deserializeJsonArray(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<BaseDTO> deserializedObjectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            BaseDTO baseObject = gson.fromJson(serializedString, BaseDTO.class);
            deserializedObjectList.add(baseObject);
        }
        return deserializedObjectList;
    }

}
