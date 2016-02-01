package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.TableCategoryDTO;
import com.vibeosys.rorderapp.data.TableCategoryDbDTO;
import com.vibeosys.rorderapp.util.CustomVolleyRequestQueue;

import java.util.ArrayList;

/**
 * Created by akshay on 20-01-2016.
 */
public class TableCategoryAdapter extends BaseAdapter {

    private final String TAG=TableCategoryAdapter.class.getSimpleName();
    private ArrayList<TableCategoryDTO> mTablecategories;
    private Context mContext;
    private ImageLoader mImageLoader;
    public TableCategoryAdapter(ArrayList<TableCategoryDTO> mTablecategories, Context context) {
        this.mTablecategories = mTablecategories;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (mTablecategories != null) return mTablecategories.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return mTablecategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TableCategoryAdapter.ViewHolder viewHolder = null;

        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.row_table_category, null);
            viewHolder = new TableCategoryAdapter.ViewHolder();
            viewHolder.txtCategory = (TextView) row.findViewById(R.id.txtTableCategory);
            viewHolder.icon=(NetworkImageView)row.findViewById(R.id.tableCategoryIcon);
            row.setTag(viewHolder);

        } else viewHolder = (TableCategoryAdapter.ViewHolder) row.getTag();
        TableCategoryDTO categoryDTO=mTablecategories.get(position);
        Log.d(TAG, categoryDTO.toString());
        viewHolder.txtCategory.setText(categoryDTO.getmTitle());
        mImageLoader = CustomVolleyRequestQueue.getInstance(mContext)
                .getImageLoader();
        //Image URL - This can point to any image file supported by Android
        final String url = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/34/Android_Studio_icon.svg/2000px-Android_Studio_icon.svg.png";
        mImageLoader.get(url, ImageLoader.getImageListener(viewHolder.icon,
                R.mipmap.ic_launcher, android.R.drawable.stat_notify_error));
        viewHolder.icon.setImageUrl(url, mImageLoader);
        //viewHolder.imgTablePhoto.loadImageFromFile("file:" + myImageDBs.get(position).getmImagePath());
        return row;
    }

    private class ViewHolder{
        TextView txtCategory;
        NetworkImageView icon;
    }
}
