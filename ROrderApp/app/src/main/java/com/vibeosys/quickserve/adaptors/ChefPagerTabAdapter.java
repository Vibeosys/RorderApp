package com.vibeosys.quickserve.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vibeosys.quickserve.fragments.FragmentChefTabDiningOrders;
import com.vibeosys.quickserve.fragments.FragmentChefTabMyPreviousOrders;
import com.vibeosys.quickserve.fragments.FragmentChefTabMyServing;
import com.vibeosys.quickserve.fragments.FragmentChefTabTakeAwayOrders;

/**
 * Created by shrinivas on 09-03-2016.
 */
public class ChefPagerTabAdapter extends FragmentPagerAdapter {

    public ChefPagerTabAdapter(FragmentManager fragmentManager,int tabcount)
    {
        super(fragmentManager);

    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                FragmentChefTabMyServing fragmentChefTabMyServing = new FragmentChefTabMyServing();
                return  fragmentChefTabMyServing;
            case 1:
                FragmentChefTabDiningOrders fragmentChefTabDiningOrders = new FragmentChefTabDiningOrders();
                return fragmentChefTabDiningOrders;

            case 2:FragmentChefTabTakeAwayOrders fragmentChefTabTakeAwayOrders = new FragmentChefTabTakeAwayOrders();
                return fragmentChefTabTakeAwayOrders;

            case 3: FragmentChefTabMyPreviousOrders fragmentChefTabMyPreviousOrders = new FragmentChefTabMyPreviousOrders();
                return fragmentChefTabMyPreviousOrders;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
       // return 0;
        return 4;
    }
}
