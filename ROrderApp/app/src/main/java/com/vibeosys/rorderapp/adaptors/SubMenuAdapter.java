package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.NoteDTO;
import com.vibeosys.rorderapp.data.SubMenuDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 01-04-2016.
 */
public class SubMenuAdapter extends BaseAdapter {

    private Context mContext;
    ArrayList<SubMenuDTO> subMenus;

    public SubMenuAdapter(Context mContext, ArrayList<SubMenuDTO> subMenus) {
        this.mContext = mContext;
        this.subMenus = subMenus;
    }

    @Override
    public int getCount() {
        return subMenus.size();
    }

    @Override
    public Object getItem(int position) {
        return subMenus.get(position);
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
            row = theLayoutInflator.inflate(R.layout.row_sub_menu, null);
            viewHolder = new ViewHolder();
            viewHolder.txtMenuTitle = (TextView) row.findViewById(R.id.txtMenuName);
            viewHolder.txtPrice = (TextView) row.findViewById(R.id.txtMenuPrice);
            viewHolder.txtQuantity = (TextView) row.findViewById(R.id.txtMenuQty);
            viewHolder.imgPlus = (ImageView) row.findViewById(R.id.imgPlus);
            viewHolder.imgMinus = (ImageView) row.findViewById(R.id.imgMinus);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();
        SubMenuDTO subMenu = subMenus.get(position);
        viewHolder.txtMenuTitle.setText(subMenu.getMenuTitle());
        viewHolder.txtPrice.setText("" + subMenu.getMenuPrice());
        viewHolder.txtQuantity.setText("" + subMenu.getQuantity());
        return row;
    }

    private class ViewHolder {
        TextView txtMenuTitle;
        TextView txtPrice;
        TextView txtQuantity;
        ImageView imgPlus;
        ImageView imgMinus;
    }
}
