package com.vibeosys.rorderapp.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by akshay on 27-01-2016.
 */
public class RestaurantDbDTO {

    private int restaurantId;
    private String title;

    public RestaurantDbDTO() {
    }

    public RestaurantDbDTO(int restaurantId, String title) {
        this.restaurantId = restaurantId;
        this.title = title;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<RestaurantDbDTO> getArrayList(String jsonArray)
    {

        ArrayList<RestaurantDbDTO> arrList=new ArrayList<>();
        try {
            JSONArray jsonArrayList=new JSONArray(jsonArray);
            for(int i=0;i<jsonArrayList.length();i++)
            {
                JSONObject obj= jsonArrayList.getJSONObject(i);
               String strTitle=obj.getString("title");
                int id=obj.getInt("restaurantId");
                RestaurantDbDTO restaurantDbDTO=new RestaurantDbDTO(id,strTitle);
                arrList.add(restaurantDbDTO);
                Log.i("","##"+restaurantDbDTO.toString());
            }
        }
        catch (Exception e)
        {
            Log.i("","##"+e.toString());
        }
        return arrList;
    }
}
