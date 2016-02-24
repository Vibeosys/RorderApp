package com.vibeosys.rorderapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.ChefOrderAdapter;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.util.NetworkUtils;


import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shrinivas on 15-02-2016.
 */
public class FragmentChefPlacedOrder extends BaseFragment
{
    private ExpandableListView chefOrderListHistory;
    public static ChefOrderAdapter chefOrderAdapter;
    private ArrayList<ChefOrderDetailsDTO> listHistory  =new ArrayList<>();
    public static Handler UIHandler;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    //       return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.chef_expandable_list_history,container,false);
        chefOrderListHistory  =(ExpandableListView) view.findViewById(R.id.expListViewForChefHistory);
        listHistory = mDbRepository.getOrderHeadesInAsc(2);
        chefOrderAdapter = new ChefOrderAdapter(getActivity().getApplicationContext(),listHistory,mDbRepository);
        chefOrderListHistory.setAdapter(chefOrderAdapter);
        if(!NetworkUtils.isActiveNetworkAvailable(getContext()))
        {
            String stringTitle ="Network error";
            String stringMessage="No Internet connection is available.Please check internet connection.";
            customAlterDialog(stringTitle,stringMessage);
        }
        chefOrderAdapter.notifyDataSetChanged();

        return view;
    }
    static {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);

    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onResume() {

        super.onResume();
    }
}
