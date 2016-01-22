package com.vibeosys.rorderapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vibeosys.rorderapp.data.HotelTableDTO;
import com.vibeosys.rorderapp.data.HotelTableDbDTO;
import com.vibeosys.rorderapp.data.Sync;
import com.vibeosys.rorderapp.data.UserDbDTO;
import com.vibeosys.rorderapp.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anand on 09/01/2016.
 */

public class DbRepository extends SQLiteOpenHelper {

    private final String TAG=DbRepository.class.getSimpleName();
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
        if (c.moveToFirst())
        {
            while ( !c.isAfterLast() ){
                dirArray.add(c.getString(c.getColumnIndex("name")));

                c.moveToNext();
            }
        }
        Log.i(TAG,"##"+dirArray);
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
                Log.d(TAG,"## User is Added Successfully");
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
                contentValues.put(SqlContract.SqlHotelTable.CREATED_DATE, dbTable.getCreatedDate());
                contentValues.put(SqlContract.SqlHotelTable.UPDATED_DATE, dbTable.getUpdatedDate());
                contentValues.put(SqlContract.SqlHotelTable.IS_OCCUPIED, dbTable.isOccupied());
                count = sqLiteDatabase.insert(SqlContract.SqlHotelTable.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG,"## Table is Added Successfully");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while adding Tables " + e.toString());
        } finally {
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return count != -1;
    }
    public int autheticateUser(String userName,String password) {
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        int userId=0;
        try {
            sqLiteDatabase = getReadableDatabase();
            String query = "SELECT * FROM "+SqlContract.SqlUser.TABLE_NAME+" WHERE "+SqlContract.SqlUser.USER_NAME
                    +"=? AND "+SqlContract.SqlUser.PASSWORD+"=? LIMIT 1";
            cursor = sqLiteDatabase.rawQuery(query, new String[]{userName,password});

            if (cursor != null) {
                if (cursor.getCount() > 0) {

                    cursor.moveToFirst();
                    userId=cursor.getInt(cursor.getColumnIndex(SqlContract.SqlUser.USER_ID));
                }
            }
        } catch (Exception e) {
            userId=0;
            Log.e(TAG, "Error occured in autheticate User function" + e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null)
                sqLiteDatabase.close();
        }
        return userId;
    }
    public ArrayList<HotelTableDTO> getTableRecords() {
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<HotelTableDTO> hotelTables = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+SqlContract.SqlHotelTable.TABLE_NAME, null);
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
}
