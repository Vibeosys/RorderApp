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
import com.vibeosys.rorderapp.data.OrderDetailsDbDTO;
import com.vibeosys.rorderapp.data.OrdersDbDTO;
import com.vibeosys.rorderapp.data.Sync;
import com.vibeosys.rorderapp.data.TableCategoryDbDTO;
import com.vibeosys.rorderapp.data.UserDTO;
import com.vibeosys.rorderapp.data.UserDbDTO;
import com.vibeosys.rorderapp.util.SessionManager;

import java.util.ArrayList;
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
        String name="";
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
                    name=cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                    active=Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.ACTIVE)));
                    rollId=cursor.getInt(cursor.getColumnIndex(SqlContract.SqlUser.ROLE_ID));
                    restaurantId=cursor.getInt(cursor.getColumnIndex(SqlContract.SqlUser.RESTAURANTID));
                    user=new UserDTO(userId,name,active,rollId,restaurantId);
                }
            }
        } catch (Exception e) {
            user=null;
            Log.e(TAG, "Error occured in autheticate User function" + e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return user;
    }

    public ArrayList<HotelTableDTO> getTableRecords() {
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<HotelTableDTO> hotelTables = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + SqlContract.SqlHotelTable.TABLE_NAME, null);
            hotelTables = new ArrayList<>();
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    do {
                        //Log.i(TAG, "##" + cursor.getCount() + " " + cursor.getInt(1));
                        int tableId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_ID));
                        int tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_NO));
                        int tableCtegory = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_CATEGORY));
                        int capacity = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.CAPACITY));
                        String createdDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlHotelTable.CREATED_DATE));
                        String updatedDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlHotelTable.UPDATED_DATE));
                        String isOccupied = cursor.getString(cursor.getColumnIndex(SqlContract.SqlHotelTable.IS_OCCUPIED));
                        HotelTableDTO table = new HotelTableDTO(tableId, tableNo, tableCtegory,
                                capacity, createdDate, updatedDate, Boolean.parseBoolean(isOccupied));
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
            for (MenuCateoryDbDTO menuCateory:menuCategoryInserts) {
                contentValues.put(SqlContract.SqlMenuCategory.CATEGORY_ID,menuCateory.getCategoryId());
                contentValues.put(SqlContract.SqlMenuCategory.CATEGORY_TITLE,menuCateory.getCategoryTitle());
                contentValues.put(SqlContract.SqlMenuCategory.CATEGORY_IMG,menuCateory.getCategoryImage());
                contentValues.put(SqlContract.SqlMenuCategory.ACTIVE,menuCateory.isActive());
                contentValues.put(SqlContract.SqlMenuCategory.CREATED_DATE,menuCateory.getCreatedDate().toString());
                contentValues.put(SqlContract.SqlMenuCategory.UPDATED_DATE,menuCateory.getUpdatedDate().toString());
                count=sqLiteDatabase.insert(SqlContract.SqlMenuCategory.TABLE_NAME,null,contentValues);
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
            for (MenuTagsDbDTO menuTag:menuTagInserts) {
                contentValues.put(SqlContract.SqlMenuTags.TAG_ID,menuTag.getTagId());
                contentValues.put(SqlContract.SqlMenuTags.TAG_TITLE,menuTag.getTagTitle());
                count=sqLiteDatabase.insert(SqlContract.SqlMenuTags.TABLE_NAME,null,contentValues);
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
        SQLiteDatabase sqLiteDatabase=null;
        ContentValues contentValues=null;
        long count=-1;
        try{
            sqLiteDatabase=getWritableDatabase();
            contentValues=new ContentValues();
            for(OrderDetailsDbDTO orderDetail: orderDetailInserts)
            {
                contentValues.put(SqlContract.SqlOrderDetails.ORDER_DETAILS_ID,orderDetail.getOrderDetailsId());
                contentValues.put(SqlContract.SqlOrderDetails.ORDER_PRICE,orderDetail.getOrderPrice());
                contentValues.put(SqlContract.SqlOrderDetails.ORDER_QUANTITY,orderDetail.getOrderQuantity());
                contentValues.put(SqlContract.SqlOrderDetails.CREATED_DATE,orderDetail.getCreatedDate().toString());
                contentValues.put(SqlContract.SqlOrderDetails.UPDATE_DATE,orderDetail.getUpdatedDate().toString());
                contentValues.put(SqlContract.SqlOrderDetails.ORDER_ID,orderDetail.getOrderId());
                contentValues.put(SqlContract.SqlOrderDetails.MENU_ID,orderDetail.getMenuId());
                contentValues.put(SqlContract.SqlOrderDetails.MENU_TITLE,orderDetail.getMenuTitle());
                count=sqLiteDatabase.insert(SqlContract.SqlOrderDetails.TABLE_NAME,null,contentValues);
                contentValues.clear();
                Log.d(TAG, "## Order detail is added successfully" + orderDetail.getOrderDetailsId());

            }

        }
        catch(Exception e)
        {
            Log.d(TAG,"## Error at insertOrderDetails"+e.toString());
        }
        finally {
            {
                if(sqLiteDatabase!=null)
                    sqLiteDatabase.close();
            }
        }
        return count!=-1;
    }

    public boolean insertOrders(List<OrdersDbDTO> orderInserts) {
        SQLiteDatabase sqLiteDatabase=null;
        ContentValues contentValues=null;
        long count=-1;
        try{
            sqLiteDatabase=getWritableDatabase();
            contentValues=new ContentValues();
            for(OrdersDbDTO order:orderInserts)
            {
                contentValues.put(SqlContract.SqlOrders.ORDER_ID,order.getOrderId());
                contentValues.put(SqlContract.SqlOrders.ORDER_NO,order.getOrderNo());
                contentValues.put(SqlContract.SqlOrders.ORDER_STATUS,order.isOrderStatus());
                contentValues.put(SqlContract.SqlOrders.ORDER_DATE,order.getOrderDate().toString());
                contentValues.put(SqlContract.SqlOrders.ORDER_TIME,order.getOrderTime().toString());
                contentValues.put(SqlContract.SqlOrders.CREATED_DATE,order.getCreatedDate().toString());
                contentValues.put(SqlContract.SqlOrders.UPDATED_DATE,order.getUpdatedDate().toString());
                contentValues.put(SqlContract.SqlOrders.TABLE_NO,order.getTableNo());
                contentValues.put(SqlContract.SqlOrders.USER_ID,order.getUserId());
                contentValues.put(SqlContract.SqlOrders.ORDER_AMOUNT,order.getOrderAmount());
                count=sqLiteDatabase.insert(SqlContract.SqlOrders.TABLE_NAME,null,contentValues);
                contentValues.clear();
                Log.d(TAG, "## Order is added successfully" +order.getOrderId());
            }

        }
        catch(Exception e)
        {
            Log.d(TAG,"## Error at insertOrders"+e.toString());
        }
        finally {
            {
                if(sqLiteDatabase!=null)
                    sqLiteDatabase.close();
            }
        }
        return count!=-1;
    }

    public boolean insertTableCategories(List<TableCategoryDbDTO> tableCategoryInsert) {
        SQLiteDatabase sqLiteDatabase=null;
        ContentValues contentValues=null;
        long count=-1;
        try{
            sqLiteDatabase=getWritableDatabase();
            contentValues=new ContentValues();
            for(TableCategoryDbDTO tableCategory:tableCategoryInsert)
            {
                contentValues.put(SqlContract.SqlTableCategory.TABLE_CATEGORY_ID,tableCategory.getTableCategoryId());
                contentValues.put(SqlContract.SqlTableCategory.CATEGORY_TITLE,tableCategory.getCategoryTitle());
                contentValues.put(SqlContract.SqlTableCategory.IMAGE,tableCategory.getImage());
                contentValues.put(SqlContract.SqlTableCategory.CREATED_DATE,tableCategory.getCreatedDate().toString());
                contentValues.put(SqlContract.SqlTableCategory.UPDATED_DATE,tableCategory.getUpdatedDate().toString());
                count=sqLiteDatabase.insert(SqlContract.SqlTableCategory.TABLE_NAME,null,contentValues);
                contentValues.clear();
                Log.d(TAG, "## Table Category is added successfully" + tableCategory.getTableCategoryId());

            }

        }
        catch(Exception e)
        {
            Log.d(TAG,"## Error at insertMenuCategory"+e.toString());
        }
        finally {
            {
                if(sqLiteDatabase!=null)
                    sqLiteDatabase.close();
            }
        }
        return count!=-1;
    }
}
