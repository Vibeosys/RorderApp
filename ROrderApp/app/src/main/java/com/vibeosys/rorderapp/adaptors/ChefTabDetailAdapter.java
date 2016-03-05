package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.ChefMenuDetailsDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 05-03-2016.
 */
public class ChefTabDetailAdapter extends BaseAdapter {
   public Context context;
    public ArrayList<ChefMenuDetailsDTO> menuDetailsDTO;

    public ChefTabDetailAdapter(Context context,ArrayList<ChefMenuDetailsDTO> menuDetailsDTO)
    {
        this.context = context;
        this.menuDetailsDTO =menuDetailsDTO;
    }
    @Override
    public int getCount() {
        return menuDetailsDTO.size();
    }

    @Override
    public Object getItem(int position) {
        return menuDetailsDTO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChildElement childElement;
        ChefMenuDetailsDTO chefMenuDetailsDTO = menuDetailsDTO.get(position);
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chef_main_row_order,null);
            childElement = new ChildElement();
            childElement.menuName= (TextView)convertView.findViewById(R.id.chefMenuName);
            childElement.MenuNote = (TextView)convertView.findViewById(R.id.chefMenuComment);
            childElement.menuNo = (TextView)convertView.findViewById(R.id.chefMenuNo);
            childElement.menuQty = (TextView)convertView.findViewById(R.id.chefQty);
            convertView.setTag(childElement);

        }else {
            childElement =(ChildElement)convertView.getTag();
        }
        childElement.menuName.setText(""+chefMenuDetailsDTO.getmChefMenuTitle());
        childElement.MenuNote.setText(chefMenuDetailsDTO.getmMenuNote());
        childElement.menuQty.setText(""+chefMenuDetailsDTO.getmChefQty());
        childElement.menuNo.setText(""+(position+1));
        return convertView;
    }
    public final class ChildElement
    {
        TextView menuName;
        TextView menuQty;
        TextView menuNo;
        TextView MenuNote;

    }
}
