package com.vibeosys.rorderapp.util;

import android.os.Build;

import com.vibeosys.rorderapp.data.BaseDTO;

/**
 * Created by anand on 19-11-2015.
 */
public class DeviceBuildInfo extends BaseDTO {
    private int fmVersion;
    private String product;
    private String manufacturer;
    private String model;
    private String brand;
    private String board;

    public static DeviceBuildInfo GetDeviceInfo() {
        DeviceBuildInfo buildInfo = new DeviceBuildInfo();
        buildInfo.setFmVersion(Build.VERSION.SDK_INT);
        buildInfo.setProduct(Build.PRODUCT);
        buildInfo.setModel(Build.MODEL);
        buildInfo.setBoard(Build.BOARD);
        buildInfo.setManufacturer(Build.MANUFACTURER);
        buildInfo.setBrand(Build.BRAND);
        return buildInfo;
    }

    public int getFmVersion() {
        return fmVersion;
    }

    public void setFmVersion(int fmVersion) {
        this.fmVersion = fmVersion;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

   /* public String[] getSupportedApi() {
        return supportedApis;
    }

    public void setSupportedApi(String[] supportedApis) {
        this.supportedApis = supportedApis;
    }*/
}
