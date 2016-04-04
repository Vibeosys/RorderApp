package com.vibeosys.rorderapp.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.epson.epsonio.DeviceInfo;
import com.epson.epsonio.Finder;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.printutils.Epson;

/**
 * Created by shrinivas on 30-03-2016.
 */
public class DiagnosticActivity extends BaseActivity {
    Button showSearchPrintBtn;
    Button samplePrintKot;
    Button samplePrintBillKot;
    Switch sampleSwitchSearchPrinter;
    DeviceInfo[] mList = null;
    int searchBtnCnt = 1;
    public static boolean flagTest = false;
    public static boolean searchPrinter_status = false;
    String GlobalIpAddress ;
    String GlobalPrinterName ;
    String GlobalMacAddress ;
    int GlobalDeviceType ;
    Context context;

    @Override
    protected String getScreenName() {
        return "DiagnosticActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnostic_layout);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.Printer_linear);

        showSearchPrintBtn = (Button) findViewById(R.id.show_search_result);
        samplePrintKot = (Button) findViewById(R.id.print_kot_diagnostic);
        samplePrintBillKot = (Button) findViewById(R.id.print_bill_diagnostic);
        sampleSwitchSearchPrinter = (Switch) findViewById(R.id.searchPrinterSwitch);
        final DiagnosticEpson searchPrinter = new DiagnosticEpson(getApplicationContext());


        sampleSwitchSearchPrinter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int returntype = searchPrinter.findPrinter();
                    if (returntype == 0) {
                        flagTest = true;
                        Toast.makeText(getApplicationContext(), "Search for Printer", Toast.LENGTH_SHORT).show();
                        showSearchPrintBtn.setVisibility(View.VISIBLE);
                        samplePrintKot.setVisibility(View.VISIBLE);
                        samplePrintBillKot.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplication(), " error while serching the Printer", Toast.LENGTH_SHORT).show();
                        searchPrinter.stopSearch();
                        flagTest = false;
                        showSearchPrintBtn.setVisibility(View.VISIBLE);


                    }

                } else {

                    showSearchPrintBtn.setVisibility(View.INVISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                    samplePrintKot.setVisibility(View.INVISIBLE);
                    samplePrintBillKot.setVisibility(View.INVISIBLE);
                    searchPrinter.stopSearch();
                    searchBtnCnt =1;
                }
            }
        });


        showSearchPrintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchBtnCnt == 1) {
                    if (flagTest == true) {
                        mList = searchPrinter.getPrinterList();
                        if (mList == null) {
                            Toast.makeText(getApplicationContext(), "Please check Printer is ON or Not", Toast.LENGTH_SHORT).show();
                            flagTest = false;
                            sampleSwitchSearchPrinter.setChecked(false);


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
                            flagTest = true;
                            samplePrintKot.setVisibility(View.VISIBLE);
                            samplePrintBillKot.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Search Print stop success", Toast.LENGTH_SHORT).show();

                        }
                    } else if (flagTest == false) {
                        Toast.makeText(getApplicationContext(), "First click on Search Print Button", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Please click the search button again", Toast.LENGTH_SHORT).show();
                }


            }
        });


        samplePrintKot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Print KOT is clicked", Toast.LENGTH_SHORT).show();



                searchPrinter.BuilderMethod(GlobalIpAddress, GlobalPrinterName, GlobalMacAddress, GlobalDeviceType,"KOT");


            }
        });
        samplePrintBillKot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPrinter.BuilderMethod(GlobalIpAddress, GlobalPrinterName, GlobalMacAddress, GlobalDeviceType, "Bill");

            }
        });
    }
}
