package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.OrderMenuDTO;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by akshay on 27-01-2016.
 */
public class OrderListAdapter extends BaseAdapter {

    private static final String TAG = OrderListAdapter.class.getSimpleName();
    private List<OrderMenuDTO> mMenus;
    private Context mContext;

    public OrderListAdapter(List<OrderMenuDTO> mMenus, Context mContext) {
        this.mMenus = mMenus;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        if (mMenus != null) return mMenus.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return mMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mMenus.get(position).getmMenuId();
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void refresh(List<OrderMenuDTO> menus)
    {
        this.mMenus.clear();
        this.mMenus=menus;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        ViewHolder viewHolder=null;
        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.row_table_menu, null);
            viewHolder = new ViewHolder();
            viewHolder.menuImage=(ImageView)row.findViewById(R.id.imgMenu);
            viewHolder.txtFoodType= (TextView)row.findViewById(R.id.txtFoodType);
            viewHolder.txtMenuTitle=(TextView)row.findViewById(R.id.txtMenuName);
            viewHolder.txtMenuTags=(TextView)row.findViewById(R.id.txtMenuTag);
            viewHolder.txtMenuCategory=(TextView)row.findViewById(R.id.txtCategory);
            viewHolder.txtPrice=(TextView)row.findViewById(R.id.txtMenuPrice);
            viewHolder.imgPlus=(ImageView)row.findViewById(R.id.imgPlus);
            viewHolder.imgMinus=(ImageView)row.findViewById(R.id.imgMinus);
           // viewHolder.imgMinus.setOnClickListener();
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();

        OrderMenuDTO menu=mMenus.get(position);
        Log.d(TAG, menu.toString());
        viewHolder.txtMenuTitle.setText(menu.getmMenuTitle());
        if(menu.getmFoodType())
        {
            viewHolder.txtFoodType.setText("Veg");
        }

        else if(!menu.getmFoodType())
        {
            viewHolder.txtFoodType.setText("Non-Veg");
        }
        viewHolder.txtMenuTags.setText(menu.getmTags());
        viewHolder.txtMenuCategory.setText(menu.getmCategory());
        viewHolder.txtPrice.setText(String.format("%.2f", menu.getmPrice()));
        return row;
    }

    private class ViewHolder{
        ImageView menuImage;
        TextView txtFoodType;
        TextView txtMenuTitle;
        TextView txtMenuTags;
        TextView txtMenuCategory;
        TextView txtPrice;
        ImageView imgPlus;
        ImageView imgMinus;
    }
}
