package com.vibeosys.quickserve.activities;

import android.content.Context;
import android.util.Log;

import com.epson.eposprint.Print;
import com.epson.epsonio.DevType;
import com.epson.epsonio.DeviceInfo;
import com.epson.epsonio.EpsonIoException;
import com.epson.epsonio.FilterOption;
import com.epson.epsonio.Finder;
import com.epson.epsonio.IoStatus;
import com.vibeosys.quickserve.data.PrinterDetailsDTO;
import com.vibeosys.quickserve.printutils.PrintPaper;
import com.vibeosys.quickserve.printutils.PrinterFactory;
import com.vibeosys.quickserve.printutils.PrinterTypes;
import com.vibeosys.quickserve.printutils.exceptions.OpenPrinterException;
import com.vibeosys.quickserve.printutils.exceptions.PrintException;

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
