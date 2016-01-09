package com.vibeosys.rorderapp.interfaces;

/**
 * Callback for handling async task states
 */
public interface BackgroundTaskCallback {
    void onResultReceived(String downloadedJson);
}
