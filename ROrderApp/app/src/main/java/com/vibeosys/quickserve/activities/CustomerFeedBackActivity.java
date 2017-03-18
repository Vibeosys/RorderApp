package com.vibeosys.quickserve.activities;

import android.os.Bundle;

import com.vibeosys.quickserve.R;

/**
 * Created by shrinivas on 09-02-2016.
 */
public class CustomerFeedBackActivity extends BaseActivity {

    @Override
    protected String getScreenName() {
        return "Customer Feedback";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_customer_feedback);

    }

}
