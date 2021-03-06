package com.vibeosys.quickserve.adaptors;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.data.RestaurantTables;

import java.util.List;

/**
 * Adapter to show the Table Grid layout
 * Created by akshay on 20-01-2016.
 */

public class TableGridAdapter extends BaseAdapter {

    private final String TAG = TableGridAdapter.class.getSimpleName();
    private Context mContext;
    private int mSessionUserId;
    // table list
    private List<RestaurantTables> mHotelTables;

    public TableGridAdapter(Context mContext, List<RestaurantTables> mHotelTables, int sessionUserId) {
        this.mContext = mContext;
        this.mHotelTables = mHotelTables;
        this.mSessionUserId = sessionUserId;
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

    public List<RestaurantTables> getHotelTables() {
        return mHotelTables;
    }

    public void setHotelTables(List<RestaurantTables> mHotelTables) {
        this.mHotelTables = mHotelTables;
    }

    public void refresh(List<RestaurantTables> tables) {
        if (mHotelTables != null)
            this.mHotelTables.clear();
        this.mHotelTables = tables;
        notifyDataSetChanged();
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

    public void clearData() {
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
            viewHolder.textTableCategory = (TextView) row.findViewById(R.id.txtCategory);
            viewHolder.txtUserName = (TextView) row.findViewById(R.id.txtUserName);
            viewHolder.imgUserLogo = (ImageView) row.findViewById(R.id.imgUserLogo);
            // viewHolder.imgTableStatus=(ImageView)row.findViewById(R.id.imgTabelStatus);
            row.setTag(viewHolder);

        } else viewHolder = (TableGridAdapter.ViewHolder) row.getTag();

        RestaurantTables hotelTableDTO = mHotelTables.get(position);
        viewHolder.txtCapacity.setText("" + hotelTableDTO.getmCapacity());
        viewHolder.txtTableNumber.setText("" + hotelTableDTO.getmTableNo());
        viewHolder.textTableCategory.setText(hotelTableDTO.getmTableCategoryName().toUpperCase());
        if (hotelTableDTO.ismIsOccupied()) {
            viewHolder.layoutIsOccupied.setBackgroundColor(mContext.getResources().getColor(R.color.red));
        } else {
            viewHolder.layoutIsOccupied.setBackgroundColor(mContext.getResources().getColor(R.color.dark_green_color));
        }
        if (hotelTableDTO.getUserName() != null && !TextUtils.isEmpty(hotelTableDTO.getUserName())) {
            if (hotelTableDTO.getUserId() == mSessionUserId) {
                viewHolder.txtUserName.setText("Me");
                viewHolder.txtUserName.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                viewHolder.txtUserName.setTextColor(mContext.getResources().getColor(R.color.white_color));
            } else {
                viewHolder.txtUserName.setText(hotelTableDTO.getUserName());
                viewHolder.txtUserName.setBackgroundColor(mContext.getResources().getColor(R.color.white_color));
                viewHolder.txtUserName.setTextColor(mContext.getResources().getColor(R.color.custom_text_inactive));
            }

            viewHolder.imgUserLogo.setVisibility(View.VISIBLE);
        } else {
            viewHolder.txtUserName.setText("");
            viewHolder.txtUserName.setBackgroundColor(mContext.getResources().getColor(R.color.white_color));
            viewHolder.txtUserName.setTextColor(mContext.getResources().getColor(R.color.custom_text_inactive));
            viewHolder.imgUserLogo.setVisibility(View.INVISIBLE);
        }

        //viewHolder.imgTablePhoto.loadImageFromFile("file:" + myImageDBs.get(position).getmImagePath());
        return row;
    }

    private static class ViewHolder {
        LinearLayout layoutIsOccupied;
        TextView txtCapacity;
        ImageView imgGroup;
        TextView txtTableNumber;
        TextView textTableCategory;//shrinivas
        TextView txtUserName;
        ImageView imgUserLogo;
    }
}
