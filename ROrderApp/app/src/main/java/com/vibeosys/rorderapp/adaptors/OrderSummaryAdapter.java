package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.MenuDbDTO;
import com.vibeosys.rorderapp.data.OrderDetailsDTO;
import com.vibeosys.rorderapp.data.OrderHeaderDTO;

import java.util.HashMap;
import java.util.List;

/**
 * Created by akshay on 04-02-2016.
 */
public class OrderSummaryAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<OrderHeaderDTO> orderHeaderDTOs;

    public OrderSummaryAdapter(Context mContext, List<OrderHeaderDTO> orderHeaderDTOs) {
        this.mContext = mContext;
        this.orderHeaderDTOs = orderHeaderDTOs;
    }

    @Override
    public int getGroupCount() {
        return this.orderHeaderDTOs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.orderHeaderDTOs.get(groupPosition).getOrderDetailsDTOs().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.orderHeaderDTOs.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.orderHeaderDTOs.get(groupPosition).getOrderDetailsDTOs().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return orderHeaderDTOs.get(groupPosition).getOrderDetailsDTOs().get(childPosition).getMenuId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final OrderHeaderDTO orderHeaderDTO = (OrderHeaderDTO) getGroup(groupPosition);
        ExpandableListView expandableListView=(ExpandableListView)parent;
        //expandableListView.expandGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_table_order, null);
        }
        TextView txtOrderName = (TextView) convertView.findViewById(R.id.txtOrderName);
        TextView orderCount = (TextView) convertView.findViewById(R.id.textOrderCount);

        txtOrderName.setText("Order # " + orderHeaderDTO.getOrderNo());
        orderCount.setText(""+orderHeaderDTO.getItemCount());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final OrderDetailsDTO orderDetail = orderHeaderDTOs.get(groupPosition).getOrderDetailsDTOs().get(childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.row_order_menu, null);
        }
        Log.d("child", "## in getChildView()");
        TextView txtMenuName = (TextView) convertView.findViewById(R.id.txtMenuName);
        TextView txtQuantity = (TextView) convertView.findViewById(R.id.txtQuantity);
        txtMenuName.setText(orderDetail.getMenuTitle());
        txtQuantity.setText(""+orderDetail.getOrderQuantity());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
      //  super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
       // super.onGroupCollapsed(groupPosition);
    }
    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
