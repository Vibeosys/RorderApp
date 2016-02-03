package com.vibeosys.rorderapp.activities;

import android.os.Bundle;
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
public class TableFilterActivity  extends BaseActivity{

    TableCategoryAdapter tableCategoryAdapter;
    ArrayList<TableCategoryDTO> allTableCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_for_menu_screen);
        ListView filterTableList = (ListView) findViewById(R.id.filterMenuList);
        TextView myServingTxt = (TextView) findViewById(R.id.myServing);
        TextView unOccupy = (TextView) findViewById(R.id.unOccupy);
        CheckBox myServingChk = (CheckBox) findViewById(R.id.chkMyserving);
        CheckBox unOccupyChk = (CheckBox) findViewById(R.id.chkUnoccupy);
        allTableCategory = mDbRepository.getTableCategories();
        tableCategoryAdapter = new TableCategoryAdapter(allTableCategory,getApplicationContext());
        filterTableList.setAdapter(tableCategoryAdapter);
    }
}
