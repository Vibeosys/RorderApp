package com.vibeosys.rorderapp.printutils;

import android.content.Context;
import android.nfc.Tag;
import android.util.*;
import android.util.Log;

import com.epson.eposprint.*;
import com.vibeosys.rorderapp.data.OrderDetailsDTO;
import com.vibeosys.rorderapp.data.OrderMenuDTO;
import com.vibeosys.rorderapp.data.PrinterDetailsDTO;
import com.vibeosys.rorderapp.printutils.exceptions.OpenPrinterException;
import com.vibeosys.rorderapp.printutils.exceptions.PrintException;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by akshay on 29-02-2016.
 */
public class Epson implements PrintPaper, StatusChangeEventListener, BatteryStatusChangeEventListener {

    private int mDeviceType = Print.DEVTYPE_TCP;
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
    public void setPrinter(Context context, PrinterDetailsDTO printerDetails) throws OpenPrinterException {
        this.mContext = context;
        this.printerDetails = printerDetails;
        this.mPrinter = new Print(mContext);
        try {
            this.mPrinter.openPrinter(mDeviceType, printerDetails.getmIpAddress(), Print.FALSE, mInterval);
        } catch (EposException e) {
            e.printStackTrace();
            switch (e.getErrorStatus()) {
                case EposException.ERR_PARAM:
                    throw new OpenPrinterException("Could not found printer please Check Connection");
                case EposException.ERR_OPEN:
                    throw new OpenPrinterException("Could not found printer please Check Connection");
                case EposException.ERR_CONNECT:
                    throw new OpenPrinterException("Could not found printer please Check Connection");
                case EposException.ERR_TIMEOUT:
                    throw new OpenPrinterException("Could not found printer please restart printer");
                case EposException.ERR_MEMORY:
                    throw new OpenPrinterException("Could not found printer please Check Connection");
                case EposException.ERR_ILLEGAL:
                    throw new OpenPrinterException("Could not found printer please contact administrator");
                case EposException.ERR_PROCESSING:
                    throw new OpenPrinterException("Could not found printer please contact administrator");
                case EposException.ERR_UNSUPPORTED:
                    throw new OpenPrinterException("Selected printer is not supported");
                case EposException.ERR_OFF_LINE:
                    throw new OpenPrinterException("Printer is offline please Check Connection");
                case EposException.ERR_FAILURE:
                    throw new OpenPrinterException("Could not found printer please Check Connection");
            }
            //throw new OpenPrinterException("Could not found printer");
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
    public void printText(PrintDataDTO printDataDTO) throws PrintException {

        android.util.Log.d("##", "## epson Printer selected");
        try {
            mMethod = "Builder";
            mBuilder = new Builder(printerDetails.getmModelName(), mLanguage, mContext);

            mMethod = "addText";
            //mBuilder.addText(printDataDTO.getBody().getMenus().toString());
            //mBuilder.addText(printDataDTO.getHeader());
            //mBuilder.addFeedLine(Builder.LINE_MEDIUM);
           /* String strPrint=printDataDTO.getPrint(45,3,5);
           Log.d("##","##"+strPrint);
           // mBuilder.addTextAlign(Builder.ALIGN_LEFT);*//*
            mBuilder.addTextLineSpace(Builder.LINE_THIN_DOUBLE);
            mBuilder.addText(strPrint);*/
            mBuilder = printDataDTO.getPrint(mBuilder, 47, 1, 3);
            /*mBuilder.addFeedLine(Builder.LINE_MEDIUM_DOUBLE);*/
            mBuilder.addCut(Builder.CUT_FEED);

           /* for( int i =0;i<= menusHashMap.size();i++)
            {

            }*/
            /*Log.d("##", "##" + printDataDTO.getBody().getMenus().toString());*/
            int[] status = new int[1];
            int[] battery = new int[1];
            try {
                this.mPrinter.sendData(mBuilder, SEND_TIMEOUT, status, battery);
                System.out.println("## print data successfully");

            } catch (EposException e) {
                System.err.println("Error at printing data " + mMethod);
                throw new PrintException(e.getMessage());
            }


        } catch (EposException e) {
            System.err.println("Error at method " + mMethod);
            switch (e.getErrorStatus()) {
                case EposException.ERR_PARAM:
                    throw new PrintException("Could not connect printer please Check Connection");
                case EposException.ERR_OPEN:
                    throw new PrintException("Could not connect printer please Check Connection");
                case EposException.ERR_CONNECT:
                    throw new PrintException("Could not connect printer please Check Connection");
                case EposException.ERR_TIMEOUT:
                    throw new PrintException("Could not connect printer please restart printer");
                case EposException.ERR_MEMORY:
                    throw new PrintException("Could not print the data low memory space");
                case EposException.ERR_ILLEGAL:
                    throw new PrintException("Could not connect printer please contact administrator");
                case EposException.ERR_PROCESSING:
                    throw new PrintException("Could not connect printer please contact administrator");
                case EposException.ERR_UNSUPPORTED:
                    throw new PrintException("Selected printer is not supported");
                case EposException.ERR_OFF_LINE:
                    throw new PrintException("Printer is offline please Check Connection");
                case EposException.ERR_FAILURE:
                    throw new PrintException("Could not connect printer please Check Connection");
            }
        } finally {
            closePrinter();
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
