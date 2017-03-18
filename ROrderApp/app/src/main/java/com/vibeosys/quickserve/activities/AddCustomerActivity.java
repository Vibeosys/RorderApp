package com.vibeosys.quickserve.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vibeosys.quickserve.MainActivity;
import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.adaptors.CustomerAdapter;
import com.vibeosys.quickserve.data.CustomerDbDTO;
import com.vibeosys.quickserve.data.TableDataDTO;
import com.vibeosys.quickserve.data.TableTransactionDbDTO;
import com.vibeosys.quickserve.data.UploadOccupiedDTO;
import com.vibeosys.quickserve.data.WaitingUserDTO;
import com.vibeosys.quickserve.util.ConstantOperations;
import com.vibeosys.quickserve.util.ROrderDateUtils;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by akshay on 08-02-2016.
 */
public class AddCustomerActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private EditText mTxtName, mTxtCount;
    private Button mBtnAdd;
    private ListView mListCustomer;
    private CustomerAdapter mCustomerAdapter;
    private ArrayList<WaitingUserDTO> mWaitingList;
    private Context mContext = this;

    @Override
    protected String getScreenName() {
        return "New Customer";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        setTitle("Waiting List");
        mWaitingList = mDbRepository.getWaitingList();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTxtCount = (EditText) findViewById(R.id.txtCustCount);
        mTxtName = (EditText) findViewById(R.id.txtCustomerName);
        mBtnAdd = (Button) findViewById(R.id.btnAdd);
        mBtnAdd.setOnClickListener(this);
        mListCustomer = (ListView) findViewById(R.id.customerList);
        mCustomerAdapter = new CustomerAdapter(getApplicationContext(), mWaitingList);
        mListCustomer.setAdapter(mCustomerAdapter);
        mCustomerAdapter.notifyDataSetChanged();
        mListCustomer.setLongClickable(true);
        mListCustomer.setOnItemClickListener(this);
        mListCustomer.setOnItemLongClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnAdd) {
            addUser();
            sendEventToGoogle("Action", "Add Customer");
        }
    }


    private void addUser() {
        boolean wrongCredential = false;
        View focus = null;
        mTxtName.setError(null);
        mTxtCount.setError(null);

        String customerName = mTxtName.getText().toString();
        String strCount = mTxtCount.getText().toString();

        if (TextUtils.isEmpty(customerName)) {
            mTxtName.setError("Customer Name is Required");
            focus = mTxtName;
            wrongCredential = true;
        }
        if (TextUtils.isEmpty(strCount)) {
            mTxtCount.setError("Customer Count is Required");
            focus = mTxtCount;
            wrongCredential = true;
        }

        if (wrongCredential) {
            focus.requestFocus();
        } else {
            UUID custid = UUID.randomUUID();
            int customerCount = 0;
            try {
                customerCount = Integer.parseInt(strCount);
            } catch (NumberFormatException e) {
                Log.e(TAG, "## Insert Count null pointer" + e.toString());
            }

            CustomerDbDTO customer = new CustomerDbDTO(custid.toString(), customerName);
            mDbRepository.insertCustomerDetails(customer);
            String currentDate = new ROrderDateUtils().getGMTCurrentDate();
            Log.d(TAG, "##" + currentDate);
            TableTransactionDbDTO tableTransaction = new TableTransactionDbDTO(custid.toString(), 1, customerCount);
            mDbRepository.insertTableTransaction(tableTransaction);
            Toast.makeText(getApplicationContext(), "Customer is Added successfully", Toast.LENGTH_SHORT).show();
            mCustomerAdapter.refresh(mDbRepository.getWaitingList());
            mTxtCount.setText("");
            mTxtName.setText("");
            uploadToServer(customer, tableTransaction);

        }
    }

    private void uploadToServer(CustomerDbDTO customer, TableTransactionDbDTO tableTransaction) {
        Gson gson = new Gson();
        TableDataDTO[] tableDataDTOs = new TableDataDTO[2];
        String serializedJsonString = gson.toJson(customer);
        //TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
        tableDataDTOs[0] = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
        String serializedTableTransaction = gson.toJson(tableTransaction);
        tableDataDTOs[1] = new TableDataDTO(ConstantOperations.ADD_WAITING_CUSTOMER, serializedTableTransaction);
        mServerSyncManager.uploadDataToServer(tableDataDTOs);
    }

    private void showMyDialog(final WaitingUserDTO waiting) {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_table_alocate);
        setTitle(getResources().getString(R.string.dialog_title_Allocate));
        final EditText txtTableNo = (EditText) dialog.findViewById(R.id.txtTableNumber);
        TextView txtReserve = (TextView) dialog.findViewById(R.id.txtReserve);
        TextView txtCancel = (TextView) dialog.findViewById(R.id.txtCancel);

        txtReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTableNo = txtTableNo.getText().toString();
                if (TextUtils.isEmpty(strTableNo)) {
                    txtTableNo.setError(getResources().getString(R.string.error_table_no));
                    txtTableNo.requestFocus();
                } else {
                    int tableId = mDbRepository.getTaleId(Integer.parseInt(strTableNo));
                    if (tableId == -1) {
                        Toast.makeText(getApplicationContext(), "Table is Already Occupied", Toast.LENGTH_SHORT).show();
                    } else if (tableId == 0) {
                        Toast.makeText(getApplicationContext(), "No Such Table found", Toast.LENGTH_SHORT).show();
                    } else {
                        TableTransactionDbDTO tableTransactionDbDTO = new TableTransactionDbDTO(tableId,
                                mSessionManager.getUserId(), waiting.getmCustomerId(), 0,
                                waiting.getmArrivalTime(), waiting.getmOccupancy());
                        mDbRepository.updateTableTransaction(tableTransactionDbDTO);
                        mDbRepository.setOccupied(true, tableId);

                        UploadOccupiedDTO occupiedDTO = new UploadOccupiedDTO(tableId, 1);
                        TableDataDTO[] tableDataDTOs = new TableDataDTO[2];
                        Gson gson = new Gson();
                        String serializedJsonString = gson.toJson(occupiedDTO);
                        tableDataDTOs[0] = new TableDataDTO(ConstantOperations.TABLE_OCCUPIED, serializedJsonString);
                        String serializedTableTransaction = gson.toJson(tableTransactionDbDTO);
                        tableDataDTOs[1] = new TableDataDTO(ConstantOperations.TABLE_TRANSACTION, serializedTableTransaction);
                        mServerSyncManager.uploadDataToServer(tableDataDTOs);

                        dialog.dismiss();
                        mCustomerAdapter.refresh(mDbRepository.getWaitingList());
                        Intent iMain = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(iMain);
                        finish();
                    }

                }

            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        WaitingUserDTO waiting = (WaitingUserDTO) mCustomerAdapter.getItem(position);
        showMyDialog(waiting);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        WaitingUserDTO customer = (WaitingUserDTO) mCustomerAdapter.getItem(position);
        confrmDeleteCustomer(customer);
        Log.d(TAG, "## Long Click on customer List");
        return true;
    }

    private void confrmDeleteCustomer(final WaitingUserDTO customer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Customer");
        //builder.setIcon(R.drawable.ic_action_warning_yellow);
        builder.setMessage("Are you sure to delete " + customer.getmCustomerName());
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean flagDeleteCust = mDbRepository.deleteCustomer(customer.getmCustomerId());
                if (flagDeleteCust) {
                    Gson gson = new Gson();
                    TableDataDTO[] tableDataDTOs = new TableDataDTO[2];
                    CustomerDbDTO deleteCustomer = new CustomerDbDTO(customer.getmCustomerId(), customer.getmCustomerName());
                    String serializedJsonString = gson.toJson(deleteCustomer);
                    //TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
                    tableDataDTOs[0] = new TableDataDTO(ConstantOperations.DELETE_CUSTOMER, serializedJsonString);
                    mServerSyncManager.uploadDataToServer(tableDataDTOs);
                    Toast.makeText(getApplicationContext(), "Customer Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
}
