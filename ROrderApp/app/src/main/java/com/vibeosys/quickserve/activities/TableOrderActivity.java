package com.vibeosys.quickserve.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.adaptors.OrderSummaryAdapter;
import com.vibeosys.quickserve.data.OrderDetailsDTO;
import com.vibeosys.quickserve.data.OrderHeaderDTO;
import com.vibeosys.quickserve.data.OrdersDbDTO;
import com.vibeosys.quickserve.data.PrinterDetailsDTO;
import com.vibeosys.quickserve.data.TableCommonInfoDTO;
import com.vibeosys.quickserve.data.TableDataDTO;
import com.vibeosys.quickserve.data.UploadOrderDetails;
import com.vibeosys.quickserve.data.UploadOrderHeader;
import com.vibeosys.quickserve.printutils.PrintBody;
import com.vibeosys.quickserve.printutils.PrintDataDTO;
import com.vibeosys.quickserve.printutils.PrintHeader;
import com.vibeosys.quickserve.printutils.PrintPaper;
import com.vibeosys.quickserve.printutils.PrinterFactory;
import com.vibeosys.quickserve.printutils.exceptions.OpenPrinterException;
import com.vibeosys.quickserve.printutils.exceptions.PrintException;
import com.vibeosys.quickserve.util.AppConstants;
import com.vibeosys.quickserve.util.ConstantOperations;
import com.vibeosys.quickserve.util.NetworkUtils;
import com.vibeosys.quickserve.util.ROrderDateUtils;
import com.vibeosys.quickserve.util.ServerSyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by akshay on 04-02-2016.
 */
