package com.vibeosys.quickserve.printutils;

import android.text.TextUtils;

import com.vibeosys.quickserve.data.PrinterDetailsDTO;

/**
 * Created by akshay on 29-02-2016.
 */
public class PrinterFactory {

    public PrintPaper getPrinter(PrinterDetailsDTO printerDetails) {
        String printerName = printerDetails.getmPrinterName();
        if (TextUtils.isEmpty(printerName)) {
            return null;
        }
        if (printerName.equals(PrinterTypes.EPSON)) {
            return new Epson();
        }
        if (printerName.equals(PrinterTypes.DELL)) {
            return new Dell();
        }
        return null;
    }
}
