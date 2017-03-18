package com.vibeosys.quickserve.data;

/**
 * Created by akshay on 01-04-2016.
 */
public class SubMenuDTO {

    private int mSubMenuId;
    private int mMenuId;
    private String mMenuTitle;
    private double mMenuPrice;
    private int mQuantity;
    private String mNote;

    public SubMenuDTO(int mSubMenuId, int mMenuId, String mMenuTitle, double mMenuPrice, int quantity) {
        this.mSubMenuId = mSubMenuId;
        this.mMenuId = mMenuId;
        this.mMenuTitle = mMenuTitle;
        this.mMenuPrice = mMenuPrice;
        this.mQuantity = quantity;
    }

    public int getSubMenuId() {
        return mSubMenuId;
    }

    public void setSubMenuId(int mSubMenuId) {
        this.mSubMenuId = mSubMenuId;
    }

    public int getMenuId() {
        return mMenuId;
    }

    public void setMenuId(int mMenuId) {
        this.mMenuId = mMenuId;
    }

    public String getMenuTitle() {
        return mMenuTitle;
    }

    public void setMenuTitle(String mMenuTitle) {
        this.mMenuTitle = mMenuTitle;
    }

    public double getMenuPrice() {
        return mMenuPrice;
    }

    public void setMenuPrice(double mMenuPrice) {
        this.mMenuPrice = mMenuPrice;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String mNote) {
        this.mNote = mNote;
    }
}
