package com.vibeosys.quickserve.printutils;

import android.content.Context;
import android.util.Log;

import com.vibeosys.quickserve.data.PrinterDetailsDTO;
import com.vibeosys.quickserve.printutils.exceptions.PrintException;

/**
 * Created by akshay on 21-03-2016.
 */
public class Dell implements PrintPaper {
    @Override
    public void setPrinter(Context context, PrinterDetailsDTO printerDetails) {

    }

    @Override
    public void openPrinter() {

    }

    @Override
    public void printText(PrintDataDTO printDataDTO) {
        Log.d("##", "## dell Printer selected");

    }

    @Override
    public void closePrinter() {

    }

    @Override
    public void printTest(String type) throws PrintException {

    }
}
