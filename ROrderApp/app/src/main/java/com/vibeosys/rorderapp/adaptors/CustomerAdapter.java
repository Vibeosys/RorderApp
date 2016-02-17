package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.TableTransactionDbDTO;
import com.vibeosys.rorderapp.data.WaitingUserDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 08-02-2016.
 */
public class CustomerAdapter extends BaseAdapter {

    private static final String TAG = CustomerAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<WaitingUserDTO> waitingList;

    public CustomerAdapter(Context mContext, ArrayList<WaitingUserDTO> waitingList) {
        this.mContext = mContext;
        this.waitingList = waitingList;
    }

    public void refresh(ArrayList<WaitingUserDTO> waiting) {
        this.waitingList.clear();
        this.waitingList = waiting;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (waitingList != null)
            return waitingList.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return waitingList.get(position);
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
            row = theLayoutInflator.inflate(R.layout.row_waiting_customer, null);
            viewHolder = new ViewHolder();
            viewHolder.txtCustName = (TextView) row.findViewById(R.id.txtCustomerName);
            viewHolder.txtCount = (TextView) row.findViewById(R.id.txtCount);
            //viewHolder.txtArrivalTime  = (TextView) row.findViewById(R.id.txtArrivalTime);
            // viewHolder.imgTableStatus=(ImageView)row.findViewById(R.id.imgTabelStatus);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();

        WaitingUserDTO waitingUserDTO = waitingList.get(position);
        Log.d(TAG, waitingUserDTO.toString());
        viewHolder.txtCustName.setText(waitingUserDTO.getmCustomerName());
        viewHolder.txtCount.setText(String.valueOf(waitingUserDTO.getmOccupancy()));
        //viewHolder.txtArrivalTime.setText(waitingUserDTO.getmArrivalTime().toString());

        //viewHolder.imgTablePhoto.loadImageFromFile("file:" + myImageDBs.get(position).getmImagePath());
        return row;
    }

    public class ViewHolder {
        TextView txtCustName;
        TextView txtCount;
        TextView txtArrivalTime;
    }
}
