package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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

/**
 * Created by shrinivas on 08-02-2016.
 */
public class BillPaymentOptionActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {
    private TableCommonInfoDTO tableCommonInfoDTO;
    PaymentModeAdapter adapter;
    private int mPaymentModeId;
    private int mBillNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_details_payment_screen);
        Spinner spineer = (Spinner) findViewById(R.id.paymentBy);
        Button closeBtn = (Button) findViewById(R.id.closeTableBtn);
        tableCommonInfoDTO = getIntent().getParcelableExtra("tableCustInfo");
        mBillNo = getIntent().getIntExtra("BillNo", 0);
        final int tableId = tableCommonInfoDTO.getTableId();
        final int tableNo = tableCommonInfoDTO.getTableNo();
        final String custId = tableCommonInfoDTO.getCustId();
        adapter = new PaymentModeAdapter(mDbRepository.getPaymentList(), getApplicationContext());
        spineer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        spineer.setOnItemSelectedListener(this);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDbRepository.setOccupied(false, tableId);
                mDbRepository.clearUpdateTempData(tableId, tableNo, custId);
                mDbRepository.clearTableTransaction(custId, tableId);
                UploadOccupiedDTO occupiedDTO = new UploadOccupiedDTO(tableId, 0);
                Gson gson = new Gson();
                String serializedJsonString = gson.toJson(occupiedDTO);
                TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.TABLE_OCCUPIED, serializedJsonString);
                mServerSyncManager.uploadDataToServer(tableDataDTO);

                TableTransactionDbDTO tableTransactionDbDTO = new TableTransactionDbDTO(tableId, custId);
                String serializedTableTransaction = gson.toJson(tableTransactionDbDTO);
                tableDataDTO = new TableDataDTO(ConstantOperations.CLOSE_TABLE, serializedTableTransaction);
                mServerSyncManager.uploadDataToServer(tableDataDTO);
                // finish();

                BillPaidUpload billPaidUpload = new BillPaidUpload(mBillNo, 1, mPaymentModeId);
                String serializedBillPaid = gson.toJson(billPaidUpload);
                tableDataDTO = new TableDataDTO(ConstantOperations.PAID_BILL, serializedBillPaid);
                mServerSyncManager.uploadDataToServer(tableDataDTO);

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);
                //
            }
        });

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
