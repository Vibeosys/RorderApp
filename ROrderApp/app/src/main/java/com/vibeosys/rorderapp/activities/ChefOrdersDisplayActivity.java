package com.vibeosys.rorderapp.activities;

import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.ChefOrderAdapter;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 12-02-2016.
 */
public class ChefOrdersDisplayActivity  extends  BaseActivity{
    ExpandableListView chefOrderList;
    ChefOrderAdapter chefOrderAdapter;
    ArrayList<ChefOrderDetailsDTO> list  =new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chef_main_screen);
        chefOrderList = (ExpandableListView) findViewById(R.id.expListViewForChef);
        list = mDbRepository.getOrderHeadesInAsc();
        chefOrderAdapter = new ChefOrderAdapter(getApplicationContext(),list,mDbRepository);
        chefOrderList.setAdapter(chefOrderAdapter);
        chefOrderList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
               /* ChefOrderDetailsDTO mChefOrderDetailsDTO = (ChefOrderDetailsDTO) chefOrderAdapter.getGroup(groupPosition);
                mChefOrderDetailsDTO.setmMenuChild(mDbRepository.getChefMenu(mChefOrderDetailsDTO.getmNewOrderId()));
                chefOrderAdapter.notifyDataSetChanged();*/
            }
        });



    }
}
