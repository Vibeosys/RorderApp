package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.TakeAwaySourceDTO;
import com.vibeosys.rorderapp.util.CustomVolleyRequestQueue;

import java.util.ArrayList;

/**
 * Created by akshay on 10-03-2016.
 */
public class TakeAwaySourceAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<TakeAwaySourceDTO> mSources;
    private ImageLoader mImageLoader;

    public TakeAwaySourceAdapter(Context mContext, ArrayList<TakeAwaySourceDTO> sources) {
        this.mContext = mContext;
        this.mSources = sources;
    }

    @Override
    public int getCount() {
        return mSources.size();
    }

    @Override
    public Object getItem(int position) {
        return mSources.get(position);
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
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_take_away_source, null);
            viewHolder = new ViewHolder();
            viewHolder.txtName = (TextView) row.findViewById(R.id.txtSource);
            viewHolder.imgSource = (NetworkImageView) row.findViewById(R.id.imgSourceIcon);
            row.setTag(viewHolder);
        } else viewHolder = (ViewHolder) row.getTag();

        TakeAwaySourceDTO takeAwaySourceDTO = mSources.get(position);
        viewHolder.txtName.setText(takeAwaySourceDTO.getName());

        //Image URL - This can point to any image file supported by Android
       /* final String url = takeAwaySourceDTO.getImgUrl();
        mImageLoader = CustomVolleyRequestQueue.getInstance(mContext)
                .getImageLoader();
        Log.i("##", "## " + url);
        if (url != null && !url.isEmpty()) {
            try {

                mImageLoader.get(url, ImageLoader.getImageListener(viewHolder.imgSource,
                        R.drawable.default_table, R.drawable.default_table));
                viewHolder.imgSource.setImageUrl(url, mImageLoader);


            } catch (Exception e) {
                //viewHolder.imgSource.setImageResource(R.drawable.default_table);
            }
        } else {
            //viewHolder.imgSource.setImageResource(R.drawable.default_table);
        }*/
        return row;
    }

    private class ViewHolder {
        TextView txtName;
        NetworkImageView imgSource;
    }
}
