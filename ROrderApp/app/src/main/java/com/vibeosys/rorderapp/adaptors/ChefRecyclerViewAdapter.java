package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.database.DbRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 01-03-2016.
 */
public class ChefRecyclerViewAdapter extends RecyclerView.Adapter<ChefRecyclerViewAdapter.OrderViewHolder> {
    private ArrayList<ChefOrderDetailsDTO> mOrderHeaderDTOs;
    private Context mContext;
    OrderViewHolder holder;
    private tabCompleteButton mTabCompleteButton;
    DbRepository dbRepository;
    Utility utility = new Utility();

    public ChefRecyclerViewAdapter(ArrayList<ChefOrderDetailsDTO> orderHeaderDTOs, Context context, DbRepository dbRepository) {
        this.mOrderHeaderDTOs = orderHeaderDTOs;
        this.mContext = context;
        this.dbRepository = dbRepository;
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
        holder.txtTableNo.setText("" + mOrderHeaderDTOs.get(position).getmTableNo());
        holder.txtOrderTime.setText(mOrderHeaderDTOs.get(position).TimeDiff() + " ago");
        holder.txtWaiterName.setText(mOrderHeaderDTOs.get(position).getmUserName());
        ChefMenuAdapter adapter = new ChefMenuAdapter(mOrderHeaderDTOs.get(position).getmMenuChild(), mContext);
        holder.menuList.setAdapter(adapter);
        utility.setListViewHeightBasedOnChildren(holder.menuList);
        holder.btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTabCompleteButton != null) {
                    mTabCompleteButton.tabComplete(mOrderHeaderDTOs.get(position).getmNewOrderId());
                    mOrderHeaderDTOs.remove(position);
                    notifyDataSetChanged();
                } else {

                }
            }
        });
        // holder.menuList.setScrollContainer(false);
        /*holder.menuList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;

                }
                v.onTouchEvent(event);
                return true;
            }
        });*/


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
            cardView = (CardView) itemView.findViewById(R.id.RecyclerCard);
            txtTableNo = (TextView) itemView.findViewById(R.id.recyclerTableNoTab);
            txtOrderNo = (TextView) itemView.findViewById(R.id.recyclerOrderNoTab);
            txtWaiterName = (TextView) itemView.findViewById(R.id.recyclerServedByName);
            txtOrderTime = (TextView) itemView.findViewById(R.id.recyclerOrderTime);
            btnComplete = (Button) itemView.findViewById(R.id.recyclerOrderDoneBtn);
            menuList = (ListView) itemView.findViewById(R.id.recyclerMenuList);

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
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                if (listItem instanceof ViewGroup) {
                    listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight / 2;//+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

            // params.height = params.height / 2;
            // Math.round(params.height);
            listView.setLayoutParams(params);
        }
    }

    public void refresh(int status) {
        if (this.mOrderHeaderDTOs != null) {
            this.mOrderHeaderDTOs.clear();
        }
        this.mOrderHeaderDTOs = dbRepository.getRecChefOrder();
        dbRepository.addMenuList(mOrderHeaderDTOs);
        notifyDataSetChanged();

    }
}
