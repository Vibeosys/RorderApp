package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 08-02-2016.
 */
public class CustomerDbDTO extends BaseDTO {

    private String custId;
    private String custName;

    public CustomerDbDTO() {
    }

    public CustomerDbDTO(String custId, String custName) {
        this.custId = custId;
        this.custName = custName;
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
}
