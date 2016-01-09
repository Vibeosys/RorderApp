package com.vibeosys.rorderapp.data;

import java.util.List;

/**
 * Created by mahesh on 10/20/2015.
 */
public class ServerSync extends BaseDTO {

    protected List<TableDataDTO> data;

    public List<TableDataDTO> getTableData() {
        return data;
    }

    public void setData(List<TableDataDTO> data) {
        this.data = data;
    }

}
