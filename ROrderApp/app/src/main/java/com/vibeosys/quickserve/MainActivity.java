package com.vibeosys.quickserve;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vibeosys.quickserve.activities.AboutUsActivity;
import com.vibeosys.quickserve.activities.AddCustomerActivity;
import com.vibeosys.quickserve.activities.BaseActivity;
import com.vibeosys.quickserve.activities.ChefOrdersDisplayActivity;
import com.vibeosys.quickserve.activities.CompletedOrdersScreen;
import com.vibeosys.quickserve.activities.DiagnosticActivity;
import com.vibeosys.quickserve.activities.LoginActivity;
import com.vibeosys.quickserve.activities.NotificationActivity;
import com.vibeosys.quickserve.activities.SelectRestaurantActivity;
import com.vibeosys.quickserve.activities.SettingPrinterActivity;
import com.vibeosys.quickserve.activities.TableFilterActivity;
import com.vibeosys.quickserve.adaptors.MainActivityAdapter;
import com.vibeosys.quickserve.data.DeliveryDTO;
import com.vibeosys.quickserve.data.RestaurantTables;
import com.vibeosys.quickserve.data.TakeAwayDTO;
import com.vibeosys.quickserve.fragments.FragmentDelivery;
import com.vibeosys.quickserve.fragments.FragmentTakeAway;
import com.vibeosys.quickserve.fragments.FragmentWaiterTable;
import com.vibeosys.quickserve.service.SyncService;
import com.vibeosys.quickserve.util.UserAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    TabLayout tab_layout;
    DrawerLayout drawer;

    private Context mContext = this;

    static int selectedCategory = 0;
    static boolean btnCancelFlag = false, chkMyservingFlag = false, chkUnoccupied = false;
    private EditText txtSearch;
    // private Tracker mTracker;
    private int selectedTab;
    List<RestaurantTables> sortedTables;
    ArrayList<TakeAwayDTO> takeAwayDTOSorted;

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

            Intent syncServiceIntent = new Intent(Intent.ACTION_SYNC, null, this, SyncService.class);
            startService(syncServiceIntent);

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);


            drawer.setDrawerListener(toggle);
            toggle.syncState();
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
            TextView txtUserName = (TextView) headerView.findViewById(R.id.txtHeaderWaiterName);
            txtUserName.setText(mSessionManager.getUserName());
            TextView txtRestaurantName = (TextView) headerView.findViewById(R.id.txtHeaderHotelName);
            txtRestaurantName.setText(mSessionManager.getUserRestaurantName());
            //     txtRestaurantName.setText(mSessionManager.getUserRestaurantName());
            txtSearch = (EditText) findViewById(R.id.search);

            tab_layout = (TabLayout) findViewById(R.id.tab_layout);

            tab_layout.addTab(tab_layout.newTab().setText("DINE IN"));

            tab_layout.addTab(tab_layout.newTab().setText("TAKE AWAY"));

            tab_layout.addTab(tab_layout.newTab().setText("DELIVERY"));

            tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
            tab_layout.setSelectedTabIndicatorHeight(4);
            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            final MainActivityAdapter mainActivityAdapteradapter = new MainActivityAdapter
                    (getSupportFragmentManager(), tab_layout.getTabCount());
            viewPager.setAdapter(mainActivityAdapteradapter);

            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
            tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    selectedTab = tab.getPosition();
                    viewPager.setCurrentItem(selectedTab);
                    if (selectedTab == 0) {
                        txtSearch.setHint(R.string.search_table);
                        txtSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
                    } else if (selectedTab == 1) {
                        txtSearch.setHint(R.string.search_order);
                        txtSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                    } else if (selectedTab == 2) {
                        txtSearch.setHint(R.string.search_order);
                        txtSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (selectedTab == 0) {
                        if (s.length() == 0) {
                            FragmentWaiterTable.adapter.refresh(mDbRepository.getTableRecords(""));


                        } else {
                            try {
                                sortedTables = sortAdapter(Integer.parseInt(s.toString()));
                                FragmentWaiterTable.adapter.refresh(sortedTables);
                            } catch (Exception e) {
                                Log.e(TAG, "##" + e.toString());
                                e.printStackTrace();
                                //Toast.makeText(getActivity().getApplicationContext(), "You should Enter number", Toast.LENGTH_SHORT).show();
                            }

                        }
                        FragmentWaiterTable.adapter.notifyDataSetChanged();
                    }
                    if (selectedTab == 1) {
                        if (s.length() <= 2) {
                            ArrayList<TakeAwayDTO> list = mDbRepository.getTakeAwayList();
                            mDbRepository.setTakeAwayStatus(list);
                            FragmentTakeAway.gridAdapter.refresh(list);
                        } else {
                            try {
                                FragmentTakeAway.gridAdapter.refresh(sortList(mDbRepository.getTakeAwayList(), s.toString()));
                            } catch (Exception e) {
                                Log.e(TAG, "##" + e.toString());
                                e.printStackTrace();
                                //Toast.makeText(getActivity().getApplicationContext(), "You should Enter number", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    if (selectedTab == 2) {
                        if (s.length() <= 2) {
                            ArrayList<DeliveryDTO> list = mDbRepository.getDeliveryList();
                            mDbRepository.setDeliveryStatus(list);
                            FragmentDelivery.gridAdapter.refresh(list);
                        } else {
                            try {
                                FragmentDelivery.gridAdapter.refresh(sortDeliveryList(mDbRepository.getDeliveryList(), s.toString()));
                            } catch (Exception e) {
                                Log.e(TAG, "##" + e.toString());
                                e.printStackTrace();
                                //Toast.makeText(getActivity().getApplicationContext(), "You should Enter number", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {
                    sendEventToGoogle("Action", "Table Filter");
                }
            });

        }

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

    private ArrayList<TakeAwayDTO> sortList(ArrayList<TakeAwayDTO> takeAwayDTOs, String search) {
        ArrayList<TakeAwayDTO> takeAway = new ArrayList<TakeAwayDTO>();

        for (TakeAwayDTO takeAwayDTO : takeAwayDTOs) {
            if (takeAwayDTO.getmCustName().toLowerCase().contains(search.toLowerCase())) {
                takeAway.add(takeAwayDTO);
            } else if (String.valueOf(takeAwayDTO.getmTakeawayNo()).contains(search.toLowerCase())) {
                takeAway.add(takeAwayDTO);
            } else if (takeAwayDTO.getmCustAddress().toLowerCase().contains(search.toLowerCase())) {
                takeAway.add(takeAwayDTO);
            } else if (takeAwayDTO.getCustPhone().contains(search)) {
                takeAway.add(takeAwayDTO);
            }
        }
        mDbRepository.setTakeAwayStatus(takeAway);
        return takeAway;

    }

    private ArrayList<DeliveryDTO> sortDeliveryList(ArrayList<DeliveryDTO> deliveryDTOs, String search) {
        ArrayList<DeliveryDTO> delivery = new ArrayList<DeliveryDTO>();

        for (DeliveryDTO deliveryDTO : deliveryDTOs) {
            if (deliveryDTO.getmCustName().toLowerCase().contains(search.toLowerCase())) {
                delivery.add(deliveryDTO);
            } else if (String.valueOf(deliveryDTO.getmDeliveryNo()).contains(search.toLowerCase())) {
                delivery.add(deliveryDTO);
            } else if (deliveryDTO.getmCustAddress().toLowerCase().contains(search.toLowerCase())) {
                delivery.add(deliveryDTO);
            } else if (deliveryDTO.getCustPhone().contains(search)) {
                delivery.add(deliveryDTO);
            }
        }
        mDbRepository.setDeliveryStatus(delivery);
        return delivery;

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
        //hitActivity();
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
            sendEventToGoogle("Action", "Filter Table");
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
            sendEventToGoogle("Action", "Notification");
            Intent iWaitingList = new Intent(getApplicationContext(), NotificationActivity.class);
            startActivity(iWaitingList);
        }
        if (id == R.id.settings) {
            Intent iSetting = new Intent(getApplicationContext(), SettingPrinterActivity.class);
            startActivity(iSetting);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_profile) {
            sendEventToGoogle("Action", "My Profile");
            // Handle the camera action
        } else if (id == R.id.nav_waiting_list) {
            sendEventToGoogle("Action", "NavBar Waiting list");
            //showWaitingDialog();
            //callWaitingIntent();
        } else if (id == R.id.nav_log_out) {
            UserAuth.CleanAuthenticationInfo();
            callLogin();
        } else if (id == R.id.about_us) {
            sendEventToGoogle("Action", "NavBar About us");
            Intent aboutUsIntent = new Intent(getApplicationContext(), AboutUsActivity.class);
            aboutUsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(aboutUsIntent);
        } else if (id == R.id.settings) {
            Intent iSetting = new Intent(getApplicationContext(), SettingPrinterActivity.class);
            startActivity(iSetting);
        } else if (id == R.id.completed_ordres) {

            Intent selectRestoIntent = new Intent(getApplicationContext(), CompletedOrdersScreen.class);
            startActivity(selectRestoIntent);

        } else if (id == R.id.diagnostic) {
            Intent diagnostic = new Intent(getApplicationContext(), DiagnosticActivity.class);
            startActivity(diagnostic);
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
                    FragmentWaiterTable.adapter.refresh(mDbRepository.getTableRecords(""));
                }
                if (where != null)
                    FragmentWaiterTable.adapter.refresh(mDbRepository.getTableRecords(where));
                else
                    FragmentWaiterTable.adapter.refresh(mDbRepository.getTableRecords(""));
            } else {
                Toast.makeText(getApplicationContext(), "All Filters are removed", Toast.LENGTH_SHORT).show();
                FragmentWaiterTable.adapter.refresh(mDbRepository.getTableRecords(""));
            }
        } else {

        }
    }

    private void callWaitingIntent() {
        Intent iWaitingList = new Intent(getApplicationContext(), AddCustomerActivity.class);
        startActivity(iWaitingList);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
    }

}
