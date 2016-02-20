package com.vibeosys.rorderapp.data;

import java.sql.Date;

/**
 * Created by akshay on 23-01-2016.
 */
public class MenuDTO {
    private int mMenuId;
    private String mMenuTitle;
    private String mImage;
    private double mPrice;
    private String mIngredients;
    private String mTags;
    private boolean mAvailabilityStatus;
    private boolean mActive;
    private boolean mFoodType;
    private int mCategoryId;

    public MenuDTO() {
    }

    public MenuDTO(int mMenuId, String mMenuTitle, String mImage,
                   double mPrice, String mIngredients, String mTags, boolean mAvailabilityStatus,
                   boolean mActive, boolean mFoodType,
                   int mCategoryId) {
        this.mMenuId = mMenuId;
        this.mMenuTitle = mMenuTitle;
        this.mImage = mImage;
        this.mPrice = mPrice;
        this.mIngredients = mIngredients;
        this.mTags = mTags;
        this.mAvailabilityStatus = mAvailabilityStatus;
        this.mActive = mActive;
        this.mFoodType = mFoodType;
        this.mCategoryId = mCategoryId;
    }

    public int getmMenuId() {
        return mMenuId;
    }

    public void setmMenuId(int mMenuId) {
        this.mMenuId = mMenuId;
    }

    public String getmMenuTitle() {
        return mMenuTitle;
    }

    public void setmMenuTitle(String mMenuTitle) {
        this.mMenuTitle = mMenuTitle;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public double getmPrice() {
        return mPrice;
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    public String getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(String mIngredients) {
        this.mIngredients = mIngredients;
    }

    public String getmTags() {
        return mTags;
    }

    public void setmTags(String mTags) {
        this.mTags = mTags;
    }

    public boolean ismAvailabilityStatus() {
        return mAvailabilityStatus;
    }

    public void setmAvailabilityStatus(boolean mAvailabilityStatus) {
        this.mAvailabilityStatus = mAvailabilityStatus;
    }

    public boolean ismActive() {
        return mActive;
    }

    public void setmActive(boolean mActive) {
        this.mActive = mActive;
    }

    public boolean ismFoodType() {
        return mFoodType;
    }

    public void setmFoodType(boolean mFoodType) {
        this.mFoodType = mFoodType;
    }


    public int getmCategoryId() {
        return mCategoryId;
    }

    public void setmCategoryId(int mCategoryId) {
        this.mCategoryId = mCategoryId;
    }
}
