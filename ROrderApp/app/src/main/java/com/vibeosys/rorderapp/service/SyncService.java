package com.vibeosys.rorderapp.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.vibeosys.rorderapp.MainActivity;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.NotificationOrderDTO;
import com.vibeosys.rorderapp.database.DbRepository;
import com.vibeosys.rorderapp.fragments.FragmentChefMyServing;
import com.vibeosys.rorderapp.fragments.FragmentChefPlacedOrder;
import com.vibeosys.rorderapp.util.AppConstants;
import com.vibeosys.rorderapp.util.DbTableNameConstants;
import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.ServerSyncManager;
import com.vibeosys.rorderapp.util.SessionManager;

import java.util.List;
import java.util.Map;

/**
 * Created by akshay on 23-01-2016.
 */
public class SyncService extends IntentService implements ServerSyncManager.OnDownloadReceived, ServerSyncManager.OnNotifyUser {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    private static int notifyId = 1;

    public SyncService() {
        super(SyncService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SessionManager mSessionManager = SessionManager.getInstance(getApplicationContext());
        ServerSyncManager mServerSyncManager = new ServerSyncManager(getApplicationContext(), mSessionManager);
        mServerSyncManager.setOnDownloadReceived(this);
        mServerSyncManager.setOnNotifyUser(this);
        while (true) {
            synchronized (this) {
                try {
                    //TODO: Hardcoded time for now, need to read from properties
                    wait(AppConstants.SERVICE_TIME_OUT * 1000);
                    if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext()))
                        mServerSyncManager.syncDataWithServer(false);

                    Log.d("SyncService", "##In service");
                } catch (Exception e) {
                    Log.e("SyncService", "##Error occurred in background service " + e.toString());
                }
            }
        }


    }

    @Override
    public void onDownloadResultReceived(@NonNull Map<String, Integer> results) {
        String showMessage = "";
        for (java.util.Map.Entry<String, Integer> entry : results.entrySet()) {
            String key = entry.getKey();
            String msgKey = null;
            if (key.equals(DbTableNameConstants.R_TABLES))
                msgKey = "Table";
            else if (key.equals(DbTableNameConstants.MENU))
                msgKey = "Menu";
            else if (key.equals(DbTableNameConstants.BILL))
                msgKey = "Bill";
            else if (key.equals(DbTableNameConstants.BILL_DETAILS))
                msgKey = "Bill Details";
            else if (key.equals(DbTableNameConstants.CUSTOMER))
                msgKey = "Customer";
            else if (key.equals(DbTableNameConstants.MENU_CATEGORY))
                msgKey = "Menu Category";
            else if (key.equals(DbTableNameConstants.MENU_TAGS))
                msgKey = "Menu Tag";
            else if (key.equals(DbTableNameConstants.ORDER))
                msgKey = "Order";
            else if (key.equals(DbTableNameConstants.ORDER_DETAILS))
                msgKey = "Order Details";
            else if (key.equals(DbTableNameConstants.TABLE_CATEGORY))
                msgKey = "Table Category";
            else if (key.equals(DbTableNameConstants.USER))
                msgKey = "User";
            showMessage += entry.getValue() + " new " + msgKey + " are added\n";

        }

        //showNotification(showMessage);
    }

    private void showNotification(String showMessage) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setContentText(showMessage);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifyId++;
        // mId allows you to update the notification later on.
        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    @Override
    public void onNotificationReceived(@NonNull String message) {
        showNotification(message);
    }
}
