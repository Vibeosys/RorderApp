package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.vibeosys.rorderapp.R;

/**
 * Created by shrinivas on 08-02-2016.
 */
public class BillPaymentOptionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_details_payment_screen);
        Spinner spineer = (Spinner) findViewById(R.id.paymentBy);
        Button closeBtn = (Button) findViewById(R.id.closeTableBtn);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.payment_String_array,android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spineer.setAdapter(adapter);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDbRepository.setOccupied(false, 4);
                mDbRepository.clearUpdateTempData(1,2,2);
                mDbRepository.clearTableTransaction(1,1);
                finish();
//                Intent i = new Intent(getApplicationContext(),CustomerFeedBackActivity.class);
//                startActivity(i);
            }
        });

    }
}
