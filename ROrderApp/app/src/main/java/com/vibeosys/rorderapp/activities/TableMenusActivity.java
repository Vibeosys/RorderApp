package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.OrderListAdapter;
import com.vibeosys.rorderapp.data.OrderMenuDTO;
import com.vibeosys.rorderapp.data.SelectedMenusDTO;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.data.UploadBillGenerate;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TableMenusActivity extends BaseActivity implements
        OrderListAdapter.CustomButtonListener, View.OnClickListener, ServerSyncManager.OnStringResultReceived,ServerSyncManager.OnDownloadReceived {

    private TableCommonInfoDTO tableCommonInfoDTO;
    private OrderListAdapter orderListAdapter;
    private List<OrderMenuDTO> allMenus;
    private ListView listMenus;
    private TextView txtTotalAmount, txtTotalItems, txtBillGenerate;
    private int mTableId, mTableNo;
    private String custId;
    private LinearLayout llCurrentOrder;
    //  private ArrayList<OrderMenuDTO> mSelectedItems= new ArrayList<>();

    private int mCount = 0;

    //List<OrderMenuDTO> sortingMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_menus);
        tableCommonInfoDTO = getIntent().getParcelableExtra("tableCustInfo");
        mTableId = tableCommonInfoDTO.getTableId();
        mTableNo = tableCommonInfoDTO.getTableNo();
        custId = tableCommonInfoDTO.getCustId();
        setTitle(getResources().getString(R.string.title_search_cuisine));

        listMenus = (ListView) findViewById(R.id.listMenus);
        allMenus = mDbRepository.getOrderMenu(custId);
//        mTableId = getIntent().getIntExtra("TableId", 0);
//        mTableNo = getIntent().getExtras().getInt("TableNo");
        //sortingMenu=mDbRepository.getOrderMenu();
        txtTotalItems = (TextView) findViewById(R.id.txtTotalItems);
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalRs);
        txtBillGenerate = (TextView) findViewById(R.id.txtGenerateBill);
        llCurrentOrder = (LinearLayout) findViewById(R.id.llCurrentOrder);
        orderListAdapter = new OrderListAdapter(allMenus, getApplicationContext());
        orderListAdapter.setCustomButtonListner(this);
        listMenus.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();
        llCurrentOrder.setOnClickListener(this);
        txtBillGenerate.setOnClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnDownloadReceived(this);
        /// changes for Tool bar  01/02/2016 by Shrinivas
        displayMenuPriceAndItems();
      /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //visiblity for bill generate button apply this as validation
      /*  int custIdFromOrders = mDbRepository.getCustmerCount(custId);
        if(custIdFromOrders == 0)
        {
            txtBillGenerate.setVisibility(View.GONE);
        }
        else
        {
            txtBillGenerate.setVisibility(View.VISIBLE);
        }*/


    }

    public void sortList(String search) {

        for (OrderMenuDTO menu : this.allMenus) {
            if (menu.getmCategory().toLowerCase().contains(search.toLowerCase())) {
                menu.setmShow(OrderMenuDTO.SHOW);
            } else if (menu.getmMenuTitle().toLowerCase().contains(search.toLowerCase())) {
                menu.setmShow(OrderMenuDTO.SHOW);
            } else if (menu.getmTags().toLowerCase().contains(search.toLowerCase())) {
                menu.setmShow(OrderMenuDTO.SHOW);
            } else {
                menu.setmShow(OrderMenuDTO.HIDE);
            }
        }
        Collections.sort(allMenus);
    }


    private void displayMenuPriceAndItems() {
        mCount = 0;
        ArrayList<OrderMenuDTO> mSelectedItems = new ArrayList<>();
        for (OrderMenuDTO menu : allMenus) {
            if (menu.getmQuantity() > 0) {
                mSelectedItems.add(menu);
            }
        }
        mCount = mSelectedItems.size();
        SelectedMenusDTO selectedMenusDTO = new SelectedMenusDTO(mSelectedItems);
        txtTotalAmount.setText(String.format(String.format("%.2f", selectedMenusDTO.getTotalBillAmount())) + " Rs.");
        txtTotalItems.setText(selectedMenusDTO.getTotalItems() + " Items are selected");
    }

    @Override
    public void onButtonClickListener(int id, int position, int value, OrderMenuDTO orderMenu) {
        if (id == R.id.imgMinus)
            if (value > 0)
                orderMenu.setmQuantity(value - 1);
            else
                Toast.makeText(getApplicationContext(), "Quantity Should be greater than 0", Toast.LENGTH_LONG).show();
        if (id == R.id.imgPlus)
            orderMenu.setmQuantity(value + 1);
        //Collections.sort(allMenus);
        displayMenuPriceAndItems();
        mDbRepository.insertOrUpdateTempOrder(mTableId, mTableNo, orderMenu.getmMenuId(), orderMenu.getmQuantity(), custId);
        orderListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.table_menus, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0 || s.length() < 3) {
                    // sortList
                    for (OrderMenuDTO menu : allMenus) {
                        menu.setmShow(OrderMenuDTO.SHOW);
                    }
                    Collections.sort(allMenus);
                    orderListAdapter.notifyDataSetChanged();
                    //((OrderListAdapter) listMenus.getAdapter()).refresh(allMenus);
                }
                if (s.length() >= 3) {
                    sortList(s.toString());
                    orderListAdapter.notifyDataSetChanged();
                }
                displayMenuPriceAndItems();
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.llCurrentOrder) {
            /*List<OrdersDbDTO> orderInserts=new ArrayList<>();
            orderInserts.add(new OrdersDbDTO("" + 2, 1234, true, Date.valueOf("2016-02-02"),
                    Time.valueOf("8:50:22"), Date.valueOf("2016-02-02"), Date.valueOf("2016-02-02"), 1, "" + 1, 61));
            mDbRepository.insertOrders(orderInserts);

            List<OrderDetailsDbDTO> orderDetailsDbDTOList=new ArrayList<>();
            orderDetailsDbDTOList.add(new OrderDetailsDbDTO(3,23,1,Date.valueOf("2016-02-02"),Date.valueOf("2016-02-02"),""+2,3,"Kabab"));
            orderDetailsDbDTOList.add(new OrderDetailsDbDTO(4,36,1,Date.valueOf("2016-02-02"),Date.valueOf("2016-02-02"),""+2,4,"Garlic Bread"));
            mDbRepository.insertOrderDetails(orderDetailsDbDTOList);*/

            // TableCommonInfoDTO tableCommonInfoDTO = new TableCommonInfoDTO(1, "DEF", 10);
            if (mCount == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_item_selected), Toast.LENGTH_SHORT).show();
            } else {
                Intent tableOrderIntent = new Intent(getApplicationContext(), TableOrderActivity.class);
                tableOrderIntent.putExtra("tableCustInfo", tableCommonInfoDTO);
                startActivity(tableOrderIntent);
                finish();
            }

        }
        if (id == R.id.txtGenerateBill) {
            //  Toast.makeText(getApplicationContext(),"bill genrate is cliked",Toast.LENGTH_LONG).show();
            if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {

                genrateBill();
            } else {
                /* if network is not available*/
            }
        }

    }

    public void genrateBill() {
        UploadBillGenerate uploadBillGenerate = new UploadBillGenerate(mTableId, custId);
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(uploadBillGenerate);
        Log.d(TAG, "##" + serializedJsonString);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.GENRATE_BILL, serializedJsonString);
        mServerSyncManager.uploadDataToServer(tableDataDTO);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {
        int errorCode = -1;
        String message = null;


        try {
            errorCode = data.getInt("errorCode");
            message = data.getString("message");
            Log.d(TAG, "##" + errorCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (errorCode == 0) {
            /*Successfully send data*/
            Toast.makeText(getApplicationContext(), "Data is send to server", Toast.LENGTH_LONG).show();
            Log.d(TAG, "##" + errorCode);
            mServerSyncManager.syncDataWithServer(true);


        } else {
            Toast.makeText(getApplicationContext(), "response " + errorCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDownloadResultReceived(@NonNull Map<String, Integer> results) {
        Intent iMenu = new Intent(getApplicationContext(), BillDetailsActivity.class);
        iMenu.putExtra("tableCustInfo", tableCommonInfoDTO);
        startActivity(iMenu);
        finish();
    }
}
