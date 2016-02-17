package com.vibeosys.rorderapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.database.DbRepository;
import com.vibeosys.rorderapp.util.ServerSyncManager;
import com.vibeosys.rorderapp.util.SessionManager;


/**
 * Created by mahesh on 10/29/2015.
 */
public class BaseFragment extends Fragment {

    /**
     * Base Activity will give the basic implementation with async task support and other things
     */
    protected ServerSyncManager mServerSyncManager;
    protected static SessionManager mSessionManager;
    protected DbRepository mDbRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSessionManager = SessionManager.getInstance(getContext());
        mServerSyncManager = new ServerSyncManager(getContext(), mSessionManager);
        mDbRepository = new DbRepository(getContext(), mSessionManager);
    }
    protected void showMyDialog(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.show_network_alert);
        dialog.setTitle("Network " + getResources().getString(R.string.alert_dialog));
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivityForResult(new Intent(Settings.ACTION_SETTINGS), 0);
            }
        });
        dialog.show();
    }
}


