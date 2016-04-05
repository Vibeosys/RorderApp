package com.vibeosys.rorderapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.epson.epsonio.DeviceInfo;
import com.epson.epsonio.Finder;
import com.google.gson.Gson;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.ChefOrderCompleted;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.printutils.Epson;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.NetworkUtils;
import com.vibeosys.rorderapp.util.ServerSyncManager;

import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;

/**
 * Created by shrinivas on 30-03-2016.
 */
public class DiagnosticActivity extends BaseActivity implements ServerSyncManager.OnStringResultReceived
        ,ServerSyncManager.OnStringErrorReceived{
    Button showSearchPrintBtn;
    Button samplePrintKot;
    Button samplePrintBillKot;
    Switch sampleSwitchSearchPrinter,sampleSwitchForInternet,sampleSwitchForDataBase,sampleSwitchForAPI;
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



        sampleSwitchSearchPrinter = (Switch) findViewById(R.id.searchPrinterSwitch);
        sampleSwitchForInternet = (Switch)findViewById(R.id.searchInternatSwitch);
        sampleSwitchForDataBase = (Switch)findViewById(R.id.searchDataBaseSwitch);
        sampleSwitchForAPI = (Switch)findViewById(R.id.searchApiSwitch);
        mServerSyncManager.setOnStringResultReceived(this);


        final DiagnosticEpson searchPrinter = new DiagnosticEpson(getApplicationContext());



        sampleSwitchSearchPrinter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int returntype = searchPrinter.findPrinter();
                    if (returntype == 0) {
                        flagTest = true;
                        Intent diagnostic = new Intent(getApplicationContext(), DiagnosticKOTPrintingActivity.class);
                        startActivity(diagnostic);
                    } else if(returntype !=255){
                        Toast.makeText(getApplication(), " error while serching the Printer", Toast.LENGTH_SHORT).show();
                        searchPrinter.stopSearch();
                        flagTest = false;

                    }else if(returntype == 255)
                    {
                       // Toast.makeText(getApplication(), "This device is not connected to the network", Toast.LENGTH_SHORT).show();
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                DiagnosticActivity.this).create();

                        alertDialog.setTitle(getString(R.string.diagnostic_alert_title));

                        alertDialog.setMessage("This device is not available in the network");

                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                sampleSwitchSearchPrinter.setChecked(false);
                            }
                        });


                        alertDialog.show();
                        searchPrinter.stopSearch();
                        flagTest = false;
                    }

                } else {
                    searchPrinter.stopSearch();
                    searchBtnCnt = 1;
                }
            }
        });


        sampleSwitchForInternet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    if(NetworkUtils.isActiveNetworkAvailable(getApplicationContext()))
                    {

                        AlertDialog alertDialog = new AlertDialog.Builder(
                                DiagnosticActivity.this).create();

                        alertDialog.setTitle(getString(R.string.diagnostic_alert_title));

                        alertDialog.setMessage(getString(R.string.diagnostic_internet_available));

                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                sampleSwitchForInternet.setChecked(false);
                            }
                        });


                        alertDialog.show();
                    }
                    else {

                        AlertDialog alertDialog = new AlertDialog.Builder(
                                DiagnosticActivity.this).create();

                        alertDialog.setTitle(getString(R.string.diagnostic_alert_title));

                        alertDialog.setMessage(getString(R.string.diagnostic_internet_not_available));

                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                sampleSwitchForInternet.setChecked(false);
                            }
                        });


                        alertDialog.show();

                    }

                }
            }
        });

        sampleSwitchForDataBase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    File dataBase= getApplicationContext().getDatabasePath(mSessionManager.getDatabaseDeviceFullPath());
                            if(dataBase.exists())
                            {

                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        DiagnosticActivity.this).create();
                                alertDialog.setTitle(getString(R.string.diagnostic_alert_title));
                                alertDialog.setMessage(getString(R.string.diagnostic_data_base_available));

                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        sampleSwitchForDataBase.setChecked(false);
                                    }
                                });


                                alertDialog.show();
                            }
                            else
                            {
                                AlertDialog alertDialog = new AlertDialog.Builder(
                                        DiagnosticActivity.this).create();
                                alertDialog.setTitle(getString(R.string.diagnostic_alert_title));
                                alertDialog.setMessage(getString(R.string.diagnostic_data_base_not_available));

                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        sampleSwitchForDataBase.setChecked(false);
                                    }
                                });


                                alertDialog.show();
                            }
                }
            }
        });
        sampleSwitchForAPI.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if(NetworkUtils.isActiveNetworkAvailable(getApplicationContext()))
                    {
                        sendTabDataToServer();
                    }
                    else
                    {
                        /*Toast.makeText(getApplicationContext(),"Internet is not available",Toast.LENGTH_SHORT).show();
                        sampleSwitchForAPI.setChecked(false);*/
                        AlertDialog alertDialog = new AlertDialog.Builder(
                                DiagnosticActivity.this).create();

                        alertDialog.setTitle(getString(R.string.diagnostic_alert_title));

                        alertDialog.setMessage(getString(R.string.diagnostic_internet_not_available));

                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                sampleSwitchForAPI.setChecked(false);
                            }
                        });


                        alertDialog.show();
                    }
                }
            }
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        sampleSwitchSearchPrinter.setChecked(false);
        sampleSwitchForInternet.setChecked(false);
        sampleSwitchForDataBase.setChecked(false);
        sampleSwitchForAPI.setChecked(false);
    }
    public void sendTabDataToServer() {

        TableDataDTO tableDataDTO = new TableDataDTO();
        mServerSyncManager.uploadDataToServer(tableDataDTO);
        mServerSyncManager.syncDataWithServer(true);


    }

    @Override
    public void onStingErrorReceived(@NonNull VolleyError error) {

    }

    @Override
    public void onStingResultReceived(@NonNull JSONObject data) {
        int errorCode=-1;
        try
        {
            errorCode = data.getInt("errorCode");
            if(errorCode != 0)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        DiagnosticActivity.this).create();
                alertDialog.setTitle(getString(R.string.diagnostic_alert_title));
                alertDialog.setMessage(getString(R.string.diagnostic_api_available));

                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        sampleSwitchForAPI.setChecked(false);
                    }
                });


                alertDialog.show();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }
}
