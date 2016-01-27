package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 27-01-2016.
 */
public class OrderMenuDTO {

    private int mMenuId;
    private String mMenuTitle;
    private String mImage;
    private boolean mFoodType;
    private String mTags;
    private String mCategory;
    private double mPrice;

    public OrderMenuDTO() {
    }

    public OrderMenuDTO(int mMenuId,String mMenuTitle, String mImage, boolean mFoodType, String mTags, String mCategory, double mPrice) {
        this.mMenuId=mMenuId;
        this.mMenuTitle = mMenuTitle;
        this.mImage = mImage;
        this.mFoodType = mFoodType;
        this.mTags = mTags;
        this.mCategory = mCategory;
        this.mPrice = mPrice;
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

    public boolean getmFoodType() {
        return mFoodType;
    }

    public void setmFoodType(boolean mFoodType) {
        this.mFoodType = mFoodType;
    }

    public String getmTags() {
        return mTags;
    }

    public void setmTags(String mTags) {
        this.mTags = mTags;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public double getmPrice() {
        return mPrice;
    }

    public void setmPrice(double mPrice) {
        this.mPrice = mPrice;
    }

    @Override
    public String toString() {
        return "OrderMenuDTO{" +
                "mMenuTitle='" + mMenuTitle + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mFoodType='" + mFoodType + '\'' +
                ", mTags='" + mTags + '\'' +
                ", mCategory='" + mCategory + '\'' +
                ", mPrice=" + mPrice +
                '}';
    }
}
