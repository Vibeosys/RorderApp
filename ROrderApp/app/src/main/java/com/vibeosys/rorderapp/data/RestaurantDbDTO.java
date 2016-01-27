package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 27-01-2016.
 */
public class RestaurantDbDTO {

    private int restaurantId;
    private String title;

    public RestaurantDbDTO() {
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
}
