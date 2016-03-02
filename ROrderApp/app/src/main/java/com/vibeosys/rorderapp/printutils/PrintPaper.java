package com.vibeosys.rorderapp.printutils;

import android.content.Context;

/**
 * Created by akshay on 29-02-2016.
 */
public interface PrintPaper {

    void setPrinter(Context context);

    void openPrinter();

    void printText(PrintDataDTO printDataDTO);

    void closePrinter();
}
