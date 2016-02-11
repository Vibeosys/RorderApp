package com.vibeosys.rorderapp.data;

/**
 * Created by shrinivas on 11-02-2016.
 */
public class ChefMenuDetailsDTO {

    int mChefMenuId;
    String mChefMenuTitle;
    int mChefQty;

    public ChefMenuDetailsDTO(int mChefMenuId, String mChefMenuTitle, int mChefQty) {
        this.mChefMenuId = mChefMenuId;
        this.mChefMenuTitle = mChefMenuTitle;
        this.mChefQty = mChefQty;
    }

    public int getmChefMenuId() {
        return mChefMenuId;
    }

    public void setmChefMenuId(int mChefMenuId) {
        this.mChefMenuId = mChefMenuId;
    }

    public String getmChefMenuTitle() {
        return mChefMenuTitle;
    }

    public void setmChefMenuTitle(String mChefMenuTitle) {
        this.mChefMenuTitle = mChefMenuTitle;
    }

    public int getmChefQty() {
        return mChefQty;
    }

    public void setmChefQty(int mChefQty) {
        this.mChefQty = mChefQty;
    }



}
