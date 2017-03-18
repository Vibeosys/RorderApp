package com.vibeosys.quickserve.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.data.PaymentModeDbDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 13-02-2016.
 */
public class PaymentModeAdapter extends BaseAdapter {

    private static final String TAG = PaymentModeAdapter.class.getSimpleName();
    private ArrayList<PaymentModeDbDTO> mPaymentModes;
    private Context mContext;

    public PaymentModeAdapter(ArrayList<PaymentModeDbDTO> paymentModes, Context mContext) {
        this.mPaymentModes = paymentModes;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mPaymentModes.size();
    }

    @Override
    public Object getItem(int position) {
        return mPaymentModes.get(position);
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
            row = theLayoutInflator.inflate(R.layout.row_restaurant_select, null);
            viewHolder = new ViewHolder();
            viewHolder.txtPaymentMode = (TextView) row.findViewById(R.id.txtRestaurant);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();
        PaymentModeDbDTO paymentModeDbDTO = mPaymentModes.get(position);
        Log.d(TAG, paymentModeDbDTO.toString());
        viewHolder.txtPaymentMode.setText(paymentModeDbDTO.getPaymentModeTitle());
        //viewHolder.imgTablePhoto.loadImageFromFile("file:" + myImageDBs.get(position).getmImagePath());
        return row;
    }

    private class ViewHolder {
        TextView txtPaymentMode;
    }

}
