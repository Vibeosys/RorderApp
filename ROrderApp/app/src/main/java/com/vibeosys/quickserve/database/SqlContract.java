package com.vibeosys.quickserve.database;

/**
 * This class is use to identify the table names and the attributes
 * Created by akshay on 21-01-2016.
 */
public class SqlContract {

    public abstract class SqlUser {
        public final static String TABLE_NAME = "users";
        public final static String USER_ID = "UserId";
        public final static String USER_NAME = "UserName";
        public final static String PASSWORD = "Password";
        public final static String ACTIVE = "Active";
        public final static String ROLE_ID = "RoleId";
        public final static String RESTAURANTID = "RestaurantId";
        public final static String PERMISSIONS = "Permissions";
    }

    public abstract class SqlHotelTable {
        public final static String TABLE_NAME = "r_tables";
        public final static String TABLE_ID = "TableId";
        public final static String TABLE_NO = "TableNo";
        public final static String TABLE_CATEGORY = "TableCategoryId";
        public final static String CAPACITY = "Capacity";
        public final static String IS_OCCUPIED = "IsOccupied";
    }

    public abstract class SqlTableCategory {
        public final static String TABLE_NAME = "table_category";
        public final static String TABLE_CATEGORY_ID = "TableCategoryId";
        public final static String CATEGORY_TITLE = "CategoryTitle";
        public final static String IMAGE = "Image";
    }

    public abstract class SqlBill {
        public final static String TABLE_NAME = "bill";
        public final static String BILL_NO = "BillNo";
        public final static String BILL_DATE = "BillDate";
        public final static String BILL_TIME = "BillTime";
        public final static String NET_AMOUNT = "NetAmount";
        public final static String TOATL_TAX_AMT = "TotalTaxAmount";
        public final static String TOTAL_PAY_AMT = "TotalPayAmount";
        public final static String USER_ID = "userId";
        public final static String CUST_ID = "CustId";
        public final static String TABLE_ID = "TableId";
        public final static String IS_PAYED = "IsPayed";
        public final static String PAID_BY = "PayedBy";
        public final static String DISCOUNT = "Discount";
        public final static String TAKE_AWAY_NO = "TakeawayNo";
    }

    public abstract class SqlBillDetails {
        public final static String TABLE_NAME = "bill_details";
        public final static String AUTO_ID = "AutoId";
        public final static String ORDER_ID = "OrderId";
        public final static String BILL_NO = "BillNo";
    }

    public abstract class SqlMenu {
        public final static String TABLE_NAME = "menu";
        public final static String MENU_ID = "MenuId";
        public final static String MENU_TITLE = "MenuTitle";
        public final static String IMAGE = "Image";
        public final static String PRICE = "Price";
        public final static String INGREDIENTS = "Ingredients";
        public final static String TAGS = "Tags";
        public final static String AVAIL_STATUS = "AvailabilityStatus";
        public final static String ACTIVE = "Active";
        public final static String FOOD_TYPE = "FoodType";
        public final static String CATEGORY_ID = "CategoryId";
        public final static String IS_SPICY = "IsSpicy";
        public final static String ROOM_ID = "RoomId";
        public final static String FB_TYPE_ID = "FbTypeId";
    }

    public abstract class SqlMenuCategory {
        public final static String TABLE_NAME = "menu_category";
        public final static String CATEGORY_ID = "CategoryId";
        public final static String CATEGORY_TITLE = "CategoryTitle";
        public final static String CATEGORY_IMG = "CategoryImage";
        public final static String ACTIVE = "Active";
        public final static String COLOUR = "Colour";
        public final static String IMG_URL = "ImgUrl";
    }

    public abstract class SqlMenuTags {
        public final static String TABLE_NAME = "menu_tags";
        public final static String TAG_ID = "TagId";
        public final static String TAG_TITLE = "TagTitle";

    }

