package com.vibeosys.quickserve.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.data.HotelTableDTO;

import java.util.ArrayList;

/**
 * Created by kiran on 20-01-2016.
 */
public class FragmentAllServing extends GridBaseFragment {

    ArrayList<HotelTableDTO> hotels;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.layout_all_table, container, false);

        setGridAdapter(v, mDbRepository.getTableRecords(""));
        return v;
    }

    @Override
    protected String getScreenName() {
        return "Chef fragment";
    }
}
