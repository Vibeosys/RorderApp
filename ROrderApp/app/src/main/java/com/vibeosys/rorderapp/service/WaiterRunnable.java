package com.vibeosys.rorderapp.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by akshay on 19-02-2016.
 */
public class WaiterRunnable extends IntentService {
    @Override
    protected void onHandleIntent(Intent intent) {
    }

    public WaiterRunnable() {
        super(WaiterRunnable.class.getName());
    }
}
