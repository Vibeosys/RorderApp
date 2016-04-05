package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.DeliveryDTO;
import com.vibeosys.rorderapp.util.AppConstants;

import java.util.ArrayList;

/**
 * Created by akshay on 05-04-2016.
 */
public class DeliveryGridAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DeliveryDTO> mDeliveryList;

    public DeliveryGridAdapter(Context mContext, ArrayList<DeliveryDTO> deliveryDTOs) {
        this.mContext = mContext;
        this.mDeliveryList = deliveryDTOs;
    }

    public void refresh(ArrayList<DeliveryDTO> deliveryDTOs) {
        if (mDeliveryList != null)
            this.mDeliveryList.clear();
        this.mDeliveryList = deliveryDTOs;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDeliveryList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDeliveryList.get(position);
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
            row = theLayoutInflator.inflate(R.layout.row_take_away_order, null);
            viewHolder = new ViewHolder();
            viewHolder.layoutIsReady = (LinearLayout) row.findViewById(R.id.isReady);
            viewHolder.txtDeliveryNo = (TextView) row.findViewById(R.id.txtTakeAwayNo);
            viewHolder.txtCustomerName = (TextView) row.findViewById(R.id.txtCustName);
            viewHolder.txtCustAddress = (TextView) row.findViewById(R.id.txtCustAddress);
            //viewHolder.txtUserName = (TextView) row.findViewById(R.id.txtUserName);
            viewHolder.txtCustPhone = (TextView) row.findViewById(R.id.txtCustPh);
            // viewHolder.imgTableStatus=(ImageView)row.findViewById(R.id.imgTabelStatus);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();
        DeliveryDTO deliveryDTO = mDeliveryList.get(position);
        int orderStatus = deliveryDTO.getOrderStatus();
        Log.d("##", deliveryDTO.getmDeliveryNo() + "## " + orderStatus);
        if (orderStatus == AppConstants.TAKAWAY_STATUS_PENDING) {
            viewHolder.layoutIsReady.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        } else if (orderStatus == AppConstants.TAKAWAY_STATUS_READY) {
            viewHolder.layoutIsReady.setBackgroundColor(mContext.getResources().getColor(R.color.dark_green_color));
        } else if (orderStatus == AppConstants.TAKAWAY_STATUS_DELIVERED || orderStatus == 0) {
            viewHolder.layoutIsReady.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
        }
        viewHolder.txtDeliveryNo.setText("" + deliveryDTO.getmDeliveryNo());
        viewHolder.txtCustomerName.setText("" + deliveryDTO.getmCustName());
        viewHolder.txtCustAddress.setText(deliveryDTO.getmCustAddress());
        //viewHolder.txtUserName.setText(takeAwayDTO.getUserName());
        viewHolder.txtCustPhone.setText(deliveryDTO.getCustPhone());
        return row;
    }

    private class ViewHolder {
        LinearLayout layoutIsReady;
        TextView txtDeliveryNo;
        //TextView txtUserName;
        TextView txtCustomerName;
        TextView txtCustAddress;
        TextView txtCustPhone;
    }
}