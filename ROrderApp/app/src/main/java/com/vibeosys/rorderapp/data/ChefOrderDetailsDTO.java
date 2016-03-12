package com.vibeosys.rorderapp.data;

import com.vibeosys.rorderapp.util.ChefDateUtil;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by shrinivas on 11-02-2016.
 */
public class ChefOrderDetailsDTO {


    private int mChefOrderId;
    private int mUserId;
    private String mNewOrderId;
    private String mUserName;
    private int mTableNo;
    private int mNewOrderStatus;
    private int mOrderNumner;
    private String mOrderDate;
    private String mOrderTime;
    private Time orderTm;
    private Date orderDt;
    private int mTakeAwayNumber;
    private int mOrderType;
    /* private Date mplacedOrderDate;
     private Time morderTime;*/
    private ArrayList<ChefMenuDetailsDTO> mMenuChild;


    private boolean mOrderStatus;

    public ChefOrderDetailsDTO(String mNewOrderId, int mTableNo, String mUserName, int mOrderNumner,
                               int mNewOrderStatus, String orderDate, String orderTime ,int mTakeAwayNumber ,int mOrderType ) {
        this.mNewOrderId = mNewOrderId;
        this.mTableNo = mTableNo;
        this.mUserName = mUserName;
        this.mOrderNumner = mOrderNumner;
        this.mNewOrderStatus = mNewOrderStatus;
        this.mOrderDate = orderDate;
        this.mOrderTime = orderTime;
        this.mTakeAwayNumber = mTakeAwayNumber;
        this.mOrderType = mOrderType;

    }

    public int getmChefOrderId() {
        return mChefOrderId;
    }

    public void setmChefOrderId(int mChefOrderId) {
        this.mChefOrderId = mChefOrderId;
    }

    public int getmUserId() {
        return mUserId;
    }

    public void setmUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public int getmTableNo() {
        return mTableNo;
    }

    public void setmTableNo(int mTableNo) {
        this.mTableNo = mTableNo;
    }


    public boolean ismOrderStatus() {
        return mOrderStatus;
    }

    public void setmOrderStatus(boolean mOrderStatus) {
        this.mOrderStatus = mOrderStatus;
    }

    public String getmNewOrderId() {
        return mNewOrderId;
    }

    public void setmNewOrderId(String mNewOrderId) {
        this.mNewOrderId = mNewOrderId;
    }

    public ArrayList<ChefMenuDetailsDTO> getmMenuChild() {
        return mMenuChild;
    }

    public void setmMenuChild(ArrayList<ChefMenuDetailsDTO> mMenuChild) {
        this.mMenuChild = mMenuChild;
    }

    public int getmOrderNumner() {
        return mOrderNumner;
    }

    public void setmOrderNumner(int mOrderNumner) {
        this.mOrderNumner = mOrderNumner;
    }

    public int getmNewOrderStatus() {
        return mNewOrderStatus;
    }

    public void setmNewOrderStatus(int mNewOrderStatus) {
        this.mNewOrderStatus = mNewOrderStatus;
    }

    public String getOrderDate() {
        return mOrderDate;
    }

    public void setOrderDate(String orderDate) {
        this.mOrderDate = orderDate;
    }

    public String getOrderTime() {
        return mOrderTime;
    }

    public void setOrderTime(String orderTime) {
        this.mOrderTime = orderTime;
    }

    public java.sql.Date getOrderDt() {
        if (this.mOrderDate == null || this.mOrderDate.isEmpty())
            return this.orderDt;

        this.orderDt = new ChefDateUtil().getOrderDt(mOrderDate);
        return orderDt;
    }

    public Time getOrderTm() {
        if (this.mOrderTime == null || this.mOrderTime.isEmpty())
            return this.orderTm;

        this.orderTm = new ChefDateUtil().getOrderTm(mOrderTime);
        return orderTm;
    }

    public String TimeDiff() {
        String str = "";

        java.util.Date calendarDate = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ROOT).getTime();
        ChefDateUtil dateUtil = new ChefDateUtil();
        long currentTime = calendarDate.getTime() - dateUtil.getTimeOffsetAsPerLocal(5, 30);
        long orderTime = dateUtil.dateAndTimeToDateTime(this.getOrderDt(), this.getOrderTm());

        long timeDiff = currentTime - orderTime;

        long totalMilliSecondsForOneMin = 60 * 1000;
        long totalMilliSecondsForOneHour = 60 * totalMilliSecondsForOneMin;
        long timeDiffInHrs = timeDiff / totalMilliSecondsForOneHour;
        long timeDiffInMins = (timeDiff / totalMilliSecondsForOneMin) % 60;

        if (timeDiffInHrs > 1)
            str += timeDiffInHrs + " hrs ";
        if (timeDiffInMins > 0)
            str += String.format("%2d",timeDiffInMins) + " mins";
        else
            str = "1 min";
        return str;
    }
    public int getmTakeAwayNumber() {
        return mTakeAwayNumber;
    }

    public void setmTakeAwayNumber(int mTakeAwayNumber) {
        this.mTakeAwayNumber = mTakeAwayNumber;
    }

    public int getmOrderType() {
        return mOrderType;
    }

    public void setmOrderType(int mOrderType) {
        this.mOrderType = mOrderType;
    }
}
