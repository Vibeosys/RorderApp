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
import android.widget.TextView;
import android.widget.Toast;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.OrderListAdapter;
import com.vibeosys.rorderapp.data.MenuDbDTO;
import com.vibeosys.rorderapp.data.OrderMenuDTO;
import com.vibeosys.rorderapp.data.SelectedMenusDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TableMenusActivity extends BaseActivity implements TextWatcher, OrderListAdapter.CustomButtonListener {

    OrderListAdapter orderListAdapter;
    EditText edtSearch;
    List<OrderMenuDTO> allMenus;
    ListView listMenus;
    TextView txtTotalAmount, txtTotalItems;
    int mTableId, mTableNo;

    //List<OrderMenuDTO> sortingMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_menus);

        listMenus = (ListView) findViewById(R.id.listMenus);
        allMenus = mDbRepository.getOrderMenu();
        mTableId = getIntent().getIntExtra("TableId", 0);
        mTableNo = getIntent().getExtras().getInt("TableNo");
        //sortingMenu=mDbRepository.getOrderMenu();
        edtSearch = (EditText) findViewById(R.id.etSearch);
        txtTotalItems = (TextView) findViewById(R.id.txtTotalItems);
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalRs);
        edtSearch.addTextChangedListener(this);
        orderListAdapter = new OrderListAdapter(allMenus, getApplicationContext());
        orderListAdapter.setCustomButtonListner(this);
        listMenus.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();

        /// changes for Tool bar  01/02/2016 by Shrinivas

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void sortList(String search) {

        for (OrderMenuDTO menu : this.allMenus) {
            if (menu.getmCategory().toLowerCase().contains(search.toLowerCase())) {
                menu.setmShow(OrderMenuDTO.SHOW);
            } else if (menu.getmMenuTitle().toLowerCase().contains(search.toLowerCase())) {
                menu.setmShow(OrderMenuDTO.SHOW);
            } else if (menu.getmTags().toLowerCase().contains(search.toLowerCase())) {
                menu.setmShow(OrderMenuDTO.SHOW);
            } else {
                menu.setmShow(OrderMenuDTO.HIDE);
            }
        }
        Collections.sort(allMenus);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0 || s.length() < 3) {
            // sortList
            for (OrderMenuDTO menu : this.allMenus) {
                menu.setmShow(OrderMenuDTO.SHOW);
            }
            Collections.sort(allMenus);
            orderListAdapter.notifyDataSetChanged();
            //((OrderListAdapter) listMenus.getAdapter()).refresh(allMenus);
        }
        if (s.length() >= 3) {
            sortList(s.toString());
            orderListAdapter.notifyDataSetChanged();
        }
        displayMenuPriceAndItems();
    }

    private void displayMenuPriceAndItems() {
        ArrayList<OrderMenuDTO> selectedItems = new ArrayList<>();
        for (OrderMenuDTO menu : allMenus) {
            if (menu.getmQuantity() > 0) {
                selectedItems.add(menu);
            }
        }
        SelectedMenusDTO selectedMenusDTO = new SelectedMenusDTO(selectedItems);
        txtTotalAmount.setText(String.format(String.format("%.2f", selectedMenusDTO.getTotalBillAmount())) + " Rs.");
        txtTotalItems.setText(selectedMenusDTO.getTotalItems() + " Items are selected");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onButtonClickListener(int id, int position, int value, OrderMenuDTO orderMenu) {
        if (id == R.id.imgMinus)
            if (value > 0)
                orderMenu.setmQuantity(value - 1);
            else
                Toast.makeText(getApplicationContext(), "Quantity Should be greater than 0", Toast.LENGTH_LONG).show();
        if (id == R.id.imgPlus)
            orderMenu.setmQuantity(value + 1);
        //Collections.sort(allMenus);
        displayMenuPriceAndItems();
        mDbRepository.insertOrUpdateTempOrder(mTableId, mTableNo, orderMenu.getmMenuId(), orderMenu.getmQuantity());
        orderListAdapter.notifyDataSetChanged();
    }
}
