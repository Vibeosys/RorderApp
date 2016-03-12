package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.database.DbRepository;

import java.util.ArrayList;

/**
 * Created by shrinivas on 09-03-2016.
 */
public class ChefRecyclerPreviousAdapter extends RecyclerView.Adapter<ChefRecyclerPreviousAdapter.OrderViewHolderPrevious>  {

    private ArrayList<ChefOrderDetailsDTO> mOrderHeaderDTO;
    private Context mContext;
    DbRepository dbRepository;
    UtilityPrevious utilityPrevious = new UtilityPrevious();
    public ChefRecyclerPreviousAdapter(ArrayList<ChefOrderDetailsDTO> orderHeaderDTO, Context context, DbRepository dbRepository)
    {
       this.mOrderHeaderDTO = orderHeaderDTO;
       this.mContext = context;
       this.dbRepository = dbRepository;
    }

    @Override
    public ChefRecyclerPreviousAdapter.OrderViewHolderPrevious onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_list_previous, parent, false);
        OrderViewHolderPrevious orderViewHolderPrevious = new OrderViewHolderPrevious(view);
        return orderViewHolderPrevious;
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public void onBindViewHolder(ChefRecyclerPreviousAdapter.OrderViewHolderPrevious holder, int position) {
        holder.preTxtTableNo.setText(""+mOrderHeaderDTO.get(position).getmTableNo());
        holder.preTxtOrderNo.setText(""+mOrderHeaderDTO.get(position).getmOrderNumner());
        holder.preTxtWaiterName.setText("" + mOrderHeaderDTO.get(position).getmUserName());
        holder.preTxtOrderTime.setText("" + mOrderHeaderDTO.get(position).TimeDiff());
        ChefMenuPreviousAdapter adapter = new ChefMenuPreviousAdapter(mOrderHeaderDTO.get(position).getmMenuChild(), mContext);
        holder.preMenuList.setAdapter(adapter);
        if(mOrderHeaderDTO.get(position).getmTableNo()==0)
        {
            holder.takeAwayTitle.setVisibility(View.VISIBLE);
            holder.preTxtTableNo.setText(""+mOrderHeaderDTO.get(position).getmTakeAwayNumber());//here take away number is going to set
          //  holder.layout.setBackgroundResource(R.color.blue);
            holder.tableNumberTitle.setVisibility(View.GONE);
        }
        else {
            holder.tableNumberTitle.setVisibility(View.VISIBLE);
            holder.preTxtTableNo.setText(""+mOrderHeaderDTO.get(position).getmTableNo());//here table number is going to set
            holder.takeAwayTitle.setVisibility(View.GONE);
        }
        utilityPrevious.setListViewHeightBasedOnChildren(holder.preMenuList);
    }

    @Override
    public int getItemCount() {
        return mOrderHeaderDTO.size();
    }
    public static class OrderViewHolderPrevious  extends RecyclerView.ViewHolder
    {

        CardView preCardView;
        TextView preTxtTableNo;
        TextView preTxtOrderNo;
        TextView preTxtWaiterName;
        TextView preTxtOrderTime;
        TextView takeAwayTitle;
        TextView tableNumberTitle;
        Button preBtnComplete;
        ListView preMenuList;
        ImageView preBtnDone;
        LinearLayout layout;

        public OrderViewHolderPrevious(View itemView) {
            super(itemView);
             preCardView =(CardView) itemView.findViewById(R.id.RecyclerCard_Previous);
             preTxtTableNo = (TextView) itemView.findViewById(R.id.recyclerTableNoTab_Previous);
             preTxtOrderNo= (TextView) itemView.findViewById(R.id.recyclerOrderNoTab_Previous);
             preTxtWaiterName= (TextView) itemView.findViewById(R.id.recyclerServedByName_Previous);
             preTxtOrderTime = (TextView) itemView.findViewById(R.id.recyclerOrderTime_Previous);
             preBtnDone = (ImageView) itemView.findViewById(R.id.recyclerChefOrderDonIcon_Previous);
            takeAwayTitle = (TextView)itemView.findViewById(R.id.recyclerTxtTakeAwayTitle_Previous);
            tableNumberTitle=(TextView)itemView.findViewById(R.id.recyclerTxtTableNoTitle_Previous);
             //preBtnComplete =  (Button) itemView.findViewById(R.id.recyclerOrderDoneBtn);
             preMenuList = (ListView) itemView.findViewById(R.id.recyclerMenuList_Previous);

        }
    }
    public class UtilityPrevious {
        public void setListViewHeightBasedOnChildren(ListView listView) {

            ChefMenuPreviousAdapter adapter = (ChefMenuPreviousAdapter) listView.getAdapter();
            if (adapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listView);
                if (listItem instanceof ViewGroup) {
                    listItem.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));

            // params.height = params.height / 2;
            // Math.round(params.height);
            listView.setLayoutParams(params);
        }
    }
}
