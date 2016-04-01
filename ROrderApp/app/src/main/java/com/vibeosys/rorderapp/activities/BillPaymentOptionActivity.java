package com.vibeosys.rorderapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vibeosys.rorderapp.MainActivity;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.FeedbackAdapter;
import com.vibeosys.rorderapp.adaptors.PaymentModeAdapter;
import com.vibeosys.rorderapp.data.ApplicationErrorDBDTO;
import com.vibeosys.rorderapp.data.BillPaidUpload;
import com.vibeosys.rorderapp.data.FeedBackDTO;
import com.vibeosys.rorderapp.data.PaymentModeDbDTO;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.data.TableTransactionDbDTO;
import com.vibeosys.rorderapp.data.UpdateCustomerDbDTO;
import com.vibeosys.rorderapp.data.UploadCustomerFeedback;
import com.vibeosys.rorderapp.data.UploadFeedback;
import com.vibeosys.rorderapp.data.UploadOccupiedDTO;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.PhoneNumberValidator;

import java.util.ArrayList;

/**
 * Created by shrinivas on 08-02-2016.
 */
public class BillPaymentOptionActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private static final String screenName = "Payment Modes";
    private TableCommonInfoDTO tableCommonInfoDTO;
    PaymentModeAdapter adapter;
    private int mPaymentModeId;
    private int mBillNo;
    private int mTableId;
    private int mTableNo;
    private String mCustId;
    private Context mContext = this;
    private double mDiscount;

    @Override
    protected String getScreenName() {
        return screenName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_details_payment_screen);
        Spinner spineer = (Spinner) findViewById(R.id.paymentBy);
        Button closeBtn = (Button) findViewById(R.id.closeTableBtn);
        tableCommonInfoDTO = getIntent().getParcelableExtra("tableCustInfo");
        mBillNo = getIntent().getIntExtra("BillNo", 0);
        mDiscount = getIntent().getDoubleExtra("DiscountAmt", 0);
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

        BillPaidUpload billPaidUpload = new BillPaidUpload(mBillNo, 1, mPaymentModeId, mDiscount);
        String serializedBillPaid = gson.toJson(billPaidUpload);
        tableDataDTOs[2] = new TableDataDTO(ConstantOperations.PAID_BILL, serializedBillPaid);
        mServerSyncManager.uploadDataToServer(tableDataDTOs);
        mServerSyncManager.syncDataWithServer(false);
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
        final EditText txtCustEmail = (EditText) dlg.findViewById(R.id.txtEmail);
        final EditText txtCustPhNo = (EditText) dlg.findViewById(R.id.txtPhNo);
        txtTitle.setText(getResources().getString(R.string.feedback_activity_name));
        final ArrayList<FeedBackDTO> mFeedbacks = mDbRepository.getFeedBackList();
        FeedbackAdapter mFeedbackAdapter = new FeedbackAdapter(mFeedbacks, getApplicationContext());
        mFeedbackList.setAdapter(mFeedbackAdapter);
        dlg.show();
        txtThank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtils.isActiveNetworkAvailable(getApplicationContext())) {
                    ArrayList<UploadFeedback> feedbackDbDTOs = new ArrayList<>();
                    for (FeedBackDTO feedBackDTO : mFeedbacks) {
                        feedbackDbDTOs.add(new UploadFeedback(feedBackDTO.getmFeedbackId(), feedBackDTO.getmRating()));
                    }
                    TableDataDTO[] tableDataDTOs = new TableDataDTO[2];

                    String custEmail = txtCustEmail.getText().toString();
                    String custMobNo = txtCustPhNo.getText().toString();
                    Gson gson = new Gson();
                    UpdateCustomerDbDTO customerDbDTO = new UpdateCustomerDbDTO(mCustId, custEmail, custMobNo);
                    String serializedJsonString1 = gson.toJson(customerDbDTO);


                    UploadCustomerFeedback customerFeedback = new UploadCustomerFeedback(mCustId, feedbackDbDTOs);
                    String serializedJsonString = gson.toJson(customerFeedback);
                    Log.d(TAG, "##" + serializedJsonString);
                    tableDataDTOs[0] = new TableDataDTO(ConstantOperations.CUSTOMER_FEEDBACK, serializedJsonString);
                    boolean chkFlag = false;
                    //boolean chkMobFlag = false;
                    if (TextUtils.isEmpty(custEmail)) {
                        if (!isEmailValid(custEmail)) {
                            chkFlag = false;
                            txtCustEmail.setError("Email id is not valid");
                        } else {
                            chkFlag = true;
                        }
                    } else {
                        chkFlag = true;
                    }
                    if (TextUtils.isEmpty(custMobNo)) {
                        PhoneNumberValidator validator = new PhoneNumberValidator();
                        if (validator.validatePhoneNumber(custMobNo) == false) {
                            txtCustPhNo.setError("Number is Not valid");
                            chkFlag = false;
                        } else {
                            chkFlag = true;
                        }
                    } else {
                        chkFlag = true;
                    }
                    if (!TextUtils.isEmpty(custEmail) || !TextUtils.isEmpty(custMobNo)) {
                        if (chkFlag)
                            tableDataDTOs[1] = new TableDataDTO(ConstantOperations.CUSTOMER_UPDATE, serializedJsonString1);
                    }
                    if (chkFlag) {
                        try {
                            mServerSyncManager.uploadDataToServer(tableDataDTOs);
                        } catch (Exception e) {
                            addError(screenName, "Thanks OnClickListener", e.getMessage());
                        }

                        Intent iMain = new Intent(getApplicationContext(), MainActivity.class);
                        iMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(iMain);
                        dlg.dismiss();
                        finish();
                        sendEventToGoogle("Action", "Give Feedback");
                    }

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
                sendEventToGoogle("Action", "Skip Feedback");
            }
        });

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
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
