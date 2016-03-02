package com.vibeosys.rorderapp.printutils;

/**
 * Created by akshay on 29-02-2016.
 */
public class PrinterFactory {

    public PrintPaper getPrinter(String printerType) {
        if (printerType == null) {
            return null;
        }
        if (printerType == PrinterTypes.EPSON_TM_P20) {
            return new EpsonTMP20();
        }
        return null;
    }
}
