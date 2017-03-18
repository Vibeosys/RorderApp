package com.vibeosys.quickserve.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Class helps to retrive or set values from shared preferences.
 * In case there are no values, it feeds in values from app.properties from assets directory
 * Created by anand on 29-10-2015.
 */
public class SessionManager {
    private static final String PROJECT_PREFERENCES = "com.vibeosys.rorderapp";

    private static SessionManager mSessionManager;
    private static SharedPreferences mProjectSharedPref = null;
    private static Context mContext = null;
    private static PropertyFileReader mPropertyFileReader = null;

    /**
     * Method is supposed to called once at beginning of the APP.
     *
     * @param context Context of any base or current activity, Needs to be called at only once.
     * @return SessionManager session manager instance
     */

    public static SessionManager getInstance(Context context) {
        if (mSessionManager != null)
            return mSessionManager;

        mContext = context;
        mPropertyFileReader = PropertyFileReader.getInstance(context);
        loadProjectSharedPreferences();
        mSessionManager = new SessionManager();

        return mSessionManager;
    }

    /**
     * Gets singleton instance of session manager class
     *
     * @return Singleton Instance of Session Manager
     */

    public static SessionManager Instance() {
        if (mSessionManager != null)
            return mSessionManager;
        else
            throw new IllegalArgumentException("No instance is yet created");
    }

    /**
     * Loads all the App.Properties file values into shared preferences.
     * In case of version upgrade, replaces all the values in the shared preferences.
     */

    private static void loadProjectSharedPreferences() {
        if (mProjectSharedPref == null) {
            mProjectSharedPref = mContext.getSharedPreferences(PROJECT_PREFERENCES, Context.MODE_PRIVATE);
        }

        String versionNumber = mProjectSharedPref.getString(PropertyTypeConstants.VERSION_NUMBER, null);
        Float versionNoValue = versionNumber == null ? 0 : Float.valueOf(versionNumber);

        if (mPropertyFileReader.getVersion() > versionNoValue) {
            boolean sharedPrefChange = addOrUdateSharedPreferences();
            if (!sharedPrefChange)
                Log.e("SharedPref", "No shared preferences are changed");
        }
    }

    /**
     * Adds or updates entries into shared preferences.
     *
     * @return true or false based upon the update in shared preferences.
     */
    private static boolean addOrUdateSharedPreferences() {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        //editor.putString(PropertyTypeConstants.API_UPLOAD_IMAGE_URL, mPropertyFileReader.getImageUploadUrl());
        editor.putString(PropertyTypeConstants.API_UPLOAD_URL, mPropertyFileReader.getUploadUrl());
        //editor.putString(PropertyTypeConstants.API_UPDATE_USERS_DETAILS_URL, mPropertyFileReader.getUploadUserDetails());
        editor.putString(PropertyTypeConstants.API_DOWNLOAD_DB_URI, mPropertyFileReader.getDownloadDbUrl());
        editor.putString(PropertyTypeConstants.API_DOWNLOAD_URI, mPropertyFileReader.getDownloadUrl());
        editor.putString(PropertyTypeConstants.DATABASE_DEVICE_FULLPATH, mPropertyFileReader.getDatabaseDeviceFullPath());
        editor.putString(PropertyTypeConstants.DATABASE_DIR_PATH, mPropertyFileReader.getDatabaseDirPath());
        editor.putString(PropertyTypeConstants.DATABASE_FILE_NAME, mPropertyFileReader.getDatabaseFileName());
        editor.putString(PropertyTypeConstants.API_RESTAURANT_URI, mPropertyFileReader.getRestaurantUrl());
        //editor.putString(PropertyTypeConstants.API_SEND_OTP_URL, mPropertyFileReader.getSendOtpUrl());
        editor.putString(PropertyTypeConstants.VERSION_NUMBER, String.valueOf(mPropertyFileReader.getVersion()));
        editor.putString(PropertyTypeConstants.GOOGLE_PLAY_SERVICE_SET, mPropertyFileReader.getPlayServiceSetting());

        editor.putString(PropertyTypeConstants.KOT_PRINTER_STATUS, mPropertyFileReader.getKotPrinterStatus());
        editor.putString(PropertyTypeConstants.BILL_PRINTER_STATUS, mPropertyFileReader.getBillPrinterStatus());
        editor.putString(PropertyTypeConstants.KOT_PRINTER_IP, mPropertyFileReader.getKotPrinterIp());
        editor.putString(PropertyTypeConstants.BILL_PRINTER_IP, mPropertyFileReader.getBillPrinterIp());

        editor.putString(PropertyTypeConstants.USER_PERMISSION, mPropertyFileReader.getUserPermission());

        editor.apply();
        return true;
    }


    private SessionManager() {
    }

    public String getDownloadDbUrl(int restaurantId) {
        if (restaurantId == 0)
            Log.e("SessionManager", "User id in download DB URL is blank");

        String downloadDbUrl = mProjectSharedPref.getString(PropertyTypeConstants.API_DOWNLOAD_DB_URI, null);
        return downloadDbUrl + restaurantId;
    }

    public String getDownloadUrl(int userId, int restaurantId) {
        if (userId == 0)
            Log.e("SessionManager", "User id in download URL is blank");

        String downloadUrl = mProjectSharedPref.getString(PropertyTypeConstants.API_DOWNLOAD_URI, null);
        return downloadUrl + userId + "&restaurantId=" + restaurantId;
    }

