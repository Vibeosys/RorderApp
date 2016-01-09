package com.vibeosys.rorderapp.data;

import android.support.annotation.Nullable;

/**
 * Created by mahesh on 10/21/2015.
 */
public class TableDataDTO {
    private String tableName;
    private String tableData;
    private String operation;

    public TableDataDTO(String tableName, String tableData, @Nullable String operation) {
        this.tableName = tableName;
        this.tableData = tableData;
        this.operation = operation;
    }

    public TableDataDTO() {

    }


    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        tableName = tableName;
    }

    public String getTableData() {
        return tableData;
    }

    public void setTableData(String tableData) {
        tableData = tableData;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
