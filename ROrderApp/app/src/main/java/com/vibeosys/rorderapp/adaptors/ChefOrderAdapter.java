package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.ChefMenuDetailsDTO;

import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;

import com.vibeosys.rorderapp.database.DbRepository;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by shrinivas on 12-02-2016.
 */
public class ChefOrderAdapter extends BaseExpandableListAdapter  {
    private Context context;
    private ArrayList<ChefOrderDetailsDTO> chefOrderDetailsDTOs;
    private GroupHolder groupHolder;
    private ChildHolder childHolder;
    private DbRepository mDbRepository;
    private OnDoneClickListener onDoneClickListener;
    ArrayList<ChefMenuDetailsDTO> child;
    ArrayList<ChefMenuDetailsDTO> child1;
    HashMap<Integer ,ArrayList<ChefMenuDetailsDTO>> expHashMap ;


   public ChefOrderAdapter(Context context,ArrayList<ChefOrderDetailsDTO>chefOrderDetailsDTOs,
                           DbRepository dbRepository)
    {
        this.context = context;
        this.chefOrderDetailsDTOs =chefOrderDetailsDTOs;
        child = new ArrayList<>();
        this.mDbRepository =dbRepository;
        expHashMap = new HashMap<>();

    }

    @Override
    public int getGroupCount() {
       // return 0;
        return chefOrderDetailsDTOs.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        if(expHashMap.size()>0) {
            if (expHashMap.containsKey(groupPosition))
            {
                child1 = expHashMap.get(groupPosition);
                {
                    return child1.size();
                }

            }
            else {
                return 0;
            }

        }


       return 0;

    }

    @Override
    public Object getGroup(int groupPosition) {

        return  this.chefOrderDetailsDTOs.get(groupPosition);

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return chefOrderDetailsDTOs.get(groupPosition).getmMenuChild().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {

        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ChefOrderDetailsDTO chefOrderDetailsDTO = chefOrderDetailsDTOs.get(groupPosition);
        if(convertView ==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chef_main_table_order,null);
            groupHolder = new GroupHolder();
            groupHolder.groupTextView = (TextView)convertView.findViewById(R.id.orderFromTableNo);
            groupHolder.getGroupTableNo = (TextView)convertView.findViewById(R.id.orderTakenBy);
            groupHolder.orderDoneBtn = (Button)convertView.findViewById(R.id.OrderDoneChef);
            groupHolder.getGroupOrderNo = (TextView)convertView.findViewById(R.id.cheforderNo);

            convertView.setTag(groupHolder);
        }
        else
        {

            groupHolder = (GroupHolder)convertView.getTag();
        }
        if(chefOrderDetailsDTO.getmNewOrderStatus() == 1)
        {
            groupHolder.orderDoneBtn.setVisibility(View.VISIBLE);
        }
        else
        {groupHolder.orderDoneBtn.setVisibility(View.INVISIBLE);}
        groupHolder.getGroupTableNo.setText(""+chefOrderDetailsDTO.getmTableNo());
        groupHolder.groupTextView.setText(chefOrderDetailsDTO.getmUserName());
        groupHolder.getGroupOrderNo.setText(""+chefOrderDetailsDTO.getmOrderNumner());
        groupHolder.orderDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDoneClickListener != null) {
                    onDoneClickListener.onDonClick(chefOrderDetailsDTO.getmNewOrderId());
                }


            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChefMenuDetailsDTO chefMenuDetailsDTO = child.get(childPosition);
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chef_main_row_order,null);
            childHolder = new ChildHolder();
            childHolder.childTextView = (TextView)convertView.findViewById(R.id.chefMenuName);
            childHolder.childQty = (TextView)convertView.findViewById(R.id.chefQty);
            convertView.setTag(childHolder);
        }
        else
        {
            childHolder = (ChildHolder)convertView.getTag();
        }

        childHolder.childTextView.setText(""+chefMenuDetailsDTO.getmChefMenuTitle());
        childHolder.childQty.setText(""+chefMenuDetailsDTO.getmChefQty());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

        int currentPosition  =-1;
        if(currentPosition !=-1 &&currentPosition != groupPosition )
        {

            onGroupCollapsed(groupPosition);
            notifyDataSetChanged();

        }
        else {
            super.onGroupExpanded(groupPosition);
            currentPosition = groupPosition;
            ChefOrderDetailsDTO chefOrderDetailsDTO = chefOrderDetailsDTOs.get(groupPosition);
            String selectedOrderId = String.valueOf(chefOrderDetailsDTO.getmNewOrderId());
            child.clear();
            child = mDbRepository.getChefMenu(selectedOrderId);
            expHashMap.put(groupPosition, child);
            notifyDataSetChanged();
        }

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);

        expHashMap.remove(groupPosition);


        notifyDataSetChanged();
    }



    public final class GroupHolder
    {
        TextView groupTextView;
        TextView getGroupTableNo;
        Button orderDoneBtn;
        ImageView imgIndicator;
        TextView getGroupOrderNo;
    }
    public final class ChildHolder
    {
        TextView childTextView;
        TextView childQty;
    }


    public interface OnDoneClickListener
    {
        public void onDonClick(String ChefOrderId);
    }
    public void setOnDoneClickListener(OnDoneClickListener listener)
    {
       this.onDoneClickListener=listener;
    }
    public void refresh(int status)
    {

        this.chefOrderDetailsDTOs.clear();
        this.child.clear();
        this.onGroupCollapsed(getGroupCount());

        this.chefOrderDetailsDTOs = mDbRepository.getOrderHeadesInAsc(status);
        notifyDataSetChanged();

    }

}
