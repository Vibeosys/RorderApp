package com.vibeosys.rorderapp.database;

/**
 * This class is use to identify the table names and the attributes
 * Created by akshay on 21-01-2016.
 */
public class SqlContract {

    public abstract class SqlUser{
        public final static String TABLE_NAME="users";
        public final static String USER_ID="UserId";
        public final static String USER_NAME="UserName";
        public final static String PASSWORD="Password";
        public final static String ACTIVE="Active";
        public final static String CREATED_DATE="CreatedDate";
        public final static String UPDATED_DATE="UpadtedDate";
        public final static String ROLE_ID="RoleId";
        public final static String RESTAURANTID="RestaurantId";
    }

    public abstract class SqlHotelTable{
        public final static String TABLE_NAME="r_tables";
        public final static String TABLE_ID="TableId";
        public final static String TABLE_NO="TableNo";
        public final static String TABLE_CATEGORY="TableCategoryId";
        public final static String CAPACITY="Capacity";
        public final static String CREATED_DATE="CreatedDate";
        public final static String UPDATED_DATE="UpdatedDate";
        public final static String IS_OCCUPIED="IsOccupied";
    }

    public abstract class SqlTableCategory{
        public final static String TABLE_NAME="table_category";
        public final static String TABLE_CATEGORY_ID="TableCategoryId";
        public final static String CATEGORY_TITLE="CategoryTitle";
        public final static String IMAGE="Image";
        public final static String CREATED_DATE="CrearedDate";
        public final static String UPDATED_DATE="UpdatedDate";
    }
}
