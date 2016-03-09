package com.vibeosys.rorderapp.activities;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.ChefOrderAdapter;
import com.vibeosys.rorderapp.adaptors.ChefPagerAdapter;

import com.vibeosys.rorderapp.adaptors.ChefRecyclerViewAdapter;
import com.vibeosys.rorderapp.adaptors.ChefTabListAdapter;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.data.ServerSync;
import com.vibeosys.rorderapp.fragments.FragmentChefTabMyServing;
import com.vibeosys.rorderapp.service.ChefService;
import com.vibeosys.rorderapp.service.SyncService;
import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.UserAuth;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.IDN;
import java.util.ArrayList;

/**
 * Created by shrinivas on 12-02-2016.
 */
public class ChefOrdersDisplayActivity extends BaseActivity {

    private ExpandableListView chefOrderList;
    private ChefOrderAdapter chefOrderAdapter;
    private ArrayList<ChefOrderDetailsDTO> list = new ArrayList<>();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TabLayout tab_layout;

    @Override
    protected String getScreenName() {
        return "Chef Dashboard";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chef_main_screen);
        getSupportActionBar();

        setTitle("Chef's Kitchen");

        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            String stringTitle = getResources().getString(R.string.error_msg_title_for_network);
            String stringMessage = getResources().getString(R.string.error_msg_for_select_restaurant);
            customAlterDialog(stringTitle, stringMessage);


        }
        getSupportActionBar();

        Intent i = new Intent(Intent.ACTION_SYNC, null, this, SyncService.class);
        startService(i);

        DisplayMetrics matrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(matrics);
        int widthPixel = matrics.widthPixels;
        int heightPixel = matrics.heightPixels;

        float scaleFactor = matrics.density;


        float widthDp = widthPixel / scaleFactor;
        float heigthDp = heightPixel / scaleFactor;

        float smallWidth = Math.min(widthDp, heigthDp);

        if (widthDp >= 590 && heigthDp >= 400) {
          //  Toast.makeText(this, "finally you are at 6 inches from activity", Toast.LENGTH_LONG).show();

            /*android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            FragmentChefTabMyServing fragmentChefTabMyServing= new FragmentChefTabMyServing();
            fragmentTransaction.add(R.id.parent_test, fragmentChefTabMyServing);
            fragmentTransaction.commit();*/

            Intent tabLayoutActivity = new Intent(this,ChefOrdersDisplayTabActivity.class);
            startActivity(tabLayoutActivity);
            finish();





        }
        else
        {

            tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tab_layout.addTab(tab_layout.newTab().setText("CURRENT ORDERS"));

        tab_layout.addTab(tab_layout.newTab().setText("ORDER HISTORY"));


        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        tab_layout.setSelectedTabIndicatorHeight(4);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final ChefPagerAdapter adapter = new ChefPagerAdapter
                (getSupportFragmentManager(), tab_layout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        }





      /*  tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tab_layout.addTab(tab_layout.newTab().setText("CURRENT ORDERS"));

        tab_layout.addTab(tab_layout.newTab().setText("ORDER HISTORY"));


        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        tab_layout.setSelectedTabIndicatorHeight(4);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final ChefPagerAdapter adapter = new ChefPagerAdapter
                (getSupportFragmentManager(), tab_layout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.chef_main, menu);


        menu.findItem(R.id.signoutChef).setVisible(true);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.signoutChef) {

            UserAuth.CleanAuthenticationInfo();
            chefCallLogin();
        }

        return super.onOptionsItemSelected(item);
    }

    public void chefCallLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtils.isActiveNetworkAvailable(this)) {
            String stringTitle = getResources().getString(R.string.error_msg_title_for_network);
            String stringMessage = getResources().getString(R.string.error_msg_for_select_restaurant);
            customAlterDialog(stringTitle, stringMessage);


        }
    }
}
