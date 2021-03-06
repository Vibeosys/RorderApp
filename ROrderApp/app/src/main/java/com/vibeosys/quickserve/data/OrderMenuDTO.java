package com.vibeosys.quickserve.data;

/**
 * Created by akshay on 27-01-2016.
 */
public class OrderMenuDTO implements Comparable<OrderMenuDTO> {

    public static final int HIDE = 0;
    public static final int SHOW = 1;
    private int mMenuId;
    private boolean mAvail;
    private String mMenuTitle;
    private String mImage;
    private boolean mFoodType;
    private String mTags;
    private String mCategory;
    private double mPrice;
    private int mQuantity;
    private int mShow;
    private boolean mSpicy;
    private String mNote;
    private int fbType;

    public OrderMenuDTO() {
    }

    public OrderMenuDTO(int mMenuId, String mMenuTitle, String mImage, boolean mFoodType,
                        String mTags, String mCategory, double mPrice, int mQuantity, int mShow,
                        boolean isSpicy, boolean avail, int fbType) {
        this.mMenuId = mMenuId;
        this.mMenuTitle = mMenuTitle;
        this.mImage = mImage;
        this.mFoodType = mFoodType;
        this.mTags = mTags;
        this.mCategory = mCategory;
        this.mPrice = mPrice;
        this.mQuantity = mQuantity;
        this.mShow = mShow;
        this.mSpicy = isSpicy;
        this.mAvail = avail;
        this.fbType = fbType;
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

    public int getmShow() {
        return mShow;
    }

    public void setmShow(int mShow) {
        this.mShow = mShow;
    }

    public boolean isSpicy() {
        return mSpicy;
    }

    public void setSpicy(boolean mSpicy) {
        this.mSpicy = mSpicy;
    }

    public boolean isAvail() {
        return mAvail;
    }

    public void setAvail(boolean mAvail) {
        this.mAvail = mAvail;
    }

    public String getNote() {
        return mNote;
    }

    public void setNote(String mNote) {
        this.mNote = mNote;
    }

    public int getFbType() {
        return fbType;
    }

    public void setFbType(int fbType) {
        this.fbType = fbType;
    }

    @Override
    public String toString() {
        return "OrderMenuDTO{" +
                "mMenuId=" + mMenuId +
                ", mAvail=" + mAvail +
                ", mMenuTitle='" + mMenuTitle + '\'' +
                ", mImage='" + mImage + '\'' +
                ", mFoodType=" + mFoodType +
                ", mTags='" + mTags + '\'' +
                ", mCategory='" + mCategory + '\'' +
                ", mPrice=" + mPrice +
                ", mQuantity=" + mQuantity +
                ", mShow=" + mShow +
                ", mSpicy=" + mSpicy +
                ", mNote='" + mNote + '\'' +
                '}';
    }

    @Override
    public int compareTo(OrderMenuDTO another) {
        int result = another.getmShow() - this.mShow;
        if (result != 0) {
            return result;
        }
        return another.getmQuantity() - this.getmQuantity();
    }
}
