package com.vibeosys.rorderapp.adaptors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.vibeosys.rorderapp.fragments.FragmentAllServing;
import com.vibeosys.rorderapp.fragments.FragmentMyServing;
import com.vibeosys.rorderapp.fragments.FragmentTable;

/**
 * Created by kiran on 20-01-2016.
 */
public class TablePagerAdapter extends FragmentPagerAdapter {

    public TablePagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentTable fragmentTable =new FragmentTable();
                return fragmentTable;

            case 1:
                FragmentMyServing fragmentTMyServing =new FragmentMyServing();
                return fragmentTMyServing;

            case 2:
                FragmentAllServing fragmentAllServing =new FragmentAllServing();
                return fragmentAllServing;

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
