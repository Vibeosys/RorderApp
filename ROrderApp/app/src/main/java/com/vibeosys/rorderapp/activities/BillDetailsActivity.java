package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vibeosys.rorderapp.data.BillDbDTO;
import com.vibeosys.rorderapp.data.BillDetailsDTO;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.util.ROrderDateUtils;

import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrinivas on 08-02-2016.
 */
public class BillDetailsActivity extends BaseActivity {

    private BillDetailsDTO mBillDetailsDTOs;
    private TableCommonInfoDTO tableCommonInfoDTO;
    private int mTableId, mTableNo;
    private String custId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_amount_payment);
        setTitle(getResources().getString(R.string.bill_details));
       /* BillDbDTO db = new BillDbDTO(1, Date.valueOf("02-02-2016"), Time.valueOf("10:10:11"),1200.00,102.00,10.00,Date.valueOf("02-02-2016"),Date.valueOf("02-02-2016"),2);

       List<BillDbDTO> bill = new ArrayList<>();
        bill.add(db);

        mDbRepository.insertBills(bill);*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Tool bar intitilization
        tableCommonInfoDTO = getIntent().getParcelableExtra("tableCustInfo");
        mTableId = tableCommonInfoDTO.getTableId();
        mTableNo = tableCommonInfoDTO.getTableNo();
        custId = tableCommonInfoDTO.getCustId();
        TextView tableNo = (TextView) findViewById(R.id.TableNumber);
        //TextView orderNo = (TextView) findViewById(R.id.OrderNumber);
        TextView servedBy = (TextView) findViewById(R.id.ServedByName);
        TextView billDate = (TextView) findViewById(R.id.DateDisplay);
        TextView netAmount = (TextView) findViewById(R.id.PaymentAmt);
        TextView totalTaxes = (TextView) findViewById(R.id.TaxAmt);
        TextView servicesCharges = (TextView) findViewById(R.id.ServicesChrgAmt);
        TextView discountAmount = (TextView) findViewById(R.id.DiscountAmt);
        TextView totalPayableAmnount = (TextView) findViewById(R.id.TotalAmt);
        Button payment_bill_details = (Button) findViewById(R.id.BillDetailsPayment);
        Button btnBillSummary = (Button) findViewById(R.id.btnBillSummary);
        mBillDetailsDTOs = mDbRepository.getBillDetailsRecords(custId);
        tableNo.setText("" + mBillDetailsDTOs.getTableNo());
        //orderNo.setText("");
        servedBy.setText(mBillDetailsDTOs.getServedByName());
        Log.d("##", "##" + mBillDetailsDTOs.getBillDate());
        java.util.Date date = new ROrderDateUtils().getFormattedDate(mBillDetailsDTOs.getBillDate());
        billDate.setText(new ROrderDateUtils().getLocalDateInReadableFormat(date));
        netAmount.setText(String.format("%.2f", mBillDetailsDTOs.getNetAmount()));
        totalTaxes.setText(String.format("%.2f", mBillDetailsDTOs.getTotalTax()));

        servicesCharges.setText("");
        discountAmount.setText("");
        totalPayableAmnount.setText(String.format("%.2f", mBillDetailsDTOs.getTotalPayableTaxAmt()));


        payment_bill_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), BillPaymentOptionActivity.class);
                i.putExtra("tableCustInfo", tableCommonInfoDTO);
                i.putExtra("BillNo", mBillDetailsDTOs.getBillNo());
                startActivity(i);
                finish();

            }
        });

        btnBillSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BillSummeryActivity.class);
                i.putExtra("tableCustInfo", tableCommonInfoDTO);
                startActivityForResult(i, 1);
            }
        });


    }
}
