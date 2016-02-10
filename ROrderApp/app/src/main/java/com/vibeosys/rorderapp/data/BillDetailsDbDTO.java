package com.vibeosys.rorderapp.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 23-01-2016.
 */
public class BillDetailsDbDTO extends BaseDTO {

    private int autoId;
    private String OrderId;
    private int billNo;
    private Date CreateDate;
    private Date UpdatedDate;

    public BillDetailsDbDTO() {
    }

    public int getAutoId() {
        return autoId;
    }

    public void setAutoId(int autoId) {
        this.autoId = autoId;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public int getBillNo() {
        return billNo;
    }

    public void setBillNo(int billNo) {
        this.billNo = billNo;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public Date getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        UpdatedDate = updatedDate;
    }

    public static List<BillDetailsDbDTO> deserializeBillDetails(List<String> serializedStringList) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ssZ").create();
        ArrayList<BillDetailsDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            BillDetailsDbDTO deserializeObject = gson.fromJson(serializedString, BillDetailsDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
