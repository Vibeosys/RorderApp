package com.vibeosys.rorderapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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
 * Created by shrinivas on 12-02-2016.
 */
public class ChefOrdersDisplayActivity  extends  BaseActivity implements ChefOrderAdapter.OnDoneClickListener,ServerSyncManager.OnStringResultReceived{
    ExpandableListView chefOrderList;
    ChefOrderAdapter chefOrderAdapter;
    ArrayList<ChefOrderDetailsDTO> list  =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chef_main_screen);
        chefOrderList = (ExpandableListView) findViewById(R.id.expListViewForChef);
        list = mDbRepository.getOrderHeadesInAsc();
        chefOrderAdapter = new ChefOrderAdapter(getApplicationContext(),list,mDbRepository,mServerSyncManager);
        chefOrderList.setAdapter(chefOrderAdapter);
        chefOrderAdapter.setOnDoneClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        chefOrderList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
               /* ChefOrderDetailsDTO mChefOrderDetailsDTO = (ChefOrderDetailsDTO) chefOrderAdapter.getGroup(groupPosition);
                mChefOrderDetailsDTO.setmMenuChild(mDbRepository.getChefMenu(mChefOrderDetailsDTO.getmNewOrderId()));
                chefOrderAdapter.notifyDataSetChanged();*/
            }
        });



    }

    @Override
    public void onDonClick(String ChefOrderId) {
        Log.d(TAG,"## button click"+ChefOrderId);
        sendToServer(ChefOrderId);

    }
    public void sendToServer(String OrderId)
    {
        Log.d(TAG, "## send to server function" + OrderId);
       // Toast.makeText(getApplicationContext(),"button is clicked"+ OrderId.toString(),Toast.LENGTH_LONG).show();
        ChefOrderCompleted chefOrderCompleted = new ChefOrderCompleted(OrderId);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(chefOrderCompleted);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CHEF_ORDER_PLACE,serializedJsonString);
        mServerSyncManager.uploadDataToServer(tableDataDTO);
        mServerSyncManager.syncDataWithServer(false);
        finish();
    }
    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {

        int errorCode = -1;
        String message = null;
        try
        {

            errorCode = data.getInt(String.valueOf(errorCode));
            message =data.getString("message");
            Log.d(TAG,"## Order Data"+ data.toString());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        if(errorCode == 0)
        {
            Toast.makeText(getApplicationContext(),"data send to the server",Toast.LENGTH_LONG).show();
        }

    }
}
