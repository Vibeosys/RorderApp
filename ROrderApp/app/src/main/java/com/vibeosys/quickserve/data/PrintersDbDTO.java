package com.vibeosys.quickserve.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 18-03-2016.
 */
public class PrintersDbDTO extends BaseDTO {

    private int printerId;
    private String ipAddress;
    private String printerName;
    private String modelName;
    private String company;
    private String macAddress;
    private int active;

    public int getPrinterId() {
        return printerId;
    }

    public void setPrinterId(int printerId) {
        this.printerId = printerId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPrinterName() {
        return printerName;
    }

    public void setPrinterName(String printerName) {
        this.printerName = printerName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public static List<PrintersDbDTO> deserializePrinters(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<PrintersDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                PrintersDbDTO deserializeObject = gson.fromJson(serializedString, PrintersDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
