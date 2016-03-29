package com.vibeosys.rorderapp.printutils.exceptions;

/**
 * Created by akshay on 29-03-2016.
 */
public class OpenPrinterException extends Exception {

    String message;
    public OpenPrinterException(String strException) {
        super(strException);
        this.message=strException;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
