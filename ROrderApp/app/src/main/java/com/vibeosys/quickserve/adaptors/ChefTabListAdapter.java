package com.vibeosys.quickserve.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.data.ChefOrderDetailsDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 01-03-2016.
 */
public class ChefTabListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ChefOrderDetailsDTO> chefOrderDetailsDTOs;
    private  GroupHolder groupHolder;
    private OnRowItemClickListner onRowItemClickListner ;
 public  ChefTabListAdapter(Context context,ArrayList<ChefOrderDetailsDTO> chefOrderDetailsDTOs)
    {
        this.context = context;
        this.chefOrderDetailsDTOs = chefOrderDetailsDTOs;
    }

    @Override
    public int getCount() {
        return chefOrderDetailsDTOs.size();
    }

    @Override
    public Object getItem(int position) {
        return chefOrderDetailsDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return chefOrderDetailsDTOs.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChefOrderDetailsDTO chefOrderDetailsDTO = chefOrderDetailsDTOs.get(position);
        if(convertView ==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chef_main_tab, null);
            groupHolder = new GroupHolder();
            groupHolder.groupOrderServedBy = (TextView) convertView.findViewById(R.id.chefOrderServedByTab);
            groupHolder.getGroupOrderNo = (TextView) convertView.findViewById(R.id.cheforderNumberTab);
            groupHolder.getGroupTableNo = (TextView) convertView.findViewById(R.id.chefTableNumberTab);
            convertView.setTag(groupHolder);
        }
        else
        {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.groupOrderServedBy.setText(chefOrderDetailsDTO.getmUserName());
        groupHolder.getGroupOrderNo.setText("" + chefOrderDetailsDTO.getmOrderNumner());
        groupHolder.getGroupTableNo.setText(""+chefOrderDetailsDTO.getmTableNo());

        return convertView;
    }
    public final class GroupHolder {
        TextView groupOrderServedBy;
        TextView getGroupTableNo;
        TextView getGroupOrderNo;


    }
   public interface OnRowItemClickListner
    {
        public void rowItemClicked(String ChefOrderId);
    }
    public void setRowItemClicked(OnRowItemClickListner listern)
    {
        this.onRowItemClickListner = listern;
    }
}
