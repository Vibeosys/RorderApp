package com.vibeosys.quickserve.util;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by akshay on 24-02-2016.
 */
public class ChefDateUtil {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public java.sql.Date getOrderDt(String orderDate) {
        java.sql.Date orderDt = null;
        java.util.Date value = null;
        try {
            value = formatter.parse(orderDate);
            orderDt = new java.sql.Date(value.getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return orderDt;
    }

    public Time getOrderTm(String orderTime) {
        Time orderTm = null;
        java.util.Date value = null;
        try {
            value = formatterTime.parse(orderTime);
            orderTm = new Time(value.getTime());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return orderTm;
    }

    public long dateAndTimeToDateTime(java.sql.Date date, java.sql.Time time) {
        String myDate = date + " " + time;

        //sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        java.util.Date utilDate = new java.util.Date();

        try {
            utilDate = sdf.parse(myDate);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return utilDate.getTime();
    }

    public long getTimeOffsetAsPerLocal(int hours, int mins) {
        return ((hours * 60 * 60 * 1000) + (mins * 60 * 1000));
    }

}
