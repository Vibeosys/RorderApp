package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 01-03-2016.
 */
public class ChefTabListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ChefOrderDetailsDTO> chefOrderDetailsDTOs;
    private  GroupHolder groupHolder;
 public  ChefTabListAdapter(Context context,ArrayList<ChefOrderDetailsDTO> chefOrderDetailsDTOs)
    {
        this.context = context;
        this.chefOrderDetailsDTOs = chefOrderDetailsDTOs;
    }

    @Override
    public int getCount() {
        return chefOrderDetailsDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        return chefOrderDetailsDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return chefOrderDetailsDTOs.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView ==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chef_main_table_order, null);
            groupHolder = new GroupHolder();
            groupHolder.groupTextView = (TextView) convertView.findViewById(R.id.orderFromTableNo);
            groupHolder.getGroupTableNo = (TextView) convertView.findViewById(R.id.orderTakenBy);
            groupHolder.orderDoneBtn = (Button) convertView.findViewById(R.id.OrderDoneChef);
            groupHolder.getGroupOrderNo = (TextView) convertView.findViewById(R.id.cheforderNo);
            groupHolder.imgIndicator = (ImageView) convertView.findViewById(R.id.chefOrderDonIcon);
            groupHolder.placedOrderTime = (TextView) convertView.findViewById(R.id.orderTime);

            convertView.setTag(groupHolder);
        }
        else
        {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        return convertView;
    }
    public final class GroupHolder {
        TextView groupTextView;
        TextView getGroupTableNo;
        Button orderDoneBtn;
        ImageView imgIndicator;
        TextView getGroupOrderNo;
        TextView placedOrderTime;

    }
}
