package com.vibeosys.rorderapp.activities;


import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.epson.*;
import com.epson.eposprint.Builder;
import com.epson.eposprint.EposException;
import com.epson.easyselect.*;
import com.epson.eposprint.Print;
import com.epson.epsonio.DeviceInfo;
import com.epson.epsonio.EpsonIoException;
import com.epson.epsonio.Finder;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.PrinterDetailsDTO;
import com.vibeosys.rorderapp.printutils.Epson;
import com.vibeosys.rorderapp.printutils.PrintDataDTO;
import com.vibeosys.rorderapp.printutils.exceptions.PrintException;


/**
 * Created by shrinivas on 01-04-2016.
 */
public class DiagnosticKOTPrintingActivity extends BaseActivity {
    Button SerachPrinter;
    Button PrinterKOT, PrintBill;
    DeviceInfo[] mList = null;
    String GlobalIpAddress ;
    String GlobalPrinterName ;
    String GlobalMacAddress ;
    int GlobalDeviceType ;
    int searchBtnCnt = 1;
    int errorStatus;
    @Override
    protected String getScreenName() {
        return "DiagnosticKOTPrintingActivity";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnostic_printing_buttons);
        SerachPrinter = (Button)findViewById(R.id.show_search_result);
        PrinterKOT = (Button)findViewById(R.id.print_kot_diagnostic);
        PrintBill =(Button)findViewById(R.id.print_bill_diagnostic);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Printer_linear);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

      final  DiagnosticEpson searchPrinter = new DiagnosticEpson(getApplicationContext());
        SerachPrinter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(searchBtnCnt == 1)
                {
                    mList = searchPrinter.getPrinterList();
                    if (mList == null) {
                        Toast.makeText(getApplicationContext(), "Please check Printer is on or not and restart your search", Toast.LENGTH_SHORT).show();



                    } else if ((mList.length) >= 0) {
                        for (int i = 0; i < mList.length; i++) {
                            linearLayout.setVisibility(View.VISIBLE);
                            String IpAddress = mList[0].getIpAddress();
                            String PrinterName = mList[0].getPrinterName();
                            String MacAddress = mList[0].getMacAddress();
                            int DeviceType = mList[0].getDeviceType();
                            GlobalIpAddress =IpAddress ;
                            GlobalPrinterName =PrinterName;
                            GlobalMacAddress =MacAddress ;
                            GlobalDeviceType =DeviceType;
                            TextView tv1 = new TextView(getApplicationContext());
                            tv1.setText("IP ADDRESS :" + IpAddress);
                            linearLayout.removeAllViews();
                            linearLayout.addView(tv1);
                            TextView tv2 = new TextView(getApplicationContext());
                            tv2.setText("PRINTER NAME :" + PrinterName);
                            linearLayout.addView(tv2);
                            TextView tv3 = new TextView(getApplicationContext());
                            tv3.setText("MAC ADDRESS : " + MacAddress);
                            linearLayout.addView(tv3);
                            TextView tv4 = new TextView(getApplicationContext());
                            tv4.setText("DEVICE TYPE : " + DeviceType);
                            linearLayout.addView(tv4);



                        }
                        searchPrinter.stopSearch();
                        searchBtnCnt++;
                        Toast.makeText(getApplicationContext(), "Search Print stop success", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Search result is displayed", Toast.LENGTH_SHORT).show();
                }

            }


        });
        PrinterKOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchPrinter.BuilderMethod(GlobalIpAddress, GlobalPrinterName, GlobalMacAddress, GlobalDeviceType, "KOT");
            }
        });
        PrintBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                searchPrinter.BuilderMethod(GlobalIpAddress, GlobalPrinterName, GlobalMacAddress, GlobalDeviceType, "Bill");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {
            Finder.stop();
        } catch (EpsonIoException e) {
            e.printStackTrace();
            errorStatus = e.getStatus();
        }
    }



}

