package com.vibeosys.quickserve.data;

/**
 * Created by akshay on 16-02-2016.
 */
public class FeedBackDTO {

    private int mFeedbackId;
    private String mTitle;
    private float mRating;

    public FeedBackDTO(int mFeedbackId, String mTitle, int mRating) {
        this.mFeedbackId = mFeedbackId;
        this.mTitle = mTitle;
        this.mRating = mRating;
    }

    public int getmFeedbackId() {
        return mFeedbackId;
    }

    public void setmFeedbackId(int mFeedbackId) {
        this.mFeedbackId = mFeedbackId;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public float getmRating() {
        return mRating;
    }

    public void setmRating(float mRating) {
        this.mRating = mRating;
    }
}
