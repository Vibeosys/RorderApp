package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;

import java.util.List;

/**
 * Created by akshay on 01-03-2016.
 */
public class ChefRecyclerViewAdapter extends RecyclerView.Adapter<ChefRecyclerViewAdapter.OrderViewHolder> {
    private List<ChefOrderDetailsDTO> mOrderHeaderDTOs;
    private Context mContext;

    public ChefRecyclerViewAdapter(List<ChefOrderDetailsDTO> orderHeaderDTOs, Context context) {
        this.mOrderHeaderDTOs = orderHeaderDTOs;
        this.mContext = context;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        OrderViewHolder dataObjectHolder = new OrderViewHolder(view);
        return dataObjectHolder;*/
        return null;
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.txtOrderNo.setText(mOrderHeaderDTOs.get(position).getmOrderNumner());
        holder.txtTableNo.setText(mOrderHeaderDTOs.get(position).getmTableNo());
        holder.txtOrderTime.setText(mOrderHeaderDTOs.get(position).TimeDiff() + " ago");
    }

    @Override
    public int getItemCount() {
        return mOrderHeaderDTOs.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView txtTableNo;
        TextView txtOrderNo;
        TextView txtWaiterName;
        TextView txtOrderTime;
        Button btnComplete;
        ListView menuList;

        public OrderViewHolder(View itemView) {
            super(itemView);

        }
    }
}
