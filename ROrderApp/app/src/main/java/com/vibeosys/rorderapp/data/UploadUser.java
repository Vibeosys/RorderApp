package com.vibeosys.rorderapp.data;

/**
 * Created by mahesh on 10/23/2015.
 */
public class UploadUser extends BaseDTO {
    protected String userId;
    protected String emailId;
    protected String userName;

    public UploadUser() {

    }

    public UploadUser(String userId, String emailId) {
        this.userId = userId;
        this.emailId = emailId;
    }

    public UploadUser(String userId, String emailId, String userName) {
        this.userId = userId;
        this.emailId = emailId;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
