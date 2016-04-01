package com.vibeosys.rorderapp.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.ApplicationErrorDBDTO;
import com.vibeosys.rorderapp.database.DbRepository;
import com.vibeosys.rorderapp.util.AnalyticsApplication;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.ROrderDateUtils;
import com.vibeosys.rorderapp.util.ServerSyncManager;
import com.vibeosys.rorderapp.util.SessionManager;


/**
 * Created by mahesh on 10/29/2015.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * Base Activity will give the basic implementation with async task support and other things
     */
    protected ServerSyncManager mServerSyncManager;
    protected static SessionManager mSessionManager;
    protected DbRepository mDbRepository;
    protected Tracker mTracker;
    protected final static String TAG = BaseFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSessionManager = SessionManager.getInstance(getContext());
        mServerSyncManager = new ServerSyncManager(getContext(), mSessionManager);
        mDbRepository = new DbRepository(getContext(), mSessionManager);

        if (mSessionManager.getAnalyticsSet().equals("on")) {
            // Google analytics tracker
            if (isGooglePlayServicesAvailable(getActivity().getApplicationContext())) {
                AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
                mTracker = application.getDefaultTracker();
            } else {
                Log.i(TAG, "Google play service is not available on device");
                mTracker = null;
            }

        } else {
            mTracker = null;
            Log.d(TAG, "Analytics is off");
        }

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

    protected void customAlterDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("" + title);
        builder.setIcon(R.drawable.ic_action_warning_yellow);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();


    }

    protected void sendEventToGoogle(String category, String action) {
        if (mTracker != null)
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory(category)
                    .setAction(action)
                    .build());
        else
            Log.d(TAG, "Analytics is not stated");
    }

    private boolean isGooglePlayServicesAvailable(Context context) {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity().getApplicationContext());

        // Showing status
        if (status == ConnectionResult.SUCCESS)
            return true;
        else {
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mTracker != null) {
            hitActivity();
        } else {
            Log.d(TAG, "Analytics is not stated");
        }
    }

    protected void hitActivity() {
        String screenName = getScreenName();
        Log.i(TAG, "Setting screen name: " + screenName);
        mTracker.setScreenName(screenName);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());


    }

    protected abstract String getScreenName();

    public void addError(String screenName, String method, String desc) {
        Gson gson = new Gson();
        ROrderDateUtils dateUtils = new ROrderDateUtils();
        ApplicationErrorDBDTO errorDBDTO = new ApplicationErrorDBDTO(screenName, method, desc, dateUtils.getLocalSQLCurrentDate(), dateUtils.getLocalCurrentTime());
        String serializedError = gson.toJson(errorDBDTO);
        mDbRepository.addDataToSync(ConstantOperations.ADD_APPLICATION_ERROR, "" + mSessionManager.getUserId(), serializedError);
    }
}


