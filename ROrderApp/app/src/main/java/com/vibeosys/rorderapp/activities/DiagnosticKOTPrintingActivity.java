package com.vibeosys.rorderapp.activities;


import android.content.Context;

import android.util.Log;

import com.epson.*;
import com.epson.eposprint.Builder;
import com.epson.eposprint.EposException;
import com.epson.easyselect.*;
import com.epson.eposprint.Print;
import com.vibeosys.rorderapp.data.PrinterDetailsDTO;
import com.vibeosys.rorderapp.printutils.Epson;
import com.vibeosys.rorderapp.printutils.PrintDataDTO;
import com.vibeosys.rorderapp.printutils.exceptions.PrintException;


/**
 * Created by shrinivas on 01-04-2016.
 */
public class DiagnosticKOTPrintingActivity {

    String GlobalIpAddress;
    String GlobalPrinterName;
    String GlobalMacAddress;
    Context context;
    int lag = Builder.MODEL_ANK;
    int[] status = new int[1];



    DiagnosticKOTPrintingActivity (String GlobalIpAddress,String GlobalPrinterName,String GlobalMacAddress,Context context)
    {
        this.GlobalIpAddress = GlobalIpAddress;
        this.GlobalPrinterName = GlobalPrinterName;
        this.GlobalMacAddress =GlobalMacAddress;
        this.context = context;
    }

    protected void BuilderMethod() {

        status[0] =0;



            try
            {
                Builder  Testbuilder = new Builder("TM-T82",Builder.MODEL_ANK,context);
                Testbuilder.addFeedPosition(30);
                Testbuilder.addText("Servred by ,\n");
                Testbuilder.addFeedPosition(20);
                Testbuilder.addText("Table number");


             }catch (EposException e)
            {
                int error = e.getErrorStatus();
                e.getPrinterStatus();
                if(e.getErrorStatus() == EposException.ERR_FAILURE)
                    Log.d("TAG", "$$" + "UNSPECIFED ERROR");
                if(e.getErrorStatus() == EposException.ERR_ILLEGAL)
                    Log.d("TAG","$$"+"SEARCH ALREADY IN PROGRESS");
                if(e.getErrorStatus() == EposException.ERR_PROCESSING)
                    Log.d("TAG","$$"+"COULD NOT EXECUTE PROCESS");
                if(e.getErrorStatus() == EposException.ERR_PARAM)
                    Log.d("TAG","$$"+"INVALID PARAMETER PASS");
                if(e.getErrorStatus() == EposException.ERR_MEMORY)
                    Log.d("TAG","$$"+"COULD NOT ALLOCATE MEMOERY");
                if(e.getErrorStatus() == EposException.SUCCESS)
                    Log.d("TAG","$$"+"COULD NOT ABLE TO CONNECT");
                if(e.getErrorStatus() == EposException.ERR_OPEN)
                    Log.d("TAG","$$"+"PRINTER IS ALREADY OPEN");
                if(e.getErrorStatus()== EposException.ERR_CONNECT)
                    Log.d("TAG" ,"$$"+"ERROR WHILE CONNECTING");
                if(e.getErrorStatus() == EposException.ERR_TIMEOUT)
                    Log.d("TAG","$$"+"TIME OUT WHILE SEARCHING");
                if(e.getErrorStatus() == EposException.ERR_OFF_LINE)
                    Log.d("TAG","$$"+"OFF LINE EXCEPTION");
               if(e.getErrorStatus()== EposException.ERR_UNSUPPORTED)
                   Log.d("TAG","$$"+"UNSUPPORTED MODEL NUMBER OR LANGAUAGE");




            }





        }

    }

