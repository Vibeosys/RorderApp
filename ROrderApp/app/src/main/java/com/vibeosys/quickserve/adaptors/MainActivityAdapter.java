package com.vibeosys.quickserve.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vibeosys.quickserve.fragments.FragmentDelivery;
import com.vibeosys.quickserve.fragments.FragmentTakeAway;
import com.vibeosys.quickserve.fragments.FragmentWaiterTable;

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

            case 2:
                FragmentDelivery delivery = new FragmentDelivery();
                return delivery;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return itemCount;
    }
}
