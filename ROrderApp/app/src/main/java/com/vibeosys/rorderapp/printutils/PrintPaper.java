package com.vibeosys.rorderapp.printutils;

import android.content.Context;

import com.vibeosys.rorderapp.data.PrinterDetailsDTO;
import com.vibeosys.rorderapp.printutils.exceptions.OpenPrinterException;
import com.vibeosys.rorderapp.printutils.exceptions.PrintException;

/**
 * Created by akshay on 29-02-2016.
 */
public interface PrintPaper {

    void setPrinter(Context context, PrinterDetailsDTO printerDetails) throws OpenPrinterException;

    void openPrinter();

    void printText(PrintDataDTO printDataDTO) throws PrintException;

    void closePrinter();
}
