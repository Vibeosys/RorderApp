package com.vibeosys.rorderapp.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.fragments.FragmentChefPlacedOrder;

/**
 * Created by shrinivas on 26-03-2016.
 */
public class CompletedOrdersScreen  extends BaseActivity{
    @Override
    protected String getScreenName() {
        return null;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getResources().getString(R.string.completed_Orders));
        setContentView(R.layout.chef_main_screen);
        FragmentChefPlacedOrder fragmentChefPlacedOrder = new FragmentChefPlacedOrder();
       getSupportFragmentManager().beginTransaction().add(R.id.parent_test,fragmentChefPlacedOrder).commit();
    }
}
