package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vibeosys.rorderapp.MainActivity;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.data.UploadOccupiedDTO;
import com.vibeosys.rorderapp.util.ConstantOperations;

/**
 * Created by shrinivas on 08-02-2016.
 */
public class BillPaymentOptionActivity extends BaseActivity {
    private TableCommonInfoDTO tableCommonInfoDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_details_payment_screen);
        Spinner spineer = (Spinner) findViewById(R.id.paymentBy);
        Button closeBtn = (Button) findViewById(R.id.closeTableBtn);
        tableCommonInfoDTO = getIntent().getParcelableExtra("tableCustInfo");
        final int tableId = tableCommonInfoDTO.getTableId();
        final int tableNo = tableCommonInfoDTO.getTableNo();
        final String custId = tableCommonInfoDTO.getCustId();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_String_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spineer.setAdapter(adapter);
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
                // finish();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(i);
                //
            }
        });

    }
}
