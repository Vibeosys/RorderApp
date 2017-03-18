package com.vibeosys.quickserve.data;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 08-02-2016.
 */
public class CustomerDbDTO extends BaseDTO {

    private String custId;
    private String custName;
    private String custAddress;
    private String custPhone;
   /* public CustomerDbDTO() {
    }*/

    public CustomerDbDTO(String custId, String custName) {
        this.custId = custId;
        this.custName = custName;
    }

    public CustomerDbDTO(String custId, String custName, String custAddress, String custPhone) {
        this.custId = custId;
        this.custName = custName;
        this.custAddress = custAddress;
        this.custPhone = custPhone;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public static List<CustomerDbDTO> deserializeCustomer(List<String> serializedStringList) {
        Gson gson = new Gson();
        ArrayList<CustomerDbDTO> objectList = new ArrayList<>();
        try {

            for (String serializedString : serializedStringList) {
                CustomerDbDTO deserializeObject = gson.fromJson(serializedString, CustomerDbDTO.class);
                objectList.add(deserializeObject);
            }
        } catch (Exception e) {
            Log.d("TAG", "##" + e.toString());
        }

        return objectList;
    }
}
