package com.vibeosys.rorderapp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.data.CustomerDbDTO;

import java.util.UUID;

/**
 * Created by akshay on 08-02-2016.
 */
public class AddCustomerActivity extends BaseActivity implements View.OnClickListener{

    EditText txtName,txtCount;
    Button btnAdd;
    ListView listCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        setTitle("Waiting List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtCount=(EditText)findViewById(R.id.txtCustCount);
        txtName=(EditText)findViewById(R.id.txtCustomerName);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        listCustomer=(ListView)findViewById(R.id.customerList);
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
        txtName.setError(null);
        txtCount.setError(null);

        String customerName=txtName.getText().toString();
        int customerCount=Integer.parseInt(txtCount.getText().toString());
        if(TextUtils.isEmpty(customerName))
        {
            txtName.setError("Customer Name is Required");
            focus=txtName;
            wrongCredential=true;
        }
        if(TextUtils.isEmpty(String.valueOf(customerCount)))
        {
            txtCount.setError("Customer Count is Required");
            focus=txtCount;
            wrongCredential=true;
        }

        if(wrongCredential)
        {
            focus.requestFocus();
        }
        else {
            UUID custid = UUID.randomUUID();
            CustomerDbDTO customer=new CustomerDbDTO(custid.toString(),customerName,"","");
            mDbRepository.insertCustomerDetails(customer);
        }
    }
}
