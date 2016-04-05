package com.vibeosys.rorderapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.google.gson.Gson;
import com.vibeosys.rorderapp.data.ApplicationErrorDBDTO;
import com.vibeosys.rorderapp.data.BillDbDTO;
import com.vibeosys.rorderapp.data.BillDetailsDTO;
import com.vibeosys.rorderapp.data.BillDetailsDbDTO;
import com.vibeosys.rorderapp.data.ChefMenuDetailsDTO;
import com.vibeosys.rorderapp.data.ChefOrderDetailsDTO;
import com.vibeosys.rorderapp.data.ConfigSettingsDbDTO;
import com.vibeosys.rorderapp.data.CustomerDbDTO;
import com.vibeosys.rorderapp.data.DeliveryDTO;
import com.vibeosys.rorderapp.data.DeliveryDbDTO;
import com.vibeosys.rorderapp.data.FeedBackDTO;
import com.vibeosys.rorderapp.data.HotelTableDbDTO;
import com.vibeosys.rorderapp.data.MenuCateoryDbDTO;
import com.vibeosys.rorderapp.data.MenuDbDTO;
import com.vibeosys.rorderapp.data.MenuTagsDbDTO;
import com.vibeosys.rorderapp.data.NoteDTO;
import com.vibeosys.rorderapp.data.OrderDetailsDTO;
import com.vibeosys.rorderapp.data.OrderDetailsDbDTO;
import com.vibeosys.rorderapp.data.OrderHeaderDTO;
import com.vibeosys.rorderapp.data.OrderMenuDTO;
import com.vibeosys.rorderapp.data.OrderTypeDbDTO;
import com.vibeosys.rorderapp.data.OrdersDbDTO;
import com.vibeosys.rorderapp.data.PaymentModeDbDTO;
import com.vibeosys.rorderapp.data.PermissionSetDbDTO;
import com.vibeosys.rorderapp.data.PrinterDetailsDTO;
import com.vibeosys.rorderapp.data.PrintersDbDTO;
import com.vibeosys.rorderapp.data.RestaurantDTO;
import com.vibeosys.rorderapp.data.RestaurantDbDTO;
import com.vibeosys.rorderapp.data.RestaurantTables;
import com.vibeosys.rorderapp.data.RoomPrintersDbDTO;
import com.vibeosys.rorderapp.data.RoomTypesDbDTO;
import com.vibeosys.rorderapp.data.RoomsDbDTO;
import com.vibeosys.rorderapp.data.SubMenuDTO;
import com.vibeosys.rorderapp.data.Sync;
import com.vibeosys.rorderapp.data.TableCategoryDTO;
import com.vibeosys.rorderapp.data.TableCategoryDbDTO;
import com.vibeosys.rorderapp.data.TableTransactionDbDTO;
import com.vibeosys.rorderapp.data.TakeAwayDTO;
import com.vibeosys.rorderapp.data.TakeAwayDbDTO;
import com.vibeosys.rorderapp.data.TakeAwaySourceDTO;
import com.vibeosys.rorderapp.data.TakeAwaySourceDbDTO;
import com.vibeosys.rorderapp.data.UploadDelivery;
import com.vibeosys.rorderapp.data.UploadTakeAway;
import com.vibeosys.rorderapp.data.UserDTO;
import com.vibeosys.rorderapp.data.UserDbDTO;
import com.vibeosys.rorderapp.data.WaitingUserDTO;
import com.vibeosys.rorderapp.util.AppConstants;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.ROrderDateUtils;
import com.vibeosys.rorderapp.util.SessionManager;

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
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<Sync> syncTableData = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("select * from Sync ", null);
                syncTableData = new ArrayList<>();
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            Sync sync = new Sync();
                            sync.setJsonSync(cursor.getString(cursor.getColumnIndex("JsonSync")));
                            sync.setTableName(cursor.getString(cursor.getColumnIndex("TableName")));
                            syncTableData.add(sync);
                        } while (cursor.moveToNext());

                    }
                }
                flagError = true;
            }


        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Sync Records", errorMessage);
        }
        return syncTableData;
    }

    public boolean clearSyncData() {
        SQLiteDatabase sqLiteDatabase = null;
        boolean flagError = false;
        String errorMessage = "";
        // ContentValues contentValues = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            synchronized (sqLiteDatabase) {
                count = sqLiteDatabase.delete("sync", null,
                        null);
                // contentValues.clear();
                //sqLiteDatabase.close();
                flagError = true;
                Log.d(TAG, " ## clear sync sucessfully");
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "## clear sync is not sucessfully" + e.toString());
            //sqLiteDatabase.close();

        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Clear Sync", errorMessage);
        }
        return count != -1;
    }

    public boolean addDataToSync(String tableName, String userId, String jsonSync) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long rowsAffected = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            contentValues = new ContentValues();
            contentValues.put("UserId", userId);
            contentValues.put("JsonSync", jsonSync);
            contentValues.put("TableName", tableName);
            if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
            rowsAffected = sqLiteDatabase.insert("Sync", null, contentValues);
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            Log.e("SyncInsert", "Error occurred while inserting in Sync table " + e.toString());
        } finally {
            if (contentValues != null)
                contentValues.clear();

            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Sync Entry", errorMessage);
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
            if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
            count = sqLiteDatabase.insert("my_users", null, contentValues);
        } catch (Exception e) {
            Log.e("DbOperationUserIns", "Error occurred while creating new user in db for " + userId + " " + e.toString());
        } finally {
            if (contentValues != null)
                contentValues.clear();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
        }

        return count != -1;
    }

    public boolean insertUsers(List<UserDbDTO> usersList) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (UserDbDTO dbUser : usersList) {
                    contentValues.put(SqlContract.SqlUser.USER_ID, dbUser.getUserId());
                    contentValues.put(SqlContract.SqlUser.USER_NAME, dbUser.getUserName());
                    contentValues.put(SqlContract.SqlUser.PASSWORD, dbUser.getPassword());
                    contentValues.put(SqlContract.SqlUser.ACTIVE, dbUser.isActive());
                    contentValues.put(SqlContract.SqlUser.ROLE_ID, dbUser.getRoleId());
                    contentValues.put(SqlContract.SqlUser.RESTAURANTID, dbUser.getRestaurantId());
                    contentValues.put(SqlContract.SqlUser.PERMISSIONS, dbUser.getPermissions());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlUser.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## User is Added Successfully");
                    flagError = true;
                }

            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while insert users " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert User", errorMessage);
        }
        return count != -1;
    }

    public boolean insertTables(List<HotelTableDbDTO> tableList) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (HotelTableDbDTO dbTable : tableList) {
                    contentValues.put(SqlContract.SqlHotelTable.TABLE_ID, dbTable.getTableId());
                    contentValues.put(SqlContract.SqlHotelTable.TABLE_NO, dbTable.getTableNo());
                    contentValues.put(SqlContract.SqlHotelTable.TABLE_CATEGORY, dbTable.getTableCategoryId());
                    contentValues.put(SqlContract.SqlHotelTable.CAPACITY, dbTable.getCapacity());
                    contentValues.put(SqlContract.SqlHotelTable.IS_OCCUPIED, dbTable.isOccupied());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlHotelTable.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Table is Added Successfully");
                    flagError = true;
                }
            }

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error while adding Tables " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Table", errorMessage);
        }
        return count != -1;
    }

    public boolean insertTakeAwaySource(List<TakeAwaySourceDbDTO> sourceList) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (TakeAwaySourceDbDTO sourceDbDTO : sourceList) {
                    contentValues.put(SqlContract.SqlTakeAwaySource.TAKE_AWAY_SOURCE_ID, sourceDbDTO.getSourceId());
                    contentValues.put(SqlContract.SqlTakeAwaySource.SOURCE_NAME, sourceDbDTO.getSourceName());
                    contentValues.put(SqlContract.SqlTakeAwaySource.SOURCE_URL, sourceDbDTO.getSourceImg());
                    contentValues.put(SqlContract.SqlTakeAwaySource.DISCOUNT, sourceDbDTO.getDiscount());
                    contentValues.put(SqlContract.SqlTakeAwaySource.ACTIVE, sourceDbDTO.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlTakeAwaySource.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Take Away source is Added Successfully");
                    flagError = true;
                }

            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while adding Take Away source " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Take Away", errorMessage);
        }
        return count != -1;
    }

    public UserDTO autheticateUser(String userName, String password, int roleId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        int userId = 0;
        String name = "";
        boolean active;
        int rollId;
        int restaurantId;
        String permission;
        UserDTO user = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String query = "SELECT * FROM " + SqlContract.SqlUser.TABLE_NAME + " WHERE " + SqlContract.SqlUser.USER_NAME
                        + "=? AND " + SqlContract.SqlUser.PASSWORD + "=? AND " + SqlContract.SqlUser.ROLE_ID + "=? LIMIT 1";
                cursor = sqLiteDatabase.rawQuery(query, new String[]{userName, password, String.valueOf(roleId)});

                if (cursor != null) {
                    if (cursor.getCount() > 0) {

                        cursor.moveToFirst();
                        userId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlUser.USER_ID));
                        name = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                        active = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.ACTIVE)));
                        rollId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlUser.ROLE_ID));
                        restaurantId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlUser.RESTAURANTID));
                        permission = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.PERMISSIONS));
                        user = new UserDTO(userId, name, active, rollId, restaurantId);
                        user.setmPermission(permission);
                    }
                }
                flagError = true;
            }

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            user = null;
            Log.e(TAG, "Error occured in autheticate User function" + e.toString());
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "User Authentication", errorMessage);
        }
        return user;
    }

    public ArrayList<RestaurantTables> getTableRecords(String where) {//changes
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<RestaurantTables> hotelTables = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                //  cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + SqlContract.SqlHotelTable.TABLE_NAME + " ORDER BY " + SqlContract.SqlHotelTable.TABLE_NO, null);
                cursor = sqLiteDatabase.rawQuery("select rs.TableId ,rs.TableNo,rs.TableCategoryId,CategoryTitle,Capacity,IsOccupied,us.UserName,tt.UserId" +
                        " From r_tables as rs LEFT Join table_category as tc  On rs.TableCategoryId = tc.TableCategoryId" +
                        " Left Join table_transaction as tt on rs.tableId=tt.tableId" +
                        " left join users as us on tt.userId=us.userId " + where +
                        " ORder by rs.TableNo ", null);
                Log.d(TAG, "## select rs.TableId ,rs.TableNo,rs.TableCategoryId,CategoryTitle,Capacity,IsOccupied,us.UserName,tt.UserId" +
                        " From r_tables as rs LEFT Join table_category as tc  On rs.TableCategoryId = tc.TableCategoryId" +
                        " Left Join table_transaction as tt on rs.tableId=tt.tableId" +
                        " left join users as us on tt.userId=us.userId " + where +
                        " ORder by rs.TableNo ");
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
                            int isOccupied = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.IS_OCCUPIED));
                            int userId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTableTransaction.USER_ID));
                            boolean occupied = isOccupied == 1 ? true : false;
                            String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                            RestaurantTables table = new RestaurantTables(tableId, tableNo, tableCtegory,
                                    tableCtegoryName, capacity, occupied, userName, userId);
                            //table.setJsonSync();
                            hotelTables.add(table);
                        } while (cursor.moveToNext());
                    }
                }
                flagError = true;
            }


        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Table Records", errorMessage);
        }
        return hotelTables;
    }


    public BillDetailsDTO getBillDetailsRecords(String custId)//08/02/2016 put where condition for table no and custer id and from sharder preference take user name ann user id
    {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        BillDetailsDTO billDetailsRecords = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] whereclause = new String[]{custId};
                cursor = sqLiteDatabase.rawQuery("select bill.BillNo,bill.BillDate,bill.NetAmount,bill.BillTime," +
                        "bill.TotalTaxAmount,bill.TotalPayAmount,bill.IsPayed,users.UserName,r_tables.TableNo" +
                        " from bill left Join users On users.UserId = bill.UserId" +
                        " Left join r_tables on bill.TableId=r_tables.TableId where bill.CustId=?", whereclause);

                if (cursor != null) {
                    if (cursor.getCount() > 0)

                    {
                        cursor.moveToFirst();
                        int billNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlBill.BILL_NO));
                        String billDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlBill.BILL_DATE));
                        double netAmount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlBill.NET_AMOUNT));
                        double totalTaxAmount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlBill.TOATL_TAX_AMT));
                        double totalPayAbleTax = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlBill.TOTAL_PAY_AMT));
                        String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                        int tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_NO));
                        String billTime = cursor.getString(cursor.getColumnIndex(SqlContract.SqlBill.BILL_TIME));
                        int isPaid = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlBill.IS_PAYED));
                        billDetailsRecords = new BillDetailsDTO(billNo, billDate, netAmount,
                                totalTaxAmount, totalPayAbleTax, userName, tableNo, billTime);
                        billDetailsRecords.setBillPayed(isPaid);
                    }

                }
                flagError = true;
            }

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Bill Details", errorMessage);
        }
        return billDetailsRecords;
    }

    public ArrayList<TableCategoryDTO> getTableCategories() {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<TableCategoryDTO> tableCategories = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
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
                            String url = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTableCategory.IMAGE));
                            TableCategoryDTO table = new TableCategoryDTO();
                            table.setmCategoryId(categoryId);
                            table.setmTitle(categoryTitle);
                            table.setmImage(url);
                            //table.setJsonSync();
                            tableCategories.add(table);
                        } while (cursor.moveToNext());
                    }
                }
                flagError = true;
            }


        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Table Categories", errorMessage);
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
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (BillDbDTO bill : billInserts) {
                    contentValues.put(SqlContract.SqlBill.BILL_NO, bill.getBillNo());
                    contentValues.put(SqlContract.SqlBill.BILL_DATE, String.valueOf(bill.getBillDate()));
                    contentValues.put(SqlContract.SqlBill.BILL_TIME, bill.getBillTime().toString());
                    contentValues.put(SqlContract.SqlBill.NET_AMOUNT, bill.getNetAmt());
                    contentValues.put(SqlContract.SqlBill.TOATL_TAX_AMT, bill.getTotalTaxAmt());
                    contentValues.put(SqlContract.SqlBill.TOTAL_PAY_AMT, bill.getTotalPayAmt());
                    contentValues.put(SqlContract.SqlBill.USER_ID, bill.getUserId());
                    contentValues.put(SqlContract.SqlBill.CUST_ID, bill.getCustId());
                    contentValues.put(SqlContract.SqlBill.TABLE_ID, bill.getTableId());
                    contentValues.put(SqlContract.SqlBill.IS_PAYED, bill.isPayed());
                    contentValues.put(SqlContract.SqlBill.PAID_BY, bill.getPayedBy());
                    contentValues.put(SqlContract.SqlBill.DISCOUNT, bill.getDiscount());
                    contentValues.put(SqlContract.SqlBill.TAKE_AWAY_NO, bill.getTakeawayNo());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlBill.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Bill is Added Successfully" + bill.getBillNo());
                }
                flagError = true;
            }

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error while adding Bills " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Add Bill", errorMessage);
        }
        return count != -1;
    }

    public boolean insertBillDetails(List<BillDetailsDbDTO> billDetailInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (BillDetailsDbDTO billDetails : billDetailInserts) {
                    contentValues.put(SqlContract.SqlBillDetails.AUTO_ID, billDetails.getAutoId());
                    contentValues.put(SqlContract.SqlBillDetails.ORDER_ID, billDetails.getOrderId());
                    contentValues.put(SqlContract.SqlBillDetails.BILL_NO, billDetails.getBillNo());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlBillDetails.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Bill Details is added successfully" + billDetails.getBillNo());
                }
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error While adding Bill details" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Add Bill Details", errorMessage);
        }
        return count != -1;
    }

    public boolean insertMenus(List<MenuDbDTO> menuInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (MenuDbDTO menu : menuInserts) {
                    contentValues.put(SqlContract.SqlMenu.MENU_ID, menu.getMenuId());
                    contentValues.put(SqlContract.SqlMenu.MENU_TITLE, menu.getMenuTitle());
                    contentValues.put(SqlContract.SqlMenu.IMAGE, menu.getImage());
                    contentValues.put(SqlContract.SqlMenu.PRICE, menu.getPrice());
                    contentValues.put(SqlContract.SqlMenu.INGREDIENTS, menu.getIngredients());
                    contentValues.put(SqlContract.SqlMenu.TAGS, menu.getTags());
                    contentValues.put(SqlContract.SqlMenu.AVAIL_STATUS, menu.getAvailabilityStatus());
                    contentValues.put(SqlContract.SqlMenu.ACTIVE, menu.getActive());
                    contentValues.put(SqlContract.SqlMenu.FOOD_TYPE, menu.getFoodType());
                    contentValues.put(SqlContract.SqlMenu.CATEGORY_ID, menu.getCategoryId());
                    contentValues.put(SqlContract.SqlMenu.IS_SPICY, menu.getIsSpicy());
                    contentValues.put(SqlContract.SqlMenu.ROOM_ID, menu.getRoomId());
                    contentValues.put(SqlContract.SqlMenu.FB_TYPE_ID, menu.getFbTypeId());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlMenu.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Menu is added successfully" + menu.getMenuId());
                }
                flagError = true;
            }

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "## Error at insertMenu" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Menu", errorMessage);
        }
        return count != -1;
    }

    public boolean insertMenuCategory(List<MenuCateoryDbDTO> menuCategoryInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (MenuCateoryDbDTO menuCateory : menuCategoryInserts) {
                    contentValues.put(SqlContract.SqlMenuCategory.CATEGORY_ID, menuCateory.getCategoryId());
                    contentValues.put(SqlContract.SqlMenuCategory.CATEGORY_TITLE, menuCateory.getCategoryTitle());
                    contentValues.put(SqlContract.SqlMenuCategory.CATEGORY_IMG, menuCateory.getCategoryImage());
                    contentValues.put(SqlContract.SqlMenuCategory.ACTIVE, menuCateory.isActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlMenuCategory.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Menu Category is added successfully" + menuCateory.getCategoryId());

                }
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at insertMenuCategory" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Menu Category", errorMessage);
        }
        return count != -1;
    }

    public boolean insertTakeAway(List<TakeAwayDbDTO> takeawayInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (TakeAwayDbDTO takeAwayDbDTO : takeawayInserts) {
                    contentValues.put(SqlContract.SqlTakeAway.TAKE_AWAY_ID, takeAwayDbDTO.getTakeawayId());
                    contentValues.put(SqlContract.SqlTakeAway.TAKE_AWAY_NO, takeAwayDbDTO.getTakeawayNo());
                    contentValues.put(SqlContract.SqlTakeAway.DISCOUNT, takeAwayDbDTO.getDiscount());
                    contentValues.put(SqlContract.SqlTakeAway.DELIVERY_CHG, takeAwayDbDTO.getDeliveryCharges());
                    contentValues.put(SqlContract.SqlTakeAway.CUST_ID, takeAwayDbDTO.getCustId());
                    contentValues.put(SqlContract.SqlTakeAway.USER_ID, takeAwayDbDTO.getUserId());
                    contentValues.put(SqlContract.SqlTakeAway.SOURCE_ID, takeAwayDbDTO.getSourceId());
                    contentValues.put(SqlContract.SqlTakeAway.DATE, takeAwayDbDTO.getCreatedDate());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlTakeAway.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Take away is added successfully" + takeAwayDbDTO.getTakeawayNo());

                }
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at insert take away" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Take Away", errorMessage);
        }
        return count != -1;
    }

    public boolean insertMenuTags(List<MenuTagsDbDTO> menuTagInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (MenuTagsDbDTO menuTag : menuTagInserts) {
                    contentValues.put(SqlContract.SqlMenuTags.TAG_ID, menuTag.getTagId());
                    contentValues.put(SqlContract.SqlMenuTags.TAG_TITLE, menuTag.getTagTitle());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlMenuTags.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Menu Tag is added successfully" + menuTag.getTagId());

                }
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at insertMenuTag" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Menu Tag", errorMessage);
        }
        return count != -1;
    }

    public boolean insertOrderType(List<OrderTypeDbDTO> menuTagInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (OrderTypeDbDTO orderTypeDbDTO : menuTagInserts) {
                    contentValues.put(SqlContract.SqlOrderType.ORDER_TYPE_ID, orderTypeDbDTO.getOrderTypeId());
                    contentValues.put(SqlContract.SqlOrderType.ORDER_TYPE_TITLE, orderTypeDbDTO.getOrderTypeTitle());
                    contentValues.put(SqlContract.SqlOrderType.ACTIVE, orderTypeDbDTO.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlOrderType.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Order Type is added successfully" + orderTypeDbDTO.getOrderTypeId());

                }
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at Order Type insert" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Order Type", errorMessage);
        }
        return count != -1;
    }

    public boolean insertOrderDetails(List<OrderDetailsDbDTO> orderDetailInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (OrderDetailsDbDTO orderDetail : orderDetailInserts) {
                    contentValues.put(SqlContract.SqlOrderDetails.ORDER_DETAILS_ID, orderDetail.getOrderDetailsId());
                    contentValues.put(SqlContract.SqlOrderDetails.ORDER_PRICE, orderDetail.getOrderPrice());
                    contentValues.put(SqlContract.SqlOrderDetails.ORDER_QUANTITY, orderDetail.getOrderQuantity());
                    contentValues.put(SqlContract.SqlOrderDetails.ORDER_ID, orderDetail.getOrderId());
                    contentValues.put(SqlContract.SqlOrderDetails.MENU_ID, orderDetail.getMenuId());
                    contentValues.put(SqlContract.SqlOrderDetails.SUB_MENU_ID, orderDetail.getSubMenuId());
                    contentValues.put(SqlContract.SqlOrderDetails.MENU_TITLE, orderDetail.getMenuTitle());
                    contentValues.put(SqlContract.SqlOrderDetails.NOTE, orderDetail.getNote());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlOrderDetails.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Order detail is added successfully" + orderDetail.getOrderDetailsId());

                }
                flagError = true;
            }

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at insertOrderDetails" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Order details", errorMessage);
        }
        return count != -1;
    }

    public boolean insertOrders(List<OrdersDbDTO> orderInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (OrdersDbDTO order : orderInserts) {
                    contentValues.put(SqlContract.SqlOrders.ORDER_ID, order.getOrderId());
                    contentValues.put(SqlContract.SqlOrders.ORDER_NO, order.getOrderNo());
                    contentValues.put(SqlContract.SqlOrders.ORDER_STATUS, order.isOrderStatus());
                    if (order.getOrderDt() != null)
                        contentValues.put(SqlContract.SqlOrders.ORDER_DATE, String.valueOf(order.getOrderDt()));
                    if (order.getOrderTm() != null)
                        contentValues.put(SqlContract.SqlOrders.ORDER_TIME, String.valueOf(order.getOrderTm()));
                    contentValues.put(SqlContract.SqlOrders.TABLE_NO, order.getTableId());
                    contentValues.put(SqlContract.SqlOrders.USER_ID, order.getUserId());
                    contentValues.put(SqlContract.SqlOrders.ORDER_AMOUNT, order.getOrderAmt());
                    contentValues.put(SqlContract.SqlOrders.CUST_ID, order.getCustId());
                    contentValues.put(SqlContract.SqlOrders.TAKE_AWAY_NO, order.getTakeawayNo());
                    contentValues.put(SqlContract.SqlOrders.ORDER_TYPE, order.getOrderType());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlOrders.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Order is added successfully" + order.getOrderId());
                }
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at insertOrders" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Orders", errorMessage);
        }
        return count != -1;
    }

    public boolean insertTableCategories(List<TableCategoryDbDTO> tableCategoryInsert) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (TableCategoryDbDTO tableCategory : tableCategoryInsert) {
                    contentValues.put(SqlContract.SqlTableCategory.TABLE_CATEGORY_ID, tableCategory.getTableCategoryId());
                    contentValues.put(SqlContract.SqlTableCategory.CATEGORY_TITLE, tableCategory.getCategoryTitle());
                    contentValues.put(SqlContract.SqlTableCategory.IMAGE, tableCategory.getImage());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlTableCategory.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Table Category is added successfully" + tableCategory.getTableCategoryId());

                }
                flagError = true;
            }

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at insertMenuCategory" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Menu Category", errorMessage);
        }
        return count != -1;
    }

    public ArrayList<OrderMenuDTO> getOrderMenu(String custId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        ArrayList<OrderMenuDTO> orderMenus = null;
        try {
            String[] whereClause = new String[]{custId};
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("SELECT Distinct menu.MenuId,menu.FoodType,menu.Image,menu.FbTypeId," +
                        "menu.MenuTitle,menu.Tags,menu.IsSpicy,menu_category.CategoryTitle," +
                        "menu.AvailabilityStatus,menu.Active,menu.Price ,(Select temp_order.Quantity " +
                        "from temp_order   where menu.MenuId=temp_order.MenuId and temp_order.CustId=?)" +
                        " as Quantity From menu Left Join menu_category on menu.CategoryId=" +
                        "menu_category.CategoryId left join temp_order on menu.MenuId=temp_order.MenuId " +
                        "where menu.Active=1", whereClause);
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
                            int quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                            int iAvail = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMenu.AVAIL_STATUS));
                            int fbType = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMenu.FB_TYPE_ID));
                            boolean isAvail = iAvail == 1 ? true : false;
                            OrderMenuDTO orderMenu = new OrderMenuDTO(menuId, menuTitle, menuImage, foodType, menuTags, menuCategory, menuPrice, quantity, OrderMenuDTO.SHOW, isSpicy, isAvail, fbType);
                            //table.setJsonSync();
                            orderMenus.add(orderMenu);
                        } while (cursor.moveToNext());
                    }
                }
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Order Menu", errorMessage);
        }
        return orderMenus;
    }

    public boolean insertOrUpdateTempOrder(int tableId, int tableNo, int menuId, int qty, String custId, String note, int subMenuId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        int rowCount = 0;
        long count = -1;
        try {
            String[] whereClause = new String[]{String.valueOf(tableId), String.valueOf(tableNo), String.valueOf(menuId), String.valueOf(subMenuId)};
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                Cursor cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlTempOrder.TABLE_NAME
                        + " Where " + SqlContract.SqlTempOrder.TABLE_ID + "=? AND " + SqlContract.SqlTempOrder.TABLE_NO
                        + "=? And " + SqlContract.SqlTempOrder.MENU_ID + "=? AND " + SqlContract.SqlTempOrder.SUB_MENU_ID + "=?", whereClause);
                rowCount = cursor.getCount();
                cursor.close();
                sqLiteDatabase.close();
            }

            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                ROrderDateUtils rOrderDateUtils = new ROrderDateUtils();
                contentValues.put(SqlContract.SqlTempOrder.TABLE_NO, tableNo);
                contentValues.put(SqlContract.SqlTempOrder.TABLE_ID, tableId);
                contentValues.put(SqlContract.SqlTempOrder.MENU_ID, menuId);
                contentValues.put(SqlContract.SqlTempOrder.QUANTITY, qty);
                contentValues.put(SqlContract.SqlTempOrder.CUST_ID, custId);
                contentValues.put(SqlContract.SqlTempOrder.ORDER_DATE, rOrderDateUtils.getGMTCurrentDate());
                contentValues.put(SqlContract.SqlTempOrder.ORDER_TIME, rOrderDateUtils.getGMTCurrentTime());
                contentValues.put(SqlContract.SqlTempOrder.ORDER_STATUS, 0);
                contentValues.put(SqlContract.SqlTempOrder.SUB_MENU_ID, subMenuId);
                contentValues.put(SqlContract.SqlTempOrder.NOTE, note);
                if (rowCount == 0 && qty != 0) {
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlTempOrder.TABLE_NAME, null, contentValues);
                } else if (qty == 0) {
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.delete(SqlContract.SqlTempOrder.TABLE_NAME,
                            SqlContract.SqlTempOrder.TABLE_ID + "=? AND " + SqlContract.SqlTempOrder.TABLE_NO
                                    + "=? And " + SqlContract.SqlTempOrder.MENU_ID + "=? AND " + SqlContract.SqlTempOrder.SUB_MENU_ID + "=?", whereClause);
                } else {
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlTempOrder.TABLE_NAME, contentValues,
                            SqlContract.SqlTempOrder.TABLE_ID + "=? AND " + SqlContract.SqlTempOrder.TABLE_NO
                                    + "=? And " + SqlContract.SqlTempOrder.MENU_ID + "=? AND " + SqlContract.SqlTempOrder.SUB_MENU_ID + "=?", whereClause);
                }

                flagError = true;
                contentValues.clear();
                //sqLiteDatabase.close();
                Log.i(TAG, "Values are inserted into TempTable" + rOrderDateUtils.getGMTCurrentDate() + "" + rOrderDateUtils.getGMTCurrentTime());
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "error at insertOrUpdateTempOrder");
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Or Update Temp Order", errorMessage);
        }

        return count != -1;
    }

    public boolean clearUpdateTempData(int tableId, int tableNo, String custId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        // ContentValues contentValues = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{String.valueOf(tableId), String.valueOf(tableNo), custId};
                count = sqLiteDatabase.delete(SqlContract.SqlTempOrder.TABLE_NAME,
                        SqlContract.SqlTempOrder.TABLE_ID + "=? AND " + SqlContract.SqlTempOrder.TABLE_NO
                                + "=? AND " + SqlContract.SqlTempOrder.CUST_ID + "=?",
                        whereClause);
                // contentValues.clear();
                //sqLiteDatabase.close();
                Log.d(TAG, " ## clear update sucessfully");
            }
            flagError = true;
        } catch (Exception e) {
            e.printStackTrace();
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## clear update is not sucessfully" + e.toString());
            //sqLiteDatabase.close();

        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Clear Update temp Data", errorMessage);
        }
        return count != -1;
    }

    public boolean setOccupied(boolean value, int tableId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                contentValues.put(SqlContract.SqlHotelTable.IS_OCCUPIED, value);
                String[] whereClause = new String[]{String.valueOf(tableId)};
                count = sqLiteDatabase.update(SqlContract.SqlHotelTable.TABLE_NAME, contentValues,
                        SqlContract.SqlHotelTable.TABLE_ID + "=?", whereClause);
                //sqLiteDatabase.close();
                Log.e(TAG, "##  Occupied sucessfully ");
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "## error at set Occupied " + e.toString());
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Set Occupy", errorMessage);
        }
        return count != -1;
    }


    public ArrayList<OrderHeaderDTO> getOrdersOfTable(int tableId, String custId) {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<OrderHeaderDTO> orders = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        String[] whereClause = new String[]{String.valueOf(tableId), custId};
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("select * from orders where " +
                        SqlContract.SqlOrders.TABLE_NO + "=? AND " + SqlContract.SqlOrders.CUST_ID + "=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();

                        do {
                            String orderId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_ID));
                            int orderNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_NO));
                            //boolean orderStatus=cursor.get;
                            //String orderDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_DATE));
                            //  String orderTime = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME));
                            //String createdDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.CREATED_DATE));
                            // String updatedDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.UPDATED_DATE));
                            int tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.TABLE_NO));
                            int userId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.USER_ID));
                            double orderAmount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_AMOUNT));
                            OrderHeaderDTO orderHeaderDTO = new OrderHeaderDTO(orderId,
                                    orderNo, true, tableNo,
                                    userId, orderAmount, false);
                            orders.add(orderHeaderDTO);
                        } while (cursor.moveToNext());
                    }
                }
                flagError = true;
            }
            //cursor.close();
            //sqLiteDatabase.close();

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getOrdersOf table " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Order", errorMessage);
        }
        return orders;
    }

    public void getOrederDetailsGroupByID(ArrayList<OrderHeaderDTO> orders) {
        boolean flagError = false;
        String errorMessage = "";
        HashMap<OrderHeaderDTO, List<OrderDetailsDTO>> hashMap = new HashMap<>();
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                for (OrderHeaderDTO order : orders) {
                    List<OrderDetailsDTO> orderDetailsList = new ArrayList<>();
                    String orderId = order.getOrderId();
                    String[] whereClause = new String[]{orderId};

                    cursor = sqLiteDatabase.rawQuery("Select * from order_details where order_details.OrderId=?", whereClause);
                    if (cursor != null) {
                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            order.setItemCount(cursor.getCount());
                            do {
                                int orderDetailsId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_DETAILS_ID));
                                double orderPrice = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_PRICE));
                                int orderQuantity = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_QUANTITY));
                                String myOrderId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_ID));
                                int menuId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.MENU_ID));
                                String menuTitle = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.MENU_TITLE));
                                String note = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.NOTE));
                                OrderDetailsDTO orderDetails = new OrderDetailsDTO(orderDetailsId,
                                        orderPrice, orderQuantity, myOrderId, menuId, menuTitle, 0, note);
                                orderDetailsList.add(orderDetails);
                            } while (cursor.moveToNext());
                        }
                    }

                    // cursor.close();
                    order.setOrderDetailsDTOs(orderDetailsList);
                    flagError = true;
                }
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "## error at getOrederDetailsGroupByID function");
        } finally {
            if (cursor != null)
                cursor.close();
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Order details Group by Id", errorMessage);
        }

    }

    public int getOccupiedTable() {
        boolean flagError = false;
        String errorMessage = "";
        int count = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlHotelTable.TABLE_NAME + " where IsOccupied = 1", null);
                count = cursor.getCount();
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "## error at getOccupied table" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Occupied", errorMessage);
        }
        return count;
    }

    public int getTakeAwayCount() {
        int count = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlOrders.TABLE_NAME + " where OrderType = 2 and OrderStatus =1", null);
                count = cursor.getCount();
            }
        } catch (Exception e) {
            Log.e(TAG, "## error at getTakeAwayCount table" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
        }
        return count;
    }

    public int getCompletedTakeAwayCount() {
        boolean flagError = false;
        String errorMessage = "";
        int count = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlOrders.TABLE_NAME + " where OrderType = 2 and OrderStatus = 2", null);
                count = cursor.getCount();
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "## error at getTakeAwayCount table" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Take away Complete count", errorMessage);
        }
        return count;
    }

    public int checkOrders() {
        boolean flagError = false;
        String errorMessage = "";
        int count = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlOrderDetails.TABLE_NAME, null);
                count = cursor.getCount();
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "## error at checkOrders" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Check Orders", errorMessage);
        }
        return count;
    }

    public OrderHeaderDTO getOrederDetailsFromTemp(int tableId, int userId, String custId) {

        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        OrderHeaderDTO orderHeaderDTO = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                List<OrderDetailsDTO> orderDetailsList = new ArrayList<>();
                String[] whereClause = new String[]{String.valueOf(tableId), custId};
                double orderAmount = 0;
                cursor = sqLiteDatabase.rawQuery("Select temp_order.TempOrderId,temp_order.Quantity,temp_order.Note," +
                        "temp_order.MenuId,temp_order.SubMenuId,temp_order.OrderDate,temp_order.OrderTime,sub_menu.SubMenuTitle," +
                        "menu.MenuTitle,menu.RoomId,menu.Price,sub_menu.Price as subPrice from temp_order left join menu on temp_order.MenuId=menu.MenuId " +
                        "left join sub_menu on sub_menu.SubMenuId=temp_order.SubMenuId " +
                        "where temp_order.MenuId=menu.MenuId and temp_order.TableId=? and temp_order.CustId=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            int orderDetailsTempId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTempOrder.TEMP_ORDER_ID));
                            double menuPrice = 0;
                            int orderQuantity = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTempOrder.QUANTITY));
                            //String myOrderId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_ID));
                            int menuId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTempOrder.MENU_ID));
                            int subMenuId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTempOrder.SUB_MENU_ID));
                            String menuTitle = cursor.getString(cursor.getColumnIndex(SqlContract.SqlMenu.MENU_TITLE));
                            if (subMenuId != 0) {
                                menuTitle = menuTitle + " " + cursor.getString(cursor.getColumnIndex(SqlContract.SqlSubMenu.MENU_TITLE));
                                menuPrice = cursor.getDouble(cursor.getColumnIndex("subPrice"));
                            } else {
                                menuPrice = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlMenu.PRICE));
                            }
                            String note = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTempOrder.NOTE));
                            double orderPice = menuPrice * orderQuantity;
                            orderAmount = orderAmount + orderPice;
                            int roomId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMenu.ROOM_ID));
                            OrderDetailsDTO
                                    orderDetails = new OrderDetailsDTO(orderDetailsTempId, orderPice
                                    , orderQuantity, "", menuId, menuTitle, menuPrice, note);
                            orderDetails.setRoomId(roomId);
                            orderDetails.setmSubMenuId(subMenuId);
                            orderDetailsList.add(orderDetails);
                        } while (cursor.moveToNext());
                    }
                }
                orderHeaderDTO = new OrderHeaderDTO(tableId, userId, orderAmount, orderDetailsList.size(), true, orderDetailsList);
                //cursor.close();
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Order Details from temp", errorMessage);
        }
        return orderHeaderDTO;
    }

    public boolean insertCustomerDetails(CustomerDbDTO customer) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                contentValues.put(SqlContract.SqlCustomer.CUST_ID, customer.getCustId());
                contentValues.put(SqlContract.SqlCustomer.CUST_NAME, customer.getCustName());
                contentValues.put(SqlContract.SqlCustomer.CUST_PHONE, customer.getCustPhone());
                contentValues.put(SqlContract.SqlCustomer.CUST_ADDRESS, customer.getCustAddress());
                if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                count = sqLiteDatabase.insert(SqlContract.SqlCustomer.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "##Customer is added successfully" + customer.getCustId());
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at Customer" + e.toString());
        } finally {
            {
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                    sqLiteDatabase.close();
            }
            if (!flagError)
                addError(TAG, "Insert Customer", errorMessage);
        }
        return count != -1;
    }

    public boolean insertTableTransaction(TableTransactionDbDTO tableTransaction) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {


                contentValues = new ContentValues();
                contentValues.put(SqlContract.SqlTableTransaction.TABLE_ID, tableTransaction.getTableId());
                contentValues.put(SqlContract.SqlTableTransaction.USER_ID, tableTransaction.getUserId());
                contentValues.put(SqlContract.SqlTableTransaction.CUST_ID, tableTransaction.getCustId());
                contentValues.put(SqlContract.SqlTableTransaction.IS_WAIT, tableTransaction.isWaiting());
                //contentValues.put(SqlContract.SqlTableTransaction.ARRIVAL_TIME, String.valueOf(tableTransaction.getArrivalTime()));
                contentValues.put(SqlContract.SqlTableTransaction.OCCUPANCY, tableTransaction.getOccupancy());
                if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                count = sqLiteDatabase.insert(SqlContract.SqlTableTransaction.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "##Table Transaction is added successfully" + tableTransaction.getCustId());
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at Table Transaction" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Table Transaction", errorMessage);
        }
        return count != -1;
    }

    public ArrayList<WaitingUserDTO> getWaitingList() {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<WaitingUserDTO> waitingList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select table_transaction.CustId," +
                                "table_transaction.Occupancy,table_transaction.ArrivalTime," +
                                "customer.CustName from table_transaction left join customer where " +
                                "table_transaction.CustId=customer.CustId and table_transaction.IsWaiting=1",
                        null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();

                        do {
                            String mCustomerId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTableTransaction.CUST_ID));
                            int mOccupancy = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTableTransaction.OCCUPANCY));
                            // Date mArrivalTime = Date.valueOf(cursor.getString(cursor.getColumnIndex(SqlContract.SqlTableTransaction.ARRIVAL_TIME)));
                            String mCustomerName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlCustomer.CUST_NAME));

                            WaitingUserDTO waiting = new WaitingUserDTO(mCustomerId, mOccupancy, mCustomerName);
                            waitingList.add(waiting);
                        } while (cursor.moveToNext());
                    }
                }
                flagError = true;
            }
            //cursor.close();
            //sqLiteDatabase.close();

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getWaitingList table " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Waiting List", errorMessage);
        }
        return waitingList;
    }

    public boolean updateTableTransaction(TableTransactionDbDTO tableTransaction) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        String[] whereClause = new String[]{tableTransaction.getCustId()};
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                contentValues.put(SqlContract.SqlTableTransaction.TABLE_ID, tableTransaction.getTableId());
                contentValues.put(SqlContract.SqlTableTransaction.USER_ID, tableTransaction.getUserId());
                contentValues.put(SqlContract.SqlTableTransaction.IS_WAIT, tableTransaction.isWaiting());
                if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                count = sqLiteDatabase.update(SqlContract.SqlTableTransaction.TABLE_NAME, contentValues,
                        SqlContract.SqlTableTransaction.CUST_ID + "=?", whereClause);
                contentValues.clear();
                Log.d(TAG, "##Table Transaction is Updated successfully" + tableTransaction.getCustId());
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at Table Transaction" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Table Trasaction", errorMessage);
        }
        return count != -1;
    }

    public int getTaleId(int tableNo) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        String[] whereClause = new String[]{String.valueOf(tableNo)};
        Cursor cursor = null;
        int tableId = 0;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select r_tables.TableId,r_tables.IsOccupied from " +
                        "r_tables where r_tables.TableNo=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        int iOccupied = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.IS_OCCUPIED));

                        tableId = iOccupied == 0 ? cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_ID)) : -1;
                    }
                }
                flagError = true;
            }
            //cursor.close();
            //sqLiteDatabase.close();

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getTableId function " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get TableId", errorMessage);
        }
        return tableId;
    }

    /*
            * clearTableTransaction(int custId,int tableId) this function clears the table_transaction from sqlite
    * */
    public boolean clearTableTransaction(String custId, int tableId) {
        String errorMessage = "";
        boolean flagError = false;
        SQLiteDatabase sqLiteDatabase = null;
        //ContentValues contentValues = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;
        try {
            synchronized (sqLiteDatabase) {
                String[] whereClasuse = new String[]{(custId), String.valueOf(tableId)};
                if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                count = sqLiteDatabase.delete(SqlContract.SqlTableTransaction.TABLE_NAME,
                        SqlContract.SqlTableTransaction.CUST_ID + "=? AND "
                                + SqlContract.SqlTableTransaction.TABLE_ID + "=?", whereClasuse);
//                contentValues.clear();
                // sqLiteDatabase.close();
                Log.d(TAG, "## Data deledted from transcation table");
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            // sqLiteDatabase.close();
            Log.d(TAG, "## Data not deledted from transcation table");
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Clear Transaction", errorMessage);
        }
        return count != -1;
    }

    /*
    getCustmerIdFromTransaction(int tableId,int userId) this function
    returns custmerId from table transaction 

    * */
    public String getCustmerIdFromTransaction(int tableId) {
        boolean flagError = false;
        String errorMessage = "";
        String custId = "";
        SQLiteDatabase sqLiteDatabase = null;
        String[] whereClasuse = new String[]{String.valueOf(tableId)};
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("select table_transaction.CustId from table_transaction " +
                        "where table_transaction.TableId=?", whereClasuse);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        custId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTableTransaction.CUST_ID));
                    }
                }
            }
            flagError = true;
            // cursor.close();
            // sqLiteDatabase.close();
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (cursor != null)
                cursor.close();
            if (!flagError)
                addError(TAG, "Get CustomerId", errorMessage);

        }
        return custId;

    }

    public String getPassword(int userId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        String password = "@password";
        String whereClause[] = new String[]{String.valueOf(userId)};
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select Password from users where UserId=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        password = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.PASSWORD));
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (cursor != null)
                cursor.close();
            if (!flagError)
                addError(TAG, "Get Password", errorMessage);
        }
        return password;
    }

    public String getRestaurantName(int restaurantId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        String restaurantName = "";
        String whereClause[] = new String[]{String.valueOf(restaurantId)};
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("select restaurant.RestaurantTitle from restaurant where restaurant.RestaurantId =?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        restaurantName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRestaurant.RESTAURANT_NAME));
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (cursor != null)
                cursor.close();
            if (!flagError)
                addError(TAG, "Get Restaurant Name", errorMessage);
        }
        return restaurantName;
    }

    public String getRestaurantUrl(int restaurantId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        String restaurantUrl = "";
        String whereClause[] = new String[]{String.valueOf(restaurantId)};
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("select restaurant.LogoUrl from restaurant where restaurant.RestaurantId =?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        restaurantUrl = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRestaurant.RESTAURANT_URL));
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (cursor != null)
                cursor.close();
            if (!flagError)
                addError(TAG, "Get Restaurant Url", errorMessage);
        }
        return restaurantUrl;
    }

    public int checkTempOrderDetails(String custId, int tableId) {
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;

        int count = -1;

        String whereClause[] = new String[]{custId, String.valueOf(tableId)};
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("select temp_order.MenuId from temp_order where " +
                        "temp_order.CustId =? and temp_order.TableId =?", whereClause);
                count = cursor.getCount();
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (cursor != null)
                cursor.close();
        }
        return count;

    }

    public int getCustmerCount(String custId) {
        int count = -1;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        String whereCondition[] = new String[]{custId};
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("select orders.CustId from orders where orders.CustId=?", whereCondition);
                count = cursor.getCount();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (cursor != null)
                cursor.close();
        }

        return count;
    }

    public boolean updateBills(List<BillDbDTO> billUpdates) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (BillDbDTO bill : billUpdates) {
                    String[] whereClause = new String[]{String.valueOf(bill.getBillNo())};
                    if (bill.getBillDate() != null)
                        contentValues.put(SqlContract.SqlBill.BILL_DATE, bill.getBillDate().toString());
                    if (bill.getBillTime() != null)
                        contentValues.put(SqlContract.SqlBill.BILL_TIME, bill.getBillTime().toString());
                    if (bill.getNetAmt() != 0)
                        contentValues.put(SqlContract.SqlBill.NET_AMOUNT, bill.getNetAmt());
                    if (bill.getTotalTaxAmt() != 0)
                        contentValues.put(SqlContract.SqlBill.TOATL_TAX_AMT, bill.getTotalTaxAmt());
                    if (bill.getTotalPayAmt() != 0)
                        contentValues.put(SqlContract.SqlBill.TOTAL_PAY_AMT, bill.getTotalPayAmt());
                    if (bill.getUserId() != 0)
                        contentValues.put(SqlContract.SqlBill.USER_ID, bill.getUserId());
                    if (bill.isPayed() != 0)
                        contentValues.put(SqlContract.SqlBill.IS_PAYED, bill.isPayed());
                    if (bill.getPayedBy() != 0)
                        contentValues.put(SqlContract.SqlBill.PAID_BY, bill.getPayedBy());
                    if (bill.getDiscount() != 0)
                        contentValues.put(SqlContract.SqlBill.DISCOUNT, bill.getDiscount());
                    if (bill.getTakeawayNo() != 0)
                        contentValues.put(SqlContract.SqlBill.TAKE_AWAY_NO, bill.getTakeawayNo());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    if (contentValues.size() != 0)
                        count = sqLiteDatabase.update(SqlContract.SqlBill.TABLE_NAME, contentValues, SqlContract.SqlBill.BILL_NO + "=?", whereClause);

                    Log.d(TAG, "## Bill is Updated Successfully" + bill.getBillNo());
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.e(TAG, "Error while updating Bills " + e.toString());
        } finally {
            if (contentValues != null) {
                contentValues.clear();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Bill", errorMessage);
        }
        return count != -1;
    }

    public boolean updateOrderType(List<OrderTypeDbDTO> menuTagInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (OrderTypeDbDTO orderTypeDbDTO : menuTagInserts) {
                    String[] where = new String[]{String.valueOf(orderTypeDbDTO.getOrderTypeId())};
                    if (orderTypeDbDTO.getOrderTypeTitle() != null && !orderTypeDbDTO.getOrderTypeTitle().isEmpty())
                        contentValues.put(SqlContract.SqlOrderType.ORDER_TYPE_TITLE, orderTypeDbDTO.getOrderTypeTitle());
                    contentValues.put(SqlContract.SqlOrderType.ACTIVE, orderTypeDbDTO.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlOrderType.TABLE_NAME, contentValues, SqlContract.SqlOrderType.ORDER_TYPE_ID + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "## Order Type is added successfully" + orderTypeDbDTO.getOrderTypeId());

                }
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at Order Type insert" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Order Type Insert", errorMessage);
        }
        return count != -1;
    }

    public boolean updateBillDetails(List<BillDetailsDbDTO> billDetailUpdates) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (BillDetailsDbDTO billDetails : billDetailUpdates) {
                    String[] whereClause = new String[]{String.valueOf(billDetails.getAutoId())};
                    if (billDetails.getOrderId() != null && billDetails.getOrderId().isEmpty())
                        contentValues.put(SqlContract.SqlBillDetails.ORDER_ID, billDetails.getOrderId());
                    if (billDetails.getBillNo() != 0)
                        contentValues.put(SqlContract.SqlBillDetails.BILL_NO, billDetails.getBillNo());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlBillDetails.TABLE_NAME, contentValues, SqlContract.SqlBillDetails.AUTO_ID + "=?", whereClause);
                    contentValues.clear();
                    Log.d(TAG, "## Bill Details is Updated successfully" + billDetails.getBillNo());
                }
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error While updating Bill details" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Updating bill Details", errorMessage);
        }
        return count != -1;
    }

    public boolean updateOrders(List<OrdersDbDTO> orderUpdates) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (OrdersDbDTO order : orderUpdates) {
                    String[] whereClause = new String[]{order.getOrderId()};
                    if (order.getOrderNo() != 0)
                        contentValues.put(SqlContract.SqlOrders.ORDER_NO, order.getOrderNo());
                    if (order.isOrderStatus() != 0)
                        contentValues.put(SqlContract.SqlOrders.ORDER_STATUS, order.isOrderStatus());
                    if (order.getOrderDt() != null)
                        contentValues.put(SqlContract.SqlOrders.ORDER_DATE, String.valueOf(order.getOrderDt()));
                    if (order.getOrderTime() != null)
                        contentValues.put(SqlContract.SqlOrders.ORDER_TIME, String.valueOf(order.getOrderTm()));
               /* if (order.getCreatedDate() != null)
                    contentValues.put(SqlContract.SqlOrders.CREATED_DATE, String.valueOf(order.getCreatedDate()));
                if (order.getUpdatedDate() != null)
                    contentValues.put(SqlContract.SqlOrders.UPDATED_DATE, String.valueOf(order.getUpdatedDate()));*/
                    if (order.getTableId() != 0)
                        contentValues.put(SqlContract.SqlOrders.TABLE_NO, order.getTableId());
                    if (order.getUserId() != 0)
                        contentValues.put(SqlContract.SqlOrders.USER_ID, order.getUserId());
                    if (order.getOrderAmt() != 0.0)
                        contentValues.put(SqlContract.SqlOrders.ORDER_AMOUNT, order.getOrderAmt());
                    if (order.getCustId() != null && !order.getCustId().isEmpty())
                        contentValues.put(SqlContract.SqlOrders.CUST_ID, order.getCustId());
                    if (order.getTakeawayNo() != 0)
                        contentValues.put(SqlContract.SqlOrders.TAKE_AWAY_NO, order.getTakeawayNo());
                    if (order.getOrderType() != 0)
                        contentValues.put(SqlContract.SqlOrders.ORDER_TYPE, order.getOrderType());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlOrders.TABLE_NAME, contentValues,
                            SqlContract.SqlOrders.ORDER_ID + "=?", whereClause);
                    contentValues.clear();
                    Log.d(TAG, "## Order is update successfully" + order.getOrderId());
                }
                flagError = true;
            }

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at update Orders" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Order", errorMessage);
        }
        return count != -1;
    }

    public boolean updateOrderDetails(List<OrderDetailsDbDTO> orderDetailUpdates) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (OrderDetailsDbDTO orderDetail : orderDetailUpdates) {
                    String[] whereClause = new String[]{orderDetail.getOrderId()};
                    if (orderDetail.getOrderPrice() != 0.0)
                        contentValues.put(SqlContract.SqlOrderDetails.ORDER_PRICE, orderDetail.getOrderPrice());
                    if (orderDetail.getOrderQuantity() != 0)
                        contentValues.put(SqlContract.SqlOrderDetails.ORDER_QUANTITY, orderDetail.getOrderQuantity());
                    if (!orderDetail.getOrderId().isEmpty() && orderDetail.getOrderId() != null)
                        contentValues.put(SqlContract.SqlOrderDetails.ORDER_ID, orderDetail.getOrderId());
                    if (orderDetail.getMenuId() != 0)
                        contentValues.put(SqlContract.SqlOrderDetails.MENU_ID, orderDetail.getMenuId());
                    if (orderDetail.getMenuTitle() != null && !orderDetail.getMenuTitle().isEmpty())
                        contentValues.put(SqlContract.SqlOrderDetails.MENU_TITLE, orderDetail.getMenuTitle());
                    if (orderDetail.getSubMenuId() != 0)
                        contentValues.put(SqlContract.SqlOrderDetails.SUB_MENU_ID, orderDetail.getSubMenuId());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlOrderDetails.TABLE_NAME, contentValues,
                            SqlContract.SqlOrderDetails.ORDER_ID + "=?", whereClause);
                    contentValues.clear();
                    Log.d(TAG, "## Order detail is Updated successfully" + orderDetail.getOrderDetailsId());
                }
            }
            flagError = true;

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at updatedOrderDetails" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Order Details", errorMessage);
        }
        return count != -1;
    }

    public boolean updateTakeAwaySource(List<TakeAwaySourceDbDTO> sourceList) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (TakeAwaySourceDbDTO sourceDbDTO : sourceList) {
                    String[] where = new String[]{String.valueOf(sourceDbDTO.getSourceId())};
                    if (sourceDbDTO.getSourceName() != null && !sourceDbDTO.getSourceName().isEmpty())
                        contentValues.put(SqlContract.SqlTakeAwaySource.SOURCE_NAME, sourceDbDTO.getSourceName());
                    if (!sourceDbDTO.getSourceImg().isEmpty() && sourceDbDTO.getSourceImg() != null)
                        contentValues.put(SqlContract.SqlTakeAwaySource.SOURCE_URL, sourceDbDTO.getSourceImg());
                    if (sourceDbDTO.getDiscount() != 0)
                        contentValues.put(SqlContract.SqlTakeAwaySource.DISCOUNT, sourceDbDTO.getDiscount());
                    contentValues.put(SqlContract.SqlTakeAwaySource.ACTIVE, sourceDbDTO.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlTakeAwaySource.TABLE_NAME, contentValues,
                            SqlContract.SqlTakeAwaySource.TAKE_AWAY_SOURCE_ID + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "## Take Away source is Added Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while adding Take Away source " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, " Take away Source", errorMessage);
        }
        return count != -1;
    }

    /*chef order header function*/
    public ArrayList<ChefOrderDetailsDTO> getOrderHeadesInAsc(int status) {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<ChefOrderDetailsDTO> AscindingOrdres = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] where = new String[]{String.valueOf(status)};
