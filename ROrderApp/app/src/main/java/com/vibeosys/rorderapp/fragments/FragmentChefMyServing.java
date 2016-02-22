package com.vibeosys.rorderapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObservable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.ChefOrderAdapter;
import com.vibeosys.rorderapp.data.ChefOrderCompleted;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.service.ChefService;
import com.vibeosys.rorderapp.service.SyncService;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by shrinivas on 15-02-2016.
 */
public class FragmentChefMyServing extends BaseFragment implements
        ChefOrderAdapter.OnDoneClickListener,ServerSyncManager.OnStringResultReceived{

    private ExpandableListView chefOrderList;
    public static ChefOrderAdapter chefOrderAdapter;
    public static Handler UIHandler;
    private Context mContext =this.getContext();
    private ArrayList<ChefOrderDetailsDTO> list  =new ArrayList<>();
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chef_expandable_list,container,false);

//        Intent i = new Intent(Intent.ACTION_SYNC,null,this.getContext(),SyncService.class);
//        getContext().startService(i);

//        Intent chefServices = new Intent(this.getContext(),ChefService.class);
//        getContext().startService(chefServices);

        chefOrderList = (ExpandableListView) view.findViewById(R.id.expListViewForChef);
        list = mDbRepository.getOrderHeadesInAsc(1);
        //ChefOrderDetailsDTO tiemDifference = list.get(0);


        chefOrderAdapter = new ChefOrderAdapter(getActivity().getApplicationContext(),list,mDbRepository);
        chefOrderList.setAdapter(chefOrderAdapter);

        chefOrderAdapter.setOnDoneClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        chefOrderAdapter.notifyDataSetChanged();

        return view;
    }


    @Override
    public void onDonClick(String ChefOrderId) {
        //       Log.d(TAG,"## button click"+ChefOrderId);
        sendToServer(ChefOrderId);
        chefOrderList.invalidateViews();

//        chefOrderAdapter.refresh(1);
//        chefOrderAdapter.notifyDataSetChanged();



    }
    public void sendToServer(String OrderId)
    {
        if(NetworkUtils.isActiveNetworkAvailable(getContext()))
        {
            ChefOrderCompleted chefOrderCompleted = new ChefOrderCompleted(OrderId);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(chefOrderCompleted);
            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CHEF_ORDER_PLACE,serializedJsonString);
            mServerSyncManager.uploadDataToServer(tableDataDTO);
            mServerSyncManager.syncDataWithServer(true);
            //  finish();
        }else
        {
            showMyDialog(mContext);
        }



    }
    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {

        int errorCode = -1;
        String message = null;
        /*try
        {

            errorCode = data.getInt(String.valueOf(errorCode));
            message =data.getString("message");
//            Log.d(TAG,"## Order Data"+ data.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        if(errorCode == 0)
        {
            Toast.makeText(getActivity().getApplicationContext(), "data send to the server", Toast.LENGTH_LONG).show();
            chefOrderAdapter.refresh(1);
            chefOrderAdapter.notifyDataSetChanged();
        }
*/
//        chefOrderAdapter.refresh(1);
//        chefOrderAdapter.notifyDataSetChanged();
    }



    static {
        UIHandler = new Handler(Looper.getMainLooper());


    }


    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
       // chefOrderAdapter.notifyDataSetChanged();
//        chefOrderAdapter.refresh(1);
//        chefOrderAdapter.notifyDataSetChanged();
//
//       // chefOrderAdapter.notifyDataSetInvalidated();

    }



    @Override
    public void onResume() {
        super.onResume();
//        list.clear();
//        list.addAll(mDbRepository.getOrderHeadesInAsc(1));
//        chefOrderAdapter.notifyDataSetChanged();



    }

}
