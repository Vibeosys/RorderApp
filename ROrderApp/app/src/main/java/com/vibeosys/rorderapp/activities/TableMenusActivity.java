package com.vibeosys.rorderapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.NoteAdapter;
import com.vibeosys.rorderapp.adaptors.OrderListAdapter;
import com.vibeosys.rorderapp.adaptors.SubMenuAdapter;
import com.vibeosys.rorderapp.data.NoteDTO;
import com.vibeosys.rorderapp.data.OrderHeaderDTO;
import com.vibeosys.rorderapp.data.OrderMenuDTO;
import com.vibeosys.rorderapp.data.SelectedMenusDTO;
import com.vibeosys.rorderapp.data.SubMenuDTO;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.data.UploadBillGenerate;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TableMenusActivity extends BaseActivity implements
        OrderListAdapter.CustomButtonListener, View.OnClickListener, ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnDownloadReceived, ServerSyncManager.OnStringErrorReceived, SubMenuAdapter.SubMenuButtonListener {

    private static final String screenName = "Menu List";
    private TableCommonInfoDTO tableCommonInfoDTO;
    private OrderListAdapter orderListAdapter;
    private List<OrderMenuDTO> allMenus;
    private ListView listMenus;
    private TextView txtTotalAmount, txtTotalItems, txtBillGenerate;
    private LinearLayout txtPreviousOrder;
    private int mTableId, mTableNo, mTakeAwayNo;
    private String custId;
    private ImageButton imgFloat;
    private LinearLayout llCurrentOrder;
    private EditText txtSearch;
    private TextView txtTableNo;
    private ProgressBar mProgressBar;
    //  private ArrayList<OrderMenuDTO> mSelectedItems= new ArrayList<>();
    private int mCount = 0;
    private final Context mContext = this;
    private LinearLayout mMainLayout;
    SubMenuAdapter subMenuAdapter;

    @Override
    protected String getScreenName() {
        return screenName;
    }

    //List<OrderMenuDTO> sortingMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_menu_main);

        tableCommonInfoDTO = getIntent().getParcelableExtra("tableCustInfo");
        mTableId = tableCommonInfoDTO.getTableId();
        mTableNo = tableCommonInfoDTO.getTableNo();
        custId = tableCommonInfoDTO.getCustId();
        mTakeAwayNo = tableCommonInfoDTO.getTakeAwayNo();

        Log.i("##", "##" + mTakeAwayNo);
        setTitle(getResources().getString(R.string.title_search_cuisine));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listMenus = (ListView) findViewById(R.id.listMenus);
        allMenus = mDbRepository.getOrderMenu(custId);
//        mTableId = getIntent().getIntExtra("TableId", 0);
//        mTableNo = getIntent().getExtras().getInt("TableNo");
        //sortingMenu=mDbRepository.getOrderMenu();

        mProgressBar = (ProgressBar) findViewById(R.id.select_reto_progress);
        mMainLayout = (LinearLayout) findViewById(R.id.layout_main);

        txtTotalItems = (TextView) findViewById(R.id.txtTotalItems);
        txtSearch = (EditText) findViewById(R.id.search);
        txtTotalAmount = (TextView) findViewById(R.id.txtTotalRs);
        txtBillGenerate = (TextView) findViewById(R.id.txtGenerateBill);
        txtPreviousOrder = (LinearLayout) findViewById(R.id.txtPreviousOrders);
        llCurrentOrder = (LinearLayout) findViewById(R.id.llCurrentOrder);
        imgFloat = (ImageButton) findViewById(R.id.fab);
        txtTableNo = (TextView) findViewById(R.id.txtTableNo);

        String tableIndicator = "T-" + mTableNo;
        String orderIndicator = "# " + mTakeAwayNo;
        String strIndicator = mTableNo != 0 ? tableIndicator : orderIndicator;
        txtTableNo.setText(strIndicator);

        orderListAdapter = new OrderListAdapter(allMenus, getApplicationContext());
        orderListAdapter.setCustomButtonListner(this);
        listMenus.setAdapter(orderListAdapter);

        orderListAdapter.notifyDataSetChanged();
        llCurrentOrder.setOnClickListener(this);
        txtBillGenerate.setOnClickListener(this);
        txtPreviousOrder.setOnClickListener(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnDownloadReceived(this);
        imgFloat.setOnClickListener(this);
        /// changes for Tool bar  01/02/2016 by Shrinivas
        displayMenuPriceAndItems();
      /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //visiblity for bill generate button apply this as validation
      /*  int custIdFromOrders = mDbRepository.getCustmerCount(custId);
        if(custIdFromOrders == 0)
        {
            txtBillGenerate.setVisibility(View.GONE);
        }
        else
        {
            txtBillGenerate.setVisibility(View.VISIBLE);
        }*/
        //generateBillColour();
        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0 || s.length() < 3) {
                    allMenus = mDbRepository.getOrderMenu(custId);
                    ((OrderListAdapter) listMenus.getAdapter()).refresh(allMenus);
                }
                if (s.length() >= 3) {
                    allMenus = sortList(mDbRepository.getOrderMenu(custId), s.toString());
                    ((OrderListAdapter) listMenus.getAdapter()).refresh(allMenus);
                    sendEventToGoogle("Action", "Search Menu");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private List<OrderMenuDTO> sortList(List<OrderMenuDTO> menus, String search) {

       /* for (OrderMenuDTO menu : this.allMenus) {
            if (menu.getmCategory().toLowerCase().contains(search.toLowerCase())) {
                menu.setmShow(OrderMenuDTO.SHOW);
            } else if (menu.getmMenuTitle().toLowerCase().contains(search.toLowerCase())) {
                menu.setmShow(OrderMenuDTO.SHOW);
            } else if (menu.getmTags().toLowerCase().contains(search.toLowerCase())) {
                menu.setmShow(OrderMenuDTO.SHOW);
            } else {
                menu.setmShow(OrderMenuDTO.HIDE);
            }
        }*/
        List<OrderMenuDTO> menuDTOs = new ArrayList<OrderMenuDTO>();

        for (OrderMenuDTO menu : menus) {
            if (menu.getmCategory().toLowerCase().contains(search.toLowerCase())) {
                menuDTOs.add(menu);
            } else if (menu.getmMenuTitle().toLowerCase().contains(search.toLowerCase())) {
                menuDTOs.add(menu);
            } else if (menu.getmTags().toLowerCase().contains(search.toLowerCase())) {
                menuDTOs.add(menu);
            }
        }
        Collections.sort(menuDTOs);
        return menuDTOs;

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mMainLayout.setVisibility(show ? View.GONE : View.VISIBLE);
            mMainLayout.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mMainLayout.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mMainLayout.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void displayMenuPriceAndItems() {
        mCount = 0;
        ArrayList<OrderMenuDTO> mSelectedItems = new ArrayList<>();
        for (OrderMenuDTO menu : allMenus) {
            if (menu.getmQuantity() > 0) {
                mSelectedItems.add(menu);
            }
        }
        mCount = mSelectedItems.size();
        SelectedMenusDTO selectedMenusDTO = new SelectedMenusDTO(mSelectedItems);
        OrderHeaderDTO mCurrentOrder = mDbRepository.getOrederDetailsFromTemp(mTableId, mSessionManager.getUserId(), custId);
        txtTotalAmount.setText(String.format(String.format("%.0f", mCurrentOrder.getTotalBillAmount())) + " Rs.");
        txtTotalItems.setText(mCurrentOrder.getOrderDetailsDTOs().size() + " Items are selected");
    }

    @Override
    public void onButtonClickListener(int id, int position, int value, OrderMenuDTO orderMenu) {
        if (id == R.id.imgMinus) {
            if (orderMenu.isAvail()) {
                if (value > 0)
                    orderMenu.setmQuantity(value - 1);
                else
                    Toast.makeText(getApplicationContext(), "Quantity Should be greater than 0", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "This item is not available now", Toast.LENGTH_SHORT).show();
            }
            displayMenuPriceAndItems();
            mDbRepository.insertOrUpdateTempOrder(mTableId, mTableNo, orderMenu.getmMenuId(),
                    orderMenu.getmQuantity(), custId, orderMenu.getNote(), 0);
            orderListAdapter.notifyDataSetChanged();
        }
        if (id == R.id.imgPlus) {
            if (orderMenu.isAvail()) {
                orderMenu.setmQuantity(value + 1);
                displayMenuPriceAndItems();
                mDbRepository.insertOrUpdateTempOrder(mTableId, mTableNo, orderMenu.getmMenuId(),
                        orderMenu.getmQuantity(), custId, orderMenu.getNote(), 0);
                orderListAdapter.notifyDataSetChanged();
            } else
                Toast.makeText(getApplicationContext(), "This item is not available now", Toast.LENGTH_SHORT).show();

        }
        if (id == R.id.imgNote) {
            showMyDialog(orderMenu);
            Log.d(TAG, "##" + orderMenu.getNote());
            displayMenuPriceAndItems();
            sendEventToGoogle("Action", "Add Note");
            orderListAdapter.notifyDataSetChanged();
        }
        if (id == R.id.imgSubMenu) {
            showBevereges(orderMenu);
        }
        //Collections.sort(allMenus);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.table_menus, menu);
        /*SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                *//*if (s.length() == 0 || s.length() < 3) {
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
                displayMenuPriceAndItems();*//*
                if (s.length() == 0 || s.length() < 3) {
                    allMenus = mDbRepository.getOrderMenu(custId);
                    ((OrderListAdapter) listMenus.getAdapter()).refresh(allMenus);
                }
                if (s.length() >= 3) {
                    allMenus = sortList(mDbRepository.getOrderMenu(custId), s.toString());
                    ((OrderListAdapter) listMenus.getAdapter()).refresh(allMenus);
                }
                return false;
            }
        });*/

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
        int id = v.getId();
        if (id == R.id.llCurrentOrder) {
            /*List<OrdersDbDTO> orderInserts=new ArrayList<>();
            orderInserts.add(new OrdersDbDTO("" + 2, 1234, true, Date.valueOf("2016-02-02"),
                    Time.valueOf("8:50:22"), Date.valueOf("2016-02-02"), Date.valueOf("2016-02-02"), 1, "" + 1, 61));
            mDbRepository.insertOrders(orderInserts);

            List<OrderDetailsDbDTO> orderDetailsDbDTOList=new ArrayList<>();
            orderDetailsDbDTOList.add(new OrderDetailsDbDTO(3,23,1,Date.valueOf("2016-02-02"),Date.valueOf("2016-02-02"),""+2,3,"Kabab"));
            orderDetailsDbDTOList.add(new OrderDetailsDbDTO(4,36,1,Date.valueOf("2016-02-02"),Date.valueOf("2016-02-02"),""+2,4,"Garlic Bread"));
            mDbRepository.insertOrderDetails(orderDetailsDbDTOList);*/

            // TableCommonInfoDTO tableCommonInfoDTO = new TableCommonInfoDTO(1, "DEF", 10);
            if (mCount == 0) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_no_item_selected), Toast.LENGTH_SHORT).show();
            } else {
                Intent tableOrderIntent = new Intent(getApplicationContext(), TableOrderActivity.class);
                tableOrderIntent.putExtra("tableCustInfo", tableCommonInfoDTO);
                tableOrderIntent.putExtra("orderTypeFlag", 0);
                startActivity(tableOrderIntent);
                finish();
            }

        }
        if (id == R.id.txtGenerateBill) {
            //  Toast.makeText(getApplicationContext(),"bill genrate is cliked",Toast.LENGTH_LONG).show();
            if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                genrateBill();

            } else {
                customAlterNetworkDialog(getResources().getString(R.string.error_msg_title_for_network), getResources().getString(R.string.network_error));
            }
        }
        if (id == R.id.txtPreviousOrders) {
            sendEventToGoogle("Action", "View Prev Order");
            Intent tableOrderIntent = new Intent(getApplicationContext(), TableOrderActivity.class);
            tableOrderIntent.putExtra("tableCustInfo", tableCommonInfoDTO);
            tableOrderIntent.putExtra("orderTypeFlag", 1);
            startActivity(tableOrderIntent);
            finish();
        }
        if (id == R.id.fab) {
            sendEventToGoogle("Action", "Veg-Non veg Filter");
            showVegNonVeg();
        }

    }

    private void customAlterNetworkDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("" + title);
        builder.setIcon(R.drawable.ic_action_warning_yellow);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_SETTINGS));
            }
        });
        builder.show();

    }

    private void showConfirmDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Bill Generate");
        // builder.setIcon(R.drawable.ic_action_warning_yellow);
        builder.setMessage("Are you confirm to generate bill?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showProgress(true);
                UploadBillGenerate uploadBillGenerate = new UploadBillGenerate(mTableId, custId, mTakeAwayNo);
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(uploadBillGenerate);
                Log.d(TAG, "##" + serializedJsonString);
                TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.GENRATE_BILL, serializedJsonString);
                mServerSyncManager.uploadDataToServer(tableDataDTO);

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void genrateBill() {
        long pendingOrder = mDbRepository.getPendingOrdersOfTable(mTableId, custId);
        int tempOrder = mDbRepository.getOrderCountFromTemp(mTableId, custId);
        if (pendingOrder > 0 || tempOrder == 0) {
            //showAlertDiaog();
            String stringTitle = getResources().getString(R.string.alert_dialog);
            String strMessage = getResources().getString(R.string.order_is_pending_to_serve);
            customAlterDialog(stringTitle, strMessage);
        } else {
            showConfirmDialog();
        }


    }


    private void showAlertDiaog() {
        final Dialog dialog = new Dialog(TableMenusActivity.this);
        dialog.setContentView(R.layout.show_network_alert);
        dialog.setTitle(getResources().getString(R.string.alert_dialog));
        TextView txtMessage = (TextView) dialog.findViewById(R.id.textView);
        txtMessage.setText(getResources().getString(R.string.order_is_pending_to_serve));
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        generateBillColour();
    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {
        showProgress(false);
        int errorCode = -1;
        String message = null;


        try {
            errorCode = data.getInt("errorCode");
            message = data.getString("message");
            Log.d(TAG, "##" + errorCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (errorCode == 0) {
            /*Successfully send data*/
            Toast.makeText(getApplicationContext(), "Bill is Generated", Toast.LENGTH_LONG).show();
            Log.d(TAG, "##" + errorCode);
            mServerSyncManager.syncDataWithServer(false);
        } else if (errorCode != 104) {
            String stringTitle = getResources().getString(R.string.alert_dialog);
            customAlterDialog(stringTitle, message);
            // Toast.makeText(getApplicationContext(), "response " + errorCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDownloadResultReceived(@NonNull Map<String, Integer> results) {
        Intent iMenu = new Intent(getApplicationContext(), BillDetailsActivity.class);
        iMenu.putExtra("tableCustInfo", tableCommonInfoDTO);
        startActivity(iMenu);
        finish();
    }


    private void showMyDialog(final OrderMenuDTO orderMenu) {
        final String[] orderNote = {""};
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.setTitle(orderMenu.getmMenuTitle());
        dialog.setContentView(R.layout.dialog_select_note);
        ArrayList<NoteDTO> notes = mDbRepository.getNoteList();
        final NoteAdapter noteadapter = new NoteAdapter(dialog.getContext(), notes);
        ListView listNotes = (ListView) dialog.findViewById(R.id.noteList);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView txtMenuName = (TextView) dialog.findViewById(R.id.txtMenuName);
        TextView txtOrder = (TextView) dialog.findViewById(R.id.txtOrder);
        listNotes.setAdapter(noteadapter);
        txtMenuName.setText(orderMenu.getmMenuTitle());
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteDTO noteDto = (NoteDTO) noteadapter.getItem(position);
                String selectedNote = noteDto.getNoteTitle();
                orderMenu.setNote(selectedNote);
                mDbRepository.insertOrUpdateTempOrder(mTableId, mTableNo, orderMenu.getmMenuId(),
                        orderMenu.getmQuantity(), custId, orderMenu.getNote(), 0);
                noteadapter.setItemChecked(noteDto.getNoteId());
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                orderMenu.setNote("");
            }
        });
        txtOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void generateBillColour() {
        //OrderHeaderDTO currentOrder = mDbRepository.getOrederDetailsFromTemp(mTableId, mSessionManager.getUserId(), custId);

        long pendingOrder = mDbRepository.getPendingOrdersOfTable(mTableId, custId);
        int tempOrder = mDbRepository.getOrderCountFromTemp(mTableId, custId);
        if (pendingOrder > 0 || tempOrder == 0) {
            txtBillGenerate.setBackgroundColor(getResources().getColor(R.color.light_grey));
            //txtBillGenerate.setTextColor(getResources().getColor(R.color.white_color));
        } else {
            txtBillGenerate.setBackgroundColor(getResources().getColor(R.color.red));
            //txtBillGenerate.setTextColor(getResources().getColor(R.color.white_color));
        }
    }

    private void showVegNonVeg() {
        final Dialog dlg = new Dialog(TableMenusActivity.this, android.R.style.Theme_Black_NoTitleBar);
        View view = getLayoutInflater().inflate(R.layout.dilaog_floating_veg_nonveg, null);
        dlg.setContentView(view);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dlg.setCancelable(true);
        dlg.setCanceledOnTouchOutside(true);
        ImageButton btnVeg = (ImageButton) dlg.findViewById(R.id.btnVeg);
        ImageButton btnNoveg = (ImageButton) dlg.findViewById(R.id.btnNonVeg);
        ImageButton btnReset = (ImageButton) dlg.findViewById(R.id.btnReset);
        ImageButton btnClose = (ImageButton) dlg.findViewById(R.id.btnClose);
        ImageButton btnBeverage = (ImageButton) dlg.findViewById(R.id.btnBeverages);
        btnVeg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allMenus = sortVegNon(mDbRepository.getOrderMenu(custId), 1);
                ((OrderListAdapter) listMenus.getAdapter()).refresh(allMenus);
                dlg.dismiss();
            }
        });
        btnNoveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allMenus = sortVegNon(mDbRepository.getOrderMenu(custId), 2);
                ((OrderListAdapter) listMenus.getAdapter()).refresh(allMenus);
                dlg.dismiss();
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allMenus = mDbRepository.getOrderMenu(custId);
                ((OrderListAdapter) listMenus.getAdapter()).refresh(allMenus);
                dlg.dismiss();
            }
        });
        btnBeverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allMenus = sortVegNon(mDbRepository.getOrderMenu(custId), 3);
                ((OrderListAdapter) listMenus.getAdapter()).refresh(allMenus);
                dlg.dismiss();
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        dlg.show();
    }

    private List<OrderMenuDTO> sortVegNon(ArrayList<OrderMenuDTO> menus, int i) {
        List<OrderMenuDTO> menuDTOs = new ArrayList<OrderMenuDTO>();
        //boolean flag = i == 0 ? false : true;
        for (OrderMenuDTO menu : menus) {
            if (menu.getFbType() == i)
                menuDTOs.add(menu);
        }
        Collections.sort(menuDTOs);
        return menuDTOs;
    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error) {
        showProgress(false);
        String stringTitle = getResources().getString(R.string.error_msg_title_for_server);
        String stringMessage = getResources().getString(R.string.error_msg_for_server_details_bill);
        customAlterDialog(stringTitle, stringMessage);
        Log.d(TAG, "##" + error.toString());
    }

    private void showBevereges(OrderMenuDTO orderMenu) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.setTitle(orderMenu.getmMenuTitle());
        dialog.setContentView(R.layout.dialog_sub_menu_list);
        ArrayList<SubMenuDTO> subMenuDTOs = mDbRepository.getSubMenu(orderMenu.getmMenuId(), custId);
        subMenuAdapter = new SubMenuAdapter(mContext, subMenuDTOs, orderMenu);
        subMenuAdapter.setCustomButtonListner(this);
        ListView subMenuList = (ListView) dialog.findViewById(R.id.subMenuList);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView txtMenuName = (TextView) dialog.findViewById(R.id.txtMenuName);
        TextView txtDone = (TextView) dialog.findViewById(R.id.txtDone);
        subMenuList.setAdapter(subMenuAdapter);
        txtMenuName.setText(orderMenu.getmMenuTitle());
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onSubMenuButtonListener(int id, int position, int value, SubMenuDTO subMenu, OrderMenuDTO orderMenuDTO) {
        if (id == R.id.imgMinus) {

            if (value > 0) {
                subMenu.setQuantity(value - 1);
                orderMenuDTO.setmQuantity(orderMenuDTO.getmQuantity() - 1);
            } else
                Toast.makeText(getApplicationContext(), "Quantity Should be greater than 0", Toast.LENGTH_LONG).show();
            //displayMenuPriceAndItems();
            mDbRepository.insertOrUpdateTempOrder(mTableId, mTableNo, subMenu.getMenuId(),
                    subMenu.getQuantity(), custId, subMenu.getNote(), subMenu.getSubMenuId());
            orderListAdapter.notifyDataSetChanged();
            subMenuAdapter.notifyDataSetChanged();
        }
        if (id == R.id.imgPlus) {
            subMenu.setQuantity(value + 1);
            orderMenuDTO.setmQuantity(orderMenuDTO.getmQuantity() + 1);
            displayMenuPriceAndItems();
            mDbRepository.insertOrUpdateTempOrder(mTableId, mTableNo, subMenu.getMenuId(),
                    subMenu.getQuantity(), custId, subMenu.getNote(), subMenu.getSubMenuId());
            orderListAdapter.notifyDataSetChanged();
            subMenuAdapter.notifyDataSetChanged();
        }
        if (id == R.id.imgNote) {
            showMyDialog(subMenu);
            Log.d(TAG, "##" + subMenu.getNote());
            displayMenuPriceAndItems();
            sendEventToGoogle("Action", "Add Note");
            subMenuAdapter.notifyDataSetChanged();
        }
    }

    private void showMyDialog(final SubMenuDTO subMenu) {
        final String[] orderNote = {""};
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // dialog.setTitle(orderMenu.getmMenuTitle());
        dialog.setContentView(R.layout.dialog_select_note);
        ArrayList<NoteDTO> notes = mDbRepository.getNoteList();
        final NoteAdapter noteadapter = new NoteAdapter(dialog.getContext(), notes);
        ListView listNotes = (ListView) dialog.findViewById(R.id.noteList);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView txtMenuName = (TextView) dialog.findViewById(R.id.txtMenuName);
        TextView txtOrder = (TextView) dialog.findViewById(R.id.txtOrder);
        listNotes.setAdapter(noteadapter);
        txtMenuName.setText(subMenu.getMenuTitle());
        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoteDTO noteDto = (NoteDTO) noteadapter.getItem(position);
                String selectedNote = noteDto.getNoteTitle();
                subMenu.setNote(selectedNote);
                mDbRepository.insertOrUpdateTempOrder(mTableId, mTableNo, subMenu.getMenuId(),
                        subMenu.getQuantity(), custId, subMenu.getNote(), subMenu.getSubMenuId());
                noteadapter.setItemChecked(noteDto.getNoteId());
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                subMenu.setNote("");
            }
        });
        txtOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
