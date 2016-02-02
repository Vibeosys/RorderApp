package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.HotelTableDTO;

import java.util.List;

/**
 * Adapter to show the Table Grid layout
 * Created by akshay on 20-01-2016.
 */

public class TableGridAdapter extends BaseAdapter {

    private final String TAG = TableGridAdapter.class.getSimpleName();
    private Context mContext;

    // table list
    private List<HotelTableDTO> mHotelTables;

    public TableGridAdapter(Context mContext, List<HotelTableDTO> mHotelTables) {
        this.mContext = mContext;
        this.mHotelTables = mHotelTables;
    }

    /**
     * Get the total number of tables
     *
     * @return count table list
     */
    @Override
    public int getCount() {
        if (mHotelTables != null) return mHotelTables.size();
        else return 0;
    }

    /**
     * Get the item of the given position
     *
     * @param position integer position
     * @return Object from the list of the given position
     */
    @Override
    public Object getItem(int position) {
        return mHotelTables.get(position);
    }

    /**
     * Get the item Id
     *
     * @param position integer position
     * @return the item id of the given position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

    }

    public void clearData()
    {
        mHotelTables.clear();
        notifyDataSetChanged();
    }
    /**
     * Get the view for the layout
     *
     * @param position    integer position
     * @param convertView View to convert
     * @param parent      Parent View Group
     * @return View which we want display
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TableGridAdapter.ViewHolder viewHolder = null;

        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.row_add_table, null);
            viewHolder = new TableGridAdapter.ViewHolder();
           viewHolder.layoutIsOccupied = (LinearLayout) row.findViewById(R.id.isOccupied);
            viewHolder.txtCapacity = (TextView) row.findViewById(R.id.txtCapacity);
            viewHolder.txtTableNumber = (TextView) row.findViewById(R.id.txtTableNumber);
           // viewHolder.imgTableStatus=(ImageView)row.findViewById(R.id.imgTabelStatus);
            row.setTag(viewHolder);

        } else viewHolder = (TableGridAdapter.ViewHolder) row.getTag();

        HotelTableDTO hotelTableDTO = mHotelTables.get(position);
        Log.d(TAG, hotelTableDTO.toString());
        viewHolder.txtCapacity.setText(""+hotelTableDTO.getmCapacity());
        viewHolder.txtTableNumber.setText(""+hotelTableDTO.getmTableNo());
        if(hotelTableDTO.ismIsOccupied())
        {
            viewHolder.layoutIsOccupied.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        }
        else {
            viewHolder.layoutIsOccupied.setBackgroundColor(mContext.getResources().getColor(R.color.dark_green_color));
        }
        //viewHolder.imgTablePhoto.loadImageFromFile("file:" + myImageDBs.get(position).getmImagePath());
        return row;
    }

    private static class ViewHolder {
       LinearLayout layoutIsOccupied;
        TextView txtCapacity;
        ImageView imgGroup;
        TextView txtTableNumber;
    }
}
