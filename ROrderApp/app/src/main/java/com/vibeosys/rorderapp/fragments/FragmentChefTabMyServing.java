package com.vibeosys.rorderapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.ChefOrderAdapter;
import com.vibeosys.rorderapp.adaptors.ChefRecyclerViewAdapter;
import com.vibeosys.rorderapp.adaptors.ChefTabListAdapter;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.database.DbRepository;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by shrinivas on 02-03-2016.
 */
public class FragmentChefTabMyServing extends BaseFragment {


    private ChefTabListAdapter chefTabListAdapter;
    public static ChefOrderAdapter chefOrderAdapter;
    public static Handler UIHandler;
    private ListView listView;
    private ArrayList<ChefOrderDetailsDTO> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chef_tab_layout,container,false);

        //listView = (ListView)view.findViewById(R.id.listChef);
        /*list = mDbRepository.getOrderHeadesInAsc(1);
        chefTabListAdapter = new ChefTabListAdapter(getActivity().getApplicationContext(),list);
        listView.setAdapter(chefTabListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ChefOrderDetailsDTO chefOrderDetailsDTO = (ChefOrderDetailsDTO)chefTabListAdapter.getItem(position);
                String orderId = chefOrderDetailsDTO.getmNewOrderId();
                String waiterName = chefOrderDetailsDTO.getmUserName();
                int tableNumber = chefOrderDetailsDTO.getmTableNo();
                int orderNumber = chefOrderDetailsDTO.getmOrderNumner();
                String orderTime = (chefOrderDetailsDTO.TimeDiff());
                android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                FragmentChefTabDetail fragmentChefTabDetail = new FragmentChefTabDetail();
                Bundle args = new Bundle();
                args.putString("orderId" ,orderId);
                args.putString("waiterName" ,waiterName);
                args.putInt("tableNumber", tableNumber);
                args.putInt("orderNumber", orderNumber);
                args.putString("orderTimeString",orderTime);
                fragmentChefTabDetail.setArguments(args);
                fragmentTransaction.add(R.id.parent_detaisl, fragmentChefTabDetail);
                fragmentTransaction.commit();

            }
        });*/
        ArrayList<ChefOrderDetailsDTO> orders = mDbRepository.getRecChefOrder();
        mDbRepository.addMenuList(orders);
        RecyclerView chefRecycle = (RecyclerView) view.findViewById(R.id.ChefRecycler);
        ChefRecyclerViewAdapter adapterRecycle = new ChefRecyclerViewAdapter(orders, getActivity().getApplicationContext());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(),2,1,true);
//        chefRecycle.setLayoutManager(gridLayoutManager);
        chefRecycle.setLayoutManager(layoutManager);
        chefRecycle.setAdapter(adapterRecycle);


        /*chefRecycle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                }
                v.onTouchEvent(event);

                return true;
            }
        });*/
        return view;
    }


}
