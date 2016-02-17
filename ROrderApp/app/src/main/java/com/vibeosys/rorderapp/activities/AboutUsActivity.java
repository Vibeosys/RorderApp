package com.vibeosys.rorderapp.activities;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;

/**
 * Created by shrinivas on 17-02-2016.
 */
public class AboutUsActivity extends BaseActivity {
    TextView abtTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_information);
        TextView abtTxt = (TextView) findViewById(R.id.abtTxtView);
        abtTxt.setText(Html.fromHtml("<font>"+"QuickServe offers a User-friendly, Reliable and Agile restaurant ordering system especially designed for the Dine-in restaurants. This App is multi-faceted, effective ordering for Waiter and efficient order fulfilment for Chef. App comes with variety of features like Table occupancy chart, Menu filtering, Suggestive favourite menu, Bill generation, Updates for waiter and chef, Waiting list preparation, Instant updates for all.\n" +
                " \n" +
                "QuickServe is all-in-one app for all types of restaurants with easy-to-use functions which needs lesser training for the staff. Admin panel accompanying the App, provides vital strategic functions like Sales forecasting, Graphs and Charts for Billing, Food item ranking based on past orders. \n" +
                " \n" +
                "For more information or tie-ups, please visit our website www.vibeosys.com or send an inquiry to info@vibeosys.com\n" +
                "\n" +
                "Please add the text for ABOUT US link in our APP.\n" +"</font>"));

    }
}
