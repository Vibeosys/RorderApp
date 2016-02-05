package com.vibeosys.rorderapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.OrderSummaryAdapter;
import com.vibeosys.rorderapp.data.OrderDetailsDTO;
import com.vibeosys.rorderapp.data.OrderHeaderDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 04-02-2016.
 */
public class TableOrderActivity extends BaseActivity implements OrderSummaryAdapter.ButtonListener {

    ExpandableListView ordersList;
    OrderSummaryAdapter adapter;
ArrayList<OrderHeaderDTO>list=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ordersList=(ExpandableListView)findViewById(R.id.expListViewForTableOrder);
        list=mDbRepository.getOrdersOfTable(1);
        OrderHeaderDTO currentOrder=mDbRepository.getOrederDetailsFromTemp(1,Integer.parseInt(mSessionManager.getUserId()));
        mDbRepository.getOrederDetailsGroupByID(list);
        list.add(0, currentOrder);
        adapter=new OrderSummaryAdapter(getApplicationContext(),list);
        ordersList.setAdapter(adapter);
        ordersList.setDividerHeight(2);
        ordersList.setGroupIndicator(null);
        ordersList.setClickable(true);
        adapter.setButtonListner(this);
        //adapter.onGroupExpanded(0);
        adapter.notifyDataSetChanged();

        ordersList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //parent.expandGroup(groupPosition);
                return false;
            }
        });
        ordersList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
        ordersList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        ordersList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                return false;
            }
        });
    }

    @Override
    public void onButtonClickListener(int id, int position, int value, OrderDetailsDTO orderMenu) {
        if (id == R.id.imgOrderMinus)
            if (value > 0)
                orderMenu.setOrderQuantity(value - 1);
            else
                Toast.makeText(getApplicationContext(), "Quantity Should be greater than 0", Toast.LENGTH_LONG).show();
        if (id == R.id.imgOrderPluse)
            orderMenu.setOrderQuantity(value + 1);
        //Collections.sort(allMenus);
       orderMenu.setOrderPrice(orderMenu.getOrderQuantity()*orderMenu.getMenuUnitPrice());
        mDbRepository.insertOrUpdateTempOrder(1, 10, orderMenu.getMenuId(), orderMenu.getOrderQuantity());
        adapter.notifyDataSetChanged();
    }
}
