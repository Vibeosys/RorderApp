package com.vibeosys.quickserve.data;

/**
 * Created by akshay on 15-02-2016.
 */
public class NoteDTO {
    private int mNoteId;
    private String mNoteTitle;
    private int mActive;
    private boolean mSelected;

    public NoteDTO(int mNoteId, String mNoteTitle, int mActive) {
        this.mNoteId = mNoteId;
        this.mNoteTitle = mNoteTitle;
        this.mActive = mActive;
    }

    public int getNoteId() {
        return mNoteId;
    }

    public void setNoteId(int mNoteId) {
        this.mNoteId = mNoteId;
    }

    public String getNoteTitle() {
        return mNoteTitle;
    }

    public void setNoteTitle(String mNoteTitle) {
        this.mNoteTitle = mNoteTitle;
    }

    public int getActive() {
        return mActive;
    }

    public void setActive(int mActive) {
        this.mActive = mActive;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean mSelected) {
        this.mSelected = mSelected;
    }
}
