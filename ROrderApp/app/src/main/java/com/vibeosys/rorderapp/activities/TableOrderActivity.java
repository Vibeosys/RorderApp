package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.OrderSummaryAdapter;
import com.vibeosys.rorderapp.data.OrderDetailsDTO;
import com.vibeosys.rorderapp.data.OrderHeaderDTO;
import com.vibeosys.rorderapp.data.OrdersDbDTO;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.data.UploadOrderDetails;
import com.vibeosys.rorderapp.data.UploadOrderHeader;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.ROrderDateUtils;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by akshay on 04-02-2016.
 */
public class TableOrderActivity extends BaseActivity implements OrderSummaryAdapter.ButtonListener, ServerSyncManager.OnStringResultReceived, OrderSummaryAdapter.PlaceOrderListener {

    ExpandableListView ordersList;
    OrderSummaryAdapter adapter;
    ArrayList<OrderHeaderDTO> list = new ArrayList<>();
    TableCommonInfoDTO tableCommonInfo;
    private int mTableNo;
    private int mTableId;
    private String mCustId;
    private OrderHeaderDTO mCurrentOrder;
    private UUID orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tableCommonInfo = getIntent().getParcelableExtra("tableCustInfo");
        mTableNo = tableCommonInfo.getTableNo();
        mTableId = tableCommonInfo.getTableId();
        mCustId = tableCommonInfo.getCustId();
        setContentView(R.layout.activity_table_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ordersList = (ExpandableListView) findViewById(R.id.expListViewForTableOrder);
        //ordersList.setGroupIndicator(getResources().getDrawable(R.drawable.expand_indicator));
        list = mDbRepository.getOrdersOfTable(mTableId, mCustId);
        mCurrentOrder = mDbRepository.getOrederDetailsFromTemp(mTableId, mSessionManager.getUserId(), mCustId);
        mDbRepository.getOrederDetailsGroupByID(list);
        list.add(0, mCurrentOrder);
        adapter = new OrderSummaryAdapter(getApplicationContext(), list);
        ordersList.setAdapter(adapter);
        ordersList.expandGroup(0);
        ordersList.setDividerHeight(2);
        ordersList.setGroupIndicator(null);
        ordersList.setClickable(true);
        adapter.setButtonListner(this);
        //adapter.onGroupExpanded(0);
        adapter.notifyDataSetChanged();
        adapter.setPlaceOrderClick(this);
        mServerSyncManager.setOnStringResultReceived(this);
        ordersList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //parent.expandGroup(groupPosition);
                return false;
            }
        });
        ordersList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
        ordersList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        ordersList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                return false;
            }
        });
    }

    @Override
    public void onButtonClickListener(int id, int position, int value, OrderDetailsDTO orderMenu) {
        if (id == R.id.imgOrderMinus)
            if (value > 0)
                orderMenu.setOrderQuantity(value - 1);
            else
                Toast.makeText(getApplicationContext(), "Quantity Should be greater than 0", Toast.LENGTH_LONG).show();
        if (id == R.id.imgOrderPluse)
            orderMenu.setOrderQuantity(value + 1);
        //Collections.sort(allMenus);
        orderMenu.setOrderPrice(orderMenu.getOrderQuantity() * orderMenu.getMenuUnitPrice());
        mDbRepository.insertOrUpdateTempOrder(mTableId, mTableNo, orderMenu.getMenuId(), orderMenu.getOrderQuantity(), mCustId);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.order_summary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (item.getItemId() == R.id.placeOrder) {
            //Toast.makeText(getApplicationContext(),"Button is clicke",Toast.LENGTH_LONG).show();
            /*Intent i = new Intent(this, BillDetailsActivity.class);
            startActivity(i);*/

            placeOrder();

        }


        return super.onOptionsItemSelected(item);
    }

    private void placeOrder() {
        // OrderHeaderDTO currentOrder = mDbRepository.getOrederDetailsFromTemp(mTableId, mSessionManager.getUserId());
        if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
            List<OrderDetailsDTO> orderDetailsDTOs = mCurrentOrder.getOrderDetailsDTOs();
            ArrayList<UploadOrderDetails> sendDetails = new ArrayList<>();
            for (OrderDetailsDTO orderDetail : orderDetailsDTOs) {
                UploadOrderDetails sendOrder = new UploadOrderDetails(orderDetail.getMenuId(), orderDetail.getOrderQuantity());
                sendDetails.add(sendOrder);
            }
            orderId = UUID.randomUUID();
            UploadOrderHeader sendOrder = new UploadOrderHeader(orderId.toString(), mTableId, mCustId, sendDetails);
            Gson gson = new Gson();

            String serializedJsonString = gson.toJson(sendOrder);
            Log.d(TAG, "##" + serializedJsonString);
            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.PLACE_ORDER, serializedJsonString);
            mServerSyncManager.uploadDataToServer(tableDataDTO);
        } else {
            startActivityForResult(new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS), 0);
        }
    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {
        int errorCode = -1;
        String message = null;
        try {
            errorCode = data.getInt("errorCode");
            message = data.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (errorCode == 0) {
            // Order is Successfully Placed. Delete all temp entry and add data in order table.
            String currentDate = new ROrderDateUtils().getGMTCurrentDate();
            String currentTime = new ROrderDateUtils().getGMTCurrentTime();
            ArrayList<OrdersDbDTO> orders = new ArrayList<>();
            orders.add(new OrdersDbDTO(orderId.toString(), Integer.parseInt(message), mCustId,
                    Date.valueOf(currentDate), Time.valueOf(currentTime), Date.valueOf(currentDate),
                    Date.valueOf(currentDate), mTableId, mSessionManager.getUserId()));
            mDbRepository.insertOrders(orders);
            mDbRepository.clearUpdateTempData(mTableId, mTableNo, mCustId);
            mServerSyncManager.syncDataWithServer(true);
            Intent iMenu = new Intent(getApplicationContext(), TableMenusActivity.class);
            iMenu.putExtra("tableCustInfo", tableCommonInfo);
            startActivity(iMenu);
            finish();
            Toast.makeText(getApplicationContext(), getResources().getString
                    (R.string.order_place_success), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onPlaceOrderClick() {
        placeOrder();
    }
}
