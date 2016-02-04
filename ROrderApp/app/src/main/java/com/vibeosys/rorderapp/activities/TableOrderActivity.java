package com.vibeosys.rorderapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.OrderSummaryAdapter;
import com.vibeosys.rorderapp.data.OrderHeaderDTO;

import java.util.ArrayList;

/**
 * Created by akshay on 04-02-2016.
 */
public class TableOrderActivity extends BaseActivity {

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
        mDbRepository.getOrederDetailsGroupByID(list);
        adapter=new OrderSummaryAdapter(getApplicationContext(),list);
        ordersList.setAdapter(adapter);
        ordersList.setDividerHeight(2);
        ordersList.setGroupIndicator(null);
        ordersList.setClickable(true);

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
}
