package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 16-02-2016.
 */
public class UploadFeedback {

    private int feedbackId;
    private float feedbackRating;

    public UploadFeedback(int feedbackId, float feedbackRating) {
        this.feedbackId = feedbackId;
        this.feedbackRating = feedbackRating;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(int feedbackId) {
        this.feedbackId = feedbackId;
    }

    public float getFeedbackRating() {
        return feedbackRating;
    }

    public void setFeedbackRating(float feedbackRating) {
        this.feedbackRating = feedbackRating;
    }
}
