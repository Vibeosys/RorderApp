package com.vibeosys.quickserve.printutils.exceptions;

/**
 * Created by akshay on 29-03-2016.
 */
public class PrintException extends Exception {

    String message;
    public PrintException(String str)
    {
        super(str);
        this.message=str;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