//            cursor = sqLiteDatabase.rawQuery("select orders.OrderId,orders.CustId,orders.OrderStatus," +
//                    "orders.OrderNo,orders.TableNo,orders.OrderTime,users.UserName," +
//                    "r_tables.TableNo,order_details.Note \n" +
//                    "from  orders\n" +
//                    " left join users on orders.UserId =users.UserId\n" +
//                    " left join r_tables on r_tables.TableId = orders.TableNo \n" +
//                    " left join order_details on  orders.OrderId = order_details.OrderId\n" +
//                    " where orders.OrderStatus=? order by  orders.OrderTime Asc     ", where);

                cursor = sqLiteDatabase.rawQuery("select orders.TakeawayNo,orders.OrderType,orders.OrderId,orders.CustId,orders.OrderStatus," +
                        "orders.OrderNo,orders.TableNo,orders.OrderTime,orders.Orderdate,users.UserName,r_tables.TableNo " +
                        "from  orders left join users on orders.UserId =users.UserId left join r_tables " +
                        "on r_tables.TableId = orders.TableNo where orders.OrderStatus=? order by  " +
                        "orders.OrderTime Asc    ", where);
                //cursor = sqLiteDatabase.rawQuery("select orders.OrderId,orders.CustId,orders.OrderStatus,orders.TableNo,orders.OrderTime from  orders where orders.OrderStatus=1 order by  orders.OrderTime Asc ", null);

                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {


                            String orderId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_ID));
                            int tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_NO));
                            String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                            int orderNumber = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_NO));
                            int orderStatus = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_STATUS));
                            String orderDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_DATE));
                            String orderTime = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME));
                            // Time orderTime = Time.valueOf(cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME)));
                            int takeAwayNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.TAKE_AWAY_NO));
                            int orderType = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TYPE));
                            ChefOrderDetailsDTO chefOrderDetailsDTO = new ChefOrderDetailsDTO(orderId, tableNo, userName, orderNumber, orderStatus, orderDate, orderTime, takeAwayNo, orderType);
                            AscindingOrdres.add(chefOrderDetailsDTO);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
            //cursor.close();
            //sqLiteDatabase.close();
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();

            Log.e(TAG, "Error in ChefDb Headre" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Chef Header", errorMessage);

        }
        return AscindingOrdres;
    }

    public ArrayList<ChefOrderDetailsDTO> getRecordChefDining() {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<ChefOrderDetailsDTO> ordres = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] where = new String[]{"1"};

                cursor = sqLiteDatabase.rawQuery("select orders.TakeawayNo,orders.OrderType,orders.OrderId,orders.CustId,orders.OrderStatus,orders.OrderNo,orders.TableNo," +
                        "orders.OrderTime,orders.Orderdate,users.UserName,r_tables.TableNo " +
                        "from  orders left join users on orders.UserId =users.UserId left join r_tables " +
                        "on r_tables.TableId = orders.TableNo where orders.OrderStatus=1 and orders.OrderType=1  order by orders.OrderTime Asc", null);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            String orderId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_ID));
                            int tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_NO));
                            String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                            int orderNumber = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_NO));
                            int orderStatus = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_STATUS));
                            String orderDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_DATE));
                            String orderTime = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME));
                            int takeAwayNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.TAKE_AWAY_NO));
                            int orderType = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TYPE));
                            // Time orderTime = Time.valueOf(cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME)));
                            ChefOrderDetailsDTO chefOrderDetailsDTO = new ChefOrderDetailsDTO(orderId, tableNo, userName, orderNumber, orderStatus, orderDate, orderTime, takeAwayNo, orderType);
                            ordres.add(chefOrderDetailsDTO);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            e.printStackTrace();
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error in ChefDb Headre" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Chef Dine In records", errorMessage);

        }
        return ordres;
    }

    public ArrayList<ChefMenuDetailsDTO> getChefMenu(String orderId) {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<ChefMenuDetailsDTO> menudetails = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{String.valueOf(orderId)};
                cursor = sqLiteDatabase.rawQuery("select order_details.OrderId,orders.OrderId,order_details.MenuId,menu.MenuTitle,order_details.OrderQuantity,order_details.Note from orders inner join order_details on order_details.OrderId= orders.OrderId  inner join  menu on menu.MenuId = order_details.MenuId where order_details.OrderId =?", whereClause);

                //   cursor = sqLiteDatabase.rawQuery("select order_details.OrderId,orders.OrderId,order_details.MenuId,menu.MenuTitle,order_details.OrderQuantity from orders inner join order_details on order_details.OrderId= orders.OrderId  inner join  menu on menu.MenuId = order_details.MenuId where order_details.OrderId =?", whereClause);
                //cursor = sqLiteDatabase.rawQuery("select orders.OrderId,orders.CustId,orders.OrderStatus,orders.TableNo,orders.OrderTime from  orders where orders.OrderStatus=1 order by  orders.OrderTime Asc ", null);

                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {


                            int mMenuQty = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_QUANTITY));
                            int mMenuId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.MENU_ID));
                            String mMenuName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlMenu.MENU_TITLE));
                            // Time orderTime = Time.valueOf(cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME)));
                            String mMenuNote = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.NOTE));
                            ChefMenuDetailsDTO chefMenuDetailsDTO = new ChefMenuDetailsDTO(mMenuId, mMenuName, mMenuQty, mMenuNote);
                            menudetails.add(chefMenuDetailsDTO);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
            // cursor.close();
            //sqLiteDatabase.close();
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();

            Log.e(TAG, "Error in ChefDb MenuLog" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Chef Menu", errorMessage);

        }
        return menudetails;
    }

    public boolean insertListCustomerDetails(List<CustomerDbDTO> customer) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                for (CustomerDbDTO customerDbDTO : customer) {
                    contentValues = new ContentValues();
                    contentValues.put(SqlContract.SqlCustomer.CUST_ID, customerDbDTO.getCustId());
                    contentValues.put(SqlContract.SqlCustomer.CUST_NAME, customerDbDTO.getCustName());
                    contentValues.put(SqlContract.SqlCustomer.CUST_ADDRESS, customerDbDTO.getCustAddress());
                    contentValues.put(SqlContract.SqlCustomer.CUST_PHONE, customerDbDTO.getCustPhone());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlCustomer.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "##Customer is added successfully" + customerDbDTO.getCustId());
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at Customer" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Customer List", errorMessage);
        }
        return count != -1;
    }

    public boolean updateCustomer(List<CustomerDbDTO> customerUpdates) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                for (CustomerDbDTO customerDbDTO : customerUpdates) {
                    String[] where = new String[]{customerDbDTO.getCustId()};
                    contentValues = new ContentValues();
                    if (customerDbDTO.getCustName() != null)
                        contentValues.put(SqlContract.SqlCustomer.CUST_NAME, customerDbDTO.getCustName());
                    if (customerDbDTO.getCustAddress() != null)
                        contentValues.put(SqlContract.SqlCustomer.CUST_ADDRESS, customerDbDTO.getCustAddress());
                    if (customerDbDTO.getCustPhone() != null)
                        contentValues.put(SqlContract.SqlCustomer.CUST_PHONE, customerDbDTO.getCustPhone());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlCustomer.TABLE_NAME, contentValues,
                            SqlContract.SqlCustomer.CUST_ID + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "##Customer is Updated successfully" + customerDbDTO.getCustId());
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at Customer" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Customer", errorMessage);
        }
        return count != -1;
    }

    public boolean insertTableTransactionList(List<TableTransactionDbDTO> tableTransactionInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                for (TableTransactionDbDTO tableTransaction : tableTransactionInserts) {
                    contentValues = new ContentValues();
                    contentValues.put(SqlContract.SqlTableTransaction.TABLE_ID, tableTransaction.getTableId());
                    contentValues.put(SqlContract.SqlTableTransaction.USER_ID, tableTransaction.getUserId());
                    contentValues.put(SqlContract.SqlTableTransaction.CUST_ID, tableTransaction.getCustId());
                    contentValues.put(SqlContract.SqlTableTransaction.IS_WAIT, tableTransaction.isWaiting());
                    contentValues.put(SqlContract.SqlTableTransaction.ARRIVAL_TIME, String.valueOf(tableTransaction.getArrivalTime()));
                    contentValues.put(SqlContract.SqlTableTransaction.OCCUPANCY, tableTransaction.getOccupancy());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlTableTransaction.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "##Table Transaction is added successfully" + tableTransaction.getCustId());
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at Table Transaction" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Table Transaction", errorMessage);
        }
        return count != -1;
    }

    public boolean updateTableTransactionList(List<TableTransactionDbDTO> tableTransactionUpdates) {
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        boolean flagError = false;
        String errorMessage = "";
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                for (TableTransactionDbDTO tableTransaction : tableTransactionUpdates) {
                    String[] whereClause = new String[]{tableTransaction.getCustId()};

                    contentValues = new ContentValues();
                    if (tableTransaction.getTableId() != 0)
                        contentValues.put(SqlContract.SqlTableTransaction.TABLE_ID, tableTransaction.getTableId());
                    if (tableTransaction.getUserId() != 0)
                        contentValues.put(SqlContract.SqlTableTransaction.USER_ID, tableTransaction.getUserId());
                    contentValues.put(SqlContract.SqlTableTransaction.IS_WAIT, tableTransaction.isWaiting());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlTableTransaction.TABLE_NAME, contentValues,
                            SqlContract.SqlTableTransaction.CUST_ID + "=?", whereClause);
                    contentValues.clear();
                    Log.d(TAG, "##Table Transaction is Updated successfully" + tableTransaction.getCustId());
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at Table Transaction" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Table Transaction", errorMessage);
        }
        return count != -1;
    }

    public ArrayList<PaymentModeDbDTO> getPaymentList() {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<PaymentModeDbDTO> paymentModeList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlPaymentMode.TABLE_NAME,
                        null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();

                        do {
                            int paymentModeId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlPaymentMode.PAYMENT_MODE_ID));
                            String paymentModeTitle = cursor.getString(cursor.getColumnIndex(SqlContract.SqlPaymentMode.PAYMENT_MODE_TITLE));
                            int isactive = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlPaymentMode.ACTIVE));
                            boolean active = isactive == 0 ? false : true;
                            PaymentModeDbDTO paymentModeDbDTO = new PaymentModeDbDTO(paymentModeId, paymentModeTitle, active);
                            paymentModeList.add(paymentModeDbDTO);
                        } while (cursor.moveToNext());
                    }
                }
                flagError = true;
                //cursor.close();
                //sqLiteDatabase.close();
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getPaymentList table " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Add payment List", errorMessage);
        }
        return paymentModeList;
    }

    public boolean deleteTableTransaction(List<TableTransactionDbDTO> tableTransactionDelete) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;
        try {
            synchronized (sqLiteDatabase) {
                for (TableTransactionDbDTO table : tableTransactionDelete) {
                    String[] whereClasuse = new String[]{table.getCustId(), String.valueOf(table.getTableId())};
                    count = sqLiteDatabase.delete(SqlContract.SqlTableTransaction.TABLE_NAME,
                            SqlContract.SqlTableTransaction.CUST_ID + "=? AND "
                                    + SqlContract.SqlTableTransaction.TABLE_ID + "=?", whereClasuse);
                    //contentValues.clear();
                    Log.d(TAG, "## Data deleted from transaction table");
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            // sqLiteDatabase.close();
            Log.d(TAG, "## Data not deleted from transaction table");
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Delete TableTransaction", errorMessage);
        }
        return count != -1;
    }

    public boolean updateRTables(List<HotelTableDbDTO> rTableUpdates) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (HotelTableDbDTO dbTable : rTableUpdates) {
                    String[] whereClause = new String[]{String.valueOf(dbTable.getTableId())};
                    if (dbTable.getTableNo() != 0)
                        contentValues.put(SqlContract.SqlHotelTable.TABLE_NO, dbTable.getTableNo());
                    if (dbTable.getTableCategoryId() != 0)
                        contentValues.put(SqlContract.SqlHotelTable.TABLE_CATEGORY, dbTable.getTableCategoryId());
                    if (dbTable.getCapacity() != 0)
                        contentValues.put(SqlContract.SqlHotelTable.CAPACITY, dbTable.getCapacity());
                    contentValues.put(SqlContract.SqlHotelTable.IS_OCCUPIED, dbTable.isOccupied());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlHotelTable.TABLE_NAME, contentValues,
                            SqlContract.SqlHotelTable.TABLE_ID + "=?", whereClause);
                    contentValues.clear();
                    Log.d(TAG, "## Table is updated Successfully");
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error while updating Tables " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update taable", errorMessage);
        }
        return count != -1;
    }

    public boolean updateTableCategories(List<TableCategoryDbDTO> tableCategoryUpdate) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (TableCategoryDbDTO tableCategory : tableCategoryUpdate) {
                    String[] whereClause = new String[]{String.valueOf(tableCategory.getTableCategoryId())};

                    if (tableCategory.getCategoryTitle() != null && !tableCategory.getCategoryTitle().isEmpty())
                        contentValues.put(SqlContract.SqlTableCategory.CATEGORY_TITLE, tableCategory.getCategoryTitle());
                    if (tableCategory.getImage() != null && !tableCategory.getImage().isEmpty())
                        contentValues.put(SqlContract.SqlTableCategory.IMAGE, tableCategory.getImage());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlTableCategory.TABLE_NAME, contentValues
                            , SqlContract.SqlTableCategory.TABLE_CATEGORY_ID + "=?", whereClause);
                    contentValues.clear();
                    Log.d(TAG, "## Table Category is updated successfully" + tableCategory.getTableCategoryId());

                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at updating table" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Table Categories", errorMessage);
        }
        return count != -1;
    }

    public boolean updateMenus(List<MenuDbDTO> menuUpdates) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (MenuDbDTO menu : menuUpdates) {
                    String[] whereClause = new String[]{String.valueOf(menu.getMenuId())};
                    if (menu.getMenuTitle() != null && menu.getMenuTitle().isEmpty())
                        contentValues.put(SqlContract.SqlMenu.MENU_TITLE, menu.getMenuTitle());
                    if (menu.getImage() != null)
                        contentValues.put(SqlContract.SqlMenu.IMAGE, menu.getImage());
                    if (menu.getPrice() != 0)
                        contentValues.put(SqlContract.SqlMenu.PRICE, menu.getPrice());
                    if (menu.getIngredients() != null && menu.getIngredients().isEmpty())
                        contentValues.put(SqlContract.SqlMenu.INGREDIENTS, menu.getIngredients());
                    if (menu.getTags() != null && menu.getTags().isEmpty())
                        contentValues.put(SqlContract.SqlMenu.TAGS, menu.getTags());
                    contentValues.put(SqlContract.SqlMenu.AVAIL_STATUS, menu.getAvailabilityStatus());
                    contentValues.put(SqlContract.SqlMenu.ACTIVE, menu.getActive());
                    contentValues.put(SqlContract.SqlMenu.FOOD_TYPE, menu.getFoodType());
                    if (menu.getCategoryId() != 0)
                        contentValues.put(SqlContract.SqlMenu.CATEGORY_ID, menu.getCategoryId());
                    if (menu.getRoomId() != 0)
                        contentValues.put(SqlContract.SqlMenu.ROOM_ID, menu.getRoomId());
                    if (menu.getFbTypeId() != 0)
                        contentValues.put(SqlContract.SqlMenu.FB_TYPE_ID, menu.getFbTypeId());
                    contentValues.put(SqlContract.SqlMenu.IS_SPICY, menu.getIsSpicy());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlMenu.TABLE_NAME, contentValues,
                            SqlContract.SqlMenu.MENU_ID + "=?", whereClause);
                    contentValues.clear();
                    Log.d(TAG, "## Menu is Updated successfully" + menu.getMenuId());
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            Log.e(TAG, "## Error at Updated Menu" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Menu", errorMessage);
        }
        return count != -1;
    }

    public boolean updateMenuCategory(List<MenuCateoryDbDTO> menuCategoryUpdates) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (MenuCateoryDbDTO menuCateory : menuCategoryUpdates) {
                    String[] whereClause = new String[]{String.valueOf(menuCateory.getCategoryId())};
                    if (menuCateory.getCategoryTitle() != null && menuCateory.getCategoryTitle().isEmpty())
                        contentValues.put(SqlContract.SqlMenuCategory.CATEGORY_TITLE, menuCateory.getCategoryTitle());
                    if (menuCateory.getCategoryImage() != null && menuCateory.getCategoryImage().isEmpty())
                        contentValues.put(SqlContract.SqlMenuCategory.CATEGORY_IMG, menuCateory.getCategoryImage());

                    contentValues.put(SqlContract.SqlMenuCategory.ACTIVE, menuCateory.isActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlMenuCategory.TABLE_NAME, contentValues,
                            SqlContract.SqlMenuCategory.CATEGORY_ID + "=?", whereClause);
                    contentValues.clear();
                    Log.d(TAG, "## Menu Category is updated successfully" + menuCateory.getCategoryId());
                }
            }
            flagError = true;

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at updated Menu Category" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Menu Category ", errorMessage);
        }
        return count != -1;
    }

    public boolean updateMenuTags(List<MenuTagsDbDTO> menuTagUpdates) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (MenuTagsDbDTO menuTag : menuTagUpdates) {
                    String[] whereClause = new String[]{String.valueOf(menuTag.getTagId())};
                    if (menuTag.getTagTitle() != null && menuTag.getTagTitle().isEmpty())
                        contentValues.put(SqlContract.SqlMenuTags.TAG_TITLE, menuTag.getTagTitle());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlMenuTags.TABLE_NAME, contentValues,
                            SqlContract.SqlMenuTags.TAG_ID + "=?", whereClause);
                    contentValues.clear();
                    Log.d(TAG, "## Menu Tag is updated successfully" + menuTag.getTagId());

                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at update Menu Tag" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Menu Tag", errorMessage);
        }
        return count != -1;
    }

    public boolean updateUsers(List<UserDbDTO> userUpdates) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (UserDbDTO dbUser : userUpdates) {
                    String[] whereClause = new String[]{String.valueOf(dbUser.getUserId())};
                    if (dbUser.getUserName() != null && dbUser.getUserName().isEmpty())
                        contentValues.put(SqlContract.SqlUser.USER_NAME, dbUser.getUserName());
                    contentValues.put(SqlContract.SqlUser.ACTIVE, dbUser.isActive());
                    if (dbUser.getRoleId() != 0)
                        contentValues.put(SqlContract.SqlUser.ROLE_ID, dbUser.getRoleId());
                    if (dbUser.getRestaurantId() != 0)
                        contentValues.put(SqlContract.SqlUser.RESTAURANTID, dbUser.getRestaurantId());
                    if (dbUser.getPermissions() != null)
                        contentValues.put(SqlContract.SqlUser.PERMISSIONS, dbUser.getPermissions());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlUser.TABLE_NAME, contentValues,
                            SqlContract.SqlUser.USER_ID + "=?", whereClause);
                    contentValues.clear();
                    Log.d(TAG, "## User is update Successfully");
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while update users " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update User", errorMessage);
        }
        return count != -1;
    }

    public boolean updateTakeAway(List<TakeAwayDbDTO> takeawayInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (TakeAwayDbDTO takeAwayDbDTO : takeawayInserts) {
                    String[] where = new String[]{takeAwayDbDTO.getTakeawayId()};
                    if (takeAwayDbDTO.getTakeawayNo() != 0)
                        contentValues.put(SqlContract.SqlTakeAway.TAKE_AWAY_NO, takeAwayDbDTO.getTakeawayNo());
                    if (takeAwayDbDTO.getDiscount() != 0)
                        contentValues.put(SqlContract.SqlTakeAway.DISCOUNT, takeAwayDbDTO.getDiscount());
                    if (takeAwayDbDTO.getDeliveryCharges() != 0)
                        contentValues.put(SqlContract.SqlTakeAway.DELIVERY_CHG, takeAwayDbDTO.getDeliveryCharges());
                    if (takeAwayDbDTO.getCustId() != null && !takeAwayDbDTO.getCustId().isEmpty())
                        contentValues.put(SqlContract.SqlTakeAway.CUST_ID, takeAwayDbDTO.getCustId());
                    if (takeAwayDbDTO.getUserId() != 0)
                        contentValues.put(SqlContract.SqlTakeAway.USER_ID, takeAwayDbDTO.getUserId());
                    if (takeAwayDbDTO.getSourceId() != 0)
                        contentValues.put(SqlContract.SqlTakeAway.SOURCE_ID, takeAwayDbDTO.getSourceId());
                    if (takeAwayDbDTO.getCreatedDate() != null)
                        contentValues.put(SqlContract.SqlTakeAway.DATE, takeAwayDbDTO.getCreatedDate());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlTakeAway.TABLE_NAME, contentValues,
                            SqlContract.SqlTakeAway.TAKE_AWAY_ID + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "## Take away is updated successfully" + takeAwayDbDTO.getTakeawayNo());

                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at update take away" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Take Away", errorMessage);
        }
        return count != -1;
    }

    public ArrayList<NoteDTO> getNoteList() {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<NoteDTO> noteList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlMenuNoteMaster.TABLE_NAME
                                + " WHERE " + SqlContract.SqlMenuNoteMaster.ACTIVE + "=1",
                        null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();

                        do {
                            int noteIdId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMenuNoteMaster.NOTE_ID));
                            String paymentModeTitle = cursor.getString(cursor.getColumnIndex(SqlContract.SqlMenuNoteMaster.NOTE_TITLE));
                            int isactive = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlMenuNoteMaster.ACTIVE));
                            NoteDTO paymentModeDbDTO = new NoteDTO(noteIdId, paymentModeTitle, isactive);
                            noteList.add(paymentModeDbDTO);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
            //cursor.close();
            //sqLiteDatabase.close();

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getoteList table " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Note List", errorMessage);
        }
        return noteList;
    }

    public OrdersDbDTO getOrderDetails(String orderId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        String[] whereClause = new String[]{orderId};
        OrdersDbDTO order = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("select * from orders where " +
                        SqlContract.SqlOrders.ORDER_ID + "=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        //String orderId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_ID));
                        int orderNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_NO));
                        int orderStatus = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_STATUS));
                        String custId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.CUST_ID));
                        //String orderDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_DATE));
                        //String orderTime = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME));
                        // String createdDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.CREATED_DATE));
                        //String updatedDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.UPDATED_DATE));
                        int tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.TABLE_NO));
                        int userId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.USER_ID));
                        double orderAmount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_AMOUNT));
                        int takeawayNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.TAKE_AWAY_NO));
                        order = new OrdersDbDTO(orderId, orderNo, custId, orderStatus, tableNo, userId);
                        order.setTakeawayNo(takeawayNo);
                    }
                }
            }
            flagError = true;
            // cursor.close();
            //sqLiteDatabase.close();

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getOrdersOf table " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Order details", errorMessage);
        }
        return order;
    }

    public int getTaleNo(int tableId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        String[] whereClause = new String[]{String.valueOf(tableId)};
        Cursor cursor = null;
        int tableNo = 0;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select r_tables.TableNo,r_tables.IsOccupied from " +
                        "r_tables where r_tables.TableId=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_NO));
                    }
                }
            }
            flagError = true;
            //cursor.close();
            //sqLiteDatabase.close();

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getTableNo function " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Table No", errorMessage);
        }
        return tableNo;
    }

    public ArrayList<FeedBackDTO> getFeedBackList() {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<FeedBackDTO> feebackList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlFeedbackMaster.TABLE_NAME
                                + " WHERE " + SqlContract.SqlFeedbackMaster.ACTIVE + "=1",
                        null);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();

                        do {
                            int feedbackId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlFeedbackMaster.FEEDBACK_ID));
                            String feedbackTitle = cursor.getString(cursor.getColumnIndex(SqlContract.SqlFeedbackMaster.FEEDBACK_TITLE));
                            int isactive = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlFeedbackMaster.ACTIVE));
                            FeedBackDTO feedback = new FeedBackDTO(feedbackId, feedbackTitle, isactive);
                            feebackList.add(feedback);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
            //cursor.close();
            //sqLiteDatabase.close();

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at feedbackList table " + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Fedback llist", errorMessage);
        }
        return feebackList;
    }

    public long getPendingOrdersOfTable(int tableId, String custId) {
        boolean flagError = false;
        String errorMessage = "";
        long count = 0;
        SQLiteDatabase sqLiteDatabase = null;
        /*String[] whereClause = new String[]{String.valueOf(tableId), custId};
        Cursor cursor = null;*/
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {

                String sql = "select count(orders.OrderId)from orders where " + SqlContract.
                        SqlOrders.TABLE_NO + "=" + tableId + " AND " + SqlContract.SqlOrders.CUST_ID + "='" + custId
                        + "' And " + SqlContract.SqlOrders.ORDER_STATUS + "=1";
               /* cursor = sqLiteDatabase.rawQuery("select orders.OrderId from orders where " +
                        SqlContract.SqlOrders.TABLE_NO + "=? AND " + SqlContract.SqlOrders.CUST_ID +
                        "=? AND " + SqlContract.SqlOrders.ORDER_STATUS + "=1", whereClause);
                count = cursor.getCount();*/
                SQLiteStatement sqLiteStatement = sqLiteDatabase.compileStatement(sql);
                count = sqLiteStatement.simpleQueryForLong();

            }
            flagError = true;
            //cursor.close();
            //sqLiteDatabase.close();

        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error at getOrdersOf table " + e.toString());
        } finally {
           /* if (cursor != null) {
                cursor.close();
            }*/
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get pending orders", errorMessage);
        }
        return count;
    }

    public ArrayList<ChefOrderDetailsDTO> getCompletedRecordsChef() {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<ChefOrderDetailsDTO> ordres = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] where = new String[]{"2"};

                cursor = sqLiteDatabase.rawQuery("select orders.TakeawayNo,orders.OrderType,orders.OrderId,orders.CustId,orders.OrderStatus," +
                        "orders.OrderNo,orders.TableNo,orders.OrderTime,orders.Orderdate,users.UserName,r_tables.TableNo " +
                        "from  orders left join users on orders.UserId =users.UserId left join r_tables " +
                        "on r_tables.TableId = orders.TableNo where orders.OrderStatus=? order by  " +
                        "orders.OrderTime Asc", where);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {


                            String orderId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_ID));
                            int tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_NO));
                            String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                            int orderNumber = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_NO));
                            int orderStatus = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_STATUS));
                            String orderDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_DATE));
                            String orderTime = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME));
                            int takeAwayNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.TAKE_AWAY_NO));
                            int orderType = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TYPE));
                            // Time orderTime = Time.valueOf(cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME)));
                            ChefOrderDetailsDTO chefOrderDetailsDTO = new ChefOrderDetailsDTO(orderId, tableNo, userName, orderNumber, orderStatus, orderDate, orderTime, takeAwayNo, orderType);
                            ordres.add(chefOrderDetailsDTO);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();

            Log.e(TAG, "Get Complete records" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Complete Records", errorMessage);
        }
        return ordres;
    }

    public int getOrderCountFromTemp(int tableId, String custId) {
        boolean flagError = false;
        String errorMessage = "";
        int count = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        OrderHeaderDTO orderHeaderDTO = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{String.valueOf(tableId), custId};
                cursor = sqLiteDatabase.rawQuery("select count(orders.OrderId) as Count from orders where " +
                        SqlContract.SqlOrders.TABLE_NO + "=? AND " + SqlContract.SqlOrders.CUST_ID +
                        "=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        count = cursor.getInt(cursor.getColumnIndex("Count"));
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Order from temp", errorMessage);
        }
        return count;
    }


    public ArrayList<ChefOrderDetailsDTO> getRecChefOrder() {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<ChefOrderDetailsDTO> ordres = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] where = new String[]{"1"};

                cursor = sqLiteDatabase.rawQuery("select orders.TakeawayNo,orders.OrderType,orders.OrderId,orders.CustId,orders.OrderStatus," +
                        "orders.OrderNo,orders.TableNo,orders.OrderTime,orders.Orderdate,users.UserName,r_tables.TableNo " +
                        "from  orders left join users on orders.UserId =users.UserId left join r_tables " +
                        "on r_tables.TableId = orders.TableNo where orders.OrderStatus=1 order by  " +
                        "orders.OrderTime Asc", null);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {


                            String orderId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_ID));
                            int tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_NO));
                            String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                            int orderNumber = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_NO));
                            int orderStatus = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_STATUS));
                            String orderDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_DATE));
                            String orderTime = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME));
                            int takeAwayNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.TAKE_AWAY_NO));
                            int orderType = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TYPE));
                            // Time orderTime = Time.valueOf(cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME)));
                            ChefOrderDetailsDTO chefOrderDetailsDTO = new ChefOrderDetailsDTO(orderId, tableNo, userName, orderNumber, orderStatus, orderDate, orderTime, takeAwayNo, orderType);
                            ordres.add(chefOrderDetailsDTO);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();

            Log.e(TAG, "Error in ChefDb Headre" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Chef Order", errorMessage);
        }
        return ordres;
    }

    public ArrayList<ChefOrderDetailsDTO> getRecChefTakeAwayOrders() {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<ChefOrderDetailsDTO> ordres = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] where = new String[]{"1"};

                cursor = sqLiteDatabase.rawQuery("select orders.TakeawayNo,orders.OrderType,orders.OrderId,orders.CustId,orders.OrderStatus,orders.OrderNo,orders.TableNo," +
                        "orders.OrderTime,orders.Orderdate,users.UserName,r_tables.TableNo " +
                        "from  orders left join users on orders.UserId =users.UserId left join r_tables " +
                        "on r_tables.TableId = orders.TableNo where orders.OrderStatus=1 and orders.OrderType=2  order by orders.OrderTime Asc", null);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {


                            String orderId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_ID));
                            int tableNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlHotelTable.TABLE_NO));
                            String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                            int orderNumber = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_NO));
                            int orderStatus = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_STATUS));
                            String orderDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_DATE));
                            String orderTime = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME));
                            int takeAwayNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.TAKE_AWAY_NO));
                            int orderType = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TYPE));
                            // Time orderTime = Time.valueOf(cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME)));
                            ChefOrderDetailsDTO chefOrderDetailsDTO = new ChefOrderDetailsDTO(orderId, tableNo, userName, orderNumber, orderStatus, orderDate, orderTime, takeAwayNo, orderType);
                            ordres.add(chefOrderDetailsDTO);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();

            Log.e(TAG, "Error in ChefDb Headre" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Chef take away orders", errorMessage);
        }
        return ordres;
    }

    public void addMenuList(ArrayList<ChefOrderDetailsDTO> list) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getReadableDatabase();
        synchronized (sqLiteDatabase) {
            Cursor cursor = null;
            try {
                for (ChefOrderDetailsDTO order : list) {
                    ArrayList<ChefMenuDetailsDTO> menudetails = new ArrayList<>();
                    String orderId = order.getmNewOrderId();
                    String[] whereClause = new String[]{orderId};
                    cursor = sqLiteDatabase.rawQuery("select order_details.OrderId,orders.OrderId," +
                            "order_details.MenuId,menu.MenuTitle,order_details.OrderQuantity," +
                            "order_details.Note from orders inner join order_details on " +
                            "order_details.OrderId= orders.OrderId  inner join  menu on menu.MenuId = " +
                            "order_details.MenuId where order_details.OrderId =?", whereClause);
                    if (cursor != null) {

                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                int mMenuQty = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_QUANTITY));
                                int mMenuId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.MENU_ID));
                                String mMenuName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlMenu.MENU_TITLE));
                                // Time orderTime = Time.valueOf(cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_TIME)));
                                String mMenuNote = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.NOTE));
                                ChefMenuDetailsDTO chefMenuDetailsDTO = new ChefMenuDetailsDTO(mMenuId, mMenuName, mMenuQty, mMenuNote);
                                menudetails.add(chefMenuDetailsDTO);
                            } while (cursor.moveToNext());
                        }

                    }
                    order.setmMenuChild(menudetails);
                }
                flagError = true;
            } catch (Exception e) {
                e.printStackTrace();
                flagError = false;
                errorMessage = e.getMessage();
                Log.e(TAG, "Error in ChefDb MenuLog" + e.toString());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
                if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                    sqLiteDatabase.close();
                if (!flagError)
                    addError(TAG, "Add Menu List", errorMessage);
            }

        }
    }

    public boolean deleteCustomer(String custId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        // ContentValues contentValues = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{custId};
                count = sqLiteDatabase.delete(SqlContract.SqlCustomer.TABLE_NAME,
                        SqlContract.SqlCustomer.CUST_ID + "=?",
                        whereClause);
                // contentValues.clear();
                //sqLiteDatabase.close();
                Log.d(TAG, " ## delete customer successfully");
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "## delete customer is not successfully" + e.toString());
            //sqLiteDatabase.close();

        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Delete Customer", errorMessage);
        }
        return count != -1;
    }

    public boolean deleteCustomerTableTrans(String custId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        // ContentValues contentValues = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{custId};
                count = sqLiteDatabase.delete(SqlContract.SqlTempOrder.TABLE_NAME,
                        SqlContract.SqlTempOrder.CUST_ID + "=?",
                        whereClause);
                // contentValues.clear();
                //sqLiteDatabase.close();
                Log.d(TAG, " ## delete customer successfully");
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "## delete customer is not successfully" + e.toString());
            //sqLiteDatabase.close();

        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Delete Customer", errorMessage);
        }
        return count != -1;
    }

    public boolean deleteBill(String custId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{custId};
                count = sqLiteDatabase.delete(SqlContract.SqlBill.TABLE_NAME,
                        SqlContract.SqlBill.CUST_ID + "=?",
                        whereClause);

                Log.d(TAG, " ## delete Bill successfully");
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "## delete bill is not successfully" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Delete Bill", errorMessage);
        }
        return count != -1;
    }

    public boolean deleteBillDetails(String custId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{custId};
                count = sqLiteDatabase.delete(SqlContract.SqlBillDetails.TABLE_NAME,
                        SqlContract.SqlBillDetails.BILL_NO + " IN (Select BillNo from bill where " +
                                "CustId='" + custId + "')",
                        null);

                Log.d(TAG, " ## delete BillDetails successfully");
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "## delete BillDetails is not successfully" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Delete Bill details", errorMessage);
        }
        return count != -1;
    }

    public boolean deleteOrderDetails(String custId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{custId};
                count = sqLiteDatabase.delete(SqlContract.SqlOrderDetails.TABLE_NAME,
                        SqlContract.SqlOrderDetails.ORDER_ID + " IN(select OrderId from orders " +
                                "Where CustId ='" + custId + "')",
                        null);

                Log.d(TAG, " ## delete order details successfully");
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "## delete order details is not successfully" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Delete Order Details", errorMessage);
        }
        return count != -1;
    }

    public boolean deleteOrder(String custId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{custId};
                count = sqLiteDatabase.delete(SqlContract.SqlOrders.TABLE_NAME,
                        SqlContract.SqlOrders.CUST_ID + "=?",
                        whereClause);

                Log.d(TAG, " ## delete Order successfully");
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            e.printStackTrace();
            Log.d(TAG, "## delete Order is not successfully" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Delete Order", errorMessage);
        }
        return count != -1;
    }

    public void cleanData(String custId, int userId, int myUserId) {
       /* if (userId == myUserId) {

        }*/
        deleteBillDetails(custId);
        deleteBill(custId);
        deleteOrderDetails(custId);
        deleteOrder(custId);
        deleteCustomer(custId);
        deleteTakeAwayOrder(custId);
    }

    public boolean deleteTakeAwayOrder(String custId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getWritableDatabase();
        long count = -1;

        try {
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{custId};
                count = sqLiteDatabase.delete(SqlContract.SqlTakeAway.TABLE_NAME,
                        SqlContract.SqlTakeAway.CUST_ID + "=?",
                        whereClause);

                Log.d(TAG, " ## delete Take away order Order successfully");
            }
            flagError = true;
        } catch (Exception e) {
            e.printStackTrace();
            flagError = false;
            Log.d(TAG, "## delete Take away Order is not successfully" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Delete Take Away", errorMessage);
        }
        return count != -1;
    }

    public ArrayList<TakeAwaySourceDTO> getTakeAwaySource() {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<TakeAwaySourceDTO> takeAwaySourceDTOs = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("select * from " + SqlContract.
                        SqlTakeAwaySource.TABLE_NAME + " where " + SqlContract.SqlTakeAwaySource.
                        ACTIVE + "=1", null);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            int takeSourceId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTakeAwaySource.TAKE_AWAY_SOURCE_ID));
                            String name = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTakeAwaySource.SOURCE_NAME));
                            String imgUrl = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTakeAwaySource.SOURCE_URL));
                            double discount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlTakeAwaySource.DISCOUNT));
                            TakeAwaySourceDTO takeAwaySourceDTO = new TakeAwaySourceDTO(takeSourceId, name, imgUrl, discount);
                            takeAwaySourceDTOs.add(takeAwaySourceDTO);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            e.printStackTrace();
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error in Take away source" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Take away source", errorMessage);
        }
        return takeAwaySourceDTOs;
    }

    public boolean insertTakeAway(UploadTakeAway takeAwayDbDTO, int userId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                contentValues.put(SqlContract.SqlTakeAway.TAKE_AWAY_ID, takeAwayDbDTO.getTakeawayId());
                contentValues.put(SqlContract.SqlTakeAway.DISCOUNT, takeAwayDbDTO.getDiscount());
                contentValues.put(SqlContract.SqlTakeAway.DELIVERY_CHG, takeAwayDbDTO.getDeliveryCharges());
                contentValues.put(SqlContract.SqlTakeAway.CUST_ID, takeAwayDbDTO.getCustId());
                contentValues.put(SqlContract.SqlTakeAway.USER_ID, userId);
                contentValues.put(SqlContract.SqlTakeAway.SOURCE_ID, takeAwayDbDTO.getSourceId());
                if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                count = sqLiteDatabase.insert(SqlContract.SqlTakeAway.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Take away is added successfully using UploadTakeAway" + takeAwayDbDTO.getTakeawayId());

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at insert take away using UploadTakeAway" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Take away", errorMessage);
        }
        return count != -1;
    }

    public ArrayList<TakeAwayDTO> getTakeAwayList() {
        boolean flagError = false;
        String errorMessage = "";
        ROrderDateUtils dateUtils = new ROrderDateUtils();
        String date = dateUtils.getLocalSQLCurrentDate();
        String time = dateUtils.getSqlOffsetTime(AppConstants.ORDER_TIME_HOUR, AppConstants.ORDER_TIME_MINUTE);

        ArrayList<TakeAwayDTO> takeAways = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String strQuery = "Select takeaway.TakeawayId,takeaway.TakeawayNo,takeaway.CustId,takeaway.CreatedDate," +
                        "takeaway.SourceId,takeaway.Discount,takeaway.DeliveryCharges," +
                        "takeaway.UserId,users.UserName,customer.CustName,customer.CustPhone," +
                        "customer.CustAddress,takeaway_source.SourceName " +
                        "from takeaway left join users on users.UserId=takeaway.UserId " +
                        "left join customer on customer.CustId=takeaway.CustId " +
                        "left join takeaway_source on takeaway_source.SourceId=takeaway.SourceId " +
                        "where takeaway.CreatedDate>= '" + date + "T" + time + "'";
                cursor = sqLiteDatabase.rawQuery(strQuery, null);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            String takeawayId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTakeAway.TAKE_AWAY_ID));
                            int takeawayNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTakeAway.TAKE_AWAY_NO));
                            double discount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlTakeAway.DISCOUNT));
                            double deliveryCharges = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlTakeAway.DELIVERY_CHG));
                            String custId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTakeAway.CUST_ID));
                            int userId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTakeAway.USER_ID));
                            int sourceId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTakeAway.SOURCE_ID));
                            String custName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlCustomer.CUST_NAME));
                            String custAddress = cursor.getString(cursor.getColumnIndex("CustAddress"));
                            String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                            String custPhone = cursor.getString(cursor.getColumnIndex(SqlContract.SqlCustomer.CUST_PHONE));
                            String sourceName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTakeAwaySource.SOURCE_NAME));
                            String createdDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTakeAway.DATE));
                            TakeAwayDTO takeaway = new TakeAwayDTO(takeawayId, takeawayNo, discount,
                                    deliveryCharges, custId, userId, sourceId, custName, custAddress, userName, custPhone, sourceName);
                            takeAways.add(takeaway);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            e.printStackTrace();
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error in get Take away list" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Take away list", errorMessage);
        }
        return takeAways;
    }

    public double getTakeAwayDiscount(int takeAwayNo) {
        boolean flagError = false;
        String errorMessage = "";
        double discount = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        OrderHeaderDTO orderHeaderDTO = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{String.valueOf(takeAwayNo)};
                cursor = sqLiteDatabase.rawQuery("select " + SqlContract.SqlTakeAway.DISCOUNT + " from "
                        + SqlContract.SqlTakeAway.TABLE_NAME + " where " +
                        SqlContract.SqlTakeAway.TAKE_AWAY_NO + "=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        discount = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTakeAway.DISCOUNT));
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Take away Discount", errorMessage);
        }
        return discount;
    }

    public double getTakeAwayDeliveryChr(int takeAwayNo) {
        boolean flagError = false;
        String errorMessage = "";
        double deliveryChr = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        OrderHeaderDTO orderHeaderDTO = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{String.valueOf(takeAwayNo)};
                cursor = sqLiteDatabase.rawQuery("select " + SqlContract.SqlTakeAway.DELIVERY_CHG + " from "
                        + SqlContract.SqlTakeAway.TABLE_NAME + " where " +
                        SqlContract.SqlTakeAway.TAKE_AWAY_NO + "=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        deliveryChr = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTakeAway.DELIVERY_CHG));
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Take away Delivery Charges", errorMessage);
        }
        return deliveryChr;
    }

    public TakeAwayDTO getTakeAway(int takeAwayNo) {
        boolean flagError = false;
        String errorMessage = "";
        TakeAwayDTO takeAway = null;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select takeaway.TakeawayId,takeaway.TakeawayNo," +
                        "takeaway.CustId,takeaway.SourceId,takeaway.Discount,takeaway.DeliveryCharges," +
                        "takeaway.UserId,users.UserName,customer.CustName,customer.CustPhone," +
                        "customer.CustAddress,takeaway_source.SourceName from takeaway left join " +
                        "users on users.UserId=takeaway.UserId left join customer on customer.CustId=" +
                        "takeaway.CustId left join takeaway_source on takeaway_source.SourceId=takeaway.SourceId " +
                        "where takeaway.TakeawayNo=" + takeAwayNo, null);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        String takeawayId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTakeAway.TAKE_AWAY_ID));
                        int takeawayNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTakeAway.TAKE_AWAY_NO));
                        double discount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlTakeAway.DISCOUNT));
                        double deliveryCharges = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlTakeAway.DELIVERY_CHG));
                        String custId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTakeAway.CUST_ID));
                        int userId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTakeAway.USER_ID));
                        int sourceId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTakeAway.SOURCE_ID));
                        String custName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlCustomer.CUST_NAME));
                        String custAddress = cursor.getString(cursor.getColumnIndex("CustAddress"));
                        String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                        String custPhone = cursor.getString(cursor.getColumnIndex(SqlContract.SqlCustomer.CUST_PHONE));
                        String sourceName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTakeAwaySource.SOURCE_NAME));
                        takeAway = new TakeAwayDTO(takeawayId, takeawayNo, discount,
                                deliveryCharges, custId, userId, sourceId, custName, custAddress, userName, custPhone, sourceName);
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            e.printStackTrace();
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error in get Take away" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Take away ", errorMessage);
        }
        return takeAway;
    }

    public void setTakeAwayStatus(ArrayList<TakeAwayDTO> takeAwayDTOs) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;

        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                for (TakeAwayDTO takeAway : takeAwayDTOs) {
                    int countSPending = 0, countSReady = 0, countSDelivered = 0;
                    String[] where = new String[]{String.valueOf(takeAway.getmTakeawayNo())};
                    String strQuery = "Select " + SqlContract.SqlOrders.ORDER_STATUS + " FROM "
                            + SqlContract.SqlOrders.TABLE_NAME + " Where " + SqlContract.SqlOrders.TAKE_AWAY_NO + "=?";
                    cursor = sqLiteDatabase.rawQuery(strQuery, where);
                    if (cursor != null) {

                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                int status = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_STATUS));
                                if (status == AppConstants.TAKAWAY_STATUS_PENDING)
                                    countSPending = countSPending + 1;
                                if (status == AppConstants.TAKAWAY_STATUS_READY)
                                    countSReady = countSReady + 1;
                                if (status == AppConstants.TAKAWAY_STATUS_DELIVERED)
                                    countSDelivered = countSDelivered + 1;
                            } while (cursor.moveToNext());
                        }
                        if (countSPending > 0)
                            takeAway.setOrderStatus(AppConstants.TAKAWAY_STATUS_PENDING);
                        if (cursor.getCount() == countSReady)
                            takeAway.setOrderStatus(AppConstants.TAKAWAY_STATUS_READY);
                        if (cursor.getCount() == countSDelivered)
                            takeAway.setOrderStatus(AppConstants.TAKAWAY_STATUS_DELIVERED);
                    }

                }


            }
            flagError = true;
        } catch (Exception e) {
            e.printStackTrace();
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error in setTakeAwayStatus" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "set Take away status", errorMessage);
        }


    }

    /**
     * Sqlite functions for printing
     */

    public boolean insertConfigSettings(List<ConfigSettingsDbDTO> configSettings) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (ConfigSettingsDbDTO configSetting : configSettings) {
                    contentValues.put(SqlContract.SqlRConfigSettings.CONFIG_KEY, configSetting.getConfigKey());
                    contentValues.put(SqlContract.SqlRConfigSettings.CONFIG_VALUE, configSetting.getConfigValue());

                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlRConfigSettings.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Config settings  is Added Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while adding Config settings " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "add Config settings", errorMessage);
        }
        return count != -1;
    }

    public boolean updateConfigSettings(List<ConfigSettingsDbDTO> configSettings) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (ConfigSettingsDbDTO configSetting : configSettings) {
                    String[] where = new String[]{configSetting.getConfigKey()};
                    contentValues.put(SqlContract.SqlRConfigSettings.CONFIG_VALUE, configSetting.getConfigValue());

                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlRConfigSettings.TABLE_NAME,
                            contentValues, SqlContract.SqlRConfigSettings.CONFIG_KEY + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "## Config settings  is Updated Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while updating  Config settings " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "update config settings", errorMessage);
        }
        return count != -1;
    }


    public boolean insertRoomType(List<RoomTypesDbDTO> roomType) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (RoomTypesDbDTO roomT : roomType) {
                    contentValues.put(SqlContract.SqlRoomType.ROOM_TYPE_ID, roomT.getRoomTypeId());
                    contentValues.put(SqlContract.SqlRoomType.ROOM_TYPE, roomT.getRoomType());
                    contentValues.put(SqlContract.SqlRoomType.ACTIVE, roomT.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlRoomType.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Room Type is Added Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while  Room Type adding " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Room type add", errorMessage);
        }
        return count != -1;
    }

    public boolean updateRoomType(List<RoomTypesDbDTO> roomType) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (RoomTypesDbDTO roomT : roomType) {
                    String[] where = new String[]{String.valueOf(roomT.getRoomTypeId())};
                    if (roomT.getRoomType() != null)
                        contentValues.put(SqlContract.SqlRoomType.ROOM_TYPE, roomT.getRoomType());
                    contentValues.put(SqlContract.SqlRoomType.ACTIVE, roomT.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlRoomType.TABLE_NAME, contentValues,
                            SqlContract.SqlRoomType.ROOM_TYPE_ID + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "## Room Type is updated Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while  Room Type updating " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update room type", errorMessage);
        }
        return count != -1;
    }

    public boolean insertRestaurant(List<RestaurantDbDTO> restaurant) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (RestaurantDbDTO restaurantDbDTO : restaurant) {
                    contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_ID, restaurantDbDTO.getRestaurantId());
                    contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_NAME, restaurantDbDTO.getTitle());
                    contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_URL, restaurantDbDTO.getLogoUrl());
                    contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_ADDRESS, restaurantDbDTO.getAddress());
                    contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_AREA, restaurantDbDTO.getArea());
                    contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_CITY, restaurantDbDTO.getCity());
                    contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_COUNTRY, restaurantDbDTO.getCountry());
                    contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_PHONE, restaurantDbDTO.getPhone());
                    contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_FOOTER, restaurantDbDTO.getFooter());

                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlRestaurant.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Restaurant is Added Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while  Restaurant  adding " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Restaurant", errorMessage);
        }
        return count != -1;
    }

    public boolean updateRestaurant(List<RestaurantDbDTO> restaurant) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (RestaurantDbDTO restaurantDbDTO : restaurant) {
                    String[] where = new String[]{String.valueOf(restaurantDbDTO.getRestaurantId())};
                    if (restaurantDbDTO.getTitle() != null)
                        contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_NAME, restaurantDbDTO.getTitle());
                    if (restaurantDbDTO.getLogoUrl() != null)
                        contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_URL, restaurantDbDTO.getLogoUrl());
                    if (restaurantDbDTO.getAddress() != null)
                        contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_ADDRESS, restaurantDbDTO.getAddress());
                    if (restaurantDbDTO.getArea() != null)
                        contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_AREA, restaurantDbDTO.getArea());
                    if (restaurantDbDTO.getCity() != null)
                        contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_CITY, restaurantDbDTO.getCity());
                    if (restaurantDbDTO.getCountry() != null)
                        contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_COUNTRY, restaurantDbDTO.getCountry());
                    if (restaurantDbDTO.getPhone() != null)
                        contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_PHONE, restaurantDbDTO.getPhone());
                    if (restaurantDbDTO.getFooter() != null)
                        contentValues.put(SqlContract.SqlRestaurant.RESTAURANT_FOOTER, restaurantDbDTO.getFooter());


                    if (!sqLiteDatabase.isOpen())
                        sqLiteDatabase = getWritableDatabase();//update statement;
                    count = sqLiteDatabase.update(SqlContract.SqlRestaurant.TABLE_NAME, contentValues,
                            SqlContract.SqlRestaurant.RESTAURANT_ID + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "## Restaurant is updated Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            Log.e("DbOperationsEx", "Error while  Updating Restaurant " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Restaurant", errorMessage);
        }
        return count != -1;
    }


    public boolean insertPrinters(List<PrintersDbDTO> printers) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (PrintersDbDTO Printer : printers) {
                    contentValues.put(SqlContract.SqlRPrinters.PRINTER_ID, Printer.getPrinterId());
                    contentValues.put(SqlContract.SqlRPrinters.IP_ADDRESS, Printer.getIpAddress());
                    contentValues.put(SqlContract.SqlRPrinters.PRINTER_NAME, Printer.getPrinterName());
                    contentValues.put(SqlContract.SqlRPrinters.MODEL_NAME, Printer.getPrinterName());
                    contentValues.put(SqlContract.SqlRPrinters.COMPANY, Printer.getCompany());
                    contentValues.put(SqlContract.SqlRPrinters.MAC_ADDRESS, Printer.getMacAddress());
                    contentValues.put(SqlContract.SqlRPrinters.ACTIVE, Printer.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlRPrinters.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Printer is Added Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while  Printer adding " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Add printer", errorMessage);
        }
        return count != -1;
    }

    public boolean updatePrinters(List<PrintersDbDTO> printers) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (PrintersDbDTO printer : printers) {
                    String[] where = new String[]{String.valueOf(printer.getPrinterId())};
                    contentValues.put(SqlContract.SqlRPrinters.PRINTER_ID, printer.getPrinterId());
                    if (printer.getIpAddress() != null)
                        contentValues.put(SqlContract.SqlRPrinters.IP_ADDRESS, printer.getIpAddress());
                    if (printer.getPrinterName() != null)
                        contentValues.put(SqlContract.SqlRPrinters.PRINTER_NAME, printer.getPrinterName());
                    if (printer.getModelName() != null)
                        contentValues.put(SqlContract.SqlRPrinters.MODEL_NAME, printer.getModelName());
                    if (printer.getCompany() != null)
                        contentValues.put(SqlContract.SqlRPrinters.COMPANY, printer.getCompany());
                    if (printer.getCompany() != null)
                        contentValues.put(SqlContract.SqlRPrinters.MAC_ADDRESS, printer.getMacAddress());
                    contentValues.put(SqlContract.SqlRPrinters.ACTIVE, printer.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlRPrinters.TABLE_NAME, contentValues,
                            SqlContract.SqlRPrinters.PRINTER_ID + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "## Printer is updated Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while  Printer updating " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Printer updating", errorMessage);
        }
        return count != -1;
    }

    public boolean insertRooms(List<RoomsDbDTO> rooms) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (RoomsDbDTO room : rooms) {
                    contentValues.put(SqlContract.SqlRRooms.ROOM_ID, room.getRoomId());
                    contentValues.put(SqlContract.SqlRRooms.DESCRIPTION, room.getDescription());
                    contentValues.put(SqlContract.SqlRRooms.ACTIVE, room.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlRRooms.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Room is Added Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while adding rooms " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Add Room", errorMessage);
        }
        return count != -1;
    }

    public boolean updateRooms(List<RoomsDbDTO> rooms) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (RoomsDbDTO room : rooms) {
                    String[] where = new String[]{String.valueOf(room.getRoomId())};
                    if (room.getDescription() != null)
                        contentValues.put(SqlContract.SqlRRooms.DESCRIPTION, room.getDescription());
                    contentValues.put(SqlContract.SqlRRooms.ACTIVE, room.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlRRooms.TABLE_NAME, contentValues, SqlContract.SqlRRooms.ROOM_ID + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "## Room is updated Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while updating rooms " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update room", errorMessage);
        }
        return count != -1;
    }

    public boolean insertRoomPrinter(List<RoomPrintersDbDTO> roomPrinters) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (RoomPrintersDbDTO roomPrinter : roomPrinters) {
                    contentValues.put(SqlContract.SqlRRoomPrinter.ROOM_ID, roomPrinter.getRoomId());
                    contentValues.put(SqlContract.SqlRRoomPrinter.ROOM_TYPE_ID, roomPrinter.getRoomTypeId());
                    contentValues.put(SqlContract.SqlRRoomPrinter.PRINTER_ID, roomPrinter.getPrinterId());
                    contentValues.put(SqlContract.SqlRRoomPrinter.DESCRIPTION, roomPrinter.getDescription());
                    contentValues.put(SqlContract.SqlRRoomPrinter.ACTIVE, roomPrinter.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlRRoomPrinter.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Room printer is Added Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while adding room printer " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Room Printer", errorMessage);
        }
        return count != -1;
    }

    public boolean updateRoomPrinter(List<RoomPrintersDbDTO> roomPrinters) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (RoomPrintersDbDTO roomPrinter : roomPrinters) {
                    String[] where = new String[]{String.valueOf(roomPrinter.getRoomId())};
                    if (roomPrinter.getRoomTypeId() != 0)
                        contentValues.put(SqlContract.SqlRRoomPrinter.ROOM_TYPE_ID, roomPrinter.getRoomTypeId());
                    if (roomPrinter.getPrinterId() != 0)
                        contentValues.put(SqlContract.SqlRRoomPrinter.PRINTER_ID, roomPrinter.getPrinterId());
                    if (roomPrinter.getDescription() != null)
                        contentValues.put(SqlContract.SqlRRoomPrinter.DESCRIPTION, roomPrinter.getDescription());
                    contentValues.put(SqlContract.SqlRRoomPrinter.ACTIVE, roomPrinter.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlRRoomPrinter.TABLE_NAME, contentValues, SqlContract.SqlRRoomPrinter.ROOM_ID + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "## Room printer is Updated Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while updating room printer " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "update room printer", errorMessage);
        }
        return count != -1;
    }

    public boolean insertPermission(List<PermissionSetDbDTO> permissions) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (PermissionSetDbDTO permission : permissions) {
                    contentValues.put(SqlContract.SqlPermissionSet.PERMISSION_ID, permission.getPermissionId());
                    contentValues.put(SqlContract.SqlPermissionSet.PERMISSION_KEY, permission.getPermissionKey());
                    contentValues.put(SqlContract.SqlPermissionSet.DESCRIPTION, permission.getDescription());
                    contentValues.put(SqlContract.SqlPermissionSet.ACTIVE, permission.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlRRoomPrinter.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Permission is Added Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while adding permission " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "adding permission", errorMessage);
        }
        return count != -1;
    }

    public boolean updatePermission(List<PermissionSetDbDTO> permissions) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (PermissionSetDbDTO permission : permissions) {
                    String[] where = new String[]{String.valueOf(permission.getPermissionId())};
                    if (permission.getPermissionKey() != null)
                        contentValues.put(SqlContract.SqlPermissionSet.PERMISSION_KEY, permission.getPermissionKey());
                    if (permission.getDescription() != null)
                        contentValues.put(SqlContract.SqlPermissionSet.DESCRIPTION, permission.getDescription());
                    contentValues.put(SqlContract.SqlPermissionSet.ACTIVE, permission.getActive());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlRRoomPrinter.TABLE_NAME, contentValues,
                            SqlContract.SqlPermissionSet.PERMISSION_ID + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "## Permission is updated Successfully");
                }

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while updating permission " + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Permission updating", errorMessage);
        }
        return count != -1;
    }

    /* End of Printer Function*/

    /*
    * Read Permission
    * */

    public int getPermissionId(String key) {
        boolean flagError = false;
        String errorMessage = "";
        int id = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{key};
                cursor = sqLiteDatabase.rawQuery("select " + SqlContract.SqlPermissionSet.PERMISSION_ID + " from "
                        + SqlContract.SqlPermissionSet.TABLE_NAME + " where " +
                        SqlContract.SqlPermissionSet.PERMISSION_KEY + "=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        id = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlPermissionSet.PERMISSION_ID));
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Permission Id", errorMessage);
        }
        return id;
    }

    public ArrayList<PrinterDetailsDTO> getPrinterDetails(int roomType) {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<PrinterDetailsDTO> detailsArray = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        PrinterDetailsDTO printerDetailsDTO = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] where = new String[]{String.valueOf(roomType)};

                cursor = sqLiteDatabase.rawQuery("SELECT r_room_printer.RoomId,r_room_printer.PrinterId,r_printers.PrinterId,r_room_printer.Description,r_room_printer.RoomTypeId," +
                        "r_printers.IpAddress,r_printers.PrinterName,r_printers.ModelName,r_printers.MacAddress," +
                        "r_printers.Company,r_room_printer.Active from r_printers left join r_room_printer " +
                        "on r_room_printer.PrinterId = r_printers.PrinterId where r_room_printer.Active =1 and r_room_printer.RoomId = ?", where);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            int printerId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlRRoomPrinter.PRINTER_ID));
                            String ipAddress = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRPrinters.IP_ADDRESS));
                            String printerName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRPrinters.PRINTER_NAME));
                            String printerModelName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRPrinters.MODEL_NAME));
                            String macAddress = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRPrinters.MAC_ADDRESS));
                            String printerCompany = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRPrinters.COMPANY));

                            printerDetailsDTO = new PrinterDetailsDTO(printerId, ipAddress, printerName, printerModelName, macAddress, printerCompany);
                            detailsArray.add(printerDetailsDTO);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while getting printer data " + e.toString());
        } finally {

            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Printer details", errorMessage);
        }
        return detailsArray;
    }

    public PrinterDetailsDTO getPrinterDetailsByRoom(int roomId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        PrinterDetailsDTO printerDetailsDTO = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] where = new String[]{String.valueOf(roomId)};

                cursor = sqLiteDatabase.rawQuery("SELECT r_room_printer.RoomId,r_room_printer.PrinterId,r_printers.PrinterId," +
                        " r_printers.IpAddress,r_printers.PrinterName,r_printers.ModelName,r_printers.MacAddress," +
                        " r_printers.Company,r_room_printer.Active from r_printers left join r_room_printer " +
                        " on r_room_printer.PrinterId = r_printers.PrinterId where r_room_printer.Active =1 and r_room_printer.RoomId =?", where);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        int printerId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlRRoomPrinter.PRINTER_ID));
                        String ipAddress = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRPrinters.IP_ADDRESS));
                        String printerName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRPrinters.PRINTER_NAME));
                        String printerModelName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRPrinters.MODEL_NAME));
                        String macAddress = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRPrinters.MAC_ADDRESS));
                        String printerCompany = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRPrinters.COMPANY));
                        printerDetailsDTO = new PrinterDetailsDTO(printerId, ipAddress, printerName,
                                printerModelName, macAddress, printerCompany);
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while getting getPrinterDetailsByRoom data " + e.toString());
        } finally {

            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Printer details by room", errorMessage);
        }
        return printerDetailsDTO;
    }

    public ArrayList<String> getOderIdForPrinting(String OrderStatus, String CustmerId) {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<String> getOrderId = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = null;
        // PrinterDetails printerDetails = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] where = new String[]{OrderStatus, CustmerId};
                // String[] where = new String[]{CustmerId};
                cursor = sqLiteDatabase.rawQuery("SELECT orders.OrderId from orders where " + SqlContract.SqlOrders.ORDER_STATUS + " =? AND " + SqlContract.SqlOrders.CUST_ID + "=?", where);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {

                            String printerCompany = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_ID));


                            getOrderId.add(printerCompany);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while getting oderId " + e.toString());
        } finally {

            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Order Id", errorMessage);
        }
        return getOrderId;
    }

    public HashMap<String, OrderDetailsDTO> getMenuDetailsForOrderPrint(ArrayList<String> getOrderId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = null;
        HashMap<String, OrderDetailsDTO> billdetails = new HashMap<>();
        String previousQty, previousOrderPrice, previousTotalPrice;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                for (int i = 0; i <= getOrderId.size(); i++) {
                    String OrderId = getOrderId.get(i);
                    String[] where = new String[]{OrderId};
                    cursor = sqLiteDatabase.rawQuery("SELECT * " +
                            "from order_details  where " + SqlContract.SqlOrderDetails.ORDER_ID + " =?", where);
                    if (cursor != null) {

                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {

                                int menuId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.MENU_ID));
                                String menuName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlOrderDetails.MENU_TITLE));
                                int menuQty = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_QUANTITY));
                                double orderPrice = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlOrderDetails.ORDER_PRICE));
                               /* TotalPriceForQty = (MenuQty * OrderPrice);
                                if (billdetails.containsKey(MenuName)) {
                                    listTemp =billdetails.get(MenuName);
                                   // ArrayList<String> temp = billdetails.get(MenuName);
                                    previousQty = listTemp.get(0);
                                    previousOrderPrice = listTemp.get(1);
                                    previousTotalPrice = listTemp.get(2);
                                    int Qty = Integer.parseInt(previousQty) + MenuQty;
                                    double orderPrice = (Double.parseDouble(previousOrderPrice));
                                    TotalPriceForQty = (Double.parseDouble(previousTotalPrice) + TotalPriceForQty) ;
                                    listTemp.clear();
                                    billdetails.remove(MenuName);
                                    listTemp.add(0,String.valueOf(Qty));
                                    listTemp.add(1,String.valueOf(orderPrice));
                                    listTemp.add(2, String.valueOf(TotalPriceForQty));

                                    billdetails.put(MenuName, listTemp);

                                } else if(!billdetails.containsKey(MenuName)){
                                    listTemp.add(0,String.valueOf(MenuQty));
                                    listTemp.add(1,String.valueOf(OrderPrice));
                                    listTemp.add(2,String.valueOf(TotalPriceForQty));
                                    billdetails.put(MenuName, listTemp);
                                }*/
                                if (billdetails.containsKey(menuName)) {
                                    OrderDetailsDTO hshOrder = billdetails.get(menuId);
                                    hshOrder.setOrderQuantity(hshOrder.getOrderQuantity() + menuQty);
                                    hshOrder.setOrderPrice(hshOrder.getOrderPrice() + orderPrice);
                                } else {
                                    billdetails.put(menuName, new OrderDetailsDTO(0, orderPrice, menuQty, "", menuId,
                                            menuName, 0.0, ""));
                                }

                            } while (cursor.moveToNext());
                        }
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while getting oderId " + e.toString());
        } finally {

            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Menu Details", errorMessage);
        }
        return billdetails;
    }

    public RestaurantDTO getRestaurantDetails(String restaurantName) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getReadableDatabase();
        RestaurantDTO restaurantDTO = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {

                String[] where = new String[]{restaurantName};
                cursor = sqLiteDatabase.rawQuery("SELECT * " +
                        "from restaurant  where " + SqlContract.SqlRestaurant.RESTAURANT_NAME + " =?", where);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {

                            int restaurantId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlRestaurant.RESTAURANT_ID));
                            String restaurantNM = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRestaurant.RESTAURANT_NAME));
                            String restaurantLogUrl = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRestaurant.RESTAURANT_URL));
                            String restaurantAddress = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRestaurant.RESTAURANT_ADDRESS));
                            String restaurantArea = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRestaurant.RESTAURANT_AREA));
                            String restaurantCity = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRestaurant.RESTAURANT_CITY));
                            String restaurantCountry = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRestaurant.RESTAURANT_COUNTRY));
                            String restaurantPhone = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRestaurant.RESTAURANT_PHONE));
                            String restaurantFooter = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRestaurant.RESTAURANT_FOOTER));
                            restaurantDTO = new RestaurantDTO(restaurantId, restaurantNM, restaurantLogUrl,
                                    restaurantAddress, restaurantArea, restaurantCity, restaurantCountry,
                                    restaurantPhone, restaurantFooter);

                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while getting oderId " + e.toString());
        } finally {

            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Restaurant details", errorMessage);
        }
        return restaurantDTO;
    }

    public int getConfigValue(String configKey) {
        boolean flagError = false;
        String errorMessage = "";
        int result = 0;
        SQLiteDatabase sqLiteDatabase = null;
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {

                String[] where = new String[]{configKey};
                cursor = sqLiteDatabase.rawQuery("SELECT * " +
                        "from r_config_settings  where " + SqlContract.SqlRConfigSettings.CONFIG_KEY + " =?", where);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        result = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlRConfigSettings.CONFIG_VALUE));
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Get Config value" + e.toString());
        } finally {

            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Config value", errorMessage);
        }
        return result;
    }

    public ArrayList<SubMenuDTO> getSubMenu(int menuId, String custId) {
        boolean flagError = false;
        String errorMessage = "";
        ArrayList<SubMenuDTO> subMenu = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] where = new String[]{String.valueOf(menuId)};

                cursor = sqLiteDatabase.rawQuery("SELECT Distinct sub_menu.SubMenuId,(Select temp_order.Quantity " +
                        "from temp_order where sub_menu.SubMenuId=temp_order.SubMenuId " +
                        "and temp_order.CustId='" + custId + "') as Quantity,sub_menu.SubMenuTitle,sub_menu.Price " +
                        "from sub_menu left join temp_order on sub_menu.MenuId=temp_order.MenuId Where sub_menu.MenuId=?", where);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            int subMenuId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlSubMenu.SUB_MENU_ID));
                            //int menuId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlRPrinters.IP_ADDRESS));
                            String menuTitle = cursor.getString(cursor.getColumnIndex(SqlContract.SqlSubMenu.MENU_TITLE));
                            double price = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlMenu.PRICE));
                            int quantity = cursor.getInt(cursor.getColumnIndex("Quantity"));
                            SubMenuDTO subMenuDTO = new SubMenuDTO(subMenuId, menuId, menuTitle, price, quantity);
                            subMenu.add(subMenuDTO);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e("DbOperationsEx", "Error while getting sub Menu data " + e.toString());
        } finally {

            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Sub Menu", errorMessage);
        }
        return subMenu;
    }

    public ArrayList<DeliveryDTO> getDeliveryList() {
        boolean flagError = false;
        String errorMessage = "";
        ROrderDateUtils dateUtils = new ROrderDateUtils();
        String date = dateUtils.getLocalSQLCurrentDate();
        String time = dateUtils.getSqlOffsetTime(AppConstants.ORDER_TIME_HOUR, AppConstants.ORDER_TIME_MINUTE);

        ArrayList<DeliveryDTO> deliveryDTOs = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String strQuery = "Select delivery.DeliveryId,delivery.DeliveryNo,delivery.CustId,delivery.CreatedDate," +
                        "delivery.SourceId,delivery.Discount,delivery.DeliveryCharges," +
                        "delivery.UserId,users.UserName,customer.CustName,customer.CustPhone," +
                        "customer.CustAddress,takeaway_source.SourceName " +
                        "from delivery left join users on users.UserId=delivery.UserId " +
                        "left join customer on customer.CustId=delivery.CustId " +
                        "left join takeaway_source on takeaway_source.SourceId=delivery.SourceId " +
                        "where delivery.CreatedDate>= '" + date + "T" + time + "'";
                cursor = sqLiteDatabase.rawQuery(strQuery, null);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            String deliveryId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlDelivery.DELIVERY_ID));
                            int deliveryNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlDelivery.DELIVERY_NO));
                            double discount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlDelivery.DISCOUNT));
                            double deliveryCharges = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlDelivery.DELIVERY_CHG));
                            String custId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlDelivery.CUST_ID));
                            int userId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlDelivery.USER_ID));
                            int sourceId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlDelivery.SOURCE_ID));
                            String custName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlCustomer.CUST_NAME));
                            String custAddress = cursor.getString(cursor.getColumnIndex("CustAddress"));
                            String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                            String custPhone = cursor.getString(cursor.getColumnIndex(SqlContract.SqlCustomer.CUST_PHONE));
                            String sourceName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTakeAwaySource.SOURCE_NAME));
                            String createdDate = cursor.getString(cursor.getColumnIndex(SqlContract.SqlDelivery.DATE));
                            DeliveryDTO deliveryDTO = new DeliveryDTO(deliveryId, deliveryNo, discount,
                                    deliveryCharges, custId, userId, sourceId, custName, custAddress, userName, custPhone, sourceName);
                            deliveryDTOs.add(deliveryDTO);
                        } while (cursor.moveToNext());
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            e.printStackTrace();
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error in get delivery list" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get delivery list", errorMessage);
        }
        return deliveryDTOs;
    }

    public void setDeliveryStatus(ArrayList<DeliveryDTO> deliveryDTOs) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;

        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                for (DeliveryDTO deliveryDTO : deliveryDTOs) {
                    int countSPending = 0, countSReady = 0, countSDelivered = 0;
                    String[] where = new String[]{String.valueOf(deliveryDTO.getmDeliveryNo())};
                    String strQuery = "Select " + SqlContract.SqlOrders.ORDER_STATUS + " FROM "
                            + SqlContract.SqlOrders.TABLE_NAME + " Where " + SqlContract.SqlOrders.TAKE_AWAY_NO + "=?";
                    cursor = sqLiteDatabase.rawQuery(strQuery, where);
                    if (cursor != null) {

                        if (cursor.getCount() > 0) {
                            cursor.moveToFirst();
                            do {
                                int status = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlOrders.ORDER_STATUS));
                                if (status == AppConstants.TAKAWAY_STATUS_PENDING)
                                    countSPending = countSPending + 1;
                                if (status == AppConstants.TAKAWAY_STATUS_READY)
                                    countSReady = countSReady + 1;
                                if (status == AppConstants.TAKAWAY_STATUS_DELIVERED)
                                    countSDelivered = countSDelivered + 1;
                            } while (cursor.moveToNext());
                        }
                        if (countSPending > 0)
                            deliveryDTO.setOrderStatus(AppConstants.TAKAWAY_STATUS_PENDING);
                        if (cursor.getCount() == countSReady)
                            deliveryDTO.setOrderStatus(AppConstants.TAKAWAY_STATUS_READY);
                        if (cursor.getCount() == countSDelivered)
                            deliveryDTO.setOrderStatus(AppConstants.TAKAWAY_STATUS_DELIVERED);
                    }

                }


            }
            flagError = true;
        } catch (Exception e) {
            e.printStackTrace();
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error in setTakeAwayStatus" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "set Take away status", errorMessage);
        }


    }

    public int getCompleteddeliveryCount() {
        boolean flagError = false;
        String errorMessage = "";
        int count = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select * From " + SqlContract.SqlOrders.TABLE_NAME + " where OrderType = 2 and OrderStatus = 2", null);
                count = cursor.getCount();
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "## error at getTakeAwayCount table" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Get Take away Complete count", errorMessage);
        }
        return count;
    }

    public boolean insertDelivery(UploadDelivery deliveryDto, int userId) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                contentValues.put(SqlContract.SqlDelivery.DELIVERY_ID, deliveryDto.getDeliveryId());
                contentValues.put(SqlContract.SqlDelivery.DISCOUNT, deliveryDto.getDiscount());
                contentValues.put(SqlContract.SqlDelivery.DELIVERY_CHG, deliveryDto.getDeliveryCharges());
                contentValues.put(SqlContract.SqlDelivery.CUST_ID, deliveryDto.getCustId());
                contentValues.put(SqlContract.SqlDelivery.USER_ID, userId);
                contentValues.put(SqlContract.SqlDelivery.SOURCE_ID, deliveryDto.getSourceId());
                if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                count = sqLiteDatabase.insert(SqlContract.SqlDelivery.TABLE_NAME, null, contentValues);
                contentValues.clear();
                Log.d(TAG, "## Delivery is added successfully using UploadDelivery" + deliveryDto.getDeliveryId());

            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at insert delivery using UploadDelivery" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Delivery", errorMessage);
        }
        return count != -1;
    }

    public boolean insertDelivery(List<DeliveryDbDTO> deliveryInserts) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (DeliveryDbDTO deliveryDbDTO : deliveryInserts) {
                    contentValues.put(SqlContract.SqlDelivery.DELIVERY_ID, deliveryDbDTO.getDeliveryId());
                    contentValues.put(SqlContract.SqlDelivery.DELIVERY_NO, deliveryDbDTO.getDeliveryNo());
                    contentValues.put(SqlContract.SqlDelivery.DISCOUNT, deliveryDbDTO.getDiscount());
                    contentValues.put(SqlContract.SqlDelivery.DELIVERY_CHG, deliveryDbDTO.getDeliveryCharges());
                    contentValues.put(SqlContract.SqlDelivery.CUST_ID, deliveryDbDTO.getCustId());
                    contentValues.put(SqlContract.SqlDelivery.USER_ID, deliveryDbDTO.getUserId());
                    contentValues.put(SqlContract.SqlDelivery.SOURCE_ID, deliveryDbDTO.getSourceId());
                    contentValues.put(SqlContract.SqlDelivery.DATE, deliveryDbDTO.getCreatedDate());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.insert(SqlContract.SqlDelivery.TABLE_NAME, null, contentValues);
                    contentValues.clear();
                    Log.d(TAG, "## Delivery is added successfully" + deliveryDbDTO.getDeliveryNo());

                }
                flagError = true;
            }
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at insert Delivery" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Insert Delivery", errorMessage);
        }
        return count != -1;
    }

    public boolean updateDelivery(List<DeliveryDbDTO> updateDelivery) {
        boolean flagError = false;
        String errorMessage = "";
        SQLiteDatabase sqLiteDatabase = null;
        ContentValues contentValues = null;
        long count = -1;
        try {
            sqLiteDatabase = getWritableDatabase();
            synchronized (sqLiteDatabase) {
                contentValues = new ContentValues();
                for (DeliveryDbDTO deliveryDbDTO : updateDelivery) {
                    String[] where = new String[]{deliveryDbDTO.getDeliveryId()};
                    if (deliveryDbDTO.getDeliveryNo() != 0)
                        contentValues.put(SqlContract.SqlDelivery.DELIVERY_NO, deliveryDbDTO.getDeliveryNo());
                    if (deliveryDbDTO.getDiscount() != 0)
                        contentValues.put(SqlContract.SqlDelivery.DISCOUNT, deliveryDbDTO.getDiscount());
                    if (deliveryDbDTO.getDeliveryCharges() != 0)
                        contentValues.put(SqlContract.SqlDelivery.DELIVERY_CHG, deliveryDbDTO.getDeliveryCharges());
                    if (deliveryDbDTO.getCustId() != null && !deliveryDbDTO.getCustId().isEmpty())
                        contentValues.put(SqlContract.SqlDelivery.CUST_ID, deliveryDbDTO.getCustId());
                    if (deliveryDbDTO.getUserId() != 0)
                        contentValues.put(SqlContract.SqlDelivery.USER_ID, deliveryDbDTO.getUserId());
                    if (deliveryDbDTO.getSourceId() != 0)
                        contentValues.put(SqlContract.SqlDelivery.SOURCE_ID, deliveryDbDTO.getSourceId());
                    if (deliveryDbDTO.getCreatedDate() != null)
                        contentValues.put(SqlContract.SqlDelivery.DATE, deliveryDbDTO.getCreatedDate());
                    if (!sqLiteDatabase.isOpen()) sqLiteDatabase = getWritableDatabase();
                    count = sqLiteDatabase.update(SqlContract.SqlDelivery.TABLE_NAME, contentValues,
                            SqlContract.SqlDelivery.DELIVERY_ID + "=?", where);
                    contentValues.clear();
                    Log.d(TAG, "## Take Delivery is updated successfully" + deliveryDbDTO.getDeliveryNo());

                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.d(TAG, "## Error at update delivery" + e.toString());
        } finally {
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "Update Delivery", errorMessage);
        }
        return count != -1;
    }

    public double getDeliveryDiscount(int deliveryNo) {
        boolean flagError = false;
        String errorMessage = "";
        double discount = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        OrderHeaderDTO orderHeaderDTO = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{String.valueOf(deliveryNo)};
                cursor = sqLiteDatabase.rawQuery("select " + SqlContract.SqlDelivery.DISCOUNT + " from "
                        + SqlContract.SqlDelivery.TABLE_NAME + " where " +
                        SqlContract.SqlDelivery.DELIVERY_NO + "=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        discount = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlDelivery.DISCOUNT));
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get delivery Discount", errorMessage);
        }
        return discount;
    }

    public double getDeliveryDeliveryChr(int deliveryNo) {
        boolean flagError = false;
        String errorMessage = "";
        double deliveryChr = 0;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        OrderHeaderDTO orderHeaderDTO = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                String[] whereClause = new String[]{String.valueOf(deliveryNo)};
                cursor = sqLiteDatabase.rawQuery("select " + SqlContract.SqlDelivery.DELIVERY_CHG + " from "
                        + SqlContract.SqlDelivery.TABLE_NAME + " where " +
                        SqlContract.SqlDelivery.DELIVERY_NO + "=?", whereClause);
                if (cursor != null) {
                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        deliveryChr = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlDelivery.DELIVERY_CHG));
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Take away Delivery Charges", errorMessage);
        }
        return deliveryChr;
    }

    public DeliveryDTO getDelivery(int deliveryNo) {
        boolean flagError = false;
        String errorMessage = "";
        DeliveryDTO deliveryDTO = null;
        SQLiteDatabase sqLiteDatabase = null;
        Cursor cursor = null;
        try {
            sqLiteDatabase = getReadableDatabase();
            synchronized (sqLiteDatabase) {
                cursor = sqLiteDatabase.rawQuery("Select delivery.DeliveryId,delivery.DeliveryNo," +
                        "delivery.CustId,delivery.SourceId,delivery.Discount,delivery.DeliveryCharges," +
                        "delivery.UserId,users.UserName,customer.CustName,customer.CustPhone," +
                        "customer.CustAddress,takeaway_source.SourceName from delivery left join " +
                        "users on users.UserId=delivery.UserId left join customer on customer.CustId=" +
                        "delivery.CustId left join takeaway_source on takeaway_source.SourceId=delivery.SourceId " +
                        "where delivery.DeliveryNo=" + deliveryNo, null);
                if (cursor != null) {

                    if (cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        String deliveryId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlDelivery.DELIVERY_ID));
                        //int delNo = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlTakeAway.TAKE_AWAY_NO));
                        double discount = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlDelivery.DISCOUNT));
                        double deliveryCharges = cursor.getDouble(cursor.getColumnIndex(SqlContract.SqlDelivery.DELIVERY_CHG));
                        String custId = cursor.getString(cursor.getColumnIndex(SqlContract.SqlDelivery.CUST_ID));
                        int userId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlDelivery.USER_ID));
                        int sourceId = cursor.getInt(cursor.getColumnIndex(SqlContract.SqlDelivery.SOURCE_ID));
                        String custName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlCustomer.CUST_NAME));
                        String custAddress = cursor.getString(cursor.getColumnIndex("CustAddress"));
                        String userName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlUser.USER_NAME));
                        String custPhone = cursor.getString(cursor.getColumnIndex(SqlContract.SqlCustomer.CUST_PHONE));
                        String sourceName = cursor.getString(cursor.getColumnIndex(SqlContract.SqlTakeAwaySource.SOURCE_NAME));
                        deliveryDTO = new DeliveryDTO(deliveryId, deliveryNo, discount,
                                deliveryCharges, custId, userId, sourceId, custName, custAddress, userName, custPhone, sourceName);
                    }
                }
            }
            flagError = true;
        } catch (Exception e) {
            e.printStackTrace();
            flagError = false;
            errorMessage = e.getMessage();
            Log.e(TAG, "Error in get delivery" + e.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (sqLiteDatabase != null && sqLiteDatabase.isOpen())
                sqLiteDatabase.close();
            if (!flagError)
                addError(TAG, "get Delivery ", errorMessage);
        }
        return deliveryDTO;
    }

    public void addError(String screenName, String method, String desc) {
        Gson gson = new Gson();
        ROrderDateUtils dateUtils = new ROrderDateUtils();
        ApplicationErrorDBDTO errorDBDTO = new ApplicationErrorDBDTO(screenName, method, desc, dateUtils.getLocalSQLCurrentDate(), dateUtils.getLocalCurrentTime());
        String serializedError = gson.toJson(errorDBDTO);
        addDataToSync(ConstantOperations.ADD_APPLICATION_ERROR, "", serializedError);
    }
}
/*
"SELECT Distinct menu.MenuId,menu.FoodType,menu.Image,menu.FbTypeId,"+
        "menu.MenuTitle,menu.Tags,menu.IsSpicy,menu_category.CategoryTitle,"+
        "menu.AvailabilityStatus,menu.Active,menu.Price ,(Select temp_order.Quantity "+
        "from temp_order   where menu.MenuId=temp_order.MenuId and temp_order.CustId=?)"+
        " as Quantity From menu Left Join menu_category on menu.CategoryId="+
        "menu_category.CategoryId left join temp_order on menu.MenuId=temp_order.MenuId "+
        "where menu.Active=1"*/
