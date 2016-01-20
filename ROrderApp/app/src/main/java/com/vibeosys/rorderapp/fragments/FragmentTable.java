package com.vibeosys.rorderapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vibeosys.rorderapp.R;

/**
 * Created by kiran on 20-01-2016.
 */
public class FragmentTable extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.layout_all_table, container, false);
        return v;
    }
}
