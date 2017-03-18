package com.vibeosys.quickserve.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.data.RestaurantDbDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 29-01-2016.
 */
public class RestaurantListAdapter extends BaseAdapter {

    private static final String TAG = RestaurantListAdapter.class.getSimpleName();
    private ArrayList<RestaurantDbDTO> restaurantDbDTOs;
    private Context mContext;

    public RestaurantListAdapter(ArrayList<RestaurantDbDTO> restaurantDbDTOs, Context mContext) {
        this.restaurantDbDTOs = restaurantDbDTOs;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (restaurantDbDTOs != null) return restaurantDbDTOs.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return restaurantDbDTOs.get(position);
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
            viewHolder.txtRestaurant = (TextView) row.findViewById(R.id.txtRestaurant);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();

        RestaurantDbDTO restaurantDbDTO=restaurantDbDTOs.get(position);
        Log.d(TAG, restaurantDbDTO.toString());
        viewHolder.txtRestaurant.setText(restaurantDbDTO.getTitle());
        //viewHolder.imgTablePhoto.loadImageFromFile("file:" + myImageDBs.get(position).getmImagePath());
        return row;
    }

    private class ViewHolder{
        TextView txtRestaurant;
    }
}