    public String getRestaurantUrl() {
        String downloadUrl = mProjectSharedPref.getString(PropertyTypeConstants.API_RESTAURANT_URI, null);
        return downloadUrl;
    }

    public String getUploadUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.API_UPLOAD_URL, null);
    }


    public String getDatabaseDeviceFullPath() {
        return mProjectSharedPref.getString(PropertyTypeConstants.DATABASE_DEVICE_FULLPATH, null);
    }

    public int getUserId() {
        return mProjectSharedPref.getInt(PropertyTypeConstants.USER_ID, 0);
    }

    public void setUserId(int userId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_ID, userId);
    }

    public String getUserName() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_NAME, null);
    }

    public void setUserName(String userName) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_NAME, userName);
    }

    public String getUserEmailId() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_EMAIL_ID, null);
    }

    public void setUserEmailId(String userEmailId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_EMAIL_ID, userEmailId);
    }

    public void setUserActive(boolean active) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_IS_ACTIVE, String.valueOf(active));
    }

    public boolean getUserActive() {
        return mProjectSharedPref.getBoolean(PropertyTypeConstants.USER_IS_ACTIVE, false);
    }

    public void setUserRollId(int rollId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_ROLL_ID, rollId);
    }

    public int getUserRollId() {
        return mProjectSharedPref.getInt(PropertyTypeConstants.USER_ROLL_ID, 0);
    }

    public void setUserRestaurantId(int restoId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_RESTO_ID, restoId);
    }

    public int getUserRestaurantId() {
        return mProjectSharedPref.getInt(PropertyTypeConstants.USER_RESTO_ID, 0);
    }

    public void setUserRestaurantName(String restaurantName) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_RESTO_NAME, restaurantName);
    }

    public String getUserRestaurantName() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_RESTO_NAME, null);
    }

    /*  public void setIntUserId(int userId) {
          setValuesInSharedPrefs(PropertyTypeConstants.USER_ID, userId);
      }

      public int getIntUserId() {
          return mProjectSharedPref.getInt(PropertyTypeConstants.USER_RESTO_ID, 0);
      }*/
    public String getDatabaseDirPath() {
        return mProjectSharedPref.getString(PropertyTypeConstants.DATABASE_DIR_PATH, null);
    }

    public String getDatabaseFileName() {
        return mProjectSharedPref.getString(PropertyTypeConstants.DATABASE_FILE_NAME, null);
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_PHOTO_URL, userPhotoUrl);
    }

    public String getUserPhotoUrl() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_PHOTO_URL, null);
    }

    public void setUserRegdApiKey(String userRegdApiKey) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_REGD_API_KEY, userRegdApiKey);
    }

    public String getUserRegdApiKey() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_REGD_API_KEY, null);
    }

    public String getDeviceId(String deviceId) {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_DEVICE_ID, null);
    }

    public void setDeviceId(String deviceId) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_DEVICE_ID, deviceId);
    }

    public void setImei(String imei) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_IMEI_ID, imei);
    }

    public String getImei() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_IMEI_ID, null);
    }
    public void setMac(String mac) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_MAC_ID, mac);
    }

    public String getMac() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_MAC_ID, null);
    }
    public String getAnalyticsSet() {
        return mProjectSharedPref.getString(PropertyTypeConstants.GOOGLE_PLAY_SERVICE_SET, "off");
    }

    public String getKotPrinterStatus() {
        return mProjectSharedPref.getString(PropertyTypeConstants.KOT_PRINTER_STATUS, null);
    }

    public String getBillPrinterStatus() {
        return mProjectSharedPref.getString(PropertyTypeConstants.BILL_PRINTER_STATUS, null);
    }

    public String getKotPrinterIp() {
        return mProjectSharedPref.getString(PropertyTypeConstants.KOT_PRINTER_IP, null);
    }

    public String getBillPrinterIp() {
        return mProjectSharedPref.getString(PropertyTypeConstants.BILL_PRINTER_IP, null);
    }


    public void setKotPrinterStatus(String status) {
        setValuesInSharedPrefs(PropertyTypeConstants.KOT_PRINTER_STATUS, status);
    }

    public void setBillPrinterStatus(String status) {
        setValuesInSharedPrefs(PropertyTypeConstants.BILL_PRINTER_STATUS, status);
    }

    public void setKotPrinterIp(String ip) {
        setValuesInSharedPrefs(PropertyTypeConstants.KOT_PRINTER_IP, ip);
    }

    public void setBillPrinterIp(String ip) {
        setValuesInSharedPrefs(PropertyTypeConstants.BILL_PRINTER_IP, ip);
    }

    public String getUserPermission() {
        return mProjectSharedPref.getString(PropertyTypeConstants.USER_PERMISSION, null);
    }

    public void setUserPermission(String permission) {
        setValuesInSharedPrefs(PropertyTypeConstants.USER_PERMISSION, permission);
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, String sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putString(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }

    private static void setValuesInSharedPrefs(String sharedPrefKey, int sharedPrefValue) {
        SharedPreferences.Editor editor = mProjectSharedPref.edit();
        editor.putInt(sharedPrefKey, sharedPrefValue);
        editor.apply();
    }
}
