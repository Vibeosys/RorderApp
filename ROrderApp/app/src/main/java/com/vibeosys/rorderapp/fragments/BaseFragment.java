package com.vibeosys.rorderapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

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
}


