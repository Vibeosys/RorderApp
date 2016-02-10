package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vibeosys.rorderapp.data.BillDbDTO;
import com.vibeosys.rorderapp.data.BillDetailsDTO;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrinivas on 08-02-2016.
 */
public class BillDetailsActivity extends BaseActivity {

    private BillDetailsDTO mBillDetailsDTOs;
    private TableCommonInfoDTO tableCommonInfoDTO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_amount_payment);
        setTitle(getResources().getString(R.string.Bill_Summary));
        BillDbDTO db = new BillDbDTO(1, Date.valueOf("02-02-2016"), Time.valueOf("10:10:11"),1200.00,102.00,10.00,Date.valueOf("02-02-2016"),Date.valueOf("02-02-2016"),2);

       List<BillDbDTO> bill = new ArrayList<>();
        bill.add(db);

        mDbRepository.insertBills(bill);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Tool bar intitilization
        TextView tableNo = (TextView) findViewById(R.id.TableNumber);
        TextView orderNo = (TextView) findViewById(R.id.OrderNumber);
        TextView servedBy = (TextView)findViewById(R.id.ServedByName);
        TextView billDate = (TextView) findViewById(R.id.DateDisplay);
        TextView netAmount = (TextView) findViewById(R.id.PaymentAmt);
        TextView totalTaxes = (TextView) findViewById(R.id.TaxAmt);
        TextView servicesCharges = (TextView) findViewById(R.id.ServicesChrgAmt);
        TextView discountAmount = (TextView) findViewById(R.id.DiscountAmt);
        TextView totalPayableAmnount = (TextView) findViewById(R.id.TotalAmt);
        Button payment_bill_details = (Button)findViewById(R.id.BillDetailsPayment);
        mBillDetailsDTOs = mDbRepository.getBillDetailsRecords();
        tableNo.setText("");
        orderNo.setText("");
        servedBy.setText(mBillDetailsDTOs.getServedByName());
        billDate.setText(mBillDetailsDTOs.getBillDate().toString());
        netAmount.setText(String.valueOf(mBillDetailsDTOs.getNetAmount()));
        totalTaxes.setText(String.valueOf(mBillDetailsDTOs.getTotalTax()));

        servicesCharges.setText("");
        discountAmount.setText("");
        totalPayableAmnount.setText(String.valueOf(mBillDetailsDTOs.getTotalPayableTaxAmt()));


        payment_bill_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(),BillPaymentOptionActivity.class);
                i.putExtra("tableCustInfo",tableCommonInfoDTO);
                startActivity(i);
                finish();

            }
        });





    }
}
