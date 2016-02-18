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
        setTitle(getResources().getString(R.string.about_us));
        abtTxt.setText(Html.fromHtml(getResources().getString(R.string.about_us_content)));

    }
}
