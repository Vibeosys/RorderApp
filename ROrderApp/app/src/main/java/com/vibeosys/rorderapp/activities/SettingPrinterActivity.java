package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.printutils.PrinterStatus;
import com.vibeosys.rorderapp.util.IPAddressValidator;

/**
 * Created by akshay on 02-03-2016.
 */
public class SettingPrinterActivity extends BaseActivity {


    private Switch mPrintKot;
    private Switch mPrintBill;
    private TextView mTxtBillIp;
    private TextView mTxtKotIp;
    private String mStatusKot, mStatusBill, mIpKot, mIpBill;
    private PrinterStatus mPrinterStatus;
    private IPAddressValidator mIpValidator;
    private String kotChangedIp = null, billChangedIp = null;
    private boolean mKotFlag = false;
    private boolean mBillFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle(getResources().getString(R.string.action_settings));
        mPrintKot = (Switch) findViewById(R.id.printKOT);
        mPrintBill = (Switch) findViewById(R.id.printBill);
        mTxtBillIp = (TextView) findViewById(R.id.txtIpBill);
        mTxtKotIp = (TextView) findViewById(R.id.txtIpKOT);
        mIpValidator = new IPAddressValidator();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeData();
        mPrintKot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mKotFlag = isChecked;
                if (isChecked) {
                    mTxtKotIp.setVisibility(View.VISIBLE);
                    mSessionManager.setKotPrinterStatus(mPrinterStatus.ON);
                } else {
                    mTxtKotIp.setVisibility(View.INVISIBLE);
                    mTxtKotIp.setText("");
                    mSessionManager.setKotPrinterStatus(mPrinterStatus.OFF);
                }

            }
        });

        mPrintBill.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mBillFlag = isChecked;
                if (isChecked) {
                    mTxtBillIp.setVisibility(View.VISIBLE);
                    mSessionManager.setBillPrinterStatus(mPrinterStatus.ON);
                } else {
                    mTxtBillIp.setVisibility(View.INVISIBLE);
                    mTxtBillIp.setText("");
                    mSessionManager.setBillPrinterStatus(mPrinterStatus.OFF);
                }
            }
        });

        mTxtKotIp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //mIpKot = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                kotChangedIp = s.toString();

                  /*  if (mIpValidator.validate(kotChangedIp)) {
                        mSessionManager.setKotPrinterIp(kotChangedIp);
                    } else {
                        //customAlterDialog("Invalid Ip", "Please Enter the valid Ip address");
                    }*/


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTxtBillIp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                billChangedIp = s.toString();
/*
                    if (mIpValidator.validate(billChangedIp)) {
                        mSessionManager.setBillPrinterIp(billChangedIp);
                    } else {
                        //customAlterDialog("Invalid Ip", "Please Enter the valid Ip address");
                    }*/

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initializeData() {
        mStatusKot = mSessionManager.getKotPrinterStatus();
        mStatusBill = mSessionManager.getBillPrinterStatus();
        mIpKot = mSessionManager.getKotPrinterIp();
        mIpBill = mSessionManager.getBillPrinterIp();

        if (mStatusKot.equals(mPrinterStatus.ON)) {
            mPrintKot.setChecked(true);
            mTxtKotIp.setVisibility(View.VISIBLE);
            if (!mIpKot.equals("null") || mIpKot != null || !mIpKot.isEmpty())
                mTxtKotIp.setText(mIpKot);
            else
                mTxtKotIp.setText("");
        } else {
            mPrintKot.setChecked(false);
            mTxtKotIp.setVisibility(View.INVISIBLE);
            mTxtKotIp.setText("");
        }

        if (mStatusBill.equals(mPrinterStatus.ON)) {
            mPrintBill.setChecked(true);
            mTxtBillIp.setVisibility(View.VISIBLE);
            if (!mIpBill.equals("null") || mIpBill != null || !mIpBill.isEmpty())
                mTxtBillIp.setText(mIpBill);
            else
                mTxtBillIp.setText("");
        } else {
            mPrintBill.setChecked(false);
            mTxtBillIp.setVisibility(View.INVISIBLE);
            mTxtBillIp.setText("");
        }
    }

    @Override
    protected String getScreenName() {
        return "Printer Setting";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            saveDataAndClose();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //
        saveDataAndClose();
    }

    private void saveDataAndClose() {
        if (mBillFlag) {
            if (billChangedIp != null && !billChangedIp.equals("null") && !billChangedIp.isEmpty()) {
                if (!mIpValidator.validate(billChangedIp)) {
                    mSessionManager.setBillPrinterIp("");
                    customAlterDialog("Invalid Ip", "Please Enter the valid Ip address of Bill Printer");
                } else {
                    mSessionManager.setBillPrinterIp(billChangedIp);
                    super.onBackPressed();
                }

            } else {
                customAlterDialog("Invalid Ip", "Please Enter The Ip Address Of KOT Printer");
            }
        } else {
            super.onBackPressed();
        }
        if (mKotFlag) {
            if (kotChangedIp != null && !kotChangedIp.equals("null") && !kotChangedIp.isEmpty()) {
                if (!mIpValidator.validate(kotChangedIp)) {
                    mSessionManager.setKotPrinterIp("");
                    customAlterDialog("Invalid IP", "Please Enter the valid Ip address of KOT Printer");
                } else {
                    mSessionManager.setKotPrinterIp(kotChangedIp);
                    super.onBackPressed();
                }
            } else {
                customAlterDialog("Invalid Ip", "Please Enter The Ip Address Of KOT Printer");
            }
        } else {
            super.onBackPressed();
        }

    }
}
