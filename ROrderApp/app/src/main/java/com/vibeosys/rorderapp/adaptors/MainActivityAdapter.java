package com.vibeosys.rorderapp.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vibeosys.rorderapp.fragments.FragmentTakeAway;
import com.vibeosys.rorderapp.fragments.FragmentWaiterTable;

/**
 * Created by akshay on 08-03-2016.
 */
public class MainActivityAdapter extends FragmentPagerAdapter {

    private int itemCount;

    public MainActivityAdapter(FragmentManager fm, int count) {
        super(fm);
        this.itemCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentWaiterTable fragmentTable = new FragmentWaiterTable();
                return fragmentTable;

            case 1:
                FragmentTakeAway takeAway = new FragmentTakeAway();
                return takeAway;


            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return itemCount;
    }
}
