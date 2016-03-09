package com.vibeosys.rorderapp.adaptors;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vibeosys.rorderapp.fragments.FragmentChefMyServing;
import com.vibeosys.rorderapp.fragments.FragmentChefPlacedOrder;

/**
 * Created by shrinivas on 15-02-2016.
 */
public class ChefPagerAdapter extends FragmentPagerAdapter{

  public   ChefPagerAdapter(FragmentManager fragmentManager,int tabcount)
    {
        super(fragmentManager);
    }
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                FragmentChefMyServing fragmentChefMyServing = new FragmentChefMyServing();
                return fragmentChefMyServing;
            case 1:
                FragmentChefPlacedOrder fragmentChefPlacedOrder = new FragmentChefPlacedOrder();
                return fragmentChefPlacedOrder;

            default:
                return null;
        }
       // return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
