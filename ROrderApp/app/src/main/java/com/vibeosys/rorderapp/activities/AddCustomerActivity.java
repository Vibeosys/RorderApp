package com.vibeosys.rorderapp.activities;

import android.app.Dialog;
import android.content.Context;
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
import com.vibeosys.rorderapp.MainActivity;
import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.CustomerAdapter;
import com.vibeosys.rorderapp.data.CustomerDbDTO;
import com.vibeosys.rorderapp.data.TableDataDTO;
import com.vibeosys.rorderapp.data.TableTransactionDbDTO;
import com.vibeosys.rorderapp.data.UploadOccupiedDTO;
import com.vibeosys.rorderapp.data.WaitingUserDTO;
import com.vibeosys.rorderapp.util.ConstantOperations;
import com.vibeosys.rorderapp.util.ROrderDateUtils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by akshay on 08-02-2016.
 */
public class AddCustomerActivity extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    private EditText mTxtName, mTxtCount;
    private Button mBtnAdd;
    private ListView mListCustomer;
    private CustomerAdapter mCustomerAdapter;
    private ArrayList<WaitingUserDTO> mWaitingList;
    private Context mContext=this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        setTitle("Waiting List");
        mWaitingList =mDbRepository.getWaitingList();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTxtCount =(EditText)findViewById(R.id.txtCustCount);
        mTxtName =(EditText)findViewById(R.id.txtCustomerName);
        mBtnAdd =(Button)findViewById(R.id.btnAdd);
        mBtnAdd.setOnClickListener(this);
        mListCustomer =(ListView)findViewById(R.id.customerList);
        mCustomerAdapter =new CustomerAdapter(getApplicationContext(), mWaitingList);
        mListCustomer.setAdapter(mCustomerAdapter);
        mCustomerAdapter.notifyDataSetChanged();
        mListCustomer.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btnAdd)
        {
            addUser();
        }
    }

    private void addUser() {
        boolean wrongCredential=false;
        View focus=null;
        mTxtName.setError(null);
        mTxtCount.setError(null);

        String customerName= mTxtName.getText().toString();
        String strCount= mTxtCount.getText().toString();

        if(TextUtils.isEmpty(customerName))
        {
            mTxtName.setError("Customer Name is Required");
            focus= mTxtName;
            wrongCredential=true;
        }
        if(TextUtils.isEmpty(strCount))
        {
            mTxtCount.setError("Customer Count is Required");
            focus= mTxtCount;
            wrongCredential=true;
        }

        if(wrongCredential)
        {
            focus.requestFocus();
        }
        else {
            UUID custid = UUID.randomUUID();
            int customerCount=0;
            try{
                customerCount=Integer.parseInt(strCount);
            }catch (NumberFormatException e)
            {
                Log.e(TAG, "## Insert Count null pointer" + e.toString());
            }

            CustomerDbDTO customer=new CustomerDbDTO(custid.toString(),customerName);
            mDbRepository.insertCustomerDetails(customer);
            String currentDate=new ROrderDateUtils().getGMTCurrentDate();
            Log.d(TAG, "##" + currentDate);
            TableTransactionDbDTO tableTransaction=new TableTransactionDbDTO(custid.toString(),true, Date.valueOf(currentDate),customerCount);
            mDbRepository.insertTableTransaction(tableTransaction);
            Toast.makeText(getApplicationContext(),"Customer is Added successfully",Toast.LENGTH_SHORT).show();
            mCustomerAdapter.refresh(mDbRepository.getWaitingList());
            mTxtCount.setText("");
            mTxtName.setText("");
            uploadToServer(customer, tableTransaction);

        }
    }

    private void uploadToServer(CustomerDbDTO customer, TableTransactionDbDTO tableTransaction) {
        Gson gson=new Gson();
        String serializedJsonString = gson.toJson(customer);
        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.ADD_CUSTOMER, serializedJsonString);
        mServerSyncManager.uploadDataToServer(tableDataDTO);

        /*String serializedTableTransaction=gson.toJson(tableTransaction);
        tableDataDTO = new TableDataDTO(ConstantOperations.GENRATE_BILL, serializedTableTransaction);
        mServerSyncManager.uploadDataToServer(tableDataDTO);*/
    }

    private void showMyDialog(final WaitingUserDTO waiting) {

        final Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_table_alocate);
        setTitle(getResources().getString(R.string.dialog_title_Allocate));
        final EditText txtTableNo=(EditText)dialog.findViewById(R.id.txtTableNumber);
        TextView txtReserve=(TextView)dialog.findViewById(R.id.txtReserve);
        TextView txtCancel=(TextView)dialog.findViewById(R.id.txtCancel);

        txtReserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTableNo=txtTableNo.getText().toString();
                if(TextUtils.isEmpty(strTableNo))
                {
                    txtTableNo.setError(getResources().getString(R.string.error_table_no));
                    txtTableNo.requestFocus();
                }
                else {
                    int tableId=mDbRepository.getTaleId(Integer.parseInt(strTableNo));
                    if(tableId==-1)
                    {
                        Toast.makeText(getApplicationContext(),"Table is Already Occupied",Toast.LENGTH_SHORT).show();
                    }
                    else if(tableId==0)
                    {
                        Toast.makeText(getApplicationContext(),"No Such Table found",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        TableTransactionDbDTO tableTransactionDbDTO=new TableTransactionDbDTO(tableId,
                                mSessionManager.getUserId(),waiting.getmCustomerId(),false,
                                waiting.getmArrivalTime(),waiting.getmOccupancy());
                        mDbRepository.updateTableTransaction(tableTransactionDbDTO);
                        mDbRepository.setOccupied(true, tableId);
                        UploadOccupiedDTO occupiedDTO=new UploadOccupiedDTO(tableId,1);
                        Gson gson=new Gson();
                        String serializedJsonString = gson.toJson(occupiedDTO);
                        TableDataDTO tableDataDTO = new TableDataDTO(ConstantOperations.TABLE_OCCUPIED, serializedJsonString);
                        mServerSyncManager.uploadDataToServer(tableDataDTO);
                        dialog.dismiss();
                        mCustomerAdapter.refresh(mDbRepository.getWaitingList());
                        Intent iMain=new Intent(getApplicationContext(), MainActivity.class);
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
        WaitingUserDTO waiting=(WaitingUserDTO) mCustomerAdapter.getItem(position);
        showMyDialog(waiting);
    }
}
