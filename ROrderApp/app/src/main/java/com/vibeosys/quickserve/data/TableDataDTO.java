package com.vibeosys.quickserve.data;

import android.support.annotation.Nullable;

/**
 * Created by mahesh on 10/21/2015.
 */
public class TableDataDTO {
    private String tableName;
    private String tableData;
    private String operation;
    private String operationData;

    public TableDataDTO(String tableName, String tableData, @Nullable String operation) {
        this.tableName = tableName;
        this.tableData = tableData;
        this.operation = operation;
    }

    public TableDataDTO(String operation, String operationData) {
        this.operation = operation;
        this.operationData = operationData;
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

    public String getOperationData() {
        return operationData;
    }

    public void setOperationData(String operationData) {
        this.operationData = operationData;
    }
}
