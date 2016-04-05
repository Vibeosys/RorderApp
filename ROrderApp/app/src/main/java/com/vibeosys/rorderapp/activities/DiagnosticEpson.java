package com.vibeosys.rorderapp.activities;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.epson.*;
import com.epson.eposprint.Builder;
import com.epson.eposprint.EposException;
import com.epson.eposprint.Print;
import com.epson.epsonio.DevType;
import com.epson.epsonio.DeviceInfo;
import com.epson.epsonio.EpsonIo;
import com.epson.epsonio.EpsonIoException;
import com.epson.epsonio.FilterOption;
import com.epson.epsonio.Finder;
import com.epson.epsonio.IoStatus;
import com.vibeosys.rorderapp.data.PrinterDetailsDTO;
import com.vibeosys.rorderapp.printutils.Epson;
import com.vibeosys.rorderapp.printutils.PrintBody;
import com.vibeosys.rorderapp.printutils.PrintDataDTO;
import com.vibeosys.rorderapp.printutils.PrintHeader;
import com.vibeosys.rorderapp.printutils.PrintPaper;
import com.vibeosys.rorderapp.printutils.PrinterFactory;
import com.vibeosys.rorderapp.printutils.PrinterTypes;
import com.vibeosys.rorderapp.printutils.exceptions.OpenPrinterException;
import com.vibeosys.rorderapp.printutils.exceptions.PrintException;
import com.vibeosys.rorderapp.util.ROrderDateUtils;

import java.io.Serializable;

/**
 * Created by shrinivas on 30-03-2016.
 */
public class DiagnosticEpson {
    Context context;
    int[] status = new int[1];
    int[] battery = new int[1];
    Print print = null;
    static final int SEND_TIMEOUT = 10 * 1000;

    DiagnosticEpson(Context context) {
        this.context = context;
    }

    int errorStatus;


    public int findPrinter() {
        try {


            Finder.start(context, DevType.TCP, "255:255:255:255");
            String s = "@find Printer";
            Log.d("TAG", "EPSVAL " + s);
            errorStatus = IoStatus.SUCCESS;


        } catch (EpsonIoException e) {
            errorStatus = e.getStatus();
            e.printStackTrace();
            stopSearch();

            if (e.getStatus() == IoStatus.ERR_FAILURE)
                Log.d("TAG", "$$" + "UNSPECIFED ERROR");
            if (e.getStatus() == IoStatus.ERR_ILLEGAL)
                Log.d("TAG", "$$" + "SEARCH ALREADY IN PROGRESS");
            if (e.getStatus() == IoStatus.ERR_PROCESSING)
                Log.d("TAG", "$$" + "COULD NOT EXECUTE PROCESS");
            if (e.getStatus() == IoStatus.ERR_PARAM)
                Log.d("TAG", "$$" + "INVALID PARAMETER PASS");
            if (e.getStatus() == IoStatus.ERR_MEMORY)
                Log.d("TAG", "$$" + "COULD NOT ALLOCATE MEMOERY");
            if (e.getStatus() == IoStatus.SUCCESS)
                Log.d("TAG", "$$" + "COULD NOT ABLE TO CONNECT");
            if (e.getStatus() == IoStatus.ERR_OPEN)
                Log.d("TAG", "$$" + "PRINTER IS ALREADY OPEN");
            if (e.getStatus() == IoStatus.ERR_CONNECT)
                Log.d("TAG", "$$" + "ERROR WHILE CONNECTING");
            if (e.getStatus() == IoStatus.ERR_TIMEOUT)
                Log.d("TAG", "$$" + "TIME OUT WHILE SEARCHING");

            if (errorStatus == e.getStatus()) {
                return errorStatus;
            }
        }
        Log.d("TAG", "EPSVAL " + errorStatus);
        return errorStatus;
    }


    public DeviceInfo[] getPrinterList() {
        DeviceInfo[] mList = null;
        try {
            if (errorStatus == 0) {

                mList = Finder.getDeviceInfoList(FilterOption.PARAM_DEFAULT);
            }


        } catch (EpsonIoException e) {
            errorStatus = e.getStatus();
            e.printStackTrace();
            if (e.getStatus() == IoStatus.ERR_FAILURE)
                Log.d("TAG", "$$" + "UNSPECIFED ERROR");
            if (e.getStatus() == IoStatus.ERR_ILLEGAL)
                Log.d("TAG", "$$" + "SEARCH ALREADY IN PROGRESS");
            if (e.getStatus() == IoStatus.ERR_PROCESSING)
                Log.d("TAG", "$$" + "COULD NOT EXECUTE PROCESS");
            if (e.getStatus() == IoStatus.ERR_PARAM)
                Log.d("TAG", "$$" + "INVALID PARAMETER PASS");
            if (e.getStatus() == IoStatus.ERR_MEMORY)
                Log.d("TAG", "$$" + "COULD NOT ALLOCATE MEMOERY");
            Log.d("TAG", "EPSVAL " + e.getStatus());

        }

        return mList;
    }

    public void stopSearch() {
        try {
            if (errorStatus == 0) {
                Finder.stop();

                String s3 = "@ Stop Printer";
                Log.d("TAG", "EPSVAL " + s3);
            }

        } catch (EpsonIoException e) {
            e.printStackTrace();
            errorStatus = e.getStatus();
        }
    }


    protected void BuilderMethod(String GlobalIpAddress, String GlobalPrinterName, String GlobalMacAddress, int GlobalDeviceType ,String type) {


        PrinterDetailsDTO printerDetailsDTO = new PrinterDetailsDTO(1, GlobalIpAddress, PrinterTypes.EPSON, "TM-P20", GlobalMacAddress, "Epson");

        PrinterFactory printerFactory = new PrinterFactory();
        PrintPaper printPaper = printerFactory.getPrinter(printerDetailsDTO);
        try {
            printPaper.setPrinter(context, printerDetailsDTO);
        } catch (OpenPrinterException e) {
            /*addError(screenName, "AsyncPrintData setPrinter", e.getMessage());
            resultCount = 0;
            return e.getMessage();*/
            e.printStackTrace();
        }

        printPaper.openPrinter();

        try {
            printPaper.printTest(type);
        } catch (PrintException e) {

            e.printStackTrace();
        }

    }



}