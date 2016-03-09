package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.ChefPagerTabAdapter;
import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.UserAuth;

/**
 * Created by shrinivas on 09-03-2016.
 */
public class ChefOrdersDisplayTabActivity extends BaseActivity{
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TabLayout tab_layout;
    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chef_main_screen);
        getSupportActionBar();
        setTitle("Chef's Kitchen");

        tab_layout = (TabLayout) findViewById(R.id.tab_layout);

        tab_layout.addTab(tab_layout.newTab().setText("CURRENT ORDERS"));
        tab_layout.addTab(tab_layout.newTab().setText("PREVIOUS ORDERS"));

        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
        tab_layout.setSelectedTabIndicatorHeight(4);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

        final ChefPagerTabAdapter chefPagerTabAdapter = new ChefPagerTabAdapter(getSupportFragmentManager(), tab_layout.getTabCount());
        viewPager.setAdapter(chefPagerTabAdapter);

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
