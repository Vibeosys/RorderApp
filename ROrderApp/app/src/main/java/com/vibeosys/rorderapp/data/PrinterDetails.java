package com.vibeosys.rorderapp.data;

/**
 * Created by shrinivas on 19-03-2016.
 */
public class PrinterDetails {
    int mPrinterId;
    String mIpAddress;
    String mPrinterName;
    String mModelName;
    String mMacAddress;
    String mCompany;

   public PrinterDetails(int mPrinterId,String mIpAddress,String mPrinterName,String mModelName,String mMacAddress,String mCompany)
    {
        this.mPrinterId = mPrinterId;
        this.mIpAddress =mIpAddress;
        this.mPrinterName= mPrinterName;
        this.mModelName = mModelName;
        this.mMacAddress = mMacAddress;
        this.mCompany = mCompany;
    }
    public int getmPrinterId() {
        return mPrinterId;
    }

    public void setmPrinterId(int mPrinterId) {
        this.mPrinterId = mPrinterId;
    }

    public String getmIpAddress() {
        return mIpAddress;
    }

    public void setmIpAddress(String mIpAddress) {
        this.mIpAddress = mIpAddress;
    }

    public String getmPrinterName() {
        return mPrinterName;
    }

    public void setmPrinterName(String mPrinterName) {
        this.mPrinterName = mPrinterName;
    }

    public String getmModelName() {
        return mModelName;
    }

    public void setmModelName(String mModelName) {
        this.mModelName = mModelName;
    }

    public String getmMacAddress() {
        return mMacAddress;
    }

    public void setmMacAddress(String mMacAddress) {
        this.mMacAddress = mMacAddress;
    }

    public String getmCompany() {
        return mCompany;
    }

    public void setmCompany(String mCompany) {
        this.mCompany = mCompany;
    }



}