    public abstract class SqlOrderDetails {
        public final static String TABLE_NAME = "order_details";
        public final static String ORDER_DETAILS_ID = "OrderDetailsId";
        public final static String ORDER_PRICE = "OrderPrice";
        public final static String ORDER_QUANTITY = "OrderQuantity";
        public final static String ORDER_ID = "OrderId";
        public final static String MENU_ID = "MenuId";
        public final static String SUB_MENU_ID = "SubMenuId";
        public final static String MENU_TITLE = "MenuTitle";
        public final static String NOTE = "Note";
    }

    public abstract class SqlOrders {
        public final static String TABLE_NAME = "orders";
        public final static String ORDER_ID = "OrderId";
        public final static String ORDER_NO = "OrderNo";
        public final static String CUST_ID = "CustId";
        public final static String ORDER_STATUS = "OrderStatus";
        public final static String ORDER_DATE = "Orderdate";
        public final static String ORDER_TIME = "OrderTime";
        public final static String TABLE_NO = "TableNo";
        public final static String USER_ID = "UserId";
        public final static String ORDER_AMOUNT = "OrderAmount";
        public final static String TAKE_AWAY_NO = "TakeawayNo";
        public final static String ORDER_TYPE = "OrderType";
    }

    public abstract class SqlTempOrder {
        public final static String TABLE_NAME = "temp_order";
        public final static String TEMP_ORDER_ID = "TempOrderId";
        public final static String CUST_ID = "CustId";
        public final static String TABLE_ID = "TableId";
        public final static String TABLE_NO = "TableNo";
        public final static String MENU_ID = "MenuId";
        public final static String QUANTITY = "Quantity";
        public final static String ORDER_DATE = "OrderDate";
        public final static String ORDER_TIME = "OrderTime";
        public final static String ORDER_STATUS = "OrderStatus";
        public final static String NOTE = "Note";
        public final static String SUB_MENU_ID = "SubMenuId";

    }

    public abstract class SqlCustomer {
        public final static String TABLE_NAME = "customer";
        public final static String CUST_ID = "CustId";
        public final static String CUST_NAME = "CustName";
        public final static String CUST_PHONE = "CustPhone";
        public final static String CUST_EMAIL = "CustEmail";
        public final static String CUST_ADDRESS = "CustAddress";
    }

    public abstract class SqlTableTransaction {
        public final static String TABLE_NAME = "table_transaction";
        public final static String TABLE_ID = "TableId";
        public final static String USER_ID = "UserId";
        public final static String CUST_ID = "CustId";
        public final static String IS_WAIT = "IsWaiting";
        public final static String ARRIVAL_TIME = "ArrivalTime";
        public final static String OCCUPANCY = "Occupancy";
    }

    public abstract class SqlPaymentMode {
        public final static String TABLE_NAME = "payment_mode_master";
        public final static String PAYMENT_MODE_ID = "PaymentModeId";
        public final static String PAYMENT_MODE_TITLE = "PaymentModeTitle";
        public final static String ACTIVE = "Active";
    }

    public abstract class SqlMenuNoteMaster {
        public final static String TABLE_NAME = "menu_note_master";
        public final static String NOTE_ID = "NoteId";
        public final static String NOTE_TITLE = "NoteTitle";
        public final static String ACTIVE = "Active";
    }

    public abstract class SqlFeedbackMaster {
        public final static String TABLE_NAME = "feedback_master";
        public final static String FEEDBACK_ID = "FeedbackId";
        public final static String FEEDBACK_TITLE = "FeedbackTitle";
        public final static String ACTIVE = "Active";
    }

    public abstract class SqlRestaurant {
        public final static String TABLE_NAME = "restaurant";
        public final static String RESTAURANT_ID = "RestaurantId";
        public final static String RESTAURANT_NAME = "RestaurantTitle";
        public final static String RESTAURANT_URL = "LogoUrl";
        public final static String RESTAURANT_ADDRESS = "Address";
        public final static String RESTAURANT_AREA = "Area";
        public final static String RESTAURANT_CITY = "City";
        public final static String RESTAURANT_COUNTRY = "Country";
        public final static String RESTAURANT_PHONE = "Phone";
        public final static String RESTAURANT_FOOTER = "Footer";
    }

