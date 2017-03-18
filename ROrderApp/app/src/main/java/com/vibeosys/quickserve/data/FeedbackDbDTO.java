package com.vibeosys.quickserve.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 16-02-2016.
 */
public class FeedbackDbDTO extends BaseDTO {

    private int feedbackId;
    private String feedbackTitle;
    private int active;


    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedbackTitle() {
        return feedbackTitle;
    }

    public void setFeedbackTitle(String feedbackTitle) {
        this.feedbackTitle = feedbackTitle;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public static List<FeedbackDbDTO> deserializeBill(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<FeedbackDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                FeedbackDbDTO deserializeObject = gson.fromJson(serializedString, FeedbackDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
