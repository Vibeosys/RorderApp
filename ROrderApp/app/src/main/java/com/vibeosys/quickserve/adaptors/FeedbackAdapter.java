package com.vibeosys.quickserve.adaptors;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.data.FeedBackDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 16-02-2016.
 */
public class FeedbackAdapter extends BaseAdapter {

    private ArrayList<FeedBackDTO> feedBackDTOs;
    private Context mContext;

    public FeedbackAdapter(ArrayList<FeedBackDTO> feedBackDTOs, Context mContext) {
        this.feedBackDTOs = feedBackDTOs;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return feedBackDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        return feedBackDTOs.get(position);
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
            LayoutInflater layoutInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.row_feedback_list, null);
            viewHolder = new ViewHolder();
            viewHolder.txtFeedBack = (TextView) row.findViewById(R.id.txtFeedback);
            viewHolder.rating = (RatingBar) row.findViewById(R.id.ratingBar);
            LayerDrawable stars = (LayerDrawable) viewHolder.rating.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(mContext.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            row.setTag(viewHolder);
        } else viewHolder = (ViewHolder) row.getTag();
        final FeedBackDTO feedBackDTO = feedBackDTOs.get(position);
        viewHolder.txtFeedBack.setText(feedBackDTO.getmTitle());
        viewHolder.rating.setRating(feedBackDTO.getmRating());
        viewHolder.rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                feedBackDTO.setmRating(rating);
            }
        });
        return row;
    }

    private class ViewHolder {
        TextView txtFeedBack;
        RatingBar rating;
    }
}
