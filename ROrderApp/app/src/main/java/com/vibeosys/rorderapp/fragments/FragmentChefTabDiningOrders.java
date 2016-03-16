package com.vibeosys.rorderapp.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.ChefRecyclerViewAdapter;
import com.vibeosys.rorderapp.adaptors.ChefRecylerTabDiningAdapter;
import com.vibeosys.rorderapp.adaptors.ChefTabListAdapter;
import com.vibeosys.rorderapp.data.ChefOrderCompleted;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shrinivas on 09-03-2016.
 */
public class FragmentChefTabDiningOrders extends BaseFragment
        implements ChefRecylerTabDiningAdapter.tabCompleteButton, ServerSyncManager.OnStringResultReceived
        ,ServerSyncManager.OnStringErrorReceived {
    private ChefTabListAdapter chefTabListAdapter;
    public static Handler UIHandler;
    private ListView listView;
    private ArrayList<ChefOrderDetailsDTO> list = new ArrayList<>();
    private RecyclerView chefRecycleDining;
    public static ChefRecylerTabDiningAdapter adapterRecycleDining;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*View view = inflater.inflate(R.layout.chef_tab_dining, container, false);

        return view;
*/
        View view = inflater.inflate(R.layout.chef_tab_layout, container, false);
        ArrayList<ChefOrderDetailsDTO> orders = mDbRepository.getRecordChefDining();
        mDbRepository.addMenuList(orders);
        chefRecycleDining = (RecyclerView) view.findViewById(R.id.ChefRecycler);
        adapterRecycleDining = new ChefRecylerTabDiningAdapter(orders, getActivity().getApplicationContext(), mDbRepository);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        chefRecycleDining.setLayoutManager(layoutManager);

        chefRecycleDining.setAdapter(adapterRecycleDining);
        adapterRecycleDining.tabSetCompleteBtn(this);
        mServerSyncManager.setOnStringResultReceived(this);


        return view;
    }

    @Override
    public void tabComplete(String chefTabOrderId) {
        if (!NetworkUtils.isActiveNetworkAvailable(getContext())) {
            String stringTitle = getResources().getString(R.string.error_msg_title_for_network);
            String stringMessage = getResources().getString(R.string.error_msg_for_select_restaurant);
            customAlterDialog(stringTitle, stringMessage);

        } else {
            String dialogMessage = getResources().getString(R.string.dialog_fragment_msg);
            dialog = ProgressDialog.show(getActivity(), "", dialogMessage, true);
            dialog.show();
            sendTabDataToServer(chefTabOrderId);


        }
    }

    public void sendTabDataToServer(String chefTabOrderId) {
        if (NetworkUtils.isActiveNetworkAvailable(getContext())) {

            ChefOrderCompleted chefOrderCompleted = new ChefOrderCompleted(chefTabOrderId);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(chefOrderCompleted);
            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CHEF_ORDER_PLACE, serializedJsonString);
            mServerSyncManager.uploadDataToServer(tableDataDTO);


        } else {
            showMyDialog(getActivity());
        }
    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {
        int errorCode = -1;
        int errorString = -1;
        String message = null;


        try {

            errorString = data.getInt("errorCode");
            message = data.getString("message");
            if (errorString == 0) {
                mServerSyncManager.syncDataWithServer(true);
                adapterRecycleDining.refresh(1);
                adapterRecycleDining.notifyDataSetChanged();
                chefRecycleDining.invalidate();
                dialog.dismiss();

            } else {
                dialog.dismiss();
            }


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

    @Override
    protected String getScreenName() {
        return "Chef dine in for Tab";
    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error) {
        dialog.dismiss();
        String StringTitle ="Server Error";
        String StringMessage="Server Error,Try again";
        customAlterDialog(StringTitle,StringMessage);
    }
}
