package com.vibeosys.quickserve.data;

/**
 * Created by anand on 07-11-2015.
 */
public class SyncDataDTO {
    private String mDownloadUrl;
    private String mUploadUrl;
    private String mUploadJson;

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.mDownloadUrl = downloadUrl;
    }

    public String getUploadUrl() {
        return mUploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.mUploadUrl = uploadUrl;
    }

    public String getUploadJson() {
        return mUploadJson;
    }

    public void setUploadJson(String uploadJson) {
        this.mUploadJson = uploadJson;
    }

}
