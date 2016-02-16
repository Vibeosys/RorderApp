package com.vibeosys.rorderapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.vibeosys.rorderapp.MainActivity;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.PaymentModeAdapter;
import com.vibeosys.rorderapp.data.BillPaidUpload;
import com.vibeosys.rorderapp.data.PaymentModeDbDTO;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.data.TableTransactionDbDTO;
import com.vibeosys.rorderapp.data.UploadOccupiedDTO;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.NetworkUtils;

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
        String serializedJsonString = gson.toJson(occupiedDTO);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.TABLE_OCCUPIED, serializedJsonString);
        mServerSyncManager.uploadDataToServer(tableDataDTO);

        TableTransactionDbDTO tableTransactionDbDTO = new TableTransactionDbDTO(mTableId, mCustId);
        String serializedTableTransaction = gson.toJson(tableTransactionDbDTO);
        tableDataDTO = new TableDataDTO(ConstantOperations.CLOSE_TABLE, serializedTableTransaction);
        mServerSyncManager.uploadDataToServer(tableDataDTO);
        // finish();

        BillPaidUpload billPaidUpload = new BillPaidUpload(mBillNo, 1, mPaymentModeId);
        String serializedBillPaid = gson.toJson(billPaidUpload);
        tableDataDTO = new TableDataDTO(ConstantOperations.PAID_BILL, serializedBillPaid);
        mServerSyncManager.uploadDataToServer(tableDataDTO);

        Intent i = new Intent(getApplicationContext(), FeedbackActivity.class);
        i.putExtra("tableCustInfo", tableCommonInfoDTO);
        startActivity(i);
        finish();
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
