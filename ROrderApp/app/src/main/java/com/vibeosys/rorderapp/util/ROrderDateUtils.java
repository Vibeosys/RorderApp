package com.vibeosys.rorderapp.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by akshay on 30-01-2016.
 */
public class ROrderDateUtils {

    final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public String getGMTCurrentDate() {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(new java.util.Date());
    }

    public String getLocalCurrentDate() {
        return dateFormat.format(new java.util.Date());
    }

    public String getLocalDateInFormat(Date date) {
        return dateFormat.format(date);
    }

    public String getGMTDateInFormat(Date date) {
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }

    public String getGMTCurrentTime() {
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return timeFormat.format(new java.util.Date());
    }

    public String getLocalCurrentTime() {
        return timeFormat.format(new java.util.Date());
    }

    public String getLocalTimeInFormat(Date date) {
        return timeFormat.format(date);
    }

    public String getGMTTimeInFormat(Date date) {
        return timeFormat.format(date);
    }
}
