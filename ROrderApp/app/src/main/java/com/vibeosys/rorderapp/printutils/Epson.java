package com.vibeosys.rorderapp.printutils;

import android.content.Context;
import android.util.*;

import com.epson.eposprint.*;
import com.vibeosys.rorderapp.data.PrinterDetailsDTO;

/**
 * Created by akshay on 29-02-2016.
 */
public class Epson implements PrintPaper, StatusChangeEventListener, BatteryStatusChangeEventListener {

    private int mDeviceType = Print.DEVTYPE_BLUETOOTH;
    PrinterDetailsDTO printerDetails;
    /* private String mIpAddress = "00:01:90:C2:A8:4F";
     private String mPrinterName = "TM-P20";*/
    private int mLanguage = Builder.MODEL_ANK;
    private int mEnabled = Print.TRUE;
    private int mInterval = 1000;
    private Context mContext;
    private Print mPrinter = null;
    private Builder mBuilder = null;
    private String mMethod = "";
    static final int SEND_TIMEOUT = 10 * 1000;
    static final int SIZEWIDTH_MAX = 8;
    static final int SIZEHEIGHT_MAX = 8;

    @Override
    public void setPrinter(Context context, PrinterDetailsDTO printerDetails) {
        this.mContext = context;
        this.printerDetails = printerDetails;
        this.mPrinter = new Print(mContext);
        try {
            this.mPrinter.openPrinter(mDeviceType, printerDetails.getmIpAddress(), Print.FALSE, mInterval);
        } catch (EposException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openPrinter() {
        try {
            //this.mPrinter.openPrinter(mDeviceType, mIpAddress, Print.FALSE, mInterval);
            if (this.mPrinter != null) {
                this.mPrinter.setStatusChangeEventCallback(this);
                this.mPrinter.setBatteryStatusChangeEventCallback(this);
            }
            System.out.println("##printer  open successfully");
        } catch (Exception e) {
            this.mPrinter = null;
            System.err.println("## error at open printer " + e.toString());
            e.printStackTrace();
            return;
        }

    }

    @Override
    public void printText(PrintDataDTO printDataDTO) {

        android.util.Log.d("##", "## epson Printer selected");
        try {
            mMethod = "Builder";
            mBuilder = new Builder(printerDetails.getmModelName(), mLanguage, mContext);

            mMethod = "addText";
            mBuilder.addText(printDataDTO.getBody().getMenus().toString());

            int[] status = new int[1];
            int[] battery = new int[1];
            try {
                this.mPrinter.sendData(mBuilder, SEND_TIMEOUT, status, battery);
                System.out.println("## print data successfully");

            } catch (EposException e) {
                System.err.println("Error at printing data " + mMethod);
            }


        } catch (EposException e) {
            System.err.println("Error at method " + mMethod);
        } finally {
            // closePrinter();
        }

    }

    @Override
    public void closePrinter() {
        try {
            this.mPrinter.closePrinter();
            this.mPrinter = null;
            System.out.println("##printer  close successfully");
        } catch (EposException e) {
            this.mPrinter = null;
            System.out.println("##printer  close Error");
        }
    }

    @Override
    public void onBatteryStatusChangeEvent(String s, int i) {
        System.out.println("## Battery changed event" + s + " Status " + i);
    }

    @Override
    public void onStatusChangeEvent(String s, int i) {
        System.out.println("## Status changed event" + s + " Status " + i);
    }
}
