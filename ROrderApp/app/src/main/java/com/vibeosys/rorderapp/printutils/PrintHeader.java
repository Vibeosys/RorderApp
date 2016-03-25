package com.vibeosys.rorderapp.printutils;

/**
 * Created by akshay on 25-03-2016.
 */
public class PrintHeader {
    private String servedBy;
    private String tableNo;
    private String time;

    public PrintHeader(String servedBy, String tableNo, String time) {
        this.servedBy = servedBy;
        this.tableNo = tableNo;
        this.time = time;
    }

    public String getServedBy() {
        return servedBy;
    }

    public void setServedBy(String servedBy) {
        this.servedBy = servedBy;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
