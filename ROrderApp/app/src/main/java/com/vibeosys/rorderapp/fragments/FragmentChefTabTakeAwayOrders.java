package com.vibeosys.rorderapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;

/**
 * Created by shrinivas on 09-03-2016.
 */
public class FragmentChefTabTakeAwayOrders  extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chef_tab_take_away, container, false);

        return view;

    }
}
