package com.vibeosys.rorderapp.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vibeosys.rorderapp.data.BillDetailsDTO;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.TableCommonInfoDTO;
import com.vibeosys.rorderapp.util.ROrderDateUtils;

import java.sql.Time;
import java.util.Date;

/**
 * Created by shrinivas on 08-02-2016.
 */
public class BillDetailsActivity extends BaseActivity {

    private BillDetailsDTO mBillDetailsDTOs;
    private TableCommonInfoDTO tableCommonInfoDTO;
    private int mTableId, mTableNo;
    private String custId;
    private TextView mTxtTableNo;
    private TextView mTxtServedBy;
    private TextView mTxtBillDate;
    private TextView mTxtNetAmount;
    private TextView mTxtTotalTaxes;
    //private TextView mTxtServicesCharges;
    private TextView mTxtDiscountAmount;
    private TextView mTxtTotalPayableAmnount;
    private double mDiscount = 0.00;

    @Override
    protected String getScreenName() {
        return "Bill Details";
    }

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


        mTxtTableNo = (TextView) findViewById(R.id.TableNumber);
        //TextView orderNo = (TextView) findViewById(R.id.OrderNumber);
        mTxtServedBy = (TextView) findViewById(R.id.ServedByName);
        mTxtBillDate = (TextView) findViewById(R.id.DateDisplay);
        mTxtNetAmount = (TextView) findViewById(R.id.PaymentAmt);
        mTxtTotalTaxes = (TextView) findViewById(R.id.TaxAmt);
        //mTxtServicesCharges = (TextView) findViewById(R.id.ServicesChrgAmt);
        mTxtDiscountAmount = (TextView) findViewById(R.id.DiscountAmt);
        mTxtTotalPayableAmnount = (TextView) findViewById(R.id.TotalAmt);
        Button payment_bill_details = (Button) findViewById(R.id.BillDetailsPayment);
        Button btnBillSummary = (Button) findViewById(R.id.btnBillSummary);
        LinearLayout mLayoutAddDiscount = (LinearLayout) findViewById(R.id.layout_discount_per);

        mBillDetailsDTOs = mDbRepository.getBillDetailsRecords(custId);

        displayData(0);

        payment_bill_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEventToGoogle("Action", "Payment Option selection");
                Intent i = new Intent(getApplicationContext(), BillPaymentOptionActivity.class);
                i.putExtra("tableCustInfo", tableCommonInfoDTO);
                i.putExtra("BillNo", mBillDetailsDTOs.getBillNo());
                i.putExtra("DiscountAmt", mDiscount);
                startActivity(i);
                finish();

            }
        });

        btnBillSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEventToGoogle("Action", "View Bill Summary");
                Intent i = new Intent(getApplicationContext(), BillSummeryActivity.class);
                i.putExtra("tableCustInfo", tableCommonInfoDTO);
                startActivityForResult(i, 1);
            }
        });
        mLayoutAddDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiscountDialog();
            }
        });

    }

    private void showDiscountDialog() {
        final Dialog dialog = new Dialog(BillDetailsActivity.this);
        //double percentage = 0;
        dialog.setContentView(R.layout.dialog_add_discount);
        final EditText txtPer = (EditText) dialog.findViewById(R.id.txtDiscountPer);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);
        TextView txtDiscount = (TextView) dialog.findViewById(R.id.txtDiscount);

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txtDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                double percentage = 0;
                String strPercentage = txtPer.getText().toString();
                try {
                    percentage = Double.parseDouble(strPercentage);
                    displayData(percentage);
                } catch (NumberFormatException e) {
                    Log.d(TAG, "## error in enter discount percentage");
                }
            }
        });
        dialog.show();
    }

    private void displayData(double percenatge) {

        double netAmount = mBillDetailsDTOs.getNetAmount();
        double totalPaybleAmount = mBillDetailsDTOs.getTotalPayableAmt();

        ROrderDateUtils dateUtils = new ROrderDateUtils();
        if (percenatge != 0) {
            mDiscount = Math.round((netAmount * percenatge) / 100);
            totalPaybleAmount = Math.round(totalPaybleAmount - mDiscount);
        }
        mTxtTableNo.setText(" # " + mBillDetailsDTOs.getTableNo());
        //orderNo.setText("");
        mTxtServedBy.setText(mBillDetailsDTOs.getServedByName());
        Log.d("##", "##" + mBillDetailsDTOs.getBillDate());
        java.util.Date date = new ROrderDateUtils().getFormattedDate(mBillDetailsDTOs.getBillDate());
        java.util.Date time = new ROrderDateUtils().getFormattedDate(mBillDetailsDTOs.getBillTime());
        long lngTime = time.getTime();

        long billTime = lngTime + dateUtils.getTimeOffsetAsPerLocal(5, 30);
        mTxtBillDate.setText(dateUtils.getLocalDateInReadableFormat(date) + " at " +
                dateUtils.getLocalTimeInReadableFormat(new Date(billTime)));

        mTxtNetAmount.setText(String.format("%.2f", netAmount));
        mTxtTotalTaxes.setText(String.format("%.2f", mBillDetailsDTOs.getTotalTax()));
        //mTxtServicesCharges.setText(String.format("%.2f", mDiscount));
        mTxtDiscountAmount.setText(String.format("%.2f", mDiscount));
        mTxtTotalPayableAmnount.setText(String.format("%.2f", totalPaybleAmount));
    }

}
