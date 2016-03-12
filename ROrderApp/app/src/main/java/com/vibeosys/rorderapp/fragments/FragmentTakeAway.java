package com.vibeosys.rorderapp.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.activities.BillDetailsActivity;
import com.vibeosys.rorderapp.activities.TableMenusActivity;
import com.vibeosys.rorderapp.adaptors.TakeAwayGridAdapter;
import com.vibeosys.rorderapp.adaptors.TakeAwaySourceAdapter;
import com.vibeosys.rorderapp.data.BillDetailsDTO;
import com.vibeosys.rorderapp.data.CustomerDbDTO;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.data.TableTransactionDbDTO;
import com.vibeosys.rorderapp.data.TakeAwayDTO;
import com.vibeosys.rorderapp.data.TakeAwaySourceDTO;
import com.vibeosys.rorderapp.data.UploadTakeAway;
import com.vibeosys.rorderapp.service.SyncService;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by akshay on 08-03-2016.
 */
public class FragmentTakeAway extends BaseFragment implements ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnStringErrorReceived {

    private TextView mTxtTotalCount;
    private GridView mGridView;
    private int mSourceId;
    private int mTakeAwayNo;
    private UUID custid;
    private ProgressBar mProgressBar;
    private LinearLayout mMainLayout;
    TextView txtTotalCount;
    GridView gridView;
    ArrayList<TakeAwayDTO> takeAwayDTOs;
    TakeAwayGridAdapter gridAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.take_away_content, container, false);
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
        takeAwayDTOs = mDbRepository.getTakeAwayList();
        gridAdapter = new TakeAwayGridAdapter(getActivity().getApplicationContext(), takeAwayDTOs);
        gridView.setAdapter(gridAdapter);
        return view;
    }

    private void showWaitingDialog(Bundle savedInstanceState) {
        final Dialog dlg = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = getLayoutInflater(savedInstanceState).inflate(R.layout.dialog_add_take_away, null);
        dlg.setContentView(view);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        TextView txtTitle = (TextView) dlg.findViewById(R.id.dlg_title);
        ImageView imgCancel = (ImageView) dlg.findViewById(R.id.imgClose);

        final EditText txtCustomerName = (EditText) dlg.findViewById(R.id.txtCustomerName);
        final EditText txtCustomerAddress = (EditText) dlg.findViewById(R.id.txtCustomerAddress);
        final EditText txtDiscountPer = (EditText) dlg.findViewById(R.id.txtDiscountPer);
        final EditText txtDeliveryCharges = (EditText) dlg.findViewById(R.id.txtDeliveryChrgs);

        TextView txtPlaceOrder = (TextView) dlg.findViewById(R.id.txtPlaceOrder);
        TextView txtCancel = (TextView) dlg.findViewById(R.id.txtCancel);
        Spinner spnSource = (Spinner) dlg.findViewById(R.id.spnSource);

        txtTitle.setText("Add take away");
        txtDiscountPer.setEnabled(false);

        ArrayList<TakeAwaySourceDTO> tableTransactionDbDTOs = mDbRepository.getTakeAwaySource();
        final TakeAwaySourceAdapter adapter = new TakeAwaySourceAdapter(getContext(),
                tableTransactionDbDTOs);
        spnSource.setAdapter(adapter);

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        txtPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrongCredential = false;
                View focus = null;

                txtCustomerAddress.setError(null);
                txtCustomerName.setError(null);

                String strName = txtCustomerName.getText().toString();
                String strAddress = txtCustomerAddress.getText().toString();
                double discount = Double.parseDouble(txtDiscountPer.getText().toString());
                String strDeliveryCharges = txtDeliveryCharges.getText().toString();
                double deliveryCharges = 0;
                if (!TextUtils.isEmpty(strDeliveryCharges)) {
                    deliveryCharges = Double.parseDouble(strDeliveryCharges);
                }


                if (TextUtils.isEmpty(strName)) {
                    txtCustomerName.setError("Field is required");
                    focus = txtCustomerName;
                    wrongCredential = true;
                }
                if (TextUtils.isEmpty(strAddress)) {
                    txtCustomerAddress.setError("Address is required");
                    focus = txtCustomerAddress;
                    wrongCredential = true;
                }
                if (wrongCredential) {
                    focus.requestFocus();
                } else {
                    showProgress(true);
                    custid = UUID.randomUUID();
                    CustomerDbDTO customer = new CustomerDbDTO(custid.toString(), strName, strAddress, "123");
                    //here inserting custmer to custmer table
                    mDbRepository.insertCustomerDetails(customer);
                    UUID takeAwayId = UUID.randomUUID();
                    UploadTakeAway uploadTakeAway = new UploadTakeAway(takeAwayId.toString(), discount, deliveryCharges, custid.toString(), mSourceId);
                    mDbRepository.insertTakeAway(uploadTakeAway, mSessionManager.getUserId());
                    // showProgress(true);
                    TableDataDTO[] tableDataDTOs = new TableDataDTO[2];
                    Gson gson = new Gson();
                    String serializedJsonString = gson.toJson(customer);
                    tableDataDTOs[0] = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);

                    String serializedTake = gson.toJson(uploadTakeAway);
                    tableDataDTOs[1] = new TableDataDTO(ConstantOperations.ADD_TAKEAWAY, serializedTake);
                    mServerSyncManager.uploadDataToServer(tableDataDTOs);
                    dlg.dismiss();
                }
            }
        });
        spnSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TakeAwaySourceDTO takeAwaySourceDTo = (TakeAwaySourceDTO) adapter.getItem(position);
                txtDiscountPer.setText(String.format("%.2f", takeAwaySourceDTo.getDiscount()));
                mSourceId = takeAwaySourceDTo.getTakeAwayId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dlg.show();
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

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error) {
        String title = "Server Error";
        String message = "" + error.toString();
        customAlterDialog(title, message);
        Log.d("##", "##" + message);
    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {
        showProgress(false);
        try {
            int errorCode = data.getInt("errorCode");
            String message = data.getString("message");
            if (errorCode == 0) {
                mTakeAwayNo = Integer.parseInt(message);
                mServerSyncManager.syncDataWithServer(false);
                callToMenuIntent(mTakeAwayNo, 0, custid.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void callToMenuIntent(int takeAwayNo, int tableId, String custId) {
        showProgress(false);
        TableCommonInfoDTO tableCommonInfoDTO = new TableCommonInfoDTO(tableId, custId, 0, takeAwayNo);
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
}
