package com.vibeosys.quickserve.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 23-01-2016.
 */
public class BillDetailsDbDTO extends BaseDTO {

    private int autoId;
    private String OrderId;
    private int billNo;

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


    public static List<BillDetailsDbDTO> deserializeBillDetails(List<String> serializedStringList) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
        ArrayList<BillDetailsDbDTO> objectList = new ArrayList<>();

        for (String serializedString : serializedStringList) {
            BillDetailsDbDTO deserializeObject = gson.fromJson(serializedString, BillDetailsDbDTO.class);
            objectList.add(deserializeObject);
        }
        return objectList;
    }
}
