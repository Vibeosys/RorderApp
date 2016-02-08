package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 08-02-2016.
 */
public class CustomerDbDTO extends BaseDTO {

    private String custId;
    private String custName;
    private String custPhone;
    private String custEmail;

    public CustomerDbDTO() {
    }

    public CustomerDbDTO(String custId, String custName, String custPhone, String custEmail) {
        this.custId = custId;
        this.custName = custName;
        this.custPhone = custPhone;
        this.custEmail = custEmail;
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

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }
}
