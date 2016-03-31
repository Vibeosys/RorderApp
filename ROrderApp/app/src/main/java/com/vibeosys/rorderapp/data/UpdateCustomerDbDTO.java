package com.vibeosys.rorderapp.data;

/**
 * Created by akshay on 31-03-2016.
 */
public class UpdateCustomerDbDTO {
    private String custId;
    private String custEmail;
    private String custPhone;

    public UpdateCustomerDbDTO(String custId, String custEmail, String custPhone) {
        this.custId = custId;
        this.custEmail = custEmail;
        this.custPhone = custPhone;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }
}
