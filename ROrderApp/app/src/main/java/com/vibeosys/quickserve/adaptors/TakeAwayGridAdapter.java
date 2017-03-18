package com.vibeosys.quickserve.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.data.TakeAwayDTO;
import com.vibeosys.quickserve.util.AppConstants;

import java.util.ArrayList;

/**
 * Created by akshay on 11-03-2016.
 */
public class TakeAwayGridAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<TakeAwayDTO> mTakeawayList;

    public TakeAwayGridAdapter(Context mContext, ArrayList<TakeAwayDTO> mTakeawayList) {
        this.mContext = mContext;
        this.mTakeawayList = mTakeawayList;
    }

    public void refresh(ArrayList<TakeAwayDTO> takeAways) {
        if (mTakeawayList != null)
            this.mTakeawayList.clear();
        this.mTakeawayList = takeAways;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mTakeawayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTakeawayList.get(position);
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
            viewHolder.txtTakeAwayNo = (TextView) row.findViewById(R.id.txtTakeAwayNo);
            viewHolder.txtCustomerName = (TextView) row.findViewById(R.id.txtCustName);
            viewHolder.txtCustAddress = (TextView) row.findViewById(R.id.txtCustAddress);
            //viewHolder.txtUserName = (TextView) row.findViewById(R.id.txtUserName);
            viewHolder.txtCustPhone = (TextView) row.findViewById(R.id.txtCustPh);
            // viewHolder.imgTableStatus=(ImageView)row.findViewById(R.id.imgTabelStatus);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();
        TakeAwayDTO takeAwayDTO = mTakeawayList.get(position);
        int orderStatus = takeAwayDTO.getOrderStatus();
        Log.d("##", takeAwayDTO.getmTakeawayNo() + "## " + orderStatus);
        if (orderStatus == AppConstants.TAKAWAY_STATUS_PENDING) {
            viewHolder.layoutIsReady.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        } else if (orderStatus == AppConstants.TAKAWAY_STATUS_READY) {
            viewHolder.layoutIsReady.setBackgroundColor(mContext.getResources().getColor(R.color.dark_green_color));
        } else if (orderStatus == AppConstants.TAKAWAY_STATUS_DELIVERED || orderStatus == 0) {
            viewHolder.layoutIsReady.setBackgroundColor(mContext.getResources().getColor(R.color.light_grey));
        }
        viewHolder.txtTakeAwayNo.setText("" + takeAwayDTO.getmTakeawayNo());
        viewHolder.txtCustomerName.setText("" + takeAwayDTO.getmCustName());
        viewHolder.txtCustAddress.setText(takeAwayDTO.getmCustAddress());
        //viewHolder.txtUserName.setText(takeAwayDTO.getUserName());
        viewHolder.txtCustPhone.setText(takeAwayDTO.getCustPhone());
        return row;
    }

    private class ViewHolder {
        LinearLayout layoutIsReady;
        TextView txtTakeAwayNo;
        //TextView txtUserName;
        TextView txtCustomerName;
        TextView txtCustAddress;
        TextView txtCustPhone;
    }
}