    public abstract class SqlTakeAwaySource {
        public final static String TABLE_NAME = "takeaway_source";
        public final static String TAKE_AWAY_SOURCE_ID = "SourceId";
        public final static String SOURCE_NAME = "SourceName";
        public final static String SOURCE_URL = "SourceImg";
        public final static String DISCOUNT = "Discount";
        public final static String ACTIVE = "Active";
    }

    public abstract class SqlTakeAway {
        public final static String TABLE_NAME = "takeaway";
        public final static String TAKE_AWAY_ID = "TakeawayId";
        public final static String TAKE_AWAY_NO = "TakeawayNo";
        public final static String DISCOUNT = "Discount";
        public final static String DELIVERY_CHG = "DeliveryCharges";
        public final static String CUST_ID = "CustId";
        public final static String RESTAURANT_ID = "RestaurantId";
        public final static String USER_ID = "UserId";
        public final static String SOURCE_ID = "SourceId";
        public final static String DATE = "CreatedDate";
    }

    public abstract class SqlDelivery {
        public final static String TABLE_NAME = "delivery";
        public final static String DELIVERY_ID = "DeliveryId";
        public final static String DELIVERY_NO = "DeliveryNo";
        public final static String DISCOUNT = "Discount";
        public final static String DELIVERY_CHG = "DeliveryCharges";
        public final static String CUST_ID = "CustId";
        public final static String RESTAURANT_ID = "RestaurantId";
        public final static String USER_ID = "UserId";
        public final static String SOURCE_ID = "SourceId";
        public final static String DATE = "CreatedDate";
    }

    public abstract class SqlOrderType {
        public final static String TABLE_NAME = "order_type";
        public final static String ORDER_TYPE_ID = "OrderTypeId";
        public final static String ORDER_TYPE_TITLE = "OrderTypeTitle";
        public final static String ACTIVE = "Active";

    }

    public abstract class SqlPermissionSet {
        public final static String TABLE_NAME = "permission_set";
        public final static String PERMISSION_ID = "PermissionId";
        public final static String PERMISSION_KEY = "PermissionKey";
        public final static String DESCRIPTION = "Description";
        public final static String ACTIVE = "Active";
    }

    public abstract class SqlRConfigSettings {
        public final static String TABLE_NAME = "r_config_settings";
        public final static String CONFIG_KEY = "ConfigKey";
        public final static String CONFIG_VALUE = "ConfigValue";
    }

    public abstract class SqlRPrinters {
        public final static String TABLE_NAME = "r_printers";
        public final static String PRINTER_ID = "PrinterId";
        public final static String IP_ADDRESS = "IpAddress";
        public final static String PRINTER_NAME = "PrinterName";
        public final static String MODEL_NAME = "ModelName";
        public final static String COMPANY = "Company";
        public final static String MAC_ADDRESS = "MacAddress";
        public final static String ACTIVE = "Active";
    }

    public abstract class SqlRRoomPrinter {
        public final static String TABLE_NAME = "r_room_printer";
        public final static String ROOM_ID = "RoomId";
        public final static String ROOM_TYPE_ID = "RoomTypeId";
        public final static String PRINTER_ID = "PrinterId";
        public final static String DESCRIPTION = "Description";
        public final static String ACTIVE = "Active";
    }

    public abstract class SqlRRooms {
        public final static String TABLE_NAME = "r_rooms";
        public final static String ROOM_ID = "RoomId";
        public final static String DESCRIPTION = "Description";
        public final static String ACTIVE = "Active";
    }

    public abstract class SqlRoomType {
        public final static String TABLE_NAME = "room_type";
        public final static String ROOM_TYPE_ID = "RoomTypeId";
        public final static String ROOM_TYPE = "RoomType";
        public final static String ACTIVE = "Active";
    }

    public abstract class SqlSubMenu {
        public final static String TABLE_NAME = "sub_menu";
        public final static String SUB_MENU_ID = "SubMenuId";
        public final static String MENU_ID = "MenuId";
        public final static String MENU_TITLE = "SubMenuTitle";
        public final static String PRICE = "Price";
    }
}
