package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vibeosys.rorderapp.data.MenuDTO;
import com.vibeosys.rorderapp.data.MenuDbDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 23-01-2016.
 */
public class MenuAdapter extends BaseAdapter {

    private ArrayList<MenuDTO> mMenus;
    private Context mContext;

    public MenuAdapter(ArrayList<MenuDTO> mMenus, Context mContext) {
        this.mMenus = mMenus;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if(mMenus!=null)return mMenus.size();
        else
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*View row = convertView;
        TableCategoryAdapter.ViewHolder viewHolder = null;

        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            *//*row = theLayoutInflator.inflate(R.layout.row_catogory, null);
            viewHolder = new TableCategoryAdapter.ViewHolder();
            viewHolder.txtCategory = (TextView) row.findViewById(R.id.txtCategory);
            row.setTag(viewHolder);*//*

        } else viewHolder = (TableCategoryAdapter.ViewHolder) row.getTag();
        TableCategoryDTO categoryDTO=mTablecategories.get(position);
        Log.d(TAG, categoryDTO.toString());
        viewHolder.txtCategory.setText(categoryDTO.getmTitle());
        //viewHolder.imgTablePhoto.loadImageFromFile("file:" + myImageDBs.get(position).getmImagePath());
        return row;*/
        return null;
    }

    private class ViewHolder{
        TextView txtMenuName;
        TextView txtMenuTag;
        TextView txtCategory;

    }
}
