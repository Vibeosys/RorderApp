package com.vibeosys.rorderapp.activities;

import android.os.Bundle;
import android.widget.ListView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.NotificationAdapter;
import com.vibeosys.rorderapp.data.NotificationOrderDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 17-02-2016.
 */
public class NotificationActivity extends BaseActivity {

    private ListView listNotification;
    private NotificationAdapter mAdapter;
    public static ArrayList<NotificationOrderDTO> notifications = new ArrayList<>();

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
