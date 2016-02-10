package com.vibeosys.rorderapp.util;

import android.view.Menu;

import com.vibeosys.rorderapp.data.BillDbDTO;
import com.vibeosys.rorderapp.data.BillDetailsDbDTO;
import com.vibeosys.rorderapp.data.HotelTableDbDTO;
import com.vibeosys.rorderapp.data.MenuCateoryDbDTO;
import com.vibeosys.rorderapp.data.MenuDbDTO;
import com.vibeosys.rorderapp.data.MenuTagsDbDTO;
import com.vibeosys.rorderapp.data.OrderDetailsDbDTO;
import com.vibeosys.rorderapp.data.OrdersDbDTO;
import com.vibeosys.rorderapp.data.TableCategoryDbDTO;
import com.vibeosys.rorderapp.data.UserDbDTO;
import com.vibeosys.rorderapp.database.DbRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 23-01-2016.
 */
public class DbOperations {

    private DbRepository dbRepository;

    public DbOperations(DbRepository dbRepository) {
        this.dbRepository = dbRepository;
    }

    public boolean addOrUpdateBills(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {
        List<BillDbDTO> billInserts = BillDbDTO.deserializeBill(jsonInsertList);
        List<BillDbDTO> billUpdates = BillDbDTO.deserializeBill(updateJsonList);

        boolean isInserted = dbRepository.insertBills(billInserts);
        boolean isUpdated = dbRepository.updateBills(billUpdates);
        return isInserted & isUpdated;
    }

    public boolean addOrUpdateBillDetails(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {
        List<BillDetailsDbDTO> billDetailInserts = BillDetailsDbDTO.deserializeBillDetails(jsonInsertList);
        List<BillDetailsDbDTO> billDetailUpdates = BillDetailsDbDTO.deserializeBillDetails(updateJsonList);

        boolean isInserted = dbRepository.insertBillDetails(billDetailInserts);
        boolean isUpdated = dbRepository.updateBillDetails(billDetailUpdates);
        //return isInserted & isUpdated;
        return isInserted;
    }

    public boolean addOrUpdateMenu(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {

        List<MenuDbDTO> menuInserts = MenuDbDTO.deserializeMenu(jsonInsertList);
        List<MenuDbDTO> menuUpdates = MenuDbDTO.deserializeMenu(updateJsonList);

        boolean isInserted = dbRepository.insertMenus(menuInserts);
        // Remove Comment after update schema will done
        //boolean isUpdated = dbRepository.updateDestinations(menuUpdates);
        //return isInserted & isUpdated;
        return isInserted;
    }

    public boolean addOrUpdateMenuCategory(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {

        List<MenuCateoryDbDTO> menuCategoryInserts = MenuCateoryDbDTO.deserializeMenuCateory(jsonInsertList);
        List<MenuCateoryDbDTO> menuCategoryUpdates = MenuCateoryDbDTO.deserializeMenuCateory(updateJsonList);

        boolean isInserted = dbRepository.insertMenuCategory(menuCategoryInserts);
        // Remove Comment after update schema will done
        //boolean isUpdated = dbRepository.updateMenuCategory(menuCategoryUpdates);
        //return isInserted & isUpdated;
        return isInserted;
    }

    public boolean addOrUpdateMenuTags(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {
        List<MenuTagsDbDTO> menuTagInserts = MenuTagsDbDTO.deserializeMenuTag(jsonInsertList);
        List<MenuTagsDbDTO> menuTagUpdates = MenuTagsDbDTO.deserializeMenuTag(updateJsonList);

        boolean isInserted = dbRepository.insertMenuTags(menuTagInserts);
        // Remove Comment after update schema will done
        //boolean isUpdated = dbRepository.updateMenuTags(menuTagUpdates);
        return isInserted;// & isUpdated;
    }

    public boolean addOrUpdateOrderDetails(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {
        List<OrderDetailsDbDTO> orderDetailInserts = OrderDetailsDbDTO.deserializeOrderDetail(jsonInsertList);
        List<OrderDetailsDbDTO> orderDetailUpdates = OrderDetailsDbDTO.deserializeOrderDetail(updateJsonList);

        boolean isInserted = dbRepository.insertOrderDetails(orderDetailInserts);
        // Remove Comment after update schema will done
        //boolean isUpdated = dbRepository.updateOrderDetails(orderDetailUpdates);
        return isInserted;// & isUpdated;
    }

    public boolean addOrUpdateOrder(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {
        List<OrdersDbDTO> orderInserts = OrdersDbDTO.deserializeOrders(jsonInsertList);
        List<OrdersDbDTO> orderUpdates = OrdersDbDTO.deserializeOrders(updateJsonList);

        boolean isInserted = dbRepository.insertOrders(orderInserts);
        // Remove Comment after update schema will done
        //boolean isUpdated = dbRepository.updateOrders(orderUpdates);
        return isInserted;// & isUpdated;
    }

    public boolean addOrUpdateRTable(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {

        List<HotelTableDbDTO> rTableInserts = HotelTableDbDTO.deserializeHotelTables(jsonInsertList);
        List<HotelTableDbDTO> rTableUpdates = HotelTableDbDTO.deserializeHotelTables(updateJsonList);

        boolean isInserted = dbRepository.insertTables(rTableInserts);
        // Remove Comment after update schema will done
        //boolean isUpdated = dbRepository.updateRTables(rTableUpdates);
        return isInserted;// & isUpdated;
    }

    public boolean addOrUpdateTableCategory(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {
        List<TableCategoryDbDTO> tableCategoryInsert = TableCategoryDbDTO.deserializeTableCateory(jsonInsertList);
        List<TableCategoryDbDTO> tableCategoryUpdate = TableCategoryDbDTO.deserializeTableCateory(updateJsonList);

        boolean isInserted = dbRepository.insertTableCategories(tableCategoryInsert);
        // Remove Comment after update schema will done
        //boolean isUpdated = dbRepository.updateTableCategories(tableCategoryUpdate);
        return isInserted;// & isUpdated;
    }

    public boolean addOrUpdateUser(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {

        List<UserDbDTO> userInserts = UserDbDTO.deserializeUser(jsonInsertList);
        List<UserDbDTO> userUpdates = UserDbDTO.deserializeUser(updateJsonList);

        boolean isInserted = dbRepository.insertUsers(userInserts);
        // Remove Comment after update schema will done
        //boolean isUpdated = dbRepository.updateUsers(userUpdates);
        return isInserted;// & isUpdated;
    }
}
