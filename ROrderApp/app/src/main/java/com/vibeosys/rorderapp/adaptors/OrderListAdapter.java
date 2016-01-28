package com.vibeosys.rorderapp.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    CustomButtonListener customButtonListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row=convertView;
        ViewHolder viewHolder=null;
        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.row_table_menu, null);
            viewHolder = new ViewHolder();
            viewHolder.rowElement=(LinearLayout)row.findViewById(R.id.row_menu_item);
            viewHolder.menuImage=(ImageView)row.findViewById(R.id.imgMenu);
            viewHolder.imgFoodType= (ImageView)row.findViewById(R.id.imgFoodType);
            viewHolder.txtMenuTitle=(TextView)row.findViewById(R.id.txtMenuName);
            viewHolder.txtMenuTags=(TextView)row.findViewById(R.id.txtMenuTag);
            viewHolder.txtMenuCategory=(TextView)row.findViewById(R.id.txtCategory);
            viewHolder.txtPrice=(TextView)row.findViewById(R.id.txtMenuPrice);
            viewHolder.imgPlus=(ImageView)row.findViewById(R.id.imgPlus);
            viewHolder.imgMinus=(ImageView)row.findViewById(R.id.imgMinus);
            viewHolder.txtQuantity=(TextView)row.findViewById(R.id.txtMenuQty);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();

        final OrderMenuDTO menu=mMenus.get(position);
        if(menu.ismShow())
        {
            viewHolder.rowElement.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.rowElement.setVisibility(View.GONE);
            viewHolder.rowElement.setMinimumHeight(0);
        }
        Log.d(TAG, menu.toString());
        viewHolder.txtMenuTitle.setText(menu.getmMenuTitle());
        if(menu.ismFoodType())
        {
            viewHolder.imgFoodType.setImageResource(R.drawable.veg_icon);
        }

        else if(!menu.ismFoodType())
        {
            viewHolder.imgFoodType.setImageResource(R.drawable.non_veg_icon);
        }
        viewHolder.txtMenuTags.setText(menu.getmTags());
        viewHolder.txtMenuCategory.setText(menu.getmCategory());
        viewHolder.txtPrice.setText(String.format("%.2f", menu.getmPrice()));
        viewHolder.txtQuantity.setText(""+menu.getmQuantity());
        viewHolder.imgMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customButtonListener != null)
                    customButtonListener.onButtonClickListener(v.getId(), position, menu.getmQuantity(), menu);
            }
        });

        viewHolder.imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customButtonListener != null)
                    customButtonListener.onButtonClickListener(v.getId(), position, menu.getmQuantity(), menu);
            }
        });
        return row;
    }

    private class ViewHolder{
        LinearLayout rowElement;
        ImageView menuImage;
        ImageView imgFoodType;
        TextView txtMenuTitle;
        TextView txtMenuTags;
        TextView txtMenuCategory;
        TextView txtPrice;
        ImageView imgPlus;
        ImageView imgMinus;
        TextView txtQuantity;
    }
    public void setCustomButtonListner(CustomButtonListener listener) {
        this.customButtonListener = listener;
    }
    public interface CustomButtonListener{
        public void onButtonClickListener(int id,int position,int value,OrderMenuDTO orderMenu);
    }
}
