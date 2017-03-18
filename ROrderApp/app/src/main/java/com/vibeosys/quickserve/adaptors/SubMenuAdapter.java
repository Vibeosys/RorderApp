package com.vibeosys.quickserve.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.data.OrderMenuDTO;
import com.vibeosys.quickserve.data.SubMenuDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 01-04-2016.
 */
public class SubMenuAdapter extends BaseAdapter {

    SubMenuButtonListener subMenuButtonListener;
    private Context mContext;
    ArrayList<SubMenuDTO> subMenus;
    OrderMenuDTO menuDTO;

    public SubMenuAdapter(Context mContext, ArrayList<SubMenuDTO> subMenus, OrderMenuDTO menuDTO) {
        this.mContext = mContext;
        this.subMenus = subMenus;
        this.menuDTO = menuDTO;
    }

    @Override
    public int getCount() {
        return subMenus.size();
    }

    @Override
    public Object getItem(int position) {
        return subMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;

        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.row_sub_menu, null);
            viewHolder = new ViewHolder();
            viewHolder.txtMenuTitle = (TextView) row.findViewById(R.id.txtMenuName);
            viewHolder.txtPrice = (TextView) row.findViewById(R.id.txtMenuPrice);
            viewHolder.txtQuantity = (TextView) row.findViewById(R.id.txtMenuQty);
            viewHolder.imgPlus = (ImageView) row.findViewById(R.id.imgPlus);
            viewHolder.imgMinus = (ImageView) row.findViewById(R.id.imgMinus);
            viewHolder.imgNote = (ImageView) row.findViewById(R.id.imgNote);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();
        final SubMenuDTO subMenu = subMenus.get(position);
        viewHolder.txtMenuTitle.setText(subMenu.getMenuTitle());
        viewHolder.txtPrice.setText("" + subMenu.getMenuPrice());
        viewHolder.txtQuantity.setText("" + subMenu.getQuantity());
        viewHolder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subMenuButtonListener.onSubMenuButtonListener(v.getId(), position, subMenu.getQuantity(), subMenu, menuDTO);
            }
        });
        viewHolder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subMenuButtonListener.onSubMenuButtonListener(v.getId(), position, subMenu.getQuantity(), subMenu, menuDTO);
            }
        });
        viewHolder.imgNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subMenuButtonListener.onSubMenuButtonListener(v.getId(), position, subMenu.getQuantity(), subMenu, menuDTO);
            }
        });
        return row;
    }

    private class ViewHolder {
        TextView txtMenuTitle;
        TextView txtPrice;
        TextView txtQuantity;
        ImageView imgPlus;
        ImageView imgMinus;
        ImageView imgNote;
    }

    public void setCustomButtonListner(SubMenuButtonListener listener) {
        this.subMenuButtonListener = listener;
    }

    public interface SubMenuButtonListener {
        public void onSubMenuButtonListener(int id, int position, int value, SubMenuDTO subMenu, OrderMenuDTO orderMenuDTO);
    }
}
