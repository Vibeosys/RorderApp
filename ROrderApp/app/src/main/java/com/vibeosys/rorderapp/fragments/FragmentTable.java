package com.vibeosys.rorderapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.HotelTableDTO;

import java.util.ArrayList;

/**
 * Created by kiran on 20-01-2016.
 */
public class FragmentTable extends GridBaseFragment {

    ArrayList<HotelTableDTO> hotels;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.layout_all_table, container, false);
        hotels=new ArrayList<>();
        hotels.add(new HotelTableDTO(1, 1, 2));
        hotels.add(new HotelTableDTO(2,1,2));
        hotels.add(new HotelTableDTO(3,2,4));
        hotels.add(new HotelTableDTO(4,2,4));
        hotels.add(new HotelTableDTO(5,1,2));
        hotels.add(new HotelTableDTO(6,1,4));
        hotels.add(new HotelTableDTO(7,1,8));

        setGridAdapter(v,hotels);
        return v;
    }
}
