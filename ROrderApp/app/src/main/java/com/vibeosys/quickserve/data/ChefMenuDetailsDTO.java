package com.vibeosys.quickserve.data;

/**
 * Created by shrinivas on 11-02-2016.
 */
public class ChefMenuDetailsDTO {

    int mChefMenuId;
    String mChefMenuTitle;
    int mChefQty;


    String mMenuNote;

    public ChefMenuDetailsDTO(int mChefMenuId, String mChefMenuTitle, int mChefQty,String mMenuNote ) {
        this.mChefMenuId = mChefMenuId;
        this.mChefMenuTitle = mChefMenuTitle;
        this.mChefQty = mChefQty;
        this.mMenuNote = mMenuNote;
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

    public String getmMenuNote() {
        return mMenuNote;
    }

    public void setmMenuNote(String mMenuNote) {
        this.mMenuNote = mMenuNote;
    }


}
