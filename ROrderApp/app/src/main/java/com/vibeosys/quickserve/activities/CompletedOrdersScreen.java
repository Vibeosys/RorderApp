package com.vibeosys.quickserve.activities;

import android.os.Bundle;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.fragments.FragmentChefPlacedOrder;

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
