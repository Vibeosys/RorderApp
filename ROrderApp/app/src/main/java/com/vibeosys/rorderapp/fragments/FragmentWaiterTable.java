package com.vibeosys.rorderapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.activities.BillDetailsActivity;
import com.vibeosys.rorderapp.activities.TableMenusActivity;
import com.vibeosys.rorderapp.adaptors.CustomerAdapter;
import com.vibeosys.rorderapp.adaptors.TableGridAdapter;
import com.vibeosys.rorderapp.data.BillDetailsDTO;
import com.vibeosys.rorderapp.data.CustomerDbDTO;
import com.vibeosys.rorderapp.data.HotelTableDTO;
import com.vibeosys.rorderapp.data.RestaurantTables;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.data.TableTransactionDbDTO;
import com.vibeosys.rorderapp.data.UploadOccupiedDTO;
import com.vibeosys.rorderapp.data.WaitingUserDTO;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.ROrderDateUtils;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by akshay on 08-03-2016.
 */
public class FragmentWaiterTable extends BaseFragment implements AdapterView.OnItemClickListener,
        ServerSyncManager.OnStringResultReceived, ServerSyncManager.OnStringErrorReceived {
    private static final String TAG = FragmentWaiterTable.class.getSimpleName();
    public static Handler UIHandler;
    TextView txtTotalCount;
    GridView gridView;
    public static TableGridAdapter adapter;
    List<RestaurantTables> hotelTableDTOs;
    private ProgressBar mProgressBar;
    private LinearLayout mMainLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_main, container, false);

        ImageButton fab = (ImageButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //callWaitingIntent();
                //Show waiting dialog
                sendEventToGoogle("Action", "Float Waiting list");
                showWaitingDialog(savedInstanceState);
            }
        });
        mServerSyncManager.setOnStringResultReceived(this);
        mServerSyncManager.setOnStringErrorReceived(this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.select_reto_progress);
        mMainLayout = (LinearLayout) view.findViewById(R.id.layout_main);
        txtTotalCount = (TextView) view.findViewById(R.id.txtCount);
        gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setOnItemClickListener(this);
        hotelTableDTOs = mDbRepository.getTableRecords("");
        adapter = new TableGridAdapter(getActivity().getApplicationContext(), hotelTableDTOs, mSessionManager.getUserId());
        gridView.setAdapter(adapter);
        txtTotalCount.setText("" + mDbRepository.getOccupiedTable() + " out of " + hotelTableDTOs.size() + " tables are occupied");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        txtTotalCount.setText("" + mDbRepository.getOccupiedTable() + " out of " + mDbRepository.getTableRecords("").size() + " tables are occupied");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        HotelTableDTO hotelTableDTO = (HotelTableDTO) adapter.getItem(position);
        if (hotelTableDTO.ismIsOccupied()) {
            String custId = mDbRepository.getCustmerIdFromTransaction(hotelTableDTO.getmTableId());
            Log.i(TAG, "## Customer Id " + custId);
            callToMenuIntent(hotelTableDTO.getmTableNo(), hotelTableDTO.getmTableId(), custId);
        } else {
            showReserveDialog(hotelTableDTO.getmTableNo(), hotelTableDTO.getmTableId());
        }
        Log.i(TAG, "##" + hotelTableDTO.getmTableNo() + "Is Clicked");
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

    private void callToMenuIntent(int tableNo, int tableId, String custId) {


        TableCommonInfoDTO tableCommonInfoDTO = new TableCommonInfoDTO(tableId, custId, tableNo, 0, 0, 0);
        BillDetailsDTO billDetailsDTO = mDbRepository.getBillDetailsRecords(custId);
        if (billDetailsDTO != null) {
            Intent intentBillDetails = new Intent(getActivity().getApplicationContext(), BillDetailsActivity.class);
            intentBillDetails.putExtra("tableCustInfo", tableCommonInfoDTO);
//        intentOpenTableMenu.putExtra("TableNo", tableNo);
//        intentOpenTableMenu.putExtra("TableId", tableId);
            startActivity(intentBillDetails);
        } else {
            Intent intentOpenTableMenu = new Intent(getActivity().getApplicationContext(), TableMenusActivity.class);
            intentOpenTableMenu.putExtra("tableCustInfo", tableCommonInfoDTO);
//        intentOpenTableMenu.putExtra("TableNo", tableNo);
//        intentOpenTableMenu.putExtra("TableId", tableId);
            startActivity(intentOpenTableMenu);
        }

    }

    private void showReserveDialog(final int tableNo, final int tableId) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_table_reserve);
        dialog.setTitle("Reserve Table");
        final EditText txtCustomerName = (EditText) dialog.findViewById(R.id.txtCustomerName);
        TextView cancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView reserve = (TextView) dialog.findViewById(R.id.txtReserve);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }

        });
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Customer Entered in db", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                UUID custid = UUID.randomUUID();

                String customerName = txtCustomerName.getText().toString();
                CustomerDbDTO customer = new CustomerDbDTO(custid.toString(), customerName);
                //here inserting custmer to custmer table
                mDbRepository.insertCustomerDetails(customer);
                //getting current date here
                String currentDate = new ROrderDateUtils().getGMTCurrentDate();

                TableTransactionDbDTO tableTransactionDbDTO = new TableTransactionDbDTO(tableId, mSessionManager.getUserId(), custid.toString(), 0, currentDate);
                //here inserting records in table transaction
                mDbRepository.insertTableTransaction(tableTransactionDbDTO);

                mDbRepository.setOccupied(true, tableId);
                uploadToServer(customer, tableTransactionDbDTO, tableId);
                adapter.refresh(mDbRepository.getTableRecords(""));
                callToMenuIntent(tableNo, tableId, custid.toString());
            }
        });
        dialog.show();
    }


    private void uploadToServer(CustomerDbDTO customer, TableTransactionDbDTO tableTransaction, int tableId) {
        showProgress(true);
        TableDataDTO[] tableDataDTOs = new TableDataDTO[3];
        Gson gson = new Gson();
        String serializedJsonString = gson.toJson(customer);
        tableDataDTOs[0] = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
        // mServerSyncManager.uploadDataToServer(tableDataDTO);

        UploadOccupiedDTO occupiedDTO = new UploadOccupiedDTO(tableId, 1);
        String serializedTableString = gson.toJson(occupiedDTO);
        tableDataDTOs[1] = new TableDataDTO(ConstantOperations.TABLE_OCCUPIED, serializedTableString);
        // mServerSyncManager.uploadDataToServer(tableDataDTO);

        String serializedTableTransaction = gson.toJson(tableTransaction);
        tableDataDTOs[2] = new TableDataDTO(ConstantOperations.TABLE_TRANSACTION, serializedTableTransaction);
        mServerSyncManager.uploadDataToServer(tableDataDTOs);
    }

    private void showWaitingDialog(Bundle savedInstanceState) {
        final Dialog dlg = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = getLayoutInflater(savedInstanceState).inflate(R.layout.dialog_waiting_list, null);
        dlg.setContentView(view);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final ArrayList<WaitingUserDTO> mWaitingList = mDbRepository.getWaitingList();
        final EditText mTxtCount = (EditText) dlg.findViewById(R.id.txtCustCount);
        final EditText mTxtName = (EditText) dlg.findViewById(R.id.txtCustomerName);
        final ImageButton btnClose = (ImageButton) dlg.findViewById(R.id.fabClose);
        TextView txtTitle = (TextView) dlg.findViewById(R.id.dlg_title);
        txtTitle.setText("Waiting List");
        Button mBtnAdd = (Button) dlg.findViewById(R.id.btnAdd);
        ListView mListCustomer = (ListView) dlg.findViewById(R.id.customerList);
        final CustomerAdapter mCustomerAdapter = new CustomerAdapter(getActivity().getApplicationContext(), mWaitingList);
        mListCustomer.setAdapter(mCustomerAdapter);
        mCustomerAdapter.notifyDataSetChanged();
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrongCredential = false;
                View focus = null;
                mTxtName.setError(null);
                mTxtCount.setError(null);

                String customerName = mTxtName.getText().toString();
                String strCount = mTxtCount.getText().toString();

                if (TextUtils.isEmpty(customerName)) {
                    mTxtName.setError("Customer Name is Required");
                    focus = mTxtName;
                    wrongCredential = true;
                }
                if (TextUtils.isEmpty(strCount)) {
                    mTxtCount.setError("Customer Count is Required");
                    focus = mTxtCount;
                    wrongCredential = true;
                }

                if (wrongCredential) {
                    focus.requestFocus();
                } else {
                    sendEventToGoogle("Action", " Add Customer");
                    UUID custid = UUID.randomUUID();
                    int customerCount = 0;
                    try {
                        customerCount = Integer.parseInt(strCount);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "## Insert Count null pointer" + e.toString());
                    }

                    CustomerDbDTO customer = new CustomerDbDTO(custid.toString(), customerName);
                    mDbRepository.insertCustomerDetails(customer);
                    String currentDate = new ROrderDateUtils().getGMTCurrentDate();
                    Log.d(TAG, "##" + currentDate);
                    TableTransactionDbDTO tableTransaction = new TableTransactionDbDTO(custid.toString(), 1, customerCount);
                    mDbRepository.insertTableTransaction(tableTransaction);
                    Toast.makeText(getActivity().getApplicationContext(), "Customer is Added successfully", Toast.LENGTH_SHORT).show();
                    mCustomerAdapter.refresh(mDbRepository.getWaitingList());
                    mTxtCount.setText("");
                    mTxtName.setText("");
                    Gson gson = new Gson();
                    TableDataDTO[] tableDataDTOs = new TableDataDTO[2];
                    String serializedJsonString = gson.toJson(customer);
                    //TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
                    tableDataDTOs[0] = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
                    String serializedTableTransaction = gson.toJson(tableTransaction);
                    tableDataDTOs[1] = new TableDataDTO(ConstantOperations.ADD_WAITING_CUSTOMER, serializedTableTransaction);
                    mServerSyncManager.uploadDataToServer(tableDataDTOs);
                }
            }
        });

        mListCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WaitingUserDTO waiting = (WaitingUserDTO) mCustomerAdapter.getItem(position);
                final Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_table_alocate);
                dialog.setTitle(getResources().getString(R.string.dialog_title_Allocate));
                final EditText txtTableNo = (EditText) dialog.findViewById(R.id.txtTableNumber);
                TextView txtReserve = (TextView) dialog.findViewById(R.id.txtReserve);
                TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);

                txtReserve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEventToGoogle("Action", "Table Reservation");
                        String strTableNo = txtTableNo.getText().toString();
                        if (TextUtils.isEmpty(strTableNo)) {
                            txtTableNo.setError(getResources().getString(R.string.error_table_no));
                            txtTableNo.requestFocus();
                        } else {
                            int tableId = mDbRepository.getTaleId(Integer.parseInt(strTableNo));
                            if (tableId == -1) {
                                Toast.makeText(getActivity().getApplicationContext(), "Table is Already Occupied", Toast.LENGTH_SHORT).show();
                            } else if (tableId == 0) {
                                Toast.makeText(getActivity().getApplicationContext(), "No Such Table found", Toast.LENGTH_SHORT).show();
                            } else {
                                TableTransactionDbDTO tableTransactionDbDTO = new TableTransactionDbDTO(tableId,
                                        mSessionManager.getUserId(), waiting.getmCustomerId(), 0,
                                        waiting.getmArrivalTime(), waiting.getmOccupancy());
                                mDbRepository.updateTableTransaction(tableTransactionDbDTO);
                                mDbRepository.setOccupied(true, tableId);

                                UploadOccupiedDTO occupiedDTO = new UploadOccupiedDTO(tableId, 1);
                                TableDataDTO[] tableDataDTOs = new TableDataDTO[2];
                                Gson gson = new Gson();
                                String serializedJsonString = gson.toJson(occupiedDTO);
                                tableDataDTOs[0] = new TableDataDTO(ConstantOperations.TABLE_OCCUPIED, serializedJsonString);
                                String serializedTableTransaction = gson.toJson(tableTransactionDbDTO);
                                tableDataDTOs[1] = new TableDataDTO(ConstantOperations.TABLE_TRANSACTION, serializedTableTransaction);
                                mServerSyncManager.uploadDataToServer(tableDataDTOs);
                                dialog.dismiss();
                                dlg.dismiss();
                                adapter.refresh(mDbRepository.getTableRecords(""));
                                callToMenuIntent(Integer.parseInt(strTableNo), tableId, waiting.getmCustomerId());
                            }

                        }

                    }
                });

                txtCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        mListCustomer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final WaitingUserDTO customer = (WaitingUserDTO) mCustomerAdapter.getItem(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Delete Customer");
                //builder.setIcon(R.drawable.ic_action_warning_yellow);
                builder.setMessage("Are you sure to delete " + customer.getmCustomerName());
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean flagDeleteCust = mDbRepository.deleteCustomerTableTrans(customer.getmCustomerId())
                                && mDbRepository.deleteCustomer(customer.getmCustomerId());
                        if (flagDeleteCust) {
                            mCustomerAdapter.refresh(mDbRepository.getWaitingList());
                            Gson gson = new Gson();
                            TableDataDTO[] tableDataDTOs = new TableDataDTO[1];
                            CustomerDbDTO deleteCustomer = new CustomerDbDTO(customer.getmCustomerId(), customer.getmCustomerName());
                            String serializedJsonString = gson.toJson(deleteCustomer);
                            //TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
                            tableDataDTOs[0] = new TableDataDTO(ConstantOperations.DELETE_CUSTOMER, serializedJsonString);
                            mServerSyncManager.uploadDataToServer(tableDataDTOs);
                            Toast.makeText(getActivity().getApplicationContext(), "Customer Deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
            }
        });
        dlg.show();
    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error) {
        showProgress(false);
        customAlterDialog("Warning Error", "" + error.toString());

    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {
        showProgress(false);
    }

    static {
        UIHandler = new Handler(Looper.getMainLooper());

    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);

    }
}
