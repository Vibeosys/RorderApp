package com.vibeosys.rorderapp.data;

import java.util.ArrayList;

/**
 * Created by akshay on 16-02-2016.
 */
public class UploadCustomerFeedback {

    private String custId;

    private ArrayList<UploadFeedback> feedback;

    public UploadCustomerFeedback(String custId, ArrayList<UploadFeedback> feedback) {
        this.custId = custId;
        this.feedback = feedback;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public ArrayList<UploadFeedback> getFeedback() {
        return feedback;
    }

    public void setFeedback(ArrayList<UploadFeedback> feedback) {
        this.feedback = feedback;
    }
}
