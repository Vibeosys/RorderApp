package com.vibeosys.rorderapp.data;

import java.util.Comparator;

/**
 * Created by akshay on 27-01-2016.
 */
public class OrderMenuDTO implements Comparator<OrderMenuDTO> {

    public static final boolean HIDE=false;
    public static final boolean SHOW=true;
    private int mMenuId;
    private String mMenuTitle;
    private String mImage;
    private boolean mFoodType;
    private String mTags;
    private String mCategory;
    private double mPrice;
    private int mQuantity;
    private boolean mShow;
    public OrderMenuDTO() {
    }

    public OrderMenuDTO(int mMenuId,String mMenuTitle, String mImage, boolean mFoodType, String mTags, String mCategory, double mPrice,int mQuantity,boolean mShow) {
        this.mMenuId=mMenuId;
        this.mMenuTitle = mMenuTitle;
        this.mImage = mImage;
        this.mFoodType = mFoodType;
        this.mTags = mTags;
        this.mCategory = mCategory;
        this.mPrice = mPrice;
        this.mQuantity=mQuantity;
        this.mShow=mShow;
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

    public boolean ismFoodType() {
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

    public int getmQuantity() {
        return mQuantity;
    }

    public void setmQuantity(int mQuantity) {
        this.mQuantity = mQuantity;
    }

    public boolean ismShow() {
        return mShow;
    }

    public void setmShow(boolean mShow) {
        this.mShow = mShow;
    }

    @Override
    public String toString() {
        return "OrderMenuDTO{" +
                "mMenuId=" + mMenuId +
                ", mMenuTitle='" + mMenuTitle + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mFoodType=" + mFoodType +
                ", mTags='" + mTags + '\'' +
                ", mCategory='" + mCategory + '\'' +
                ", mPrice=" + mPrice +
                ", mQuantity=" + mQuantity +
                '}';
    }

    @Override
    public int compare(OrderMenuDTO o1, OrderMenuDTO o2) {
        boolean v1 = o1.ismShow();
        boolean v2 = o2.ismShow();
        return (v1 ^ v2) ? ((v1 ^ this.mShow) ? 1 : -1) : 0;
    }
}
