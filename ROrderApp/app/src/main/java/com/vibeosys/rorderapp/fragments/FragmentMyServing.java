package com.vibeosys.rorderapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.TableGridAdapter;
import com.vibeosys.rorderapp.data.HotelTableDTO;
import com.vibeosys.rorderapp.data.TableCategoryDTO;

import java.util.ArrayList;

/**
 * Created by kiran on 20-01-2016.
 */
public class FragmentMyServing extends GridBaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.layout_all_table, container, false);
       ArrayList<HotelTableDTO> hotels=mDbRepository.getTableRecords();
        TableCategoryDTO categoryDTO=new TableCategoryDTO();
        setGridAdapter(v,categoryDTO.filterByCategory(hotels,2));
        return v;
    }
}
