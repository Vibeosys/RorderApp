package com.vibeosys.rorderapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vibeosys.rorderapp.data.BillDbDTO;
import com.vibeosys.rorderapp.data.BillDetailsDbDTO;
import com.vibeosys.rorderapp.data.HotelTableDTO;
import com.vibeosys.rorderapp.data.HotelTableDbDTO;
import com.vibeosys.rorderapp.data.MenuCateoryDbDTO;
import com.vibeosys.rorderapp.data.MenuDbDTO;
import com.vibeosys.rorderapp.data.MenuTagsDbDTO;
import com.vibeosys.rorderapp.data.OrderDetailsDTO;
import com.vibeosys.rorderapp.data.OrderDetailsDbDTO;
import com.vibeosys.rorderapp.data.OrderHeaderDTO;
import com.vibeosys.rorderapp.data.OrderMenuDTO;
import com.vibeosys.rorderapp.data.OrdersDbDTO;
import com.vibeosys.rorderapp.data.Sync;
import com.vibeosys.rorderapp.data.TableCategoryDTO;
import com.vibeosys.rorderapp.data.TableCategoryDbDTO;
import com.vibeosys.rorderapp.data.UserDTO;
import com.vibeosys.rorderapp.data.UserDbDTO;
import com.vibeosys.rorderapp.util.ROrderDateUtils;
import com.vibeosys.rorderapp.util.SessionManager;
import com.vibeosys.rorderapp.data.BillDetailsDTO;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anand on 09/01/2016.
 */

public class DbRepository extends SQLiteOpenHelper {

    private final String TAG = DbRepository.class.getSimpleName();

    public DbRepository(Context context, SessionManager sessionManager) {
        super(context, sessionManager.getDatabaseDeviceFullPath(), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    public void getDatabaseStructure() {
        final ArrayList<String> dirArray = new ArrayList<String>();

        SQLiteDatabase DB = getReadableDatabase();
        //SQLiteDatabase DB = sqlHelper.getWritableDatabase();
        Cursor c = DB.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        c.moveToFirst();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                dirArray.add(c.getString(c.getColumnIndex("name")));

                c.moveToNext();
            }
        }
        Log.i(TAG, "##" + dirArray);
        c.close();

    }

    public ArrayList<Sync> getPendingSyncRecords() {
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<Sync> syncTableData = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("select * from Sync ", null);
            syncTableData = new ArrayList<>();
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    do {
                        Sync sync = new Sync();
                        sync.setJsonSync(cursor.getString(cursor.getColumnIndex("JsonSync")));
                        syncTableData.add(sync);
                    } while (cursor.moveToNext());

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return syncTableData;
    }