public class TableOrderActivity extends BaseActivity implements
        OrderSummaryAdapter.ButtonListener, ServerSyncManager.OnStringResultReceived,
        OrderSummaryAdapter.PlaceOrderListener, ServerSyncManager.OnStringErrorReceived {

    private final static String screenName = "Place Order";
    private ExpandableListView mOrdersList;
    private OrderSummaryAdapter mAdapter;
    private ArrayList<OrderHeaderDTO> mList = new ArrayList<>();
    private TableCommonInfoDTO tableCommonInfo;
    private int mTableNo;
    private int mTableId;
    private int mTakeAwayNo;
    private int mDeliveryNo;
    private String mCustId;
    private OrderHeaderDTO mCurrentOrder;
    private Context mContext = this;
    private int mOrderFlag;
    private ProgressBar mProgressBar;
    private int mOrderType;
    private int resultCount = 0;
    private Set<Integer> keyRoomId;

    @Override
    protected String getScreenName() {
        return screenName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tableCommonInfo = getIntent().getParcelableExtra("tableCustInfo");
        mOrderFlag = getIntent().getIntExtra("orderTypeFlag", 0);
        mTableNo = tableCommonInfo.getTableNo();
        mTableId = tableCommonInfo.getTableId();
        mCustId = tableCommonInfo.getCustId();
        mTakeAwayNo = tableCommonInfo.getTakeAwayNo();
        mDeliveryNo = tableCommonInfo.getDeliveryNo();

        mOrderType = mTableId != 0 ? AppConstants.DINE_IN : mTakeAwayNo != 0 ? AppConstants.TAKE_AWAY : AppConstants.DELIVERY;
        setContentView(R.layout.activity_table_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mOrdersList = (ExpandableListView) findViewById(R.id.expListViewForTableOrder);
        mProgressBar = (ProgressBar) findViewById(R.id.select_reto_progress);
        mList = mDbRepository.getOrdersOfTable(mTableId, mCustId);
        mDbRepository.getOrederDetailsGroupByID(mList);
        if (mOrderFlag == 0) {
            mCurrentOrder = mDbRepository.getOrederDetailsFromTemp(mTableId, mSessionManager.getUserId(), mCustId);
            mList.add(0, mCurrentOrder);
        }

        mAdapter = new OrderSummaryAdapter(getApplicationContext(), mList);
        mOrdersList.setAdapter(mAdapter);
        mOrdersList.setDividerHeight(2);
        mOrdersList.setGroupIndicator(null);
        mOrdersList.setClickable(true);
        mAdapter.setButtonListner(this);
        mAdapter.notifyDataSetChanged();
        mAdapter.setPlaceOrderClick(this);
        mServerSyncManager.setOnStringResultReceived(this);
        mOrdersList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //parent.expandGroup(groupPosition);
                return false;
            }
        });
        mOrdersList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
        mOrdersList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        mOrdersList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

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
        orderMenu.setOrderPrice(orderMenu.getOrderQuantity() * orderMenu.getMenuUnitPrice());
        mDbRepository.insertOrUpdateTempOrder(mTableId, mTableNo, orderMenu.getMenuId(), orderMenu.getOrderQuantity(), mCustId, orderMenu.getmNote(), 0);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.order_summary_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.placeOrder) {

            sendEventToGoogle("Action", "ActionBar Place Order");
            //Toast.makeText(getApplicationContext(),"Button is clicke",Toast.LENGTH_LONG).show();
            /*Intent i = new Intent(this, BillDetailsActivity.class);
            startActivity(i);*/
            if (mOrderFlag == 1) {
                Toast.makeText(getApplicationContext(), "You Can not place an empty order", Toast.LENGTH_SHORT).show();
            } else {
                if (!NetworkUtils.isActiveNetworkAvailable(this)) {
                    String stringTitle = getResources().getString(R.string.error_msg_title_for_network);
                    String StringMessage = getResources().getString(R.string.error_msg_for_select_restaurant);
                    customAlterDialog(stringTitle, StringMessage);
                } else {
                    placeOrder();
                }

            }


        }
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mOrdersList.setVisibility(show ? View.GONE : View.VISIBLE);
            mOrdersList.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mOrdersList.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mOrdersList.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void placeOrder() {

        // OrderHeaderDTO currentOrder = mDbRepository.getOrederDetailsFromTemp(mTableId, mSessionManager.getUserId());
        int permissionId = mDbRepository.getPermissionId(AppConstants.PERMISSION_PLACE_ORDER);
        if (getPermissionStatus(permissionId)) {
            if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                OrderHeaderDTO sendOrderHeader = mDbRepository.getOrederDetailsFromTemp(mTableId, mSessionManager.getUserId(), mCustId);
                if (sendOrderHeader.getOrderDetailsDTOs().size() <= 0) {
                    showEmptyOrderDialog(mContext);
                } else {

                    List<OrderDetailsDTO> orderDetailsDTOs = sendOrderHeader.getOrderDetailsDTOs();
                    HashMap<Integer, List<OrderDetailsDTO>> sortOrderByKitchen = new HashMap<>();
                /*ArrayList<UploadOrderDetails> sendDetails = new ArrayList<>();
                for (OrderDetailsDTO orderDetail : orderDetailsDTOs) {
                    UploadOrderDetails sendOrder = new UploadOrderDetails(orderDetail.getMenuId(), orderDetail.getOrderQuantity(), orderDetail.getmNote());
                    sendDetails.add(sendOrder);
                }
                mOrderId = UUID.randomUUID();
                UploadOrderHeader sendOrder = new UploadOrderHeader(mOrderId.toString(), mTableId, mCustId, sendDetails, mTakeAwayNo, mOrderType);
                Gson gson = new Gson();

                String serializedJsonString = gson.toJson(sendOrder);
                Log.d(TAG, "##" + serializedJsonString);
                TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.PLACE_ORDER, serializedJsonString);
                mServerSyncManager.uploadDataToServer(tableDataDTO);*/
                    showProgress(true);
                    for (OrderDetailsDTO order : orderDetailsDTOs) {
                        int roomId = order.getRoomId();
                        if (!sortOrderByKitchen.containsKey(roomId)) {
                            List<OrderDetailsDTO> orderList = new ArrayList<>();
                            orderList.add(order);
                            sortOrderByKitchen.put(roomId, orderList);
                        } else {
                            List<OrderDetailsDTO> orderList = sortOrderByKitchen.get(roomId);
                            orderList.add(order);
                            sortOrderByKitchen.put(roomId, orderList);
                        }
                    }
                    keyRoomId = sortOrderByKitchen.keySet();
                    if (mDbRepository.getConfigValue(AppConstants.CONFIG_KOT_PRINT) == 0) {
                        for (final Integer i : keyRoomId) {
                            ArrayList<UploadOrderDetails> sendDetails = new ArrayList<>();
                            List<OrderDetailsDTO> orderListByRoom = sortOrderByKitchen.get(i);
                            for (OrderDetailsDTO orderDetail : orderListByRoom) {
                                UploadOrderDetails sendOrder = new UploadOrderDetails(orderDetail.getMenuId(), orderDetail.getOrderQuantity(), orderDetail.getmNote(), orderDetail.getmSubMenuId());
                                sendDetails.add(sendOrder);
                            }

                            UUID mOrderId;
                            mOrderId = UUID.randomUUID();
                            UploadOrderHeader sendOrder = new UploadOrderHeader(mOrderId.toString(), mTableId, mCustId, sendDetails, mTakeAwayNo, mOrderType, mDeliveryNo);
                            Gson gson = new Gson();

                            String serializedJsonString = gson.toJson(sendOrder);
                            Log.d(TAG, "##" + serializedJsonString);
                            TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.PLACE_ORDER, serializedJsonString);
                            mServerSyncManager.uploadDataToServer(tableDataDTO);
                        }
                    } else {
                        AsyncPrintData asyncPrintData = new AsyncPrintData();
                        asyncPrintData.execute(sortOrderByKitchen);
                    }
                }
            } else {
                showMyDialog(mContext);
            }
        } else {
            customAlterDialog(getResources().getString(R.string.dialog_access_denied), getResources().getString(R.string.access_denied_place_order));
        }
    }

    private void printKot(List<OrderDetailsDTO> orderListByRoom, int roomId) {

    }


    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {
        showProgress(false);
        int errorCode = -1;
        String message = null;
        int orderNo = 0;
        String orderId = null;
        try {
            errorCode = data.getInt("errorCode");
            message = data.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (errorCode == 0) {

            JSONObject jsMessage = null;
            try {
                jsMessage = new JSONObject(message);
                orderNo = jsMessage.getInt("orderNo");
                orderId = jsMessage.getString("orderId");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Order is Successfully Placed. Delete all temp entry and add data in order table.
            String currentDate = new ROrderDateUtils().getGMTCurrentDate();
            String currentTime = new ROrderDateUtils().getGMTCurrentTime();
            ArrayList<OrdersDbDTO> orders = new ArrayList<>();
            orders.add(new OrdersDbDTO(orderId, orderNo, mCustId,
                    Date.valueOf(currentDate), Time.valueOf(currentTime), mTableId, mSessionManager.getUserId(), 1));
            mDbRepository.insertOrders(orders);
            resultCount++;
            if (resultCount == keyRoomId.size()) {
                mDbRepository.clearUpdateTempData(mTableId, mTableNo, mCustId);
                mServerSyncManager.syncDataWithServer(true);
                Toast.makeText(getApplicationContext(), getResources().getString
                        (R.string.order_place_success), Toast.LENGTH_SHORT).show();
                Intent iMenu = new Intent(getApplicationContext(), TableMenusActivity.class);
                iMenu.putExtra("tableCustInfo", tableCommonInfo);
                startActivity(iMenu);
                finish();
            }

        } else if (errorCode != 104) {
           /* String stringTitle = getResources().getString(R.string.alert_dialog);
            customAlterDialog(stringTitle, message);*/
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent iMenu = new Intent(getApplicationContext(), TableMenusActivity.class);
        iMenu.putExtra("tableCustInfo", tableCommonInfo);
        startActivity(iMenu);
        finish();
    }

    @Override
    public void onPlaceOrderClick() {
        sendEventToGoogle("Action", "Place Order in list");
        placeOrder();
    }

    protected void showEmptyOrderDialog(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.show_network_alert);
        dialog.setTitle(getResources().getString(R.string.alert_dialog));
        TextView txtMessage = (TextView) dialog.findViewById(R.id.textView);
        txtMessage.setText(getResources().getString(R.string.empty_order_place));
        TextView txtOk = (TextView) dialog.findViewById(R.id.txtOk);
        txtOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEventToGoogle("Action", "Empty Order prevent");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error) {
        showProgress(false);
        String stringTitle = getResources().getString(R.string.error_msg_title_for_server);
        String stringMessage = getResources().getString(R.string.error_msg_for_server_details);
        customAlterDialog(stringTitle, stringMessage);
    }

    private class AsyncPrintData extends AsyncTask<HashMap<Integer, List<OrderDetailsDTO>>, Void, String> {
        //;
        HashMap<Integer, List<OrderDetailsDTO>> sortOrderByKitchen = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected String doInBackground(HashMap<Integer, List<OrderDetailsDTO>>... params) {
            keyRoomId = params[0].keySet();
            sortOrderByKitchen = params[0];
            for (final Integer i : keyRoomId) {
                ArrayList<UploadOrderDetails> sendDetails = new ArrayList<>();
                List<OrderDetailsDTO> orderListByRoom = sortOrderByKitchen.get(i);
                for (OrderDetailsDTO orderDetail : orderListByRoom) {
                    UploadOrderDetails sendOrder = new UploadOrderDetails(orderDetail.getMenuId(), orderDetail.getOrderQuantity(), orderDetail.getmNote(), orderDetail.getmSubMenuId());
                    sendDetails.add(sendOrder);
                }

                printKot(orderListByRoom, i);
                PrintBody printBody = new PrintBody();
                HashMap<String, OrderDetailsDTO> hshMap = new HashMap<>();
                /**
                 * Create hash map for kot printing 1 menu item and add quantity */

                for (OrderDetailsDTO order : orderListByRoom) {
                    String menuTitle = order.getMenuTitle();
                    if (hshMap.containsKey(menuTitle)) {
                        OrderDetailsDTO hshOrder = hshMap.get(menuTitle);
                        hshOrder.setOrderQuantity(hshOrder.getOrderQuantity() + order.getOrderQuantity());
                    } else {
                        hshMap.put(menuTitle, order);
                    }
                }
                printBody.setMenus(hshMap);
                Log.d("##", "##" + printBody.getMenus().toString());
                PrinterDetailsDTO printerDetails = mDbRepository.getPrinterDetailsByRoom(i);
                PrinterFactory printerFactory = new PrinterFactory();
                PrintPaper printPaper = printerFactory.getPrinter(printerDetails);
                try {
                    printPaper.setPrinter(getApplicationContext(), printerDetails);
                } catch (OpenPrinterException e) {
                    addError(screenName, "AsyncPrintData setPrinter", e.getMessage());
                    resultCount = 0;
                    return e.getMessage();
                }

                printPaper.openPrinter();
                PrintHeader header = new PrintHeader("Served By :" + mSessionManager.getUserName(), "Table No.: #"
                        + mTableNo, new ROrderDateUtils().getLocalTimeInReadableFormat());
                if (mTakeAwayNo > 0) {
                    header.setTableNo("Take Away No.: #" + mTakeAwayNo);
                } else if (mDeliveryNo > 0) {
                    header.setTableNo("Delivery No.: #" + mDeliveryNo);
                }
                String footer = "Powered by QuickServe";
                PrintDataDTO printData = new PrintDataDTO();
                printData.setHeader(header);
                printData.setFooter(footer);
                printData.setBody(printBody);
                printData.setType(PrintDataDTO.KOT);
                try {
                    printPaper.printText(printData);
                } catch (PrintException e) {
                    addError(screenName, "AsyncPrintData printText", e.getMessage());
                    resultCount = 0;
                    return e.getMessage();
                }
            }
            return "Success";
        }

        @Override
        protected void onPostExecute(String str) {
            super.onPostExecute(str);
            showProgress(false);
            if (str.equals("Success")) {
                for (final Integer i : keyRoomId) {
                    ArrayList<UploadOrderDetails> sendDetails = new ArrayList<>();
                    List<OrderDetailsDTO> orderListByRoom = sortOrderByKitchen.get(i);
                    for (OrderDetailsDTO orderDetail : orderListByRoom) {
                        UploadOrderDetails sendOrder = new UploadOrderDetails(orderDetail.getMenuId(), orderDetail.getOrderQuantity(), orderDetail.getmNote(), orderDetail.getmSubMenuId());
                        sendDetails.add(sendOrder);
                    }

                    UUID mOrderId;
                    mOrderId = UUID.randomUUID();
                    UploadOrderHeader sendOrder = new UploadOrderHeader(mOrderId.toString(), mTableId, mCustId, sendDetails, mTakeAwayNo, mOrderType, mDeliveryNo);
                    Gson gson = new Gson();

                    String serializedJsonString = gson.toJson(sendOrder);
                    Log.d(TAG, "##" + serializedJsonString);
                    TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.PLACE_ORDER, serializedJsonString);
                    mServerSyncManager.uploadDataToServer(tableDataDTO);
                }

            } else {
                customAlterDialog(getResources().getString(R.string.printer_error_title), str);
            }

        }
    }
}
