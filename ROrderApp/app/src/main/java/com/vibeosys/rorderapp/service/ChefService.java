package com.vibeosys.rorderapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.vibeosys.rorderapp.fragments.FragmentChefMyServing;
import com.vibeosys.rorderapp.fragments.FragmentChefPlacedOrder;

/**
 * Created by akshay on 17-02-2016.
 */
public class ChefService extends IntentService {

    @Override
    protected void onHandleIntent(Intent intent) {
        while (true) {
            synchronized (this) {
                try {
                    //TODO: Hardcoded time for now, need to read from properties
                    wait(20 * 1000);


                    FragmentChefMyServing.runOnUI(new Runnable() {
                        public void run() {
                            try {
                            } catch (Exception e) {
                                Log.e("SyncService", "## Error in chef my serving  order runOnUi method " + e.toString());
                                e.printStackTrace();
                            }

                        }
                    });
                    FragmentChefPlacedOrder.runOnUI(new Runnable() {
                        @Override
                        public void run() {
                            try {
                            } catch (Exception e) {
                                Log.e("SyncService", "## Error in chef placed order runOnUi method " + e.toString());
                                e.printStackTrace();

                            }

                        }
                    });

                } catch (Exception e) {
                    Log.e("SyncService", "##Error occurred in background  chef service " + e.toString());
                }
            }
        }
    }

    public ChefService() {
        super(ChefService.class.getName());
    }
}
