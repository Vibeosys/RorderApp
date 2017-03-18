package com.vibeosys.quickserve.printutils;

import android.content.Context;

import com.vibeosys.quickserve.data.PrinterDetailsDTO;
import com.vibeosys.quickserve.printutils.exceptions.OpenPrinterException;
import com.vibeosys.quickserve.printutils.exceptions.PrintException;

/**
 * Created by akshay on 29-02-2016.
 */
public interface PrintPaper {

    void setPrinter(Context context, PrinterDetailsDTO printerDetails) throws OpenPrinterException;

    void openPrinter();

    void printText(PrintDataDTO printDataDTO) throws PrintException;

    void closePrinter();

    void printTest(String type) throws PrintException;
}
