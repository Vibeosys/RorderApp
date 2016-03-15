package com.vibeosys.rorderapp.util;

import android.util.Log;

import com.vibeosys.rorderapp.MainActivity;
import com.vibeosys.rorderapp.data.BillDbDTO;
import com.vibeosys.rorderapp.data.BillDetailsDbDTO;
import com.vibeosys.rorderapp.data.CustomerDbDTO;
import com.vibeosys.rorderapp.data.HotelTableDbDTO;
import com.vibeosys.rorderapp.data.MenuCateoryDbDTO;
import com.vibeosys.rorderapp.data.MenuDbDTO;
import com.vibeosys.rorderapp.data.MenuTagsDbDTO;
import com.vibeosys.rorderapp.data.OrderDetailsDbDTO;
import com.vibeosys.rorderapp.data.OrderTypeDbDTO;
import com.vibeosys.rorderapp.data.OrdersDbDTO;
import com.vibeosys.rorderapp.data.TableCategoryDbDTO;
import com.vibeosys.rorderapp.data.TableTransactionDbDTO;
import com.vibeosys.rorderapp.data.TakeAwayDTO;
import com.vibeosys.rorderapp.data.TakeAwayDbDTO;
import com.vibeosys.rorderapp.data.TakeAwaySourceDbDTO;
import com.vibeosys.rorderapp.data.UserDbDTO;
import com.vibeosys.rorderapp.database.DbRepository;
import com.vibeosys.rorderapp.fragments.FragmentChefMyServing;
import com.vibeosys.rorderapp.fragments.FragmentChefPlacedOrder;
import com.vibeosys.rorderapp.fragments.FragmentChefTabDiningOrders;
import com.vibeosys.rorderapp.fragments.FragmentChefTabMyPreviousOrders;
import com.vibeosys.rorderapp.fragments.FragmentChefTabMyServing;
import com.vibeosys.rorderapp.fragments.FragmentChefTabTakeAwayOrders;
import com.vibeosys.rorderapp.fragments.FragmentTakeAway;
import com.vibeosys.rorderapp.fragments.FragmentWaiterTable;

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
        return isInserted & isUpdated;

    }

    public boolean addOrUpdateMenu(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {

        List<MenuDbDTO> menuInserts = MenuDbDTO.deserializeMenu(jsonInsertList);
        List<MenuDbDTO> menuUpdates = MenuDbDTO.deserializeMenu(updateJsonList);

        boolean isInserted = dbRepository.insertMenus(menuInserts);

        boolean isUpdated = dbRepository.updateMenus(menuUpdates);
        return isInserted & isUpdated;

    }

    public boolean addOrUpdateMenuCategory(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {

        List<MenuCateoryDbDTO> menuCategoryInserts = MenuCateoryDbDTO.deserializeMenuCateory(jsonInsertList);
        List<MenuCateoryDbDTO> menuCategoryUpdates = MenuCateoryDbDTO.deserializeMenuCateory(updateJsonList);

        boolean isInserted = dbRepository.insertMenuCategory(menuCategoryInserts);
        boolean isUpdated = dbRepository.updateMenuCategory(menuCategoryUpdates);
        return isInserted & isUpdated;
    }

    public boolean addOrUpdateMenuTags(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {
        List<MenuTagsDbDTO> menuTagInserts = MenuTagsDbDTO.deserializeMenuTag(jsonInsertList);
        List<MenuTagsDbDTO> menuTagUpdates = MenuTagsDbDTO.deserializeMenuTag(updateJsonList);

        boolean isInserted = dbRepository.insertMenuTags(menuTagInserts);
        boolean isUpdated = dbRepository.updateMenuTags(menuTagUpdates);
        return isInserted & isUpdated;
    }

    public boolean addOrUpdateOrderDetails(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {
        List<OrderDetailsDbDTO> orderDetailInserts = OrderDetailsDbDTO.deserializeOrderDetail(jsonInsertList);
        List<OrderDetailsDbDTO> orderDetailUpdates = OrderDetailsDbDTO.deserializeOrderDetail(updateJsonList);

        boolean isInserted = dbRepository.insertOrderDetails(orderDetailInserts);

        boolean isUpdated = dbRepository.updateOrderDetails(orderDetailUpdates);

        return isInserted & isUpdated;
    }

    public boolean addOrUpdateOrder(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {
        List<OrdersDbDTO> orderInserts = OrdersDbDTO.deserializeOrders(jsonInsertList);
        List<OrdersDbDTO> orderUpdates = OrdersDbDTO.deserializeOrders(updateJsonList);

        boolean isInserted = dbRepository.insertOrders(orderInserts);
        boolean isUpdated = dbRepository.updateOrders(orderUpdates);
        if (FragmentChefMyServing.chefOrderAdapter != null) {
            FragmentChefMyServing.runOnUI(new Runnable() {
                @Override
                public void run() {

                    try {
                        FragmentChefMyServing.chefOrderAdapter.refresh(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("exception in my serving", "##" + e.toString());
                    }
                }
            });
        }

        if (FragmentChefPlacedOrder.chefOrderAdapter != null) {
            FragmentChefPlacedOrder.runOnUI(new Runnable() {
                @Override
                public void run() {
                    try {
                        FragmentChefPlacedOrder.chefOrderAdapter.refresh(2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }

        if (FragmentChefTabMyServing.adapterRecycle != null) {
            FragmentChefTabMyServing.runOnUI(new Runnable() {
                @Override
                public void run() {
                    try {

                        FragmentChefTabMyServing.adapterRecycle.refresh(2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (FragmentChefTabDiningOrders.adapterRecycleDining != null) {
            FragmentChefTabDiningOrders.runOnUI(new Runnable() {
                @Override
                public void run() {
                    try {
                        FragmentChefTabDiningOrders.adapterRecycleDining.refresh(2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (FragmentChefTabTakeAwayOrders.adapterRecycleTakeAway != null) {
            FragmentChefTabTakeAwayOrders.runOnUI(new Runnable() {
                @Override
                public void run() {
                    try {
                        FragmentChefTabTakeAwayOrders.adapterRecycleTakeAway.refresh(2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }
        if (FragmentChefTabMyPreviousOrders.adapterRecycle_previous == null) {
            FragmentChefTabMyPreviousOrders.runOnUI(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        FragmentChefTabMyPreviousOrders.adapterRecycle_previous.refresh(2);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });
        }
        if (FragmentTakeAway.gridAdapter != null) {
            FragmentTakeAway.runOnUI(new Runnable() {
                public void run() {
                    try {
                        ArrayList<TakeAwayDTO> takeAwayDTOs = dbRepository.getTakeAwayList();
                        dbRepository.setTakeAwayStatus(takeAwayDTOs);
                        FragmentTakeAway.gridAdapter.refresh(takeAwayDTOs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return isInserted & isUpdated;
    }

    public boolean addOrUpdateRTable(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {

        List<HotelTableDbDTO> rTableInserts = HotelTableDbDTO.deserializeHotelTables(jsonInsertList);
        List<HotelTableDbDTO> rTableUpdates = HotelTableDbDTO.deserializeHotelTables(updateJsonList);

        boolean isInserted = dbRepository.insertTables(rTableInserts);

        boolean isUpdated = dbRepository.updateRTables(rTableUpdates);
        return isInserted & isUpdated;
    }

    public boolean addOrUpdateTableCategory(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {
        List<TableCategoryDbDTO> tableCategoryInsert = TableCategoryDbDTO.deserializeTableCateory(jsonInsertList);
        List<TableCategoryDbDTO> tableCategoryUpdate = TableCategoryDbDTO.deserializeTableCateory(updateJsonList);

        boolean isInserted = dbRepository.insertTableCategories(tableCategoryInsert);

        boolean isUpdated = dbRepository.updateTableCategories(tableCategoryUpdate);
        return isInserted & isUpdated;
    }

    public boolean addOrUpdateUser(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {

        List<UserDbDTO> userInserts = UserDbDTO.deserializeUser(jsonInsertList);
        List<UserDbDTO> userUpdates = UserDbDTO.deserializeUser(updateJsonList);

        boolean isInserted = dbRepository.insertUsers(userInserts);
        boolean isUpdated = dbRepository.updateUsers(userUpdates);
        return isInserted & isUpdated;
    }

    public boolean addOrUpdateTakeAwaySource(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {

        List<TakeAwaySourceDbDTO> inserts = TakeAwaySourceDbDTO.deserializeTakeSource(jsonInsertList);
        List<TakeAwaySourceDbDTO> updates = TakeAwaySourceDbDTO.deserializeTakeSource(updateJsonList);

        boolean isInserted = dbRepository.insertTakeAwaySource(inserts);
        boolean isUpdated = dbRepository.updateTakeAwaySource(updates);
        return isInserted & isUpdated;
    }

    public boolean addOrUpdateTakeAway(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {

        List<TakeAwayDbDTO> inserts = TakeAwayDbDTO.deserializeTakeAway(jsonInsertList);
        List<TakeAwayDbDTO> updates = TakeAwayDbDTO.deserializeTakeAway(updateJsonList);

        boolean isInserted = dbRepository.insertTakeAway(inserts);
        boolean isUpdated = dbRepository.updateTakeAway(updates);
        FragmentTakeAway.runOnUI(new Runnable() {
            public void run() {
                try {
                    ArrayList<TakeAwayDTO> takeAwayDTOs = dbRepository.getTakeAwayList();
                    dbRepository.setTakeAwayStatus(takeAwayDTOs);
                    FragmentTakeAway.gridAdapter.refresh(takeAwayDTOs);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return isInserted & isUpdated;
    }

    public boolean addOrUpdateOrderType(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {

        List<OrderTypeDbDTO> inserts = OrderTypeDbDTO.deserializeOrderType(jsonInsertList);
        List<OrderTypeDbDTO> updates = OrderTypeDbDTO.deserializeOrderType(updateJsonList);

        boolean isInserted = dbRepository.insertOrderType(inserts);
        boolean isUpdated = dbRepository.updateOrderType(updates);
        return isInserted & isUpdated;
    }

    public boolean addOrUpdateCustomerDetails(ArrayList<String> jsonInsertList, ArrayList<String> updateJsonList) {
        List<CustomerDbDTO> customerInserts = CustomerDbDTO.deserializeCustomer(jsonInsertList);
        List<CustomerDbDTO> customerUpdates = CustomerDbDTO.deserializeCustomer(updateJsonList);

        boolean isInserted = dbRepository.insertListCustomerDetails(customerInserts);
        boolean isUpdated = dbRepository.updateCustomer(customerUpdates);
        return isInserted & isUpdated;
    }

    public boolean addOrUpdateTableTransaction(ArrayList<String> jsonInsertList,
                                               ArrayList<String> updateJsonList,
                                               ArrayList<String> delete, int myUserId) {
        List<TableTransactionDbDTO> tableTransactionInserts = TableTransactionDbDTO.deserializeTableTransaction(jsonInsertList);
        List<TableTransactionDbDTO> tableTransactionUpdates = TableTransactionDbDTO.deserializeTableTransaction(updateJsonList);
        List<TableTransactionDbDTO> tableTransactionDelete = TableTransactionDbDTO.deserializeTableTransaction(delete);
        boolean isInserted = dbRepository.insertTableTransactionList(tableTransactionInserts);
        boolean isUpdated = dbRepository.updateTableTransactionList(tableTransactionUpdates);
        boolean isDeleted = dbRepository.deleteTableTransaction(tableTransactionDelete);

        for (TableTransactionDbDTO deleteDto : tableTransactionDelete) {
            dbRepository.cleanData(deleteDto.getCustId(), deleteDto.getUserId(), myUserId);
        }
        FragmentWaiterTable.runOnUI(new Runnable() {
            public void run() {
                try {
                    FragmentWaiterTable.adapter.refresh(dbRepository.getTableRecords(""));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return isInserted & isUpdated & isDeleted;
    }
}
