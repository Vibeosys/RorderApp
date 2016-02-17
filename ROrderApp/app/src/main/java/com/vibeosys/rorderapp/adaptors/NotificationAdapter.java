package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.NotificationOrderDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 17-02-2016.
 */
public class NotificationAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<NotificationOrderDTO> orderDTOs;

    public NotificationAdapter(Context mContext, ArrayList<NotificationOrderDTO> orderDTOs) {
        this.mContext = mContext;
        this.orderDTOs = orderDTOs;
    }


    @Override
    public int getCount() {
        return orderDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        return orderDTOs.get(position);
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
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.row_notification, null);
            viewHolder = new ViewHolder();
            viewHolder.txtNotification = (TextView) row.findViewById(R.id.txtNotification);
            row.setTag(viewHolder);
        } else viewHolder = (ViewHolder) row.getTag();

        NotificationOrderDTO notificationOrderDTO = orderDTOs.get(position);
        viewHolder.txtNotification.setText(notificationOrderDTO.getMessage());
        return row;
    }

    private class ViewHolder {
        TextView txtNotification;
    }
}
