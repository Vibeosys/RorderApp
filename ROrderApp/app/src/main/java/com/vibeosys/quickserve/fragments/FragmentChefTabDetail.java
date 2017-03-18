package com.vibeosys.quickserve.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.adaptors.ChefTabDetailAdapter;
import com.vibeosys.quickserve.data.ChefMenuDetailsDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 04-03-2016.
 */
public class FragmentChefTabDetail extends BaseFragment {
    ArrayList<ChefMenuDetailsDTO> menuDetailsDTO = new ArrayList<>();

    Context context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_list, container, false);
        Bundle args = this.getArguments();
        String orderId = args.getString("orderId");
        String waiterName = args.getString("waiterName");
        int tableNumber = args.getInt("tableNumber");
        int orderNumber = args.getInt("orderNumber");
        String orderTimeString = args.getString("orderTimeString");


        TextView waiterNameTv = (TextView) view.findViewById(R.id.recyclerServedByName);
        TextView orderNumberTv = (TextView) view.findViewById(R.id.recyclerOrderNoTab);
        TextView tableNumberTv = (TextView) view.findViewById(R.id.recyclerTableNoTab);
        TextView orderTimeTv = (TextView) view.findViewById(R.id.recyclerOrderTime);
        ListView orderMenuItem = (ListView) view.findViewById(R.id.recyclerMenuList);


        menuDetailsDTO = mDbRepository.getChefMenu(orderId);
        ChefTabDetailAdapter chefTabDetailAdapter = new ChefTabDetailAdapter(getContext(), menuDetailsDTO);
        orderMenuItem.setAdapter(chefTabDetailAdapter);


        waiterNameTv.setText("" + waiterName);
        orderNumberTv.setText("" + orderNumber);
        tableNumberTv.setText("" + tableNumber);
        orderTimeTv.setText("" + orderTimeString);

        return view;
    }

    @Override
    protected String getScreenName() {
        return "Chef dashboard Tab";
    }
}
