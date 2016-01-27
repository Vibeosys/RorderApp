package com.vibeosys.rorderapp.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.OrderListAdapter;
import com.vibeosys.rorderapp.data.MenuDbDTO;
import com.vibeosys.rorderapp.data.OrderMenuDTO;

import java.util.ArrayList;
import java.util.List;

public class TableMenusActivity extends BaseActivity implements TextWatcher {

    OrderListAdapter orderListAdapter;
    EditText edtSearch;
    List<OrderMenuDTO> allMenus;
    ListView listMenus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_menus);
        listMenus=(ListView)findViewById(R.id.listMenus);
        allMenus=mDbRepository.getOrderMenu();
        edtSearch=(EditText)findViewById(R.id.etSearch);
        edtSearch.addTextChangedListener(this);
        orderListAdapter=new OrderListAdapter(allMenus,getApplicationContext());
        listMenus.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();
    }

    public List<OrderMenuDTO> sortList(List<OrderMenuDTO> menus,String search) {
        List<OrderMenuDTO> menuDTOs = new ArrayList<OrderMenuDTO>();

        for (OrderMenuDTO menu : menus) {
            if(menu.getmCategory().contains(search)){
                menuDTOs.add(menu);
            }
            else if(menu.getmMenuTitle().contains(search))
            {
                menuDTOs.add(menu);
            }
            else if(menu.getmTags().contains(search))
            {
                menuDTOs.add(menu);
            }
        }
        return menuDTOs;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()==0||s.length()<3)
            {
                allMenus=mDbRepository.getOrderMenu();
                ((OrderListAdapter)listMenus.getAdapter()).refresh(allMenus);
            }
            if(s.length()>=3)
            {
                allMenus=sortList(mDbRepository.getOrderMenu(),s.toString());
                ((OrderListAdapter)listMenus.getAdapter()).refresh(allMenus);
            }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
