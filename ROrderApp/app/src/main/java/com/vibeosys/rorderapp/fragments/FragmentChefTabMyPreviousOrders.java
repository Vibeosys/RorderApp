package com.vibeosys.rorderapp.fragments;

import android.app.ProgressDialog;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.ChefRecyclerPreviousAdapter;
import com.vibeosys.rorderapp.adaptors.ChefRecyclerViewAdapter;
import com.vibeosys.rorderapp.adaptors.ChefTabListAdapter;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 09-03-2016.
 */
public class FragmentChefTabMyPreviousOrders extends BaseFragment  {

    private ChefTabListAdapter chefTabListAdapter;
    public static Handler UIHandler;
    private ListView listView;
    private ArrayList<ChefOrderDetailsDTO> list = new ArrayList<>();
    private RecyclerView chefRecycle_Previous;
    public static ChefRecyclerPreviousAdapter adapterRecycle_previous;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chef_tab_previous_layout, container, false);


        ArrayList<ChefOrderDetailsDTO> orders = mDbRepository.getCompletedRecordsChef();
        mDbRepository.addMenuList(orders);
        chefRecycle_Previous = (RecyclerView) view.findViewById(R.id.ChefRecycler_previous);

        adapterRecycle_previous = new ChefRecyclerPreviousAdapter(orders, getActivity().getApplicationContext(), mDbRepository);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
      //  layoutManager.scrollToPositionWithOffset(4,20);

        chefRecycle_Previous.setNestedScrollingEnabled(false);
        chefRecycle_Previous.setLayoutManager(layoutManager);
        chefRecycle_Previous.setAdapter(adapterRecycle_previous);
        chefRecycle_Previous.offsetChildrenVertical(2);

        /*adapterRecycle.tabSetCompleteBtn(this);
        mServerSyncManager.setOnStringResultReceived(this);*/


        return view;
    }

    static {
        UIHandler = new Handler(Looper.getMainLooper());


    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
        //adapterRecycle.notifyDataSetChanged();
    }



}
