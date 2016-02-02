package com.vibeosys.rorderapp.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.vibeosys.rorderapp.R;

/**
 * Created by shrinivas on 02-02-2016.
 */
public class BillSummeryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.total_amount_payment);
       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
