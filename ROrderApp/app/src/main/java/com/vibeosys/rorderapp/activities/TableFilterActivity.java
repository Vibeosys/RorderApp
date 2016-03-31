package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.TableCategoryAdapter;
import com.vibeosys.rorderapp.data.TableCategoryDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by shrinivas on 03-02-2016.
 */
public class TableFilterActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String screenName = "Filter Tables";
    private TableCategoryAdapter mTableCategoryAdapter;
    private ArrayList<TableCategoryDTO> mAllTableCategory;
    private int mSelectedCategory;
    private boolean mBtnFlag = false, chkMyservingFlag = false, chkUnoccupied = false;

    @Override
    protected String getScreenName() {
        return screenName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_for_menu_screen);
        ListView filterTableList = (ListView) findViewById(R.id.filterMenuList);
        TextView myServingTxt = (TextView) findViewById(R.id.myServing);
        TextView unOccupy = (TextView) findViewById(R.id.unOccupy);
        CheckBox myServingChk = (CheckBox) findViewById(R.id.chkMyserving);
        CheckBox unOccupyChk = (CheckBox) findViewById(R.id.chkUnoccupy);
        String jsonString = getIntent().getStringExtra("json");
        Log.d(TAG, "##" + jsonString);
        setTitle("Filter Tables");

        TextView txtCancel = (TextView) findViewById(R.id.txtCancel);
        TextView txtApply = (TextView) findViewById(R.id.txtApply);

        mAllTableCategory = mDbRepository.getTableCategories();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTableCategoryAdapter = new TableCategoryAdapter(mAllTableCategory, getApplicationContext());
        filterTableList.setAdapter(mTableCategoryAdapter);
        if (jsonString == "null" || jsonString.isEmpty()) {

        } else {
            JSONObject json;
            try {
                json = new JSONObject(jsonString);
                mBtnFlag = json.getBoolean("btnFlag");
                chkMyservingFlag = json.getBoolean("chkMyservingFlag");
                chkUnoccupied = json.getBoolean("chkUnoccupied");
                mSelectedCategory = json.getInt("Category");
                changeUi();
            } catch (JSONException e) {
                addError(screenName, "Null Json", e.getMessage());
                e.printStackTrace();
            }
        }
        mTableCategoryAdapter.setItemChecked(mSelectedCategory);
        mTableCategoryAdapter.notifyDataSetChanged();
        myServingChk.setChecked(chkMyservingFlag);
        unOccupyChk.setChecked(chkUnoccupied);


        txtApply.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        unOccupyChk.setOnClickListener(this);
        filterTableList.setOnItemClickListener(this);
        myServingChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chkMyservingFlag = true;


                } else if (!isChecked) {
                    chkMyservingFlag = false;

                }
                sendEventToGoogle("Action", "Filter by my serving");

            }
        });
        unOccupyChk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chkUnoccupied = true;


                } else if (!isChecked) {
                    chkUnoccupied = false;

                }
                sendEventToGoogle("Action", "Filter Occupancy");
            }
        });

    }

    private void changeUi() {


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.txtApply) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Category", mSelectedCategory);
                jsonObject.put("chkMyservingFlag", chkMyservingFlag);
                jsonObject.put("chkUnoccupied", chkUnoccupied);
                jsonObject.put("btnFlag", true);
            } catch (JSONException e) {
                addError(screenName, "txtApply On Click", e.getMessage());
                e.printStackTrace();
            }
            sendEventToGoogle("Action", "Table filter apply");
            Intent intent = new Intent();
            intent.putExtra("json", jsonObject.toString());//json data added here
            setResult(2, intent);
            finish();
        }
        if (id == R.id.txtCancel) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Category", -1);
                jsonObject.put("chkMyservingFlag", false);
                jsonObject.put("chkUnoccupied", false);
                jsonObject.put("btnFlag", false);
                Intent intent = new Intent();
                intent.putExtra("json", jsonObject.toString());//json data added here
                setResult(2, intent);
                finish();
            } catch (JSONException e) {
                addError(screenName, "txtCancel On Click", e.getMessage());
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TableCategoryDTO categoryDTO = (TableCategoryDTO) mTableCategoryAdapter.getItem(position);
        //categoryDTO.setSelected(!categoryDTO.isSelected());
        if (!categoryDTO.isSelected()) {
            mSelectedCategory = categoryDTO.getmCategoryId();
            mTableCategoryAdapter.setItemChecked(mSelectedCategory);
            mTableCategoryAdapter.notifyDataSetChanged();
        } else {
            mTableCategoryAdapter.setItemChecked(mSelectedCategory);
            mSelectedCategory = 0;
            mTableCategoryAdapter.notifyDataSetChanged();
        }
        Log.d(TAG, "## " + mSelectedCategory);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

   /* public int getPosition() {
        int position = 0;
        for (int i = 0; i < mAllTableCategory.size(); i++) {
            TableCategoryDTO table = mAllTableCategory.get(i);
            if (mSelectedCategory == table.getmCategoryId())
                position = i;
            break;
        }
        return position;
    }*/
}
