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
import android.widget.Toast;

import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.ChefOrderAdapter;
import com.vibeosys.rorderapp.data.ChefOrderCompleted;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shrinivas on 15-02-2016.
 */
public class FragmentChefMyServing extends BaseFragment implements
        ChefOrderAdapter.OnDoneClickListener,ServerSyncManager.OnStringResultReceived{
    private ExpandableListView chefOrderList;
    public static ChefOrderAdapter chefOrderAdapter;
    public static Handler UIHandler;
    private ArrayList<ChefOrderDetailsDTO> list  =new ArrayList<>();
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chef_expandable_list,container,false);

        chefOrderList = (ExpandableListView) view.findViewById(R.id.expListViewForChef);
        list = mDbRepository.getOrderHeadesInAsc(1);
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


//        chefOrderAdapter.refresh(1);
//        chefOrderAdapter.notifyDataSetChanged();



    }
    public void sendToServer(String OrderId)
    {

        ChefOrderCompleted chefOrderCompleted = new ChefOrderCompleted(OrderId);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(chefOrderCompleted);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CHEF_ORDER_PLACE,serializedJsonString);
        mServerSyncManager.uploadDataToServer(tableDataDTO);
        mServerSyncManager.syncDataWithServer(true);
        //  finish();
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
        chefOrderAdapter.refresh(1);
        chefOrderAdapter.notifyDataSetChanged();
    }



    static {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }
}
