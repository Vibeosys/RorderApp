package com.vibeosys.rorderapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vibeosys.rorderapp.MainActivity;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.FeedbackAdapter;
import com.vibeosys.rorderapp.adaptors.PaymentModeAdapter;
import com.vibeosys.rorderapp.data.BillPaidUpload;
import com.vibeosys.rorderapp.data.FeedBackDTO;
import com.vibeosys.rorderapp.data.PaymentModeDbDTO;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.data.TableTransactionDbDTO;
import com.vibeosys.rorderapp.data.UploadCustomerFeedback;
import com.vibeosys.rorderapp.data.UploadFeedback;
import com.vibeosys.rorderapp.data.UploadOccupiedDTO;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.NetworkUtils;

import java.util.ArrayList;

/**
 * Created by shrinivas on 08-02-2016.
 */
public class BillPaymentOptionActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private TableCommonInfoDTO tableCommonInfoDTO;
    PaymentModeAdapter adapter;
    private int mPaymentModeId;
    private int mBillNo;
    private int mTableId;
    private int mTableNo;
    private String mCustId;
    private Context mContext = this;

    @Override
    protected String getScreenName() {
        return "Payment Modes";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_details_payment_screen);
        Spinner spineer = (Spinner) findViewById(R.id.paymentBy);
        Button closeBtn = (Button) findViewById(R.id.closeTableBtn);
        tableCommonInfoDTO = getIntent().getParcelableExtra("tableCustInfo");
        mBillNo = getIntent().getIntExtra("BillNo", 0);
        mTableId = tableCommonInfoDTO.getTableId();
        mTableNo = tableCommonInfoDTO.getTableNo();
        mCustId = tableCommonInfoDTO.getCustId();
        adapter = new PaymentModeAdapter(mDbRepository.getPaymentList(), getApplicationContext());
        spineer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        spineer.setOnItemSelectedListener(this);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                    closeTable();
                } else {
                    showMyDialog(mContext);
                }
                //
            }
        });

    }

    private void closeTable() {

        mDbRepository.setOccupied(false, mTableId);
        mDbRepository.clearUpdateTempData(mTableId, mTableNo, mCustId);
        mDbRepository.clearTableTransaction(mCustId, mTableId);
        UploadOccupiedDTO occupiedDTO = new UploadOccupiedDTO(mTableId, 0);
        Gson gson = new Gson();

        TableDataDTO[] tableDataDTOs = new TableDataDTO[3];
        String serializedJsonString = gson.toJson(occupiedDTO);
        tableDataDTOs[0] = new TableDataDTO(ConstantOperations.TABLE_OCCUPIED, serializedJsonString);

        TableTransactionDbDTO tableTransactionDbDTO = new TableTransactionDbDTO(mTableId, mCustId);
        String serializedTableTransaction = gson.toJson(tableTransactionDbDTO);
        tableDataDTOs[1] = new TableDataDTO(ConstantOperations.CLOSE_TABLE, serializedTableTransaction);

        BillPaidUpload billPaidUpload = new BillPaidUpload(mBillNo, 1, mPaymentModeId);
        String serializedBillPaid = gson.toJson(billPaidUpload);
        tableDataDTOs[2] = new TableDataDTO(ConstantOperations.PAID_BILL, serializedBillPaid);
        mServerSyncManager.uploadDataToServer(tableDataDTOs);

        showFeedBackDialog();
       /* Intent i = new Intent(getApplicationContext(), FeedbackActivity.class);
        i.putExtra("tableCustInfo", tableCommonInfoDTO);
        startActivity(i);
        finish();*/
    }

    private void showFeedBackDialog() {
        final Dialog dlg = new Dialog(BillPaymentOptionActivity.this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = getLayoutInflater().inflate(R.layout.activity_feedback, null);
        dlg.setContentView(view);
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dlg.setTitle(getResources().getString(R.string.feedback_activity_name));
        ListView mFeedbackList = (ListView) dlg.findViewById(R.id.listFeedback);
        TextView txtThank = (TextView) dlg.findViewById(R.id.txtThank);
        TextView txtSkip = (TextView) dlg.findViewById(R.id.dlg_skip);
        TextView txtTitle = (TextView) dlg.findViewById(R.id.dlg_title);
        txtTitle.setText(getResources().getString(R.string.feedback_activity_name));
        final ArrayList<FeedBackDTO> mFeedbacks = mDbRepository.getFeedBackList();
        FeedbackAdapter mFeedbackAdapter = new FeedbackAdapter(mFeedbacks, getApplicationContext());
        mFeedbackList.setAdapter(mFeedbackAdapter);

        txtThank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                    ArrayList<UploadFeedback> feedbackDbDTOs = new ArrayList<>();
                    for (FeedBackDTO feedBackDTO : mFeedbacks) {
                        feedbackDbDTOs.add(new UploadFeedback(feedBackDTO.getmFeedbackId(), feedBackDTO.getmRating()));
                    }

                    UploadCustomerFeedback customerFeedback = new UploadCustomerFeedback(mCustId, feedbackDbDTOs);

                    Gson gson = new Gson();

                    String serializedJsonString = gson.toJson(customerFeedback);
                    Log.d(TAG, "##" + serializedJsonString);
                    TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.CUSTOMER_FEEDBACK, serializedJsonString);
                    mServerSyncManager.uploadDataToServer(tableDataDTO);

                    Intent iMain = new Intent(getApplicationContext(), MainActivity.class);
                    iMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(iMain);
                    finish();
                } else {
                    showMyDialog(mContext);
                }
            }
        });
        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                Intent iMain = new Intent(getApplicationContext(), MainActivity.class);
                iMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(iMain);
                finish();
            }
        });
        dlg.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        PaymentModeDbDTO paymentModeDbDTO = (PaymentModeDbDTO) adapter.getItem(position);
        mPaymentModeId = paymentModeDbDTO.getPaymentModeId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
