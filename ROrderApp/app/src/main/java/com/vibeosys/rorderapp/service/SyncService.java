package com.vibeosys.rorderapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.ServerSyncManager;
import com.vibeosys.rorderapp.util.SessionManager;

/**
 * Created by akshay on 23-01-2016.
 */
public class SyncService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public SyncService() {
        super(SyncService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SessionManager mSessionManager = SessionManager.getInstance(getApplicationContext());
        ServerSyncManager mServerSyncManager = new ServerSyncManager(getApplicationContext(), mSessionManager);
        //mServerSyncManager.setOnDownloadReceived(this);

        while (true) {
            synchronized (this) {
                try {
                    //TODO: Hardcoded time for now, need to read from properties
                    wait(10 * 1000);

                    if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext()))
                        mServerSyncManager.syncDataWithServer(false);

                } catch (Exception e) {
                    Log.e("SyncService", "Error occurred in background service " + e.toString());
                }
            }
        }
    }
}
