package com.vibeosys.quickserve.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.adaptors.NotificationAdapter;
import com.vibeosys.quickserve.data.NotificationOrderDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 17-02-2016.
 */
public class NotificationActivity extends BaseActivity {

    private ListView listNotification;
    private NotificationAdapter mAdapter;
    public static ArrayList<NotificationOrderDTO> notifications = new ArrayList<>();

    @Override
    protected String getScreenName() {
        return "Waiter Notification";
    }

    @Override
    protected void onCreate(Bundle savedInstatnceState) {
        super.onCreate(savedInstatnceState);
        setContentView(R.layout.activity_notification);
        setTitle("Notifications");
        listNotification = (ListView) findViewById(R.id.listNotification);
        mAdapter = new NotificationAdapter(getApplicationContext(), notifications);
        listNotification.setAdapter(mAdapter);
    }
}
