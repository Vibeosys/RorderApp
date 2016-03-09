package com.vibeosys.rorderapp.fragments;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.CustomerDbDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.service.SyncService;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

/**
 * Created by akshay on 08-03-2016.
 */
public class FragmentTakeAway extends BaseFragment implements ServerSyncManager.OnStringResultReceived,
        ServerSyncManager.OnStringErrorReceived {

    private TextView mTxtTotalCount;
    private GridView mGridView;

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
        TextView txtPlaceOrder = (TextView) dlg.findViewById(R.id.txtPlaceOrder);

        txtPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wrongCredential = false;
                View focus = null;

                txtCustomerAddress.setError(null);
                txtCustomerName.setError(null);

                String strName = txtCustomerName.getText().toString();
                String strAddress = txtCustomerAddress.getText().toString();

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
                    UUID custid = UUID.randomUUID();
                    CustomerDbDTO customer = new CustomerDbDTO(custid.toString(), strName, strAddress);
                    //here inserting custmer to custmer table
                    mDbRepository.insertCustomerDetails(customer);
                    // showProgress(true);
                    TableDataDTO[] tableDataDTOs = new TableDataDTO[3];
                    Gson gson = new Gson();
                    String serializedJsonString = gson.toJson(customer);
                    tableDataDTOs[0] = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
                    dlg.dismiss();
                }
            }
        });
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
        txtTitle.setText("Add take away");

        dlg.show();
    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error) {
        String title = "Server Error";
        String message = "" + error.toString();
        customAlterDialog(title, message);
    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {

        try {
            int errorCode = data.getInt("errorCode");
            String message = data.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
    }
}
