package com.vibeosys.rorderapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.activities.TableMenusActivity;
import com.vibeosys.rorderapp.adaptors.TableGridAdapter;
import com.vibeosys.rorderapp.data.HotelTableDTO;
import com.vibeosys.rorderapp.data.RestaurantTables;

import java.util.ArrayList;

/**
 * Created by akshay on 21-01-2016.
 */
public class GridBaseFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private final String TAG = GridBaseFragment.class.getSimpleName();
    GridView gridView;
    TableGridAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected String getScreenName() {
        return "Grid base";
    }

    protected void setGridAdapter(View v, ArrayList<RestaurantTables> hotels) {
        gridView = (GridView) v.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(this);
        adapter = new TableGridAdapter(getContext(), hotels, mSessionManager.getUserId());
        gridView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RestaurantTables hotelTableDTO = (RestaurantTables) adapter.getItem(position);

        Intent intentOpenTableMenu = new Intent(getActivity(), TableMenusActivity.class);
        intentOpenTableMenu.putExtra("TableNo", hotelTableDTO.getmTableNo());
        intentOpenTableMenu.putExtra("TableId", hotelTableDTO.getmTableId());
        startActivity(intentOpenTableMenu);
        Log.i(TAG, "##" + hotelTableDTO.getmTableNo() + "Is Clicked");
    }
}
