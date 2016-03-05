package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.ChefMenuDetailsDTO;
import com.vibeosys.rorderapp.data.RestaurantDbDTO;

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
        View row = convertView;
        ViewHolder viewHolder = null;

        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.recycler_row_item_list, null);
            viewHolder = new ViewHolder();
            viewHolder.txtSrNo = (TextView) row.findViewById(R.id.chefMenuNo);
            viewHolder.txtmenuName = (TextView) row.findViewById(R.id.recyclerChefMenuName);
            viewHolder.txtQty = (TextView) row.findViewById(R.id.recyclerChefQty);
            viewHolder.txtComment = (TextView) row.findViewById(R.id.recyclerChefMenuComment);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();

        ChefMenuDetailsDTO menu = mMenuList.get(position);
        viewHolder.txtSrNo.setText("" + (position + 1));
        viewHolder.txtmenuName.setText(menu.getmChefMenuTitle());
        viewHolder.txtQty.setText("" + menu.getmChefQty());
        viewHolder.txtComment.setText(menu.getmMenuNote());
        //String note = menu.getmMenuNote();
       /* if (menu.getmMenuNote().isEmpty() && menu.getmMenuNote() == null) {
            viewHolder.txtComment.setText("");
            viewHolder.txtComment.setVisibility(View.GONE);
        } else {
            viewHolder.txtComment.setVisibility(View.VISIBLE);
            viewHolder.txtComment.setText("" + menu.getmMenuNote());
        }*/

        return row;
    }

    private class ViewHolder {
        TextView txtSrNo;
        TextView txtmenuName;
        TextView txtQty;
        TextView txtComment;
    }
}
