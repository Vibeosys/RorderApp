package com.vibeosys.rorderapp;

import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vibeosys.rorderapp.activities.BaseActivity;
import com.vibeosys.rorderapp.activities.LoginActivity;
import com.vibeosys.rorderapp.activities.SelectRestaurantActivity;
import com.vibeosys.rorderapp.activities.TableMenusActivity;
import com.vibeosys.rorderapp.adaptors.TableCategoryAdapter;
import com.vibeosys.rorderapp.adaptors.TableGridAdapter;
import com.vibeosys.rorderapp.adaptors.TablePagerAdapter;
import com.vibeosys.rorderapp.data.HotelTableDTO;
import com.vibeosys.rorderapp.service.SyncService;
import com.vibeosys.rorderapp.util.UserAuth;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    TabLayout tab_layout;
    DrawerLayout drawer;
    GridView gridView;
    TableGridAdapter adapter;
    List<HotelTableDTO> hotelTableDTOs;
    List<HotelTableDTO> sortedTables;
    private Context mContext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);
        setContentView(R.layout.activity_main);
        ContextWrapper ctw = new ContextWrapper(getApplicationContext());
        File directory = ctw.getDir(mSessionManager.getDatabaseDirPath(), Context.MODE_PRIVATE);
        File dbFile = new File(directory, mSessionManager.getDatabaseFileName());
        if (!dbFile.exists()) {
            Intent selectRestoIntent = new Intent(getApplicationContext(), SelectRestaurantActivity.class);
            selectRestoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(selectRestoIntent);
            finish();
        } else if (!UserAuth.isUserLoggedIn()) {
            callLogin();
        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
           /* TextView txtUserName = (TextView) findViewById(R.id.txtHeaderWaiterName);
            txtUserName.setText("");
            TextView txtRestaurantName = (TextView)findViewById(R.id.txtHeaderHotelName);
            txtRestaurantName.setText("");*/
            drawer.setDrawerListener(toggle);
            toggle.syncState();
            gridView = (GridView) findViewById(R.id.gridview);
            gridView.setOnItemClickListener(this);
            hotelTableDTOs=mDbRepository.getTableRecords();
            adapter = new TableGridAdapter(getApplicationContext(), hotelTableDTOs);
            gridView.setAdapter(adapter);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
        }

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
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
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() == 0) {

                } else {
                    try {
                        sortedTables= sortAdapter(Integer.parseInt(s));
                        adapter.refresh(sortedTables);
                    } catch (Exception e) {
                        Log.e(TAG,"##"+e.toString());
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "You should Enter number", Toast.LENGTH_SHORT).show();
                    }

                }
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.refresh(mDbRepository.getTableRecords());
                return false;
            }
        });

        return true;
    }

    public List<HotelTableDTO> sortAdapter(int tableNo)
    {
        List<HotelTableDTO> mHotelTables=new ArrayList<>();

        for(HotelTableDTO table:mDbRepository.getTableRecords())
        {
            if(table.getmTableNo()==tableNo)
            {
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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_log_out) {
            UserAuth.CleanAuthenticationInfo();
            callLogin();
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
        if(hotelTableDTO.ismIsOccupied())
        {
            callToMenuIntent(hotelTableDTO.getmTableNo(),hotelTableDTO.getmTableId());
        }
       else
        {
            showReserveDialog(hotelTableDTO.getmTableNo(),hotelTableDTO.getmTableId());
        }
        Log.i(TAG, "##" + hotelTableDTO.getmTableNo() + "Is Clicked");
    }

    private void callToMenuIntent(int tableNo, int tableId) {
        Intent intentOpenTableMenu = new Intent(getApplicationContext(), TableMenusActivity.class);
        intentOpenTableMenu.putExtra("TableNo",tableNo);
        intentOpenTableMenu.putExtra("TableId", tableId);
        startActivity(intentOpenTableMenu);
    }

    private void showReserveDialog(final int tableNo, final int tableId) {

        final Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_table_reserve);
        dialog.setTitle("Reserve Table");
        EditText txtCustomerName=(EditText)dialog.findViewById(R.id.txtCustomerName);
        TextView cancel=(TextView)dialog.findViewById(R.id.txtCancel);
        TextView reserve=(TextView)dialog.findViewById(R.id.txtReserve);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Customer Entered in db",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                callToMenuIntent(tableNo,tableId);
            }
        });
        dialog.show();
    }
}
