package com.vibeosys.rorderapp.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
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

import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.ChefOrderAdapter;
import com.vibeosys.rorderapp.adaptors.ChefRecyclerViewAdapter;
import com.vibeosys.rorderapp.adaptors.ChefTabListAdapter;
import com.vibeosys.rorderapp.data.ChefOrderCompleted;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.database.DbRepository;
import com.vibeosys.rorderapp.service.SyncService;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by shrinivas on 02-03-2016.
 */
public class FragmentChefTabMyServing extends BaseFragment
        implements ChefRecyclerViewAdapter.tabCompleteButton, ServerSyncManager.OnStringResultReceived {


    private ChefTabListAdapter chefTabListAdapter;
    public static Handler UIHandler;
    private ListView listView;
    private ArrayList<ChefOrderDetailsDTO> list = new ArrayList<>();
    private RecyclerView chefRecycle;
    public static ChefRecyclerViewAdapter adapterRecycle;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chef_tab_layout, container, false);


       ArrayList<ChefOrderDetailsDTO> orders = mDbRepository.getRecChefOrder();
     //   ArrayList<ChefOrderDetailsDTO> order1 = mDbRepository.getRecChefOrder("1");
        mDbRepository.addMenuList(orders);
        chefRecycle = (RecyclerView) view.findViewById(R.id.ChefRecycler);
        adapterRecycle = new ChefRecyclerViewAdapter(orders, getActivity().getApplicationContext(), mDbRepository);
     //   adapterRecycle = new ChefRecyclerViewAdapter(order1, getActivity().getApplicationContext(), mDbRepository);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        chefRecycle.setLayoutManager(layoutManager);

        chefRecycle.setAdapter(adapterRecycle);
        adapterRecycle.tabSetCompleteBtn(this);
        mServerSyncManager.setOnStringResultReceived(this);


        return view;
    }


    @Override
    public void tabComplete(String chefTabOrderId) {
        if (!NetworkUtils.isActiveNetworkAvailable(getContext())) {
            String stringTitle = "Network error";
            String stringMessage = "No Internet connection is available.Please check internet connection.";
            customAlterDialog(stringTitle, stringMessage);

        } else {
            dialog = ProgressDialog.show(getActivity(), "", "Please wait ...", true);
            dialog.show();
            sendTabDataToServer(chefTabOrderId);
            mServerSyncManager.syncDataWithServer(true);
            chefRecycle.invalidate();



        }
    }

    public void sendTabDataToServer(String chefTabOrderId) {
        if (NetworkUtils.isActiveNetworkAvailable(getContext())) {

            ChefOrderCompleted chefOrderCompleted = new ChefOrderCompleted(chefTabOrderId);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(chefOrderCompleted);
            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CHEF_ORDER_PLACE, serializedJsonString);
            mServerSyncManager.uploadDataToServer(tableDataDTO);
            mServerSyncManager.syncDataWithServer(true);
            chefRecycle.invalidate();

        } else {
            showMyDialog(getActivity());
        }
    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {
        int errorCode = -1;
        String errorString = "";
        String message = null;

        dialog.dismiss();
        chefRecycle.invalidate();
        try {
           /* errorCode = data.getInt(String.valueOf(errorCode));
            errorString = data.getString("errorCode");
            message = data.getString("message");
            if(errorCode == 0)
            {
                Toast.makeText(getActivity(),"Data send to server",Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getActivity(),"Data  not send",Toast.LENGTH_LONG).show();
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        UIHandler = new Handler(Looper.getMainLooper());


    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
        //adapterRecycle.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();


    }
}
