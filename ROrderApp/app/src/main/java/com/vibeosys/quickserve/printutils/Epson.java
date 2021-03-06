package com.vibeosys.quickserve.printutils;

import android.content.Context;
import android.widget.Toast;

import com.epson.eposprint.*;
import com.vibeosys.quickserve.data.PrinterDetailsDTO;
import com.vibeosys.quickserve.printutils.exceptions.OpenPrinterException;
import com.vibeosys.quickserve.printutils.exceptions.PrintException;

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
                    throw new OpenPrinterException("Something went wrong try again.");
                case EposException.ERR_OPEN:
                    throw new OpenPrinterException("Could not found printer, please check connection and printer power.");
                case EposException.ERR_CONNECT:
                    throw new OpenPrinterException("Could not found printer, please check connection.");
                case EposException.ERR_TIMEOUT:
                    throw new OpenPrinterException("Printer is busy try again.");
                case EposException.ERR_MEMORY:
                    throw new OpenPrinterException("Restart printer and try again.");
                case EposException.ERR_ILLEGAL:
                    throw new OpenPrinterException("Something went wrong try again.");
                case EposException.ERR_PROCESSING:
                    throw new OpenPrinterException("Could not found printer, please contact administrator.");
                case EposException.ERR_UNSUPPORTED:
                    throw new OpenPrinterException("Selected printer is not supported.");
                case EposException.ERR_OFF_LINE:
                    throw new OpenPrinterException("Printer cover is open or no paper, check the paper and cover.");
                case EposException.ERR_FAILURE:
                    throw new OpenPrinterException("Could not found printer, please Check Connection.");
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
            String check = printerDetails.getmModelName();
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

            this.mPrinter.sendData(mBuilder, SEND_TIMEOUT, status, battery);
            System.out.println("## print data successfully");

             /*catch (EposException e) {*/
            /*System.err.println("Error at printing data " + mMethod);
            throw new PrintException(e.getMessage());*/
            // }


        } catch (EposException e) {
            System.err.println("Error at method " + mMethod);
            switch (e.getErrorStatus()) {
                case EposException.ERR_PARAM:
                    throw new PrintException("Something went wrong try again.");
                case EposException.ERR_OPEN:
                    throw new PrintException("Could not connect printer, please check connection and printer power.");
                case EposException.ERR_CONNECT:
                    throw new PrintException("Could not connect to the printer, please check connection.");
                case EposException.ERR_TIMEOUT:
                    throw new PrintException("Printer is busy try again.");
                case EposException.ERR_MEMORY:
                    throw new PrintException("Restart printer and try again.");
                case EposException.ERR_ILLEGAL:
                    throw new PrintException("Something went wrong try again.");
                case EposException.ERR_PROCESSING:
                    throw new PrintException("Could not found printer, please contact administrator.");
                case EposException.ERR_UNSUPPORTED:
                    throw new PrintException("Selected printer is not supported.");
                case EposException.ERR_OFF_LINE:
                    throw new PrintException("Printer cover is open or no paper check the paper and cover.");
                case EposException.ERR_FAILURE:
                    throw new PrintException("Could not found printer, please check connection.");
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
    public void printTest(String type) throws PrintException {
        try {
            mMethod = "Builder";
            String check = printerDetails.getmModelName();
            mBuilder = new Builder(printerDetails.getmModelName(), mLanguage, mContext);

            mMethod = "addText";
            mBuilder.addTextSize(1, 2);
            mBuilder.addText(type + " Test Print\n");
            mBuilder.addTextSize(1, 1);
            mBuilder.addText("Ip Address: " + printerDetails.getmIpAddress() + " \n" +
                    "Printer name: Epson " + printerDetails.getmModelName() + "\n" +
                    "Mac Address:" + printerDetails.getmMacAddress() + "\nPowered By QuickServe(TM)\n");

            mBuilder.addCut(Builder.CUT_FEED);


            int[] status = new int[1];
            int[] battery = new int[1];

                this.mPrinter.sendData(mBuilder, SEND_TIMEOUT, status, battery);


            System.out.println("## print data successfully");

        } /*catch (EposException e) {
            e.getErrorStatus();
            Log.d("TAG","$$"+e.getErrorStatus());
            System.err.println("Error at method ");

            switch (e.getErrorStatus()) {
                case EposException.ERR_PARAM:
                    throw new PrintException("Something went wrong try again.");
                case EposException.ERR_OPEN:
                    throw new PrintException("Could not connect printer, please check connection and printer power.");
                case EposException.ERR_CONNECT:
                    throw new PrintException("Could not connect to the printer, please check connection.");
                case EposException.ERR_TIMEOUT:
                    throw new PrintException("Printer is busy try again.");
                case EposException.ERR_MEMORY:
                    throw new PrintException("Restart printer and try again.");
                case EposException.ERR_ILLEGAL:
                    throw new PrintException("Something went wrong try again.");
                case EposException.ERR_PROCESSING:
                    throw new PrintException("Could not found printer, please contact administrator.");
                case EposException.ERR_UNSUPPORTED:
                    throw new PrintException("Selected printer is not supported.");
                case EposException.ERR_OFF_LINE:
                    throw new PrintException("Printer cover is open or no paper check the paper and cover.");
                case EposException.ERR_FAILURE:
                    throw new PrintException("Could not found printer, please check connection.");
            }
        }*/
        catch (EposException e )
        {
            e.getErrorStatus();
            e.getPrinterStatus();
            e.printStackTrace();
            if(e.getErrorStatus() == EposException.ERR_OFF_LINE)
            Toast.makeText(mContext,"Please check Printer cover or paper availability",Toast.LENGTH_SHORT).show();
            if(e.getErrorStatus() ==EposException.ERR_TIMEOUT)
                Toast.makeText(mContext,"Time out for printer",Toast.LENGTH_SHORT).show();
            if(e.getErrorStatus()== EposException.ERR_CONNECT)
                Toast.makeText(mContext,"Fail to connect ot device",Toast.LENGTH_SHORT).show();
            if(e.getErrorStatus() == EposException.ERR_PARAM)
                Toast.makeText(mContext,"Invalid parameter is passed",Toast.LENGTH_SHORT).show();
            if(e.getErrorStatus() ==EposException.ERR_FAILURE)
                Toast.makeText(mContext,"Check printer settings",Toast.LENGTH_SHORT).show();
            if(e.getErrorStatus() ==EposException.ERR_UNSUPPORTED)
                Toast.makeText(mContext,"Unsupported Pinter Model name ",Toast.LENGTH_SHORT).show();
            if(e.getErrorStatus() ==EposException.ERR_PROCESSING)
                Toast.makeText(mContext,"Processing time overlap ",Toast.LENGTH_SHORT).show();
            if(e.getErrorStatus() == EposException.ERR_MEMORY)
                Toast.makeText(mContext,"Memory issue .End the unneeded applications ",Toast.LENGTH_SHORT).show();
            if(e.getErrorStatus() == EposException.ERR_OPEN)
                Toast.makeText(mContext,"Opening process failed ",Toast.LENGTH_SHORT).show();

        }finally {
            closePrinter();
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


    /*public void printTest() throws PrintException {

        android.util.Log.d("##", "## epson Printer selected");


    }*/
}
