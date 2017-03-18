package com.vibeosys.quickserve.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.vibeosys.quickserve.R;
import com.vibeosys.quickserve.adaptors.OrderSummaryAdapter;
import com.vibeosys.quickserve.data.OrderHeaderDTO;
import com.vibeosys.quickserve.data.TableCommonInfoDTO;

import java.util.ArrayList;

/**
 * Created by shrinivas on 02-02-2016.
 */
public class BillSummeryActivity extends BaseActivity {

    private ExpandableListView mOrdersList;
    private OrderSummaryAdapter mAdapter;
    private ArrayList<OrderHeaderDTO> mList = new ArrayList<>();
    private TableCommonInfoDTO tableCommonInfo;
    private int mTableNo;
    private int mTableId;
    private String mCustId;
    private Button mPrintBillDetails;

    @Override
    protected String getScreenName() {
        return "Bill Summary";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_summary);
        // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        tableCommonInfo = getIntent().getParcelableExtra("tableCustInfo");
        mTableNo = tableCommonInfo.getTableNo();
        mTableId = tableCommonInfo.getTableId();
        mCustId = tableCommonInfo.getCustId();
        mOrdersList = (ExpandableListView) findViewById(R.id.expListViewForTableOrder);
     //   mPrintBillDetails = (Button)findViewById(R.id.PrintBillDetails);
        mList = mDbRepository.getOrdersOfTable(mTableId, mCustId);
        mDbRepository.getOrederDetailsGroupByID(mList);
        mAdapter = new OrderSummaryAdapter(getApplicationContext(), mList);
        mOrdersList.setAdapter(mAdapter);
        mOrdersList.setDividerHeight(2);
        mOrdersList.setGroupIndicator(null);
        mOrdersList.setClickable(true);
        mAdapter.notifyDataSetChanged();
        mOrdersList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //parent.expandGroup(groupPosition);
                return false;
            }
        });
        mOrdersList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
        mOrdersList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {


            }
        });

        mOrdersList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                return false;
            }
        });
       /* mPrintBillDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // int mPermissionId = mDbRepository.getPermissionId(AppConstants.PERMISSION_PRINT_BILL);
               // if(getPermissionStatus(mPermissionId))
               // {
                    if(NetworkUtils.isActiveNetworkAvailable(getApplicationContext()))
                    {
                        ArrayList<String> getOderIdFor = mDbRepository.getOderIdForPrinting("3",mCustId) ;
                        Toast.makeText(getApplicationContext(),"Button is clicked",Toast.LENGTH_LONG);
                    }

               // }
              //  else {
               //     customAlterDialog(getResources().getString(R.string.dialog_access_denied), getResources().getString(R.string.access_denied_print_bill));
               // }

            }
        });*/
    }


    protected void OnBackPressed() {
        super.onBackPressed();
    }
}
