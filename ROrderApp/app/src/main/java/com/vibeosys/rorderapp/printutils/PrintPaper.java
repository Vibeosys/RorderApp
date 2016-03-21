package com.vibeosys.rorderapp.printutils;

import android.content.Context;

import com.vibeosys.rorderapp.data.PrinterDetailsDTO;

/**
 * Created by akshay on 29-02-2016.
 */
public interface PrintPaper {

    void setPrinter(Context context, PrinterDetailsDTO printerDetails);

    void openPrinter();

    void printText(PrintDataDTO printDataDTO);

    void closePrinter();
}