    public boolean addDataToSync(String tableName, String userId, String jsonSync) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long rowsAffected = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            contentValues.put("UserId", userId);
            contentValues.put("JsonSync", jsonSync);
            contentValues.put("TableName", tableName);
            rowsAffected = sqLiteDatabase.insert("Sync", null, contentValues);
        } catch (Exception e) {
            Log.e("SyncInsert", "Error occurred while inserting in Sync table " + e.toString());
        } finally {
            if (contentValues != null)
                contentValues.clear();

            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return rowsAffected != -1;
    }

    public boolean createUserId(String userId) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            contentValues.put("MyUserId", userId);
            count = sqLiteDatabase.insert("my_users", null, contentValues);
        } catch (Exception e) {
            Log.e("DbOperationUserIns", "Error occurred while creating new user in db for " + userId + " " + e.toString());
        } finally {
            if (contentValues != null)
                contentValues.clear();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }

        return count != -1;
    }

    public boolean insertUsers(List<UserDbDTO> usersList) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            for (UserDbDTO dbUser : usersList) {
                contentValues.put(SqlContract.SqlUser.USER_ID, dbUser.getUserId());
                contentValues.put(SqlContract.SqlUser.USER_NAME, dbUser.getUserName());
                contentValues.put(SqlContract.SqlUser.ACTIVE, dbUser.isActive());
                contentValues.put(SqlContract.SqlUser.ROLE_ID, dbUser.getRoleId());
                contentValues.put(SqlContract.SqlUser.RESTAURANTID, dbUser.getRestaurantId());
                count = sqLiteDatabase.insert(SqlContract.SqlUser.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## User is Added Successfully");
            }
        } catch (Exception e) {
            Log.e("DbOperationsEx", "Error while adding users " + e.toString());
        } finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return count != -1;
    }

    public boolean insertTables(List<HotelTableDbDTO> tableList) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            for (HotelTableDbDTO dbTable : tableList) {
                contentValues.put(SqlContract.SqlHotelTable.TABLE_ID, dbTable.getTableId());
                contentValues.put(SqlContract.SqlHotelTable.TABLE_NO, dbTable.getTableNo());
                contentValues.put(SqlContract.SqlHotelTable.TABLE_CATEGORY, dbTable.getTableCategoryId());
                contentValues.put(SqlContract.SqlHotelTable.CAPACITY, dbTable.getCapacity());
                contentValues.put(SqlContract.SqlHotelTable.CREATED_DATE, "" + dbTable.getCreatedDate());
                contentValues.put(SqlContract.SqlHotelTable.UPDATED_DATE, "" + dbTable.getUpdatedDate());
                contentValues.put(SqlContract.SqlHotelTable.IS_OCCUPIED, dbTable.isOccupied());
                count = sqLiteDatabase.insert(SqlContract.SqlHotelTable.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Table is Added Successfully");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while adding Tables " + e.toString());
        } finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return count != -1;
    }

    public UserDTO autheticateUser(String userName, String password) {
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        int userId = 0;
        String name = "";
        boolean active;
        int rollId;
        int restaurantId;
        UserDTO user = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            String query = "SELECT * FROM " + SqlContract.SqlUser.TABLE_NAME + " WHERE " + SqlContract.SqlUser.USER_NAME
                    + "=? AND " + SqlContract.SqlUser.PASSWORD + "=? LIMIT 1";
            cursor = sqLiteDatabase.rawQuery(query, new String[]{userName, password});

            if (cursor != null) {
                if (cursor.getCount() > 0) {

                    cursor.moveToFirst();
                    userId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlUser.USER_ID));
                    name = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                    active = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.ACTIVE)));
                    rollId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlUser.ROLE_ID));
                    restaurantId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlUser.RESTAURANTID));
                    user = new UserDTO(userId, name, active, rollId, restaurantId);
                }
            }
        } catch (Exception e) {
            user = null;
            Log.e(TAG, "Error occured in autheticate User function" + e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return user;
    }

    public ArrayList<HotelTableDTO> getTableRecords() {//changes
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<HotelTableDTO> hotelTables = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            //  cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + SqlContract.SqlHotelTable.TABLE_NAME + " ORDER BY " + SqlContract.SqlHotelTable.TABLE_NO, null);
            cursor = sqLiteDatabase.rawQuery("select TableId ,TableNo,r_tables.TableCategoryId,CategoryTitle,Capacity,IsOccupied,r_tables.CreatedDate,r_tables.UpdatedDate From r_tables LEFT Join table_category  On r_tables.TableCategoryId = table_category.TableCategoryId ORder by r_tables.TableNo", null);

            hotelTables = new ArrayList<>();
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    do {
                        //Log.i(TAG, "##" + cursor.getCount() + " " + cursor.getInt(1));
                        int tableId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_ID));
                        int tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_NO));
                        int tableCtegory = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_CATEGORY));
                        String tableCtegoryName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTableCategory.CATEGORY_TITLE));
                        int capacity = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.CAPACITY));
                        String createdDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlHotelTable.CREATED_DATE));
                        String updatedDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlHotelTable.UPDATED_DATE));
                        int isOccupied = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.IS_OCCUPIED));
                        boolean occupied = isOccupied == 1 ? true : false;
                        HotelTableDTO table = new HotelTableDTO(tableId, tableNo, tableCtegory,
                                tableCtegoryName, capacity, createdDate, updatedDate,
                                occupied);
                        //table.setJsonSync();
                        hotelTables.add(table);
                    } while (cursor.moveToNext());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return hotelTables;
    }


    public BillDetailsDTO  getBillDetailsRecords()//08/02/2016 put where condition for table no and custer id and from sharder preference take user name ann user id
    {
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        BillDetailsDTO billDetailsRecords = null;
        try
        {
            sqLiteDatabase = getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("select bill.BillNo,bill.BillDate,bill.NetAmount,bill.TotalTaxAmount,bill.TotalPayAmount,users.UserName from bill INNER Join users On users.UserId = bill.UserId;", null);

            if(cursor !=null) {
                if(cursor.getCount() > 0 )
                    cursor.moveToFirst();
                {
                   do
                   {
                       int billNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlBill.BILL_NO));
                       Date billDate = Date.valueOf(cursor.getString(cursor.getColumnIndex(SqlContract.SqlBill.BILL_DATE)));
                       double netAmount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlBill.NET_AMOUNT));
                       double totalTaxAmount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlBill.TOATL_TAX_AMT));
                       double totalPayAbleTax = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlBill.TOTAL_PAY_AMT));
                       String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));

                       billDetailsRecords = new BillDetailsDTO(billNo,billDate,netAmount,totalTaxAmount,totalPayAbleTax,userName);

                   }while (cursor.moveToNext());

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return billDetailsRecords;
    }

    public ArrayList<TableCategoryDTO> getTableCategories() {
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<TableCategoryDTO> tableCategories = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + SqlContract.SqlTableCategory.TABLE_NAME, null);
            tableCategories = new ArrayList<>();
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    do {
                        //Log.i(TAG, "##" + cursor.getCount() + " " + cursor.getInt(1));
                        int categoryId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTableCategory.TABLE_CATEGORY_ID));
                        String categoryTitle = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTableCategory.CATEGORY_TITLE));
                        // String updatedDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTableCategory.));

                        TableCategoryDTO table = new TableCategoryDTO();
                        table.setmCategoryId(categoryId);
                        table.setmTitle(categoryTitle);
                        //table.setJsonSync();
                        tableCategories.add(table);
                    } while (cursor.moveToNext());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return tableCategories;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP DATABASE IF EXISTS Answer");
        db.execSQL("DROP DATABASE IF EXISTS CommentAndLike");
        db.execSQL("DROP DATABASE IF EXISTS Config");
        db.execSQL("DROP DATABASE IF EXISTS  Destination");
        db.execSQL("DROP DATABASE IF EXISTS usersImages");
        db.execSQL("DROP DATABASE IF EXISTS MyMap");
        db.execSQL("DROP DATABASE IF EXISTS My_Images");
        db.execSQL("DROP DATABASE IF EXISTS Option");
        db.execSQL("DROP DATABASE IF EXISTS Question");
        db.execSQL("DROP DATABASE IF EXISTS  Sync");
        db.execSQL("DROP DATABASE IF EXISTS  TempData");
        db.execSQL("DROP DATABASE IF EXISTS  User");
        db.execSQL("DROP DATABASE IF EXISTS myUser");
    }

    public boolean insertBills(List<BillDbDTO> billInserts) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            for (BillDbDTO dbTable : billInserts) {
                contentValues.put(SqlContract.SqlBill.BILL_NO, dbTable.getBillNo());
                contentValues.put(SqlContract.SqlBill.BILL_DATE, dbTable.getBillDate().toString());
                contentValues.put(SqlContract.SqlBill.BILL_TIME, dbTable.getBillTime().toString());
                contentValues.put(SqlContract.SqlBill.NET_AMOUNT, dbTable.getNetAmount());
                contentValues.put(SqlContract.SqlBill.TOATL_TAX_AMT, dbTable.getTotalTaxAmount());
                contentValues.put(SqlContract.SqlBill.TOTAL_PAY_AMT, dbTable.getTotalPayAmount());
                contentValues.put(SqlContract.SqlBill.CREATED_DATE, dbTable.getCreatedDate().toString());
                contentValues.put(SqlContract.SqlBill.UPDATED_DATE, dbTable.getUpdatedDate().toString());
                contentValues.put(SqlContract.SqlBill.USER_ID, dbTable.getUserId());
                count = sqLiteDatabase.insert(SqlContract.SqlBill.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Bill is Added Successfully" + dbTable.getBillNo());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while adding Bills " + e.toString());
        } finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return count != -1;
    }

    public boolean insertBillDetails(List<BillDetailsDbDTO> billDetailInserts) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            for (BillDetailsDbDTO billDetails : billDetailInserts) {
                contentValues.put(SqlContract.SqlBillDetails.AUTO_ID, billDetails.getAutoId());
                contentValues.put(SqlContract.SqlBillDetails.ORDER_ID, billDetails.getOrderId());
                contentValues.put(SqlContract.SqlBillDetails.BILL_NO, billDetails.getBillNo());
                contentValues.put(SqlContract.SqlBillDetails.CREATED_DATE, billDetails.getCreateDate().toString());
                contentValues.put(SqlContract.SqlBillDetails.UPDATED_DATE, billDetails.getUpdatedDate().toString());
                count = sqLiteDatabase.insert(SqlContract.SqlBillDetails.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Bill Details is added successfully" + billDetails.getBillNo());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error While adding Bill details" + e.toString());
        } finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return count != -1;
    }

    public boolean insertMenus(List<MenuDbDTO> menuInserts) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            for (MenuDbDTO menu : menuInserts) {
                contentValues.put(SqlContract.SqlMenu.MENU_ID, menu.getMenuId());
                contentValues.put(SqlContract.SqlMenu.MENU_TITLE, menu.getMenuTitle());
                contentValues.put(SqlContract.SqlMenu.IMAGE, menu.getImage());
                contentValues.put(SqlContract.SqlMenu.PRICE, menu.getPrice());
                contentValues.put(SqlContract.SqlMenu.INGREDIENTS, menu.getIngredients());
                contentValues.put(SqlContract.SqlMenu.TAGS, menu.getTags());
                contentValues.put(SqlContract.SqlMenu.AVAIL_STATUS, menu.isAvailabilityStatus());
                contentValues.put(SqlContract.SqlMenu.ACTIVE, menu.isActive());
                contentValues.put(SqlContract.SqlMenu.FOOD_TYPE, menu.isFoodType());
                contentValues.put(SqlContract.SqlMenu.CREATED_DATE, menu.getCreatedDate().toString());
                contentValues.put(SqlContract.SqlMenu.UPDATED_DATE, menu.getUpdatedDate().toString());
                contentValues.put(SqlContract.SqlMenu.CATEGORY_ID, menu.getCategoryId());
                contentValues.put(SqlContract.SqlMenu.IS_SPICY, menu.isSpicy());
                count = sqLiteDatabase.insert(SqlContract.SqlMenu.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Menu is added successfully" + menu.getMenuId());
            }

        } catch (Exception e) {
            Log.e(TAG, "## Error at insertMenu" + e.toString());
        } finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return count != -1;
    }

    public boolean insertMenuCategory(List<MenuCateoryDbDTO> menuCategoryInserts) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            for (MenuCateoryDbDTO menuCateory : menuCategoryInserts) {
                contentValues.put(SqlContract.SqlMenuCategory.CATEGORY_ID, menuCateory.getCategoryId());
                contentValues.put(SqlContract.SqlMenuCategory.CATEGORY_TITLE, menuCateory.getCategoryTitle());
                contentValues.put(SqlContract.SqlMenuCategory.CATEGORY_IMG, menuCateory.getCategoryImage());
                contentValues.put(SqlContract.SqlMenuCategory.ACTIVE, menuCateory.isActive());
                contentValues.put(SqlContract.SqlMenuCategory.CREATED_DATE, menuCateory.getCreatedDate().toString());
                contentValues.put(SqlContract.SqlMenuCategory.UPDATED_DATE, menuCateory.getUpdatedDate().toString());
                count = sqLiteDatabase.insert(SqlContract.SqlMenuCategory.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Menu Category is added successfully" + menuCateory.getCategoryId());

            }

        } catch (Exception e) {
            Log.d(TAG, "## Error at insertMenuCategory" + e.toString());
        } finally {
            {
                if (sqLiteDatabase != null)
                    sqLiteDatabase.close();
            }
        }
        return count != -1;
    }

    public boolean insertMenuTags(List<MenuTagsDbDTO> menuTagInserts) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            for (MenuTagsDbDTO menuTag : menuTagInserts) {
                contentValues.put(SqlContract.SqlMenuTags.TAG_ID, menuTag.getTagId());
                contentValues.put(SqlContract.SqlMenuTags.TAG_TITLE, menuTag.getTagTitle());
                count = sqLiteDatabase.insert(SqlContract.SqlMenuTags.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Menu Tag is added successfully" + menuTag.getTagId());

            }

        } catch (Exception e) {
            Log.d(TAG, "## Error at insertMenuTag" + e.toString());
        } finally {
            {
                if (sqLiteDatabase != null)
                    sqLiteDatabase.close();
            }
        }
        return count != -1;
    }

    public boolean insertOrderDetails(List<OrderDetailsDbDTO> orderDetailInserts) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            for (OrderDetailsDbDTO orderDetail : orderDetailInserts) {
                contentValues.put(SqlContract.SqlOrderDetails.ORDER_DETAILS_ID, orderDetail.getOrderDetailsId());
                contentValues.put(SqlContract.SqlOrderDetails.ORDER_PRICE, orderDetail.getOrderPrice());
                contentValues.put(SqlContract.SqlOrderDetails.ORDER_QUANTITY, orderDetail.getOrderQuantity());
                contentValues.put(SqlContract.SqlOrderDetails.CREATED_DATE, orderDetail.getCreatedDate().toString());
                contentValues.put(SqlContract.SqlOrderDetails.UPDATE_DATE, orderDetail.getUpdatedDate().toString());
                contentValues.put(SqlContract.SqlOrderDetails.ORDER_ID, orderDetail.getOrderId());
                contentValues.put(SqlContract.SqlOrderDetails.MENU_ID, orderDetail.getMenuId());
                contentValues.put(SqlContract.SqlOrderDetails.MENU_TITLE, orderDetail.getMenuTitle());
                count = sqLiteDatabase.insert(SqlContract.SqlOrderDetails.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Order detail is added successfully" + orderDetail.getOrderDetailsId());

            }

        } catch (Exception e) {
            Log.d(TAG, "## Error at insertOrderDetails" + e.toString());
        } finally {
            {
                if (sqLiteDatabase != null)
                    sqLiteDatabase.close();
            }
        }
        return count != -1;
    }

    public boolean insertOrders(List<OrdersDbDTO> orderInserts) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            for (OrdersDbDTO order : orderInserts) {
                contentValues.put(SqlContract.SqlOrders.ORDER_ID, order.getOrderId());
                contentValues.put(SqlContract.SqlOrders.ORDER_NO, order.getOrderNo());
                contentValues.put(SqlContract.SqlOrders.ORDER_STATUS, order.isOrderStatus());
                contentValues.put(SqlContract.SqlOrders.ORDER_DATE, order.getOrderDate().toString());
                contentValues.put(SqlContract.SqlOrders.ORDER_TIME, order.getOrderTime().toString());
                contentValues.put(SqlContract.SqlOrders.CREATED_DATE, order.getCreatedDate().toString());
                contentValues.put(SqlContract.SqlOrders.UPDATED_DATE, order.getUpdatedDate().toString());
                contentValues.put(SqlContract.SqlOrders.TABLE_NO, order.getTableNo());
                contentValues.put(SqlContract.SqlOrders.USER_ID, order.getUserId());
                contentValues.put(SqlContract.SqlOrders.ORDER_AMOUNT, order.getOrderAmount());
                count = sqLiteDatabase.insert(SqlContract.SqlOrders.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Order is added successfully" + order.getOrderId());
            }

        } catch (Exception e) {
            Log.d(TAG, "## Error at insertOrders" + e.toString());
        } finally {
            {
                if (sqLiteDatabase != null)
                    sqLiteDatabase.close();
            }
        }
        return count != -1;
    }

    public boolean insertTableCategories(List<TableCategoryDbDTO> tableCategoryInsert) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            for (TableCategoryDbDTO tableCategory : tableCategoryInsert) {
                contentValues.put(SqlContract.SqlTableCategory.TABLE_CATEGORY_ID, tableCategory.getTableCategoryId());
                contentValues.put(SqlContract.SqlTableCategory.CATEGORY_TITLE, tableCategory.getCategoryTitle());
                contentValues.put(SqlContract.SqlTableCategory.IMAGE, tableCategory.getImage());
                contentValues.put(SqlContract.SqlTableCategory.CREATED_DATE, tableCategory.getCreatedDate().toString());
                contentValues.put(SqlContract.SqlTableCategory.UPDATED_DATE, tableCategory.getUpdatedDate().toString());
                count = sqLiteDatabase.insert(SqlContract.SqlTableCategory.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Table Category is added successfully" + tableCategory.getTableCategoryId());

            }

        } catch (Exception e) {
            Log.d(TAG, "## Error at insertMenuCategory" + e.toString());
        } finally {
            {
                if (sqLiteDatabase != null)
                    sqLiteDatabase.close();
            }
        }
        return count != -1;
    }

    public ArrayList<OrderMenuDTO> getOrderMenu() {
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<OrderMenuDTO> orderMenus = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT menu.MenuId,menu.FoodType," +
                    "menu.Image,menu.MenuTitle,menu.Tags,menu.IsSpicy,menu_category.CategoryTitle," +
                    "menu.Price From menu Left Join menu_category " +
                    "where menu.CategoryId=menu_category.CategoryId", null);
            orderMenus = new ArrayList<>();
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    do {
                        //Log.i(TAG, "##" + cursor.getCount() + " " + cursor.getInt(1));
                        int menuId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMenu.MENU_ID));
                        int iFoodType = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMenu.FOOD_TYPE));
                        boolean foodType = iFoodType == 1 ? true : false;
                        String menuImage = cursor.getString(cursor.getColumnIndex(SqlContract.SqlMenu.IMAGE));
                        String menuTitle = cursor.getString(cursor.getColumnIndex(SqlContract.SqlMenu.MENU_TITLE));
                        String menuTags = cursor.getString(cursor.getColumnIndex(SqlContract.SqlMenu.TAGS));
                        String menuCategory = cursor.getString(cursor.getColumnIndex(SqlContract.SqlMenuCategory.CATEGORY_TITLE));
                        int iSpicy = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMenu.IS_SPICY));
                        boolean isSpicy = iSpicy == 1 ? true : false;
                        double menuPrice = Double.parseDouble(cursor.getString(cursor.getColumnIndex(SqlContract.SqlMenu.PRICE)));
                        OrderMenuDTO orderMenu = new OrderMenuDTO(menuId, menuTitle, menuImage, foodType, menuTags, menuCategory, menuPrice, 0, OrderMenuDTO.SHOW, isSpicy);
                        //table.setJsonSync();
                        orderMenus.add(orderMenu);
                    } while (cursor.moveToNext());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return orderMenus;
    }

    public boolean insertOrUpdateTempOrder(int tableId, int tableNo, int menuId, int qty) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            String[] whereClause = new String[]{String.valueOf(tableId), String.valueOf(tableNo), String.valueOf(menuId)};
            sqLiteDatabase = getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlTempOrder.TABLE_NAME
                    + " Where " + SqlContract.SqlTempOrder.TABLE_ID + "=? AND " + SqlContract.SqlTempOrder.TABLE_NO
                    + "=? And " + SqlContract.SqlTempOrder.MENU_ID + "=?", whereClause);
            int rowCount = cursor.getCount();
            cursor.close();
            sqLiteDatabase.close();

            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            ROrderDateUtils rOrderDateUtils = new ROrderDateUtils();
            contentValues.put(SqlContract.SqlTempOrder.TABLE_NO, tableNo);
            contentValues.put(SqlContract.SqlTempOrder.TABLE_ID, tableId);
            contentValues.put(SqlContract.SqlTempOrder.MENU_ID, menuId);
            contentValues.put(SqlContract.SqlTempOrder.QUANTITY, qty);
            contentValues.put(SqlContract.SqlTempOrder.ORDER_DATE, rOrderDateUtils.getGMTCurrentDate());
            contentValues.put(SqlContract.SqlTempOrder.ORDER_TIME, rOrderDateUtils.getGMTCurrentTime());
            contentValues.put(SqlContract.SqlTempOrder.ORDER_STATUS, 0);

            if (rowCount == 0&&qty!=0)
                count = sqLiteDatabase.insert(SqlContract.SqlTempOrder.TABLE_NAME, null, contentValues);
            else if(qty==0)

                count=sqLiteDatabase.delete(SqlContract.SqlTempOrder.TABLE_NAME,SqlContract.SqlTempOrder.TABLE_ID + "=? AND " + SqlContract.SqlTempOrder.TABLE_NO
                        + "=? And " + SqlContract.SqlTempOrder.MENU_ID + "=?",whereClause);
            else
                count = sqLiteDatabase.update(SqlContract.SqlTempOrder.TABLE_NAME, contentValues, SqlContract.SqlTempOrder.TABLE_ID + "=? AND " + SqlContract.SqlTempOrder.TABLE_NO
                        + "=? And " + SqlContract.SqlTempOrder.MENU_ID + "=?", whereClause);

            contentValues.clear();
            sqLiteDatabase.close();
            Log.i(TAG, "Values are inserted into TempTable" + rOrderDateUtils.getGMTCurrentDate() + "" + rOrderDateUtils.getGMTCurrentTime());
        } catch (Exception e) {
            Log.e(TAG, "error at insertOrUpdateTempOrder");
        } finally {
            if (sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
        }

        return count != -1;
    }

    public boolean clearUpdateTempData(int tableId, int tableNo) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            String[] whereClause = new String[]{String.valueOf(tableId), String.valueOf(tableNo)};
            count = sqLiteDatabase.delete(SqlContract.SqlTempOrder.TABLE_NAME,
                    SqlContract.SqlTempOrder.TABLE_ID + "=? AND " + SqlContract.SqlTempOrder.TABLE_NO + "=?",
                    whereClause);
            contentValues.clear();
            sqLiteDatabase.close();
        } catch (Exception e) {

        } finally {
            sqLiteDatabase.close();
        }
        return count != -1;
    }

    public boolean setOccupied(boolean value, int tableId) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            contentValues = new ContentValues();
            contentValues.put(SqlContract.SqlHotelTable.IS_OCCUPIED, value);
            String[] whereClause = new String[]{String.valueOf(tableId)};
            count = sqLiteDatabase.update(SqlContract.SqlHotelTable.TABLE_NAME, contentValues,
                    SqlContract.SqlHotelTable.TABLE_ID + "=?", whereClause);
            sqLiteDatabase.close();

        } catch (Exception e) {
            Log.e(TAG, "## error at set Occupied " + e.toString());
            e.printStackTrace();
        } finally {
            sqLiteDatabase.close();
        }
        return count != -1;
    }


    public ArrayList<OrderHeaderDTO> getOrdersOfTable(int tableId) {
        ArrayList<OrderHeaderDTO> orders = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        String[] whereClause = new String[]{String.valueOf(tableId)};
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();

            cursor = sqLiteDatabase.rawQuery("select * from orders where " + SqlContract.SqlOrders.TABLE_NO + "=?", whereClause);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    do {
                        int orderId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_ID));
                        int orderNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_NO));
                        //boolean orderStatus=cursor.get;
                        String orderDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_DATE));
                        String orderTime = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME));
                        String createdDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.CREATED_DATE));
                        String updatedDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.UPDATED_DATE));
                        int tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.TABLE_NO));
                        int userId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.USER_ID));
                        double orderAmount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_AMOUNT));
                        OrderHeaderDTO orderHeaderDTO = new OrderHeaderDTO(orderId,
                                orderNo, true, Date.valueOf(orderDate), Time.valueOf(orderTime),
                                Date.valueOf(createdDate), Date.valueOf(updatedDate), tableNo,
                                userId, orderAmount, false);
                        orders.add(orderHeaderDTO);
                    } while (cursor.moveToNext());
                }
            }

            cursor.close();
            sqLiteDatabase.close();

        } catch (Exception e) {
            Log.e(TAG, "Error at getOrdersOf table " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return orders;
    }

    public void getOrederDetailsGroupByID(ArrayList<OrderHeaderDTO> orders) {
        HashMap<OrderHeaderDTO, List<OrderDetailsDTO>> hashMap = new HashMap<>();
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            for (OrderHeaderDTO order : orders) {
                List<OrderDetailsDTO> orderDetailsList = new ArrayList<>();
                int orderId = order.getOrderId();
                String[] whereClause = new String[]{String.valueOf(orderId)};
                Cursor cursor = null;
                cursor = sqLiteDatabase.rawQuery("Select * from order_details where order_details.OrderId=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        order.setItemCount(cursor.getCount());
                        do {
                            int orderDetailsId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_DETAILS_ID));
                            double orderPrice = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_PRICE));
                            int orderQuantity = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_QUANTITY));
                            String createdDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.CREATED_DATE));
                            String updatedDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.UPDATE_DATE));
                            String myOrderId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_ID));
                            int menuId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.MENU_ID));
                            String menuTitle = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.MENU_TITLE));

                            OrderDetailsDTO orderDetails = new OrderDetailsDTO(orderDetailsId,
                                    orderPrice, orderQuantity, Date.valueOf(createdDate),
                                    Date.valueOf(updatedDate), myOrderId, menuId, menuTitle,0);
                            orderDetailsList.add(orderDetails);
                        } while (cursor.moveToNext());
                    }
                }

                cursor.close();
                order.setOrderDetailsDTOs(orderDetailsList);
            }
        } catch (Exception e) {
            Log.e(TAG, "## error at getOrederDetailsGroupByID function");
        } finally {
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }

    }

    public int getOccupiedTable() {
        int count = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlHotelTable.TABLE_NAME + " where IsOccupied = 1", null);
            count = cursor.getCount();
        } catch (Exception e) {
            Log.e(TAG, "## error at getOccupied table" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return count;
    }

    public OrderHeaderDTO getOrederDetailsFromTemp(int tableId, int userId) {

        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        OrderHeaderDTO orderHeaderDTO=null;
        try {
            sqLiteDatabase = getReadableDatabase();
            List<OrderDetailsDTO> orderDetailsList = new ArrayList<>();
            String[] whereClause = new String[]{String.valueOf(tableId)};
            double orderAmount = 0;
            cursor = sqLiteDatabase.rawQuery("Select temp_order.TempOrderId,temp_order.Quantity," +
                    "temp_order.MenuId,temp_order.OrderDate,temp_order.OrderTime,menu.MenuTitle," +
                    "menu.Price from temp_order left join menu where " +
                    "temp_order.MenuId=menu.MenuId and temp_order.TableId=?", whereClause);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        int orderDetailsTempId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTempOrder.TEMP_ORDER_ID));
                        double menuPrice = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlMenu.PRICE));
                        int orderQuantity = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTempOrder.QUANTITY));
                        String createdDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTempOrder.ORDER_DATE));
                        //String updatedDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.UPDATE_DATE));
                        //String myOrderId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_ID));
                        int menuId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTempOrder.MENU_ID));
                        String menuTitle = cursor.getString(cursor.getColumnIndex(SqlContract.SqlMenu.MENU_TITLE));
                        double orderPice = menuPrice * orderQuantity;
                        orderAmount = orderAmount + orderPice;
                        OrderDetailsDTO orderDetails = new OrderDetailsDTO(orderDetailsTempId,orderPice
                                , orderQuantity, Date.valueOf(createdDate),
                                Date.valueOf(createdDate), "", menuId, menuTitle,menuPrice);
                        orderDetailsList.add(orderDetails);
                    } while (cursor.moveToNext());
                }
            }
            orderHeaderDTO = new OrderHeaderDTO(tableId, userId, orderAmount, orderDetailsList.size(), true, orderDetailsList);
            cursor.close();
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return orderHeaderDTO;
    }
}
