package com.vibeosys.rorderapp.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;


import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.ChefOrderAdapter;
import com.vibeosys.rorderapp.adaptors.ChefPagerAdapter;

import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.service.ChefService;
import com.vibeosys.rorderapp.service.SyncService;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shrinivas on 12-02-2016.
 */
public class ChefOrdersDisplayActivity extends AppCompatActivity {

    private ExpandableListView chefOrderList;
    private ChefOrderAdapter chefOrderAdapter;
    private ArrayList<ChefOrderDetailsDTO> list = new ArrayList<>();
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TabLayout tab_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chef_main_screen);
        getSupportActionBar();
        setTitle("Chef dashboard");

       Intent i = new Intent(Intent.ACTION_SYNC,null,this,ChefService.class);
        startService(i);

        chefOrderList = (ExpandableListView) findViewById(R.id.expListViewForChef);
        getSupportActionBar();
//        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab_text_view,null);
//        textView.setText("Current Orders");
        Intent syncServiceIntent = new Intent(Intent.ACTION_SYNC, null, this, ChefService.class);
        startService(syncServiceIntent);
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


}
