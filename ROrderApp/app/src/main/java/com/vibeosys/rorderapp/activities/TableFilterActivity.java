package com.vibeosys.rorderapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.vibeosys.rorderapp.R;
import com.vibeosys.rorderapp.adaptors.TableCategoryAdapter;
import com.vibeosys.rorderapp.data.TableCategoryDTO;
import com.vibeosys.rorderapp.data.TableCategoryDbDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shrinivas on 03-02-2016.
 */
public class TableFilterActivity  extends BaseActivity implements View.OnClickListener,AdapterView.OnItemClickListener{

    TableCategoryAdapter tableCategoryAdapter;
    ArrayList<TableCategoryDTO> allTableCategory;
    int selectedCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_for_menu_screen);
        ListView filterTableList = (ListView) findViewById(R.id.filterMenuList);
        TextView myServingTxt = (TextView) findViewById(R.id.myServing);
        TextView unOccupy = (TextView) findViewById(R.id.unOccupy);
        CheckBox myServingChk = (CheckBox) findViewById(R.id.chkMyserving);
        CheckBox unOccupyChk = (CheckBox) findViewById(R.id.chkUnoccupy);
        TextView txtCancel=(TextView)findViewById(R.id.txtCancel);
        TextView txtApply=(TextView)findViewById(R.id.txtApply);
        allTableCategory = mDbRepository.getTableCategories();
        tableCategoryAdapter = new TableCategoryAdapter(allTableCategory,getApplicationContext());
        filterTableList.setAdapter(tableCategoryAdapter);
        txtApply.setOnClickListener(this);
        txtCancel.setOnClickListener(this);
        filterTableList.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();

        if(id==R.id.txtApply)
        {
            Intent intent=new Intent();
            intent.putExtra("Category", selectedCategory);
            setResult(2,intent);
            finish();
        }
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TableCategoryDTO categoryDTO= (TableCategoryDTO) tableCategoryAdapter.getItem(position);
        selectedCategory=categoryDTO.getmCategoryId();
        Log.d(TAG,"## "+selectedCategory);
    }
}
