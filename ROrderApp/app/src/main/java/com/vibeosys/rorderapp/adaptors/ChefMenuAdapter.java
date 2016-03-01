package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.vibeosys.rorderapp.data.ChefMenuDetailsDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 01-03-2016.
 */
public class ChefMenuAdapter extends BaseAdapter {

    private ArrayList<ChefMenuDetailsDTO> mMenuList;
    private Context mContext;

    public ChefMenuAdapter(ArrayList<ChefMenuDetailsDTO> mMenuList, Context mContext) {
        this.mMenuList = mMenuList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mMenuList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMenuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    private class ViewHolder {

    }
}
