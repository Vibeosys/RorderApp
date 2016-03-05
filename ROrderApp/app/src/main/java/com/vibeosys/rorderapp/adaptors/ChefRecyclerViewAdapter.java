package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;

import java.util.List;

/**
 * Created by akshay on 01-03-2016.
 */
public class ChefRecyclerViewAdapter extends  RecyclerView.Adapter<ChefRecyclerViewAdapter.OrderViewHolder> {
    private List<ChefOrderDetailsDTO> mOrderHeaderDTOs;
    private Context mContext;

    public ChefRecyclerViewAdapter(List<ChefOrderDetailsDTO> orderHeaderDTOs, Context context) {
        this.mOrderHeaderDTOs = orderHeaderDTOs;
        this.mContext = context;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list, parent, false);

        OrderViewHolder dataObjectHolder = new OrderViewHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        holder.txtOrderNo.setText("" + mOrderHeaderDTOs.get(position).getmOrderNumner());
        holder.txtTableNo.setText("" + mOrderHeaderDTOs.get(position).getmTableNo());
        holder.txtOrderTime.setText(mOrderHeaderDTOs.get(position).TimeDiff() + " ago");
        holder.txtWaiterName.setText(mOrderHeaderDTOs.get(position).getmUserName());
        ChefMenuAdapter adapter = new ChefMenuAdapter(mOrderHeaderDTOs.get(position).getmMenuChild(), mContext);
        holder.menuList.setAdapter(adapter);
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
            /*cardView = (CardView) itemView.findViewById(R.id.RecyclerCard);
            txtTableNo = (TextView) itemView.findViewById(R.id.recyclerTableNo);
            txtOrderNo = (TextView) itemView.findViewById(R.id.recyclerOrderNo);
            txtWaiterName = (TextView) itemView.findViewById(R.id.recyclerServedByName);
            txtOrderTime = (TextView) itemView.findViewById(R.id.recyclerOrderTime);
            btnComplete = (Button) itemView.findViewById(R.id.recyclerOrderDoneBtn);
            menuList = (ListView) itemView.findViewById(R.id.recyclerMenuList);*/
        }
    }
}
