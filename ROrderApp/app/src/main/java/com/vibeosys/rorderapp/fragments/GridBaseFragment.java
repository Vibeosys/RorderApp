package com.vibeosys.rorderapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.TableGridAdapter;
import com.vibeosys.rorderapp.data.HotelTableDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 21-01-2016.
 */
public class GridBaseFragment extends BaseFragment implements AdapterView.OnItemClickListener{

    private final String TAG=GridBaseFragment.class.getSimpleName();
    GridView gridView;
    TableGridAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void setGridAdapter(View v,ArrayList<HotelTableDTO> hotels){
        gridView=(GridView)v.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(this);
        adapter=new TableGridAdapter(getContext(),hotels);
        gridView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HotelTableDTO hotelTableDTO= (HotelTableDTO) adapter.getItem(position);
        Log.i(TAG,"##"+hotelTableDTO.getmTableNo()+"Is Clicked");
    }
}
