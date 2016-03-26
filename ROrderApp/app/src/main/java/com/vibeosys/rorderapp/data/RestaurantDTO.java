package com.vibeosys.rorderapp.data;

/**
 * Created by shrinivas on 26-03-2016.
 */
public class RestaurantDTO {
    private int mRestaurantId;
    private String mRestauranrTitle;
    private String mLogoUrl;
    private String mAddress;
    private String mArea;
    private String mCity;
    private String mCountry;
    private String mPhoneNumber;
    private String mFooter;

    public RestaurantDTO(int mRestaurantId, String mRestauranrTitle, String mLogoUrl, String mAddress,
                         String mArea, String mCity, String mCountry, String mPhoneNumber, String mFooter)
    {
        this.mRestaurantId = mRestaurantId;
        this.mRestauranrTitle = mRestauranrTitle;
        this.mLogoUrl = mLogoUrl;
        this.mAddress = mAddress;
        this.mArea = mArea;
        this.mCity = mCity;
        this.mCountry = mCountry;
        this.mPhoneNumber = mPhoneNumber;
        this.mFooter = mFooter;
    }


    public int getmRestaurantId() {
        return mRestaurantId;
    }

    public void setmRestaurantId(int mRestaurantId) {
        this.mRestaurantId = mRestaurantId;
    }

    public String getmRestauranrTitle() {
        return mRestauranrTitle;
    }

    public void setmRestauranrTitle(String mRestauranrTitle) {
        this.mRestauranrTitle = mRestauranrTitle;
    }

    public String getmLogoUrl() {
        return mLogoUrl;
    }

    public void setmLogoUrl(String mLogoUrl) {
        this.mLogoUrl = mLogoUrl;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmArea() {
        return mArea;
    }

    public void setmArea(String mArea) {
        this.mArea = mArea;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmFooter() {
        return mFooter;
    }

    public void setmFooter(String mFooter) {
        this.mFooter = mFooter;
    }
}
