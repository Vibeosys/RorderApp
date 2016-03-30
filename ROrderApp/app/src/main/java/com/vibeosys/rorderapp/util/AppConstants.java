package com.vibeosys.rorderapp.util;

/**
 * Created by akshay on 25-02-2016.
 */
public final class AppConstants {

    public final static int SERVICE_TIME_OUT = 30;// 30 sec app hit the server
    public final static int DINE_IN = 1;
    public final static int TAKE_AWAY = 2;
    public final static int ORDER_TIME_HOUR = 2;// show the 2 hours and given minute before orders
    public final static int ORDER_TIME_MINUTE = 0;

    // status for take away order
    public final static int TAKAWAY_STATUS_PENDING = 1;
    public final static int TAKAWAY_STATUS_READY = 2;
    public final static int TAKAWAY_STATUS_DELIVERED = 3;

    // Permissions constants
    public final static String PERMISSION_PLACE_ORDER = "PLACE_ORDER";
    public final static String PERMISSION_PRINT_BILL = "PRINT_BILL";
    public final static String PERMISSION_CANCEL_ORDER = "CANCEL_ORDER";
    public final static String PERMISSION_VIEW_DASHBOARD = "VIEW_DASHBOARD";
    public final static String PERMISSION_CHANGE_RESTAURANT_DATA = "CHANGE_RESTAURANT_DATA";

    public final static String CONFIG_LOGO_PRINT="PRINT_LOGO";
    public final static String CONFIG_BARCODE_PRINT="PRINT_BARCODE";

}
