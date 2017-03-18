package com.vibeosys.quickserve.adaptors;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.data.ChefOrderDetailsDTO;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.view.LayoutInflater;

import com.vibeosys.quickserve.database.DbRepository;

/**
 * Created by shrinivas on 14-03-2016.
 */
public class ChefRecyclerTabTakeAwayAdapter extends RecyclerView.Adapter<ChefRecyclerTabTakeAwayAdapter.OrderViewHolder> {
    private ArrayList<ChefOrderDetailsDTO> mOrderHeaderDTOs;
    private Context mContext;
    OrderViewHolder holder;
    private tabCompleteButton mTabCompleteButton;
    DbRepository dbRepository;
    Utility utility;// = new Utility();
    ProgressDialog dialog;

    public ChefRecyclerTabTakeAwayAdapter(ArrayList<ChefOrderDetailsDTO> orderHeaderDTOs, Context context, DbRepository dbRepository) {
        this.mOrderHeaderDTOs = orderHeaderDTOs;
        this.mContext = context;
        this.dbRepository = dbRepository;
        utility = new Utility();
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
    public void onBindViewHolder(OrderViewHolder holder, final int position) {
        holder.txtOrderNo.setText("" + mOrderHeaderDTOs.get(position).getmOrderNumner());
        //   holder.txtTableNo.setText("" + mOrderHeaderDTOs.get(position).getmTableNo());
        holder.txtOrderTime.setText(mOrderHeaderDTOs.get(position).TimeDiff() + " ago");
        holder.txtWaiterName.setText(mOrderHeaderDTOs.get(position).getmUserName());
        ChefMenuAdapter adapter = new ChefMenuAdapter(mOrderHeaderDTOs.get(position).getmMenuChild(), mContext);
        holder.menuList.setAdapter(adapter);
        if (mOrderHeaderDTOs.get(position).getmTableNo() == 0) {

            holder.takeAwayTitle.setVisibility(View.VISIBLE);
            holder.txtTableNo.setText("" + mOrderHeaderDTOs.get(position).getmTakeAwayNumber());//here take away number is going to set
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.chef_take_away_back));
            holder.tableNumberTitle.setVisibility(View.GONE);
            holder.hrsGlassIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_hour_white));
            holder.servedByIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ico_new_servedby));
            int colour = Color.WHITE;
            holder.takeAwayTitle.setTextColor(colour);
            holder.txtHash.setTextColor(colour);
            holder.txtOrderNo.setTextColor(colour);
            holder.txtOrderTime.setTextColor(colour);
            holder.txtWaiterName.setTextColor(colour);
            holder.txtTableNo.setTextColor(colour);
        } else if(mOrderHeaderDTOs.get(position).getmTableNo() != 0) {
            holder.hrsGlassIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.icon_hours_glass));
            holder.servedByIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ico_served_by_black));
            holder.tableNumberTitle.setVisibility(View.VISIBLE);
            holder.txtTableNo.setText("" + mOrderHeaderDTOs.get(position).getmTableNo());//here table number is going to set
            holder.takeAwayTitle.setVisibility(View.GONE);
            holder.layout.setBackgroundColor(mContext.getResources().getColor(R.color.chef_dining_background));
            int colour = Color.BLACK;
            holder.takeAwayTitle.setTextColor(colour);
            holder.txtHash.setTextColor(colour);
            holder.txtOrderNo.setTextColor(colour);
            holder.txtOrderTime.setTextColor(colour);
            holder.txtWaiterName.setTextColor(colour);
            holder.txtTableNo.setTextColor(colour);
        }


        utility.setListViewHeightBasedOnChildren(holder.menuList);
        holder.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTabCompleteButton != null) {
                    mTabCompleteButton.tabComplete(mOrderHeaderDTOs.get(position).getmNewOrderId());

                }
            }
        });


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
        TextView takeAwayTitle;
        TextView tableNumberTitle;
        Button btnComplete;
        ListView menuList;
        ImageView btnDone;
        LinearLayout layout;
        TextView txtHash;
        ImageView servedByIcon, hrsGlassIcon;


        public OrderViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.RecyclerCard);
            txtTableNo = (TextView) itemView.findViewById(R.id.recyclerTableNoTab);
            txtOrderNo = (TextView) itemView.findViewById(R.id.recyclerOrderNoTab);
            txtWaiterName = (TextView) itemView.findViewById(R.id.recyclerServedByName);
            txtOrderTime = (TextView) itemView.findViewById(R.id.recyclerOrderTime);
            btnComplete = (Button) itemView.findViewById(R.id.recyclerOrderDoneBtn);
            takeAwayTitle = (TextView) itemView.findViewById(R.id.recyclerTxtTakeAwayNo);
            tableNumberTitle = (TextView) itemView.findViewById(R.id.recyclerTxtTableNo);
            menuList = (ListView) itemView.findViewById(R.id.recyclerMenuList);
            layout = (LinearLayout) itemView.findViewById(R.id.orderHeaderDtl);
            txtHash = (TextView) itemView.findViewById(R.id.txtHash);
            servedByIcon = (ImageView) itemView.findViewById(R.id.servedByIcon);
            hrsGlassIcon = (ImageView) itemView.findViewById(R.id.hrsGlassIcon);
            //LinearLayout mlinearlayout = (LinearLayout)itemView.findViewById(R.id.recycler_row_layout);
        }
    }


    public interface tabCompleteButton {
        public void tabComplete(String chefTabOrderId);
    }

    public void tabSetCompleteBtn(tabCompleteButton listener) {
        this.mTabCompleteButton = listener;
    }


    public class Utility {
        public void setListViewHeightBasedOnChildren(ListView listView) {

            ChefMenuAdapter listAdapter = (ChefMenuAdapter) listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
           /* for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                if (listItem instanceof View) {
                    listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    totalHeight += listItem.getMeasuredHeight();
                }
                listItem.measure(0, 0);

                totalHeight += listItem.getMeasuredHeight();
            }*/
            int demo = listView.getDividerHeight();
            ViewGroup.LayoutParams params = listView.getLayoutParams();
          //  params.height = totalHeight; //+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            // params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            // Math.round(totalHeight);
            params.height = (48 * listAdapter.getCount());
            listView.setLayoutParams(params);
            listView.requestLayout();

        }
    }

    public void refresh(int status) {
        if (this.mOrderHeaderDTOs != null) {
            this.mOrderHeaderDTOs.clear();
            this.mOrderHeaderDTOs = dbRepository.getRecChefTakeAwayOrders();
            dbRepository.addMenuList(mOrderHeaderDTOs);
            notifyDataSetChanged();

        }


    }
}
