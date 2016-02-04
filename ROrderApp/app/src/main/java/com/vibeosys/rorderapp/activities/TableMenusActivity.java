package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.OrderListAdapter;
import com.vibeosys.rorderapp.data.MenuDbDTO;
import com.vibeosys.rorderapp.data.OrderDetailsDbDTO;
import com.vibeosys.rorderapp.data.OrderMenuDTO;
import com.vibeosys.rorderapp.data.OrdersDbDTO;
import com.vibeosys.rorderapp.data.SelectedMenusDTO;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TableMenusActivity extends BaseActivity implements OrderListAdapter.CustomButtonListener,View.OnClickListener{

    OrderListAdapter orderListAdapter;
    List<OrderMenuDTO> allMenus;
    ListView listMenus;
    TextView txtTotalAmount, txtTotalItems;
    int mTableId, mTableNo;
    LinearLayout llCurrentOrder;
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
        txtTotalItems = (TextView) findViewById(R.id.txtTotalItems);
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalRs);
        llCurrentOrder=(LinearLayout)findViewById(R.id.llCurrentOrder);
        orderListAdapter = new OrderListAdapter(allMenus, getApplicationContext());
        orderListAdapter.setCustomButtonListner(this);
        listMenus.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();
        llCurrentOrder.setOnClickListener(this);
        /// changes for Tool bar  01/02/2016 by Shrinivas

      /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.table_menus, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0 || s.length() < 3) {
                    // sortList
                    for (OrderMenuDTO menu : allMenus) {
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
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.llCurrentOrder)
        {
            /*List<OrdersDbDTO> orderInserts=new ArrayList<>();
            orderInserts.add(new OrdersDbDTO("" + 2, 1234, true, Date.valueOf("2016-02-02"),
                    Time.valueOf("8:50:22"), Date.valueOf("2016-02-02"), Date.valueOf("2016-02-02"), 1, "" + 1, 61));
            mDbRepository.insertOrders(orderInserts);

            List<OrderDetailsDbDTO> orderDetailsDbDTOList=new ArrayList<>();
            orderDetailsDbDTOList.add(new OrderDetailsDbDTO(3,23,1,Date.valueOf("2016-02-02"),Date.valueOf("2016-02-02"),""+2,3,"Kabab"));
            orderDetailsDbDTOList.add(new OrderDetailsDbDTO(4,36,1,Date.valueOf("2016-02-02"),Date.valueOf("2016-02-02"),""+2,4,"Garlic Bread"));
            mDbRepository.insertOrderDetails(orderDetailsDbDTOList);*/

            startActivity(new Intent(getApplicationContext(), TableOrderActivity.class));
        }
    }
}
