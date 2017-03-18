package com.vibeosys.quickserve.data;

/**
 * Created by anand on 09/01/2016.
 */
public class Sync {
    private String mUserId;
    private String mJsonSync;
    private String mTableName;

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getJsonSync() {
        return mJsonSync;
    }

    public void setJsonSync(String jsonSync) {
        mJsonSync = jsonSync;
    }

    public String getTableName() {
        return mTableName;
    }

    public void setTableName(String tableName) {
        mTableName = tableName;
    }
}
