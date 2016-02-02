package com.vibeosys.rorderapp;

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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

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

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemClickListener {

    TabLayout tab_layout;
    DrawerLayout drawer;
    GridView gridView;
    TableGridAdapter adapter;
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
            gridView=(GridView)findViewById(R.id.gridview);
            gridView.setOnItemClickListener(this);
            adapter=new TableGridAdapter(getApplicationContext(),mDbRepository.getTableRecords());
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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

        HotelTableDTO hotelTableDTO= (HotelTableDTO) adapter.getItem(position);
        Intent intentOpenTableMenu=new Intent(getApplicationContext(), TableMenusActivity.class);
        intentOpenTableMenu.putExtra("TableNo",hotelTableDTO.getmTableNo());
        intentOpenTableMenu.putExtra("TableId",hotelTableDTO.getmTableId());
        startActivity(intentOpenTableMenu);
        Log.i(TAG,"##"+hotelTableDTO.getmTableNo()+"Is Clicked");
    }
}
