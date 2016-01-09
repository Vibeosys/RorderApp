package com.vibeosys.rorderapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vibeosys.rorderapp.data.Sync;
import com.vibeosys.rorderapp.util.SessionManager;

import java.util.ArrayList;

/**
 * Created by anand on 09/01/2016.
 */

public class DbRepository extends SQLiteOpenHelper {

    public DbRepository(Context context, SessionManager sessionManager) {
        super(context, sessionManager.getDatabaseDeviceFullPath(), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
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
            contentValues.put("UserId", userId);
            count = sqLiteDatabase.insert("MyUser", null, contentValues);
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
