package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 13-02-2016.
 */
public class BillPaidUpload {
    private int billNo;
    private int isPayed;
    private int payedBy;

    public BillPaidUpload(int billNo, int isPayed, int payedBy) {
        this.billNo = billNo;
        this.isPayed = isPayed;
        this.payedBy = payedBy;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public int getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(int isPayed) {
        this.isPayed = isPayed;
    }

    public int getPayedBy() {
        return payedBy;
    }

    public void setPayedBy(int payedBy) {
        this.payedBy = payedBy;
    }
}
