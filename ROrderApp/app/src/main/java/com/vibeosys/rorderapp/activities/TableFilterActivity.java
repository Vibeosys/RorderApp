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
import android.widget.Toast;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.TableCategoryAdapter;
import com.vibeosys.rorderapp.data.TableCategoryDTO;
import com.vibeosys.rorderapp.data.TableCategoryDbDTO;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrinivas on 03-02-2016.
 */
public class TableFilterActivity  extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    TableCategoryAdapter tableCategoryAdapter;
    ArrayList<TableCategoryDTO> allTableCategory;
    int selectedCategory;
    boolean btnFlag =false , chkMyservingFlag =false ,chkUnoccupied =false ;

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


        TextView txtCancel=(TextView)findViewById(R.id.txtCancel);
        TextView txtApply=(TextView)findViewById(R.id.txtApply);

        allTableCategory = mDbRepository.getTableCategories();
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tableCategoryAdapter = new TableCategoryAdapter(allTableCategory,getApplicationContext());
        filterTableList.setAdapter(tableCategoryAdapter);
        if(jsonString=="null"||jsonString.isEmpty())
        {

        }
        else {
            JSONObject json;
            try {
                json =new JSONObject(jsonString);
                btnFlag=json.getBoolean("btnFlag");
                chkMyservingFlag=json.getBoolean("chkMyservingFlag");
                chkUnoccupied=json.getBoolean("chkUnoccupied");
                selectedCategory=json.getInt("Category");
                changeUi();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //filterTableList.setSelection(getPosition());
        if(chkMyservingFlag)
        {
            myServingChk.setSelected(true);
        }
        if(chkUnoccupied)
        {
            unOccupy.setSelected(true);
        }


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
            }
        });

    }

    private void changeUi() {


    }


    @Override
    public void onClick(View v) {
        int id=v.getId();

        if(id==R.id.txtApply)
        {
             JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Category", selectedCategory);
                jsonObject.put("chkMyservingFlag", chkMyservingFlag);
                jsonObject.put("chkUnoccupied",chkUnoccupied);
                jsonObject.put("btnFlag",true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Intent intent=new Intent();
          //  intent.putExtra("Category", selectedCategory);
            intent.putExtra("json",jsonObject.toString());//json data added here
            setResult(2,intent);
            finish();
        }
        if(id == R.id.txtCancel)
        {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("Category", -1);
                jsonObject.put("chkMyservingFlag", false);
                jsonObject.put("chkUnoccupied",false);
                jsonObject.put("btnFlag",false);
                Intent intent=new Intent();
                //  intent.putExtra("Category", selectedCategory);
                intent.putExtra("json", jsonObject.toString());//json data added here
                setResult(2, intent);
                finish();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TableCategoryDTO categoryDTO= (TableCategoryDTO) tableCategoryAdapter.getItem(position);
        selectedCategory=categoryDTO.getmCategoryId();
        Log.d(TAG,"## "+selectedCategory);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Category", -1);
            jsonObject.put("chkMyservingFlag", false);
            jsonObject.put("chkUnoccupied",false);
            jsonObject.put("btnFlag",false);
            Intent intent=new Intent();
            //  intent.putExtra("Category", selectedCategory);
            intent.putExtra("json", jsonObject.toString());//json data added here
            setResult(2, intent);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    }

    public int getPosition()
    {
        int position=0;
        for (int i=0;i<allTableCategory.size();i++)
        {
            TableCategoryDTO table=allTableCategory.get(i);
            if(selectedCategory==table.getmCategoryId())
                position= i;
            break;
        }
        return position;
    }
}
