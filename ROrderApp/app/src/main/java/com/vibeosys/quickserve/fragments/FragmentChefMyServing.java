package com.vibeosys.quickserve.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.adaptors.ChefOrderAdapter;
import com.vibeosys.quickserve.adaptors.ChefTabListAdapter;
import com.vibeosys.quickserve.data.ChefOrderCompleted;
import com.vibeosys.quickserve.data.ChefOrderDetailsDTO;
import com.vibeosys.quickserve.data.TableDataDTO;
import com.vibeosys.quickserve.util.ConstantOperations;
import com.vibeosys.quickserve.util.NetworkUtils;
import com.vibeosys.quickserve.util.ServerSyncManager;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shrinivas on 15-02-2016.
 */
public class FragmentChefMyServing extends BaseFragment implements
        ChefOrderAdapter.OnDoneClickListener, ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnStringErrorReceived {

    private ExpandableListView chefOrderList;
    private ListView listView;
    private ChefTabListAdapter chefTabListAdapter;
    public static ChefOrderAdapter chefOrderAdapter;
    public static Handler UIHandler;
    ProgressDialog dialog;
    private Context mContext = this.getContext();
    private ArrayList<ChefOrderDetailsDTO> list = new ArrayList<>();

    //private Context context=this;
    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int count = 0;

        View view = inflater.inflate(R.layout.chef_expandable_list, container, false);
        count = mDbRepository.checkOrders();
        list = mDbRepository.getOrderHeadesInAsc(1);
//        Intent i = new Intent(Intent.ACTION_SYNC,null,this.getContext(),SyncService.class);
//        getContext().startService(i);

//        Intent chefServices = new Intent(this.getContext(),ChefService.class);
//        getContext().startService(chefServices);


        DisplayMetrics matrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(matrics);
        int widthPixel = matrics.widthPixels;
        int heightPixel = matrics.heightPixels;

        float scaleFactor = matrics.density;


        float widthDp = widthPixel / scaleFactor;
        float heigthDp = heightPixel / scaleFactor;

        float smallWidth = Math.min(widthDp, heigthDp);


        if (widthDp <= 450 && widthDp < 590) {
            // Toast.makeText(getContext(), "finally you are at normall screen", Toast.LENGTH_LONG).show();
        }

        float widthDpi = matrics.xdpi;
        float heightDpi = matrics.ydpi;
        float widthInches = widthPixel / widthDpi;
        float heightInches = heightPixel / heightDpi;

        double diagonalInches = Math.sqrt(
                (widthInches * widthInches)
                        + (heightInches * heightInches));


        if (count == 0) {
            // Toast.makeText(getContext(),"No records to display",Toast.LENGTH_LONG).show();
            String stringTitle = "No Records";
            String stringMessage = "No Records Available to display";
            customAlterDialog(stringTitle, stringMessage);
        }

        chefOrderList = (ExpandableListView) view.findViewById(R.id.expListViewForChef);
        list = mDbRepository.getOrderHeadesInAsc(1);
        //ChefOrderDetailsDTO tiemDifference = list.get(0);


        chefOrderAdapter = new ChefOrderAdapter(getActivity().getApplicationContext(), list, mDbRepository);
        chefOrderList.setAdapter(chefOrderAdapter);

        chefOrderAdapter.setOnDoneClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        chefOrderAdapter.notifyDataSetChanged();

        return view;
    }


    @Override
    public void onDonClick(String ChefOrderId) {
        //       Log.d(TAG,"## button click"+ChefOrderId);
        if (!NetworkUtils.isActiveNetworkAvailable(getContext())) {
            String stringTitle = getResources().getString(R.string.error_msg_title_for_network);
            String stringMessage = getResources().getString(R.string.error_msg_for_select_restaurant);
            customAlterDialog(stringTitle, stringMessage);

        } else {
            String dialogMessage = getResources().getString(R.string.dialog_fragment_msg);
            dialog = ProgressDialog.show(getContext(), "", dialogMessage, true);
            dialog.show();
            sendToServer(ChefOrderId);
            mServerSyncManager.syncDataWithServer(true);
            chefOrderList.invalidateViews();
        }


    }

    public void sendToServer(String OrderId) {
        if (NetworkUtils.isActiveNetworkAvailable(getContext())) {
            ChefOrderCompleted chefOrderCompleted = new ChefOrderCompleted(OrderId);
            Gson gson = new Gson();
            String serializedJsonString = gson.toJson(chefOrderCompleted);
            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CHEF_ORDER_PLACE, serializedJsonString);
            mServerSyncManager.uploadDataToServer(tableDataDTO);
            mServerSyncManager.syncDataWithServer(true);
            //  finish();
        } else {
            showMyDialog(mContext);
        }


    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {

        int errorCode = -1;
        String message = null;
        dialog.dismiss();
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


    }


    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    protected String getScreenName() {
        return "Chef Mobile Dashboard";
    }

    @Override
    public void onStart() {


        super.onStart();
    }
    @Override
    public void onStingErrorReceived(@NonNull VolleyError error) {
        dialog.dismiss();
        String StringTitle ="Server Error";
        String StringMessage="Server Error,Try again";
        customAlterDialog(StringTitle, StringMessage);
    }
}
