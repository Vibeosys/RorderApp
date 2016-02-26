package com.vibeosys.rorderapp;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.vibeosys.rorderapp.activities.AboutUsActivity;
import com.vibeosys.rorderapp.activities.AddCustomerActivity;
import com.vibeosys.rorderapp.activities.BaseActivity;
import com.vibeosys.rorderapp.activities.BillDetailsActivity;
import com.vibeosys.rorderapp.activities.ChefOrdersDisplayActivity;
import com.vibeosys.rorderapp.activities.LoginActivity;
import com.vibeosys.rorderapp.activities.NotificationActivity;
import com.vibeosys.rorderapp.activities.SelectRestaurantActivity;
import com.vibeosys.rorderapp.activities.TableFilterActivity;
import com.vibeosys.rorderapp.activities.TableMenusActivity;
import com.vibeosys.rorderapp.adaptors.CustomerAdapter;
import com.vibeosys.rorderapp.adaptors.TableGridAdapter;
import com.vibeosys.rorderapp.data.BillDetailsDTO;
import com.vibeosys.rorderapp.data.CustomerDbDTO;
import com.vibeosys.rorderapp.data.HotelTableDTO;
import com.vibeosys.rorderapp.data.RestaurantTables;
import com.vibeosys.rorderapp.data.TableCategoryDTO;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.data.TableTransactionDbDTO;
import com.vibeosys.rorderapp.data.UploadOccupiedDTO;
import com.vibeosys.rorderapp.data.WaitingUserDTO;
import com.vibeosys.rorderapp.service.SyncService;
import com.vibeosys.rorderapp.util.AnalyticsApplication;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.ROrderDateUtils;
import com.vibeosys.rorderapp.util.UserAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    public static Handler UIHandler;
    TabLayout tab_layout;
    DrawerLayout drawer;
    GridView gridView;
    public static TableGridAdapter adapter;
    List<RestaurantTables> hotelTableDTOs;
    List<RestaurantTables> sortedTables;
    private Context mContext = this;
    TextView txtTotalCount;
    static int selectedCategory = 0;
    static boolean btnCancelFlag = false, chkMyservingFlag = false, chkUnoccupied = false;
    EditText txtSearch;
    // private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_main);
        setTitle(getResources().getString(R.string.title_search_table));
        ContextWrapper ctw = new ContextWrapper(getApplicationContext());
        File directory = ctw.getDir(mSessionManager.getDatabaseDirPath(), Context.MODE_PRIVATE);
        File dbFile = new File(directory, mSessionManager.getDatabaseFileName());

      /*  AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();*/

        if (!dbFile.exists()) {
            Intent selectRestoIntent = new Intent(getApplicationContext(), SelectRestaurantActivity.class);
            selectRestoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(selectRestoIntent);
            finish();
        } else if (!UserAuth.isUserLoggedIn()) {
            callLogin();
        } else if (mSessionManager.getUserRollId() == 2) {
            Intent selectRestoIntent = new Intent(getApplicationContext(), ChefOrdersDisplayActivity.class);
            selectRestoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(selectRestoIntent);
            finish();
        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            ImageButton fab = (ImageButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //callWaitingIntent();
                    //Show waiting dialog

                    showWaitingDialog();
                }
            });
            Intent syncServiceIntent = new Intent(Intent.ACTION_SYNC, null, this, SyncService.class);
            startService(syncServiceIntent);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            txtSearch = (EditText) findViewById(R.id.search);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            txtTotalCount = (TextView) findViewById(R.id.txtCount);
            gridView = (GridView) findViewById(R.id.gridview);
            gridView.setOnItemClickListener(this);
            hotelTableDTOs = mDbRepository.getTableRecords("");
            adapter = new TableGridAdapter(getApplicationContext(), hotelTableDTOs, mSessionManager.getUserId());
            gridView.setAdapter(adapter);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
            TextView txtUserName = (TextView) headerView.findViewById(R.id.txtHeaderWaiterName);
            txtUserName.setText(mSessionManager.getUserName());
            TextView txtRestaurantName = (TextView) headerView.findViewById(R.id.txtHeaderHotelName);
            txtRestaurantName.setText(mSessionManager.getUserRestaurantName());
            //     txtRestaurantName.setText(mSessionManager.getUserRestaurantName());
            txtTotalCount.setText("" + mDbRepository.getOccupiedTable() + " out of " + hotelTableDTOs.size() + " tables are occupied");

            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() == 0) {
                        adapter.refresh(mDbRepository.getTableRecords(""));
                    } else {
                        try {
                            sortedTables = sortAdapter(Integer.parseInt(s.toString()));
                            adapter.refresh(sortedTables);
                        } catch (Exception e) {
                            Log.e(TAG, "##" + e.toString());
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "You should Enter number", Toast.LENGTH_SHORT).show();
                        }

                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected String getScreenName() {
        return "Tables";
    }

    @Override
    protected void onResume() {
        super.onResume();
        //adapter.refresh(mDbRepository.getTableRecords(""));

        hitActivity();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        // SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() == 0) {

                } else {
                    try {
                        sortedTables = sortAdapter(Integer.parseInt(s));
                        adapter.refresh(sortedTables);
                    } catch (Exception e) {
                        Log.e(TAG, "##" + e.toString());
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "You should Enter number", Toast.LENGTH_SHORT).show();
                    }

                }
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.refresh(mDbRepository.getTableRecords(""));
                return false;
            }
        });*/

        return true;
    }

    public List<RestaurantTables> sortAdapter(int tableNo) {
        List<RestaurantTables> mHotelTables = new ArrayList<>();

        for (RestaurantTables table : mDbRepository.getTableRecords("")) {
            if (table.getmTableNo() == tableNo) {
                mHotelTables.add(table);
            }
        }
        return mHotelTables;
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
        if (id == R.id.filter) {
            mTracker.send(new HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("Filter").setValue(1)
                    .build());
            Intent iFilter = new Intent(this, TableFilterActivity.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Category", selectedCategory);
                jsonObject.put("chkMyservingFlag", chkMyservingFlag);
                jsonObject.put("chkUnoccupied", chkUnoccupied);
                jsonObject.put("btnFlag", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            iFilter.putExtra("json", jsonObject.toString());
            startActivityForResult(iFilter, 2);
        }
        if (id == R.id.notification) {
            Intent iWaitingList = new Intent(getApplicationContext(), NotificationActivity.class);
            startActivity(iWaitingList);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_waiting_list) {

            showWaitingDialog();
            //callWaitingIntent();
        } else if (id == R.id.nav_log_out) {
            UserAuth.CleanAuthenticationInfo();
            callLogin();
        } else if (id == R.id.about_us) {
            Intent aboutUsIntent = new Intent(getApplicationContext(), AboutUsActivity.class);
            aboutUsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(aboutUsIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void callLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        HotelTableDTO hotelTableDTO = (HotelTableDTO) adapter.getItem(position);
        if (hotelTableDTO.ismIsOccupied()) {
            String custId = mDbRepository.getCustmerIdFromTransaction(hotelTableDTO.getmTableId());
            Log.i(TAG, "## Customer Id " + custId);
            callToMenuIntent(hotelTableDTO.getmTableNo(), hotelTableDTO.getmTableId(), custId);
        } else {
            showReserveDialog(hotelTableDTO.getmTableNo(), hotelTableDTO.getmTableId());
        }
        Log.i(TAG, "##" + hotelTableDTO.getmTableNo() + "Is Clicked");
    }

    private void callToMenuIntent(int tableNo, int tableId, String custId) {


        TableCommonInfoDTO tableCommonInfoDTO = new TableCommonInfoDTO(tableId, custId, tableNo);
        BillDetailsDTO billDetailsDTO = mDbRepository.getBillDetailsRecords(custId);
        if (billDetailsDTO != null) {
            Intent intentBillDetails = new Intent(getApplicationContext(), BillDetailsActivity.class);
            intentBillDetails.putExtra("tableCustInfo", tableCommonInfoDTO);
//        intentOpenTableMenu.putExtra("TableNo", tableNo);
//        intentOpenTableMenu.putExtra("TableId", tableId);
            startActivity(intentBillDetails);
        } else {
            Intent intentOpenTableMenu = new Intent(getApplicationContext(), TableMenusActivity.class);
            intentOpenTableMenu.putExtra("tableCustInfo", tableCommonInfoDTO);
//        intentOpenTableMenu.putExtra("TableNo", tableNo);
//        intentOpenTableMenu.putExtra("TableId", tableId);
            startActivity(intentOpenTableMenu);
        }

    }

    private void showReserveDialog(final int tableNo, final int tableId) {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_table_reserve);
        dialog.setTitle("Reserve Table");
        final EditText txtCustomerName = (EditText) dialog.findViewById(R.id.txtCustomerName);
        TextView cancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView reserve = (TextView) dialog.findViewById(R.id.txtReserve);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Customer Entered in db", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                UUID custid = UUID.randomUUID();

                String customerName = txtCustomerName.getText().toString();
                CustomerDbDTO customer = new CustomerDbDTO(custid.toString(), customerName);
                //here inserting custmer to custmer table
                mDbRepository.insertCustomerDetails(customer);
                //getting current date here
                String currentDate = new ROrderDateUtils().getGMTCurrentDate();

                TableTransactionDbDTO tableTransactionDbDTO = new TableTransactionDbDTO(tableId, mSessionManager.getUserId(), custid.toString(), 0, currentDate);
                //here inserting records in table transaction
                mDbRepository.insertTableTransaction(tableTransactionDbDTO);

                mDbRepository.setOccupied(true, tableId);
                uploadToServer(customer, tableTransactionDbDTO, tableId);
                adapter.refresh(mDbRepository.getTableRecords(""));
                callToMenuIntent(tableNo, tableId, custid.toString());
            }
        });
        dialog.show();
    }

    private void uploadToServer(CustomerDbDTO customer, TableTransactionDbDTO tableTransaction, int tableId) {
        TableDataDTO[] tableDataDTOs = new TableDataDTO[3];
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(customer);
        tableDataDTOs[0] = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
        // mServerSyncManager.uploadDataToServer(tableDataDTO);

        UploadOccupiedDTO occupiedDTO = new UploadOccupiedDTO(tableId, 1);
        String serializedTableString = gson.toJson(occupiedDTO);
        tableDataDTOs[1] = new TableDataDTO(ConstantOperations.TABLE_OCCUPIED, serializedTableString);
        // mServerSyncManager.uploadDataToServer(tableDataDTO);

        String serializedTableTransaction = gson.toJson(tableTransaction);
        tableDataDTOs[2] = new TableDataDTO(ConstantOperations.TABLE_TRANSACTION, serializedTableTransaction);
        mServerSyncManager.uploadDataToServer(tableDataDTOs);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2

        if (requestCode == 2 && data != null) {
            JSONObject json;
            String jsonString = data.getStringExtra("json");
            Log.d(TAG, "##" + jsonString);
            try {
                json = new JSONObject(jsonString);
                btnCancelFlag = json.getBoolean("btnFlag");
                chkMyservingFlag = json.getBoolean("chkMyservingFlag");
                chkUnoccupied = json.getBoolean("chkUnoccupied");
                selectedCategory = json.getInt("Category");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (btnCancelFlag) {
                String where = null;
                if (chkMyservingFlag) {
                    if (where == null) {
                        where = " where tt.userId=" + mSessionManager.getUserId();
                    } else {
                        where = where + " and tt.userId=" + mSessionManager.getUserId();
                    }

                }
                if (chkUnoccupied) {
                    if (where == null) {
                        where = " where rs.IsOccupied=0";
                    } else {
                        where = where + " and rs.IsOccupied=0";
                    }

                }
                if (selectedCategory != -1 && selectedCategory != 0) {
                    if (where == null) {
                        where = " where rs.TableCategoryId=" + selectedCategory;
                    } else {
                        where = where + " and rs.TableCategoryId=" + selectedCategory;
                    }

                }
                if (!chkMyservingFlag && !chkUnoccupied && selectedCategory == 0 || selectedCategory == -1) {
                    adapter.refresh(mDbRepository.getTableRecords(""));
                }
                adapter.refresh(mDbRepository.getTableRecords(where));

            } else {
                Toast.makeText(getApplicationContext(), "All Filters are removed", Toast.LENGTH_SHORT).show();
                adapter.refresh(mDbRepository.getTableRecords(""));
            }
        } else {

        }
    }

    private void callWaitingIntent() {
        Intent iWaitingList = new Intent(getApplicationContext(), AddCustomerActivity.class);
        startActivity(iWaitingList);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

    static {
        UIHandler = new Handler(Looper.getMainLooper());

    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);

    }

    private void showWaitingDialog() {
        final Dialog dlg = new Dialog(MainActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = getLayoutInflater().inflate(R.layout.dialog_waiting_list, null);
        dlg.setContentView(view);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        ArrayList<WaitingUserDTO> mWaitingList = mDbRepository.getWaitingList();
        final EditText mTxtCount = (EditText) dlg.findViewById(R.id.txtCustCount);
        final EditText mTxtName = (EditText) dlg.findViewById(R.id.txtCustomerName);
        final ImageButton btnClose = (ImageButton) dlg.findViewById(R.id.fabClose);
        TextView txtTitle = (TextView) dlg.findViewById(R.id.dlg_title);
        txtTitle.setText("Waiting List");
        Button mBtnAdd = (Button) dlg.findViewById(R.id.btnAdd);
        ListView mListCustomer = (ListView) dlg.findViewById(R.id.customerList);
        final CustomerAdapter mCustomerAdapter = new CustomerAdapter(getApplicationContext(), mWaitingList);
        mListCustomer.setAdapter(mCustomerAdapter);
        mCustomerAdapter.notifyDataSetChanged();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrongCredential = false;
                View focus = null;
                mTxtName.setError(null);
                mTxtCount.setError(null);

                String customerName = mTxtName.getText().toString();
                String strCount = mTxtCount.getText().toString();

                if (TextUtils.isEmpty(customerName)) {
                    mTxtName.setError("Customer Name is Required");
                    focus = mTxtName;
                    wrongCredential = true;
                }
                if (TextUtils.isEmpty(strCount)) {
                    mTxtCount.setError("Customer Count is Required");
                    focus = mTxtCount;
                    wrongCredential = true;
                }

                if (wrongCredential) {
                    focus.requestFocus();
                } else {
                    UUID custid = UUID.randomUUID();
                    int customerCount = 0;
                    try {
                        customerCount = Integer.parseInt(strCount);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "## Insert Count null pointer" + e.toString());
                    }

                    CustomerDbDTO customer = new CustomerDbDTO(custid.toString(), customerName);
                    mDbRepository.insertCustomerDetails(customer);
                    String currentDate = new ROrderDateUtils().getGMTCurrentDate();
                    Log.d(TAG, "##" + currentDate);
                    TableTransactionDbDTO tableTransaction = new TableTransactionDbDTO(custid.toString(), 1, customerCount);
                    mDbRepository.insertTableTransaction(tableTransaction);
                    Toast.makeText(getApplicationContext(), "Customer is Added successfully", Toast.LENGTH_SHORT).show();
                    mCustomerAdapter.refresh(mDbRepository.getWaitingList());
                    mTxtCount.setText("");
                    mTxtName.setText("");
                    Gson gson = new Gson();
                    TableDataDTO[] tableDataDTOs = new TableDataDTO[2];
                    String serializedJsonString = gson.toJson(customer);
                    //TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
                    tableDataDTOs[0] = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
                    String serializedTableTransaction = gson.toJson(tableTransaction);
                    tableDataDTOs[1] = new TableDataDTO(ConstantOperations.ADD_WAITING_CUSTOMER, serializedTableTransaction);
                    mServerSyncManager.uploadDataToServer(tableDataDTOs);
                }
            }
        });

        mListCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WaitingUserDTO waiting = (WaitingUserDTO) mCustomerAdapter.getItem(position);
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.dialog_table_alocate);
                setTitle(getResources().getString(R.string.dialog_title_Allocate));
                final EditText txtTableNo = (EditText) dialog.findViewById(R.id.txtTableNumber);
                TextView txtReserve = (TextView) dialog.findViewById(R.id.txtReserve);
                TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);

                txtReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strTableNo = txtTableNo.getText().toString();
                        if (TextUtils.isEmpty(strTableNo)) {
                            txtTableNo.setError(getResources().getString(R.string.error_table_no));
                            txtTableNo.requestFocus();
                        } else {
                            int tableId = mDbRepository.getTaleId(Integer.parseInt(strTableNo));
                            if (tableId == -1) {
                                Toast.makeText(getApplicationContext(), "Table is Already Occupied", Toast.LENGTH_SHORT).show();
                            } else if (tableId == 0) {
                                Toast.makeText(getApplicationContext(), "No Such Table found", Toast.LENGTH_SHORT).show();
                            } else {
                                TableTransactionDbDTO tableTransactionDbDTO = new TableTransactionDbDTO(tableId,
                                        mSessionManager.getUserId(), waiting.getmCustomerId(), 0,
                                        waiting.getmArrivalTime(), waiting.getmOccupancy());
                                mDbRepository.updateTableTransaction(tableTransactionDbDTO);
                                mDbRepository.setOccupied(true, tableId);

                                UploadOccupiedDTO occupiedDTO = new UploadOccupiedDTO(tableId, 1);
                                TableDataDTO[] tableDataDTOs = new TableDataDTO[2];
                                Gson gson = new Gson();
                                String serializedJsonString = gson.toJson(occupiedDTO);
                                tableDataDTOs[0] = new TableDataDTO(ConstantOperations.TABLE_OCCUPIED, serializedJsonString);
                                String serializedTableTransaction = gson.toJson(tableTransactionDbDTO);
                                tableDataDTOs[1] = new TableDataDTO(ConstantOperations.TABLE_TRANSACTION, serializedTableTransaction);
                                mServerSyncManager.uploadDataToServer(tableDataDTOs);
                                dialog.dismiss();
                                dlg.dismiss();
                                adapter.refresh(mDbRepository.getTableRecords(""));
                            }

                        }

                    }
                });

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        dlg.show();
    }

}
