package com.vibeosys.quickserve.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.data.OrderMenuDTO;
import com.vibeosys.quickserve.util.CustomVolleyRequestQueue;
import com.vibeosys.quickserve.views.NetworkRoundImageView;

import java.util.List;

/**
 * Created by akshay on 27-01-2016.
 */
public class OrderListAdapter extends BaseAdapter {

    private static final String TAG = OrderListAdapter.class.getSimpleName();
    private List<OrderMenuDTO> mMenus;
    private Context mContext;
    CustomButtonListener customButtonListener;
    private ImageLoader mImageLoader;

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

    public void refresh(List<OrderMenuDTO> menus) {
        this.mMenus.clear();
        this.mMenus = menus;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;
        if (row == null) {
            LayoutInflater theLayoutInflator = (LayoutInflater) mContext.getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            row = theLayoutInflator.inflate(R.layout.row_table_menu, null);
            viewHolder = new ViewHolder();
            viewHolder.rowElement = (LinearLayout) row.findViewById(R.id.row_menu_item);
            // viewHolder.menuImage=(ImageView)row.findViewById(R.id.imgMenu);
            viewHolder.imgFoodType = (ImageView) row.findViewById(R.id.imgFoodType);
            viewHolder.imgSpicy = (ImageView) row.findViewById(R.id.imgSpicy);
            viewHolder.txtMenuTitle = (TextView) row.findViewById(R.id.txtMenuName);
            viewHolder.txtMenuTags = (TextView) row.findViewById(R.id.txtMenuTag);
            viewHolder.txtMenuCategory = (TextView) row.findViewById(R.id.txtCategory);
            viewHolder.txtPrice = (TextView) row.findViewById(R.id.txtMenuPrice);
            viewHolder.imgPlus = (ImageView) row.findViewById(R.id.imgPlus);
            viewHolder.imgMinus = (ImageView) row.findViewById(R.id.imgMinus);
            viewHolder.txtQuantity = (TextView) row.findViewById(R.id.txtMenuQty);
            viewHolder.networkImageView = (NetworkRoundImageView) row.findViewById(R.id.menuImageLoader);
            viewHolder.imgNote = (ImageView) row.findViewById(R.id.imgNote);
            viewHolder.imgSubMenu = (ImageView) row.findViewById(R.id.imgSubMenu);
            row.setTag(viewHolder);

        } else viewHolder = (ViewHolder) row.getTag();

        final OrderMenuDTO menu = mMenus.get(position);
        if (menu.getmShow() == 1) {
            viewHolder.rowElement.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rowElement.setVisibility(View.GONE);
            viewHolder.rowElement.setMinimumHeight(0);
        }
        Log.d(TAG, menu.toString());
        viewHolder.txtMenuTitle.setText(menu.getmMenuTitle());
        if (menu.getFbType() == 1) {
            viewHolder.imgFoodType.setVisibility(View.VISIBLE);
            viewHolder.imgFoodType.setImageResource(R.drawable.veg_icon);
        } else if (menu.getFbType() == 2) {
            viewHolder.imgFoodType.setVisibility(View.VISIBLE);
            viewHolder.imgFoodType.setImageResource(R.drawable.non_veg_icon);
        } else if (menu.getFbType() == 3) {
            viewHolder.imgFoodType.setVisibility(View.VISIBLE);
            viewHolder.imgFoodType.setImageResource(R.drawable.beverage_icon);
        } else {
            viewHolder.imgFoodType.setVisibility(View.INVISIBLE);
        }
        if (menu.getmPrice() != 0) {
            viewHolder.imgSubMenu.setVisibility(View.INVISIBLE);
            viewHolder.imgMinus.setVisibility(View.VISIBLE);
            viewHolder.imgPlus.setVisibility(View.VISIBLE);
            viewHolder.imgNote.setVisibility(View.VISIBLE);
            viewHolder.txtQuantity.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgSubMenu.setVisibility(View.VISIBLE);
            viewHolder.imgMinus.setVisibility(View.INVISIBLE);
            viewHolder.imgPlus.setVisibility(View.INVISIBLE);
            viewHolder.imgNote.setVisibility(View.INVISIBLE);
            viewHolder.txtQuantity.setVisibility(View.INVISIBLE);
        }
        if (menu.isSpicy()) {
            viewHolder.imgSpicy.setVisibility(View.VISIBLE);
        } else {
            viewHolder.imgSpicy.setVisibility(View.INVISIBLE);
        }
        mImageLoader = CustomVolleyRequestQueue.getInstance(mContext)
                .getImageLoader();
        //Image URL - This can point to any image file supported by Android
        final String url = menu.getmImage();
        if (url != null && !url.isEmpty()) {
            try {
                mImageLoader.get(url, ImageLoader.getImageListener(viewHolder.networkImageView,
                        R.drawable.menu_image_generic, R.drawable.menu_image_generic));
                viewHolder.networkImageView.setImageUrl(url, mImageLoader);
            } catch (Exception e) {
                viewHolder.networkImageView.setImageResource(R.drawable.menu_image_generic);
            }
        } else {
            viewHolder.networkImageView.setImageResource(R.drawable.menu_image_generic);
        }


        String menuTag = menu.getmTags();
        if (menuTag != null && !menuTag.isEmpty() && !menuTag.equals("null")) {
            viewHolder.txtMenuTags.setText(menuTag);
        } else {
            viewHolder.txtMenuTags.setText("");
        }
        viewHolder.txtMenuCategory.setText(menu.getmCategory());
        viewHolder.txtPrice.setText(String.format("%.0f", menu.getmPrice()));
        viewHolder.txtQuantity.setText("" + menu.getmQuantity());
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
        viewHolder.imgNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customButtonListener != null)
                    customButtonListener.onButtonClickListener(v.getId(), position, menu.getmMenuId(), menu);
            }
        });
        viewHolder.imgSubMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customButtonListener != null)
                    customButtonListener.onButtonClickListener(v.getId(), position, menu.getmMenuId(), menu);
            }
        });
        return row;
    }

    private class ViewHolder {
        LinearLayout rowElement;
        ImageView menuImage;
        ImageView imgFoodType;
        ImageView imgSpicy;
        TextView txtMenuTitle;
        TextView txtMenuTags;
        TextView txtMenuCategory;
        TextView txtPrice;
        ImageView imgPlus;
        ImageView imgMinus;
        TextView txtQuantity;
        NetworkRoundImageView networkImageView;
        ImageView imgNote;
        ImageView imgSubMenu;
    }

    public void setCustomButtonListner(CustomButtonListener listener) {
        this.customButtonListener = listener;
    }

    public interface CustomButtonListener {
        public void onButtonClickListener(int id, int position, int value, OrderMenuDTO orderMenu);
    }
}
