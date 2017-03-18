package com.vibeosys.quickserve.data;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 27-01-2016.
 */
public class RestaurantDbDTO {

    private int restaurantId;
    private String title;
    private String logoUrl;
    private String address;
    private String area;
    private String city;
    private String country;
    private String phone;
    private String footer;


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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
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

    public static List<RestaurantDbDTO> deserializeRestaurant(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<RestaurantDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                RestaurantDbDTO deserializeObject = gson.fromJson(serializedString, RestaurantDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
